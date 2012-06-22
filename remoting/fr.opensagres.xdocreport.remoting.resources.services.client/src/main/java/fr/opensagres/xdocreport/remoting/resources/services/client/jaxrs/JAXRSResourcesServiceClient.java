/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.LargeBinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServiceName;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.Providers;

public class JAXRSResourcesServiceClient
    implements JAXRSResourcesService
{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( JAXRSResourcesServiceClient.class.getName() );

    private final WebClient client;

    public JAXRSResourcesServiceClient( String baseAddress, String username, String password, Long connectionTimeout,
                                        Boolean allowChunking )
    {

        JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setAddress( baseAddress );

        if ( StringUtils.isNotEmpty( username ) )
        {
            // setup basic auth
            bean.setUsername( username );
            bean.setPassword( password );
        }

        // registrer here client side providers
        bean.setProviders( Providers.get() );

        this.client = bean.createWebClient();

        if ( connectionTimeout != null || allowChunking != null )
        {
            ClientConfiguration config = WebClient.getConfig( client );
            HTTPConduit http = (HTTPConduit) config.getConduit();
            HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
            if ( connectionTimeout != null )
            {
                httpClientPolicy.setConnectionTimeout( connectionTimeout );
            }
            if ( allowChunking != null )
            {
                httpClientPolicy.setAllowChunking( allowChunking );
            }
            http.setClient( httpClientPolicy );

        }

        if ( LOGGER.isLoggable( Level.FINE ) )
        {
            ClientConfiguration config = WebClient.getConfig( client );
            config.getInInterceptors().add( new LoggingInInterceptor() );
            config.getOutInterceptors().add( new LoggingOutInterceptor() );
        }
    }

    public String getName()
    {
        reset();
        return client.path( ResourcesServiceName.name ).accept( MediaType.TEXT_PLAIN ).get( String.class );
    }

    public Resource getRoot()
    {
        reset();
        return client.path( ResourcesServiceName.root ).accept( MediaType.APPLICATION_JSON ).get( Resource.class );
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
        reset();
        StringBuilder path = new StringBuilder( ResourcesServiceName.download.name() );
        path.append( "/" );
        path.append( resourceId );
        return client.path( path.toString() ).accept( MediaType.APPLICATION_JSON_TYPE ).get( BinaryData.class );
    }

    public void upload( BinaryData data )
    {
        reset();
        // Use Void.class to throw an exception of there is HTTP error.s
        client.path( ResourcesServiceName.upload.name() ).accept( MediaType.TEXT_PLAIN ).type( MediaType.APPLICATION_JSON ).post( data,Void.class );
    }

    protected void reset()
    {
        client.reset();

    }

	
	public LargeBinaryData downloadLarge(String resourceId)
			throws ResourcesException {
		reset();
		
		
        StringBuilder path = new StringBuilder( "downloadLarge" );
        path.append( "/" );
        path.append( resourceId );
        return client.path( path.toString() ).accept( MediaType.APPLICATION_JSON_TYPE ).get( LargeBinaryData.class );
	}


	public void uploadLarge(LargeBinaryData data) throws ResourcesException {
	    reset();
        // Use Void.class to throw an exception of there is HTTP error.s
        client.path( "uploadLarge" ).accept( MediaType.TEXT_PLAIN ).type( MediaType.APPLICATION_JSON ).post( data,Void.class );
		
	}
}
