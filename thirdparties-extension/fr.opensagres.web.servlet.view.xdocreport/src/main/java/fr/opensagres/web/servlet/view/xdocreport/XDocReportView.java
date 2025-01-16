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
package fr.opensagres.web.servlet.view.xdocreport;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.utils.HttpHeaderUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.dump.DumperOptions;
import fr.opensagres.xdocreport.document.dump.IDumper;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;

/**
 * Spring MVC {@link View} implementation with XDocReport. You can use it like this with Spring bean :
 * 
 * <pre>
 *     <bean id="reportConfiguration"
 *           class="fr.opensagres.xdocreport.samples.reporting.springmvc.report.DocxProjectWithVelocityListConfiguration" />
 * 
 *     <bean id="docxReport"
 *         class="fr.opensagres.web.servlet.view.xdocreport.XDocReportView"
 *         p:url="classpath:fr/opensagres/xdocreport/samples/reporting/springmvc/report/DocxProjectWithVelocityList.docx"
 *         p:templateEngineId="Velocity" >
 *         <property name="configuration" ref="reportConfiguration" /> 
 *     </bean>
 * 
 * </pre>
 */
public class XDocReportView
    extends AbstractUrlBasedView
{

    /**
     * If set to true, this view load the report when Spring {@link ApplicationContext} is initialized.
     */
    private boolean loadOnInit = true;

    /**
     * The reportId of the report. Fill this report id if you wish to use {@link XDocReportRegistry#getReport(String)}
     * in Java code on other part of your application.
     */
    private String reportId;

    /**
     * The template engine id (Velocity|Freemarker) to use. This property is required.
     */
    private String templateEngineId;

    /**
     * If set to true, the report is cached after loading of the report.
     */
    private boolean cacheReport = true;

    /**
     * The convert to if the report should be converted to other format. See constants at {@link ConverterTypeTo}.
     */
    private String convertTo;

    /**
     * The convert via if the report should be converted to other format. See constants at {@link ConverterTypeVia}.
     */
    private String convertVia;

    /**
     * True if the content disposition should be generated in the HTTP header (download) or not (view teh report in teh
     * browser).
     */
    private boolean generateContentDisposition = true;

    /**
     * The configuration to use after the report is loaded.
     */
    private IXDocReportConfiguration configuration;

    /**
     * Set to true, if the report must be loaded when Spring {@link ApplicationContext} is initialized and false
     * otherwise. By default, the report is loaded when {@link ApplicationContext} is initialized.
     * 
     * @param loadOnInit
     */
    public void setLoadOnInit( boolean loadOnInit )
    {
        this.loadOnInit = loadOnInit;
    }

    /**
     * Returns true, if the report must be loaded when Spring {@link ApplicationContext} is initialized and false
     * otherwise. By default, the report is loaded when {@link ApplicationContext} is initialized.
     * 
     * @return
     */
    public boolean isLoadOnInit()
    {
        return loadOnInit;
    }

    /**
     * Set the reportId of the report. Fill this report id if you wish to use
     * {@link XDocReportRegistry#getReport(String)} in Java code on other part of your application.
     * 
     * @param reportId the report id.
     */
    public void setReportId( String reportId )
    {
        this.reportId = reportId;
    }

    /**
     * Returns the id of the report if not null otherwise returns the bean/@id.
     * 
     * @param request
     * @return
     */
    public String getReportId()
    {
        if ( reportId != null )
        {
            return reportId;
        }
        return getBeanName();
    }

    /**
     * Set the template engine id (Velocity|Freemarker) to use. This property is required.
     * 
     * @param templateEngineId the template engine id (Velocity|Freemarker) to use. This property is required.
     */
    public void setTemplateEngineId( String templateEngineId )
    {
        this.templateEngineId = templateEngineId;
    }

    /**
     * Returns the template engine id to use.
     * 
     * @return
     */
    protected String getTemplateEngineId()
    {
        return templateEngineId;
    }

    /**
     * Set to true, if the report must be cached after loading of the report and false otherwise. By default the report
     * is cached.
     * 
     * @param cacheReport
     */
    public void setCacheReport( boolean cacheReport )
    {
        this.cacheReport = cacheReport;
    }

    /**
     * Returns true, if the report must be cached after loading of the report and false otherwise.
     */
    public boolean isCacheReport()
    {
        return cacheReport;
    }

    /**
     * Set the convert to if the report should be converted to other format. See constants at {@link ConverterTypeTo}.
     * 
     * @param convertTo
     */
    public void setConvertTo( String convertTo )
    {
        this.convertTo = convertTo;
    }

    /**
     * Return convert to if the report should be converted to other format and null otherwise.
     * 
     * @return
     */
    public String getConvertTo()
    {
        return convertTo;
    }

    /**
     * Set the convert via if the report should be converted to other format. See constants at {@link ConverterTypeVia}.
     * 
     * @param convertVia
     */
    public void setConvertVia( String convertVia )
    {
        this.convertVia = convertVia;
    }

    /**
     * Returns convert via if the report should be converted to other format. See constants at {@link ConverterTypeVia}.
     * 
     * @return
     */
    public String getConvertVia()
    {
        return convertVia;
    }

    /**
     * Set true if the content disposition should be generated in the HTTP header (download) or not (view teh report in
     * teh browser).
     * 
     * @param generateContentDisposition
     */
    public void setGenerateContentDisposition( boolean generateContentDisposition )
    {
        this.generateContentDisposition = generateContentDisposition;
    }

    /**
     * Returns true if the content disposition should be generated in the HTTP header (download) or not (view teh report
     * in teh browser).
     * 
     * @return
     */
    public boolean isGenerateContentDisposition()
    {
        return generateContentDisposition;
    }

    /**
     * Set the configuration to use after the report is loaded.
     * 
     * @param configuration
     */
    public void setConfiguration( IXDocReportConfiguration configuration )
    {
        this.configuration = configuration;
    }

    /**
     * Returns the configuration to use after the report is loaded and null otherwise.
     * 
     * @return
     */
    public IXDocReportConfiguration getConfiguration()
    {
        return configuration;
    }

    @Override
    protected final void initApplicationContext()
        throws ApplicationContextException
    {
        if ( isLoadOnInit() )
        {
            // load the report when Spring bean is initialized.
            getReport();
        }
        onInit();
    }

    /**
     * Subclasses can override this to add some custom initialization logic. Called by {@link #initApplicationContext()}
     * as soon as all standard initialization logic has finished executing.
     * 
     * @see #initApplicationContext()
     */
    protected void onInit()
    {
    }

    @Override
    public void afterPropertiesSet()
        throws Exception
    {
        super.afterPropertiesSet();
        if ( getTemplateEngineId() == null )
        {
            throw new IllegalArgumentException( "Property 'templateEngineId' is required" );
        }
    }

    @Override
    protected void renderMergedOutputModel( Map<String, Object> model, HttpServletRequest request,
                                            HttpServletResponse response )
        throws Exception
    {
        // 1) Get the report
        IXDocReport report = getReport();
        if ( report == null )
        {
            throw new IllegalStateException( "No main report defined for 'renderMergedOutputModel' - "
                + "specify a 'url' on this view or override 'getReport()'" );
        }

        // 2) Prepare Java model context
        IContext context = createContext( report, model );
        Options options = getOptionsConverter( model );
        DumperOptions dumperOptions = getOptionsDumper( model );
        if ( options == null )
        {
            // populateContext( context, report.getId(), request );
            // 3) Generate report
            doProcessReport( report, context, dumperOptions, request, response );
        }
        else
        {
            // Generate and convert report.
            doProcessReportWithConverter( report, context, options, dumperOptions, request, response );
        }
    }

    protected IContext createContext( IXDocReport report, Map<String, Object> model )
        throws XDocReportException
    {
        return report.createContext( model );
    }

    protected Options getOptionsConverter( Map<String, Object> model )
    {
        String to = getConvertTo();
        if ( to != null )
        {
            String via = getConvertVia();
            Options options = Options.getTo( to );
            if ( via != null )
            {
                options.via( via );
            }
            return options;
        }
        return CollectionUtils.findValueOfType( model.values(), Options.class );
    }

    protected DumperOptions getOptionsDumper( Map<String, Object> model )
    {
        return CollectionUtils.findValueOfType( model.values(), DumperOptions.class );
    }

    // ----------------- Get Report

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport getReport()
        throws ApplicationContextException
    {
        XDocReportRegistry registry = getRegistry();
        // 1) Get report id
        String reportId = getReportId();
        if ( StringUtils.isNotEmpty( reportId ) )
        {
            // Search if report is cached in the registry
            IXDocReport report = registry.getReport( reportId );
            if ( report != null )
            {
                return report;
            }
        }
        return loadReport( reportId, registry );
    }

    /**
     * Load the main {@code XDocReport} from the specified {@code Resource}. If the {@code Resource} points to an
     * uncompiled report design file then the report file is compiled dynamically and loaded into memory.
     * 
     * @return a {@code XDocReport} instance, or {@code null} if no main report has been statically defined
     */
    protected IXDocReport loadReport( String reportId, XDocReportRegistry registry )
    {
        String url = getUrl();
        if ( url == null )
        {
            return null;
        }
        Resource resource = getApplicationContext().getResource( url );
        try
        {
            InputStream is = resource.getInputStream();
            String templateEngineId = getTemplateEngineId();
            IXDocReport report = registry.loadReport( is, reportId, templateEngineId, isCacheReport() );
            this.reportId = report.getId();
            IXDocReportConfiguration configuration = getConfiguration();
            if ( configuration != null )
            {
                configuration.configure( report );
            }
            return report;
        }
        catch ( Exception ex )
        {
            throw new ApplicationContextException( "Could not load XDocReport report from " + resource, ex );
        }
    }

    // ------------------ XDocReport registry

    /**
     * Returns the XDocReport registry which load and cache document.
     * 
     * @return
     */
    protected XDocReportRegistry getRegistry()
    {
        return XDocReportRegistry.getRegistry();
    }

    /**
     * Generate report with process.
     * 
     * @param report
     * @param entryName
     * @param dumperOptions
     * @param request
     * @param response
     * @throws XDocReportException
     * @throws IOException
     */
    private void doProcessReport( IXDocReport report, IContext context, DumperOptions dumperOptions,
                                  HttpServletRequest request, HttpServletResponse response )
        throws XDocReportException, IOException
    {

        if ( dumperOptions != null )
        {
            // dump must be done
            // 2) Get dumper
            IDumper dumper = report.getDumper( dumperOptions );
            // 3) Prepare HTTP response content type
            prepareHTTPResponse( report.getId(), dumper.getMimeMapping(), request, response );
            // 4) Generate dump
            report.dump( context, dumperOptions, response.getOutputStream() );
        }
        else
        {
            // 2) Prepare HTTP response content type
            prepareHTTPResponse( report.getId(), report.getMimeMapping(), request, response );
            // 3) Generate report
            report.process( context, response.getOutputStream() );
        }
    }

    /**
     * Generate report with conversion.
     * 
     * @param report
     * @param options
     * @param dumperOptions
     * @param request
     * @param response
     * @throws XDocReportException
     * @throws IOException
     * @throws XDocConverterException
     */
    protected void doProcessReportWithConverter( IXDocReport report, IContext context, Options options,
                                                 DumperOptions dumperOptions, HttpServletRequest request,
                                                 HttpServletResponse response )
        throws XDocReportException, IOException, XDocConverterException
    {

        if ( dumperOptions != null )
        {
            // 2) Get dumper
            IDumper dumper = report.getDumper( dumperOptions );
            // 3) Prepare HTTP response content type
            prepareHTTPResponse( report.getId(), dumper.getMimeMapping(), request, response );
            // 4) Generate dump
            report.dump( context, dumperOptions, response.getOutputStream() );
        }
        else
        {
            // 2) Get converter
            IConverter converter = report.getConverter( options );
            // 3) Prepare HTTP response content type
            prepareHTTPResponse( report.getId(), converter.getMimeMapping(), request, response );
            // 4) Generate report with conversion
            report.convert( context, options, response.getOutputStream() );
        }
    }

    protected void prepareHTTPResponse( String reportId, MimeMapping mimeMapping, HttpServletRequest request,
                                        HttpServletResponse response )
    {
        if ( mimeMapping != null )
        {
            response.setContentType( mimeMapping.getMimeType() );
        }

        // Check if Content-Disposition must be generated?
        if ( isGenerateContentDisposition() )
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

    protected boolean isDisableHTTPResponCache()
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
}
