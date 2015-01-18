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
package fr.opensagres.xdocreport.document.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.ProcessState;
import fr.opensagres.xdocreport.document.domain.DataContext;
import fr.opensagres.xdocreport.document.domain.ReportId;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class XDocReportServiceImpl
{

    private static final Logger LOGGER = LogUtils.getLogger( XDocReportServiceImpl.class );

    public static final XDocReportServiceImpl INSTANCE = new XDocReportServiceImpl();

    private boolean cacheOriginalDocument;

    public List<ReportId> listReports()
    {
        XDocReportRegistry registry = getXDocReportRegistry();
        Collection<IXDocReport> reports = registry.getCachedReports();

        List<ReportId> reportIDs = new ArrayList<ReportId>();
        for ( IXDocReport docReport : reports )
        {
            ReportId aReport =
                new ReportId( docReport.getId(), docReport.getTemplateEngine().getId(),
                              new Date( docReport.getLastModified() ) );

            reportIDs.add( aReport );
        }
        return reportIDs;
    }

    /**
     * @param reportID the report ID which was registered in the registry.
     * @param dataContext "live" data to be merged in the template
     * @param options optional, used to customize the output in the case if convertion must be done.
     * @return the merged content eventually converted in another format (PDF or HTML)
     */
    public byte[] process( String reportId, List<DataContext> dataContext, Options options )
        throws XDocReportException
    {
        XDocReportRegistry registry = getXDocReportRegistry();

        IXDocReport report = registry.getReport( reportId );
        if ( report == null )
        {
            throw new XDocReportException( "Cannot find report with the id=" + reportId );
        }
        return process( report, dataContext, options );
    }

    /**
     * @param document the template in a binary form
     * @param fieldsMetadatas fields metadata used to generate for instance lzy loop for ODT, Docx..row table.
     * @param templateEngineID the template engine ID....
     * @param dataContext "live" data to be merged in the template
     * @param options optional, used to customize the output in the case if convertion must be done.
     * @return the merged content eventually converted in another format (PDF or HTML)
     */
    public byte[] process( byte[] document, FieldsMetadata fieldsMetadata, String templateEngineId,
                           List<DataContext> dataContext, Options options )
        throws XDocReportException
    {

        // 1) Load report without cache it to the registry
        IXDocReport report =
            loadReport( getXDocReportRegistry(), "", document, fieldsMetadata, templateEngineId, false,
                        isCacheOriginalDocument() );
        // 2) Create context + Process report generation
        return process( report, dataContext, options );
    }

    private byte[] process( IXDocReport report, List<DataContext> dataContext, Options options )
        throws XDocReportException
    {
        try
        {
            // 2) Create context
            IContext context = createContext( report, dataContext );
            // 3) Process report generation
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if ( options == null )
            {
                // Process
                report.process( context, out );
            }
            else
            {
                // Converter
                report.convert( context, options, out );
            }
            return out.toByteArray();
        }
        catch ( Throwable e )
        {
            throw new XDocReportException( e );
        }
    }

    private IContext createContext( IXDocReport report, List<DataContext> dataContexts )
        throws XDocReportException
    {
        IContext context;
        try
        {
            context = report.createContext();
            for ( DataContext dataContext : dataContexts )
            {
                context.put( dataContext.getKey(), dataContext.getValue() );
            }

            return context;
        }
        catch ( XDocReportException e )
        {
            throw new XDocReportException( e );
        }
    }

    public void registerReport( String reportId, byte[] document, FieldsMetadata fieldsMetadata, String templateEngineId )
        throws XDocReportException
    {
        // Load report and cache it to the registry
        loadReport( getXDocReportRegistry(), reportId, document, fieldsMetadata, templateEngineId, true,
                    isCacheOriginalDocument() );

        System.out.println( getXDocReportRegistry().getCachedReports() );
    }

    private IXDocReport loadReport( XDocReportRegistry registry, String reportId, byte[] document,
                                    FieldsMetadata fieldsMetadata, String templateEngineId, boolean cacheReport,
                                    boolean cacheOriginalDocument )
        throws XDocReportException
    {
        // 1) Get sourceStream
        InputStream sourceStream = getInputStream( document );
        // 2) Load report
        IXDocReport report = null;
        try
        {
            report = registry.loadReport( sourceStream, reportId, templateEngineId, cacheReport );
        }
        catch ( Throwable e )
        {
            throw new XDocReportException( e );
        }

        // 6) Set FieldsMetaData
        report.setFieldsMetadata( fieldsMetadata );

        // 7) Set cache
        report.setCacheOriginalDocument( cacheOriginalDocument );
        return report;

    }

    private ByteArrayInputStream getInputStream( byte[] document )
        throws XDocReportException
    {
        if ( document == null )
        {
            throw new XDocReportException( "Byte array of the document cannot be null." );
        }
        return new ByteArrayInputStream( document );
    }

    public byte[] convert( byte[] document, Options options )
        throws XDocReportException
    {
        try
        {
            IConverter converter = ConverterRegistry.getRegistry().findConverter( options );
            InputStream in = getInputStream( document );
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            converter.convert( in, out, options );
            return out.toByteArray();
        }
        catch ( XDocConverterException e )
        {
            throw new XDocReportException( e );
        }
    }

    public void unregisterReport( String reportId )
    {
        XDocReportRegistry registry = getXDocReportRegistry();
        registry.unregisterReport( reportId );
    }

    public byte[] download( String reportID, String processState )
        throws XDocReportException
    {
        XDocArchive archive = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        IXDocReport report = getXDocReportRegistry().getReport( reportID );
        if(report==null)
              throw new XDocReportException( "report not found " +reportID);
        if ( ProcessState.ORIGINAL.name().equalsIgnoreCase(  processState ) )
        {
            archive = report.getOriginalDocumentArchive().createCopy();
        }
        else if ( ProcessState.PREPROCESSED.name().equalsIgnoreCase( processState ) )
        {

            archive = report.getPreprocessedDocumentArchive().createCopy();
        }
        else
        {
            throw new XDocReportException( "processState should be " + ProcessState.ORIGINAL + " or "+ ProcessState.PREPROCESSED );
        }

        try
        {
            XDocArchive.writeZip( archive, out );
        }
        catch ( IOException e )
        {
            LOGGER.severe( e.getMessage() );
            throw new XDocReportException(e.getMessage());
        }

        return out.toByteArray();
    }

    protected XDocReportRegistry getXDocReportRegistry()
    {
        return XDocReportRegistry.getRegistry();
    }

    public boolean isCacheOriginalDocument()
    {
        return cacheOriginalDocument;
    }

    public void setCacheOriginalDocument( boolean cacheOriginalDocument )
    {
        this.cacheOriginalDocument = cacheOriginalDocument;
    }
}
