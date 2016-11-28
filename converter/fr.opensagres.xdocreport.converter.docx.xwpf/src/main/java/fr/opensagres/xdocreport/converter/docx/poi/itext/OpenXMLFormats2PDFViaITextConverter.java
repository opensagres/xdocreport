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
package fr.opensagres.xdocreport.converter.docx.poi.itext;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import fr.opensagres.poi.xwpf.converter.core.openxmlformats.AbstractOpenXMLFormatsPartProvider;
import fr.opensagres.poi.xwpf.converter.pdf.FastPdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.MimeMappingConstants;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.OptionsHelper;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.converter.internal.AbstractConverterEntriesSupport;
import fr.opensagres.xdocreport.core.io.IEntryInputStreamProvider;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;

public class OpenXMLFormats2PDFViaITextConverter
    extends AbstractConverterEntriesSupport
{

    private static final OpenXMLFormats2PDFViaITextConverter INSTANCE = new OpenXMLFormats2PDFViaITextConverter();

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( OpenXMLFormats2PDFViaITextConverter.class.getName() );

    public static OpenXMLFormats2PDFViaITextConverter getInstance()
    {
        return INSTANCE;
    }

    // @Override
    public void convert( final IEntryInputStreamProvider inProvider, OutputStream out, Options options )
        throws XDocConverterException
    {
        try
        {
            fr.opensagres.poi.xwpf.converter.core.openxmlformats.IOpenXMLFormatsPartProvider provider =
                new AbstractOpenXMLFormatsPartProvider()
                {

                    public InputStream getEntryInputStream( String entryName )
                    {
                        return inProvider.getEntryInputStream( entryName );
                    }
                };
            FastPdfConverter.getInstance().convert( provider, out, toPdfOptions( options ) );
        }
        catch ( Exception e )
        {
            LOGGER.severe( e.getMessage() );
            throw new XDocConverterException( e );
        }
    }

    public PdfOptions toPdfOptions( Options options )
    {
        if ( options == null )
        {
            return null;
        }
        Object value = options.getSubOptions( PdfOptions.class );
        if ( value instanceof PdfOptions )
        {
            return (PdfOptions) value;
        }
        PdfOptions pdfOptions = PdfOptions.create();
        // Populate font encoding
        String fontEncoding = OptionsHelper.getFontEncoding( options );
        if ( StringUtils.isNotEmpty( fontEncoding ) )
        {
            pdfOptions.fontEncoding( fontEncoding );
        }
        return pdfOptions;
    }

    public MimeMapping getMimeMapping()
    {

        return MimeMappingConstants.PDF_MIME_MAPPING;
    }

}
