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
package fr.opensagres.xdocreport.template.formatter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.TextStylingKind;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

/**
 * Fields Metadata is used in the preprocessing step to modify some XML entries
 * like generate script (Freemarker, Velocity...) for loop for Table row,
 * generate script for Image...
 * 
 */
public class FieldsMetadata {

	public static final FieldsMetadata EMPTY = new FieldsMetadata();
	public static final String DEFAULT_BEFORE_ROW_TOKEN = "@before-row";
	public static final String DEFAULT_AFTER_ROW_TOKEN = "@after-row";
	public static final String DEFAULT_BEFORE_TABLE_CELL_TOKEN = "@before-cell";
	public static final String DEFAULT_AFTER_TABLE_CELL_TOKEN = "@after-cell";

	private final IFieldsMetadataClassSerializer serializer;
	private final List<FieldMetadata> fields;
	private final Map<String, FieldMetadata> fieldsAsList;
	private final Map<String, FieldMetadata> fieldsAsImage;
	private final Map<String, FieldMetadata> fieldsAsTextStyling;
	private String beforeRowToken;
	private String afterRowToken;
	private String beforeTableCellToken;
	private String afterTableCellToken;

	public FieldsMetadata() {
		this((IFieldsMetadataClassSerializer) null);
	}

	public FieldsMetadata(String templateEngineKind) {
		this(TemplateEngineRegistry.getRegistry()
				.getFieldsMetadataClassSerializer());
	}

	public FieldsMetadata(IFieldsMetadataClassSerializer serializer) {
		this.serializer = serializer;
		this.fields = new ArrayList<FieldMetadata>();
		this.fieldsAsList = new HashMap<String, FieldMetadata>();
		this.fieldsAsImage = new HashMap<String, FieldMetadata>();
		this.fieldsAsTextStyling = new HashMap<String, FieldMetadata>();
		this.beforeRowToken = DEFAULT_BEFORE_ROW_TOKEN;
		this.afterRowToken = DEFAULT_AFTER_ROW_TOKEN;
		this.beforeTableCellToken = DEFAULT_BEFORE_TABLE_CELL_TOKEN;
		this.afterTableCellToken = DEFAULT_AFTER_TABLE_CELL_TOKEN;
	}

	/**
	 * Add a field name which is considered as an image.
	 * 
	 * @param fieldName
	 */
	public void addFieldAsImage(String fieldName) {
		addFieldAsImage(fieldName, fieldName);
	}

	/**
	 * Add a field name which is considered as an image.
	 * 
	 * @param imageName
	 * @param fieldName
	 */
	public void addFieldAsImage(String imageName, String fieldName) {
		addField(fieldName, false, imageName, null);
	}

	/**
	 * Add a field name which can contains text stylink (Html, Wikipedia,
	 * etc..).
	 * 
	 * @param fieldName
	 * @param textStylingKind
	 */
	public void addFieldAsTextStyling(String fieldName,
			TextStylingKind textStylingKind) {
		addFieldAsTextStyling(fieldName, textStylingKind.name());
	}

	/**
	 * Add a field name which can contains text stylink (Html, Wikipedia,
	 * etc..).
	 * 
	 * @param fieldName
	 * @param textStylingKind
	 */
	public void addFieldAsTextStyling(String fieldName, String textStylingKind) {
		// Test if it exists fields with the given name
		addField(fieldName, false, null, textStylingKind);
	}

	/**
	 * Add a field name which belongs to a list.
	 * 
	 * @param fieldName
	 */
	public void addFieldAsList(String fieldName) {
		addField(fieldName, true, null, null);
	}

	public void addField(String fieldName, boolean listType, String imageName,
			String textStylingKind) {
		// Test if it exists fields with the given name
		FieldMetadata exsitingField = fieldsAsImage.get(fieldName);
		if (exsitingField == null) {
			exsitingField = fieldsAsList.get(fieldName);
		}
		if (exsitingField == null) {
			exsitingField = fieldsAsTextStyling.get(fieldName);
		}

		if (exsitingField == null) {
			FieldMetadata fieldMetadata = new FieldMetadata(fieldName,
					listType, imageName, textStylingKind);
			fields.add(fieldMetadata);
			if (fieldMetadata.isImageType()) {
				fieldsAsImage.put(fieldMetadata.getImageName(), fieldMetadata);
			}
			if (fieldMetadata.isListType()) {
				fieldsAsList.put(fieldMetadata.getFieldName(), fieldMetadata);
			}
			if (fieldMetadata.getTextStylingKind() != null) {
				fieldsAsTextStyling.put(fieldMetadata.getFieldName(),
						fieldMetadata);
			}
		} else {
			if (listType == true && listType != exsitingField.isListType()) {
				exsitingField.setListType(true);
				fieldsAsList.put(exsitingField.getFieldName(), exsitingField);
			}
			if (imageName != null
					&& !imageName.equals(exsitingField.getImageName())) {
				exsitingField.setImageName(imageName);
				fieldsAsImage.put(imageName, exsitingField);
			}
			if (textStylingKind != null) {
				exsitingField.setTextStylingKind(textStylingKind);
				fieldsAsTextStyling.put(imageName, exsitingField);
			}
		}
	}

	/**
	 * Returns list of fields name which belongs to a list.
	 * 
	 * @return
	 */
	public Collection<String> getFieldsAsList() {
		return Collections.unmodifiableCollection(fieldsAsList.keySet());
	}

