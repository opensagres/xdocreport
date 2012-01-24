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
package fr.opensagres.xdocreport.document.docx.textstyling;

import java.util.Stack;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.AbstractDocumentHandler;
import fr.opensagres.xdocreport.template.IContext;

/**
 * 
 * Document handler implementation to build docx fragment content.
 * 
 */
public class DocxDocumentHandler extends AbstractDocumentHandler {

	private boolean bolding;
	private boolean italicsing;
	private Stack<Boolean> paragraphsStack;

	public DocxDocumentHandler(BufferedElement parent, IContext context) {
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
		// startParagraphIfNeeded();
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
			internalStartParagraph(false);
		}
	}

	private void internalStartParagraph(boolean containerIsList) {
		writer.write("<w:p>");
		paragraphsStack.push(containerIsList);
	}

	private void internalEndParagraph() {
		writer.write("</w:p>");
		paragraphsStack.pop();
	}

	public void startListItem() {
		// if (!paragraphsStack.isEmpty() && !paragraphsStack.peek()) {
		// internalEndParagraph();
		// }
		internalStartParagraph(true);
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
		// writer.write(String.valueOf(numIdVal));
		writer.write(String.valueOf(numIdVal));
		writer.write("\" />");

		writer.write("</w:numPr>");
		writer.write("</w:pPr>");

	}

	public void endListItem() {
		internalEndParagraph();
	}

	public void startParagraph() {
		internalStartParagraph(false);
	}

	public void endParagraph() {
		internalEndParagraph();
	}

}
