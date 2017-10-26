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
package fr.opensagres.poi.xwpf.converter.core.styles;

import java.util.List;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.poi.xwpf.converter.core.Color;
import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.utils.ColorHelper;

public class FontStylesBasedOnTestCase
{

    @Test
    public void testParagraphStyles()
        throws Exception
    {
        // 1) Load docx with Apache POI
        XWPFDocument document = new XWPFDocument( Data.class.getResourceAsStream( "TestFontStylesBasedOn.docx" ) );

        // Create styles engine
        XWPFStylesDocument stylesDocument = new XWPFStylesDocument( document );

        // Loop for each paragraph
        List<IBodyElement> elements = document.getBodyElements();
        for ( IBodyElement element : elements )
        {
            if ( element.getElementType() == BodyElementType.PARAGRAPH )
            {
                testParagraph( (XWPFParagraph) element, stylesDocument );
            }
        }
    }

    private void testParagraph( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {

        String styleId = paragraph.getStyleID();
        if ( "Style1".equals( styleId ) )
        {
            testParagraphWithStyle1( paragraph, stylesDocument );
        }
        else if ( "Style2".equals( styleId ) )
        {
            testParagraphWithStyle2( paragraph, stylesDocument );
        }
        else if ( "Style3".equals( styleId ) )
        {
            testParagraphWithStyle3( paragraph, stylesDocument );
        }
        else if ( "Style4".equals( styleId ) )
        {
            testParagraphWithStyle4( paragraph, stylesDocument );
        }
    }

    /**
     * Paragraph from word/document.xml :
     * 
     * <pre>
     * <w:p w:rsidR="00F030AA" w:rsidRDefault="00045474" w:rsidP="00045474">
     *         <w:pPr>
     *             <w:pStyle w:val="Style1" />
     *         </w:pPr>
     *         <w:r>
     *             <w:t>A</w:t>
     *         </w:r>
     *     </w:p>
     * </pre>
     * 
     * Style1 from word/styles.xml :
     * 
     * <pre>
     *  <w:style w:type="paragraph" w:customStyle="1" w:styleId="Style1">
     *         <w:name w:val="Style1" />
     *         <w:basedOn w:val="Normal" />
     *         <w:qFormat />
     *         <w:rsid w:val="00045474" />
     *         <w:rPr>
     *             <w:b />
     *         </w:rPr>
     *     </w:style>
     * </pre>
     * 
     * @param paragraph
     * @param stylesDocument
     */
    private void testParagraphWithStyle1( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        List<XWPFRun> runs = paragraph.getRuns();
        XWPFRun run = runs.get( 0 );

        // family = Calibri (Corps)
        String fontFamily = stylesDocument.getFontFamilyAscii( run );
        // should be "Calibri (Corps)" but is null
        // Assert.assertEquals( "Magneto", fontFamily );

        // size= 11
        Float fontSize = stylesDocument.getFontSize( run );
        Assert.assertNotNull( fontSize );
        Assert.assertEquals( 11, fontSize.intValue() );

        // bold= true
        Boolean bold = stylesDocument.getFontStyleBold( run );
        Assert.assertNotNull( bold );
        Assert.assertTrue( bold );

        // italic not defined
        Boolean italic = stylesDocument.getFontStyleItalic( run );
        Assert.assertNull( italic );

        // color not defined
        Color color = stylesDocument.getFontColor( run );
        Assert.assertNull( color );

    }

    /**
     * Paragraph from word/document.xml :
     * 
     * <pre>
     *         <w:p w:rsidR="00045474" w:rsidRDefault="00045474" w:rsidP="00045474">
     *             <w:pPr>
     *                 <w:pStyle w:val="Style2" />
     *             </w:pPr>
     *             <w:r>
     *                 <w:t>B</w:t>
     *             </w:r>
     *         </w:p>
     * </pre>
     * 
     * Style3 from word/styles.xml :
     * 
     * <pre>
     *     <w:style w:type="paragraph" w:customStyle="1" w:styleId="Style2">
     *         <w:name w:val="Style2" />
     *         <w:basedOn w:val="Style1" />
     *         <w:qFormat />
     *         <w:rsid w:val="00045474" />
     *         <w:rPr>
     *             <w:sz w:val="40" />
     *         </w:rPr>
     *     </w:style>
     * </pre>
     * 
     * @param paragraph
     * @param stylesDocument
     */
    private void testParagraphWithStyle2( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        List<XWPFRun> runs = paragraph.getRuns();
        XWPFRun run = runs.get( 0 );

        // family = Calibri (Corps)
        String fontFamily = stylesDocument.getFontFamilyAscii( run );
        // should be "Calibri (Corps)" but is null
        // Assert.assertEquals( "Magneto", fontFamily );

        // size= 20
        Float fontSize = stylesDocument.getFontSize( run );
        Assert.assertNotNull( fontSize );
        Assert.assertEquals( 20, fontSize.intValue() );

        // bold= true
        Boolean bold = stylesDocument.getFontStyleBold( run );
        Assert.assertNotNull( bold );
        Assert.assertTrue( bold );

        // italic not defined
        Boolean italic = stylesDocument.getFontStyleItalic( run );
        Assert.assertNull( italic );

        // color not defined
        Color color = stylesDocument.getFontColor( run );
        Assert.assertNull( color );
    }

    /**
     * Paragraph from word/document.xml :
     * 
     * <pre>
     *      <w:p w:rsidR="00045474" w:rsidRDefault="00045474" w:rsidP="00045474">
     *             <w:pPr>
     *                 <w:pStyle w:val="Style3" />
     *             </w:pPr>
     *             <w:r>
     *                 <w:t>C</w:t>
     *             </w:r>
     *         </w:p>
     * </pre>
     * 
     * Style3 from word/styles.xml :
     * 
     * <pre>
     *  <w:style w:type="paragraph" w:customStyle="1" w:styleId="Style3">
     *         <w:name w:val="Style3" />
     *         <w:basedOn w:val="Style2" />
     *         <w:qFormat />
     *         <w:rsid w:val="00045474" />
     *         <w:rPr>
     *             <w:rFonts w:ascii="Magneto" w:hAnsi="Magneto" />
     *             <w:b w:val="0" />
     *             <w:i />
     *             <w:color w:val="FABF8F" w:themeColor="accent6" w:themeTint="99" />
     *         </w:rPr>
     *     </w:style>
     * </pre>
     * 
     * @param paragraph
     * @param stylesDocument
     */
    private void testParagraphWithStyle3( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        List<XWPFRun> runs = paragraph.getRuns();
        XWPFRun run = runs.get( 0 );

        // family = Magneto
        String fontFamily = stylesDocument.getFontFamilyAscii( run );
        Assert.assertEquals( "Magneto", fontFamily );

        // size= 20
        Float fontSize = stylesDocument.getFontSize( run );
        Assert.assertNotNull( fontSize );
        Assert.assertEquals( 20, fontSize.intValue() );

        // bold= false
        Boolean bold = stylesDocument.getFontStyleBold( run );
        Assert.assertNotNull( bold );
        Assert.assertFalse( bold );

        // italic= true
        Boolean italic = stylesDocument.getFontStyleItalic( run );
        Assert.assertNotNull( italic );
        Assert.assertTrue( italic );

        // color=#FABF8F
        Color color = stylesDocument.getFontColor( run );
        Assert.assertNotNull( color );
        Assert.assertEquals( "#FABF8F", ColorHelper.toHexString( color ).toUpperCase() );

    }

    /**
     * Paragraph from word/document.xml :
     * 
     * <pre>
     * <w:p w:rsidR="00F70C92" w:rsidRDefault="00F70C92" w:rsidP="00F70C92">
     *             <w:pPr>
     *                 <w:pStyle w:val="Style4" />
     *             </w:pPr>
     *             <w:r>
     *                 <w:t>D</w:t>
     *             </w:r>
     *         </w:p>
     * </pre>
     * 
     * Style4 from word/styles.xml :
     * 
     * <pre>
     * <w:style w:type="paragraph" w:customStyle="1" w:styleId="Style4">
     *         <w:name w:val="Style4" />
     *         <w:basedOn w:val="Style3" />
     *         <w:qFormat />
     *         <w:rsid w:val="00F70C92" />
     *         <w:rPr>
     *             <w:rFonts w:ascii="Chiller" w:hAnsi="Chiller" />
     *         </w:rPr>
     *     </w:style>
     * </pre>
     * 
     * @param paragraph
     * @param stylesDocument
     */
    private void testParagraphWithStyle4( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        List<XWPFRun> runs = paragraph.getRuns();
        XWPFRun run = runs.get( 0 );

        // family = Chiller
        String fontFamily = stylesDocument.getFontFamilyAscii( run );
        Assert.assertEquals( "Chiller", fontFamily );

        // size= 20
        Float fontSize = stylesDocument.getFontSize( run );
        Assert.assertNotNull( fontSize );
        Assert.assertEquals( 20, fontSize.intValue() );

        // italic= true
        Boolean italic = stylesDocument.getFontStyleItalic( run );
        Assert.assertNotNull( italic );
        Assert.assertTrue( italic );

        // color=#FABF8F
        Color color = stylesDocument.getFontColor( run );
        Assert.assertNotNull( color );
        Assert.assertEquals( "#FABF8F", ColorHelper.toHexString( color ).toUpperCase() );
    }
}
