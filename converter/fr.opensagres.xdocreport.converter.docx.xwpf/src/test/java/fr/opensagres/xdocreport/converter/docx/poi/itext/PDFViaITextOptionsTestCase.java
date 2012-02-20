package fr.opensagres.xdocreport.converter.docx.poi.itext;

import org.apache.poi.xwpf.converter.itext.PDFViaITextOptions;
import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.OptionsHelper;

public class PDFViaITextOptionsTestCase
{

    @Test
    public void testNullOptions()
        throws Exception
    {
        Options options = null;
        PDFViaITextOptions pdfOptions = XWPF2PDFViaITextConverter.getInstance().toPDFViaITextOptions( options );
        Assert.assertNull( pdfOptions );
    }

    @Test
    public void testNotNullOptions()
        throws Exception
    {
        Options options = Options.getFrom( "DOCX" );
        PDFViaITextOptions pdfOptions = XWPF2PDFViaITextConverter.getInstance().toPDFViaITextOptions( options );
        Assert.assertNotNull( pdfOptions );
    }

    @Test
    public void testOptionsWithFontEncoding()
        throws Exception
    {
        Options options = Options.getFrom( "DOCX" );
        OptionsHelper.setFontEncoding( options, "UTF-8" );
        PDFViaITextOptions pdfOptions = XWPF2PDFViaITextConverter.getInstance().toPDFViaITextOptions( options );
        Assert.assertNotNull( pdfOptions );
        Assert.assertEquals( "UTF-8", pdfOptions.getFontEncoding() );
    }

    @Test
    public void testWithSubOptions()
        throws Exception
    {
        PDFViaITextOptions pdfOptions1 = PDFViaITextOptions.create();
        Options options = Options.getFrom( "DOCX" ).subOptions( pdfOptions1 );
        PDFViaITextOptions pdfOptions2 = XWPF2PDFViaITextConverter.getInstance().toPDFViaITextOptions( options );
        Assert.assertNotNull( pdfOptions2 );
        Assert.assertEquals( pdfOptions1, pdfOptions2 );
    }
}
