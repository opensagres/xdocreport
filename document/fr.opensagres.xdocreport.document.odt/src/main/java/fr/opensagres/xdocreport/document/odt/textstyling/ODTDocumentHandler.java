package fr.opensagres.xdocreport.document.odt.textstyling;

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

	public void endDocument() {
		if (!paragraphsStack.isEmpty()) {
			paragraphsStack.size();
			for (int i = 0; i < paragraphsStack.size(); i++) {
				internalEndParagraph();
			}
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
	public void handleString(String content) {
		if (isHeader) {
			writer.write(content);
		} else {
			writer.write("<text:span");
			if (bolding || italicsing) {
				writer.write(" text:style-name=\"");
				if (bolding && italicsing) {
					writer.write(ODTBufferedDocumentContentHandler.BOLD_ITALIC_STYLE_NAME);
				} else if (italicsing) {
					writer.write(ODTBufferedDocumentContentHandler.ITALIC_STYLE_NAME);
				} else if (bolding) {
					writer.write(ODTBufferedDocumentContentHandler.BOLD_STYLE_NAME);
				}
				writer.write("\" ");
			}
			writer.write(">");
			writer.write(content);
			writer.write("</text:span>");
		}
	}

	public void startListItem() {
		// TODO Auto-generated method stub

	}

	public void endListItem() {
		// TODO Auto-generated method stub

	}

	public void startParagraph() {
		internalStartParagraph(false);
	}

	public void endParagraph() {
		internalEndParagraph();
	}

	private void internalStartParagraph(boolean containerIsList) {
		writer.write("<text:span>");
		paragraphsStack.push(containerIsList);
	}

	private void internalEndParagraph() {
		writer.write("</text:span>");
		paragraphsStack.pop();
	}

	public void startHeading(int level) {
		// writer.write("<text:span text:style-name=\"" +
		// PARAGRAPH_AUTOBREAK_START + "\">  </text:span>");
		writer.write("<text:h text:style-name=\"Heading_20_" + level
				+ "\" text:outline-level=\"" + level + "\">");
		isHeader = true; // XXX nested Headers ?
	}

	public void endHeading(int level) {
		writer.write("</text:h>");
		// writer.write("<text:span text:style-name=\"" +
		// PARAGRAPH_AUTOBREAK_END + "\">  </text:span>");
		isHeader = false;
	}

}
