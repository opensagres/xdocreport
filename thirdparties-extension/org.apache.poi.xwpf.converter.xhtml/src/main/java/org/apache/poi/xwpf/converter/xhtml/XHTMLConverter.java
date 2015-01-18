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
package org.apache.poi.xwpf.converter.xhtml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.poi.xwpf.converter.core.AbstractXWPFConverter;
import org.apache.poi.xwpf.converter.core.IXWPFConverter;
import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.apache.poi.xwpf.converter.xhtml.internal.XHTMLMapper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.xml.sax.ContentHandler;

public class XHTMLConverter
    extends AbstractXWPFConverter<XHTMLOptions>
{

    private static final IXWPFConverter<XHTMLOptions> INSTANCE = new XHTMLConverter();

    public static IXWPFConverter<XHTMLOptions> getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected void doConvert( XWPFDocument document, OutputStream out, Writer writer, XHTMLOptions options )
        throws XWPFConverterException, IOException
    {

        options = options != null ? options : XHTMLOptions.getDefault();
        // Create SAX content handler.
        IContentHandlerFactory factory = options.getContentHandlerFactory();
        if ( factory == null )
        {
            factory = DefaultContentHandlerFactory.INSTANCE;
        }
        ContentHandler contentHandler = factory.create( out, writer, options );
        // convert the document to XHTML
        convert( document, contentHandler, options );
    }

    public void convert( XWPFDocument document, ContentHandler contentHandler, XHTMLOptions options )
        throws XWPFConverterException, IOException
    {
        try
        {
            options = options != null ? options : XHTMLOptions.getDefault();
            XHTMLMapper mapper = new XHTMLMapper( document, contentHandler, options );
            mapper.start();
        }
        catch ( Exception e )
        {
            throw new XWPFConverterException( e );
        }
    }

}