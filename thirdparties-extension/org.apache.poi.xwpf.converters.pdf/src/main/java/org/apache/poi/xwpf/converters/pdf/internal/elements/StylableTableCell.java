/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.apache.poi.xwpf.converters.pdf.internal.elements;

import java.awt.Color;
import java.math.BigInteger;

import org.apache.poi.xwpf.converters.core.utils.ColorHelper;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;

import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPCell;
import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPTable;
import fr.opensagres.xdocreport.itext.extension.IITextContainer;

public class StylableTableCell
    extends ExtendedPdfPCell
    implements IITextContainer
{

    private final StylableDocument ownerDocument;

    private final IITextContainer parent;

    public StylableTableCell( StylableDocument ownerDocument, IITextContainer parent )
    {
        this.ownerDocument = ownerDocument;
        this.parent = parent;
        // make iText first line alignment compatible with open office
        getColumn().setAdjustFirstLine( false );
        // make a room for borders
        setUseBorderPadding( true );
    }

    public StylableTableCell( StylableDocument ownerDocument, IITextContainer parent, ExtendedPdfPTable table )
    {
        super( table );
        this.ownerDocument = ownerDocument;
        this.parent = parent;
        // make iText first line alignment compatible with open office
        getColumn().setAdjustFirstLine( false );
        // make a room for borders
        setUseBorderPadding( true );
    }

    public void addElement( Element element )
    {
        super.addElement( element );
    }

    public IITextContainer getParent()
    {
        return parent;
    }

    public Element getElement()
    {
        return this;
    }

    public void setBorder( CTTcBorders cellBorders, CTTblBorders tableBorders, CTTblBorders tableStyleBorders,
                           boolean firstRow, boolean lastRow, boolean firstCell, boolean lastCell, int borderSide )
    {
        if ( cellBorders == null && tableBorders == null && tableStyleBorders == null )
        {
            this.disableBorderSide( borderSide );
            return;
        }
        // border from cell
        CTBorder cellBorder = getBorder( cellBorders, borderSide );
        if ( cellBorder == null )
        {
            // border from table
            cellBorder = getBorder( tableBorders, firstRow, lastRow, firstCell, lastCell, borderSide );
        }
        if ( cellBorder == null )
        {
            // border from table style
            cellBorder = getBorder( tableStyleBorders, firstRow, lastRow, firstCell, lastCell, borderSide );
        }
        if ( cellBorder == null )
        {
            this.disableBorderSide( borderSide );
            return;
        }
        setBorder( cellBorder, borderSide );
    }

    public static CTBorder getBorder( CTTcBorders cellBorders, int borderSide )
    {
        if ( cellBorders == null )
        {
            return null;
        }
        switch ( borderSide )
        {
            case Rectangle.TOP:

                return cellBorders.getTop();
            case Rectangle.BOTTOM:
                return cellBorders.getBottom();
            case Rectangle.LEFT:
                return cellBorders.getLeft();
            case Rectangle.RIGHT:
                return cellBorders.getRight();
        }
        return null;
    }

    public static CTBorder getBorder( CTTblBorders tableBorders, boolean firstRow, boolean lastRow, boolean firstCell,
                                      boolean lastCell, int borderSide )
    {
        if ( tableBorders == null )
        {
            return null;
        }
        switch ( borderSide )
        {
            case Rectangle.TOP:
                if ( firstRow )
                {
                    return tableBorders.getTop();
                }
                return tableBorders.getInsideH();
            case Rectangle.BOTTOM:
                if ( lastRow )
                {
                    return tableBorders.getBottom();
                }
                return tableBorders.getInsideH();
            case Rectangle.LEFT:
                if ( firstCell )
                {
                    return tableBorders.getLeft();
                }
                return tableBorders.getInsideV();
            case Rectangle.RIGHT:
                if ( lastCell )
                {
                    return tableBorders.getRight();
                }
                return tableBorders.getInsideV();
        }
        return null;
    }

    public void setBorder( CTBorder border, int borderSide )
    {
        if ( border == null )
        {
            this.disableBorderSide( borderSide );
            return;
        }
        boolean noBorder = ( STBorder.NONE == border.getVal() || STBorder.NIL == border.getVal() );

        // No border
        if ( noBorder )
        {
            this.disableBorderSide( borderSide );
        }
        else
        {
            float size = -1;
            BigInteger borderSize = border.getSz();
            if ( borderSize != null )
            {
                // http://officeopenxml.com/WPtableBorders.php
                // if w:sz="4" => 1/4 points
                size = borderSize.floatValue() / 8f;
            }

            Color borderColor = ColorHelper.getBorderColor( border );

            switch ( borderSide )
            {
                case Rectangle.TOP:
                    if ( size != -1 )
                    {
                        this.setBorderWidthTop( size );
                    }
                    if ( borderColor != null )
                    {

                        this.setBorderColorTop( borderColor );
                    }
                    break;
                case Rectangle.BOTTOM:
                    if ( size != -1 )
                    {
                        this.setBorderWidthBottom( size );
                    }
                    if ( borderColor != null )
                    {
                        this.setBorderColorBottom( borderColor );
                    }
                    break;
                case Rectangle.LEFT:
                    if ( size != -1 )
                    {
                        this.setBorderWidthLeft( size );
                    }
                    if ( borderColor != null )
                    {
                        this.setBorderColorLeft( borderColor );
                    }
                    break;
                case Rectangle.RIGHT:
                    if ( size != -1 )
                    {
                        this.setBorderWidthRight( size );
                    }
                    if ( borderColor != null )
                    {
                        this.setBorderColorRight( borderColor );
                    }
                    break;
            }
        }
    }

}
