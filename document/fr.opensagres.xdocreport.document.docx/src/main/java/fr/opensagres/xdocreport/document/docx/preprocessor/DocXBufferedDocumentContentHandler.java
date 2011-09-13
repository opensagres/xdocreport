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
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isFldChar;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isFldSimple;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isInstrText;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isT;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.DocXConstants;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * SAX content handler to generate lazy Freemarker/Velocity loop directive in
 * the table row which contains a list fields.
 */
public class DocXBufferedDocumentContentHandler extends
		TransformedBufferedDocumentContentHandler<DocxBufferedDocument>
		implements DocXConstants {

	private boolean instrTextParsing;
	private boolean tParsing = false;
	
	protected DocXBufferedDocumentContentHandler(FieldsMetadata fieldsMetadata,
			IDocumentFormatter formater, Map<String, Object> context) {
		super(fieldsMetadata, formater, context);
	}

	@Override
	protected DocxBufferedDocument createDocument() {
		return new DocxBufferedDocument(this);
	}

	@Override
	protected String getTableRowName() {
		return "w:tr";
	}

	@Override
	protected String getTableCellName() {
		return "w:tc";
	}

	@Override
	public boolean doStartElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		
		IDocumentFormatter formatter = super.getFormatter();
		
		// Transform mergefield name WordML code with just name of name
		// Merge field is represent with w:fldSimple or w:instrText (complex
		// field). See
		// http://www.documentinteropinitiative.org/implnotes/ecma-376/812d4aca-3071-4352-872a-ca21d65ec913.aspx
		
		RBufferedRegion currentRRegion = bufferedDocument.getCurrentRRegion();
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
			FldSimpleBufferedRegion currentFldSimpleRegion = bufferedDocument
					.getCurrentFldSimpleRegion();
			if (currentFldSimpleRegion.getFieldName() == null) {
				super.doStartElement(uri, localName, name, attributes);
				return true;
			}
			return false;
		}

		if (isBlip(uri, localName, name)) {
			BookmarkBufferedRegion currentBookmark = bufferedDocument
					.getCurrentBookmark();
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
		}
		// Another element
		return super.doStartElement(uri, localName, name, attributes);

	}

	@Override
	public void doEndElement(String uri, String localName, String name)
			throws SAXException {
		IBufferedRegion currentRegion = getCurrentElement();
		// if (isP(uri, localName, name) && currentPRegion != null) {
		// super.doEndElement(uri, localName, name);
		// currentPRegion.process();
		// currentRegion = currentPRegion.getParent();
		// currentPRegion = null;
		// return;
		// }
		//
		// if (isR(uri, localName, name) && currentRRegion != null
		// && currentFldSimpleRegion == null) {
		// super.doEndElement(uri, localName, name);
		// currentRegion = currentRRegion.getParent();
		// currentRRegion = null;
		// return;
		// }

		RBufferedRegion currentRRegion = bufferedDocument.getCurrentRRegion();
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

		FldSimpleBufferedRegion currentFldSimpleRegion = bufferedDocument
				.getCurrentFldSimpleRegion();
		if (isFldSimple(uri, localName, name) && currentFldSimpleRegion != null) {
			// it's end of fldSimple and it's Mergefield; ignore the element
			String fieldName = currentFldSimpleRegion.getFieldName();
			if (fieldName == null) {
				super.doEndElement(uri, localName, name);
			}
			return;
		}
		//
		// if (isHyperlink(uri, localName, name)) {
		// // </w:hyperlink>
		// HyperlinkBufferedRegion hyperlink = (HyperlinkBufferedRegion)
		// currentRegion;
		// super.doEndElement(uri, localName, name);
		// hyperlink.process();
		// currentRegion = hyperlink.getParent();
		// return;
		// }
//		if (isTableRow(uri, localName, name)) {
//			// remove list directive if needed
//			if (nbLoopDirectiveToRemove > 0) {
//				for (int i = 0; i < nbLoopDirectiveToRemove; i++) {
//					if (!getDirectives().isEmpty()) {
//						getDirectives().pop();
//
//					}
//				}
//				nbLoopDirectiveToRemove = 0;
//			}
//		}
		super.doEndElement(uri, localName, name);
	}

	@Override
	protected void flushCharacters(String characters) {
		FldSimpleBufferedRegion currentFldSimpleRegion = bufferedDocument
				.getCurrentFldSimpleRegion();
		if (tParsing && currentFldSimpleRegion != null) {
			// fldSimple mergefield is parsing, replace with field name.
			currentFldSimpleRegion.setTContent(characters);
			extractListDirectiveInfo(currentFldSimpleRegion);
			resetCharacters();
			return;
		}

		RBufferedRegion currentRRegion = bufferedDocument.getCurrentRRegion();
		if (currentRRegion != null) {
			if (instrTextParsing) {
				characters = processRowIfNeeded(characters);
				currentRRegion.setInstrText(characters);
				extractListDirectiveInfo(currentRRegion);
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

	private void extractListDirectiveInfo(MergefieldBufferedRegion mergefield) {
		super.extractListDirectiveInfo(mergefield.getFieldName());
	}
}
