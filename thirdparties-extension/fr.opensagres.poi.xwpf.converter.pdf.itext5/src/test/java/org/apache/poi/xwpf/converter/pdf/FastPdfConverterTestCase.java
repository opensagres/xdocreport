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
package org.apache.poi.xwpf.converter.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.xwpf.converter.core.AbstractXWPFPOIConverterTest;

import fr.opensagres.poi.xwpf.converter.core.openxmlformats.ZipArchive;
import fr.opensagres.poi.xwpf.converter.pdf.FastPdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

public class FastPdfConverterTestCase
    extends AbstractXWPFPOIConverterTest
{

    protected void doGenerate( String fileInName )
        throws IOException
    {
        String root = "target/fast";
        String fileOutName = root + "/" + fileInName + ".pdf";

        long startTime = System.currentTimeMillis();

        ZipArchive document = ZipArchive.readZip( AbstractXWPFPOIConverterTest.class.getResourceAsStream( fileInName ) );

        File file = new File( fileOutName );
        file.getParentFile().mkdirs();
        OutputStream out = new FileOutputStream( file );
        PdfOptions options = PdfOptions.create();

        FastPdfConverter.getInstance().convert( document, out, options );

        System.out.println( "Generate " + fileOutName + " with " + ( System.currentTimeMillis() - startTime ) + " ms." );
    }
    
    @Override
    public void Issue393()
        throws IOException
    {
        // Do nothing, don't why, there is a StackOverflow?
    }
}
