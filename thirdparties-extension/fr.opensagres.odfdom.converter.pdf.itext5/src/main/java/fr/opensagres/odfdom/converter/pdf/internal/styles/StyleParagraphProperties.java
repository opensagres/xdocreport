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
package fr.opensagres.odfdom.converter.pdf.internal.styles;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;

/**
 * fixes for paragraph pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StyleParagraphProperties
{
    private int alignment = Element.ALIGN_UNDEFINED;

    private Boolean autoTextIndent;

    private BaseColor backgroundColor;

    private StyleBorder border;

    private StyleBorder borderBottom;

    private StyleBorder borderLeft;

    private StyleBorder borderRight;

    private StyleBorder borderTop;

    private StyleBreak breakBefore;

    private Boolean joinBorder = Boolean.TRUE;

    private Boolean keepTogether;

    private StyleLineHeight lineHeight;

    private Float margin;

    private Float marginBottom;

    private Float marginLeft;

    private Float marginRight;

    private Float marginTop;

    private Float padding;

    private Float paddingBottom;

    private Float paddingLeft;

    private Float paddingRight;

    private Float paddingTop;

    private Float textIndent;

    public StyleParagraphProperties()
    {
    }

    public StyleParagraphProperties( StyleParagraphProperties paragraphProperties )
    {
        if ( paragraphProperties != null )
        {
            merge( paragraphProperties );
        }
    }

    public void merge( StyleParagraphProperties paragraphProperties )
    {
        if ( paragraphProperties.getAlignment() != Element.ALIGN_UNDEFINED )
        {
            alignment = paragraphProperties.getAlignment();
        }
        if ( paragraphProperties.getAutoTextIndent() != null )
        {
            autoTextIndent = paragraphProperties.getAutoTextIndent();
        }
        if ( paragraphProperties.getBackgroundColor() != null )
        {
            backgroundColor = paragraphProperties.getBackgroundColor();
        }
        if ( paragraphProperties.getBorder() != null )
        {
            border = paragraphProperties.getBorder();
        }
        if ( paragraphProperties.getBorderBottom() != null )
        {
            borderBottom = paragraphProperties.getBorderBottom();
        }
        if ( paragraphProperties.getBorderLeft() != null )
        {
            borderLeft = paragraphProperties.getBorderLeft();
        }
        if ( paragraphProperties.getBorderRight() != null )
        {
            borderRight = paragraphProperties.getBorderRight();
        }
        if ( paragraphProperties.getBorderTop() != null )
        {
            borderTop = paragraphProperties.getBorderTop();
        }
        if ( paragraphProperties.getBreakBefore() != null )
        {
            breakBefore = paragraphProperties.getBreakBefore();
        }
        if ( paragraphProperties.getJoinBorder() != null )
        {
            joinBorder = paragraphProperties.getJoinBorder();
        }
        if ( paragraphProperties.getKeepTogether() != null )
        {
            keepTogether = paragraphProperties.getKeepTogether();
        }
        if ( paragraphProperties.getLineHeight() != null )
        {
            lineHeight = paragraphProperties.getLineHeight();
        }
        if ( paragraphProperties.getMargin() != null )
        {
            margin = paragraphProperties.getMargin();
        }
        if ( paragraphProperties.getMarginBottom() != null )
        {
            marginBottom = paragraphProperties.getMarginBottom();
        }
        if ( paragraphProperties.getMarginLeft() != null )
        {
            marginLeft = paragraphProperties.getMarginLeft();
        }
        if ( paragraphProperties.getMarginRight() != null )
        {
            marginRight = paragraphProperties.getMarginRight();
        }
        if ( paragraphProperties.getMarginTop() != null )
        {
            marginTop = paragraphProperties.getMarginTop();
        }
        if ( paragraphProperties.getPadding() != null )
        {
            padding = paragraphProperties.getPadding();
        }
        if ( paragraphProperties.getPaddingBottom() != null )
        {
            paddingBottom = paragraphProperties.getPaddingBottom();
        }
        if ( paragraphProperties.getPaddingLeft() != null )
        {
            paddingLeft = paragraphProperties.getPaddingLeft();
        }
        if ( paragraphProperties.getPaddingRight() != null )
        {
            paddingRight = paragraphProperties.getPaddingRight();
        }
        if ( paragraphProperties.getPaddingTop() != null )
        {
            paddingTop = paragraphProperties.getPaddingTop();
        }
        if ( paragraphProperties.getTextIndent() != null )
        {
            textIndent = paragraphProperties.getTextIndent();
        }
    }

    public int getAlignment()
    {
        return alignment;
    }

    public void setAlignment( int alignment )
    {
        this.alignment = alignment;
    }

    public Boolean getAutoTextIndent()
    {
        return autoTextIndent;
    }

    public void setAutoTextIndent( Boolean autoTextIndent )
    {
        this.autoTextIndent = autoTextIndent;
    }

    public BaseColor getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor( BaseColor backgroundColor )
    {
        this.backgroundColor = backgroundColor;
    }

    public StyleBorder getBorder()
    {
        return border;
    }

    public void setBorder( StyleBorder border )
    {
        this.border = border;
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

    public StyleBorder getBorderTop()
    {
        return borderTop;
    }

    public void setBorderTop( StyleBorder borderTop )
    {
        this.borderTop = borderTop;
    }

    public StyleBreak getBreakBefore()
    {
        return breakBefore;
    }

    public void setBreakBefore( StyleBreak breakBefore )
    {
        this.breakBefore = breakBefore;
    }

    public Boolean getJoinBorder()
    {
        return joinBorder;
    }

    public void setJoinBorder( Boolean joinBorder )
    {
        this.joinBorder = joinBorder;
    }

    public Boolean getKeepTogether()
    {
        return keepTogether;
    }

    public void setKeepTogether( Boolean keepTogether )
    {
        this.keepTogether = keepTogether;
    }

    public StyleLineHeight getLineHeight()
    {
        return lineHeight;
    }

    public void setLineHeight( StyleLineHeight lineHeight )
    {
        this.lineHeight = lineHeight;
    }

    public Float getMargin()
    {
        return margin;
    }

    public void setMargin( Float margin )
    {
        this.margin = margin;
    }

    public Float getMarginBottom()
    {
        return marginBottom;
    }

    public void setMarginBottom( Float marginBottom )
    {
        this.marginBottom = marginBottom;
    }

    public Float getMarginLeft()
    {
        return marginLeft;
    }

    public void setMarginLeft( Float marginLeft )
    {
        this.marginLeft = marginLeft;
    }

    public Float getMarginRight()
    {
        return marginRight;
    }

    public void setMarginRight( Float marginRight )
    {
        this.marginRight = marginRight;
    }

    public Float getMarginTop()
    {
        return marginTop;
    }

    public void setMarginTop( Float marginTop )
    {
        this.marginTop = marginTop;
    }

    public Float getPadding()
    {
        return padding;
    }

    public void setPadding( Float padding )
    {
        this.padding = padding;
    }

    public Float getPaddingBottom()
    {
        return paddingBottom;
    }

    public void setPaddingBottom( Float paddingBottom )
    {
        this.paddingBottom = paddingBottom;
    }

    public Float getPaddingLeft()
    {
        return paddingLeft;
    }

    public void setPaddingLeft( Float paddingLeft )
    {
        this.paddingLeft = paddingLeft;
    }

    public Float getPaddingRight()
    {
        return paddingRight;
    }

    public void setPaddingRight( Float paddingRight )
    {
        this.paddingRight = paddingRight;
    }

    public Float getPaddingTop()
    {
        return paddingTop;
    }

    public void setPaddingTop( Float paddingTop )
    {
        this.paddingTop = paddingTop;
    }

    public Float getTextIndent()
    {
        return textIndent;
    }

    public void setTextIndent( Float textIndent )
    {
        this.textIndent = textIndent;
    }
}
