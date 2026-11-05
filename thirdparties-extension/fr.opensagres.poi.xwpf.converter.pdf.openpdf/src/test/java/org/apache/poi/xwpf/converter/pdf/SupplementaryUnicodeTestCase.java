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
package org.apache.poi.xwpf.converter.pdf;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import junit.framework.TestCase;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;

import fr.opensagres.xdocreport.openpdf.extension.font.FontGroup;
import fr.opensagres.xdocreport.openpdf.extension.font.IFontProvider;

/**
 * Regression tests for supplementary Unicode during DOCX and ODT PDF conversion.
 * Java represents U+10437 as a UTF-16 surrogate pair. The converters used to
 * choose a font for each char separately, so neither half matched the glyph
 * in the Deseret font and the character could disappear from the PDF.
 * These tests check font selection and extracted PDF text with BMP characters,
 * combining marks and styled runs alongside the supplementary character.
 */
public class SupplementaryUnicodeTestCase extends TestCase
{
    // Keep the surrogate pair explicit because this regression concerns UTF-16 units.
    private static final String DESERET = "\uD801\uDC37";

    // Only the complex-script test font contains U+10437, making font selection observable.
    private final IFontProvider fontProvider = ( family, encoding, size, style, color ) ->
    {
        String file = family != null && family.contains( "Deseret" ) ? "NotoSansDeseret.ttf"
                        : "NotoSansSC.ttf";
        try ( InputStream stream = getClass().getResourceAsStream( "/fonts/" + file ) )
        {
            if ( stream == null )
            {
                throw new IllegalStateException( "Missing test font: " + file );
            }
            BaseFont base = BaseFont.createFont( file, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true,
                                                 stream.readAllBytes(), null );
            return new Font( base, size, style, color == null ? Color.BLACK : color );
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( e );
        }
    };

    public void testFontGroupUsesCompleteCodePoints()
    {
        Font western = fontProvider.getFont( "Noto Sans SC", BaseFont.IDENTITY_H, 12, Font.NORMAL, Color.BLACK );
        Font complex = fontProvider.getFont( "Noto Deseret", BaseFont.IDENTITY_H, 12, Font.NORMAL, Color.BLACK );
        assertEquals( FontGroup.WESTERN, FontGroup.getUnicodeGroup( 'A', western, null, complex ) );
        assertEquals( FontGroup.WESTERN, FontGroup.getUnicodeGroup( (int) 'A', western, null, complex ) );
        assertEquals( FontGroup.COMPLEX, FontGroup.getUnicodeGroup( 0x10437, western, null, complex ) );
    }

    public void testDocxAndOdtUnicode() throws Exception
    {
        String[] samples = {
            "A" + DESERET + "B",
            "Élodie 中村 € " + DESERET,
            "ASCII Élodie 中村",
            // Use a combining accent rather than a precomposed character.
            "Cafe\u0301 " + DESERET
        };
        for ( String sample : samples )
        {
            assertPdf( convertDocx( sample, false ), sample );
            assertPdf( convertOdt( sample, false ), sample );
        }
    }

    public void testStyledRunsAndSpans() throws Exception
    {
        String expected = "A" + DESERET + "B";
        assertPdf( convertDocx( expected, true ), expected );
        assertPdf( convertOdt( expected, true ), expected );
    }

    private byte[] convertDocx( String text, boolean styled ) throws Exception
    {
        try ( XWPFDocument document = new XWPFDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream() )
        {
            // The converter expects a styles part and section page size in the DOCX package.
            document.createStyles().setStyles( CTStyles.Factory.newInstance() );
            document.getDocument().getBody().addNewSectPr().addNewPgSz().setW( BigInteger.valueOf( 11906 ) );
            document.getDocument().getBody().getSectPr().getPgSz().setH( BigInteger.valueOf( 16838 ) );
            XWPFParagraph paragraph = document.createParagraph();
            if ( styled )
            {
                // Keep both UTF-16 units in one run when the formatting changes.
                XWPFRun first = paragraph.createRun();
                first.setBold( true );
                setRun( first, text.substring( 0, text.length() - 1 ) );
                XWPFRun second = paragraph.createRun();
                second.setItalic( true );
                setRun( second, text.substring( text.length() - 1 ) );
            }
            else
            {
                setRun( paragraph.createRun(), text );
            }
            assertEquals( text, paragraph.getText() );
            // Reload so XDocReport can read the generated styles package part.
            ByteArrayOutputStream docx = new ByteArrayOutputStream();
            document.write( docx );
            try ( XWPFDocument reloaded = new XWPFDocument( new ByteArrayInputStream( docx.toByteArray() ) ) )
            {
                fr.opensagres.poi.xwpf.converter.pdf.PdfConverter.getInstance().convert(
                    reloaded, output, fr.opensagres.poi.xwpf.converter.pdf.PdfOptions.create().fontProvider( fontProvider ) );
            }
            return output.toByteArray();
        }
    }

