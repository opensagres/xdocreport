package fr.opensagres.xdocreport.document.tools.json;

import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class FieldsMetadataJSONSerializerTestCase {

	@Test
	public void testEmptyFieldsWithNoIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		StringWriter writer = new StringWriter();
		FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata, writer,
				false);
		Assert.assertEquals("{}", writer.toString());
	}

	@Test
	public void testEmptyFieldsWithIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		StringWriter writer = new StringWriter();
		FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata, writer,
				true);
		Assert.assertEquals("{}", writer.toString());
	}

	@Test
	public void testSimpleFieldWithNoIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addField("name", false, null, null);

		StringWriter writer = new StringWriter();
		FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata, writer,
				false);
		Assert.assertEquals("{\"name\":\"name_Value\"}", writer.toString());
	}

	@Test
	public void testSimpleFieldWithIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addField("name", false, null, null);

		StringWriter writer = new StringWriter();
		FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata, writer,
				true);
		Assert.assertEquals("{\"name\": \"name_Value\"}", writer.toString());
	}
	
	@Test
	public void testTwoSimpleFieldWithNoIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addField("name", false, null, null);
		fieldsMetadata.addField("name2", false, null, null);

		StringWriter writer = new StringWriter();
		FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata, writer,
				false);
		Assert.assertEquals("{\"name\":\"name_Value\",\"name2\":\"name2_Value\"}", writer.toString());
	}

	@Test
	public void testTwoSimpleFieldWithIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addField("name", false, null, null);
		fieldsMetadata.addField("name2", false, null, null);

		StringWriter writer = new StringWriter();
		FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata, writer,
				true);
		Assert.assertEquals("{" +
				"\n \"name\": \"name_Value\"," +
				"\n \"name2\": \"name2_Value\"" +
				"\n}", writer.toString());
	}
	
	@Test
	public void testSimpleDottedFieldWithNoIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addField("project.name", false, null, null);		

		StringWriter writer = new StringWriter();
		FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata, writer,
				false);
		Assert.assertEquals("{\"project\":{\"name\":\"name_Value\"}}", writer.toString());
	}
	
	@Test
	public void testSimpleDottedFieldWithIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addField("project.name", false, null, null);		

		StringWriter writer = new StringWriter();
		FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata, writer,
				true);
		Assert.assertEquals("{\"project\": {\"name\": \"name_Value\"}}", writer.toString());
	}
	
	@Test
	public void testForSimpleDottedFieldWithNoIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addField("project.name", false, null, null);		
		fieldsMetadata.addField("project.url", false, null, null);
		fieldsMetadata.addField("developer.name", false, null, null);
		fieldsMetadata.addField("developer.mail", false, null, null);

		StringWriter writer = new StringWriter();
		FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata, writer,
				false);
		Assert.assertEquals("{\"project\":{\"name\":\"name_Value\",\"url\":\"url_Value\"},\"developer\":{\"mail\":\"mail_Value\",\"name\":\"name_Value\"}}", writer.toString());
	}
	
	@Test
	public void testListDottedFieldWithNoIndent() throws Exception {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addField("developers.name", true, null, null);		

		StringWriter writer = new StringWriter();
		FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata, writer,
				false);
		Assert.assertEquals("{\"developers\":[{\"name\":\"name_Value0\"},{\"name\":\"name_Value1\"},{\"name\":\"name_Value2\"},{\"name\":\"name_Value3\"},{\"name\":\"name_Value4\"},{\"name\":\"name_Value5\"},{\"name\":\"name_Value6\"},{\"name\":\"name_Value7\"},{\"name\":\"name_Value8\"},{\"name\":\"name_Value9\"}]}", writer.toString());
	}
}
