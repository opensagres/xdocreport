package fr.opensagres.xdocreport.remoting.javaclient;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.domain.ReportAndDataRepresentation;
import fr.opensagres.xdocreport.document.domain.ReportId;
import fr.opensagres.xdocreport.document.domain.ReportRepresentation;
import fr.opensagres.xdocreport.document.service.XDocReportService;

public class XDocReportServiceJaxRsClient implements XDocReportService
{

    public static final XDocReportServiceJaxRsClient INSTANCE= new XDocReportServiceJaxRsClient();

    private  String serviceEndpointUrl;

    public void setServiceEndpointUrl( String serviceEndpointUrl )
    {
        this.serviceEndpointUrl = serviceEndpointUrl;
    }

    public static void main( String[] args )
    {
        INSTANCE.setServiceEndpointUrl( "http://localhost:8080/demo-webapp/jaxrs" );
        System.out.println(INSTANCE.listReports());
    }


    @SuppressWarnings( "unchecked" )
    public List<ReportId> listReports()
    {
        WebClient client = WebClient.create( serviceEndpointUrl );
        client.path( "listReports" );
        client.accept( MediaType.APPLICATION_JSON );
        return (List<ReportId>) client.getCollection( ReportId.class );

    }

    public byte[] download( String reportID, String processState )
        throws XDocReportException
    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    public void upload( ReportRepresentation report )
        throws XDocReportException
    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    public byte[] processReport( ReportAndDataRepresentation reportAndDataRepresentation )
        throws XDocReportException
    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    public void unRegister( String reportId )
    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}
