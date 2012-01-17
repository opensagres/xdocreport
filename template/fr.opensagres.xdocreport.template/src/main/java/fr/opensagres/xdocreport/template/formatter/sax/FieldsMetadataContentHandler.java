package fr.opensagres.xdocreport.template.formatter.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.XMLFieldsConstants;

public class FieldsMetadataContentHandler extends DefaultHandler {

	private FieldsMetadata fieldsMetadata;
	private FieldMetadata currentField;
	private StringBuilder description;

	@Override
	public void startDocument() throws SAXException {
		fieldsMetadata = new FieldsMetadata();
	}

	public FieldsMetadata getFieldsMetadata() {
		return fieldsMetadata;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (XMLFieldsConstants.FIELDS_ELT.equals(localName)) {
			String templateEngineKind = attributes
					.getValue(XMLFieldsConstants.TEMPLATE_ENGINE_KIND_ATTR);
			fieldsMetadata.setTemplateEngineKind(templateEngineKind);
		} else if (XMLFieldsConstants.FIELD_ELT.equals(localName)) {
			String fieldName = attributes
					.getValue(XMLFieldsConstants.NAME_ATTR);
			boolean listType = StringUtils.asBoolean(
					attributes.getValue(XMLFieldsConstants.LIST_ATTR), false);
			String imageName = attributes
					.getValue(XMLFieldsConstants.IMAGE_NAME_ATTR);
			String syntaxKind = attributes
					.getValue(XMLFieldsConstants.SYNTAX_KIND_ATTR);
			this.currentField = fieldsMetadata.addField(fieldName, listType,
					imageName, syntaxKind);
		} else if (XMLFieldsConstants.DESCRIPTION_ELT.equals(localName)) {
			description = new StringBuilder();
		}
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (XMLFieldsConstants.FIELD_ELT.equals(localName)) {
			this.currentField = null;
		} else if (XMLFieldsConstants.DESCRIPTION_ELT.equals(localName)) {
			if (currentField != null) {
				currentField.setDescription(description.toString());
			} else {
				fieldsMetadata.setDescription(description.toString());
			}
			description = null;
		}
		super.endElement(uri, localName, qName);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (description != null) {
			description.append(ch, start, length);
		}
		super.characters(ch, start, length);
	}

}
