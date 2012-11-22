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
package org.apache.poi.xwpf.converter.pdf.internal.elements;

import java.awt.Color;
import java.math.BigInteger;

import org.apache.poi.xwpf.converter.core.utils.ColorHelper;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;

import fr.opensagres.xdocreport.itext.extension.ExtendedParagraph;
import fr.opensagres.xdocreport.itext.extension.IITextContainer;

public class StylableParagraph
    extends ExtendedParagraph
    implements IITextContainer
{

    private static final long serialVersionUID = 664309269352903329L;

    private static final float DEFAULT_LINE_HEIGHT = 1.0f;

    private final StylableDocument ownerDocument;

    private IITextContainer parent;

    private String listItemText;

    public StylableParagraph( StylableDocument ownerDocument, IITextContainer parent )
    {
        super();
        this.ownerDocument = ownerDocument;
        this.parent = parent;
        super.setMultipliedLeading( DEFAULT_LINE_HEIGHT );
    }

    public StylableParagraph( StylableDocument ownerDocument, Paragraph title, IITextContainer parent )
    {
        super( title );
        this.ownerDocument = ownerDocument;
        this.parent = parent;
    }

    // public void applyStyles( XWPFParagraph p, Style style )
    // {
    //
    // if ( style != null )
    // {
    // // first process values from "style"
    // // will be overridden by in-line values if available...
    // StyleParagraphProperties paragraphProperties = style.getParagraphProperties();
    //
    // if ( paragraphProperties != null )
    // {
    // FontInfos fontInfos = paragraphProperties.getFontInfos();
    // if ( fontInfos != null )
    // {
    // Font font =
    // XWPFFontRegistry.getRegistry().getFont( fontInfos.getFontFamily(), fontInfos.getFontEncoding(),
    // fontInfos.getFontSize(), fontInfos.getFontStyle(),
    // fontInfos.getFontColor() );
    // setFont( font );
    // }
    // // Alignment
    // int alignment = paragraphProperties.getAlignment();
    // if ( alignment != Element.ALIGN_UNDEFINED )
    // {
    // setAlignment( alignment );
    // }
    //
    // Float lineHeight = paragraphProperties.getLineHeight();
    // if ( lineHeight != null )
    // {
    // // super.getPdfPCell().setMinimumHeight(lineHeight);
    // // FIXME : Is it correct???
    // setLeading( lineHeight * super.getTotalLeading() );
    // }
    // }
    //
    // }
    // }

    // FIXME check with Angelo the purpose of this method....
    public IITextContainer getParent()
    {
        return parent;
    }

    public StylableDocument getOwnerDocument()
    {
        return ownerDocument;
    }

    public void setBorder( CTBorder border, int borderSide )
    {
        if ( border == null )
        {
            return;
        }
        boolean noBorder = ( STBorder.NONE == border.getVal() || STBorder.NIL == border.getVal() );

        // No border
        if ( noBorder )
        {
            return;
        }
        else
        {
            // border size
            float size = -1;
            BigInteger borderSize = border.getSz();
            if ( borderSize != null )
            {
                // http://officeopenxml.com/WPtableBorders.php
                // if w:sz="4" => 1/4 points
                size = borderSize.floatValue() / 8f;
            }
            // border color
            Color borderColor = ColorHelper.getBorderColor( border );

            // border padding
            Float space = null;
            BigInteger borderSpace = border.getSpace();
            if ( borderSpace != null )
            {
                // Specifies the spacing offset. Values are specified in points (1/72nd of an inch).
                space = borderSpace.floatValue();
            }

            switch ( borderSide )
            {
                case Rectangle.TOP:
                    if ( size != -1 )
                    {
                        this.setBorderWidthTop( size );
                    }
                    if ( borderColor != null )
                    {

                        super.setBorderColorTop( borderColor );
                    }
                    if ( space != null )
                    {
                        super.setBorderPaddingTop( space );
                    }
                    break;
                case Rectangle.BOTTOM:
                    if ( size != -1 )
                    {
                        this.setBorderWidthBottom( size );
                    }
                    if ( borderColor != null )
                    {
                        super.setBorderColorBottom( borderColor );
                    }
                    if ( space != null )
                    {
                        super.setBorderPaddingBottom( space );
                    }
                    break;
                case Rectangle.LEFT:
                    if ( size != -1 )
                    {
                        this.setBorderWidthLeft( size );
                    }
                    if ( borderColor != null )
                    {
                        super.setBorderColorLeft( borderColor );
                    }
                    if ( space != null )
                    {
                        super.setBorderPaddingLeft( space );
                    }
                    break;
                case Rectangle.RIGHT:
                    if ( size != -1 )
                    {
                        this.setBorderWidthRight( size );
                    }
                    if ( borderColor != null )
                    {
                        super.setBorderColorRight( borderColor );
                    }
                    if ( space != null )
                    {
                        super.setBorderPaddingRight( space );
                    }
                    break;
            }
        }
    }

    public String getListItemText()
    {
        return listItemText;
    }

    public void setListItemText( String listItemText )
    {
        this.listItemText = listItemText;
    }
}