	/**
	 * Returns list of fields name which are considered as an image.
	 * 
	 * @return
	 */
	public Collection<FieldMetadata> getFieldsAsImage() {
		return Collections.unmodifiableCollection(fieldsAsImage.values());
	}

	/**
	 * Returns list of fields name which can contains text styling.
	 * 
	 * @return
	 */
	public Collection<FieldMetadata> getFieldsAsTextStyling() {
		return Collections.unmodifiableCollection(fieldsAsTextStyling.values());
	}

	/**
	 * Returns true if there are fields as image and false otherwise.
	 * 
	 * @return
	 */
	public boolean hasFieldsAsImage() {
		return fieldsAsImage.size() > 0;
	}

	public boolean isFieldAsImage(String fieldName) {
		if (StringUtils.isEmpty(fieldName)) {
			return false;
		}
		return fieldsAsImage.containsKey(fieldName);
	}

	public String getImageFieldName(String fieldName) {
		if (StringUtils.isEmpty(fieldName)) {
			return null;
		}
		FieldMetadata metadata = fieldsAsImage.get(fieldName);
		if (metadata != null) {
			return metadata.getFieldName();
		}
		return null;
	}

	public String getBeforeRowToken() {
		return beforeRowToken;
	}

	public void setBeforeRowToken(String beforeRowToken) {
		this.beforeRowToken = beforeRowToken;
	}

	public String getAfterRowToken() {
		return afterRowToken;
	}

	public void setAfterRowToken(String afterRowToken) {
		this.afterRowToken = afterRowToken;
	}

	public String getBeforeTableCellToken() {
		return beforeTableCellToken;
	}

	public void setBeforeTableCellToken(String beforeTableCellToken) {
		this.beforeTableCellToken = beforeTableCellToken;
	}

	public String getAfterTableCellToken() {
		return afterTableCellToken;
	}

	public void setAfterTableCellToken(String afterTableCellToken) {
		this.afterTableCellToken = afterTableCellToken;
	}

	/**
	 * Returns list of fields metadata.
	 * 
	 * @return
	 */
	public List<FieldMetadata> getFields() {
		return fields;
	}

	/**
	 * Load fields metadata from the given XML reader.
	 * 
	 * Here a sample of XML reader :
	 * 
	 * <pre>
	 * <fields>
	 * 	<field name="project.Name" imageName="" listType="false" />
	 * 	<field name="developers.Name" imageName="" listType="true" />
	 * <field name="project.Logo" imageName="Logo" listType="false" />
	 * </fields>
	 * </pre>
	 * 
	 * @param reader
	 */
	public void loadXML(Reader reader) {
		FieldsMetadataXMLSerializer.getInstance().load(this, reader);
	}

	/**
	 * Serialize as XML without indentation the fields metadata to the given XML
	 * writer.
	 * 
	 * Here a sample of XML writer :
	 * 
	 * <pre>
	 * <fields>
	 * 	<field name="project.Name" imageName="" listType="false" />
	 * 	<field name="developers.Name" imageName="" listType="true" />
	 * <field name="project.Logo" imageName="Logo" listType="false" />
	 * </fields>
	 * </pre>
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void saveXML(Writer writer) throws IOException {
		saveXML(writer, false);
	}

	/**
	 * Serialize as XML the fields metadata to the given XML writer.
	 * 
	 * Here a sample of XML writer :
	 * 
	 * <pre>
	 * <fields>
	 * 	<field name="project.Name" imageName="" listType="false" />
	 * 	<field name="developers.Name" imageName="" listType="true" />
	 * <field name="project.Logo" imageName="Logo" listType="false" />
	 * </fields>
	 * </pre>
	 * 
	 * @param writer
	 *            XML writer.
	 * @param indent
	 *            true if indent must be managed and false otherwise.
	 * @throws IOException
	 */
	public void saveXML(Writer writer, boolean indent) throws IOException {
		FieldsMetadataXMLSerializer.getInstance().save(this, writer, indent);
	}

	/**
	 * Load simple fields metadata in the given fieldsMetadata by using the
	 * given key and Java Class.
	 * 
	 * @param fieldsMetadata
	 *            the fieldsMetadata where fields metadata must be added.
	 * @param key
	 *            the key (first token) to use to generate field name.
	 * @param clazz
	 *            the Java class model to use to load fields metadata.
	 */
	public void load(FieldsMetadata fieldsMetadata, String key, Class<?> clazz) {
		if (serializer == null) {
			// TODO : check that serializer is not null
		}
		serializer.load(this, key, clazz);
	}

	/**
	 * Load simple/list fields metadata in the given fieldsMetadata by using the
	 * given key and Java Class.
	 * 
	 * @param fieldsMetadata
	 *            the fieldsMetadata where fields metadata must be added.
	 * @param key
	 *            the key (first token) to use to generate field name.
	 * @param clazz
	 *            the Java class model to use to load fields metadata.
	 * @param listType
	 *            true if it's a list and false otherwise.
	 */
	public void load(FieldsMetadata fieldsMetadata, String key, Class<?> clazz,
			boolean listType) {
		if (serializer == null) {
			// TODO : check that serializer is not null
		}
		serializer.load(this, key, clazz, listType);
	}

	@Override
	public String toString() {
		StringWriter xml = new StringWriter();
		try {
			saveXML(xml, true);
		} catch (IOException e) {
			return super.toString();
		}
		return xml.toString();
	}

}
