package fr.opensagres.xdocreport.remoting.converter.server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * JAX-RS Application to register the {@link ConverterServiceImpl}.
 */
public class ConverterApplication
    extends Application
{

    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add( ConverterServiceImpl.class );
        return classes;
    }

    @Override
    public Set<Object> getSingletons()
    {
        Set<Object> classes = new HashSet<Object>();
        return classes;
    }
}
