package fr.opensagres.xdocreport.document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.dispatcher.IXDocReportController;
import fr.opensagres.xdocreport.document.dispatcher.IXDocReportLoader;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * Helper class to generate report and convert it if needed.
 */
public class XDocReport
{

    // ------------------------------------ With controller

    /**
     * Generate report by using controller to load report.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param controller the controller used to load the template report if it is not already loaded.
     * @param contextMap the context map values used to replace values of the template report.
     * @param out the output stream where the report must be generated.
     * @return an instance of the loaded template report.
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport generateReport( String reportId, IXDocReportController controller,
                                              Map<String, Object> contextMap, OutputStream out )
        throws IOException, XDocReportException
    {
        return generateReport( reportId, controller, contextMap, out, getDefaultRegistry() );
    }

    /**
     * Generate report by using controller to load report by setting the registry.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param controller the controller used to load the template report if it is not already loaded.
     * @param contextMap the context map values used to replace values of the template report.
     * @param out the output stream where the report must be generated.
     * @param registry
     * @return an instance of the loaded template report.
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport generateReport( String reportId, IXDocReportController controller,
                                              Map<String, Object> contextMap, OutputStream out,
                                              XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        IXDocReport report = getReport( reportId, controller, registry );
        report.process( contextMap, out );
        return report;
    }

    /**
     * Generate report and convert it by using controller to load report.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param controller the controller used to load the template report if it is not already loaded.
     * @param contextMap the context map values used to replace values of the template report.
     * @param options the converter options.
     * @param out the output stream where the report must be generated.
     * @return an instance of the loaded template report.
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport generateReportAndConvert( String reportId, IXDocReportController controller,
                                                        Map<String, Object> contextMap, Options options,
                                                        OutputStream out )
        throws IOException, XDocReportException
    {
        return generateReportAndConvert( reportId, controller, contextMap, options, out, getDefaultRegistry() );
    }

    /**
     * Generate report and convert it by using controller to load report by setting the registry.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param controller the controller used to load the template report if it is not already loaded.
     * @param contextMap the context map values used to replace values of the template report.
     * @param options the converter options.
     * @param out the output stream where the report must be generated.
     * @param registry
     * @return an instance of the loaded template report.
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport generateReportAndConvert( String reportId, IXDocReportController controller,
                                                        Map<String, Object> contextMap, Options options,
                                                        OutputStream out, XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        IXDocReport report = getReport( reportId, controller, registry );
        report.convert( contextMap, options, out );
        return report;
    }

    /**
     * Get the template report from the cache registered with report id and load it if it is not found by using the
     * given controller.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param controller the controller used to load the template report if it is not already loaded.
     * @return an instance of the loaded template report.
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport getReport( String reportId, IXDocReportController controller )
        throws IOException, XDocReportException
    {
        XDocReportRegistry registry = getDefaultRegistry();
        return getReport( reportId, controller, registry );
    }

    /**
     * Get the template report from the cache registered with report id and load it if it is not found by using the
     * given controller by setting the registry.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param controller the controller used to load the template report if it is not already loaded.
     * @param registry
     * @return an instance of the loaded template report.
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport getReport( String reportId, IXDocReportController controller, XDocReportRegistry registry )
        throws IOException, XDocReportException
    {

        IXDocReport report = registry.getReport( reportId );
        if ( report != null )
        {
            return report;
        }
        return loadReport( reportId, controller, registry );
    }

    /**
     * Load the template report by using the given controller and cache it to the given registry
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param controller the controller used to load the template report.
     * @param registry
     * @return an instance of the loaded template report.
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport loadReport( String reportId, IXDocReportController controller, XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        // 1) Get sourceStream
        InputStream sourceStream = controller.getSourceStream();
        if ( sourceStream == null )
        {
            throw new XDocReportException( "Input stream is null with reportId=" + reportId );
        }
        // 2) Load report with template engine kind
        String templateEngineKind = controller.getTemplateEngineKind();
        IXDocReport report = registry.loadReport( sourceStream, reportId, templateEngineKind );
        // 3) Set FieldsMetaData
        report.setFieldsMetadata( controller.getFieldsMetadata() );
        return report;
    }

    // ------------------------------------ With reportLoader

    /**
     * Generate report by using report loader to load report.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param reportLoader the reportLoader used to load the template report if it is not already loaded.
     * @param contextMap the context map values used to replace values of the template report.
     * @param out the output stream where the report must be generated.
     * @return an instance of the loaded template report.
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport generateReport( String reportId, IXDocReportLoader reportLoader,
                                              Map<String, Object> contextMap, OutputStream out )
        throws IOException, XDocReportException
    {
        return generateReport( reportId, reportLoader, contextMap, out, getDefaultRegistry() );
    }

    /**
     * Generate report by using report loader to load report by setting the registry.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param reportLoader the report loader used to load the template report if it is not already loaded.
     * @param contextMap the context map values used to replace values of the template report.
     * @param out the output stream where the report must be generated.
     * @param registry
     * @return an instance of the loaded template report.
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport generateReport( String reportId, IXDocReportLoader reportLoader,
                                              Map<String, Object> contextMap, OutputStream out,
                                              XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        IXDocReport report = getReport( reportId, reportLoader, registry );
        report.process( contextMap, out );
        return report;
    }

    /**
     * Generate report and convert it by using report loader to load report.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param reportLoader the report loader used to load the template report if it is not already loaded.
     * @param contextMap the context map values used to replace values of the template report.
     * @param options the converter options.
     * @param out the output stream where the report must be generated.
     * @return an instance of the loaded template report.
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport generateReportAndConvert( String reportId, IXDocReportLoader reportLoader,
                                                        Map<String, Object> contextMap, Options options,
                                                        OutputStream out )
        throws IOException, XDocReportException
    {
        return generateReportAndConvert( reportId, reportLoader, contextMap, options, out, getDefaultRegistry() );
    }

    /**
     * Generate report and convert it by using report loader to load report by setting the registry.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param reportLoader the report loader used to load the template report if it is not already loaded.
     * @param contextMap the context map values used to replace values of the template report.
     * @param options the converter options.
     * @param out the output stream where the report must be generated.
     * @param registry
     * @return an instance of the loaded template report.
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport generateReportAndConvert( String reportId, IXDocReportLoader reportLoader,
                                                        Map<String, Object> contextMap, Options options,
                                                        OutputStream out, XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        IXDocReport report = getReport( reportId, reportLoader, registry );
        report.convert( contextMap, options, out );
        return report;
    }

    /**
     * Get the template report from the cache registered with report id and load it if it is not found by using the
     * given report loader.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param reportLoader the report loader used to load the template report if it is not already loaded.
     * @return an instance of the loaded template report.
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport getReport( String reportId, IXDocReportLoader reportLoader )
        throws IOException, XDocReportException
    {
        XDocReportRegistry registry = getDefaultRegistry();
        return getReport( reportId, reportLoader, registry );
    }

    /**
     * Get the template report from the cache registered with report id and load it if it is not found by using the
     * given report loader by setting the registry.
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param reportLoader the report loader used to load the template report if it is not already loaded.
     * @param registry
     * @return an instance of the loaded template report.
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport getReport( String reportId, IXDocReportLoader reportLoader, XDocReportRegistry registry )
        throws IOException, XDocReportException
    {

        IXDocReport report = registry.getReport( reportId );
        if ( report != null )
        {
            return report;
        }
        return loadReport( reportId, reportLoader, registry );
    }

    /**
     * Load the template report by using the given report loader and cache it to the given registry
     * 
     * @param reportId the report id used to retrieves from the cache the template report if it is already loaded.
     * @param reportLoader the report loader used to load the template report.
     * @param registry
     * @return an instance of the loaded template report.
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport loadReport( String reportId, IXDocReportLoader reportLoader, XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        // 1) Get sourceStream
        InputStream sourceStream = reportLoader.getSourceStream( reportId );
        if ( sourceStream == null )
        {
            throw new XDocReportException( "Input stream is null with reportId=" + reportId );
        }
        // 2) Load report with template engine kind
        String templateEngineKind = reportLoader.getTemplateEngineKind( reportId );
        IXDocReport report = registry.loadReport( sourceStream, reportId, templateEngineKind );
        // 3) Set FieldsMetaData
        report.setFieldsMetadata( reportLoader.getFieldsMetadata( reportId ) );
        return report;
    }

    // ------------------------------------ No cache report

    /**
     * Generate report without cache.
     * 
     * @param sourceStream the input stream of the template report (docx, odt, etc).
     * @param templateEngineKind the template engine kind (Velocity, Freemarker).
     * @param metadata the fields metadata or null if no need the fields metadata.
     * @param contextMap the context map values used to replace values of the template report.
     * @param out the output stream where the report must be generated.
     * @return an instance of the loaded template report.
     * @throws XDocReportException
     * @throws IOException
     */
    public static IXDocReport generateReport( InputStream sourceStream, String templateEngineKind,
                                              FieldsMetadata metadata, Map<String, Object> contextMap, OutputStream out )
        throws XDocReportException, IOException
    {
        return generateReport( sourceStream, templateEngineKind, metadata, contextMap, out, getDefaultRegistry() );
    }

