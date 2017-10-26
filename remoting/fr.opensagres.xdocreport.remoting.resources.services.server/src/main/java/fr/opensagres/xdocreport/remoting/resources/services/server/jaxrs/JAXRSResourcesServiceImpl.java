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
package fr.opensagres.xdocreport.remoting.resources.services.server.jaxrs;

import java.util.List;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.LargeBinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.DelegateResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;

public class JAXRSResourcesServiceImpl
    extends DelegateResourcesService
    implements JAXRSResourcesService
{

    public JAXRSResourcesServiceImpl( ResourcesService delegate )
    {
        super( delegate );
    }

    @Override
    public String getName()
    {
        return super.getName();
    }

    @Override
    public Resource getRoot()
        throws ResourcesException
    {
    	return super.getRoot();
    }

    @Override
    public Resource getRootWithFilter( Filter filter )
        throws ResourcesException
    {
        return super.getRootWithFilter( filter );
    }

    @Override
    public BinaryData download( String resourceId )
        throws ResourcesException
    {
        return super.download( resourceId );

    }

    @Override
    public List<BinaryData> downloadMultiple( List<String> resourceIds )
        throws ResourcesException
    {
        return super.downloadMultiple( resourceIds );
    }

    @Override
    public void upload( BinaryData dataIn )
        throws ResourcesException
    {
        super.upload( dataIn );
    }

    public LargeBinaryData downloadLarge( String resourceId )
        throws ResourcesException
    {
        return getDelegate().downloadLarge( resourceId );
    }

    @Override
    protected JAXRSResourcesService getDelegate()
    {
        return (JAXRSResourcesService) super.getDelegate();
    }

    public void uploadLarge( LargeBinaryData data )
        throws ResourcesException
    {
        getDelegate().uploadLarge( data );

    }

}
