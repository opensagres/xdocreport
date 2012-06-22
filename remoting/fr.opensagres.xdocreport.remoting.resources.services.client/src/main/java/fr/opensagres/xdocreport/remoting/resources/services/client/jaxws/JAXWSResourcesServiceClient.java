package fr.opensagres.xdocreport.remoting.resources.services.client.jaxws;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

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
