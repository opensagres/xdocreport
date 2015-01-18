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
package fr.opensagres.xdocreport.converter.odt.xhtml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.dom.DOMResult;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.converter.odt.XMLUtils;
import fr.opensagres.xdocreport.core.io.IEntryInputStreamProvider;

public class StructuresODT2XHTMLTest
    extends TestCase
{

    public void testNo()
        throws Exception
    {

    }

    public static void main( String[] args )
    {
        long startTime = System.currentTimeMillis();

        ODTXHTMLConverter converter = ODTXHTMLConverter.getInstance();

        IEntryInputStreamProvider inProvider = new IEntryInputStreamProvider()
        {

            public InputStream getEntryInputStream( String entryName )
            {
                if ( entryName.equals( "content.xml" ) )
                {
                    return StructuresODT2XHTMLTest.class.getResourceAsStream( "Structures.content.xml" );
                }
                if ( entryName.equals( "styles.xml" ) )
                {
                    return StructuresODT2XHTMLTest.class.getResourceAsStream( "Structures.styles.xml" );
                }
                if ( entryName.equals( "meta.xml" ) )
                {
                    return StructuresODT2XHTMLTest.class.getResourceAsStream( "Structures.meta.xml" );
                }
                return null;
            }
        };

        // OutputStream outputStream = new StringBuilderOutputStream();
        DOMResult result = new DOMResult();
        try
        {
            converter.convert2XHTML( inProvider, result, null );
            System.err.println( XMLUtils.toString( result.getNode() ) );
        }
        catch ( XDocConverterException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        System.out.println( System.currentTimeMillis() - startTime + "(ms)" );

    }
}
