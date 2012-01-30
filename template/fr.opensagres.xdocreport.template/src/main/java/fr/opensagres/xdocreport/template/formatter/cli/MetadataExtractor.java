package fr.opensagres.xdocreport.template.formatter.cli;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadataXMLSerializer;

public class MetadataExtractor
{

    public static void main( String[] args )
    {
        if ( args.length == 0 )
        {
            printUsage();
        }
        String fileName = "fields.xml";
        String className = null;
        for ( int i = 0; i < args.length; i++ )
        {

            if ( "-out".equals( args[i] ) )
            {
                if ( i == ( args.length - 1 ) )
                {
                    printUsage();
                }
                else
                {
                    fileName = args[i + 1];
                }
            }
            if ( "-rootClass".equals( args[i] ) )
            {
                if ( i == ( args.length - 1 ) )
                {
                    printUsage();
                }
                else
                {
                    className = args[i + 1];
                }
            }

        }

        try
        {
            process( className, fileName );
        }
        catch ( ClassNotFoundException e )
        {
            System.out.println( e.getMessage() );
        }
        catch ( IntrospectionException e )
        {
            System.out.println( e.getMessage() );
        }
        catch ( IOException e )
        {
            System.out.println( e.getMessage() );
        }

    }

    private static void process( String className, String fileName )
        throws ClassNotFoundException, IntrospectionException, IOException
    {
        if ( fileName == null || "".equals( fileName.trim() ) )
        {
            fileName = "fields.xml";
        }

        FileWriter fw = new FileWriter( fileName );
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        Class aClass = Class.forName( className );

        process( fieldsMetadata, aClass.getSimpleName().toLowerCase(), aClass, false );
        FieldsMetadataXMLSerializer.getInstance().save( fieldsMetadata, fw, true );
        fw.flush();
        fw.close();
    }

    private static void process( FieldsMetadata fieldsMetadata, String name, Class aClass, boolean listType )
        throws ClassNotFoundException, IntrospectionException
    {

        fieldsMetadata.addField( name, listType, null, null );

        // continue to introspect if necessary
        if ( aClass.getPackage() == null )
            return;

        if ( "java.lang".equals( aClass.getPackage().getName() ) )
        {
            return;
        }
        if ( "java.util".equals( aClass.getPackage().getName() ) )
        {
            return;
        }

        BeanInfo infos = Introspector.getBeanInfo( aClass );

        PropertyDescriptor[] desc = infos.getPropertyDescriptors();
        for ( int i = 0; i < desc.length; i++ )
        {
            Method aMethod = desc[i].getReadMethod();
            Class child = aMethod.getReturnType();

            if ( !"getClass".equals( aMethod.getName() ) )
            {

                if ( Collection.class.isAssignableFrom( child ) )
                {
                    // process generic collection
                    Type aType = aMethod.getGenericReturnType();
                    if ( aType != null && ( aType instanceof ParameterizedType ) )
                    {
                        ParameterizedType aParameterizedType = (ParameterizedType) aMethod.getGenericReturnType();
                        Type[] types = aParameterizedType.getActualTypeArguments();
                        if ( types.length == 1 )
                        {
                            Class clazz = (Class) types[0];
                            process( fieldsMetadata, name + "." + desc[i].getName() + "[]" + clazz.getSimpleName(),
                                     clazz, false );
                        }
                    }
                    process( fieldsMetadata, name + "." + desc[i].getName(), child, true );
                }
                else
                {
                    process( fieldsMetadata, name + "." + desc[i].getName(), child, false );
                }

            }
        }
    }

    static private void printUsage()
    {

        System.out.print( "Usage: " );
        System.out.print( "java fr.opensagres.xdocreport.template.formatter.cli.MetadataExtractor" );
        System.out.print( " -rootClass <a class>" );
        System.out.println( " -out <a fileName>" );
        System.out.println( "Where 'a class' is present in the class path" );
        System.out.println( "Where 'a fileName' is optional a is 'fields.xml' by default" );
        System.exit( -1 );
    }
}
