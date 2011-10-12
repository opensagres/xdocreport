package fr.opensagres.xdocreport.document.docx.discovery;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.discovery.ITextStylingDocumentHandlerFactoryDiscovery;
import fr.opensagres.xdocreport.document.docx.textstyling.DocxDocumentHandler;
import fr.opensagres.xdocreport.template.textstyling.IDocumentHandler;

public class DocxDocumentHandlerFactoryDiscovery implements
		ITextStylingDocumentHandlerFactoryDiscovery {

	public String getId() {
		return DocumentKind.DOCX.name();
	}

	public IDocumentHandler createDocumentHandler() {
		return new DocxDocumentHandler();
	}

	public String getDescription() {
		return "Docx document vistor factory.";
	}
}
