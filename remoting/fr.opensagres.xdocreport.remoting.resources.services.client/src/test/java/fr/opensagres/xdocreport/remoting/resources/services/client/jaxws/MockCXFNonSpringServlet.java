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

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.xml.ws.Endpoint;

import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import fr.opensagres.xdocreport.remoting.resources.services.MockResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.server.jaxws.JAXWSResourcesServiceImpl;

public class MockCXFNonSpringServlet
    extends CXFNonSpringServlet
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4030924536903714778L;

	@Override
    public void init( ServletConfig sc )
        throws ServletException
    {
        super.init( sc );
        String address = "/resources";
        Endpoint e= jakarta.xml.ws.Endpoint.publish( address,
                                       new JAXWSResourcesServiceImpl(
                                                                      new MockResourcesService(
                                                                                                JAXWSResourcesServiceClientTestCase.resourcesDir ) ) );
        System.err.println(e);
    }
}
