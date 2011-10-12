package fr.opensagres.xdocreport.document.docx.textstyling;

import java.util.Stack;

import fr.opensagres.xdocreport.template.textstyling.AbstractDocumentVisitor;

public class DocxDocumentVisitor extends AbstractDocumentVisitor {

	private static int i = 0;
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
				endParagraph();
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
		startParagraphIfNeeded();
		writer.write("<w:r>");
		if (bolding || italicsing) {
			writer.write("<w:rPr>");
			if (bolding) {
				writer.write("<w:b />");
			}
			if (italicsing) {
				writer.write("<w:i />");
			}
			writer.write("</w:rPr>");
		}
		writer.write("<w:t xml:space=\"preserve\" >");
		writer.write(content);
		writer.write("</w:t>");
		writer.write("</w:r>");
	}

	private void startParagraphIfNeeded() {
		if (paragraphsStack.isEmpty()) {
			startParagraph(false);
		}
	}

	private void startParagraph(boolean containerIsList) {
		writer.write("<w:p>");
		paragraphsStack.push(containerIsList);
	}

	private void endParagraph() {
		writer.write("</w:p>");
		paragraphsStack.pop();
	}

	public void startListItem() {
		if (!paragraphsStack.isEmpty() && !paragraphsStack.peek()) {
			endParagraph();
		}
		startParagraph(true);
		boolean ordered = super.getCurrentListOrder();
		writer.write("<w:pPr>");
		writer.write("<w:pStyle w:val=\"Paragraphedeliste\" />");
		writer.write("<w:numPr>");

		// <w:ilvl w:val="0" />
		int ilvlVal = super.getCurrentListIndex();
		writer.write("<w:ilvl w:val=\"");
		writer.write(String.valueOf(ilvlVal));
		writer.write("\" />");

		// "<w:numId w:val="1" />"
		int numIdVal = ordered ? 2 : 1;
		writer.write("<w:numId w:val=\"");
		//writer.write(String.valueOf(numIdVal));
		writer.write(String.valueOf(5));
		writer.write("\" />");

		writer.write("</w:numPr>");
		writer.write("</w:pPr>");

	}

	public void endListItem() {
		endParagraph();
	}

}
