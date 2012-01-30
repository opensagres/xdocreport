package fr.opensagres.xdocreport.document.docx.discovery;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.discovery.ITextStylingDocumentHandlerFactoryDiscovery;
import fr.opensagres.xdocreport.document.docx.textstyling.DocxDocumentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.template.IContext;

public class DocxDocumentHandlerFactoryDiscovery
    implements ITextStylingDocumentHandlerFactoryDiscovery
{

    public String getId()
    {
        return DocumentKind.DOCX.name();
    }

    public IDocumentHandler createDocumentHandler( BufferedElement parent, IContext context )
    {
        return new DocxDocumentHandler( parent, context );
    }

    public String getDescription()
    {
        return "Docx document vistor factory.";
    }
}
