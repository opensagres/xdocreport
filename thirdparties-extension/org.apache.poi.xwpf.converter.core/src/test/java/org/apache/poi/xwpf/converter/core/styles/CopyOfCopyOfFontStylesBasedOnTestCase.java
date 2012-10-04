package org.apache.poi.xwpf.converter.core.styles;

import java.awt.Color;
import java.util.List;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.ColorHelper;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Assert;
import org.junit.Test;

public class CopyOfCopyOfFontStylesBasedOnTestCase
{

    @Test
    public void testParagraphStyles()
        throws Exception
    {
        // 1) Load docx with Apache POI
        XWPFDocument document = new XWPFDocument( Data.class.getResourceAsStream( "DocxStructures.docx" ) );

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
        if ( "Project: ${project.name}".equals( paragraph.getText() ) )
        {
            testParagraphWithTitre( paragraph, stylesDocument );
        }
    }

    /**
     * Paragraph from word/document.xml :
     * 
     * <pre>
     *         <w:p w:rsidR="008A2506" w:rsidRDefault="008A2506" w:rsidP="008A2506">
     *             <w:pPr>
     *                 <w:pStyle w:val="Titre" />
     *                 <w:outlineLvl w:val="0" />
     *             </w:pPr>
     *             <w:r>
     *                 <w:t>Office Open XML</w:t>
     *             </w:r>
     *         </w:p>
     * 
     * Titre from word/styles.xml :
     * 
     * <pre>
     *     <w:style w:type="paragraph" w:styleId="Titre">
     *         <w:name w:val="Title" />
     *         <w:aliases w:val="Document Title" />
     *         <w:next w:val="Normal" />
     *         <w:link w:val="TitreCar" />
     *         <w:rsid w:val="008A2506" />
     *         <w:pPr>
     *             <w:widowControl w:val="0" />
     *             <w:jc w:val="center" />
     *         </w:pPr>
     *         <w:rPr>
     *             <w:rFonts w:eastAsia="Times New Roman" w:cs="Arial" />
     *             <w:color w:val="17365D" w:themeColor="text2" w:themeShade="BF" />
     *             <w:sz w:val="192" />
     *             <w:lang w:val="en-CA" w:eastAsia="en-CA" />
     *         </w:rPr>
     *     </w:style>
     * </pre>
     * 
     * @param paragraph
     * @param stylesDocument
     */
    private void testParagraphWithTitre( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {

        // Spacing after : call twice (the first set teh value on cache, the second retrieves teh value from the cache.
        Float spacingAfter = stylesDocument.getSpacingAfter( paragraph );
        spacingAfter = stylesDocument.getSpacingAfter( paragraph );

        Assert.assertNotNull( spacingAfter );
        Assert.assertEquals( 10, spacingAfter.intValue() );

        List<XWPFRun> runs = paragraph.getRuns();
        XWPFRun run = runs.get( 0 );

        // family = Times New Roman
        String fontFamily = stylesDocument.getFontFamily( run );
        Assert.assertEquals( "Times New Roman", fontFamily );

        // size= 12
        Integer fontSize = stylesDocument.getFontSize( run );
        Assert.assertNotNull( fontSize );
        Assert.assertEquals( 12, fontSize.intValue() );

        // bold = true
        Boolean bold = stylesDocument.getFontStyleBold( run );
        Assert.assertNotNull( bold );
        Assert.assertTrue( bold );

        // italic not defined
        Boolean italic = stylesDocument.getFontStyleItalic( run );
        Assert.assertNull( italic );

        // color =#17365D
        Color color = stylesDocument.getFontColor( run );
        Assert.assertNotNull( color );
        Assert.assertEquals( "#000000", ColorHelper.toHexString( color ).toUpperCase() );

    }

}
