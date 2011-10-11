package fr.opensagres.xdocreport.document.odt.discovery;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTHTMLTextStylingFormatter;
import fr.opensagres.xdocreport.template.TextStylingKind;
import fr.opensagres.xdocreport.template.discovery.AbstractTextStylingFormatterDiscovery;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;

public class ODTHTMLTextStylingFormatterDiscovery extends
		AbstractTextStylingFormatterDiscovery {

	public ODTHTMLTextStylingFormatterDiscovery() {
		super(DocumentKind.ODT, TextStylingKind.Html);
	}

	public ITextStylingFormatter getFormatter() {
		return ODTHTMLTextStylingFormatter.INSTANCE;
	}

	public String getDescription() {
		return "HTML text styling for ODT.";
	}
}
