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
package fr.opensagres.xdocreport.remoting.resources.services.server.jaxrs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.ws.rs.core.Application;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServicesRegistry;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.Providers;

public class JAXRSResourcesApplication
    extends Application
{
    private HashSet<Object> singletons;

    public JAXRSResourcesApplication()
    {
        this.singletons = null;
    }

    public Set<Class<?>> getClasses()
    {
        HashSet<Class<?>> set = new HashSet<Class<?>>();
        set.add(com.fasterxml.jackson.jakarta.rs.json.JacksonXmlBindJsonProvider.class);
        return set;
    }

    public Set<Object> getSingletons()
    {
        loadIfNeed();
        return singletons;
    }

    private void loadIfNeed()
    {
        if ( singletons != null )
        {
            return;
        }
        load();
    }

    private synchronized void load()
    {
        if ( singletons != null )
        {
            return;
        }

        HashSet<Object> singletons = new HashSet<Object>();
        // register here Providers
        singletons.addAll( Providers.get() );
        
        List<JAXRSResourcesService> services = getRegistry().getJaxRsResourcesService();
        for ( final ResourcesService service : services )
        {
            singletons.add( new JAXRSResourcesServiceImpl( service ) );
        }

        this.singletons = singletons;
    }

    protected ResourcesServicesRegistry getRegistry()
    {
        return ResourcesServicesRegistry.getRegistry();
    }
}
