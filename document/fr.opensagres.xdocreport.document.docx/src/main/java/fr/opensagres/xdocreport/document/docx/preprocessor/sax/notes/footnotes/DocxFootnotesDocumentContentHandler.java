package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.footnotes;

import java.util.Map;

import fr.opensagres.xdocreport.document.docx.preprocessor.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocxBufferedDocument;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class DocxFootnotesDocumentContentHandler
    extends DocXBufferedDocumentContentHandler
{

    protected DocxFootnotesDocumentContentHandler( String entryName, FieldsMetadata fieldsMetadata,
                                                   IDocumentFormatter formater, Map<String, Object> sharedContext )
    {
        super( entryName, fieldsMetadata, formater, sharedContext );
    }
    
    @Override
    protected DocxBufferedDocument createDocument()
    {
        return new FootnotesBufferedDocument( this );
    }

}
