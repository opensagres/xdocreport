package org.odftoolkit.odfdom.converter.pdf;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Test;
import org.odftoolkit.odfdom.converter.core.AbstractODFDOMConverterTest;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

import fr.opensagres.xdocreport.itext.extension.font.IFontProvider;

public class PdfConvertChineseTestCase
{

    @Test
    public void TestChineseCharacters()
        throws Exception
    {

        OutputStream out = new FileOutputStream( new File( "target/TestChineseCharacters.odt.pdf" ) );

        OdfTextDocument document =
            OdfTextDocument.loadDocument( AbstractODFDOMConverterTest.class.getResourceAsStream( "chinese/TestChineseCharacters.odt" ) );

        PdfOptions options = PdfOptions.create();

        // Customize Font provider for Chinese characters
        // This code is not optimized (font are not cached and it works only for windows)
        // Chinese people could you explain us how odt manage chinese characters?
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

    // Uncomment that once the problem with StackOverflow will be fixed.
    @Test
    public void TestChineseCharactersWithFloatImage()
        throws Exception
    {

        OutputStream out = new FileOutputStream( new File( "target/TestChineseCharactersWithFloatImage.odt.pdf" ) );

        OdfTextDocument document =
            OdfTextDocument.loadDocument( AbstractODFDOMConverterTest.class.getResourceAsStream( "chinese/TestChineseCharactersWithFloatImage.odt" ) );

        PdfOptions options = PdfOptions.create();

        // Customize Font provider for Chinese characters
        // This code is not optimized (font are not cached and it works only for windows)
        // Chinese people could you explain us how odt manage chinese characters?
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
