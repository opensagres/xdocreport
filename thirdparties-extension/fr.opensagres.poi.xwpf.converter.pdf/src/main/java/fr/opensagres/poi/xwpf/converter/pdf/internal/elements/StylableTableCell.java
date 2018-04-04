/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
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
package fr.opensagres.poi.xwpf.converter.pdf.internal.elements;

import java.awt.Color;

import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;

import fr.opensagres.poi.xwpf.converter.core.TableCellBorder;
import fr.opensagres.poi.xwpf.converter.pdf.internal.Converter;
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
        setPadding( 0 );
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

    @Override
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

    public void setBorderTop( TableCellBorder borderTop, boolean inside )
    {
        setBorder( borderTop, inside, Rectangle.TOP );
    }

    public void setBorderBottom( TableCellBorder borderBottom, boolean inside )
    {
        setBorder( borderBottom, inside, Rectangle.BOTTOM );
    }

    public void setBorderLeft( TableCellBorder borderLeft, boolean inside )
    {
        setBorder( borderLeft, inside, Rectangle.LEFT );
    }

    public void setBorderRight( TableCellBorder borderRight, boolean inside )
    {
        setBorder( borderRight, inside, Rectangle.RIGHT );
    }

    public void setBorder( TableCellBorder border, boolean inside, int borderSide )
    {
        if ( border == null || !border.hasBorder() )
        {
            this.disableBorderSide( borderSide );
            return;
        }
        Float borderSize = border.getBorderSize();

        if ( inside )
        {
            // manage conflict border

            // divide the border side by 2 to avoid multiply with 2 the border
            // this code simplify the "Conflicts between adjacent cells"
            // http://officeopenxml.com/WPtableCellBorderConflicts.php
            borderSize = borderSize / 2;
        }

        Color borderColor = Converter.toAwtColor( border.getBorderColor() );
        switch ( borderSide )
        {
            case Rectangle.TOP:
                if ( borderSize != null )
                {
                    this.setBorderWidthTop( borderSize );
                }
                if ( borderColor != null )
                {

                    this.setBorderColorTop( borderColor );
                }
                break;
            case Rectangle.BOTTOM:
                if ( borderSize != null )
                {
                    this.setBorderWidthBottom( borderSize );
                }
                if ( borderColor != null )
                {
                    this.setBorderColorBottom( borderColor );
                }
                break;
            case Rectangle.LEFT:
                if ( borderSize != null )
                {
                    this.setBorderWidthLeft( borderSize );
                }
                if ( borderColor != null )
                {
                    this.setBorderColorLeft( borderColor );
                }
                break;
            case Rectangle.RIGHT:
                if ( borderSize != null )
                {
                    this.setBorderWidthRight( borderSize );
                }
                if ( borderColor != null )
                {
                    this.setBorderColorRight( borderColor );
                }
                break;
        }
    }

}
