package fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.document.docx.DocxUtils;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class DocxNumberingDocumentContentHandler
    extends BufferedDocumentContentHandler
{

    private static final String W_ABSTRACT_NUM_ID_ATTR = "w:abstractNumId";

    private final IDocumentFormatter formatter;

    private final Map<String, Object> sharedContext;

    private String currentAbstractNumId;
    
    public DocxNumberingDocumentContentHandler( IDocumentFormatter formatter, Map<String, Object> sharedContext )
    {
        this.formatter = formatter;
        this.sharedContext = sharedContext;
    }

    @Override
    public boolean doStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        if (DocxUtils.isAbstractNum( uri, localName, name )) {
            // w:abstractNumId
            this.currentAbstractNumId = attributes.getValue( W_ABSTRACT_NUM_ID_ATTR );
            System.err.println(currentAbstractNumId);
        }
        return super.doStartElement( uri, localName, name, attributes );
    }
}
