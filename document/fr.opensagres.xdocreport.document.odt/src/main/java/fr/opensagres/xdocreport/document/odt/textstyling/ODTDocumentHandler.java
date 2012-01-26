package fr.opensagres.xdocreport.document.odt.textstyling;

import java.io.IOException;
import java.util.Stack;

import fr.opensagres.xdocreport.document.odt.preprocessor.ODTBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.AbstractDocumentHandler;
import fr.opensagres.xdocreport.template.IContext;

public class ODTDocumentHandler extends AbstractDocumentHandler {

	private boolean bolding;
	private boolean italicsing;
	private Stack<Boolean> paragraphsStack;
	private boolean isHeader = false;

	public ODTDocumentHandler(BufferedElement parent, IContext context) {
		super(parent, context);
	}

	public void startDocument() {
		this.bolding = false;
		this.italicsing = false;
		this.paragraphsStack = new Stack<Boolean>();
	}

	public void endDocument() throws IOException {
		endParagraphIfNeeded();
	}

	private void endParagraphIfNeeded() throws IOException {
		if (!paragraphsStack.isEmpty()) {
			paragraphsStack.size();
			for (int i = 0; i < paragraphsStack.size(); i++) {
				internalEndParagraph();
			}
			paragraphsStack.clear();
		}
	}

	public void startBold() {
		this.bolding = true;
	}

	public void endBold() {
		this.bolding = false;
	}

	public void startItalics() {
		this.italicsing = true;
	}

	public void endItalics() {
		this.italicsing = false;
	}

	@Override
	public void handleString(String content) throws IOException {
		if (isHeader) {
			super.write(content);
		} else {
			super.write("<text:span");
			if (bolding || italicsing) {
				super.write(" text:style-name=\"");
				if (bolding && italicsing) {
					super.write(ODTBufferedDocumentContentHandler.BOLD_ITALIC_STYLE_NAME);
				} else if (italicsing) {
					super.write(ODTBufferedDocumentContentHandler.ITALIC_STYLE_NAME);
				} else if (bolding) {
					super.write(ODTBufferedDocumentContentHandler.BOLD_STYLE_NAME);
				}
				super.write("\" ");
			}
			super.write(">");
			super.write(content);
			super.write("</text:span>");
		}
	}

	public void startListItem() {
		// TODO Auto-generated method stub

	}

	public void endListItem() {
		// TODO Auto-generated method stub

	}

	public void startParagraph() throws IOException {
		super.setTextLocation(TextLocation.End);
		internalStartParagraph(false);
	}

	public void endParagraph() throws IOException {
		internalEndParagraph();
	}

	private void internalStartParagraph(boolean containerIsList)
			throws IOException {
		super.write("<text:p>");
		paragraphsStack.push(containerIsList);
	}

	private void internalEndParagraph() throws IOException {
		if (!paragraphsStack.isEmpty()) {
			super.write("</text:p>");
			paragraphsStack.pop();
		}
	}

	public void startHeading(int level) throws IOException {
		endParagraphIfNeeded();
		super.setTextLocation(TextLocation.End);
		super.write("<text:h text:style-name=\"Heading_20_" + level
				+ "\" text:outline-level=\"" + level + "\">");
		isHeader = true; // XXX nested Headers ?
	}

	public void endHeading(int level) throws IOException {
		super.write("</text:h>");
		isHeader = false;
		startParagraph();
	}

}
