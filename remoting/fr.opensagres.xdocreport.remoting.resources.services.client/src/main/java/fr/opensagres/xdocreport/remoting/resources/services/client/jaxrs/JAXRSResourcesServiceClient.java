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
package fr.opensagres.xdocreport.remoting.resources.services.client.jaxrs;

import java.util.List;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.LargeBinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServiceName;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;

public class JAXRSResourcesServiceClient  implements JAXRSResourcesService
{

    private final WebTarget target;

    public JAXRSResourcesServiceClient( Client client, String baseAddress)
    {
    	//client.register(Providers.get() );
    	target = client.target(baseAddress);

        // registrer here client side providers

    }

    public String getName()
    {
        reset();
        
        return target.path( ResourcesServiceName.name.name() ).request().accept( MediaType.TEXT_PLAIN ).get( String.class );
    }

    public Resource getRoot()
    {
        reset();
        
        return target.path( ResourcesServiceName.root.name()).request().accept(MediaType.APPLICATION_JSON).get(Resource.class);
		
    }

    public Resource getRootWithFilter( Filter filter )
    {
        reset();
        return null;
    }

    public List<BinaryData> downloadMultiple( List<String> resourceIds )
    {
        reset();
        return null;
    }

    public BinaryData download( String resourceId )
    {
    	//
        reset();
        return target.path( ResourcesServiceName.download.name() ).queryParam( "resourceId", resourceId ).request().accept(MediaType.APPLICATION_JSON_TYPE).get( BinaryData.class );
    }

    public void upload( BinaryData data )
    {
        reset();
        // Use Void.class to throw an exception of there is HTTP error.s
        //.type( MediaType.APPLICATION_JSON )
        target.path( ResourcesServiceName.upload.name() ).request().accept( MediaType.TEXT_PLAIN ).post(Entity.entity(data, MediaType.APPLICATION_JSON));
//        .post( data,
//                                                                                                                                  Void.class );
    }

    protected void reset()
    {
    	//target.reset();

    }

    public LargeBinaryData downloadLarge( String resourceId )
        throws ResourcesException
    {
    	//
        reset();
        return target.path( ResourcesServiceName.downloadLarge.name() ).queryParam( "resourceId", resourceId ).request().accept( MediaType.WILDCARD ).get( LargeBinaryData.class );
    }

    public void uploadLarge( LargeBinaryData data )
        throws ResourcesException
    {
        reset();
        // Use Void.class to throw an exception of there is HTTP error.s
        target.path( ResourcesServiceName.uploadLarge.name() ).request().accept( MediaType.TEXT_PLAIN ).post(Entity.entity(data, MediaType.APPLICATION_JSON));

    }

}
