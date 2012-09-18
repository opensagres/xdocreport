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
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class XDocReport
{

    // ------------------------------------ With controller

    /**
     * Generate report by using controller to load report.
     * 
     * @param reportId
     * @param controller
     * @param contextMap
     * @param out
     * @return
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
     * Generate report by using controller to load report.
     * 
     * @param reportId
     * @param controller
     * @param contextMap
     * @param out
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public static IXDocReport generateReport( String reportId, IXDocReportController controller,
                                              Map<String, Object> contextMap, OutputStream out,
                                              XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        IXDocReport report = getReport( reportId, controller, registry );
        IContext context = report.createContext();
        context.putMap( contextMap );
        report.process( context, out );
        return report;
    }

    /**
     * Generate report and convert it by using controller to load report.
     * 
     * @param reportId
     * @param controller
     * @param contextMap
     * @param out
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

    public static IXDocReport generateReportAndConvert( String reportId, IXDocReportController controller,
                                                        Map<String, Object> contextMap, Options options,
                                                        OutputStream out, XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        IXDocReport report = getReport( reportId, controller, registry );
        IContext context = report.createContext();
        context.putMap( contextMap );
        report.convert( context, options, out );
        return report;
    }

    public static IXDocReport getReport( String reportId, IXDocReportController controller )
        throws IOException, XDocReportException
    {
        XDocReportRegistry registry = getDefaultRegistry();
        return getReport( reportId, controller, registry );
    }

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

    public static IXDocReport generateReport( String reportId, IXDocReportLoader reportLoader,
                                              Map<String, Object> contextMap, OutputStream out )
        throws IOException, XDocReportException
    {
        return generateReport( reportId, reportLoader, contextMap, out, getDefaultRegistry() );
    }

    public static IXDocReport generateReport( String reportId, IXDocReportLoader reportLoader,
                                              Map<String, Object> contextMap, OutputStream out,
                                              XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        IXDocReport report = getReport( reportId, reportLoader, registry );
        IContext context = report.createContext();
        context.putMap( contextMap );
        report.process( context, out );
        return report;
    }

    public static IXDocReport generateReportAndConvert( String reportId, IXDocReportLoader reportLoader,
                                                        Map<String, Object> contextMap, Options options,
                                                        OutputStream out )
        throws IOException, XDocReportException
    {
        return generateReportAndConvert( reportId, reportLoader, contextMap, options, out, getDefaultRegistry() );
    }

    public static IXDocReport generateReportAndConvert( String reportId, IXDocReportLoader reportLoader,
                                                        Map<String, Object> contextMap, Options options,
                                                        OutputStream out, XDocReportRegistry registry )
        throws IOException, XDocReportException
    {
        IXDocReport report = getReport( reportId, reportLoader, registry );
        IContext context = report.createContext();
        context.putMap( contextMap );
        report.convert( context, options, out );
        return report;
    }

    public static IXDocReport getReport( String reportId, IXDocReportLoader reportLoader )
        throws IOException, XDocReportException
    {
        XDocReportRegistry registry = getDefaultRegistry();
        return getReport( reportId, reportLoader, registry );
    }

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

    public static IXDocReport generateReport( InputStream sourceStream, String templateEngineKind,
                                              FieldsMetadata metadata, Map<String, Object> contextMap, OutputStream out )
        throws XDocReportException, IOException
    {
        return generateReport( sourceStream, templateEngineKind, metadata, contextMap, out, getDefaultRegistry() );
    }

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
        // 3) Set FieldsMetaData
        report.setFieldsMetadata( metadata );
        return report;
    }

    private static XDocReportRegistry getDefaultRegistry()
    {
        return XDocReportRegistry.getRegistry();
    }
}
