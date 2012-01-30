package fr.opensagres.xdocreport.service.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class XDocreportApplication
    extends Application
{
    HashSet<Object> singletons = new HashSet<Object>();

    public XDocreportApplication()
    {
        singletons.add( new RESTXDocReportService() );
    }

    @Override
    public Set<Class<?>> getClasses()
    {
        HashSet<Class<?>> set = new HashSet<Class<?>>();
        return set;
    }

    @Override
    public Set<Object> getSingletons()
    {
        return singletons;
    }

}
