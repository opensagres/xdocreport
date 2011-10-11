package fr.opensagres.xdocreport.document.docx.discovery;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.docx.textstyling.DocxHTMLTextStylingFormatter;
import fr.opensagres.xdocreport.template.TextStylingKind;
import fr.opensagres.xdocreport.template.discovery.AbstractTextStylingFormatterDiscovery;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;

public class DocxHTMLTextStylingFormatterDiscovery extends
		AbstractTextStylingFormatterDiscovery {

	public DocxHTMLTextStylingFormatterDiscovery() {
		super(DocumentKind.DOCX, TextStylingKind.Html);
	}

	public ITextStylingFormatter getFormatter() {
		return DocxHTMLTextStylingFormatter.INSTANCE;
	}

	public String getDescription() {
		return "HTML text styling for docx.";
	}
}
