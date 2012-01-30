/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package org.apache.poi.xwpf.converter.internal.itext.styles;

import static org.apache.poi.xwpf.converter.internal.DxaUtil.dxa2points;

import java.math.BigInteger;

import com.lowagie.text.Element;

public class StyleParagraphProperties
{

    // private StyleBorder border;
    private StyleBorder borderTop;

    private StyleBorder borderBottom;

    private StyleBorder borderLeft;

    private StyleBorder borderRight;

    // private BaseColor backgroundColor;

    private int alignment = Element.ALIGN_UNDEFINED;

    private int verticalAlignment = Element.ALIGN_UNDEFINED;

    // private Float indentation;
    private Float lineHeight;

    // private boolean autoTextIndent = false;
    //
    private int indentationFirstLine;

    private int indentationLeft;

    private int indentationRight;

    private int spacingAfter;

    private int spacingBefore;

    public StyleParagraphProperties()
    {

    }

    public StyleParagraphProperties( StyleParagraphProperties paragraphProperties )
    {
        if ( paragraphProperties == null )
        {
            return;
        }
        // backgroundColor = paragraphProperties.backgroundColor;
        // alignment = paragraphProperties.alignment;
        // indentation = paragraphProperties.indentation;
    }

    public int getSpacingAfter()
    {
        return spacingAfter;
    }

    public void setSpacingAfter( int spacingAfter )
    {
        this.spacingAfter = dxa2points( spacingAfter );
    }

    public float getSpacingBefore()
    {
        return spacingBefore;
    }

    public void setSpacingBefore( int spacingBefore )
    {
        this.spacingBefore = dxa2points( spacingBefore );
    }

    public int getAlignment()
    {
        return alignment;
    }

    public void setAlignment( int alignment )
    {
        this.alignment = alignment;
    }

    public Float getLineHeight()
    {
        return lineHeight;
    }

    public void setLineHeight( Float lineHeight )
    {
        this.lineHeight = lineHeight;
    }

    public int getIndentationFirstLine()
    {
        return indentationFirstLine;
    }

    public void setIndentationFirstLine( int indentationFirstLine )
    {
        this.indentationFirstLine = dxa2points( indentationFirstLine );
    }

    public int getIndentationLeft()
    {
        return indentationLeft;
    }

    public void setIndentationLeft( int indentationLeft )
    {
        this.indentationLeft = dxa2points( indentationLeft );
    }

    public void setIndentationRight( BigInteger indentationRight )
    {
        this.indentationRight = dxa2points( indentationRight );
    }

    public int getIndentationRight()
    {
        return indentationRight;
    }

    public void merge( StyleParagraphProperties paragraphProperties )
    {
        if ( paragraphProperties.getAlignment() != Element.ALIGN_UNDEFINED )
        {
            alignment = paragraphProperties.getAlignment();
        }
        if ( paragraphProperties.getLineHeight() != null )
        {
            lineHeight = paragraphProperties.getLineHeight();
        }
    }

    private FontInfos fontInfos;

    public FontInfos getFontInfos()
    {
        return fontInfos;
    }

    public void setFontInfos( FontInfos fontInfos )
    {
        this.fontInfos = fontInfos;
    }

    public int getVerticalAlignment()
    {
        return verticalAlignment;
    }

    public void setVerticalAlignment( int verticalAlignment )
    {
        this.verticalAlignment = verticalAlignment;
    }

    public StyleBorder getBorderTop()
    {
        return borderTop;
    }

    public void setBorderTop( StyleBorder borderTop )
    {
        this.borderTop = borderTop;
    }

    public StyleBorder getBorderBottom()
    {
        return borderBottom;
    }

    public void setBorderBottom( StyleBorder borderBottom )
    {
        this.borderBottom = borderBottom;
    }

    public StyleBorder getBorderLeft()
    {
        return borderLeft;
    }

    public void setBorderLeft( StyleBorder borderLeft )
    {
        this.borderLeft = borderLeft;
    }

    public StyleBorder getBorderRight()
    {
        return borderRight;
    }

    public void setBorderRight( StyleBorder borderRight )
    {
        this.borderRight = borderRight;
    }

    public void setIndentationRight( int indentationRight )
    {
        this.indentationRight = indentationRight;
    }

}
