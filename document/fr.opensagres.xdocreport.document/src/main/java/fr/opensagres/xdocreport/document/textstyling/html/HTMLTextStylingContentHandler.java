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
package fr.opensagres.xdocreport.document.textstyling.html;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;

/**
 * SAX content handler used to parse HTML content and call teh well method of
 * {@link IDocumentHandler} according the HTML content.
 * 
 */
public class HTMLTextStylingContentHandler extends DefaultHandler {

	// HTML elements for Bold style
	private static final String STRONG_ELT = "strong";
	private static final String B_ELT = "b";

	// HTML elements for Italic style
	private static final String EM_ELT = "em";
	private static final String I_ELT = "i";

	// HTML elements for list
	private static final String OL_ELT = "ol";
	private static final String UL_ELT = "ul";
	private static final String LI_ELT = "li";

	// HTML elements for paragraph
	private static final String P_ELT = "p";

	private final IDocumentHandler documentHandler;

	public HTMLTextStylingContentHandler(IDocumentHandler visitor) {
		this.documentHandler = visitor;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		documentHandler.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		documentHandler.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (STRONG_ELT.equals(name) || B_ELT.equals(name)) {
			// Bold
			documentHandler.startBold();
		} else if (EM_ELT.equals(name) || I_ELT.equals(name)) {
			// Italic
			documentHandler.startItalics();
		} else if (UL_ELT.equals(name)) {
			// Unordered List
			startList(false);
		} else if (OL_ELT.equals(name)) {
			// Orderer List
			startList(true);
		} else if (LI_ELT.equals(name)) {
			// List item
			documentHandler.startListItem();
		} else if (P_ELT.equals(name)) {
			// Paragraph
			documentHandler.startParagraph();
		}
		super.startElement(uri, localName, name, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (STRONG_ELT.equals(name) || B_ELT.equals(name)) {
			// Bold
			documentHandler.endBold();
		} else if (EM_ELT.equals(name) || I_ELT.equals(name)) {
			// Italic
			documentHandler.endItalics();
		} else if (UL_ELT.equals(name)) {
			// Unordered List
			endList(false);
		} else if (OL_ELT.equals(name)) {
			// Orderer List
			endList(true);
		} else if (LI_ELT.equals(name)) {
			// List item
			documentHandler.endListItem();
		} else if (P_ELT.equals(name)) {
			// Paragraph
			documentHandler.endParagraph();
		}
		super.endElement(uri, localName, name);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		documentHandler.handleString(String.valueOf(ch, start, length));
		super.characters(ch, start, length);
	}

	private void startList(boolean ordered) {
		if (ordered) {
			documentHandler.startOrderedList();
		} else {
			documentHandler.startUnorderedList();
		}
	}

	private void endList(boolean ordered) {
		if (ordered) {
			documentHandler.endOrderedList();
		} else {
			documentHandler.endUnorderedList();
		}
	}
}
