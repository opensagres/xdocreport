/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.opensagres.xdocreport.controller;

import fr.opensagres.xdocreport.document.domain.ReportId;
import fr.opensagres.xdocreport.remoting.javaclient.XDocReportServiceJaxRsClient;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pascalleclercq
 */
public class Controller {

    public static final Controller INSTANCE = new Controller();
    private URL xdocReportURL;

    public List<ReportId> getTemplateList() {

        return XDocReportServiceJaxRsClient.INSTANCE.listReports();
    }

    /**
     * @return the xdocReportURL
     */
    public URL getXdocReportURL() {
        configureDefault();
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
        XDocReportServiceJaxRsClient.INSTANCE.setServiceEndpointUrl(strURL);
    }

    private void configureDefault() {
        if (xdocReportURL == null) {
            try {
                validateAndUpdateURL("http://xdocreport.opensagres.cloudbees.net/jaxrs");
            } catch (MalformedURLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
