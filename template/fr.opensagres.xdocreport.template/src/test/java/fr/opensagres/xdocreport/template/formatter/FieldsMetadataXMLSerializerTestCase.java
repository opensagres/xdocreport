package fr.opensagres.xdocreport.template.formatter;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

public class FieldsMetadataXMLSerializerTestCase {

	@Test
	public void testLoadXMFromReader() throws Exception {
		StringReader reader = new StringReader(
				"<fields><field name='developer.Name' listType='true' /></fields>");

		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		FieldsMetadataXMLSerializer.getInstance().load(fieldsMetadata, reader);

		// START remove that once it's implemented
		fieldsMetadata.addFieldAsList("developer.Name");
		// START remove that once it's implemented

		Assert.assertEquals(1, fieldsMetadata.getFields().size());
		Assert.assertEquals("developer.Name", fieldsMetadata.getFields().get(0)
				.getFieldName());
		Assert.assertTrue(fieldsMetadata.getFields().get(0).isListType());

	}
	
	@Test
	public void testLoadXMFromInputStream() throws Exception {
		InputStream inputStream = FieldsMetadataXMLSerializerTestCase.class.getResourceAsStream("fields.xml");

		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		FieldsMetadataXMLSerializer.getInstance().load(fieldsMetadata, inputStream);

		// START remove that once it's implemented
		fieldsMetadata.addFieldAsList("developer.Name");
		// START remove that once it's implemented

		Assert.assertEquals(1, fieldsMetadata.getFields().size());
		Assert.assertEquals("developer.Name", fieldsMetadata.getFields().get(0)
				.getFieldName());
		Assert.assertTrue(fieldsMetadata.getFields().get(0).isListType());

	}

	@Test
	public void testSaveAsXMLWithIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addFieldAsList("developers.Name");

		StringWriter writer = new StringWriter();
		FieldsMetadataXMLSerializer.getInstance().save(fieldsMetadata, writer,
				true);

		Assert.assertEquals(
				"<fields>\n\t<field name=\"developers.Name\"/>\n</fields>",
				writer.toString());

	}

	@Test
	public void testSaveAsXMLWithoutIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addFieldAsList("developers.Name");

		StringWriter writer = new StringWriter();
		FieldsMetadataXMLSerializer.getInstance().save(fieldsMetadata, writer,
				false);

		Assert.assertEquals(
				"<fields><field name=\"developers.Name\"/></fields>",
				writer.toString());
	}
}
