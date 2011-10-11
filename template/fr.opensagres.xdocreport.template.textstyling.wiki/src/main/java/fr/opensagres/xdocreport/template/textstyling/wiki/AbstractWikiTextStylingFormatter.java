package fr.opensagres.xdocreport.template.textstyling.wiki;

import java.io.StringReader;

import org.wikimodel.wem.IWikiParser;

import fr.opensagres.xdocreport.template.textstyling.AbstractTextStylingFormatter;
import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;

public abstract class AbstractWikiTextStylingFormatter extends
		AbstractTextStylingFormatter {

	@Override
	protected void doFormat(String s, IDocumentVisitor visitor)
			throws Exception {
		IWikiParser parser = createWikiParser();
		parser.parse(new StringReader(s), new WemListenerAdapter(visitor));
	}

	protected abstract IWikiParser createWikiParser();

}
