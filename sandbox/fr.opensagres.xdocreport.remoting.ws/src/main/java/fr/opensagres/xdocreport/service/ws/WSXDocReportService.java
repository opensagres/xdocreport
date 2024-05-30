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
package fr.opensagres.xdocreport.service.ws;

import jakarta.jws.WebService;

import fr.opensagres.xdocreport.document.internal.XDocReportServiceImpl;

@WebService( serviceName = "WSXDocReportService", targetNamespace = "http://xdocreport.opensagres.fr" )
public class WSXDocReportService
    extends XDocReportServiceImpl
{

    // private XDocReportService delegate = XDocReportService.INSTANCE;
    //
    // public byte[] processReport(String documentID, DataContext[] d, Options o) throws WSException {
    // return delegate.processReport(documentID, d, o);
    // }
    //
    // public byte[] processReport(byte[] document, DataContext[] dataContext,
    // String templateEngineID, Options o) throws WSException {
    // return delegate.processReport(document, dataContext, templateEngineID,
    // o);
    // }
    //
    // public void upload(String reportID, byte[] content, String templateEngineID) throws WSException {
    // delegate.upload(reportID, content, templateEngineID);
    // }
    //
    // public byte[] download(String reportID) {
    // return delegate.download(reportID);
    // }
    //
    // public void unRegister(String reportID) {
    // delegate.unRegister(reportID);
    // }

}
