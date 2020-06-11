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

import java.awt.Color;

import com.lowagie.text.Element;

public class StyleTableCellProperties
{
    private Color backgroundColor;

    private StyleBorder border;

    private StyleBorder borderBottom;

    private StyleBorder borderLeft;

    private StyleBorder borderRight;

    private StyleBorder borderTop;

    private Float padding;

    private Float paddingBottom;

    private Float paddingRight;

    private Float paddingLeft;

    private Float paddingTop;

    private int verticalAlignment = Element.ALIGN_UNDEFINED;

    public StyleTableCellProperties()
    {
    }

    public StyleTableCellProperties( StyleTableCellProperties tableCellProperties )
    {
        if ( tableCellProperties != null )
        {
            merge( tableCellProperties );
        }
    }

    public void merge( StyleTableCellProperties tableCellProperties )
    {
        if ( tableCellProperties.getBackgroundColor() != null )
        {
            backgroundColor = tableCellProperties.getBackgroundColor();
        }
        if ( tableCellProperties.getBorder() != null )
        {
            border = tableCellProperties.getBorder();
        }
        if ( tableCellProperties.getBorderBottom() != null )
        {
            borderBottom = tableCellProperties.getBorderBottom();
        }
        if ( tableCellProperties.getBorderLeft() != null )
        {
            borderLeft = tableCellProperties.getBorderLeft();
        }
        if ( tableCellProperties.getBorderRight() != null )
        {
            borderRight = tableCellProperties.getBorderRight();
        }
        if ( tableCellProperties.getBorderTop() != null )
        {
            borderTop = tableCellProperties.getBorderTop();
        }
        if ( tableCellProperties.getPadding() != null )
        {
            padding = tableCellProperties.getPadding();
        }
        if ( tableCellProperties.getPaddingBottom() != null )
        {
            paddingBottom = tableCellProperties.getPaddingBottom();
        }
        if ( tableCellProperties.getPaddingLeft() != null )
        {
            paddingLeft = tableCellProperties.getPaddingLeft();
        }
        if ( tableCellProperties.getPaddingRight() != null )
        {
            paddingRight = tableCellProperties.getPaddingRight();
        }
        if ( tableCellProperties.getPaddingTop() != null )
        {
            paddingTop = tableCellProperties.getPaddingTop();
        }
        if ( tableCellProperties.getVerticalAlignment() != Element.ALIGN_UNDEFINED )
        {
            verticalAlignment = tableCellProperties.getVerticalAlignment();
        }
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor( Color backgroundColor )
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

    public Float getPaddingRight()
    {
        return paddingRight;
    }

    public void setPaddingRight( Float paddingRight )
    {
        this.paddingRight = paddingRight;
    }

    public Float getPaddingLeft()
    {
        return paddingLeft;
    }

    public void setPaddingLeft( Float paddingLeft )
    {
        this.paddingLeft = paddingLeft;
    }

    public Float getPaddingTop()
    {
        return paddingTop;
    }

    public void setPaddingTop( Float paddingTop )
    {
        this.paddingTop = paddingTop;
    }

    public int getVerticalAlignment()
    {
        return verticalAlignment;
    }

    public void setVerticalAlignment( int verticalAlignment )
    {
        this.verticalAlignment = verticalAlignment;
    }
}
