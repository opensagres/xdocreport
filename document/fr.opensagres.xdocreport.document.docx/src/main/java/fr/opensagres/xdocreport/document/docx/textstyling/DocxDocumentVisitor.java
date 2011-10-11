package fr.opensagres.xdocreport.document.docx.textstyling;

import fr.opensagres.xdocreport.template.textstyling.AbstractDocumentVisitor;

public class DocxDocumentVisitor extends AbstractDocumentVisitor {

	private boolean bolding;
	private boolean italicsing;

	public void startDocument() {
		this.bolding = false;
		this.italicsing = false;
		writer.write("<w:p>");
	}

	public void endDocument() {
		writer.write("</w:p>");
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

}
