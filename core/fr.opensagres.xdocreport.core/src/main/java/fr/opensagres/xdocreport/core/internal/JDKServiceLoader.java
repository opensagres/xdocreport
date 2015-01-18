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
package fr.opensagres.xdocreport.core.internal;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.core.logging.LogUtils;

/**
 * JDK ServiceLoader is used to load services declared in the META-INF/services/MyClass. Switch JDK using, it uses:
 * <ul>
 * <li><b>java.util.ServiceLoader</b> if XDocReport works on Java6. For example :
 * <p>
 * <code>Iterator<Discovery> discoveries =
                ServiceLoader.load( registryType, getClass().getClassLoader() ).iterator();</code>
 * </p>
 * </li>
 * <li><b>javax.imageio.spi.ServiceRegistry</b> if XDocReport works on Java5. For example :
 * <p>
 * <code>Iterator<Discovery> discoveries =
                ServiceRegistry.lookupProviders( registryType, getClass().getClassLoader() );</code>
 * </p>
 * </li>
 * </ul>
 */
public abstract class JDKServiceLoader
{

    private static final Logger LOGGER = LogUtils.getLogger( JDKServiceLoader.class.getName() );

    // The JDK Service loader to use.
    private static JDKServiceLoader JDK_SERVICE_LOADER;

    static
    {
        ClassLoader classLoader = JDKServiceLoader.class.getClassLoader();
        try
        {
            // At first, try to use JDK6 java.util.ServiceLoader
            JDK_SERVICE_LOADER = new JDK6ServiceLoader( classLoader );
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( "Uses JDK6 java.util.ServiceLoader to load services." );
            }
        }
        catch ( Throwable e )
        {
            // JDK6 is not used here, uses the JDK5 javax.imageio.spi.ServiceRegistry
            try
            {
                JDK_SERVICE_LOADER = new JDK5ServiceLoader( classLoader );
                if ( LOGGER.isLoggable( Level.FINE ) )
                {
                    LOGGER.fine( "Uses JDK5 javax.imageio.spi.ServiceRegistry to load services." );
                }
            }
            catch ( Throwable e1 )
            {
                // Should never thrown.
                if ( LOGGER.isLoggable( Level.SEVERE ) )
                {
                    LOGGER.log( Level.SEVERE, "Error while initialization of JDKServiceLoader", e1 );
                }
            }
        }
    }

    public static <T> Iterator<T> lookupProviders( Class<T> providerClass, ClassLoader loader )
    {
        try
        {
            return JDK_SERVICE_LOADER.lookupProvidersFromJDK( providerClass, loader );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    protected abstract <T> Iterator<T> lookupProvidersFromJDK( Class<T> providerClass, ClassLoader loader )
        throws Exception;

    private static class JDK5ServiceLoader
        extends JDKServiceLoader
    {
        private final Method lookupProvidersMethod;

        public JDK5ServiceLoader( ClassLoader classLoader )
            throws ClassNotFoundException, SecurityException, NoSuchMethodException
        {
            Class<?> slc = classLoader.loadClass( "javax.imageio.spi.ServiceRegistry" );
            lookupProvidersMethod = slc.getMethod( "lookupProviders", Class.class, ClassLoader.class );
        }

        @SuppressWarnings( "unchecked" )
        @Override
        protected <T> Iterator<T> lookupProvidersFromJDK( Class<T> providerClass, ClassLoader loader )
            throws Exception
        {
            return (Iterator<T>) lookupProvidersMethod.invoke( null, providerClass, loader );
        }
    }

    private static class JDK6ServiceLoader
        extends JDKServiceLoader
    {
        private Method loadMethod;

        private Method iteratorMethod;

        public JDK6ServiceLoader( ClassLoader classLoader )
            throws ClassNotFoundException, SecurityException, NoSuchMethodException
        {
            Class<?> slc = classLoader.loadClass( "java.util.ServiceLoader" );
            loadMethod = slc.getMethod( "load", Class.class, ClassLoader.class );
            iteratorMethod = slc.getMethod( "iterator" );
        }

        @SuppressWarnings( "unchecked" )
        @Override
        protected <T> Iterator<T> lookupProvidersFromJDK( Class<T> providerClass, ClassLoader loader )
            throws Exception
        {
            Object serviceLoader = loadMethod.invoke( null, providerClass, loader );
            return (Iterator<T>) iteratorMethod.invoke( serviceLoader );

        }
    }
}
