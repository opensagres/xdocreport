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
package fr.opensagres.xdocreport.remoting.resources.services;

import fr.opensagres.xdocreport.remoting.resources.services.rest.client.JAXRSResourcesServiceClientFactory;
import fr.opensagres.xdocreport.remoting.resources.services.ws.client.JAXWSResourcesServiceClientFactory;

/**
 * Factory to create client {@link ResourcesService} for REST or SOAP.
 */
public class ResourcesServiceClientFactory
{

    /**
     * Create an instance of the (REST or SOAP) client {@link ResourcesService}.
     * 
     * @param baseAddress base address of the REST/SOAP {@link ResourcesService}.
     * @param serviceType server type (REST or SOAP)
     * @param user user for authentication.
     * @param password user for authentication
     * @return an instance of the (REST or SOAP) client {@link ResourcesService}.
     */
    public static ResourcesService create( String baseAddress, ServiceType serviceType, String user, String password )
    {
        if ( serviceType == ServiceType.REST )
        {
            return JAXRSResourcesServiceClientFactory.create( baseAddress, user, password );
        }
        return JAXWSResourcesServiceClientFactory.create( baseAddress );
    }
}
