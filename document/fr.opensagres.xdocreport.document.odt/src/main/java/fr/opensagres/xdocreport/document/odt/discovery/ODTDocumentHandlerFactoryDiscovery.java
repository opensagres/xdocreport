package fr.opensagres.xdocreport.document.odt.discovery;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.discovery.ITextStylingDocumentHandlerFactoryDiscovery;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTDocumentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.template.IContext;

public class ODTDocumentHandlerFactoryDiscovery
    implements ITextStylingDocumentHandlerFactoryDiscovery
{

    public String getId()
    {
        return DocumentKind.ODT.name();
    }

    public IDocumentHandler createDocumentHandler( BufferedElement parent, IContext context )
    {
        return new ODTDocumentHandler( parent, context );
    }

    public String getDescription()
    {
        return "ODT document handler factory.";
    }

}