    /**
     * Generate report without cache by setting the registry.
     * 
     * @param sourceStream the input stream of the template report (docx, odt, etc).
     * @param templateEngineKind the template engine kind (Velocity, Freemarker).
     * @param metadata the fields metadata or null if no need the fields metadata.
     * @param contextMap the context map values used to replace values of the template report.
     * @param out the output stream where the report must be generated.
     * @param registry
     * @return an instance of the loaded template report.
     * @throws XDocReportException
     * @throws IOException
     */
    public static IXDocReport generateReport( InputStream sourceStream, String templateEngineKind,
                                              FieldsMetadata metadata, Map<String, Object> contextMap,
                                              OutputStream out, XDocReportRegistry registry )
        throws XDocReportException, IOException
    {
        if ( sourceStream == null )
        {
            throw new XDocReportException( "Input stream is null with reportId=" );
        }
        IXDocReport report = registry.loadReport( sourceStream, templateEngineKind, false );
        report.setFieldsMetadata( metadata );
        return report;
    }

    /**
     * Generate report and convert it to another format (PDF, XHTML) without cache.
     * 
     * @param sourceStream the input stream of the template report (docx, odt, etc).
     * @param templateEngineKind the template engine kind (Velocity, Freemarker).
     * @param metadata the fields metadata or null if no need the fields metadata.
     * @param contextMap the context map values used to replace values of the template report.
     * @param options the converter options.
     * @param out the output stream where the report must be generated.
     * @return an instance of the loaded template report.
     * @throws XDocReportException
     * @throws IOException
     */
    public static IXDocReport generateReportAndConvert( InputStream sourceStream, String templateEngineKind,
                                                        FieldsMetadata metadata, Map<String, Object> contextMap,
                                                        Options options, OutputStream out )
        throws IOException, XDocReportException
    {
        return generateReportAndConvert( sourceStream, templateEngineKind, metadata, contextMap, options, out,
                                         getDefaultRegistry() );
    }

    /**
     * Generate report and convert it to another format (PDF, XHTML) without cache by setting the registry.
     * 
     * @param sourceStream the input stream of the template report (docx, odt, etc).
     * @param templateEngineKind the template engine kind (Velocity, Freemarker).
     * @param metadata the fields metadata or null if no need the fields metadata.
     * @param contextMap the context map values used to replace values of the template report.
     * @param options the converter options.
     * @param out the output stream where the report must be generated.
     * @param registry
     * @return an instance of the loaded template report.
     * @throws XDocReportException
     * @throws IOException
     */
    public static IXDocReport generateReportAndConvert( InputStream sourceStream, String templateEngineKind,
                                                        FieldsMetadata metadata, Map<String, Object> contextMap,
                                                        Options options, OutputStream out, XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        IXDocReport report = generateReport( sourceStream, templateEngineKind, metadata, contextMap, out, registry );
        report.convert( contextMap, options, out );
        return report;
    }

    /**
     * Returns the default registry.
     * 
     * @return
     */
    private static XDocReportRegistry getDefaultRegistry()
    {
        return XDocReportRegistry.getRegistry();
    }
}
