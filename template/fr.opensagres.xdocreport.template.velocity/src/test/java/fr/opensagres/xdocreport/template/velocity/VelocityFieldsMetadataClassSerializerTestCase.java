/**
 * Copyright (C) 2011-2012 The XDocReport Team <xdocreport@googlegroups.com>
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
package fr.opensagres.xdocreport.template.velocity;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.registry.FieldsMetadataClassSerializerRegistry;

public class VelocityFieldsMetadataClassSerializerTestCase
{

	protected class Project
    {

        private final String name;

        public Project( String name )
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    protected class Role
    {

        private final String name;

        public Role( String name )
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    protected static class Developer
    {

        private final String name;

        private final String lastName;

        private final String mail;

        private final List<Role> roles;

        public Developer( String name, String lastName, String mail )
        {
            this.name = name;
            this.lastName = lastName;
            this.mail = mail;
            this.roles = new ArrayList<Role>();
        }

        public String getName()
        {
            return name;
        }

        public String getLastName()
        {
            return lastName;
        }

        public String getMail()
        {
            return mail;
        }

        public Developer addRole( Role role )
        {
            roles.add( role );
            return this;
        }

        public List<Role> getRoles()
        {
            return roles;
        }

    }
	
	public class ComplexType
	{
		private String complexTypeValue;
		public void setComplexTypeValue(String complexTypeValue){this.complexTypeValue=complexTypeValue;}
		public String getComplexTypeValue(){return this.complexTypeValue;}
		}
		
	public class Parent
	{
		private String fieldA;
		private String fieldB;
		private ComplexType complexType;
		
		public void setFieldA(String val){this.fieldA=val;}
		public void setFieldB(String val){this.fieldB=val;}
		public String getFieldA(){return this.fieldA;}
		public String getFieldB(){return this.fieldB;}
		public void setComplexType(ComplexType complexType){this.complexType=complexType;}
		public ComplexType getComplexType(){return this.complexType;}
	}
	public class ChildA extends Parent
    {
		private String fieldC;
		public void setFieldC(String val){this.fieldC=val;}
		public String getFieldC(){return this.fieldC;}
	}
	
	public class ChildB extends Parent
    {
		private String fieldD;
		public void setFieldD(String val){this.fieldD=val;}
		public String getFieldD(){return this.fieldD;}
	}
	
    @Test
    public void testSimplePOJO()
        throws XDocReportException
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        VelocityFieldsMetadataClassSerializer serializer = new VelocityFieldsMetadataClassSerializer();
        serializer.load( fieldsMetadata, "project", Project.class );

        Assert.assertEquals( 1, fieldsMetadata.getFields().size() );
        Assert.assertEquals( "project.Name", fieldsMetadata.getFields().get( 0 ).getFieldName() );
    }

    @Test
    public void testListPOJO()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        VelocityFieldsMetadataClassSerializer serializer = new VelocityFieldsMetadataClassSerializer();
        serializer.load( fieldsMetadata, "developers", Developer.class, true );

        Assert.assertEquals( 4, fieldsMetadata.getFields().size() );
        Assert.assertEquals( "developers.LastName", fieldsMetadata.getFields().get( 0 ).getFieldName() );
        Assert.assertTrue( fieldsMetadata.getFields().get( 0 ).isListType() );
        Assert.assertEquals( "developers.Mail", fieldsMetadata.getFields().get( 1 ).getFieldName() );
        Assert.assertTrue( fieldsMetadata.getFields().get( 1 ).isListType() );
        Assert.assertEquals( "developers.Name", fieldsMetadata.getFields().get( 2 ).getFieldName() );
        Assert.assertTrue( fieldsMetadata.getFields().get( 2 ).isListType() );
        Assert.assertEquals( "developers.Roles.Name", fieldsMetadata.getFields().get( 3 ).getFieldName() );
        Assert.assertTrue( fieldsMetadata.getFields().get( 3 ).isListType() );
    }

	@Test
	public void testInheritanceWithComplexTypePOJO() throws Exception
	{		
			List<Class<?>> pojoClasses=new ArrayList<Class<?>>();
			pojoClasses.add(Parent.class);
			pojoClasses.add(ChildA.class);
			pojoClasses.add(ChildB.class);
			HashMap<Class<?>,Integer> fieldsCountExpected=new HashMap<Class<?>,Integer>();
			fieldsCountExpected.put(Parent.class,3);
			fieldsCountExpected.put(ChildA.class,4);//expect and complex type to be here
			fieldsCountExpected.put(ChildB.class,4);//expect and complex type to be here
			for(Class<?> clazz: pojoClasses)
			{
				//FieldsMetadataClassSerializerRegistry.getRegistry().dispose();//uncomment this line to pass test
				FieldsMetadata fieldsMetadata=new FieldsMetadata("Velocity");
				fieldsMetadata.load("template", clazz);
				System.out.println("\n======================================\n"); 
				System.out.println(fieldsMetadata.toString());
				System.out.println("\n======================================\n");
				Assert.assertEquals( fieldsCountExpected.get(clazz).intValue(), fieldsMetadata.getFields().size() );
			}
			
		}
	
    @Test
    public void testSimpleImage()
    {
        // Problem IImageProvider is in the document package
        // IImageProvider
    }
}
