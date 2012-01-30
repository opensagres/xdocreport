package fr.opensagres.xdocreport.document.odt.preprocessor;

import java.util.Map;

import fr.opensagres.xdocreport.document.preprocessor.IXDocPreprocessor;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.SAXXDocPreprocessor;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class ODTStylesPreprocessor
    extends SAXXDocPreprocessor
{

    public static final IXDocPreprocessor INSTANCE = new ODTStylesPreprocessor();

    @Override
    protected BufferedDocumentContentHandler<?> createBufferedDocumentContentHandler( String entryName,
                                                                                      FieldsMetadata fieldsMetadata,
                                                                                      IDocumentFormatter formatter,
                                                                                      Map<String, Object> sharedContext )
    {
        return new ODTStyleContentHandler();
    }

}
