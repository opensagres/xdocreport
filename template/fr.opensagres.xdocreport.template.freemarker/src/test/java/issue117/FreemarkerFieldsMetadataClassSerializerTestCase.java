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
package issue117;

import org.junit.Test;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerFieldsMetadataClassSerializer;

public class FreemarkerFieldsMetadataClassSerializerTestCase
{


    @Test
    public void testSimplePOJO()
        throws XDocReportException
    {
        try {
			FieldsMetadata fieldsMetadata = new FieldsMetadata();
			FreemarkerFieldsMetadataClassSerializer serializer = new FreemarkerFieldsMetadataClassSerializer();
			serializer.load(fieldsMetadata, "parent", Parent.class);

			System.out.println(fieldsMetadata);
			//Assert.assertEquals(1, fieldsMetadata.getFields().size());
			//Assert.assertEquals( "parent.name", fieldsMetadata.getFields().get( 0 ).getFieldName() );
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
/*
    @Test
    public void testListPOJO()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        FreemarkerFieldsMetadataClassSerializer serializer = new FreemarkerFieldsMetadataClassSerializer();
        serializer.load( fieldsMetadata, "developers", Developer.class, true );

        Assert.assertEquals( 4, fieldsMetadata.getFields().size() );
        Assert.assertEquals( "developers.lastName", fieldsMetadata.getFields().get( 0 ).getFieldName() );
        Assert.assertTrue( fieldsMetadata.getFields().get( 0 ).isListType() );
        Assert.assertEquals( "developers.mail", fieldsMetadata.getFields().get( 1 ).getFieldName() );
        Assert.assertTrue( fieldsMetadata.getFields().get( 1 ).isListType() );
        Assert.assertEquals( "developers.name", fieldsMetadata.getFields().get( 2 ).getFieldName() );
        Assert.assertTrue( fieldsMetadata.getFields().get( 2 ).isListType() );
        Assert.assertEquals( "developers.roles.name", fieldsMetadata.getFields().get( 3 ).getFieldName() );
        Assert.assertTrue( fieldsMetadata.getFields().get( 3 ).isListType() );
    }*/


}
