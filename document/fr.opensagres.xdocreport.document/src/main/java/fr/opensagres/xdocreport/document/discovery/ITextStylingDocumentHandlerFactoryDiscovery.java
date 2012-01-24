package fr.opensagres.xdocreport.document.discovery;

import fr.opensagres.xdocreport.core.discovery.IBaseDiscovery;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.template.IContext;

public interface ITextStylingDocumentHandlerFactoryDiscovery extends
		IBaseDiscovery {

	IDocumentHandler createDocumentHandler(BufferedElement parent, IContext context);

}
