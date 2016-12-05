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
package fr.opensagres.poi.xwpf.converter.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import fr.opensagres.poi.xwpf.converter.core.XWPFConverterException;
import fr.opensagres.poi.xwpf.converter.core.openxmlformats.AbstractOpenXMlFormatsConverter;
import fr.opensagres.poi.xwpf.converter.core.openxmlformats.IOpenXMLFormatsPartProvider;
import fr.opensagres.poi.xwpf.converter.pdf.internal.FastPdfMapper;

public class FastPdfConverter
    extends AbstractOpenXMlFormatsConverter<PdfOptions>
{

    private static final FastPdfConverter INSTANCE = new FastPdfConverter();

    public static FastPdfConverter getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected void doConvert( IOpenXMLFormatsPartProvider provider, OutputStream out, Writer writer, PdfOptions options )
        throws XWPFConverterException, IOException
    {
        try
        {
            FastPdfMapper mapper = new FastPdfMapper( provider, out, options );
            mapper.start();
        }
        catch ( Exception e )
        {
            throw new XWPFConverterException( e );
        }

    }

}
