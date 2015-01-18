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
package fr.opensagres.xdocreport.document.web;

/**
 * Constants for request and session HTTP parameters used in the {@link BaseXDocReportServlet}.
 */
public interface XDocBaseServletConstants
{

    // HTTP parameters name
    String REPORT_ID_HTTP_PARAM = "reportId";

    String TEMPLATE_ENGINE_KIND_HTTP_PARAM = "templateEngineKind";

    String TEMPLATE_ENGINE_ID_HTTP_PARAM = "templateEngineId";

    String ENTRY_NAME_HTTP_PARAM = "entryName";

    String PROCESS_STATE_HTTP_PARAM = "processState";

    String DISPATCH_HTTP_PARAM = "dispatch";

    // HTTP session keys
    String XDOCREPORTREGISTRY_SESSION_KEY = "XDocReportRegistry_session";

    String XDOCREPORT_ATTR_KEY = "XDocReport";

}
