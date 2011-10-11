package fr.opensagres.xdocreport.document.odt.discovery;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.discovery.ITextStylingDocumentVisitorFactoryDiscovery;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTDocumentVisitor;
import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;

public class ODTDocumentVisitorFactoryDiscovery implements
		ITextStylingDocumentVisitorFactoryDiscovery {

	public String getId() {
		return DocumentKind.ODT.name();
	}

	public IDocumentVisitor createDocumentVisitor() {
		return new ODTDocumentVisitor();
	}

	public String getDescription() {
		return "ODT document vistor factory.";
	}

}
