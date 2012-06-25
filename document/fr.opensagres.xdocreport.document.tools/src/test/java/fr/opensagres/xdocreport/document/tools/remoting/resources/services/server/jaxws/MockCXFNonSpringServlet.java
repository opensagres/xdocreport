package fr.opensagres.xdocreport.document.tools.remoting.resources.services.server.jaxws;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.ws.Endpoint;

import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import fr.opensagres.xdocreport.document.tools.remoting.resources.services.MockResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.server.jaxws.JAXWSResourcesServiceImpl;

public class MockCXFNonSpringServlet
    extends CXFNonSpringServlet
{

    @Override
    public void init( ServletConfig sc )
        throws ServletException
    {
        super.init( sc );
        String address = "/resources";
        Endpoint e =
            javax.xml.ws.Endpoint.publish( address, new JAXWSResourcesServiceImpl( new MockResourcesService() ) );
        System.err.println( e );
    }
}
