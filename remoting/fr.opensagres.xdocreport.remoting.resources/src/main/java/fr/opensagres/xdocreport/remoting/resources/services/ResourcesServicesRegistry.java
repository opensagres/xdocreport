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
package fr.opensagres.xdocreport.remoting.resources.services;

import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;

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
        this.jaxRsResourcesService.clear();
    }
}
