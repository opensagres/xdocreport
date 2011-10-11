package fr.opensagres.xdocreport.document.discovery;

import fr.opensagres.xdocreport.core.discovery.IBaseDiscovery;
import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;

public interface ITextStylingDocumentVisitorFactoryDiscovery extends
		IBaseDiscovery {

	IDocumentVisitor createDocumentVisitor();

}
