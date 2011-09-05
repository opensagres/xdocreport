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

import java.util.Collection;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.DocXConstants;
import fr.opensagres.xdocreport.document.docx.images.DocxImageRegistry;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * This handler modify the XML entry word/_rels/document.xml.rels to add
 * Relationship for dynamic image and hyperlink :
 * 
 * <pre>
 *   <?xml version="1.0" encoding="UTF-8" standalone="yes" ?> 
 * <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
 *   <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml" /> 
 *   <Relationship Id="rId7" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme" Target="theme/theme1.xml" /> 
 *   <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml" /> 
 *   <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml" Target="../customXml/item1.xml" /> 
 *   <Relationship Id="rId6" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable" Target="fontTable.xml" /> 
 *   <Relationship Id="rId5" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink" Target="mailto:$developers.Mail" TargetMode="External" /> 
 *   <Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings" Target="webSettings.xml" /> 
 * </Relationships>
 * </pre>
 * 
 * to add template engine script like this for Freemarker :
 * 
 * <pre>
 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
 * 	<Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml"/>
 * 	<Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
 * 	<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml" Target="../customXml/item1.xml"/>
 * 	<Relationship Id="rId6" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme" Target="theme/theme1.xml"/>
 * 	<Relationship Id="rId5" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable" Target="fontTable.xml"/>
 * 	<Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings" Target="webSettings.xml"/>
 * 	[#if imageRegistry??]
 * 		[#list imageRegistry.imageProviderInfos as ___info]
 * 		<Relationship Id="${___info.imageId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${___info.imageFileName}" />
 * 		[/#list]
 * 	[/#if]
 * </Relationships>
 * </pre>
 * 
 * * to add template engine script like this for Velocity :
 * 
 * <pre>
 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">	
 * 	<Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml"/>
 * 	<Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
 * 	<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml" Target="../customXml/item1.xml"/>
 * 	<Relationship Id="rId6" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme" Target="theme/theme1.xml"/>
 * 	<Relationship Id="rId5" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable" Target="fontTable.xml"/>
 * 	<Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings" Target="webSettings.xml"/>
 * 	#if( $imageRegistry)
 * 		#foreach( $___info in $imageRegistry.ImageProviderInfos)<Relationship Id="$___info.ImageId" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/$___info.ImageFileName" />
 * 		#end
 * 	#end
 * </Relationships>
 * </pre>
 */
public class DocxDocumentXMLRelsDocumentContentHandler extends
		BufferedDocumentContentHandler implements DocXConstants {

	private static final String ITEM_INFO = "___info";

	protected final IDocumentFormatter formatter;
	protected final FieldsMetadata fieldsMetadata;
	private final Map<String, Object> context;

	public DocxDocumentXMLRelsDocumentContentHandler(
			FieldsMetadata fieldsMetadata, IDocumentFormatter formatter,
			Map<String, Object> context) {
		this.formatter = formatter;
		this.fieldsMetadata = fieldsMetadata;
		this.context = context;
	}

	@Override
	public boolean doStartElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (RELATIONSHIP_ELT.equals(name)) {
			String type = attributes.getValue(RELATIONSHIP_TYPE_ATTR);
			if (RELATIONSHIPS_HYPERLINK_NS.equals(type)) {
				Map<String, HyperlinkInfo> hyperlinks = (Map<String, HyperlinkInfo>) context
						.get(HyperlinkInfo.KEY);
				if (hyperlinks != null) {
					String target = StringUtils.decode(attributes
							.getValue(RELATIONSHIP_TARGET_ATTR));

					Collection<String> fieldsAsList = fieldsMetadata
							.getFieldsAsList();
					for (final String fieldName : fieldsAsList) {
						if (target.contains(fieldName)) {
							String newContent = formatter
									.formatAsFieldItemList(target, fieldName,
											false);
							if (newContent != null) {
								target = newContent;
								break;
							}
						}
					}

					String hyperlinkId = attributes
							.getValue(RELATIONSHIP_ID_ATTR);
					HyperlinkInfo info = hyperlinks.get(hyperlinkId);
					if (info != null) {
						generateScriptsForDynamicHyperlink(info, target);
					}
				}
			}

		}
		return super.doStartElement(uri, localName, name, attributes);
	}

	@Override
	public void doEndElement(String uri, String localName, String name)
			throws SAXException {
		if (RELATIONSHIPS_ELT.equals(name)) {
			StringBuilder script = new StringBuilder();

			String startIf = formatter
					.getStartIfDirective(IDocumentFormatter.IMAGE_REGISTRY_KEY);
			script.append(startIf);

			// Generate script for dynamic images
			generateScriptsForDynamicImages(script);

			script.append(formatter
					.getEndIfDirective(IDocumentFormatter.IMAGE_REGISTRY_KEY));

			currentRegion.append(script.toString());
		}
		super.doEndElement(uri, localName, name);
	}

	private void generateScriptsForDynamicHyperlink(HyperlinkInfo info,
			String target) {
		StringBuilder script = new StringBuilder();
		String relationId = info.getScriptId();

		script.append(info.getStartLoopDirective());

		generateRelationship(script, relationId, RELATIONSHIPS_HYPERLINK_NS,
				target, TARGET_MODE_EXTERNAL);

		// 3) end loop
		script.append(info.getEndLoopDirective());

		currentRegion.append(script.toString());

	}

	private void generateScriptsForDynamicImages(StringBuilder script) {

		String listInfos = formatter.formatAsSimpleField(false,
				IDocumentFormatter.IMAGE_REGISTRY_KEY, "ImageProviderInfos");
		String itemListInfos = formatter.formatAsSimpleField(false, ITEM_INFO);

		// 1) Start loop
		String startLoop = formatter.getStartLoopDirective(itemListInfos,
				listInfos);
		script.append(startLoop);

		// <Relationship Id="rId4"
		// Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
		// Target="media/image1.png"/>
		String relationId = formatter.formatAsSimpleField(true, ITEM_INFO,
				"ImageId");
		String target = DocxImageRegistry.MEDIA_PATH
				+ formatter.formatAsSimpleField(true, ITEM_INFO,
						"ImageFileName");
		generateRelationship(script, relationId, RELATIONSHIPS_IMAGE_NS,
				target, null);

		// 3) end loop
		script.append(formatter.getEndLoopDirective(itemListInfos));
	}

	// private void generateScriptsForDynamicHyperlinks(StringBuilder script) {
	//
	// String hyperlinkList = formatter.formatAsSimpleField(false,
	// IDocumentFormatter.IMAGE_REGISTRY_KEY, "Hyperlinks");
	// String hyperlinkListItem = formatter.formatAsSimpleField(false,
	// ITEM_HYPERLINK);
	//
	// String startLoop = formatter.getStartLoopDirective(hyperlinkListItem,
	// hyperlinkList);
	//
	// // 1) Start loop
	// script.append(startLoop);
	//
	// // <Relationship Id="rId4"
	// //
	// Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
	// // Target="media/image1.png"/>
	// String relationId = formatter.formatAsSimpleField(true, ITEM_HYPERLINK,
	// "Id");
	// String target = formatter.formatAsSimpleField(true, ITEM_HYPERLINK,
	// "Value");
	// generateRelationship(script, relationId, HYPERLINK_NS, target);
	//
	// // 3) end loop
	// script.append(formatter.getEndLoopDirective(hyperlinkListItem));
	//
	// }

	protected void generateRelationship(StringBuilder script,
			String relationId, String type, String target, String targetMode) {
		script.append("<");
		script.append(RELATIONSHIP_ELT);
		// Id
		script.append(" ");
		script.append(RELATIONSHIP_ID_ATTR);
		script.append("=\"");
		script.append(relationId);
		// Type
		script.append("\" ");
		script.append(RELATIONSHIP_TYPE_ATTR);
		script.append("=\"");
		script.append(type);
		// Target
		script.append("\" ");
		script.append(RELATIONSHIP_TARGET_ATTR);
		script.append("=\"");
		script.append(target);
		if (targetMode != null) {
			script.append("\" ");
			script.append(RELATIONSHIP_TARGET_MODE_ATTR);
			script.append("=\"");
			script.append(targetMode);

		}
		script.append("\" />");
	}
}
