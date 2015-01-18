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
package fr.opensagres.xdocreport.document.tools.json;

import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class FieldsMetadataJSONSerializerTestCase
{

    @Test
    public void testEmptyFieldsWithNoIndent()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        StringWriter writer = new StringWriter();
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, writer, false );
        Assert.assertEquals( "{}", writer.toString() );
    }

    @Test
    public void testEmptyFieldsWithIndent()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        StringWriter writer = new StringWriter();
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, writer, true );
        Assert.assertEquals( "{}", writer.toString() );
    }

    @Test
    public void testSimpleFieldWithNoIndent()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.addField( "name", false, null, null, null );

        StringWriter writer = new StringWriter();
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, writer, false );
        Assert.assertEquals( "{\"name\":\"name_Value\"}", writer.toString() );
    }

    @Test
    public void testSimpleFieldWithIndent()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.addField( "name", false, null, null, null );

        StringWriter writer = new StringWriter();
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, writer, true );
        Assert.assertEquals( "{\"name\": \"name_Value\"}", writer.toString() );
    }

    @Test
    public void testTwoSimpleFieldWithNoIndent()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.addField( "name", false, null, null, null );
        fieldsMetadata.addField( "name2", false, null, null, null );

        StringWriter writer = new StringWriter();
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, writer, false );
        // In some context (which?) the order of JSON object change
        if ( "{\"name\":\"name_Value\",\"name2\":\"name2_Value\"}".equals( writer.toString() ) )
        {
            Assert.assertEquals( "{\"name\":\"name_Value\",\"name2\":\"name2_Value\"}", writer.toString() );
        }
        else
        {
            Assert.assertEquals( "{\"name2\":\"name2_Value\",\"name\":\"name_Value\"}", writer.toString() );
        }
    }

    @Test
    public void testTwoSimpleFieldWithIndent()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.addField( "name", false, null, null, null );
        fieldsMetadata.addField( "name2", false, null, null, null );

        StringWriter writer = new StringWriter();
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, writer, true );
        String expected = "{" + "\n \"name\": \"name_Value\"," + "\n \"name2\": \"name2_Value\"" + "\n}";
        if ( expected.equals( writer.toString() ) )
        {
            Assert.assertEquals( "{" + "\n \"name\": \"name_Value\"," + "\n \"name2\": \"name2_Value\"" + "\n}",
                                 writer.toString() );
        }
        else
        {
            Assert.assertEquals( "{" + "\n \"name2\": \"name2_Value\"," + "\n \"name\": \"name_Value\"" + "\n}",
                                 writer.toString() );
        }
    }

    @Test
    public void testSimpleDottedFieldWithNoIndent()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.addField( "project.name", false, null, null, null );

        StringWriter writer = new StringWriter();
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, writer, false );
        Assert.assertEquals( "{\"project\":{\"name\":\"name_Value\"}}", writer.toString() );
    }

    @Test
    public void testSimpleDottedFieldWithIndent()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.addField( "project.name", false, null, null, null );

        StringWriter writer = new StringWriter();
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, writer, true );
        Assert.assertEquals( "{\"project\": {\"name\": \"name_Value\"}}", writer.toString() );
    }

    @Test
    public void testForSimpleDottedFieldWithNoIndent()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.addField( "project.name", false, null, null, null );
        fieldsMetadata.addField( "project.url", false, null, null, null );
        fieldsMetadata.addField( "developer.name", false, null, null, null );
        fieldsMetadata.addField( "developer.mail", false, null, null, null );

        StringWriter writer = new StringWriter();
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, writer, false );
        String expected =
            "{\"project\":{\"name\":\"name_Value\",\"url\":\"url_Value\"},\"developer\":{\"mail\":\"mail_Value\",\"name\":\"name_Value\"}}";
        if ( expected.equals( writer.toString() ) )
        {
            Assert.assertEquals( "{\"project\":{\"name\":\"name_Value\",\"url\":\"url_Value\"},\"developer\":{\"mail\":\"mail_Value\",\"name\":\"name_Value\"}}",
                                 writer.toString() );
        }
        else
        {
            Assert.assertEquals( "{\"developer\":{\"mail\":\"mail_Value\",\"name\":\"name_Value\"},\"project\":{\"url\":\"url_Value\",\"name\":\"name_Value\"}}",
                                 writer.toString() );
        }
    }

    @Test
    public void testListDottedFieldWithNoIndent()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        fieldsMetadata.addField( "developers.name", true, null, null, null );

        StringWriter writer = new StringWriter();
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, writer, false );
        Assert.assertEquals( "{\"developers\":[{\"name\":\"name_Value0\"},{\"name\":\"name_Value1\"},{\"name\":\"name_Value2\"},{\"name\":\"name_Value3\"},{\"name\":\"name_Value4\"},{\"name\":\"name_Value5\"},{\"name\":\"name_Value6\"},{\"name\":\"name_Value7\"},{\"name\":\"name_Value8\"},{\"name\":\"name_Value9\"}]}",
                             writer.toString() );
    }
}
