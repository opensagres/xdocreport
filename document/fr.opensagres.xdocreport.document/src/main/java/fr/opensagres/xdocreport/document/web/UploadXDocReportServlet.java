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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.servlet5.JakartaServletFileUpload;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;

/**
 * Class to upload files (odt, docx..) and register the report in the {@link XDocReportRegistry}.
 */
public class UploadXDocReportServlet
    extends BaseXDocReportServlet
{

    private static final long serialVersionUID = 9102651291455406387L;

    private static final String LOADREPORT_JSP = "loadReport.jsp";

    @Override
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        doUpload( request, response );
    }

    /**
     * Handles all requests (by default).
     * 
     * @param request HttpServletRequest object containing client request
     * @param response HttpServletResponse object for the response
     */
    protected void doUpload( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        boolean isMultipart = JakartaServletFileUpload.isMultipartContent( request );

        if ( isMultipart )
        {

            // Create a factory for disk-based file items
            DiskFileItemFactory factory = DiskFileItemFactory.builder().get();

            // Create a new file upload handler
            JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload = new JakartaServletFileUpload<>( factory );

            // Parse the request
            try
            {
                List<DiskFileItem> items = upload.parseRequest( request );
                for ( Iterator<DiskFileItem> iterator = items.iterator(); iterator.hasNext(); )
                {

                    DiskFileItem fileItem = iterator.next();

                    if ( "uploadfile".equals( fileItem.getFieldName() ) )
                    {

                        InputStream in = fileItem.getInputStream();
                        try
                        {
                            String reportId = generateReportId( fileItem, request );
                            IXDocReport report = getRegistryForUpload( request ).loadReport( in, reportId );

                            // Check if report id exists in global registry
                            getRegistry( request ).checkReportId( report.getId() );
                            reportLoaded( report, request );
                            doForward( report, request, response );
                            break;
                        }
                        catch ( XDocReportException e )
                        {
                            throw new ServletException( e );
                        }
                    }
                }
            }
            catch ( FileUploadException e )
            {
                throw new ServletException( e );
            }

        }
    }

    protected void reportLoaded( IXDocReport report, HttpServletRequest request )
    {
        // Do Nothing
    }

    protected String generateReportId( FileItem<?> fileItem, HttpServletRequest request )
    {
        String reportId = fileItem.getName();
        // test if report id has slash (when document is uploaded with IE,
        // fileItem.getName() is the full path of teh uploaded file).
        int index = reportId.lastIndexOf( '/' );
        if ( index == -1 )
        {
            index = reportId.lastIndexOf( '\\' );
        }
        if ( index != -1 )
        {
            reportId = reportId.substring( index + 1, reportId.length() );
        }
        return getRegistry( request ).generateUniqueReportId( reportId );
    }

    protected void doForward( IXDocReport report, HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        if ( report != null )
        {
            request.setAttribute( XDOCREPORT_ATTR_KEY, report );
        }
        request.getRequestDispatcher( LOADREPORT_JSP ).forward( request, response );
    }

    protected XDocReportRegistry getRegistryForUpload( HttpServletRequest request )
    {
        return super.getRegistry( request );
    }

}
