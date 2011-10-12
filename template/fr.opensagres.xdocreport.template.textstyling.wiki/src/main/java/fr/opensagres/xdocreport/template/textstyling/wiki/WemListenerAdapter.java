package fr.opensagres.xdocreport.template.textstyling.wiki;

import java.util.List;

import org.wikimodel.wem.EmptyWemListener;
import org.wikimodel.wem.IWemConstants;
import org.wikimodel.wem.WikiFormat;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiStyle;

import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;

public class WemListenerAdapter extends EmptyWemListener {

	protected final IDocumentVisitor visitor;

	public WemListenerAdapter(IDocumentVisitor visitor) {
		this.visitor = visitor;
	}

	@Override
	public void beginDocument() {
		visitor.startDocument();
	}

	@Override
	public void endDocument() {
		visitor.endDocument();
	}

	@Override
	public void beginFormat(WikiFormat format) {
		List<WikiStyle> styles = format.getStyles();
		for (WikiStyle style : styles) {
			if (IWemConstants.STRONG.equals(style)) {
				visitor.startBold();
			} else if (IWemConstants.EM.equals(style)) {
				visitor.startItalics();
			}
		}
	}

	@Override
	public void endFormat(WikiFormat format) {
		List<WikiStyle> styles = format.getStyles();
		for (WikiStyle style : styles) {
			if (IWemConstants.STRONG.equals(style)) {
				visitor.endBold();
			} else if (IWemConstants.EM.equals(style)) {
				visitor.endItalics();
			}
		}
	}

	@Override
	public void beginList(WikiParameters params, boolean ordered) {
		if (ordered) {
			visitor.startOrderedList();
		} else {
			visitor.startUnorderedList();
		}
	}

	@Override
	public void beginListItem() {
		visitor.startListItem();
	}
	
	@Override
	public void endListItem() {
		visitor.endListItem();
	}
	
	@Override
	public void endList(WikiParameters params, boolean ordered) {
		if (ordered) {
			visitor.endOrderedList();
		} else {
			visitor.endUnorderedList();
		}
	}

	@Override
	public void onSpace(String str) {
		visitor.handleString(str);
	}

	@Override
	public void onWord(String str) {
		visitor.handleString(str);
	}

}
