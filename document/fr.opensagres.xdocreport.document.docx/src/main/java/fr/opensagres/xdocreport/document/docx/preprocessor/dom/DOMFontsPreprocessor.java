package fr.opensagres.xdocreport.document.docx.preprocessor.dom;

import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.utils.XPathUtils;
import fr.opensagres.xdocreport.document.preprocessor.IXDocPreprocessor;
import fr.opensagres.xdocreport.document.preprocessor.dom.DOMPreprocessor;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class DOMFontsPreprocessor
    extends DOMPreprocessor
{

    public static final String FONT_NAME_KEY = "___fontName";

    public static IXDocPreprocessor INSTANCE = new DOMFontsPreprocessor();

    @Override
    protected void visit( Document document, String entryName, FieldsMetadata fieldsMetadata,
                          IDocumentFormatter formatter, Map<String, Object> sharedContext )
        throws XDocReportException
    {

        /**
         * <w:rPr> <w:rFonts w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial" /> <w:sz w:val="24" /> <w:szCs w:val="24" />
         * </w:rPr>
         */
        // get list of w:rPr
        try
        {
            Element rFontsElt = null;
            NodeList rFontsNodeList =
                XPathUtils.evaluateNodeSet( document, "//w:rFonts", DocxNamespaceContext.INSTANCE );
            for ( int i = 0; i < rFontsNodeList.getLength(); i++ )
            {

                rFontsElt = (Element) rFontsNodeList.item( i );
                
                updateDynamicAttr( rFontsElt, "w:ascii", FONT_NAME_KEY, formatter );
                updateDynamicAttr( rFontsElt, "w:cs", FONT_NAME_KEY, formatter );
                updateDynamicAttr( rFontsElt, "w:hAnsi", FONT_NAME_KEY, formatter );

                
                
                // try
                // {
                // DOMUtils.save( rFontsElt, System.out );
                // }
                // catch ( TransformerException e )
                // {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
            }
        }
        catch ( XPathExpressionException e )
        {
            throw new XDocReportException( e );
        }

    }

}