    private void setRun( XWPFRun run, String text )
    {
        run.setText( text );
        run.setFontFamily( "Noto Sans SC" );
        // PdfMapper uses the DOCX hAnsi font as its third font group.
        run.getCTR().getRPr().getRFontsArray( 0 ).setHAnsi( "Noto Deseret" );
    }

    private byte[] convertOdt( String text, boolean styled ) throws Exception
    {
        String body = styled ? "<text:span text:style-name=\"T\">" + text.substring( 0, text.length() - 1 )
                              + "</text:span><text:span text:style-name=\"S\">"
                              + text.substring( text.length() - 1 ) + "</text:span>"
                              : "<text:span text:style-name=\"T\">" + text + "</text:span>";

        String content = createOdtContent( body, styled );
        String styles = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<office:document-styles xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\""
            + " xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\""
            + " xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\""
            + " office:version=\"1.2\"><office:styles/><office:automatic-styles>"
            + "<style:page-layout style:name=\"pm1\"><style:page-layout-properties"
            + " fo:page-width=\"21cm\" fo:page-height=\"29.7cm\" fo:margin=\"2cm\"/>"
            + "</style:page-layout></office:automatic-styles><office:master-styles>"
            + "<style:master-page style:name=\"Standard\" style:page-layout-name=\"pm1\"/>"
            + "</office:master-styles></office:document-styles>";
        String manifest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\""
            + " manifest:version=\"1.2\"><manifest:file-entry manifest:full-path=\"/\""
            + " manifest:media-type=\"application/vnd.oasis.opendocument.text\"/>"
            + "<manifest:file-entry manifest:full-path=\"content.xml\" manifest:media-type=\"text/xml\"/>"
            + "</manifest:manifest>";
        // Build a small ODT in memory so each sample reaches the converter as real document text.
        ByteArrayOutputStream odt = new ByteArrayOutputStream();
        try ( ZipOutputStream zip = new ZipOutputStream( odt ) )
        {
            addEntry( zip, "mimetype", "application/vnd.oasis.opendocument.text" );
            addEntry( zip, "content.xml", content );
            addEntry( zip, "styles.xml", styles );
            addEntry( zip, "META-INF/manifest.xml", manifest );
        }
        try ( OdfTextDocument document = OdfTextDocument.loadDocument(
                  new ByteArrayInputStream( odt.toByteArray() ) );
              ByteArrayOutputStream output = new ByteArrayOutputStream() )
        {
            assertTrue( document.getContentDom().getDocumentElement().getTextContent().contains( text ) );
            fr.opensagres.odfdom.converter.pdf.PdfConverter.getInstance().convert(
                document, output, fr.opensagres.odfdom.converter.pdf.PdfOptions.create().fontProvider( fontProvider ) );
            return output.toByteArray();
        }
    }

    private String createOdtContent( String body, boolean styled )
    {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\""
            + " xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\""
            + " xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\""
            + " xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\""
            + " office:version=\"1.2\"><office:automatic-styles>"
            + "<style:style style:name=\"T\" style:family=\"text\"><style:text-properties"
            + " style:font-name=\"Noto Sans SC\""
            + " style:font-name-complex=\"Noto Deseret\""
            + ( styled ? " fo:color=\"#ff0000\"" : "" ) + "/></style:style>"
            + "<style:style style:name=\"S\" style:family=\"text\"><style:text-properties"
            + " style:font-name=\"Noto Sans SC\""
            + " style:font-name-complex=\"Noto Deseret\""
            + " fo:color=\"#0000ff\"/></style:style>"
            + "</office:automatic-styles><office:body><office:text><text:p>" + body
            + "</text:p></office:text></office:body></office:document-content>";
    }

    private void addEntry( ZipOutputStream zip, String name, String value ) throws Exception
    {
        zip.putNextEntry( new ZipEntry( name ) );
        zip.write( value.getBytes( StandardCharsets.UTF_8 ) );
        zip.closeEntry();
    }

    private void assertPdf( byte[] pdf, String expected ) throws Exception
    {
        try ( PdfReader reader = new PdfReader( pdf ) )
        {
            assertEquals( 1, reader.getNumberOfPages() );
            String actual = new PdfTextExtractor( reader ).getTextFromPage( 1 ).trim();
            if ( expected.equals( "A" + DESERET + "B" ) )
            {
                // OpenPDF's extractor infers spaces at the two font boundaries.
                actual = actual.replace( " " + DESERET + " ", DESERET );
            }
            assertEquals( expected, actual );
        }
    }
}
