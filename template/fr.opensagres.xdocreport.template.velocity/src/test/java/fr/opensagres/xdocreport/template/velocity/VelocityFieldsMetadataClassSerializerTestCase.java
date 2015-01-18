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
package fr.opensagres.xdocreport.template.velocity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

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
    {// expected 4 fields
        private String complexTypeValue;

        private List<String> listOfStrings;

        private ComplexType me;

        private transient String transientValue;

        public void setComplexTypeValue( String complexTypeValue )
        {
            this.complexTypeValue = complexTypeValue;
        }

        @FieldMetadata( syntaxWithDirective = true, syntaxKind = "Html", description = "Complex Type Value Description" )
        public String getComplexTypeValue()
        {
            return this.complexTypeValue;
        }

        public void setListOfStrings( List<String> listOfStrings )
        {
            this.listOfStrings = listOfStrings;
        }

        @FieldMetadata( syntaxWithDirective = true, syntaxKind = "Html", description = "List of strings description" )
        public List<String> getListOfStrings()
        {
            return listOfStrings;
        }

        public void setMe( ComplexType me )
        {
            this.me = me;
        }

        public ComplexType getMe()
        {
            return me;
        }

        public void setTransientValue( String transientValue )
        {
            this.transientValue = transientValue;
        }

        public String getTransientValue()
        {
            return transientValue;
        }
    }

    public class Parent
    {// expected 20
        private String fieldA;// 1

        private String fieldB;// 1

        private ComplexType complexType;// 4

        private List<ComplexType> listOfComplexTypes;// 4

        private Parent me;// 10

        private transient String transientValue;

        private int intVal;// should be ignored as is primitive type

        public void setFieldA( String val )
        {
            this.fieldA = val;
        }

        public void setFieldB( String val )
        {
            this.fieldB = val;
        }

        public String getFieldA()
        {
            return this.fieldA;
        }

        public String getFieldB()
        {
            return this.fieldB;
        }

        public void setTransientValue( String transientValue )
        {
            this.transientValue = transientValue;
        }

        public String getTransientValue()
        {
            return transientValue;
        }

        public void setListOfComplexTypes( List<ComplexType> listOfComplexTypes )
        {
            this.listOfComplexTypes = listOfComplexTypes;
        }

        public List<ComplexType> getListOfComplexTypes()
        {
            return listOfComplexTypes;
        }

        public void setMe( Parent me )
        {
            this.me = me;
        }

        public Parent getMe()
        {
            return me;
        }

        public void setComplexType( ComplexType complexType )
        {
            this.complexType = complexType;
        }

        public ComplexType getComplexType()
        {
            return complexType;
        }

        public void setIntVal( int intVal )
        {
            this.intVal = intVal;
        }

        public int getIntVal()
        {
            return intVal;
        }

    }

    public class ChildA
        extends Parent
    {
        private String fieldC;

        public void setFieldC( String val )
        {
            this.fieldC = val;
        }

        public String getFieldC()
        {
            return this.fieldC;
        }
    }

    public class ChildB
        extends Parent
    {
        private String fieldD;

        public void setFieldD( String val )
        {
            this.fieldD = val;
        }

        public String getFieldD()
        {
            return this.fieldD;
        }
    }

    protected class ProjectWithImage
    {

        private final String name;

        public ProjectWithImage( String name )
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        @FieldMetadata( images = { @ImageMetadata( name = "logo" ) } )
        public InputStream getLogo()
        {
            return null;
        }
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

        System.out.println( "\n======================================\n" );
        System.out.println( fieldsMetadata.toString() );
        System.out.println( "\n======================================\n" );

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
    public void testInheritanceWithComplexTypePOJO()
        throws Exception
    {
        List<Class<?>> pojoClasses = new ArrayList<Class<?>>();
        pojoClasses.add( ComplexType.class );
        pojoClasses.add( Parent.class );
        pojoClasses.add( ChildA.class );
        pojoClasses.add( ChildB.class );
        HashMap<Class<?>, Integer> fieldsCountExpected = new HashMap<Class<?>, Integer>();
        fieldsCountExpected.put( ComplexType.class, 4 );
        fieldsCountExpected.put( Parent.class, 20 );
        fieldsCountExpected.put( ChildA.class, 21 );// expect and complex type to be here
        fieldsCountExpected.put( ChildB.class, 21 );// expect and complex type to be here
        for ( Class<?> clazz : pojoClasses )
        {
            // FieldsMetadataClassSerializerRegistry.getRegistry().dispose();//uncomment this line to pass test
            FieldsMetadata fieldsMetadata = new FieldsMetadata( TemplateEngineKind.Velocity );
            fieldsMetadata.load( "template", clazz );
            System.out.println( "\n===============" + clazz.getSimpleName() + "=======================\n" );
            System.out.println( fieldsMetadata.toString() );
            System.out.println( "\n======================================\n" );
            Assert.assertEquals( fieldsCountExpected.get( clazz ).intValue(), fieldsMetadata.getFields().size() );
        }

    }

    @Test
    public void testBuildFieldPaths()
        throws XDocReportException
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata( TemplateEngineKind.Velocity );
        fieldsMetadata.load( "template", ChildA.class );
        System.out.println( "=================" );
        fieldsMetadata.load( "template1", ChildB.class );
    }

    @Test
    public void testSyntaxAnnotations()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata( TemplateEngineKind.Velocity );
        fieldsMetadata.load( "template", Parent.class );
        System.out.println( fieldsMetadata.toString() );
        Assert.assertEquals( 16, fieldsMetadata.getFieldsAsTextStyling().size() );
    }

    @Test
    public void testImageAnnotations()
        throws Exception
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata( TemplateEngineKind.Velocity );
        fieldsMetadata.load( "project", ProjectWithImage.class );
        System.out.println( fieldsMetadata.toString() );
        Assert.assertEquals( 1, fieldsMetadata.getFieldsAsImage().size() );
    }

    @Test
    public void testSimpleImage()
    {
        // Problem IImageProvider is in the document package
        // IImageProvider
    }
}
