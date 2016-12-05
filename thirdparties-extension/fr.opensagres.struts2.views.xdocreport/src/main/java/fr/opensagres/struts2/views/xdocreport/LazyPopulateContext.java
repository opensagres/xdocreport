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
package fr.opensagres.struts2.views.xdocreport;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class LazyPopulateContext
    extends AbstractPopulateContext
{

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private static final LazyPopulateContext INSTANCE = new LazyPopulateContext();

    public static LazyPopulateContext getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected PropertyDescriptor[] getPropertyDescriptors( Object pojo )
        throws Exception
    {
        BeanInfo info = Introspector.getBeanInfo( pojo.getClass() );
        return info.getPropertyDescriptors();
    }

    @Override
    protected Object getSimpleProperty( Object pojo, PropertyDescriptor descriptor )
        throws Exception
    {
        Method readMethod = getReadMethod( pojo.getClass(), descriptor );
        if ( readMethod == null )
        {
            return null;
        }
        return readMethod.invoke( pojo, EMPTY_OBJECT_ARRAY );
    }

    protected Method getReadMethod( Class clazz, PropertyDescriptor descriptor )
    {
        return ( getAccessibleMethod( clazz, descriptor.getReadMethod() ) );
    }

    /**
     * <p>
     * Return an accessible method (that is, one that can be invoked via reflection) that implements the specified
     * Method. If no such method can be found, return <code>null</code>.
     * </p>
     * 
     * @param clazz The class of the object
     * @param method The method that we wish to call
     * @return The accessible method
     * @since 1.8.0
     */
    protected Method getAccessibleMethod( Class clazz, Method method )
    {

        // Make sure we have a method to check
        if ( method == null )
        {
            return ( null );
        }

        // If the requested method is not public we cannot call it
        if ( !Modifier.isPublic( method.getModifiers() ) )
        {
            return ( null );
        }

        boolean sameClass = true;
        if ( clazz == null )
        {
            clazz = method.getDeclaringClass();
        }
        else
        {
            sameClass = clazz.equals( method.getDeclaringClass() );
            if ( !method.getDeclaringClass().isAssignableFrom( clazz ) )
            {
                throw new IllegalArgumentException( clazz.getName() + " is not assignable from "
                    + method.getDeclaringClass().getName() );
            }
        }

        // If the class is public, we are done
        if ( Modifier.isPublic( clazz.getModifiers() ) )
        {
            if ( !sameClass && !Modifier.isPublic( method.getDeclaringClass().getModifiers() ) )
            {
                // setMethodAccessible(method); // Default access superclass
                // workaround
            }
            return ( method );
        }

        return ( method );

    }

}
