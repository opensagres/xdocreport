package fr.opensagres.xdocreport.document.docx.textstyling;

import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;
import fr.opensagres.xdocreport.template.textstyling.html.HTMLTextStylingFormatter;

public class DocxHTMLTextStylingFormatter extends HTMLTextStylingFormatter {

	public static final ITextStylingFormatter INSTANCE = new DocxHTMLTextStylingFormatter();

	@Override
	protected IDocumentVisitor createVisitor() {
		return new DocxDocumentVisitor();
	}

}
