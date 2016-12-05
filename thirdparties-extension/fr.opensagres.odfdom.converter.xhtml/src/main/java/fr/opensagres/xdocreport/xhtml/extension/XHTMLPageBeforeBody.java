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

import fr.opensagres.odfdom.converter.core.utils.StringUtils;

public class XHTMLPageBeforeBody
    extends XHTMLPageContentBuffer
{

    private final XHTMLPage page;

    private String bodyClass = null;

    public XHTMLPageBeforeBody( XHTMLPage page, int indent )
    {
        super( indent );
        this.page = page;
        super.setText( XHTML_1_0_DOCTYPE );
        super.startElement( HTML_ELEMENT );
        super.startElement( HEAD_ELEMENT );
        super.setText( "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" );
    }

    @Override
    public void save( Writer out )
        throws IOException
    {
        super.save( out );
        CSSStyleSheet styleSheet = page.getCSSStyleSheet();
        if ( styleSheet != null && !styleSheet.isEmpty() )
        {
            startElement( STYLE_ELEMENT, true, out, indent + 1 );
            styleSheet.save( out );
            endElement( STYLE_ELEMENT, out, indent + 1 );
        }
        endElement( HEAD_ELEMENT, out, indent );
        super.startElement( BODY_ELEMENT, false, out, 1 );
        if ( StringUtils.isNotEmpty( bodyClass ) )
        {
            out.write( " " );
            out.write( CLASS_ATTR );
            out.write( "=\"" );
            out.write( bodyClass );
            out.write( "\"" );
        }
        out.write( ">" );
    }

    @Override
    public void save( OutputStream out )
        throws IOException
    {
        super.save( out );
        CSSStyleSheet styleSheet = page.getCSSStyleSheet();
        if ( styleSheet != null && !styleSheet.isEmpty() )
        {
            startElement( STYLE_ELEMENT, true, out, indent + 1 );
            styleSheet.save( out );
            endElement( STYLE_ELEMENT, out, indent + 1 );
        }
        endElement( HEAD_ELEMENT, out, 1 );
        super.startElement( BODY_ELEMENT, false, out, 1 );
        if ( StringUtils.isNotEmpty( bodyClass ) )
        {
            out.write( " ".getBytes() );
            out.write( CLASS_ATTR.getBytes() );
            out.write( "=\"".getBytes() );
            out.write( bodyClass.getBytes() );
            out.write( "\"".getBytes() );
        }
        out.write( ">".getBytes() );
    }

    public void setBodyClass( String bodyClass )
    {
        this.bodyClass = bodyClass;
    }

}
