package fr.opensagres.xdocreport.template.textstyling.wiki.gwiki;

import fr.opensagres.xdocreport.template.TextStylingKind;
import fr.opensagres.xdocreport.template.discovery.ITextStylingFormatterDiscovery;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;

public class GWikiTextStylingFormatterDiscovery implements
		ITextStylingFormatterDiscovery {

	public String getId() {
		return TextStylingKind.GWiki.name();
	}

	public ITextStylingFormatter getFormatter() {
		return GWikiTextStylingFormatter.INSTANCE;
	}

	public String getDescription() {
		return "Google Wiki test styling";
	}

}
