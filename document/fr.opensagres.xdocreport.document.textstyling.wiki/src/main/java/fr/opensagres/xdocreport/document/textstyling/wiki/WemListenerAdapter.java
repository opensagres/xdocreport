/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.document.textstyling.wiki;

import java.util.List;

import org.wikimodel.wem.EmptyWemListener;
import org.wikimodel.wem.IWemConstants;
import org.wikimodel.wem.WikiFormat;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiStyle;

import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;

/**
 * Wiki Event Model Adaptor to call methods of {@link IDocumentHandler}.
 * 
 */
public class WemListenerAdapter extends EmptyWemListener {

	protected final IDocumentHandler documentHandler;

	public WemListenerAdapter(IDocumentHandler visitor) {
		this.documentHandler = visitor;
	}

	@Override
	public void beginDocument() {
		documentHandler.startDocument();
	}

	@Override
	public void endDocument() {
		documentHandler.endDocument();
	}

	@Override
	public void beginFormat(WikiFormat format) {
		List<WikiStyle> styles = format.getStyles();
		for (WikiStyle style : styles) {
			if (IWemConstants.STRONG.equals(style)) {
				documentHandler.startBold();
			} else if (IWemConstants.EM.equals(style)) {
				documentHandler.startItalics();
			}
		}
	}

	@Override
	public void endFormat(WikiFormat format) {
		List<WikiStyle> styles = format.getStyles();
		for (WikiStyle style : styles) {
			if (IWemConstants.STRONG.equals(style)) {
				documentHandler.endBold();
			} else if (IWemConstants.EM.equals(style)) {
				documentHandler.endItalics();
			}
		}
	}

	@Override
	public void beginList(WikiParameters params, boolean ordered) {
		if (ordered) {
			documentHandler.startOrderedList();
		} else {
			documentHandler.startUnorderedList();
		}
	}

	@Override
	public void beginListItem() {
		documentHandler.startListItem();
	}

	@Override
	public void endListItem() {
		documentHandler.endListItem();
	}

	@Override
	public void endList(WikiParameters params, boolean ordered) {
		if (ordered) {
			documentHandler.endOrderedList();
		} else {
			documentHandler.endUnorderedList();
		}
	}

	@Override
	public void onSpace(String str) {
		documentHandler.handleString(str);
	}

	@Override
	public void onWord(String str) {
		documentHandler.handleString(str);
	}

	@Override
	public void beginParagraph(WikiParameters params) {
		documentHandler.startParagraph();
	}

	@Override
	public void endParagraph(WikiParameters params) {
		documentHandler.endParagraph();
	}

}
