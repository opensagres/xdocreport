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
package org.odftoolkit.odfdom.converter.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Test;
import org.odftoolkit.odfdom.converter.core.AbstractODFDOMConverterTest;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import fr.opensagres.odfdom.converter.pdf.PdfConverter;
import fr.opensagres.odfdom.converter.pdf.PdfOptions;

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
        /*options.fontProvider( new IFontProvider()
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
        } );*/
        PdfConverter.getInstance().convert( document, out, options );
    }

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
        /*options.fontProvider( new IFontProvider()
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
        } );*/
        PdfConverter.getInstance().convert( document, out, options );
    }
}
