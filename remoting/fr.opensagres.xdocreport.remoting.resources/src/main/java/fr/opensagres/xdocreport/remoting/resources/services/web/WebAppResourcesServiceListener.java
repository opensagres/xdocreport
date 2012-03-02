package fr.opensagres.xdocreport.remoting.resources.services.web;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServicesRegistry;
import fr.opensagres.xdocreport.remoting.resources.services.file.FileRepositoryService;

public abstract class WebAppResourcesServiceListener
    extends FileRepositoryService
    implements ServletContextListener
{

    private ServletContext servletContext;

    private File webAppFolder;

    private File rootFolder;

    public WebAppResourcesServiceListener()
    {
        super( null );
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
        if ( webAppFolder != null )
        {
            return webAppFolder;
        }
        webAppFolder = new File( servletContext.getRealPath( "/" ) );
        return webAppFolder;
    }

    protected abstract File getRootFolder( File webAppFolder );

}
