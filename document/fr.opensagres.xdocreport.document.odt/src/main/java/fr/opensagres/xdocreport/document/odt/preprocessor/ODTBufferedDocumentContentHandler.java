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
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isTextA;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isTextInput;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.odt.ODTConstants;
import fr.opensagres.xdocreport.document.odt.ODTUtils;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * SAX content handler to generate lazy Freemarker/Velocity loop directive in
 * the table row which contains a list fields.
 */
public class ODTBufferedDocumentContentHandler extends
		TransformedBufferedDocumentContentHandler implements ODTConstants {

	private String dynamicImageName;

	protected ODTBufferedDocumentContentHandler(FieldsMetadata fieldsMetadata,
			IDocumentFormatter formatter, Map<String, Object> sharedContext) {
		super(fieldsMetadata, formatter, sharedContext);
	}

	@Override
	public boolean doStartElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		FieldsMetadata fieldsMetadata = super.getFieldsMetadata();
		IDocumentFormatter formatter = super.getFormatter();
		if (isTextInput(uri, localName, name)) {
			// Ignore element start text:text-input
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
		} else {
			if (isDrawFrame(uri, localName, name)) {
				dynamicImageName = null;
			}
			super.doEndElement(uri, localName, name);
		}
	}

	@Override
	protected boolean isTable(String uri, String localName, String name) {
		return ODTUtils.isTable(uri, localName, name);
	}

	@Override
	protected boolean isRow(String uri, String localName, String name) {
		return ODTUtils.isTableRow(uri, localName, name);
	}

}
