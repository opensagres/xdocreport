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
package fr.opensagres.xdocreport.document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.registry.TemplateEngineInitializerRegistry;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

public abstract class Generator<In, Out>
{

    // Dispatch values
    public static final String REMOVE_DISPATCH = "remove";

    public static final String DOWNLOAD_DISPATCH = "download";

    public static final String VIEW_DISPATCH = "view";

    // HTTP parameters name
    public static final String REPORT_ID_HTTP_PARAM = "reportId";

    public static final String TEMPLATE_ENGINE_KIND_HTTP_PARAM = "templateEngineKind";

    public static final String TEMPLATE_ENGINE_ID_HTTP_PARAM = "templateEngineId";

    public static final String ENTRY_NAME_HTTP_PARAM = "entryName";

    public static final String PROCESS_STATE_HTTP_PARAM = "processState";

    public static final String DISPATCH_HTTP_PARAM = "dispatch";

    public static final String CONVERTER_ID_HTTP_PARAM = "converter";

    private boolean cacheOriginalDocument;

    /**
     * Handles all requests (by default).
     * 
     * @param request In object containing client request
     * @param response Out object for the response
     */
    public void processRequest( In request, Out response )
        throws Exception
    {
        String dispatch = getDispatchParameter( request );
        if ( REMOVE_DISPATCH.equals( dispatch ) )
        {
            doRemoveReport( request, response );
        }
        else
        {
            String entryName = getEntryName( request );
            ProcessState processState = getProcessState( request );
            if ( processState == null )
            {
                processState =
                    ( StringUtils.isNotEmpty( entryName ) ) ? ProcessState.PREPROCESSED : ProcessState.GENERATED;
            }
            switch ( processState )
            {
                case ORIGINAL:
                case PREPROCESSED:
                    doDocumentArchive( processState, entryName, request, response );
                    break;
                case GENERATED:
                    doGenerateReport( entryName, request, response );
                    break;
            }
        }
    }

    /**
     * Remove report from the registry.
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    protected void doRemoveReport( In request, Out response )
        throws IOException
    {
        String reportId = getReportId( request );
        if ( StringUtils.isNotEmpty( reportId ) )
        {
            getRegistry( request ).unregisterReport( reportId );
        }
        doRedirectAfterRemoveReport( request, response );
    }

    protected void doRedirectAfterRemoveReport( In request, Out response )
        throws IOException
    {
        // response.sendRedirect(ADMIN_JSP);
    }

    protected void doDocumentArchive( ProcessState state, String entryName, In request, Out response )
        throws Exception
    {
        if ( StringUtils.isEmpty( entryName ) )
        {
            doSaveReport( state, request, response );
        }
        else
        {
            doSaveEntry( state, entryName, request, response );
        }
    }

    /**
     * Save document archive of the report.
     * 
     * @param processState
     * @param request
     * @param response
     * @throws IOException
     * @throws Exception
     */
    protected void doSaveReport( ProcessState processState, In request, Out response )
        throws Exception
    {

        IXDocReport report = getReport( request );
        if ( report != null )
        {
            // 2) Prepare HTTP response content type
            prepareHTTPResponse( report.getId(), report.getMimeMapping(), request, response );
            report.save( processState, getOutputStream( response ) );
        }

    }

    /**
     * Extract entry from a report.
     * 
     * @param processState
     * @param request
     * @param response
     * @throws IOException
     * @throws Exception
     */
    protected void doSaveEntry( ProcessState processState, String entryName, In request, Out response )
        throws IOException, Exception
    {

        IXDocReport report = getReport( request );
        if ( report != null )
        {
            // 2) Prepare HTTP response content type
            prepareHTTPResponse( report.getId(), entryName, request, response );
            report.saveEntry( entryName, processState, getOutputStream( response ) );
        }
    }

    protected boolean doGenerateReport( String entryName, In request, Out response )
        throws Exception, IOException
    {

        try
        {
            // 1) Get XDoc report
            IXDocReport report = getReport( request );
            if ( report == null )
            {
                throw new XDocReportException( "Cannot get XDoc Report for the HTTP request" );
            }
            Options options = getOptionsConverter( report, request );
            if ( options == null )
            {
                doProcessReport( report, entryName, request, response );
            }
            else
            {
                doProcessReportWithConverter( report, options, request, response );
            }
            return true;
        }
        catch ( Exception e )
        {
            /*
             * call the error handler to let the derived class do something useful with this failure.
             */
            error( request, response, e );
            return false;
        }
    }

