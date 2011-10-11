package fr.opensagres.xdocreport.template.textstyling.wiki.mediawiki;

import fr.opensagres.xdocreport.template.TextStylingKind;
import fr.opensagres.xdocreport.template.discovery.ITextStylingFormatterDiscovery;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;

public class MediaWikiTextStylingFormatterDiscovery implements
		ITextStylingFormatterDiscovery {

	public String getId() {
		return TextStylingKind.MediaWiki.name();
	}

	public ITextStylingFormatter getFormatter() {
		return MediaWikiTextStylingFormatter.INSTANCE;
	}

	public String getDescription() {
		return "Media Wiki test styling";
	}

}
