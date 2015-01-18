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
package fr.opensagres.xdocreport.xhtml.extension;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class XHTMLPage
{

    private final XHTMLPageBeforeBody pageBeforeBody;

    private final XHTMLPageBodyContentHeader pageBodyContentHeader;

    private final XHTMLPageBodyContentBody pageBodyContentBody;

    private final XHTMLPageBodyContentFooter pageBodyContentFooter;

    private final XHTMLPageAfterBody pageAfterBody;

    public XHTMLPage( int indent )
    {
        this.pageBeforeBody = new XHTMLPageBeforeBody( this, indent );
        this.pageBodyContentHeader = new XHTMLPageBodyContentHeader( indent );
        this.pageBodyContentBody = new XHTMLPageBodyContentBody( indent );
        this.pageBodyContentFooter = new XHTMLPageBodyContentFooter( indent );
        this.pageAfterBody = new XHTMLPageAfterBody( indent );
    }

    public void save( Writer writer )
        throws IOException
    {

        getPageBeforeBody().save( writer );
        getPageBodyContentHeader().save( writer );
        getPageBodyContentBody().save( writer );
        getPageBodyContentFooter().save( writer );
        getPageAfterBody().save( writer );

    }

    public void save( OutputStream out )
        throws IOException
    {
        // // 1) Write XHTML before HTML Head

        getPageBeforeBody().save( out );
        getPageBodyContentHeader().save( out );
        getPageBodyContentBody().save( out );
        getPageBodyContentFooter().save( out );
        getPageAfterBody().save( out );

        // // 1) Write XHTML before HTML Head
        // out.write(xhtmlBeforeHTMLHead.toString().getBytes());
        // // 2) Write CSS Styles declaration
        // CSSStyleSheet styleSheet = styleEngine.getCSSStyleSheet();
        // if (!styleSheet.isEmpty()) {
        // out.write(startElement(STYLE_ELEMENT, true, new StringBuilder(), 1)
        // .toString().getBytes());
        // styleSheet.save(out);
        // out.write(endElement(STYLE_ELEMENT, new StringBuilder(), 1)
        // .toString().getBytes());
        // }
        // // 3) Write XHTML after HTML Head
        // out.write(xhtmlAfterHTMLHead.toString().getBytes());
    }

    public XHTMLPageBeforeBody getPageBeforeBody()
    {
        return pageBeforeBody;
    }

    public XHTMLPageBodyContentHeader getPageBodyContentHeader()
    {
        return pageBodyContentHeader;
    }

    public XHTMLPageBodyContentBody getPageBodyContentBody()
    {
        return pageBodyContentBody;
    }

    public XHTMLPageBodyContentFooter getPageBodyContentFooter()
    {
        return pageBodyContentFooter;
    }

    public XHTMLPageAfterBody getPageAfterBody()
    {
        return pageAfterBody;
    }

    public CSSStyleSheet getCSSStyleSheet()
    {
        return null;
    }

}
