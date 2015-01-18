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
