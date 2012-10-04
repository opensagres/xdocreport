/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.poi.xwpf.converter.core.AbstractXWPFConverter;
import org.apache.poi.xwpf.converter.core.IXWPFConverter;
import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.apache.poi.xwpf.converter.pdf.internal.PdfMapper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class PdfConverter
    extends AbstractXWPFConverter<PdfOptions>
{

    private static final IXWPFConverter<PdfOptions> INSTANCE = new PdfConverter();

    public static IXWPFConverter<PdfOptions> getInstance()
    {
        return INSTANCE;
    }

    // redefining method here might solve classloading issues
    @Override
    public void convert( XWPFDocument XWPFDocument, OutputStream out, PdfOptions options )
        throws XWPFConverterException, IOException
    {
        // TODO Auto-generated method stub
        super.convert( XWPFDocument, out, options );
    }

    // redefining method here might solve classloading issues
    @Override
    public void convert( XWPFDocument XWPFDocument, Writer writer, PdfOptions options )
        throws XWPFConverterException, IOException
    {
        super.convert( XWPFDocument, writer, options );
    }

    @Override
    protected void doConvert( XWPFDocument document, OutputStream out, Writer writer, PdfOptions options )
        throws XWPFConverterException, IOException
    {
        try
        {
            PdfMapper mapper = new PdfMapper( document, out, options );
            mapper.start();
        }
        catch ( Exception e )
        {
            throw new XWPFConverterException( e );
        }

    }

}
