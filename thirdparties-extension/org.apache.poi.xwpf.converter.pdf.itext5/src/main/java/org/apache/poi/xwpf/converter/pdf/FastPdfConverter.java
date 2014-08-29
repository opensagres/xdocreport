package org.apache.poi.xwpf.converter.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.apache.poi.xwpf.converter.core.openxmlformats.AbstractOpenXMlFormatsConverter;
import org.apache.poi.xwpf.converter.core.openxmlformats.IOpenXMLFormatsPartProvider;
import org.apache.poi.xwpf.converter.pdf.internal.FastPdfMapper;

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
