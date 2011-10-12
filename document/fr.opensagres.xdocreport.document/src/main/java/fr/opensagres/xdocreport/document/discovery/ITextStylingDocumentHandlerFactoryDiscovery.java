package fr.opensagres.xdocreport.document.discovery;

import fr.opensagres.xdocreport.core.discovery.IBaseDiscovery;
import fr.opensagres.xdocreport.template.textstyling.IDocumentHandler;

public interface ITextStylingDocumentHandlerFactoryDiscovery extends
		IBaseDiscovery {

	IDocumentHandler createDocumentHandler();

}
