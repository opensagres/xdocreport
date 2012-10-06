package fr.opensagres.xdocreport.converter.internal;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


public class ConverterApplication extends Application {



	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(ConverterResourceImpl.class);
        return classes;
    }

	@Override
	public Set<Object> getSingletons() {
		Set<Object> classes = new HashSet<Object>();
		classes.add(new BinaryFileMessageBodyWriter());
		return classes;
	}
}
