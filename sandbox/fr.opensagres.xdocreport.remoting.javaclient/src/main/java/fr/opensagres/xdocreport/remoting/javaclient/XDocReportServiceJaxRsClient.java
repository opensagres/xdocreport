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
package fr.opensagres.xdocreport.remoting.javaclient;

import java.util.List;

import jakarta.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;

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
    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    public void upload( ReportRepresentation report )
    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    public byte[] processReport( ReportAndDataRepresentation reportAndDataRepresentation )

    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    public void unRegister( String reportId )
    {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}
