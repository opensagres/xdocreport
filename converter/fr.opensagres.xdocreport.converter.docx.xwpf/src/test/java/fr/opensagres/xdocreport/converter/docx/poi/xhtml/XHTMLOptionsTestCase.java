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
package fr.opensagres.xdocreport.converter.docx.poi.xhtml;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import fr.opensagres.xdocreport.converter.IURIResolver;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.OptionsHelper;

public class XHTMLOptionsTestCase
{

    @Test
    public void testNullOptions()
        throws Exception
    {
        Options options = null;
        XHTMLOptions xhtmlOptions = XWPF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNull( xhtmlOptions );
    }

    @Test
    public void testNotNullOptions()
        throws Exception
    {
        Options options = Options.getFrom( "DOCX" );
        XHTMLOptions xhtmlOptions = XWPF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNotNull( xhtmlOptions );
    }

    @Test
    public void testOptionsWithURIResolver()
        throws Exception
    {
        Options options = Options.getFrom( "DOCX" );
        OptionsHelper.setURIResolver( options, new IURIResolver()
        {

            public String resolve( String uri )
            {
                return null;
            }
        } );
        XHTMLOptions xhtmlOptions = XWPF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNotNull( xhtmlOptions );
        Assert.assertNotNull( xhtmlOptions.getURIResolver() );
    }

    @Test
    public void testWithSubOptions()
        throws Exception
    {
        XHTMLOptions xhtmlOptions1 =
            XHTMLOptions.create().URIResolver( new fr.opensagres.poi.xwpf.converter.core.IURIResolver()
            {

                public String resolve( String uri )
                {
                    return uri;
                }
            } );
        Options options = Options.getFrom( "DOCX" ).subOptions( xhtmlOptions1 );
        XHTMLOptions xhtmlOptions2 = XWPF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNotNull( xhtmlOptions2 );
        Assert.assertEquals( xhtmlOptions1, xhtmlOptions2 );
    }
}
