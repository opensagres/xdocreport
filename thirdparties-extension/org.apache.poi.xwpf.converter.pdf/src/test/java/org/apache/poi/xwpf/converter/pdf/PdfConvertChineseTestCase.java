package org.apache.poi.xwpf.converter.pdf;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.xwpf.converter.core.AbstractXWPFPOIConverterTest;
import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

import fr.opensagres.xdocreport.itext.extension.font.IFontProvider;

public class PdfConvertChineseTestCase
{

    @Test
    public void TestChineseCharacters()
        throws XWPFConverterException, IOException
    {

        OutputStream out = new FileOutputStream( new File( "target/TestChineseCharacters.docx.pdf" ) );

        XWPFDocument document =
            new XWPFDocument( AbstractXWPFPOIConverterTest.class.getResourceAsStream( "chinese/TestChineseCharacters.docx" ) );

        PdfOptions options = PdfOptions.create();

        // Customize Font provider for Chinese characters
        // This code is not optimized (font are not cached and it works only for windows)
        // Chinese people could you explain us how docx manage chinese characters?
        options.fontProvider( new IFontProvider()
        {

            public Font getFont( String familyName, String encoding, float size, int style, Color color )
            {
                try
                {
                    BaseFont bfChinese =
                        BaseFont.createFont( "c:/Windows/Fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED );
                    Font fontChinese = new Font( bfChinese, size, style, color );
                    if ( familyName != null )
                        fontChinese.setFamily( familyName );
                    return fontChinese;
                }
                catch ( Throwable e )
                {
                    e.printStackTrace();
                    // An error occurs, use the default font provider.
                    return ITextFontRegistry.getRegistry().getFont( familyName, encoding, size, style, color );
                }
            }
        } );
        PdfConverter.getInstance().convert( document, out, options );
    }
}
