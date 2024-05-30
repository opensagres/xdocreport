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
package fr.opensagres.struts2.views.xdocreport;

import static fr.opensagres.struts2.views.xdocreport.ActionInvocationUtils.getRequest;
import static fr.opensagres.struts2.views.xdocreport.ActionInvocationUtils.getResponse;
import static fr.opensagres.struts2.views.xdocreport.ActionInvocationUtils.getServletContext;
import static fr.opensagres.xdocreport.core.utils.StringUtils.FALSE;
import static fr.opensagres.xdocreport.core.utils.StringUtils.TRUE;
import static fr.opensagres.xdocreport.core.utils.StringUtils.asBoolean;
import static fr.opensagres.xdocreport.core.utils.StringUtils.isEmpty;
import static fr.opensagres.xdocreport.core.utils.StringUtils.isNotEmpty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.result.StrutsResultSupport;

import com.opensymphony.xwork2.ActionInvocation;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.IURIResolver;
import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.OptionsHelper;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.document.web.WEBURIResolver;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * Abstract class for manage Struts2 Result with XDocReport to generate odt, docx report by using odt, docx document and
 * convert it to another format like PDF/XHTML.
 */
public abstract class AbstractXDocReportResult
    extends StrutsResultSupport
{

    private static final long serialVersionUID = -3844927561499091875L;

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( AbstractXDocReportResult.class );

    // HTTP Header constants
    private static final String SAT_6_MAY_1995_12_00_00_GMT = "Sat, 6 May 1995 12:00:00 GMT";

    private static final String EXPIRES = "Expires";

    private static final String POST_CHECK_0_PRE_CHECK_0 = "post-check=0, pre-check=0";

    private static final String NO_CACHE = "no-cache";

    private static final String PRAGMA = "Pragma";

    private static final String NO_STORE_NO_CACHE_MUST_REVALIDATE = "no-store, no-cache, must-revalidate";

    private static final String CACHE_CONTROL_HTTP_HEADER = "Cache-Control";

    // Content-Disposition HTTP response Header
    private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";

    private static final String ATTACHMENT_FILENAME = "attachment; filename=\"";

    private static final String WEB_URI_RESOLVER_DATA_KEY = WEBURIResolver.class.getName();

    protected static final String ACTION_KEY = "#action";

    private static final Map<Class, Boolean> isXDocReportInitializerAwareClassCache = new HashMap<Class, Boolean>();

    public static final String LAST_MODIFIED = AbstractXDocReportResult.class.getSimpleName() + "_LAST_MODIFIED";

    private String templateEngine = TemplateEngineKind.Velocity.name();

    private String converter = null;

    private String[] expressions = { ACTION_KEY };

    private String trackLastModified = FALSE;

    private String download = TRUE;

    private String fieldsAsList = null;

    /**
     * Set Template engine to use (according the JAR which is added in the classpath, values are Velocity|Freemarker).
     * The template engine can use OGNL expression too (ex : #action.templateEngine will use the
     * Action#getTemplateEngine() method).
     * 
     * @param templateEngine
     */
    public void setTemplateEngine( String templateEngine )
    {
        this.templateEngine = templateEngine;
    }

    /**
     * Get Template engine to use (according the JAR which is added in the classpath, values are Velocity|Freemarker).
     * 
     * @return
     */
    public String getTemplateEngine()
    {
        return templateEngine;
    }

    /**
     * Set the converter to use (according the JAR which is added in the classpath, values are static value
     * PDF_ITEXT|XHTML_XWPF ). The converter can use OGNL expression too (ex : #action.converter will use the
     * Action#getConverter() method). If converter is null, no conversion is done.
     * 
     * @param converter
     */
    public void setConverter( String converter )
    {
        this.converter = converter;
    }

    /**
     * Returns the converter to use (according the JAR which is added in the classpath, values are
     * PDF_ITEXT|XHTML_XWPF). If converter is null, no conversion is done.
     * 
     * @return
     */
    public String getConverter()
    {
        return converter;
    }

    /**
     * @param expression
     */
    public void setExpression( String expression )
    {
        this.expressions = expression.split( "," );
    }

    /**
     * @return
     */
    public String[] getExpressions()
    {
        return expressions;
    }

    /**
     * Set to true if docx, odt file which is used to load must be tracked to observe the document change. If document
     * change, report will be reloaded with the new file.
     * 
     * @param trackLastModified
     */
    public void setTrackLastModified( String trackLastModified )
    {
        this.trackLastModified = trackLastModified;
    }

    /**
     * Returns true if docx, odt file which is used to load must be tracked to observe the document change and false
     * otherwise. If document change, report will be reloaded with the new file.
     * 
     * @return
     */
    public String getTrackLastModified()
    {
        return trackLastModified;
    }

    /**
     * Set to true if report must be downloaded (generate Content-Disposition:"attachment; filename... in the HTTP
     * Header) and false otherwise. By default download is true.
     * 
     * @param download
     */
    public void setDownload( String download )
    {
        this.download = download;
    }

    /**
     * Returns true if report must be downloaded (generate Content-Disposition:"attachment; filename... in the HTTP
     * Header) and false otherwise.
     * 
     * @return
     */
    public String getDownload()
    {
        return download;
    }

    public void setFieldAsList( String fieldAsList )
    {
        this.fieldsAsList = fieldAsList;
    }

    public String[] getFieldsAsList()
    {
        if ( isEmpty( fieldsAsList ) )
        {
            return StringUtils.EMPTY_STRING_ARRAY;
        }
        return fieldsAsList.split( "," );
    }

    @Override
    protected void doExecute( String finalLocation, ActionInvocation invocation )
        throws Exception
    {

        String location = getLocation( finalLocation, invocation );
        long startTime = -1;
        if ( LOGGER.isLoggable( Level.FINE ) )
        {
            startTime = System.currentTimeMillis();

            LOGGER.fine( String.format( "Start XDocReportResult for location=%s, templateEngine %s", location,
                                        getTemplateEngine() ) );
        }

        try
        {

            // 1) Get or load Report from the registry
            XDocReportRegistry registry = XDocReportRegistry.getRegistry();
            IXDocReport report = getReport( registry, location, invocation );
            if ( report == null )
            {
                throw new XDocReportException( "Cannot get XDoc Report for location=" + location );
            }

            // 2) Populate Java model context with Struts2 value Stack
            IContext context = report.createContext();
            populateContext( report, context, location, invocation );

            // 3) Process or convert the report
            Options options = getOptionsConverter( report, location, invocation );
            if ( options == null )
            {
                doProcessReport( report, context, location, invocation );
            }
            else
            {
                doProcessReportWithConverter( report, context, options, location, invocation );
            }

            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                startTime = System.currentTimeMillis();
                LOGGER.fine( String.format( "End XDocReportResult with SUCCESS for location=%s, templateEngine %s  done in %s ms",
                                            location, getTemplateEngine(), ( System.currentTimeMillis() - startTime ) ) );
            }

        }
        catch ( Throwable e )
        {
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                startTime = System.currentTimeMillis();
                LOGGER.fine( String.format( "End XDocReportResult with SUCCESS for location=%s, templateEngine %s  done in %s ms",
                                            location, getTemplateEngine(), ( System.currentTimeMillis() - startTime ) ) );
                LOGGER.throwing( getClass().getName(), "doExecute", e );
            }
            if ( e instanceof Exception )
            {
                throw (Exception) e;
            }
            throw new ServletException( e );
        }
    }

    // ------------------------------ Get Report
    /**
     * @param registry
     * @param finalLocation
     * @param invocation
     * @return
     * @throws ServletException
     * @throws IOException
     */
    protected IXDocReport getReport( XDocReportRegistry registry, String location, ActionInvocation invocation )
        throws ServletException, IOException
    {

        boolean trackLastModified = isTrackLastModified( invocation );
        // 1) Test if report is already registered in the registry.
        String reportId = getReportId( location );
        IXDocReport report = registry.getReport( reportId );
        if ( report != null && !trackLastModified )
        {
            return report;
        }

        // 2) Compute location type
        LocationType locationType = LocationType.getLocationType( location );
        if ( report != null )
        {
            // Here report was already loaded and track last modified must be
            // done.
            if ( locationType == LocationType.CLASSPATH )
            {
                // Source stream comes from classpath, returns the loaded
                // report.
                return report;
            }

        }

        // 3) Get the source file
        File sourceFile = getSourceFile( location, locationType, invocation );
        if ( report != null )
        {
            if ( sourceFile == null )
            {
                // Here report was already loaded but source file is not
                // available
                // (comes from CLASSPATH), returns the loaded report.
                return report;
            }
            // Here report was already loaded and source file is
            // available, compare last modified
            Long lastModified = report.getData( LAST_MODIFIED );
            if ( lastModified == null )
            {
                return report;
            }
            if ( lastModified >= sourceFile.lastModified() )
            {
                // No modification about source file.
                return report;
            }
            // Source file was modified after load report, remove the report
            // from the registry to force the load.
            registry.unregisterReport( reportId );
        }

        // 2) Get source stream
        InputStream sourceStream = getSourceStream( location, locationType, sourceFile, invocation );
        if ( sourceStream == null )
        {
            throw new IOException( "Stream null for location=" + location );
        }

        try
        {
            report = registry.loadReport( sourceStream, reportId, getTemplateEngine( invocation ) );
            report.setFieldsMetadata( getFieldsMetadata( report, location, invocation ) );
            Object action = invocation.getStack().findValue( ACTION_KEY );
            XDocReportInitializerAware initializer = getXDocReportInitializerAware( action );
            if ( initializer != null )
            {
                initializer.initialize( report );
            }
            report.setData( LAST_MODIFIED, System.currentTimeMillis() );
            return report;
        }
        catch ( IOException e )
        {
            throw new ServletException( "Inputstream", e );
        }
        catch ( XDocReportException e )
        {
            throw new ServletException( "Inputstream", e );
        }
    }

    protected boolean isTrackLastModified( ActionInvocation invocation )
    {
        String trackLastModified = getTrackLastModified();
        trackLastModified = getValue( trackLastModified, invocation );
        return asBoolean( trackLastModified, false );
    }

    protected boolean isDownload( ActionInvocation invocation )
    {
        String download = getDownload();
        download = getValue( download, invocation );
        return asBoolean( download, true );
    }

    protected String getLocation( String finalLocation, ActionInvocation invocation )
    {
        return getValue( finalLocation, invocation );
    }

    protected String getTemplateEngine( ActionInvocation invocation )
    {
        String templateEngine = getTemplateEngine();
        return getValue( templateEngine, invocation );
    }

    protected String getValue( String value, ActionInvocation invocation )
    {
        if ( isEmpty( value ) )
        {
            return value;
        }
        if ( value.startsWith( "#" ) )
        {
            return invocation.getStack().findString( value );
        }
        return value;
    }

    protected String getReportId( String finalLocation )
    {
        StringBuilder reportId = new StringBuilder( StringUtils.replaceAll( finalLocation, "/", "_" ) );
        reportId.append( "_" );
        reportId.append( getTemplateEngine() );
        return reportId.toString();
    }

    protected InputStream getSourceStream( final String finalLocation, LocationType locationType, File sourceFile,
                                           ActionInvocation invocation )
        throws IOException
    {
        switch ( locationType )
        {
            case CLASSPATH:
                String location = locationType.getLocation( finalLocation );
                return getSourceStreamFromClasspath( location );
            case FILESYSTEM:
                return new FileInputStream( sourceFile );
            default:
                return new FileInputStream( sourceFile );
        }
    }

    protected File getSourceFile( final String finalLocation, LocationType locationType, ActionInvocation invocation )
    {
        String location = locationType.getLocation( finalLocation );
        switch ( locationType )
        {
            case CLASSPATH:
                return null;
            case FILESYSTEM:
                return new File( location );
            default:
                ServletContext servletContext = getServletContext( invocation );
                return new File( servletContext.getRealPath( location ) );
        }
    }

    protected InputStream getSourceStreamFromClasspath( String path )
    {
        ClassLoader cl = this.getClass().getClassLoader();
        if ( cl == null )
        {
            // no class loader specified -> use thread context class loader
            cl = Thread.currentThread().getContextClassLoader();
        }
        return cl.getResourceAsStream( path );
    }

    protected FieldsMetadata getFieldsMetadata( IXDocReport report, String location, ActionInvocation invocation )
    {
        String[] fieldsAsList = getFieldsAsList();
        if ( fieldsAsList != null && fieldsAsList.length > 0 )
        {
            FieldsMetadata fieldsMetadata = new FieldsMetadata();
            for ( int i = 0; i < fieldsAsList.length; i++ )
            {
                fieldsMetadata.addFieldAsList( getValue( fieldsAsList[i], invocation ) );
            }
            return fieldsMetadata;
        }
        return null;
    }

    // --------- Process reportoid

    /**
     * @param report
     * @param context
     * @param options
     * @param finalLocation
     * @param invocation
     * @throws IOException
     * @throws XDocReportException
     */
    protected void doProcessReport( IXDocReport report, IContext context, String finalLocation,
                                    ActionInvocation invocation )
        throws XDocReportException, IOException
    {

        HttpServletResponse response = getResponse( invocation );
        // 2) Prepare HTTP response content type
        prepareHTTPResponse( report.getId(), finalLocation, report.getMimeMapping(), invocation,
                             getRequest( invocation ), response );

        // 2) Generate report
        report.process( context, response.getOutputStream() );

    }

    // --------- Converter

    /**
     * @param report
     * @param context
     * @param finalLocation
     * @param invocation
     * @throws IOException
     * @throws XDocReportException
     * @throws XDocConverterException
     */
    protected void doProcessReportWithConverter( IXDocReport report, IContext context, Options options,
                                                 String finalLocation, ActionInvocation invocation )
        throws XDocConverterException, XDocReportException, IOException
    {

        HttpServletResponse response = getResponse( invocation );

        // 2) Get converter
        IConverter converter = report.getConverter( options );
        // 3) Prepare HTTP response content type
        prepareHTTPResponse( report.getId(), finalLocation, converter.getMimeMapping(), invocation,
                             getRequest( invocation ), response );

        // 4) Generate report with conversion
        report.convert( context, options, response.getOutputStream() );

    }

    /**
     * @param report
     * @param request
     * @return
     */
    protected Options getOptionsConverter( IXDocReport report, String finalLocation, ActionInvocation invocation )
    {
        final String converterId = getConverter( invocation );
        if ( isEmpty( converterId ) )
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
        prepareOptions( options, report, converterId, finalLocation, invocation );
        return options;
    }

    protected String getConverter( ActionInvocation invocation )
    {
        String converter = getConverter();
        return getValue( converter, invocation );
    }

    protected void prepareOptions( Options options, IXDocReport report, String converterId, String finalLocation,
                                   ActionInvocation invocation )
    {
        if ( ConverterTypeTo.FO.name().equals( options.getTo() )
            || ConverterTypeTo.XHTML.name().equals( options.getTo() ) )
        {
            OptionsHelper.setURIResolver( options,
                                          createWEBURIResolver( report, converterId, finalLocation, invocation ) );
        }
    }

    protected IURIResolver createWEBURIResolver( IXDocReport report, String converterId, String finalLocation,
                                                 ActionInvocation invocation )
    {
        WEBURIResolver resolver = report.getData( WEB_URI_RESOLVER_DATA_KEY );
        if ( resolver == null )
        {
            resolver = new WEBURIResolver( report.getId(), getRequest( invocation ) );
            report.setData( WEB_URI_RESOLVER_DATA_KEY, resolver );
        }
        return resolver;
    }

    protected void prepareHTTPResponse( String reportId, String finalLocation, MimeMapping mimeMapping,
                                        ActionInvocation invocation, HttpServletRequest request,
                                        HttpServletResponse response )
    {
        if ( mimeMapping != null )
        {
            response.setContentType( mimeMapping.getMimeType() );
        }

        // Check if Content-Disposition must be generated?
        if ( isGenerateContentDisposition( reportId, mimeMapping, invocation ) )
        {
            String sourceFileName = finalLocation;
            int index = finalLocation.lastIndexOf( '/' );
            if ( index != -1 )
            {
                sourceFileName = finalLocation.substring( index + 1, finalLocation.length() );
            }
            String contentDisposition = getContentDisposition( sourceFileName, mimeMapping, request );
            if ( isNotEmpty( contentDisposition ) )
            {
                response.setHeader( CONTENT_DISPOSITION_HEADER, contentDisposition.toString() );
            }
        }
        // Disable HTTP response cache
        if ( isDisableHTTPResponseCache() )
        {
            disableHTTPResponseCache( response );
        }
    }

    protected boolean isGenerateContentDisposition( String reportId, MimeMapping mimeMapping,
                                                    ActionInvocation invocation )
    {
        return isDownload( invocation );
    }

    protected void prepareHTTPResponse( String reportId, String entryName, ActionInvocation invocation,
                                        HttpServletRequest request, HttpServletResponse response )
    {

        // Check if Content-Disposition must be generated?
        if ( isGenerateContentDisposition( reportId, null, invocation ) )
        {
            String contentDisposition = getContentDisposition( entryName );
            if ( isNotEmpty( contentDisposition ) )
            {
                response.setHeader( CONTENT_DISPOSITION_HEADER, contentDisposition.toString() );
            }
        }

        // Disable HTTP response cache
        if ( isDisableHTTPResponseCache() )
        {
            disableHTTPResponseCache( response );
        }
    }

    protected boolean isDisableHTTPResponseCache()
    {
        return true;
    }

    protected String getContentDisposition( String sourceFileName, MimeMapping mimeMapping, HttpServletRequest request )
    {
        if ( mimeMapping != null )
        {
            String fileName = mimeMapping.formatFileName( sourceFileName );
            return getContentDisposition( fileName );
        }
        return null;
    }

    protected String getContentDisposition( String fileName )
    {
        StringBuilder contentDisposition = new StringBuilder( ATTACHMENT_FILENAME );
        contentDisposition.append( fileName );
        contentDisposition.append( "\"" );
        return contentDisposition.toString();
    }

    /**
     * Disable cache HTTP hearder.
     * 
     * @param response
     */
    protected void disableHTTPResponseCache( HttpServletResponse response )
    {
        // see article http://onjava.com/pub/a/onjava/excerpt/jebp_3/index2.html
        // Set to expire far in the past.
        response.setHeader( EXPIRES, SAT_6_MAY_1995_12_00_00_GMT );
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader( CACHE_CONTROL_HTTP_HEADER, NO_STORE_NO_CACHE_MUST_REVALIDATE );
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader( CACHE_CONTROL_HTTP_HEADER, POST_CHECK_0_PRE_CHECK_0 );
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader( PRAGMA, NO_CACHE );
    }

    protected XDocReportInitializerAware getXDocReportInitializerAware( Object action )
    {
        if ( action == null )
        {
            return null;
        }
        Boolean result = isXDocReportInitializerAwareClassCache.get( action.getClass() );
        if ( result == null )
        {
            result = ( action instanceof XDocReportInitializerAware );
            isXDocReportInitializerAwareClassCache.put( action.getClass(), result );
        }
        if ( result )
        {
            return ( (XDocReportInitializerAware) action );
        }
        return null;
    }

    /**
     * @param report
     * @param finalLocation
     * @param invocation
     * @return
     * @throws XDocReportException
     */
    protected abstract void populateContext( IXDocReport report, IContext context, String finalLocation,
                                             ActionInvocation invocation )
        throws Exception;

}
