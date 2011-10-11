package fr.opensagres.xdocreport.document.odt.textstyling;

import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;
import fr.opensagres.xdocreport.template.textstyling.html.HTMLTextStylingFormatter;

public class ODTHTMLTextStylingFormatter extends HTMLTextStylingFormatter {

	public static final ITextStylingFormatter INSTANCE = new ODTHTMLTextStylingFormatter();

	@Override
	protected IDocumentVisitor createVisitor() {
		return new ODTDocumentVisitor();
	}

}
