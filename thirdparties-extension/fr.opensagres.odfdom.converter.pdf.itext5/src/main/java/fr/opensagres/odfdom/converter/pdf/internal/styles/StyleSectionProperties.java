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

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StyleSectionProperties
{
    private BaseColor backgroundColor;

    private Integer columnCount;

    private Float columnGap;

    private Boolean dontBalanceTextColumns;

    private Float marginLeft;

    private Float marginRight;

    public StyleSectionProperties()
    {
    }

    public BaseColor getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor( BaseColor backgroundColor )
    {
        this.backgroundColor = backgroundColor;
    }

    public Integer getColumnCount()
    {
        return columnCount;
    }

    public void setColumnCount( Integer columnCount )
    {
        this.columnCount = columnCount;
    }

    public Float getColumnGap()
    {
        return columnGap;
    }

    public void setColumnGap( Float columnGap )
    {
        this.columnGap = columnGap;
    }

    public Boolean getDontBalanceTextColumns()
    {
        return dontBalanceTextColumns;
    }

    public void setDontBalanceTextColumns( Boolean dontBalanceTextColumns )
    {
        this.dontBalanceTextColumns = dontBalanceTextColumns;
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
}
