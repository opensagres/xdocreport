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
package fr.opensagres.xdocreport.document.registry;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.cache.CacheStorageRegistry;
import fr.opensagres.xdocreport.core.cache.ICacheStorage;
import fr.opensagres.xdocreport.core.io.IEntryInfo;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.discovery.IXDocReportFactoryDiscovery;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.cache.ITemplateCacheInfoProvider;

/**
 * Registry for loading and caching {@link IXDocReport} instances.
 */
public class XDocReportRegistry
    extends AbstractRegistry<IXDocReportFactoryDiscovery>
    implements ITemplateCacheInfoProvider, Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1653550067972102340L;

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( AbstractRegistry.class.getName() );

    private static final String FILES_TYPE_ERROR =
        "Impossible to create report for the input stream. The report loader supports only [{0}] files type.";

    private static final XDocReportRegistry INSTANCE = new XDocReportRegistry();

    private final Collection<IXDocReportFactoryDiscovery> reportFactoryDiscoveries =
        new ArrayList<IXDocReportFactoryDiscovery>();

    /**
     * IXDocReport cache.
     */
    private final ICacheStorage<String, IXDocReport> cachedReports;
    private Timer cleanupTimer;
    public XDocReportRegistry()
    {
        super( IXDocReportFactoryDiscovery.class );
        this.cachedReports = createCache();
    }

    /**
     * Create the storage cache to store instances of IXDocReport.
     * 
     * @return
     */
    protected ICacheStorage<String, IXDocReport> createCache()
    {
        return CacheStorageRegistry.getRegistry().createCache();
    }

    public static XDocReportRegistry getRegistry()
    {
        return INSTANCE;
    }

    /**
     * Load report.
     *
     * Note : this function does't cache the report.
     * 
     * @param sourceStream
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, null, null, null, false );
    }

    /**
     * Load report.
     * 
     * @param sourceStream
     * @param cacheReport
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, boolean cacheReport )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, null, null, null, cacheReport );
    }

    /**
     * Load report.
     *
     * Note : this function cache the report.
     * 
     * @param sourceStream
     * @param reportId
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, String reportId )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, reportId, true );
    }

    /**
     * Load report.
     * 
     * @param sourceStream
     * @param reportId
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, String reportId, boolean cacheReport )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, reportId, null, null, cacheReport );
    }

    /**
     * Load report.
     *
     * Note : this function cache the report.
     * 
     * @param sourceStream
     * @param reportId
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, String reportId, ITemplateEngine templateEngine )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, reportId, null, templateEngine, true );
    }

    /**
     * Load report.
     * 
     * @param sourceStream
     * @param reportId
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, String reportId, ITemplateEngine templateEngine,
                                   boolean cacheReport )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, reportId, null, templateEngine, cacheReport );
    }

    /**
     * Load report.
     *
     * Note : this function cache the report.
     * 
     * @param sourceStream
     * @param reportId
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, String reportId, String templateEngineKind )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, reportId, templateEngineKind, true );
    }

    /**
     * Load report.
     * 
     * @param sourceStream
     * @param reportId
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, String reportId, String templateEngineKind,
                                   boolean cacheReport )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, reportId, templateEngineKind, null, cacheReport );
    }

    /**
     * Load report.
     *
     * Note : this function cache the report.
     *
     * @param sourceStream
     * @param reportId
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, String reportId, TemplateEngineKind templateEngineKind )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, reportId, templateEngineKind, true );
    }

    /**
     * Load report.
     * 
     * @param sourceStream
     * @param reportId
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, String reportId, TemplateEngineKind templateEngineKind,
                                   boolean cacheReport )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, reportId, templateEngineKind.name(), null, cacheReport );
    }

    /**
     * Load report.
     *
     * Note : this function does't cache the report.
     * 
     * @param sourceStream
     * @param templateEngine
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, ITemplateEngine templateEngine )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, templateEngine, false );
    }

    /**
     * Load report.
     * 
     * @param sourceStream
     * @param templateEngine
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, ITemplateEngine templateEngine, boolean cacheReport )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, null, null, templateEngine, cacheReport );
    }

    /**
     * Load report.
     *
     * Note : this function does't cache the report.
     * 
     * @param sourceStream
     * @param templateEngineKind
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, TemplateEngineKind templateEngineKind )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, templateEngineKind, false );
    }

    /**
     * Load report.
     * 
     * @param sourceStream
     * @param templateEngineKind
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public IXDocReport loadReport( InputStream sourceStream, TemplateEngineKind templateEngineKind, boolean cacheReport )
        throws IOException, XDocReportException
    {
        return loadReport( sourceStream, null, templateEngineKind.name(), null, cacheReport );
    }

    private IXDocReport loadReport( InputStream sourceStream, String reportId, String templateEngineKind,
                                    ITemplateEngine templateEngine, boolean cacheReport )
        throws IOException, XDocReportException
    {
        initializeIfNeeded();
        // 2) zip was loaded, create an instance of report
        IXDocReport report = createReport( sourceStream );
        // 3) Update the report id if need.
        if ( StringUtils.isEmpty( reportId ) )
        {
            reportId = report.toString();
        }
        report.setId( reportId );
        // 4) Search or set the template engine.
        if ( templateEngine == null && StringUtils.isNotEmpty( templateEngineKind ) )
        {
            // Template engine was not forced.
            // Search template engine
            String documentKind = report.getKind();
            templateEngine =
                TemplateEngineInitializerRegistry.getRegistry().getTemplateEngine( templateEngineKind, documentKind );
            if ( templateEngine == null )
            {
                templateEngine =
                    TemplateEngineInitializerRegistry.getRegistry().getTemplateEngine( templateEngineKind, null );
            }
        }
        report.setTemplateEngine( templateEngine );
        if ( cacheReport )
        {
            registerReport( report );
        }
        return report;
    }

    public IXDocReport createReport( InputStream sourceStream )
        throws IOException, XDocReportException
    {
        // Load zipped XML document
        XDocArchive documentArchive = XDocArchive.readZip( sourceStream );
        return createReport( documentArchive );
    }

    public IXDocReport createReport( XDocArchive documentArchive )
        throws IOException, XDocReportException
    {
        initializeIfNeeded();
        for ( IXDocReportFactoryDiscovery discovery : reportFactoryDiscoveries )
        {
            if ( discovery.isAdaptFor( documentArchive ) )
            {
                IXDocReport report = discovery.createReport();
                if ( report != null )
                {
                    report.setDocumentArchive( documentArchive );
                }
                return report;
            }
        }

        throw new XDocReportException( format( FILES_TYPE_ERROR, getFilesType() ) );
    }

    private String getFilesType()
    {
        StringBuilder filesType = new StringBuilder();
        Collection<IXDocReportFactoryDiscovery> discoveries = getReportFactoryDiscoveries();
        for ( IXDocReportFactoryDiscovery discovery : discoveries )
        {
            if ( filesType.length() > 0 )
            {
                filesType.append( "," );
            }
            filesType.append( discovery.getMimeMapping().getExtension() );
        }
        return filesType.toString();
    }

    /**
     * Register report and throws XDocReportException if it already exists a report in the registry with the same id .
     * 
     * @param report
     * @throws XDocReportException
     */
    public void registerReport( IXDocReport report )
        throws XDocReportException
    {
        registerReport( report, false );
    }

    /**
     * Register report.
     * 
     * @param report the report to register
     * @param force true if report must be forced (if report already exists with the same id) and false otherwise (throw
     *            XDocReportException if report exists with the same id).
     * @throws XDocReportException
     */
    public synchronized void registerReport( IXDocReport report, boolean force )
        throws XDocReportException
    {
        String reportId = report.getId();
        if ( StringUtils.isEmpty( reportId ) )
        {
            throw new XDocReportException( "Cannot register report. IXDocReport#getId() cannot be empty." );
        }
        if ( !force )
        {
            checkReportId( reportId );
        }
        cachedReports.put( report.getId(), report );
    }

    /**
     * Check if registry can register the report with this id.
     * 
     * @param reportId
     * @throws XDocReportException
     */
    public void checkReportId( String reportId )
        throws XDocReportException
    {
        if ( cachedReports.containsKey( reportId ) )
        {
            String msg =
                String.format( "Cannot register report. A report with id=%s already exists in the registry", reportId );
            LOGGER.warning( msg );
            throw new XDocReportException( msg );
        }
    }

    /**
     * Returns the report identified with the given id.
     * 
     * @param reportId
     * @return
     */
    public IXDocReport getReport( String reportId )
    {
        return cachedReports.get( reportId );
    }

    /**
     * Returns true if report identified with the given id exists in the registry and false otherwise.
     * 
     * @param reportId
     * @return
     */
    public boolean existsReport( String reportId )
    {
        return cachedReports.containsKey( reportId );
    }

    /**
     * Unregister report identified with the given id.
     * 
     * @param reportId
     */
    public void unregisterReport( String reportId )
    {
        if ( existsReport( reportId ) )
        {
            cachedReports.remove( reportId );
        }
    }

    /**
     * Unregister report.
     * 
     * @param report
     */
    public void unregisterReport( IXDocReport report )
    {
        if ( report != null )
        {
            cachedReports.remove( report.getId() );
        }
    }

    /**
     * Returns list of report factory discoveries.
     * 
     * @return
     */
    public Collection<IXDocReportFactoryDiscovery> getReportFactoryDiscoveries()
    {
        initializeIfNeeded();

        return reportFactoryDiscoveries;
    }

    /**
     * Returns cached report {@link IXDocReport};
     * 
     * @return
     */
    public Collection<IXDocReport> getCachedReports()
    {
        return Collections.unmodifiableCollection( cachedReports.values() );
    }
    
	/**
	 * Clear cache with timeout. If this method is called, you must call
	 * {@link XDocReportRegistry#dispose()} method at the end of your program to
	 * stop the timer Thread.
	 * 
	 * @param timeout
	 */
    public void setClearTimeout( long timeout )
    {
    	if (cleanupTimer == null) {
    		cleanupTimer = new Timer();
    	}
    	cleanupTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
                clear();
				
			}
		}, timeout, timeout);
    }

    /**
     * Clear the cached reports.
     */
    public void clear()
    {
        cachedReports.clear();
    }

    /**
     * Returns mime mapping for the file extension.
     * 
     * @param fileExtension
     * @return
     * @throws IOException
     * @throws XDocReportException
     */
    public MimeMapping getMimeMapping( String fileExtension )
        throws IOException, XDocReportException
    {
        initializeIfNeeded();
        for ( IXDocReportFactoryDiscovery discovery : reportFactoryDiscoveries )
        {
            if ( discovery.isAdaptFor( fileExtension ) )
            {
                return discovery.getMimeMapping();
            }
        }
        return null;
    }

    /**
     * Generate unique report id.
     * 
     * @param reportId
     * @return
     */
    public synchronized String generateUniqueReportId( String reportId )
    {
        IXDocReport report = getReport( reportId );
        if ( report != null )
        {
            StringBuilder id = new StringBuilder( reportId );
            id.append( "_" );
            id.append( System.currentTimeMillis() );
            return id.toString();
        }
        return reportId;
    }

    public IEntryInfo getTemplateCacheInfo( String reportId, String entryName )
    {
        IXDocReport report = getReport( reportId );
        if ( report == null )
        {
            return null;
        }
        return report.getPreprocessedDocumentArchive().getEntryInfo( entryName );
    }

    @Override
    protected void doDispose()
    {
    	if (cleanupTimer != null) {
        	this.cleanupTimer.cancel();	
    	}
        this.reportFactoryDiscoveries.clear();
        this.cachedReports.clear();
    }

    @Override
    protected boolean registerInstance( IXDocReportFactoryDiscovery instance )
    {
        reportFactoryDiscoveries.add( instance );
        return true;
    }
}
