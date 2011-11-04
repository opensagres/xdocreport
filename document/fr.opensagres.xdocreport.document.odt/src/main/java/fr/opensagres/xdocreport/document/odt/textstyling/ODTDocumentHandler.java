package fr.opensagres.xdocreport.document.odt.textstyling;

import java.util.Stack;

import fr.opensagres.xdocreport.document.odt.preprocessor.ODTBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.textstyling.AbstractDocumentHandler;

public class ODTDocumentHandler extends AbstractDocumentHandler {

	private boolean bolding;
	private boolean italicsing;
	private Stack<Boolean> paragraphsStack;

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
		// System.err.println(writer.getBuffer().toString());
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

}
