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
package fr.opensagres.xdocreport.remoting.resources.services;

import java.util.List;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;

/**
 * Interface to define resources services to retrieves from a repository list of resources (folder/files), and
 * upload/download some resources.
 */
public interface ResourcesService
{

    /**
     * Returns the repository name.
     * 
     * @return
     */
    String getName();

    /**
     * Returns the root resource.
     * 
     * @return
     */
    Resource getRoot()
        throws ResourcesException;

    Resource getRootWithFilter( Filter filter )
        throws ResourcesException;

    List<BinaryData> downloadMultiple( List<String> resourceIds )
        throws ResourcesException;

    /**
     * Download the content of the given unique resource id.
     * 
     * @param resourcePath the unique resource id.
     * @return the byte array of the content of the given resourcePath.
     */
    BinaryData download( String resourceId )
        throws ResourcesException;

    void upload( BinaryData data )
        throws ResourcesException;

}