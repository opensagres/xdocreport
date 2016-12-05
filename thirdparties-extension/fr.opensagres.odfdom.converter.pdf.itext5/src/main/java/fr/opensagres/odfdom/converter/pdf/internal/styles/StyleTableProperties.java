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

public class StyleTableProperties
{
    private int alignment = Element.ALIGN_UNDEFINED;

    private BaseColor backgroundColor;

    private Float margin;

    private Float marginBottom;

    private Float marginLeft;

    private Float marginRight;

    private Float marginTop;

    private Boolean mayBreakBetweenRows;

    private Float width;

    public StyleTableProperties()
    {
    }

    public StyleTableProperties( StyleTableProperties tableProperties )
    {
        if ( tableProperties != null )
        {
            merge( tableProperties );
        }
    }

    public void merge( StyleTableProperties tableProperties )
    {
        if ( tableProperties.getAlignment() != Element.ALIGN_UNDEFINED )
        {
            alignment = tableProperties.getAlignment();
        }
        if ( tableProperties.getBackgroundColor() != null )
        {
            backgroundColor = tableProperties.getBackgroundColor();
        }
        if ( tableProperties.getMargin() != null )
        {
            margin = tableProperties.getMargin();
        }
        if ( tableProperties.getMarginBottom() != null )
        {
            marginBottom = tableProperties.getMarginBottom();
        }
        if ( tableProperties.getMarginLeft() != null )
        {
            marginLeft = tableProperties.getMarginLeft();
        }
        if ( tableProperties.getMarginRight() != null )
        {
            marginRight = tableProperties.getMarginRight();
        }
        if ( tableProperties.getMarginTop() != null )
        {
            marginTop = tableProperties.getMarginTop();
        }
        if ( tableProperties.getMayBreakBetweenRows() != null )
        {
            mayBreakBetweenRows = tableProperties.getMayBreakBetweenRows();
        }
        if ( tableProperties.getWidth() != null )
        {
            width = tableProperties.getWidth();
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

    public BaseColor getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor( BaseColor backgroundColor )
    {
        this.backgroundColor = backgroundColor;
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

    public Boolean getMayBreakBetweenRows()
    {
        return mayBreakBetweenRows;
    }

    public void setMayBreakBetweenRows( Boolean mayBreakBetweenRows )
    {
        this.mayBreakBetweenRows = mayBreakBetweenRows;
    }

    public Float getWidth()
    {
        return width;
    }

    public void setWidth( Float width )
    {
        this.width = width;
    }
}
