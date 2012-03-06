package fr.opensagres.xdocreport.remoting.resources.services;

import java.util.ArrayList;
import java.util.List;

public class ResourcesServicesRegistry
{
    private final List<ResourcesService> services;

    public static final ResourcesServicesRegistry INSTANCE = new ResourcesServicesRegistry();

    public static ResourcesServicesRegistry getRegistry()
    {
        return INSTANCE;
    }

    protected ResourcesServicesRegistry()
    {
        this.services = new ArrayList<ResourcesService>();
    }

    public void addService( ResourcesService service )
    {
        this.services.add( service );
    }

    public List<ResourcesService> getServices()
    {
        return services;
    }

    public void clear()
    {
        this.services.clear();
        
    }
}
