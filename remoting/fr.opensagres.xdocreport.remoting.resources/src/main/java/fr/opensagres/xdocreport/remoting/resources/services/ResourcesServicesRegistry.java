package fr.opensagres.xdocreport.remoting.resources.services;

import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.remoting.resources.services.rest.JAXRSResourcesService;

public class ResourcesServicesRegistry
{
    private final List<ResourcesService> services;


    private final List<JAXRSResourcesService> jaxRsResourcesService;

    public static final ResourcesServicesRegistry INSTANCE = new ResourcesServicesRegistry();

    public static ResourcesServicesRegistry getRegistry()
    {
        return INSTANCE;
    }

    protected ResourcesServicesRegistry()
    {
        this.services = new ArrayList<ResourcesService>();
        this.jaxRsResourcesService= new ArrayList<JAXRSResourcesService>();
    }

    public void addService( ResourcesService service )
    {
        this.services.add( service );
        if(service instanceof JAXRSResourcesService){
        	this.jaxRsResourcesService.add((JAXRSResourcesService)service);
        }
    }

    public List<ResourcesService> getServices()
    {
        return services;

    }

    public List<JAXRSResourcesService> getJaxRsResourcesService() {
		return jaxRsResourcesService;
	}

    public void clear()
    {
        this.services.clear();

    }
}
