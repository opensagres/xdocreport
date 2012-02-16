/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.opensagres.xdocreport.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 *
 * @author pascalleclercq
 */
public class Controller {

    public static final Controller INSTANCE = new Controller();
    private URL xdocReportURL;

    public List getTemplateList() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @return the xdocReportURL
     */
    public URL getXdocReportURL() {
        return xdocReportURL;
    }


    /**
     * Validate the URL and If Ok update the XDocReport URL
     * @param strURL the strURL to set
     */
    public void validateAndUpdateURL(String strURL) throws MalformedURLException, IOException {

        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();
        conn.connect();
        this.xdocReportURL = url;
    }
}
