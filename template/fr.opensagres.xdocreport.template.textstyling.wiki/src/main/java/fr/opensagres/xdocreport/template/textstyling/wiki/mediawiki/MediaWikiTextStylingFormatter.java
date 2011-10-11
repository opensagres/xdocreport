package fr.opensagres.xdocreport.template.textstyling.wiki.mediawiki;

import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.mediawiki.MediaWikiParser;

import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;
import fr.opensagres.xdocreport.template.textstyling.wiki.AbstractWikiTextStylingFormatter;

public class MediaWikiTextStylingFormatter extends
		AbstractWikiTextStylingFormatter {

	public static final ITextStylingFormatter INSTANCE = new MediaWikiTextStylingFormatter();

	@Override
	protected IWikiParser createWikiParser() {
		return new MediaWikiParser();
	}
}
