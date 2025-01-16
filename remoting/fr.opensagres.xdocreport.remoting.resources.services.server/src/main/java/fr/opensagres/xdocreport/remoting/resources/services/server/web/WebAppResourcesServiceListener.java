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
package fr.opensagres.xdocreport.remoting.resources.services.server.web;

import java.io.File;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServicesRegistry;
import fr.opensagres.xdocreport.remoting.resources.services.server.file.FileResourcesService;

public abstract class WebAppResourcesServiceListener
    extends FileResourcesService
    implements ServletContextListener
{

    private ServletContext servletContext;

    //private File webAppFolder;

    private File rootFolder;

    public WebAppResourcesServiceListener()
    {
        this( false );
    }

    public WebAppResourcesServiceListener( boolean templateHierarchy )
    {
        super( null, templateHierarchy );
        this.servletContext = null;
        this.rootFolder = null;
        ResourcesServicesRegistry.getRegistry().addService( this );
    }

    public void contextInitialized( ServletContextEvent event )
    {
        this.servletContext = event.getServletContext();
    }

    public void contextDestroyed( ServletContextEvent event )
    {
        // Do Nothing
    }

    public ServletContext getServletContext()
    {
        return servletContext;
    }

    @Override
    public File getRootFolder()
    {
        if ( rootFolder != null )
        {
            return rootFolder;
        }
        File webAppFolder = getWebAppFolder( getServletContext() );
        rootFolder = getRootFolder( webAppFolder );
        return rootFolder;
    }

    protected File getWebAppFolder( ServletContext servletContext )
    {
        return WebAppHelper.getWebAppFolder( servletContext );
    }

    protected abstract File getRootFolder( File webAppFolder );

}
