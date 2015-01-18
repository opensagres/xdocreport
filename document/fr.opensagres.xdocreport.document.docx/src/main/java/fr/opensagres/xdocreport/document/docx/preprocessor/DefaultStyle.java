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
package fr.opensagres.xdocreport.document.docx.preprocessor;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.utils.StringUtils;

public class DefaultStyle
{

    private String hyperLinkStyleId;

    private Map<Integer, String> headersStyleId;

    private Integer abstractNumIdForOrdererList = null;

    private Integer abstractNumIdForUnordererList = null;

    private Integer maxNumId = null;

    public String getHyperLinkStyleId()
    {
        return hyperLinkStyleId;
    }

    public void setHyperLinkStyleId( String hyperLinkStyleId )
    {
        this.hyperLinkStyleId = hyperLinkStyleId;
    }

    public boolean hasHyperLinkStyleId()
    {
        return StringUtils.isNotEmpty( getHyperLinkStyleId() );
    }

    public void addHeaderStyle( int level, String styleId )
    {
        if ( headersStyleId == null )
        {
            headersStyleId = new HashMap<Integer, String>();
        }
        headersStyleId.put( level, styleId );
    }

    public boolean hasHeaderStyle( int level )
    {
        return headersStyleId != null && headersStyleId.containsKey( level );
    }

    public String getHeaderStyle( int level )
    {
        return headersStyleId.get( level );
    }

    public Integer getAbstractNumIdForOrdererList()
    {
        return abstractNumIdForOrdererList;
    }

    public void setAbstractNumIdForOrdererList( Integer abstractNumIdForOrdererList )
    {
        this.abstractNumIdForOrdererList = abstractNumIdForOrdererList;
    }

    public Integer getAbstractNumIdForUnordererList()
    {
        return abstractNumIdForUnordererList;
    }

    public void setAbstractNumIdForUnordererList( Integer abstractNumIdForUnordererList )
    {
        this.abstractNumIdForUnordererList = abstractNumIdForUnordererList;
    }

    public void setMaxNumId( Integer maxNumId )
    {
        this.maxNumId = maxNumId;
    }

    public Integer getMaxNumId()
    {
        return maxNumId;
    }

}
