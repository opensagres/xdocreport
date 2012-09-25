package org.apache.poi.xwpf.converter.styles;

import java.util.List;

import org.apache.poi.xwpf.converter.styles.pargraph.ParagraphFontFamilyValueProvider;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.Test;

import com.lowagie.text.Font;

public class FontStylesBasedOnTestCase
{

    @Test
    public void testname()
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

        String fontFamily = ParagraphFontFamilyValueProvider.INSTANCE.getValue( paragraph, stylesDocument );
        System.err.println(fontFamily);
        String styleId = paragraph.getStyleID();
        if ( "Style1".equals( styleId ) )
        {
            testParagraphWithStyle1( paragraph, stylesDocument );
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
        Font font = stylesDocument.getFont(paragraph);

    }
}
