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

public class StyleTableRowProperties
{
    private Boolean keepTogether;

    private Float minRowHeight;

    private Float rowHeight;

    public StyleTableRowProperties()
    {
    }

    public StyleTableRowProperties( StyleTableRowProperties tableRowProperties )
    {
        if ( tableRowProperties != null )
        {
            merge( tableRowProperties );
        }
    }

    public void merge( StyleTableRowProperties tableRowProperties )
    {
        if ( tableRowProperties.getKeepTogether() != null )
        {
            keepTogether = tableRowProperties.getKeepTogether();
        }
        if ( tableRowProperties.getMinRowHeight() != null )
        {
            minRowHeight = tableRowProperties.getMinRowHeight();
        }
        if ( tableRowProperties.getRowHeight() != null )
        {
            rowHeight = tableRowProperties.getRowHeight();
        }
    }

    public Boolean getKeepTogether()
    {
        return keepTogether;
    }

    public void setKeepTogether( Boolean keepTogether )
    {
        this.keepTogether = keepTogether;
    }

    public Float getMinRowHeight()
    {
        return minRowHeight;
    }

    public void setMinRowHeight( Float minRowHeight )
    {
        this.minRowHeight = minRowHeight;
    }

    public Float getRowHeight()
    {
        return rowHeight;
    }

    public void setRowHeight( Float rowHeight )
    {
        this.rowHeight = rowHeight;
    }
}
