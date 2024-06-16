/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax;

import static fr.opensagres.xdocreport.document.docx.DocxConstants.CX_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.CY_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.EMBED_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.FLDCHARTYPE_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.R_NS;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.W_NS;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isBlip;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isExt;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isExtent;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isFldChar;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isFldSimple;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isInstrText;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isT;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isR;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.xdocreport.document.images.AbstractImageRegistry;
import fr.opensagres.xdocreport.document.images.IImageRegistry;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.TemplateContextHelper;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * SAX content handler to generate lazy Freemarker/Velocity loop directive in
 * the table row which contains a list fields.
 */
public class DocXBufferedDocumentContentHandler extends
		TransformedBufferedDocumentContentHandler<DocxBufferedDocument> {

	private static final String W_TBL = "w:tbl";
	
	private static final String W_TR = "w:tr";

	private static final String W_TC = "w:tc";

	private boolean instrTextParsing;

	private boolean tParsing = false;

	private int tIndex = -1;

	protected DocXBufferedDocumentContentHandler(String entryName,
			FieldsMetadata fieldsMetadata, IDocumentFormatter formater,
			Map<String, Object> sharedContext) {
		super(entryName, fieldsMetadata, formater, sharedContext);
	}

	@Override
	protected DocxBufferedDocument createDocument() {
		return new DocxBufferedDocument(this);
	}
	
	@Override
	protected String getTableTableName() {
		return W_TBL;
	}

	@Override
	protected String getTableRowName() {
		return W_TR;
	}

	@Override
	protected String getTableCellName() {
		return W_TC;
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
			tIndex++;
			return super.doStartElement(uri, localName, name, attributes);
		}

		if (isR(uri, localName, name)) {
			tIndex = -1;
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
				int index = attributes.getIndex(R_NS, EMBED_ATTR);
				if (index >= 0) {
					// modify "embed" attribute with image script (Velocity,
					// Freemarker)
					// <a:blip
					// r:embed="${imageRegistry.getPath(___imageInfo,'rId5')}"
					// />
					String embed = attributes.getValue(index);
					String newEmbed = formatter
							.getFunctionDirective(
									TemplateContextHelper.IMAGE_REGISTRY_KEY,
									IImageRegistry.GET_PATH_METHOD,
									AbstractImageRegistry.IMAGE_INFO, "'"
											+ embed + "'");
					AttributesImpl attr = toAttributesImpl(attributes);
					attr.setValue(index, newEmbed);
					attributes = attr;
				}
			}
		} else if (isExtent(uri, localName, name)
				|| isExt(uri, localName, name)) {
			// <wp:extent cx="1262380" cy="1352550" />
			// OR
			// <a:ext cx="1262380" cy="1352550" />
			BookmarkBufferedRegion currentBookmark = bufferedDocument
					.getCurrentBookmark();
			if (currentBookmark != null && formatter != null) {
				// modify "cx" and "cy" attribute with image script (Velocity,
				// Freemarker)
				// <wp:extent
				// cx="${imageRegistry.getWidth(___imageInfo, '1262380', '1352550')}"
				// cy="${imageRegistry.getHeight(___imageInfo, '1262380', '1352550')}" />
				// "cx" and "cy" attributes can also be modified as follows
				// <wp:extent
				// cx="${imageRegistry.getWidth(___imageInfo, '1262380')}"
				// cy="${imageRegistry.getHeight(___imageInfo, '1352550')}" />
				String newCX = null;
				String newCY = null;
				String oldCX = null;
				String oldCY = null;
				int cxIndex = attributes.getIndex(CX_ATTR);
				if (cxIndex != -1) {
					oldCX = attributes.getValue(cxIndex);
				}
				int cyIndex = attributes.getIndex(CY_ATTR);
				if (cyIndex != -1) {
					oldCY = attributes.getValue(cyIndex);
				}

				// get the parameters for the get width and height methods
				String[] parameters = null;
				if (oldCX != null && oldCY != null) {
					parameters = new String[]{IImageRegistry.IMAGE_INFO, "'" + oldCX + "'", "'" + oldCY + "'"};
				} else if (oldCX != null) {
					parameters = new String[]{IImageRegistry.IMAGE_INFO, "'" + oldCX + "'"};
				} else if (oldCY != null) {
					parameters = new String[]{IImageRegistry.IMAGE_INFO, "'" + oldCY + "'"};
				}

				if (oldCX != null) {
					newCX = formatter.getFunctionDirective(
							TemplateContextHelper.IMAGE_REGISTRY_KEY,
							IImageRegistry.GET_WIDTH_METHOD,
							parameters);
				}
				if (oldCY != null) {
					newCY = formatter.getFunctionDirective(
							TemplateContextHelper.IMAGE_REGISTRY_KEY,
							IImageRegistry.GET_HEIGHT_METHOD,
							parameters);
				}

				if (newCX != null || newCY != null) {
					AttributesImpl attr = toAttributesImpl(attributes);
					if (newCX != null) {
						attr.setValue(cxIndex, newCX);
					}
					if (newCY != null) {
						attr.setValue(cyIndex, newCY);
					}
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
			// it's end of fldSimple and it's Mergefield=> ignore the element
			String fieldName = currentFldSimpleRegion.getFieldName();
			if (fieldName == null) {
				super.doEndElement(uri, localName, name);
			}
			return;
		}
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
				FieldMetadata fieldAsTextStyling = super
						.getFieldAsTextStyling(characters);
				characters = processRowIfNeeded(characters);
				currentRRegion.setInstrText(characters, fieldAsTextStyling);
				extractListDirectiveInfo(currentRRegion);
				resetCharacters();
				return;
			} else {
				if (tParsing) {
					currentRRegion.setTContent(tIndex, characters);
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
