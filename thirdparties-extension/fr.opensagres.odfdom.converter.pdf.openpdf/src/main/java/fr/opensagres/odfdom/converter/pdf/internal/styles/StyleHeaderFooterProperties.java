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

public class StyleHeaderFooterProperties
{
    private Float margin;

    private Float marginBottom;

    private Float marginLeft;

    private Float marginRight;

    private Float marginTop;

    private Float minHeight;

    private Float height;

    public Float getHeight()
    {
    	return height;
    }

    public void setHeight( Float height )
    {
    	this.height = height;
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

    public Float getMinHeight()
    {
        return minHeight;
    }

    public void setMinHeight( Float minHeight )
    {
        this.minHeight = minHeight;
    }
}
