package fr.opensagres.xdocreport.document.odt.textstyling;

import fr.opensagres.xdocreport.template.textstyling.AbstractDocumentVisitor;

public class ODTDocumentVisitor extends AbstractDocumentVisitor {

	private boolean bolding;
	private boolean italicsing;

	public void startDocument() {
		this.bolding = false;
		this.italicsing = false;
		// writer.write("<text:p>");
	}

	public void endDocument() {
		// writer.write("</text:p>");
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
				writer.write("XDocReport_BoldItalic");
			} else if (italicsing) {
				writer.write("XDocReport_Italic");
			} else if (bolding) {
				writer.write("XDocReport_Bold");
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

}