    /**
     * Generate report with process.
     * 
     * @param report
     * @param entryName
     * @param request
     * @param response
     * @throws XDocReportException
     * @throws IOException
     */
    private void doProcessReport( IXDocReport report, String entryName, In request, Out response )
        throws XDocReportException, IOException
    {
        // 1) Prepare Java model context
        IContext context = report.createContext();
        populateContext( context, report.getId(), request );

        if ( StringUtils.isEmpty( entryName ) )
        {
            // 2) Prepare HTTP response content type
            prepareHTTPResponse( report.getId(), report.getMimeMapping(), request, response );
            // 3) Generate report
            report.process( context, getOutputStream( response ) );
        }
        else
        {
            // 2) Prepare HTTP response content type
            prepareHTTPResponse( report.getId(), entryName, request, response );
            // 3) Generate report
            report.process( context, entryName, getOutputStream( response ) );
        }
    }

    /**
     * Generate report with conversion.
     * 
     * @param report
     * @param options
     * @param request
     * @param response
     * @throws XDocReportException
     * @throws IOException
     * @throws XDocConverterException
     */
    private void doProcessReportWithConverter( IXDocReport report, Options options, In request, Out response )
        throws XDocReportException, IOException, XDocConverterException
    {
        IContext context = null;
        ITemplateEngine templateEngine = report.getTemplateEngine();
        if ( templateEngine != null )
        {
            // 1) Prepare Java model context
            context = report.createContext();
            populateContext( context, report.getId(), request );
        }

        // 2) Get converter
        IConverter converter = report.getConverter( options );
        // 3) Prepare HTTP response content type
        prepareHTTPResponse( report.getId(), converter.getMimeMapping(), request, response );
        // 4) Generate report with conversion
        report.convert( context, options, getOutputStream( response ) );
    }

    // ----------------- Get Report

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    protected IXDocReport getReport( In request )
        throws IOException, XDocReportException
    {
        XDocReportRegistry registry = getRegistry( request );
        // 1) Get report id
        String reportId = getReportId( request );
        if ( StringUtils.isNotEmpty( reportId ) )
        {
            // Search if report is cached in the registry
            IXDocReport report = registry.getReport( reportId );
            if ( report != null )
            {
                return report;
            }
        }
        return loadReport( reportId, registry, request );
    }

    /**
     * Load report.
     * 
     * @param reportId
     * @param registry
     * @param request
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    protected IXDocReport loadReport( String reportId, XDocReportRegistry registry, In request )
        throws IOException, XDocReportException
    {
        // 2) Get sourceStream
        InputStream sourceStream = getSourceStream( reportId, request );
        if ( sourceStream == null )
        {
            throw new XDocReportException( "Input stream is null with reportId=" + reportId );
        }
        IXDocReport report = null;
        // 3) Get template engine to use for the report
        ITemplateEngine templateEngine = null;

        String templateEngineKind = getTemplateEngineKind( reportId, request );
        if ( StringUtils.isNotEmpty( templateEngineKind ) )
        {
            // 3.1) Load report with template engine kind
            report = registry.loadReport( sourceStream, reportId, templateEngineKind );
        }
        else
        {
            // 3.1) Load report with template engine
            templateEngine = getTemplateEngine( reportId, request );
            report = registry.loadReport( sourceStream, reportId, templateEngine );
        }

        // 6) Set FieldsMetaData
        FieldsMetadata fieldsMetadata = getFieldsMetadata( reportId, request );
        report.setFieldsMetadata( fieldsMetadata );

        // 7) Set cache
        report.setCacheOriginalDocument( isCacheOriginalDocument( reportId, request ) );

        return report;
    }

    protected boolean isCacheOriginalDocument( String reportId, In request )
    {
        return cacheOriginalDocument;
    }

    protected FieldsMetadata getFieldsMetadata( String reportId, In request )
    {
        return null;
    }

    /**
     * Invoked when there is an error thrown in any part of doRequest() processing. <br>
     * <br>
     * Default will send a simple HTML response indicating there was a problem.
     * 
     * @param request original In from servlet container.
     * @param response Out object from servlet container.
     * @param cause Exception that was thrown by some other part of process.
     */
    protected abstract void error( In request, Out response, Exception cause );

