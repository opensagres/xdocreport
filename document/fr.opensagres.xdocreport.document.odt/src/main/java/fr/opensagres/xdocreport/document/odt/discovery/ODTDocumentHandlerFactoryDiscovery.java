package fr.opensagres.xdocreport.document.odt.discovery;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.discovery.ITextStylingDocumentHandlerFactoryDiscovery;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTDocumentHandler;
import fr.opensagres.xdocreport.template.textstyling.IDocumentHandler;

public class ODTDocumentHandlerFactoryDiscovery implements
		ITextStylingDocumentHandlerFactoryDiscovery {

	public String getId() {
		return DocumentKind.ODT.name();
	}

	public IDocumentHandler createDocumentHandler() {
		return new ODTDocumentHandler();
	}

	public String getDescription() {
		return "ODT document handler factory.";
	}

}
