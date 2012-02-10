package fr.opensagres.xdocreport.document.docx.preprocessor.dom;

import java.util.Map;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.utils.DOMUtils;
import fr.opensagres.xdocreport.core.utils.XPathUtils;
import fr.opensagres.xdocreport.document.preprocessor.IXDocPreprocessor;
import fr.opensagres.xdocreport.document.preprocessor.dom.DOMPreprocessor;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * Experimental work to use DOM instead of using SAX for preprocessing step.
 *
 */
public class DocxDocumentPreprocessor
    extends DOMPreprocessor
{

    public static final IXDocPreprocessor INSTANCE = new DocxDocumentPreprocessor();

    @Override
    protected void visit( Document document, String entryName, FieldsMetadata fieldsMetadata, IDocumentFormatter formatter,
                          Map<String, Object> sharedContext )
        throws XDocReportException
    {

        try
        {
            Element fldSimpleElt = null;
            NodeList fldSimpleNodeList =
                XPathUtils.evaluateNodeSet( document.getDocumentElement(), "//w:fldSimple",
                                            DocxNamespaceContext.INSTANCE );
            for ( int i = 0; i < fldSimpleNodeList.getLength(); i++ )
            {
                fldSimpleElt = (Element) fldSimpleNodeList.item( i );
                processFldSimple( fldSimpleElt, fieldsMetadata, formatter, sharedContext );
            }
        }
        catch ( XPathExpressionException e )
        {
            throw new XDocReportException( e );
        }
    }

    /**
     * w:fldSimple with MERGEFIELD type and false otherwise. If element is w:fldSimple :
     * 
     * <pre>
     * <w:fldSimple w:instr=" MERGEFIELD  ${name} ">
     *      <w:r w:rsidR="00396432">
     *          <w:rPr>
     *              <w:noProof/>
     *          </w:rPr>
     *          <w:t>�${name}�</w:t>
     *      </w:r>
     *  </w:fldSimple>
     * </pre>
     * 
     * it is transformed to this content :
     * 
     * <pre>
     * <w:r w:rsidR="00396432">
     *          <w:rPr>
     *              <w:noProof/>
     *              </w:rPr>
     *              <w:t>${name}</w:t>
     *      </w:r>
     * </pre>
     */
    private void processFldSimple( Element fldSimpleElt, FieldsMetadata fieldsMetadata, IDocumentFormatter formatter,
                                   Map<String, Object> sharedContext )
    {
        try
        {
            DOMUtils.save( fldSimpleElt, System.err );
        }
        catch ( TransformerException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
