package fr.opensagres.xdocreport.template.formatter;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

public class FieldsMetadataXMLSerializerTestCase {

	private static final String LF = System.getProperty("line.separator");
			
	@Test
	public void testLoadXMFromReader() throws Exception {
		StringReader reader = new StringReader(
				"<fields><field name='developers.Name' list='true' /></fields>");
		FieldsMetadata fieldsMetadata = FieldsMetadataXMLSerializer
				.getInstance().load(reader);
		
		Assert.assertEquals(1, fieldsMetadata.getFields().size());
		Assert.assertEquals("developers.Name", fieldsMetadata.getFields()
				.get(0).getFieldName());
		Assert.assertTrue(fieldsMetadata.getFields().get(0).isListType());

	}

	@Test
	public void testLoadXMFromInputStream() throws Exception {
		InputStream inputStream = FieldsMetadataXMLSerializerTestCase.class
				.getResourceAsStream("fields.xml");
		FieldsMetadata fieldsMetadata = FieldsMetadataXMLSerializer
				.getInstance().load(inputStream);

		Assert.assertEquals(1, fieldsMetadata.getFields().size());
		Assert.assertEquals("developers.Name", fieldsMetadata.getFields()
				.get(0).getFieldName());
		Assert.assertTrue(fieldsMetadata.getFields().get(0).isListType());
		Assert.assertEquals("A field description...", fieldsMetadata.getFields().get(0).getDescription());
		Assert.assertEquals("Velocity", fieldsMetadata.getTemplateEngineKind());
		Assert.assertEquals("A description...", fieldsMetadata.getDescription());
	}

	@Test
	public void testSaveAsXMLWithIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.setTemplateEngineKind("Velocity");
		fieldsMetadata.setDescription("A description...");
		FieldMetadata metadata = fieldsMetadata.addFieldAsList("developers.Name");
		metadata.setDescription("A field description...");
		metadata = fieldsMetadata.addField("project.Name", false, null, null);
		metadata.setDescription("An other field description...");
		
		StringWriter writer = new StringWriter();
		FieldsMetadataXMLSerializer.getInstance().save(fieldsMetadata, writer,
				true);		
		Assert.assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				LF +"<fields templateEngineKind=\"Velocity\" >" +
				LF +"\t<description><![CDATA[A description...]]></description>" +
				LF +"\t<field name=\"developers.Name\" list=\"true\" imageName=\"\" syntaxKind=\"\">" +
				LF +"\t\t<description><![CDATA[A field description...]]></description>" +
				LF +"\t</field>" +
				LF +"\t<field name=\"project.Name\" list=\"false\" imageName=\"\" syntaxKind=\"\">" +
				LF +"\t\t<description><![CDATA[An other field description...]]></description>" +
				LF +"\t</field>" + 				
				LF +"</fields>",
				writer.toString());

	}

	@Test
	public void testSaveAsXMLWithoutIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.setTemplateEngineKind("Velocity");
		fieldsMetadata.setDescription("A description...");
		FieldMetadata metadata = fieldsMetadata.addFieldAsList("developers.Name");
		metadata.setDescription("A field description...");

		StringWriter writer = new StringWriter();
		FieldsMetadataXMLSerializer.getInstance().save(fieldsMetadata, writer,
				false);

		Assert.assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<fields templateEngineKind=\"Velocity\" >" +
				"<description><![CDATA[A description...]]></description>" +
				"<field name=\"developers.Name\" list=\"true\" imageName=\"\" syntaxKind=\"\">" +
				"<description><![CDATA[A field description...]]></description>" +
				"</field>" + 
				"</fields>",
				writer.toString());
	}
}
