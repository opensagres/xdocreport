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
import java.io.UnsupportedEncodingException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.utils.HttpHeaderUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.ProcessState;
import fr.opensagres.xdocreport.document.registry.TemplateEngineInitializerRegistry;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

/**
 * Base class for XDoc Report servlet.
 */
public abstract class BaseXDocReportServlet
    extends HttpServlet
    implements XDocBaseServletConstants
{

    private static final long serialVersionUID = -5890298276937085849L;

    private String encoding;

    private boolean forceEncoding;

    @Override
    public void init( ServletConfig config )
        throws ServletException
    {
        super.init( config );
        this.encoding = config.getInitParameter( "encoding" );
        this.forceEncoding = StringUtils.asBoolean( config.getInitParameter( "forceEncoding" ), false );
    }

    /**
     * Handles HTTP <code>GET</code> requests by calling {@link #doRequest()}.
     */
    @Override
    public void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        prepareHTTPRequest( request );
        processRequest( request, response );
    }

    /**
     * Handles HTTP <code>POST</code> requests by calling {@link #doRequest()}.
     */
    @Override
    public void doPost( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        prepareHTTPRequest( request );
        processRequest( request, response );
    }

    private void prepareHTTPRequest( HttpServletRequest request )
        throws UnsupportedEncodingException
    {
        if ( this.encoding != null && ( this.forceEncoding || request.getCharacterEncoding() == null ) )
        {
            request.setCharacterEncoding( this.encoding );
        }
    }

    /**
     * Returns dispatch parameter value.
     * 
     * @param request
     * @return
     */
    protected String getDispatchParameter( HttpServletRequest request )
    {
        return request.getParameter( DISPATCH_HTTP_PARAM );
    }

    /**
     * Returns the id of the report.
     * 
     * @param request
     * @return
     */
    protected String getReportId( HttpServletRequest request )
    {
        return (String) request.getParameter( REPORT_ID_HTTP_PARAM );
    }

    /**
     * Returns process state (original|preprocessed|generated).
     * 
     * @param request
     * @return
     */
    protected ProcessState getProcessState( HttpServletRequest request )
    {
        String state = request.getParameter( PROCESS_STATE_HTTP_PARAM );
        if ( ProcessState.ORIGINAL.name().equalsIgnoreCase( state ) )
        {
            return ProcessState.ORIGINAL;
        }
        if ( ProcessState.PREPROCESSED.name().equalsIgnoreCase( state ) )
        {
            return ProcessState.PREPROCESSED;
        }
        if ( ProcessState.GENERATED.name().equalsIgnoreCase( state ) )
        {
            return ProcessState.GENERATED;
        }
        return null;
    }

    /**
     * Returns the entry name of the report.
     * 
     * @param request
     * @return
     */
    protected String getEntryName( HttpServletRequest request )
    {
        return (String) request.getParameter( ENTRY_NAME_HTTP_PARAM );
    }

    protected String getTemplateEngineKind( String reportId, HttpServletRequest request )
    {
        return getTemplateEngineKind( request );
    }

    protected String getTemplateEngineKind( HttpServletRequest request )
    {
        return (String) request.getParameter( TEMPLATE_ENGINE_KIND_HTTP_PARAM );
    }

    /**
     * Returns the template engine id from request.
     * 
     * @param request
     * @return
     */
    protected String getTemplateEngineId( HttpServletRequest request )
    {
        return (String) request.getParameter( TEMPLATE_ENGINE_ID_HTTP_PARAM );
    }

    /**
     * Returns the template engine to use for the report. By default, it search if there is template id from request and
     * otherwise returns the default template engine.
     * 
     * @param reportId
     * @param request
     * @return
     */
    protected ITemplateEngine getTemplateEngine( String reportId, HttpServletRequest request )
    {
        return getTemplateEngine( request );
    }

    protected ITemplateEngine getTemplateEngine( IXDocReport report, HttpServletRequest request )
    {
        String documentKind = report.getKind();
        String templateEngineKind = getTemplateEngineKind( request );
        ITemplateEngine templateEngine =
            TemplateEngineInitializerRegistry.getRegistry().getTemplateEngine( templateEngineKind, documentKind );
        if ( templateEngine == null )
        {
            templateEngine =
                TemplateEngineInitializerRegistry.getRegistry().getTemplateEngine( templateEngineKind, null );
        }
        return templateEngine;
    }

    /**
     * Returns the template engine from request and otherwise returns the default template engine.
     * 
     * @param request
     * @return
     */
    protected ITemplateEngine getTemplateEngine( HttpServletRequest request )
    {
        String templateEngineId = getTemplateEngineId( request );
        if ( StringUtils.isNotEmpty( templateEngineId ) )
        {
            return TemplateEngineInitializerRegistry.getRegistry().getTemplateEngine( templateEngineId );
        }
        return TemplateEngineRegistry.getRegistry().getDefaultTemplateEngine();
    }

    /**
     * Returns the XDocReport registry which load and cache document. By default the registry is a singleton. If you
     * wish manage registry per HTTP session, override this method, create an instance per session and returns the
     * registry instance linked to the HTTP session.
     * 
     * @param request
     * @return
     */
    protected XDocReportRegistry getRegistry( HttpServletRequest request )
    {
        return XDocReportRegistry.getRegistry();
    }

    /**
     * Returns registry from HTTP session.
     * 
     * @param request
     * @return
     */
    protected XDocReportRegistry getRegistryFromHTTPSession( HttpServletRequest request )
    {
        HttpSession session = request.getSession();
        XDocReportRegistry registry = (XDocReportRegistry) session.getAttribute( XDOCREPORTREGISTRY_SESSION_KEY );
        if ( registry == null )
        {
            registry = new XDocReportRegistry();
            session.setAttribute( XDOCREPORTREGISTRY_SESSION_KEY, registry );
        }
        return registry;
    }

    /**
     * Clear registry from HTTP session.
     * 
     * @param request
     */
    protected void clearRegistryFromHTTPSession( HttpServletRequest request )
    {
        HttpSession session = request.getSession();
        XDocReportRegistry registry =
            (XDocReportRegistry) session.getAttribute( XDocBaseServletConstants.XDOCREPORTREGISTRY_SESSION_KEY );
        if ( registry != null )
        {
            registry.dispose();
        }
        session.removeAttribute( XDOCREPORTREGISTRY_SESSION_KEY );
    }

    /**
     * Disable cache HTTP hearder.
     * 
     * @param response
     */
    protected void disableHTTPResponCache( HttpServletResponse response )
    {
        // see article http://onjava.com/pub/a/onjava/excerpt/jebp_3/index2.html
        // Set to expire far in the past.
        response.setHeader( HttpHeaderUtils.EXPIRES, HttpHeaderUtils.SAT_6_MAY_1995_12_00_00_GMT );
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader( HttpHeaderUtils.CACHE_CONTROL_HTTP_HEADER,
                            HttpHeaderUtils.NO_STORE_NO_CACHE_MUST_REVALIDATE );
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader( HttpHeaderUtils.CACHE_CONTROL_HTTP_HEADER, HttpHeaderUtils.POST_CHECK_0_PRE_CHECK_0 );
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader( HttpHeaderUtils.PRAGMA, HttpHeaderUtils.NO_CACHE );
    }

    protected void prepareHTTPResponse( String reportId, MimeMapping mimeMapping, HttpServletRequest request,
                                        HttpServletResponse response )
    {
        if ( mimeMapping != null )
        {
            response.setContentType( mimeMapping.getMimeType() );
        }

        // Check if Content-Disposition must be generated?
        if ( isGenerateContentDisposition( reportId, mimeMapping, request ) )
        {
            String contentDisposition = getContentDisposition( reportId, mimeMapping, request );
            if ( StringUtils.isNotEmpty( contentDisposition ) )
            {
                response.setHeader( HttpHeaderUtils.CONTENT_DISPOSITION_HEADER, contentDisposition.toString() );
            }
        }
        // Disable HTTP response cache
        if ( isDisableHTTPResponCache() )
        {
            disableHTTPResponCache( response );
        }
    }

    protected void prepareHTTPResponse( String reportId, String entryName, HttpServletRequest request,
                                        HttpServletResponse response )
    {

        // Check if Content-Disposition must be generated?
        if ( isGenerateContentDisposition( reportId, null, request ) )
        {
            String contentDisposition = getContentDisposition( entryName );
            if ( StringUtils.isNotEmpty( contentDisposition ) )
            {
                response.setHeader( HttpHeaderUtils.CONTENT_DISPOSITION_HEADER, contentDisposition.toString() );
            }
        }

        // Disable HTTP response cache
        if ( isDisableHTTPResponCache() )
        {
            disableHTTPResponCache( response );
        }
    }

    protected boolean isDisableHTTPResponCache()
    {
        return true;
    }

    protected boolean isGenerateContentDisposition( String reportId, MimeMapping mimeMapping, HttpServletRequest request )
    {
        return true;
    }

    protected String getContentDisposition( String reportId, MimeMapping mimeMapping, HttpServletRequest request )
    {
        if ( mimeMapping != null )
        {
            String fileName = mimeMapping.formatFileName( reportId );
            return getContentDisposition( fileName );
        }
        return null;
    }

    protected String getContentDisposition( String fileName )
    {
        return HttpHeaderUtils.getAttachmentFileName( fileName );
    }

    /**
     * Handles all requests (by default).
     * 
     * @param request HttpServletRequest object containing client request
     * @param response HttpServletResponse object for the response
     */
    protected abstract void processRequest( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException;
}
