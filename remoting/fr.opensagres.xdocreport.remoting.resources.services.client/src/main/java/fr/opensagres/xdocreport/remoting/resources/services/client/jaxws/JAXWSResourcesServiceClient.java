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
package fr.opensagres.xdocreport.remoting.resources.services.client.jaxws;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import jakarta.xml.ws.Service;
import jakarta.xml.ws.soap.SOAPBinding;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.jaxws.JAXWSResourcesService;

public class JAXWSResourcesServiceClient
    implements JAXWSResourcesService
{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( JAXWSResourcesServiceClient.class.getName() );

    private final JAXWSResourcesService client;

    public JAXWSResourcesServiceClient( String baseAddress, String user, String password, Long connectionTimeout,
                                        Boolean allowChunking )
    {
        QName serviceName =
            new QName( "http://services.resources.remoting.xdocreport.opensagres.fr/", "ResourcesServiceService" );
        QName portName =
            new QName( "http://services.resources.remoting.xdocreport.opensagres.fr/", "ResourcesServicePort" );

        Service service = Service.create( serviceName );
        service.addPort( portName, SOAPBinding.SOAP11HTTP_BINDING, baseAddress );
        this.client = service.getPort( portName, JAXWSResourcesService.class );

        // Specific code with CXF
        initializeClient( user, password, connectionTimeout, allowChunking );
    }

    private void initializeClient( String user, String password, Long connectionTimeout, Boolean allowChunking )
    {
        Client cxfClient = ClientProxy.getClient( client );
        //
        boolean hasPolicy = connectionTimeout != null || allowChunking != null;
        if ( StringUtils.isNotEmpty( user ) || hasPolicy )
        {
            HTTPConduit http = (HTTPConduit) cxfClient.getConduit();
            if ( hasPolicy )
            {
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
                        
            AuthorizationPolicy authorizationPolicy = http.getAuthorization();
            // passage du user + password a l'entete HTTP.
            if ( StringUtils.isNotEmpty( user ) )
            {
                authorizationPolicy.setUserName( user );
            }
            if ( StringUtils.isNotEmpty( password ) )
            {
                authorizationPolicy.setPassword( password );
            }
        }

        if ( LOGGER.isLoggable( Level.FINE ) )
        {
            // Logs SOAP messages IN/OUT
            cxfClient.getEndpoint().getInInterceptors().add( new LoggingInInterceptor() );
            cxfClient.getEndpoint().getOutInterceptors().add( new LoggingOutInterceptor() );
        }
    }

    public String getName()
    {
        return client.getName();
    }

    public Resource getRoot()
    {
        return client.getRoot();
    }

    public Resource getRootWithFilter( Filter filter )
        throws ResourcesException
    {
        return client.getRootWithFilter( filter );
    }

    public List<BinaryData> downloadMultiple( List<String> resourceIds )
        throws ResourcesException
    {
        return client.downloadMultiple( resourceIds );
    }

    public BinaryData download( String resourceId )
        throws ResourcesException
    {
        return client.download( resourceId );
    }

    public void upload( BinaryData data )
        throws ResourcesException
    {
        client.upload( data );

    }
}
