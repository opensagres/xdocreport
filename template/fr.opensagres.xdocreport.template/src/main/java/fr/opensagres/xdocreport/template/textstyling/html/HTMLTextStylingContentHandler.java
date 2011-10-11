package fr.opensagres.xdocreport.template.textstyling.html;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;

public class HTMLTextStylingContentHandler extends DefaultHandler {

	private final IDocumentVisitor visitor;

	public HTMLTextStylingContentHandler(IDocumentVisitor visitor) {
		this.visitor = visitor;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		visitor.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		visitor.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if ("strong".equals(name) || "b".equals(name)) {
			visitor.startBold();
		} else if ("em".equals(name) || "i".equals(name)) {
			visitor.startItalics();
		}
		super.startElement(uri, localName, name, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if ("strong".equals(name) || "b".equals(name)) {
			visitor.endBold();
		} else if ("em".equals(name) || "i".equals(name)) {
			visitor.endItalics();
		}
		super.endElement(uri, localName, name);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		visitor.handleString(String.valueOf(ch, start, length));
		super.characters(ch, start, length);
	}

}
