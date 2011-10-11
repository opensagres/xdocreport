package fr.opensagres.xdocreport.document.docx.discovery;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.discovery.ITextStylingDocumentVisitorFactoryDiscovery;
import fr.opensagres.xdocreport.document.docx.textstyling.DocxDocumentVisitor;
import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;

public class DocxDocumentVisitorFactoryDiscovery implements
		ITextStylingDocumentVisitorFactoryDiscovery {

	public String getId() {
		return DocumentKind.DOCX.name();
	}

	public IDocumentVisitor createDocumentVisitor() {
		return new DocxDocumentVisitor();
	}

	public String getDescription() {
		return "Docx document vistor factory.";
	}
}