    /**
     * Returns the converter id.
     * 
     * @param request
     * @return
     */
    protected String getConverterId( IXDocReport report, In request )
    {
        return getParameter( request, CONVERTER_ID_HTTP_PARAM );
    }

    protected Options getOptionsConverter( IXDocReport report, In request )
    {
        final String converterId = getConverterId( report, request );
        if ( StringUtils.isEmpty( converterId ) )
        {
            return null;
        }
        Options options = null;
        int index = converterId.lastIndexOf( '_' );
        if ( index != -1 )
        {
            String to = converterId.substring( 0, index );
            String via = converterId.substring( index + 1, converterId.length() );
            options = Options.getTo( to ).via( via );
        }
        else
        {
            options = Options.getTo( converterId );
        }
        prepareOptions( options, report, converterId, request );
        return options;
    }

    protected void prepareOptions( Options options, IXDocReport report, String converterId, In request )
    {
    }

    protected boolean isGenerateContentDisposition( String reportId, MimeMapping mimeMapping, In request )
    {
        return !VIEW_DISPATCH.equals( getDispatchParameter( request ) );
    }

    /**
     * Returns dispatch parameter value.
     * 
     * @param request
     * @return
     */
    protected String getDispatchParameter( In request )
    {
        return getParameter( request, DISPATCH_HTTP_PARAM );
    }

    /**
     * Returns the id of the report.
     * 
     * @param request
     * @return
     */
    protected String getReportId( In request )
    {
        return getParameter( request, REPORT_ID_HTTP_PARAM );
    }

    /**
     * Returns process state (original|preprocessed|generated).
     * 
     * @param request
     * @return
     */
    protected ProcessState getProcessState( In request )
    {
        String state = getParameter( request, PROCESS_STATE_HTTP_PARAM );
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
    protected String getEntryName( In request )
    {
        return getParameter( request, ENTRY_NAME_HTTP_PARAM );
    }

    protected String getTemplateEngineKind( String reportId, In request )
    {
        return getTemplateEngineKind( request );
    }

    protected String getTemplateEngineKind( In request )
    {
        return getParameter( request, TEMPLATE_ENGINE_KIND_HTTP_PARAM );
    }

    /**
     * Returns the template engine id from request.
     * 
     * @param request
     * @return
     */
    protected String getTemplateEngineId( In request )
    {
        return getParameter( request, TEMPLATE_ENGINE_ID_HTTP_PARAM );
    }

    /**
     * Returns the template engine to use for the report. By default, it search if there is template id from request and
     * otherwise returns the default template engine.
     * 
     * @param reportId
     * @param request
     * @return
     */
    protected ITemplateEngine getTemplateEngine( String reportId, In request )
    {
        return getTemplateEngine( request );
    }

    protected ITemplateEngine getTemplateEngine( IXDocReport report, In request )
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
    protected ITemplateEngine getTemplateEngine( In request )
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
    protected XDocReportRegistry getRegistry( In request )
    {
        return XDocReportRegistry.getRegistry();
    }

    /**
     * Returns input stream of the report to load identified with <code>reportId</code>.
     * 
     * @param reportId report id.
     * @param request Http servlet request context.
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    protected abstract InputStream getSourceStream( String reportId, In request )
        throws IOException, XDocReportException;

    /**
     * Put the Java model in the context for the report <code>reportId</code>.
     * 
     * @param context XDocReport context to register Java data model.
     * @param reportId report id.
     * @param request Http servlet request context.
     * @throws IOException
     * @throws XDocReportException
     */
    protected abstract void populateContext( IContext context, String reportId, In request )
        throws IOException, XDocReportException;

    protected abstract OutputStream getOutputStream( Out response )
        throws IOException;

    protected abstract String getParameter( In request, String name );

    protected abstract void prepareHTTPResponse( String id, MimeMapping mimeMapping, In request, Out response );

    protected abstract void prepareHTTPResponse( String reportId, String entryName, In request, Out response );

}
