/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.opensagres.xdocreport;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStatusListener;
import com.sun.star.util.URL;
import fr.opensagres.xdocreport.controller.Controller;
import fr.opensagres.xdocreport.gui.ConfigureXDocReportURL;
import fr.opensagres.xdocreport.gui.ListTemplateDialog;
import fr.opensagres.xdocreport.util.Util;

/**
 *
 * @author pascalleclercq
 */
public class XDocReportQueryDispatch implements com.sun.star.frame.XDispatch {

    Runnable openSettings = new Runnable() {

        public void run() {
            ConfigureXDocReportURL configureXDocReportURL = new ConfigureXDocReportURL(null, true);
            configureXDocReportURL.setVisible(true);
        }
    };
    Runnable listExisting = new Runnable() {

        public void run() {
            ListTemplateDialog listTemplateDialog = new ListTemplateDialog(null, true);
            listTemplateDialog.setVisible(true);
        }
    };

    public void dispatch(URL aURL, PropertyValue[] arg1) {
        if (aURL.Protocol.compareTo("fr.opensagres.xdocreport.xdocreportdesigntool:") == 0) {
            if (aURL.Path.compareTo("XDocReport") == 0) {
                startNewThread(openSettings);
                return;
            } else if (aURL.Path.compareTo("ListExisting") == 0) {
                if (Controller.INSTANCE.getXdocReportURL() != null) {
                    startNewThread(listExisting);
                } else {
                    startNewThread(openSettings);
                }

                return;
            }

        }
    }

    public void addStatusListener(XStatusListener arg0, URL arg1) {
    }

    public void removeStatusListener(XStatusListener arg0, URL arg1) {
    }

    //PLQ
    private void startNewThread(Runnable runnable) {
        // Configuration.setClassLoader(this.getClass().getClassLoader());
        Util.startNewThread(getClass().getClassLoader(), runnable);
    }
}
