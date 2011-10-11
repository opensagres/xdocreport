package fr.opensagres.xdocreport.template.textstyling.wiki.gwiki;

import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.gwiki.GWikiParser;

import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;
import fr.opensagres.xdocreport.template.textstyling.wiki.AbstractWikiTextStylingFormatter;

public class GWikiTextStylingFormatter extends AbstractWikiTextStylingFormatter {

	public static final ITextStylingFormatter INSTANCE = new GWikiTextStylingFormatter();

	@Override
	protected IWikiParser createWikiParser() {
		return new GWikiParser();
	}
}
