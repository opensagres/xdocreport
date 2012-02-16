/*
 * CentralRegistrationClass.java
 *
 * Created on 2012.02.09 - 15:36:36
 *
 */
package fr.opensagres.xdocreport;

import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 *
 * @author pascalleclercq
 */
public class CentralRegistrationClass {
    
    public static XSingleComponentFactory __getComponentFactory( String sImplementationName ) {
        String regClassesList = getRegistrationClasses();
        StringTokenizer t = new StringTokenizer(regClassesList, " ");
        while (t.hasMoreTokens()) {
            String className = t.nextToken();
            if (className != null && className.length() != 0) {
                try {
                    Class regClass = Class.forName(className);
                    Method writeRegInfo = regClass.getDeclaredMethod("__getComponentFactory", new Class[]{String.class});
                    Object result = writeRegInfo.invoke(regClass, sImplementationName);
                    if (result != null) {
                       return (XSingleComponentFactory)result;
                    }
                }
                catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (ClassCastException ex) {
                    ex.printStackTrace();
                } catch (SecurityException ex) {
                    ex.printStackTrace();
                } catch (NoSuchMethodException ex) {
                    ex.printStackTrace();
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean __writeRegistryServiceInfo( XRegistryKey xRegistryKey ) {
        boolean bResult = true;
        String regClassesList = getRegistrationClasses();
        StringTokenizer t = new StringTokenizer(regClassesList, " ");
        while (t.hasMoreTokens()) {
            String className = t.nextToken();
            if (className != null && className.length() != 0) {
                try {
                    Class regClass = Class.forName(className);
                    Method writeRegInfo = regClass.getDeclaredMethod("__writeRegistryServiceInfo", new Class[]{XRegistryKey.class});
                    Object result = writeRegInfo.invoke(regClass, xRegistryKey);
                    bResult &= ((Boolean)result).booleanValue();
                }
                catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (ClassCastException ex) {
                    ex.printStackTrace();
                } catch (SecurityException ex) {
                    ex.printStackTrace();
                } catch (NoSuchMethodException ex) {
                    ex.printStackTrace();
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return bResult;
    }

    private static String getRegistrationClasses() {
        CentralRegistrationClass c = new CentralRegistrationClass();
        String name = c.getClass().getCanonicalName().replace('.', '/').concat(".class");
        try {
            Enumeration<URL> urlEnum = c.getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (urlEnum.hasMoreElements()) {
                URL url = urlEnum.nextElement();
                String file = url.getFile();
                JarURLConnection jarConnection =
                    (JarURLConnection) url.openConnection();
                Manifest mf = jarConnection.getManifest();

                Attributes attrs = (Attributes) mf.getAttributes(name);
                if ( attrs != null ) {
                    String classes = attrs.getValue( "RegistrationClasses" );
                    return classes;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
            
        return "";
    }
    
    /** Creates a new instance of CentralRegistrationClass */
    private CentralRegistrationClass() {
    }
}
