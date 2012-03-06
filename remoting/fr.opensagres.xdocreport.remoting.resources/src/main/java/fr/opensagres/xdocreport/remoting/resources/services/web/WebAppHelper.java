package fr.opensagres.xdocreport.remoting.resources.services.web;

import java.io.File;

import javax.servlet.ServletContext;

public class WebAppHelper
{

    private static File webAppFolder = null;

    public static File getWebAppFolder( ServletContext servletContext )
    {
        if ( webAppFolder != null )
        {
            return webAppFolder;
        }
        webAppFolder = new File( servletContext.getRealPath( "/" ) );
        return webAppFolder;
    }

}
