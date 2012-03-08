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

package fr.opensagres.xdocreport.remoting.resources.services.rest.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServiceName;
import fr.opensagres.xdocreport.remoting.resources.services.rest.Providers;

public class JAXRSResourcesServiceClient
    implements ResourcesService
{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( JAXRSResourcesServiceClient.class.getName() );

    private final WebClient client;

    public JAXRSResourcesServiceClient( String baseAddress, String username, String password )
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

        // I don't know why, but httpClientPolicy.setAllowChunking(false);
        // must be done to manage /upload with the WebApp demo
        // at http://xdocreport.opensagres.cloudbees.net/cxf otherwise we have every time
        // an HHTP error 411
        ClientConfiguration config = WebClient.getConfig( client );
        HTTPConduit http = (HTTPConduit) config.getConduit();
        // Turn off chunking so that NTLM can occur
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        // httpClientPolicy.setConnectionTimeout(36000);
        httpClientPolicy.setAllowChunking( false );
        http.setClient( httpClientPolicy );

        if ( LOGGER.isLoggable( Level.FINE ) )
        {
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

    public Resource getRoot( Filter filter )
    {
        reset();
        return null;
    }

    public List<BinaryData> download( List<String> resourceIds )
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
        Response response =
            client.path( ResourcesServiceName.upload.name() ).accept( MediaType.TEXT_PLAIN ).type( MediaType.APPLICATION_JSON ).post( data );
        // TODO : display status of the response.
    }

    protected void reset()
    {
        client.reset();

    }
}
