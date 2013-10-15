package fr.opensagres.xdocreport.core;

import java.io.InputStream;
import java.util.Properties;

public abstract class Platform {

	private static String version;
	public static String getVersion() {
		if (version != null) {
	        return version;
	    }

	    // try to load from maven properties first
	    try {
	        Properties p = new Properties();
	        InputStream is = ClassLoader.getSystemResourceAsStream("META-INF/maven/fr.opensagres.xdocreport/fr.opensagres.xdocreport.core/pom.properties");
	        if (is != null) {
	            p.load(is);
	            version = p.getProperty("version", "");
	        }
	    } catch (Exception e) {
	        // ignore
	    }

	    // fallback to using Java API
	    if (version == null) {
	        Package aPackage = Platform.class.getPackage();
	        if (aPackage != null) {
	            version = aPackage.getImplementationVersion();
	            if (version == null) {
	                version = aPackage.getSpecificationVersion();
	            }
	        }
	    }

	    if (version == null) {
	        // we could not compute the version so use a blank
	        version = "not found";
	    }

	    return version;
	} 

}
