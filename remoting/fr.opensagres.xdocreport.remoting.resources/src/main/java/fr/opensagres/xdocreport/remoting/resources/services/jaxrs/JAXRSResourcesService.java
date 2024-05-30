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
package fr.opensagres.xdocreport.remoting.resources.services.jaxrs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.LargeBinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

@Path( "/" )
public interface JAXRSResourcesService
    extends ResourcesService
{

    @GET
    @Path( "/name" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.TEXT_PLAIN )
    String getName();

    @GET
    @Path( "/root" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    Resource getRoot()
        throws ResourcesException;

    @GET
    @Path( "/download" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.WILDCARD )
    BinaryData download( @QueryParam( "resourceId" )
    String resourceId )
        throws ResourcesException;

    @POST
    @Path( "/upload" )
    // @Consumes( MediaType.APPLICATION_JSON )
    void upload( BinaryData data )
        throws ResourcesException;

    @GET
    @Path( "/downloadLarge" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.WILDCARD )
    LargeBinaryData downloadLarge( @QueryParam( "resourceId" )
    String resourceId )
        throws ResourcesException;

    @POST
    @Path( "/uploadLarge" )
    // @Consumes( MediaType.APPLICATION_JSON )
    void uploadLarge( LargeBinaryData data )
        throws ResourcesException;

}
