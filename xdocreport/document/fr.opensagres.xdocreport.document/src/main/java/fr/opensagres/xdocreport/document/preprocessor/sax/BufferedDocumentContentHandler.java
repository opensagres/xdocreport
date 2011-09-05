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
package fr.opensagres.xdocreport.document.preprocessor.sax;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import fr.opensagres.xdocreport.core.EncodingConstants;
import fr.opensagres.xdocreport.core.utils.StringUtils;

/**
 * SAX Content Handler which build a {@link BufferedDocument} from the XML
 * source stream.
 * 
 */
public class BufferedDocumentContentHandler extends DefaultHandler implements
		EncodingConstants {

	private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
	public static final String CDATA_TYPE = "CDATA";

	private final List<PrefixMapping> prefixs;

	protected final BufferedDocument bufferedDocument;
	protected IBufferedRegion currentRegion;
	private final StringBuilder currentCharacters = new StringBuilder();
	private boolean startingElement = false;

	public BufferedDocumentContentHandler() {
		this.bufferedDocument = new BufferedDocument();
		this.prefixs = new ArrayList<PrefixMapping>();
	}

	public BufferedDocument getBufferedDocument() {
		return bufferedDocument;
	}

	@Override
	public void startDocument() throws SAXException {
		this.currentRegion = this.bufferedDocument;
		this.currentRegion.append(XML_DECLARATION);
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		String xmlnsPrefix = StringUtils.isEmpty(prefix) ? "xmlns" : "xmlns:"
				+ prefix;
		prefixs.add(new PrefixMapping(xmlnsPrefix, uri));
	}

	@Override
	public final void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (startingElement) {
			currentRegion.append(">");
		}
		if (currentCharacters.length() > 0) {
			flushCharacters(currentCharacters.toString());
			resetCharacters();
		}
		startingElement = doStartElement(uri, localName, name, attributes);
	}

	public boolean doStartElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		currentRegion.append("<");
		currentRegion.append(name);
		String attrName = null;
		String attrValue = null;
		// prefix mapping
		if (prefixs.size() > 0) {
			for (PrefixMapping prefix : prefixs) {
				attrName = prefix.getPrefix();
				attrValue = prefix.getURI();
				currentRegion.append(' ');
				currentRegion.append(attrName);
				currentRegion.append("=\"");
				currentRegion.append(attrValue);
				currentRegion.append("\"");
			}
			prefixs.clear();
		}
		// attributes
		int length = attributes.getLength();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				currentRegion.append(' ');
				attrName = attributes.getQName(i);
				attrValue = attributes.getValue(i);
				currentRegion.append(attrName);
				currentRegion.append("=\"");
				currentRegion.append(attrValue);
				currentRegion.append("\"");
			}
		}
		return true;
	}

	@Override
	public final void endElement(String uri, String localName, String name)
			throws SAXException {
		if (currentCharacters.length() > 0) {
			flushCharacters(currentCharacters.toString());
			resetCharacters();
		}
		if (startingElement) {
			currentRegion.append("/>");
			startingElement = false;
		} else {
			doEndElement(uri, localName, name);
		}
	}

	public void doEndElement(String uri, String localName, String name)
			throws SAXException {

		currentRegion.append("</");
		currentRegion.append(name);
		currentRegion.append(">");
	}

	@Override
	public final void characters(char[] ch, int start, int length)
			throws SAXException {
		if (startingElement) {
			currentRegion.append(">");
		}
		startingElement = false;
		char c;
		for (int i = start; i < start + length; i++) {
			c = ch[i];
			if (c == '<') {
				currentCharacters.append(LT);
			} else if (c == '>') {
				currentCharacters.append(GT);
			} else if (c == '\'') {
				currentCharacters.append(APOS);
			} else {
				currentCharacters.append(c);
			}
		}
	}

	protected void flushCharacters(String characters) {
		currentRegion.append(characters);
	}

	protected void resetCharacters() {
		currentCharacters.setLength(0);
	}

	/**
	 * Get the SAX {@link AttributesImpl} of teh given attributes to modify
	 * attribute values.
	 * 
	 * @param attributes
	 * @return
	 */
	public static AttributesImpl toAttributesImpl(Attributes attributes) {
		if (attributes instanceof AttributesImpl) {
			return (AttributesImpl) attributes;
		}
		// Another SAX Implementation, create a new instance.
		AttributesImpl attributesImpl = new AttributesImpl();
		int length = attributes.getLength();
		for (int i = 0; i < length; i++) {
			attributesImpl.addAttribute(attributes.getURI(i),
					attributes.getLocalName(i), attributes.getQName(i),
					attributes.getType(i), attributes.getValue(i));
		}
		return attributesImpl;
	}
}
