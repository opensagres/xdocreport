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
package fr.opensagres.poi.xwpf.converter.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import fr.opensagres.poi.xwpf.converter.core.IXWPFConverter;
import fr.opensagres.poi.xwpf.converter.core.Options;
import fr.opensagres.poi.xwpf.converter.core.XWPFConverterException;

public abstract class AbstractXWPFConverter<T extends Options>
    implements IXWPFConverter<T>
{

    public void convert( XWPFDocument XWPFDocument, OutputStream out, T options )
        throws XWPFConverterException, IOException
    {
        try
        {
            doConvert( XWPFDocument, out, null, options );
        }
        finally
        {
            if ( out != null )
            {
                out.close();
            }
        }
    }

    public void convert( XWPFDocument XWPFDocument, Writer writer, T options )
        throws XWPFConverterException, IOException
    {
        try
        {
            doConvert( XWPFDocument, null, writer, options );
        }
        finally
        {
            if ( writer != null )
            {
                writer.close();
            }
        }
    }

    protected abstract void doConvert( XWPFDocument XWPFDocument, OutputStream out, Writer writer, T options )
        throws XWPFConverterException, IOException;
}
