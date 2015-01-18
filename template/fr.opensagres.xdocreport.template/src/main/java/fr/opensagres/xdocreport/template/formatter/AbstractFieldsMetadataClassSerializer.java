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
package fr.opensagres.xdocreport.template.formatter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;
import java.lang.reflect.WildcardType;

/**
 * Abstract class for Fields metadata serializer.
 */
public abstract class AbstractFieldsMetadataClassSerializer
    implements IFieldsMetadataClassSerializer
{

    private static Class IIMAGEPROVIDER_CLASS = null;
    static
    {
        try
        {
            IIMAGEPROVIDER_CLASS = Class.forName( "fr.opensagres.xdocreport.document.images.IImageProvider" );
        }
        catch ( ClassNotFoundException e )
        {
            e.printStackTrace();
        }
    }

    private final String id;

    private final String description;

    // package name to exclude while processing
    private final List<String> excludedPackages;

    private IPropertyDescriptorFilter filter = new AllowAllPropertyDescriptorFilter();;
    
    public AbstractFieldsMetadataClassSerializer( String id, String description )
    {
        this.id = id;
        this.description = description;
        this.excludedPackages = new ArrayList<String>();
        this.excludedPackages.add( "java." );
    }

    public String getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    
    public void setFilter(IPropertyDescriptorFilter filter) {
		this.filter = filter;
	}
    
    public void load( FieldsMetadata fieldsMetadata, String key, Class<?> clazz )
        throws XDocReportException
    {
        load( fieldsMetadata, key, clazz, false );
    }

    public void load( FieldsMetadata fieldsMetadata, String key, Class<?> clazz, boolean listType )
        throws XDocReportException
    {
        try
        {
            List<PropertyDescriptor> path = new ArrayList<PropertyDescriptor>();
            process( fieldsMetadata, key, clazz, path, listType );
        }
        catch ( Exception e )
        {
            throw new XDocReportException( e );
        }
    }

    private void process( FieldsMetadata fieldsMetadata, String key, Class<?> clazz, List<PropertyDescriptor> path,
                          boolean isList )
        throws IntrospectionException
    {

        BeanInfo infos = Introspector.getBeanInfo( clazz );
        PropertyDescriptor[] descs = infos.getPropertyDescriptors();

        for ( PropertyDescriptor propertyDescriptor : descs )
        {

            // should not be transient and should have getter method
            if ( isTransient( propertyDescriptor, clazz ) || !haveGetterMethod( propertyDescriptor ) )
                continue;

            Method method = propertyDescriptor.getReadMethod();
            Class<?> returnTypeClass = method.getReturnType();

            // if this current property is already in path, we have to continue without it.
            boolean wasVisited = false;
            for ( PropertyDescriptor item : path )
            {
                if ( item.equals( propertyDescriptor ) )
                {
                    wasVisited = true;
                    break;
                }
            }

            if ( wasVisited )
                continue;
            
            if(filter.test(propertyDescriptor)) {
            	return;
            }

            // process the field
            if ( Iterable.class.isAssignableFrom( returnTypeClass ) )
            {
                Type collectionType = method.getGenericReturnType();
                if ( collectionType != null && ( collectionType instanceof ParameterizedType ) )
                {
                    ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
                    Type[] types = parameterizedType.getActualTypeArguments();
                    if ( types.length == 1 )
                    {
                        Class<?> itemClazz = null;
                        if ( types[0] instanceof WildcardType )
                        {
                            // WildcardType cannot be cast to Class
                            WildcardType wildcardType = (WildcardType) types[0];
                            if ( wildcardType.getLowerBounds().length != 0 )
                            {
                                itemClazz = (Class<?>) wildcardType.getLowerBounds()[0];
                            }
                            else if ( wildcardType.getUpperBounds().length != 0 )
                            {
                                itemClazz = (Class<?>) wildcardType.getUpperBounds()[0];
                            }
                        }
                        else
                        {
                            itemClazz = (Class<?>) types[0];
                        }
                        if ( itemClazz != null )
                        {
                            if ( isImageField( itemClazz ) )
                            {// add image field as is
                                addField( key, fieldsMetadata, path, propertyDescriptor, true, true );
                            }
                            else if ( isTextField( itemClazz ) )
                            {// add text field as is
                                addField( key, fieldsMetadata, path, propertyDescriptor, true, false );
                            }
                            else
                            {// continue building with this class
                                path.add( propertyDescriptor );
                                process( fieldsMetadata, key, itemClazz, path, true );
                                path.remove( propertyDescriptor );
                            }
                        }
                    }
                }
            }
            else if ( isImageField( returnTypeClass ) )
            {// add image field
                addField( key, fieldsMetadata, path, propertyDescriptor, isList, true );
            }
            else if ( isTextField( returnTypeClass ) )
            {// add text field

                addField( key, fieldsMetadata, path, propertyDescriptor, isList, false );
            }
            else
            {// continue building with this class
                path.add( propertyDescriptor );
                process( fieldsMetadata, key, returnTypeClass, path, isList );
                path.remove( propertyDescriptor );
            }
        }
    }

    private boolean isTextField( Class<?> returnTypeClass )
    {
        return String.class.isAssignableFrom( returnTypeClass ) || isClassToExclude( returnTypeClass );
    }

    private boolean isImageField( Class<?> returnTypeClass )
    {
        return InputStream.class.isAssignableFrom( returnTypeClass ) || byte[].class.isAssignableFrom( returnTypeClass )
            || File.class.isAssignableFrom( returnTypeClass )
            || ( IIMAGEPROVIDER_CLASS != null && IIMAGEPROVIDER_CLASS.isAssignableFrom( returnTypeClass ) );
    }

    /**
     * @param key - it is constant for whole process, this key is defined by user
     * @param fieldsMetadata - destination for fields
     * @param path - list of fields which should be accessed to read current field
     * @param currentField - field to be added to fieldsMetadata
     * @param proDesc - Property Descriptor. Used to retrieve getter annotations to be able to manage styles
     * @param isList true if field is list and false otherwise.
     * @param isImage true if field is an image and false otherwise.
     */
    private void addField( String key, FieldsMetadata fieldsMetadata, List<PropertyDescriptor> path,
                           PropertyDescriptor currentField, Boolean isList, boolean isImage )
    {
        String fieldName = createFieldName( key, path, currentField );

        // check if field is already there
        for ( fr.opensagres.xdocreport.template.formatter.FieldMetadata fieldMetadata : fieldsMetadata.getFields() )
            if ( fieldMetadata.getFieldName().equals( fieldName ) )
                return;

        Method method = currentField.getReadMethod();
        FieldMetadata fMetadata = method.getAnnotation( FieldMetadata.class );

        if ( fMetadata != null )
        {
            //
            ImageMetadata[] images = fMetadata.images();
            if ( images.length < 1 )
            {
                addFieldAndUpdateWithAnnotation( fieldsMetadata, isList, fieldName, null, fMetadata );
            }
            else
            {
                for ( int i = 0; i < images.length; i++ )
                {
                    addFieldAndUpdateWithAnnotation( fieldsMetadata, isList, fieldName, images[i], fMetadata );
                }
            }
        }
        else
        {
            String imageName = isImage ? fieldName : null;
            fieldsMetadata.addField( fieldName, isList, imageName, null, null );
        }

    }

    private String createFieldName( String key, List<PropertyDescriptor> path, PropertyDescriptor currentField )
    {
        StringBuilder fieldName = new StringBuilder( key );
        for ( PropertyDescriptor fieldFromPath : path )
        {
            fieldName.append( getFieldName( "", fieldFromPath.getName() ) );
        }

        // generate field path
        fieldName.append( getFieldName( "", currentField.getName() ) );
        return fieldName.toString();
    }

    private void addFieldAndUpdateWithAnnotation( FieldsMetadata fieldsMetadata, Boolean isList, String fieldName,
                                                  ImageMetadata imageData, FieldMetadata fMetadata )
    {
        String imageName = null;
        if ( imageData != null )
        {
            imageName = imageData.name();
        }
        fr.opensagres.xdocreport.template.formatter.FieldMetadata newField =
            fieldsMetadata.addField( fieldName.toString(), isList, imageName, fMetadata.syntaxKind(),
                                     fMetadata.syntaxWithDirective() );
        // description from annotation
        newField.setDescription( fMetadata.description() );
        if ( imageData != null )
        {
            newField.setBehaviour( imageData.behaviour() );
        }
    }

    /**
     * This method check if the propertyDescriptor is transient in Class clazz. It will go upper in hierarchy tree, also
     * as taking in consideration the implemented interfaces, as for clazz, as and for implemented interfaces by super
     * classes.
     * 
     * @param propertyDescriptor
     * @param clazz
     * @return true if the property from propertyDescriptor in class clazz is transient
     */
    private boolean isTransient( PropertyDescriptor propertyDescriptor, Class<?> clazz )
    {
        try
        {
            if ( clazz != null )
            {
                Field field = clazz.getDeclaredField( propertyDescriptor.getName() );
                return Modifier.isTransient( field.getModifiers() );
            }
            else
            {// if we are here this mean we processed
             // whole tree for class and interfaces and
             // not found field, we will consider that it is just an get
             // Method which can't have transient modifier
                return false;
            }
        }
        catch ( SecurityException e )
        {// if we have no access because of
         // security, we will mark it as
         // transient, as from
         // template engine we also will not be able to access it
            return true;
        }
        catch ( NoSuchFieldException e )
        {
            // in this case we will go upper with parent and interfaces
            // check parent class
            if ( isTransient( propertyDescriptor, clazz.getSuperclass() ) )
                return true;
            // check implemented interfaces
            Class<?>[] interfaces = clazz.getInterfaces();
            for ( Class<?> _interfase : interfaces )
            {
                if ( isTransient( propertyDescriptor, _interfase ) )
                    return true;
            }
            return false;
        }

    }

    private boolean haveGetterMethod( PropertyDescriptor propertyDescriptor )
    {
        Method method = propertyDescriptor.getReadMethod();
        return isGetterMethod( method );
    }

    /**
     * Return true if package of the given class start with list of package to exclude and false otherwise.
     * 
     * @param clazz
     * @return
     */
    private boolean isClassToExclude( Class<?> clazz )
    {
        if ( clazz != null && clazz.getPackage() != null )
        {
            String packageName = clazz.getPackage().getName();
            for ( String excludePackageName : excludedPackages )
            {
                if ( packageName.startsWith( excludePackageName ) )
                    return true;
            }
        }
        return false;
    }

    private boolean isGetterMethod( Method method )
    {
        if ( method == null )
        {
            return false;
        }
        String name = method.getName();
        return !name.equals( "getClass" ) && ( name.startsWith( "get" ) || name.startsWith( "is" ) );
    }

    
    
    protected abstract String getFieldName( String key, String getterName );
    

    static class AllowAllPropertyDescriptorFilter implements IPropertyDescriptorFilter {

		public boolean test(PropertyDescriptor descriptor) {
			return false;
		}
    	
    }
}
