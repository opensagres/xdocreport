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

import java.math.BigInteger;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;

import fr.opensagres.poi.xwpf.converter.core.utils.ColorHelper;
import fr.opensagres.poi.xwpf.converter.pdf.internal.Converter;
import fr.opensagres.xdocreport.itext.extension.ExtendedParagraph;
import fr.opensagres.xdocreport.itext.extension.IITextContainer;

import java.util.List;

public class StylableParagraph
    extends ExtendedParagraph
    implements IITextContainer
{

    private static final long serialVersionUID = 664309269352903329L;

    private final StylableDocument ownerDocument;

    private IITextContainer parent;

    private String listItemText;

    private Float originMultipliedLeading = 1f;

    private String listItemFontFamily;

    private Float listItemFontSize;

    private int listItemFontStyle = -1;

    private BaseColor listItemFontColor;

    public StylableParagraph( StylableDocument ownerDocument, IITextContainer parent )
    {
        super();
        this.ownerDocument = ownerDocument;
        this.parent = parent;
    }

    public StylableParagraph( StylableDocument ownerDocument, Paragraph title, IITextContainer parent )
    {
        super( title );
        this.ownerDocument = ownerDocument;
        this.parent = parent;
    }

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
            BaseColor borderColor = Converter.toBaseColor(ColorHelper.getBorderColor( border ));

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

    public String getListItemFontFamily()
    {
        return listItemFontFamily;
    }

    public void setListItemFontFamily( String listItemFontFamily )
    {
        this.listItemFontFamily = listItemFontFamily;
    }

    public Float getListItemFontSize()
    {
        return listItemFontSize;
    }

    public void setListItemFontSize( Float listItemFontSize )
    {
        this.listItemFontSize = listItemFontSize;
    }

    public int getListItemFontStyle()
    {
        return listItemFontStyle;
    }

    public void setListItemFontStyle( int listItemFontStyle )
    {
        this.listItemFontStyle = listItemFontStyle;
    }

    public BaseColor getListItemFontColor()
    {
        return listItemFontColor;
    }

    public void setListItemFontColor( BaseColor listItemFontColor )
    {
        this.listItemFontColor = listItemFontColor;
    }

    @Override
    public void setLeading( float fixedLeading, float multipliedLeading )
    {
        super.setLeading( fixedLeading, multipliedLeading );
        this.originMultipliedLeading = multipliedLeading;
    }

    @Override
    public void setMultipliedLeading( float multipliedLeading )
    {
        super.setMultipliedLeading( multipliedLeading );
        this.originMultipliedLeading = multipliedLeading;
    }

    /**
     * Adjust iText multiplied leading according the given font.
     * 
     * @param font
     */
    public void adjustMultipliedLeading( Font font )
    {
        if ( originMultipliedLeading != null && font != null && font.getBaseFont() != null )
        {
            // iText and open office computes proportional line height differently
            // [iText] line height = coefficient * font size
            // [MS Word] line height = coefficient * (font ascender + font descender + font extra margin)
            // we have to increase paragraph line height to generate pdf similar to OOXML docx document
            // this algorithm may be inaccurate if fonts with different multipliers are used in this paragraph
            float size = font.getSize();
            float ascender = font.getBaseFont().getFontDescriptor( BaseFont.AWT_ASCENT, size );
            float descender = -font.getBaseFont().getFontDescriptor( BaseFont.AWT_DESCENT, size ); // negative value
            float margin = font.getBaseFont().getFontDescriptor( BaseFont.AWT_LEADING, size );
            float multiplier = ( ascender + descender + margin ) / size;
            super.setMultipliedLeading( originMultipliedLeading * multiplier );
            // copied from ODF
            float itextdescender = -font.getBaseFont().getFontDescriptor( BaseFont.DESCENT, size ); // negative
            float textRise = itextdescender + getTotalLeading() - font.getSize() * multiplier;
            
            List<Chunk> chunks = getChunks();
            for ( Chunk chunk : chunks )
            {
                Font f = chunk.getFont();
                if ( f != null )
                {
                    // have to raise underline and strikethru as well
                    float s = f.getSize();
                    if ( f.isUnderlined() )
                    {
                        f.setStyle( f.getStyle() & ~Font.UNDERLINE );
                        chunk.setUnderline( s * 1 / 17, s * -1 / 7 + textRise );
                    }
                    if ( f.isStrikethru() )
                    {
                        f.setStyle( f.getStyle() & ~Font.STRIKETHRU );
                        chunk.setUnderline( s * 1 / 17, s * 1 / 4 + textRise );
                    }
                }
                chunk.setTextRise( chunk.getTextRise() + textRise );
            }
        }
    }

}
