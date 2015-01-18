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
package fr.opensagres.xdocreport.converter.odt.pdf.fop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.io.IEntryInputStreamProvider;

public class StructuresODT2PDFTest
    extends TestCase
{

    public void testNo()
        throws Exception
    {

    }

    public static void main( String[] args )
    {
        doGenerate();
        doGenerate();
    }

    public static void doGenerate()
    {
        long startTime = System.currentTimeMillis();

        ODTFOPConverter converter = ODTFOPConverter.getInstance();

        IEntryInputStreamProvider provider = new IEntryInputStreamProvider()
        {

            public InputStream getEntryInputStream( String entryName )
            {
                if ( entryName.endsWith( ".png" ) )
                {
                    entryName = entryName.substring( entryName.indexOf( '/' ) + 1, entryName.length() );
                    return StructuresODT2PDFTest.class.getResourceAsStream( entryName );
                }
                return StructuresODT2PDFTest.class.getResourceAsStream( "Structures." + entryName );
            }
        };

        try
        {
            OutputStream outputStream = new FileOutputStream( new File( "Structures.pdf" ) );
            converter.convert( provider, outputStream, System.out, null );
        }
        catch ( XDocConverterException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println( System.currentTimeMillis() - startTime + "(ms)" );

    }
}
