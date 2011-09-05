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
package fr.opensagres.xdocreport.document.docx.preprocessor;

import static fr.opensagres.xdocreport.document.docx.DocxUtils.isBlip;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isBookmarkEnd;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isBookmarkStart;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isFldChar;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isFldSimple;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isHyperlink;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isInstrText;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isP;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isR;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isT;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.DocXConstants;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * SAX content handler to generate lazy Freemarker/Velocity loop directive in
 * the table row which contains a list fields.
 */
public class DocXBufferedDocumentContentHandler extends
		TransformedBufferedDocumentContentHandler implements DocXConstants {

	private static final String NAME_ATTR = "name";

	private RBufferedRegion currentRRegion = null;
	private PBufferedRegion currentPRegion = null;
	private FldSimpleBufferedRegion currentFldSimpleRegion = null;
	private boolean instrTextParsing;
	private boolean tParsing = false;

	private BookmarkBufferedRegion currentBookmark;

	protected DocXBufferedDocumentContentHandler(FieldsMetadata fieldsMetadata,
			IDocumentFormatter formater, Map<String, Object> context) {
		super(fieldsMetadata, formater, context);
	}

	@Override
	protected boolean isTable(String uri, String localName, String name) {
		return W_NS.equals(uri) && TBL_ELT.equals(localName);
	}

	@Override
	protected boolean isRow(String uri, String localName, String name) {
		return W_NS.equals(uri) && TR_ELT.equals(localName);
	}

	@Override
	public boolean doStartElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		FieldsMetadata fieldsMetadata = super.getFieldsMetadata();
		IDocumentFormatter formatter = super.getFormatter();
		// Transform mergefield name WordML code with just name of name
		// Merge field is represent with w:fldSimple or w:instrText (complex
		// field). See
		// http://www.documentinteropinitiative.org/implnotes/ecma-376/812d4aca-3071-4352-872a-ca21d65ec913.aspx

		if (isP(uri, localName, name)) {
			// w:p element
			currentPRegion = new PBufferedRegion(currentRegion);
			currentRegion = currentPRegion;
			return super.doStartElement(uri, localName, name, attributes);
		}

		if (isR(uri, localName, name) && currentFldSimpleRegion == null) {
			// w:r element
			currentRRegion = new RBufferedRegion(currentRegion);
			currentRegion = currentRRegion;
			return super.doStartElement(uri, localName, name, attributes);
		}

		if (isFldChar(uri, localName, name) && currentRRegion != null) {
			// w:fdlChar element
			String fldCharType = attributes.getValue(W_NS, FLDCHARTYPE_ATTR);
			currentRRegion.setFldCharType(fldCharType);
			return super.doStartElement(uri, localName, name, attributes);
		}

		if (isInstrText(uri, localName, name) && currentRRegion != null) {
			// w:instrText element
			instrTextParsing = true;
			return super.doStartElement(uri, localName, name, attributes);
		}

		if (isT(uri, localName, name)) {
			// w:t element
			tParsing = true;
			return super.doStartElement(uri, localName, name, attributes);
		}

		if (isFldSimple(uri, localName, name)) {
			// w:fldSimple element
			// start of fldSimple mergefield, add the fieldName of mergefield
			// and ignore element
			String instrText = processRowIfNeeded(attributes.getValue(W_NS,
					INSTR_ATTR));
			currentFldSimpleRegion = new FldSimpleBufferedRegion(currentRegion);
			currentFldSimpleRegion.setInstrText(instrText);
			boolean addElement = false;
			if (currentFldSimpleRegion.getFieldName() == null) {
				super.doStartElement(uri, localName, name, attributes);
				addElement = true;
			}
			currentRegion = currentFldSimpleRegion;
			return addElement;
		}

		if (isBookmarkStart(uri, localName, name)) {
			// <w:bookmarkStart w:id="0" w:name="logo" />
			String bookmarkName = attributes.getValue(W_NS, NAME_ATTR);
			if (fieldsMetadata != null) {
				String imageFieldName = fieldsMetadata
						.getImageFieldName(bookmarkName);
				if (imageFieldName != null) {
					currentBookmark = new BookmarkBufferedRegion(bookmarkName,
							imageFieldName, currentRegion);
					currentRegion = currentBookmark;
				}
			}
			return super.doStartElement(uri, localName, name, attributes);
		}

		if (isBookmarkEnd(uri, localName, name)) {
			// w:bookmarkEnd
			boolean result = super.doStartElement(uri, localName, name,
					attributes);
			if (currentBookmark != null) {
				currentRegion = currentBookmark.getParent();
			}
			currentBookmark = null;
			return result;
		}

		if (isBlip(uri, localName, name)) {
			// <a:blip r:embed="rId5" />
			if (currentBookmark != null && formatter != null) {
				// modify "embed" attribute with image script (Velocity,
				// Freemarker)
				// <a:blip
				// r:embed="${imageRegistry.registerImage($bookmarkName)}" />
				String newEmbed = formatter
						.getImageDirective(processRowIfNeeded(
								currentBookmark.getImageFieldName(), true));
				if (StringUtils.isNotEmpty(newEmbed)) {
					AttributesImpl attr = toAttributesImpl(attributes);
					int index = attr.getIndex(R_NS, EMBED_ATTR);
					attr.setValue(index, newEmbed);
					attributes = attr;
				}
			}
		} else if (isHyperlink(uri, localName, name)) {
			// <w:hyperlink r:id="rId5" w:history="1">
			HyperlinkBufferedRegion hyperlink = new HyperlinkBufferedRegion(
					this, currentRegion);
			currentRegion = hyperlink;
			int idIndex = attributes.getIndex(R_NS, ID_ATTR);
			if (idIndex != -1) {
				String attrName = attributes.getQName(idIndex);
				AttributesImpl attributesImpl = toAttributesImpl(attributes);
				attributesImpl.removeAttribute(idIndex);
				super.doStartElement(uri, localName, name, attributesImpl);
				String id = attributes.getValue(idIndex);
				hyperlink.setId(attrName, id);
				return true;
			}
			return super.doStartElement(uri, localName, name, attributes);
		}

		// Another element
		return super.doStartElement(uri, localName, name, attributes);

	}

	@Override
	public void doEndElement(String uri, String localName, String name)
			throws SAXException {

		if (isP(uri, localName, name) && currentPRegion != null) {
			super.doEndElement(uri, localName, name);
			currentPRegion.process();
			currentRegion = currentPRegion.getParent();
			currentPRegion = null;
			return;
		}

		if (isR(uri, localName, name) && currentRRegion != null
				&& currentFldSimpleRegion == null) {
			super.doEndElement(uri, localName, name);
			currentRegion = currentRRegion.getParent();
			currentRRegion = null;
			return;
		}

		if (isInstrText(uri, localName, name) && currentRRegion != null) {
			super.doEndElement(uri, localName, name);
			instrTextParsing = false;
			return;
		}

		if (isT(uri, localName, name)) {
			super.doEndElement(uri, localName, name);
			tParsing = false;
			return;
		}

		if (isFldSimple(uri, localName, name) && currentFldSimpleRegion != null) {
			// it's end of fldSimple and it's Mergefield; ignore the element
			if (currentFldSimpleRegion.getFieldName() == null) {
				super.doEndElement(uri, localName, name);
			}
			currentRegion = currentFldSimpleRegion.getParent();
			currentFldSimpleRegion = null;
			return;
		}

		if (isHyperlink(uri, localName, name)) {
			// </w:hyperlink>
			HyperlinkBufferedRegion hyperlink = (HyperlinkBufferedRegion) currentRegion;
			super.doEndElement(uri, localName, name);
			hyperlink.process();
			currentRegion = hyperlink.getParent();
			return;
		}
		super.doEndElement(uri, localName, name);
	}

	@Override
	protected void flushCharacters(String characters) {
		if (tParsing && currentFldSimpleRegion != null) {
			// fldSimple mergefield is parsing, replace with field name.
			currentFldSimpleRegion.setTContent(characters);
			resetCharacters();
			return;
		}

		if (currentRRegion != null) {
			if (instrTextParsing) {
				currentRRegion.setInstrText(processRowIfNeeded(characters));
				resetCharacters();
				return;
			} else {
				if (tParsing) {
					currentRRegion.setTContent(characters);
					resetCharacters();
					return;
				}
			}
		}

		super.flushCharacters(characters);
	}
}
