package fr.opensagres.xdocreport.template.discovery;

import fr.opensagres.xdocreport.template.TextStylingKind;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;
import fr.opensagres.xdocreport.template.textstyling.html.HTMLTextStylingFormatter;

public class HTMLTextStylingFormatterDiscovery implements
		ITextStylingFormatterDiscovery {

	public String getId() {
		return TextStylingKind.Html.name();
	}

	public ITextStylingFormatter getFormatter() {
		return HTMLTextStylingFormatter.INSTANCE;
	}

	public String getDescription() {
		return "HTML test styling";
	}

}
