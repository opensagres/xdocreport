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
package fr.opensagres.xdocreport.document.odt.preprocessor;

import static fr.opensagres.xdocreport.document.odt.ODTUtils.isDrawFrame;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isDrawImage;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isOfficeAutomaticStyles;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isTextA;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isTextInput;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.odt.ODTConstants;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITransformResult;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * SAX content handler to generate lazy Freemarker/Velocity loop directive in
 * the table row which contains a list fields.
 */
public class ODTBufferedDocumentContentHandler extends
		TransformedBufferedDocumentContentHandler<ODTBufferedDocument>
		implements ODTConstants {

	public static final String BOLD_STYLE_NAME = "XDocReport_Bold";
	public static final String ITALIC_STYLE_NAME = "XDocReport_Italic";
	public static final String BOLD_ITALIC_STYLE_NAME = "XDocReport_BoldItalic";

	private String dynamicImageName;
	private boolean textInputParsing = false;
	private int variableIndex = 0;

	protected ODTBufferedDocumentContentHandler(FieldsMetadata fieldsMetadata,
			IDocumentFormatter formatter, Map<String, Object> sharedContext) {
		super(fieldsMetadata, formatter, sharedContext);
	}

	@Override
	protected ODTBufferedDocument createDocument() {
		return new ODTBufferedDocument();
	}

	@Override
	public boolean doStartElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		FieldsMetadata fieldsMetadata = super.getFieldsMetadata();
		IDocumentFormatter formatter = super.getFormatter();
		if (isTextInput(uri, localName, name)) {
			// Ignore element start text:text-input
			this.textInputParsing = true;
			return false;
		}
		if (isTextA(uri, localName, name)) {
			// <text:a xlink:type="simple"
			// xlink:href="mailto:$developers.Mail">$developers.Mail</text:a>
			if (fieldsMetadata != null && formatter != null) {
				String href = attributes.getValue(XLINK_NS, HREF_ATTR);
				if (StringUtils.isNotEmpty(href)) {
					String newHref = processRowIfNeeded(StringUtils
							.decode(href));
					if (newHref != null) {
						AttributesImpl attributesImpl = toAttributesImpl(attributes);
						int index = attributesImpl
								.getIndex(XLINK_NS, HREF_ATTR);
						attributesImpl.setValue(index, newHref);
						attributes = attributesImpl;
					}
				}
			}
		} else if (isDrawFrame(uri, localName, name)) {
			/*
			 * <draw:frame draw:style-name="fr1" draw:name="images1"
			 * text:anchor-type="paragraph" svg:x="69.96pt" svg:y="18.31pt"
			 * svg:width="21pt" svg:height="22.51pt" draw:z-index="0">
			 * <draw:image
			 * xlink:href="Pictures/100000000000001C0000001EE8812A78.png"
			 * xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad" />
			 * </draw:frame>
			 */
			if (fieldsMetadata != null) {
				String drawName = attributes.getValue(DRAW_NS, DRAW_NAME_ATTR);
				String imageFieldName = fieldsMetadata
						.getImageFieldName(drawName);
				if (imageFieldName != null) {
					dynamicImageName = processRowIfNeeded(imageFieldName, true);
					if (dynamicImageName != null && formatter != null) {
						// Modify svg:width="21pt" svg:height="22.51pt" with
						// Freemarker/Velocity directive
						//
						String newWith = null;
						String newHeight = null;
						int widthIndex = attributes
								.getIndex(SWG_NS, WIDTH_ATTR);
						if (widthIndex != -1) {
							String defaultWidth = attributes
									.getValue(widthIndex);
							newWith = formatter.getImageWidthDirective(
									dynamicImageName, defaultWidth);
						}
						int heightIndex = attributes.getIndex(SWG_NS,
								HEIGHT_ATTR);
						if (heightIndex != -1) {
							String defaultHeight = attributes
									.getValue(heightIndex);
							newHeight = formatter.getImageHeightDirective(
									dynamicImageName, defaultHeight);
						}
						if (newWith != null || newHeight != null) {
							AttributesImpl attr = toAttributesImpl(attributes);
							if (newWith != null) {
								attr.setValue(widthIndex, newWith);
							}
							if (newHeight != null) {
								attr.setValue(heightIndex, newHeight);
							}
							attributes = attr;
						}
					}
				}
			}
		} else if (isDrawImage(uri, localName, name)) {
			if (dynamicImageName != null && formatter != null) {
				String newHref = formatter.getImageDirective(dynamicImageName);
				AttributesImpl attributesImpl = toAttributesImpl(attributes);
				int index = attributesImpl.getIndex(XLINK_NS, HREF_ATTR);
				if (index != -1) {
					attributesImpl.setValue(index, newHref);
					attributes = attributesImpl;
				}
			}
		}
		return super.doStartElement(uri, localName, name, attributes);

	}

	@Override
	public void doEndElement(String uri, String localName, String name)
			throws SAXException {
		if (isTextInput(uri, localName, name)) {
			// Ignore element end text:text-input
			this.textInputParsing = false;
		} else {
			if (isDrawFrame(uri, localName, name)) {
				dynamicImageName = null;
			} else if (isOfficeAutomaticStyles(uri, localName, name)) {
				// Add bold, italic, bold+italic styles for text styling.
				generateStyle(BOLD_STYLE_NAME, true, false);
				generateStyle(ITALIC_STYLE_NAME, false, true);
				generateStyle(BOLD_ITALIC_STYLE_NAME, true, true);
			}
			super.doEndElement(uri, localName, name);
		}
	}

	private void generateStyle(String styleName, boolean bold, boolean italic) {
		IBufferedRegion region = getCurrentElement();
		region.append("<style:style style:name=\"");
		region.append(styleName);
		region.append("\" style:family=\"text\">");
		region.append("<style:text-properties");
		if (bold) {
			region.append(" fo:font-weight=\"bold\"");
		}
		if (italic) {
			region.append(" fo:font-style=\"italic\"");
		}
		region.append("/></style:style>");
	}

	@Override
	protected String getTableRowName() {
		return "table:table-row";
	}

	@Override
	protected String getTableCellName() {
		return "table:table-cell";
	}

	@Override
	protected void flushCharacters(String characters) {
		if (textInputParsing) {
			IDocumentFormatter formatter = getFormatter();
			if (formatter != null
					&& (formatter.containsInterpolation(characters) || formatter
							.hasDirective(characters))) {
				// It's an interpolation, unescape the XML
				characters = StringUtils.xmlUnescape(characters);
			}
			String fieldName = characters;
			if (processScriptBefore(fieldName)) {
				return;
			}
			if (processScriptAfter(fieldName)) {
				return;
			}
			FieldMetadata fieldAsTextStyling = getFieldAsTextStyling(fieldName);
			if (fieldAsTextStyling != null && getFormatter() != null) {
				// register parent buffered element

				BufferedElement textPElement = getCurrentElement().findParent(
						"text:p");
				if (textPElement == null) {
					textPElement = getCurrentElement().getParent();
				}
				String elementId = registerBufferedElement(textPElement);

				// [#assign
				// 1327511861250_id=___TextStylingRegistry.transform(comments_odt,"NoEscape","ODT","1327511861250_id",___context)]
				long variableIndex = getVariableIndex();
				String setVariableDirective = getFormatter()
						.formatAsCallTextStyling(variableIndex, fieldName,
								fieldAsTextStyling.getFieldName(),
								DocumentKind.ODT.name(),
								fieldAsTextStyling.getSyntaxKind(), elementId);

				String textBefore = getFormatter().formatAsTextStylingField(
						variableIndex, ITransformResult.TEXT_BEFORE_PROPERTY);
				String textBody = getFormatter().formatAsTextStylingField(
						variableIndex, ITransformResult.TEXT_BODY_PROPERTY);
				String textEnd = getFormatter().formatAsTextStylingField(
						variableIndex, ITransformResult.TEXT_END_PROPERTY);

				textPElement
						.setContentBeforeStartTagElement(setVariableDirective
								+ " " + textBefore);
				textPElement.setContentAfterEndTagElement(textEnd);
				super.flushCharacters(textBody);
				return;
			}
		}
		super.flushCharacters(characters);
	}

	

}
