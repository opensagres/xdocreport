/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package fr.opensagres.xdocreport.template;

import static fr.opensagres.xdocreport.core.utils.StringUtils.isNotEmpty;
import static fr.opensagres.xdocreport.core.utils.XMLUtils.prettyPrint;
import static fr.opensagres.xdocreport.template.utils.TemplateUtils.getCachedTemplateName;
import static java.lang.String.format;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.MultiWriter;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.template.cache.ITemplateCacheInfoProvider;
import fr.opensagres.xdocreport.template.config.ITemplateEngineConfiguration;

public abstract class AbstractTemplateEngine
    implements ITemplateEngine
{

    private ITemplateCacheInfoProvider templateCacheInfoProvider;

    private ITemplateEngineConfiguration configuration;

    public ITemplateCacheInfoProvider getTemplateCacheInfoProvider()
    {
        return templateCacheInfoProvider;
    }

    public void setTemplateCacheInfoProvider( ITemplateCacheInfoProvider templateCacheInfoProvider )
    {
        this.templateCacheInfoProvider = templateCacheInfoProvider;
    }

    public ITemplateEngineConfiguration getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration( ITemplateEngineConfiguration configuration )
    {
        this.configuration = configuration;
    }

    public void process( String reportId, String entryName, IEntryReaderProvider readerProvider,
                         IEntryWriterProvider writerProvider, IContext context )
        throws XDocReportException, IOException
    {

        // Get writer of the entry to merge Java model with template engine
        Writer writer = writerProvider.getEntryWriter( entryName );
        process( reportId, entryName, readerProvider, writer, context );
    }

    private static final Logger LOGGER = LogUtils.getLogger( AbstractTemplateEngine.class.getName() );

    public void process( String reportId, String entryName, IEntryReaderProvider readerProvider, Writer writer,
                         IContext context )
        throws XDocReportException, IOException
    {
        boolean useTemplateCache = isUseTemplateCache( reportId );
        // 1) Start process template engine
        long startTime = -1;
        if ( LOGGER.isLoggable( Level.FINE ) )
        {

            startTime = System.currentTimeMillis();
            LOGGER.fine( format( "Start template engine id=%s for the entry=%s with template cache=%s", getId(),
                                 entryName, useTemplateCache ) );

        }

        Reader reader = null;
        try
        {
            writer = getWriter( writer );
            if ( useTemplateCache )
            {
                // cache template is used, process it
                processWithCache( getCachedTemplateName( reportId, entryName ), context, writer );
            }
            else
            {
                // No cache template is used, get the reader from the entry
                reader = readerProvider.getEntryReader( entryName );
                processNoCache( entryName, context, reader, writer );
            }
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                // Debug start preprocess
                startTime = System.currentTimeMillis();

                LOGGER.fine( format( "Result template engine id=" + getId() + "  for the entry=" + entryName + ": " ) );
                LOGGER.fine( prettyPrint( ( (MultiWriter) writer ).getWriter( 1 ).toString() ) );

                LOGGER.fine( "End template engine id=" + getId() + " for the entry=" + entryName + " done with "
                    + ( System.currentTimeMillis() - startTime ) + "(ms)." );
            }

        }
        catch ( Throwable e )
        {
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( ( "End template engine id=" + getId() + " for the entry=" + entryName + " done with "
                    + ( System.currentTimeMillis() - startTime ) + "(ms)." ) );
            }
            if ( e instanceof RuntimeException )
            {
                throw (RuntimeException) e;
            }
            if ( e instanceof IOException )
            {
                throw (IOException) e;
            }
            if ( e instanceof XDocReportException )
            {
                throw (XDocReportException) e;
            }
            throw new XDocReportException( e );
        }
        finally
        {
            if ( reader != null )
            {
                IOUtils.closeQuietly( reader );
            }
            if ( writer != null )
            {
                IOUtils.closeQuietly( writer );
            }
        }
    }

    public void process( String entryName, IContext context, Reader reader, Writer writer )
        throws XDocReportException, IOException
    {
        try
        {
            processNoCache( entryName, context, reader, writer );
        }
        finally
        {
            if ( reader != null )
            {
                IOUtils.closeQuietly( reader );
            }
            if ( writer != null )
            {
                IOUtils.closeQuietly( writer );
            }
        }

    }

    public void extractFields( IEntryReaderProvider readerProvider, String entryName, FieldsExtractor extractor )
        throws XDocReportException
    {
        Reader reader = readerProvider.getEntryReader( entryName );
        extractFields( reader, entryName, extractor );
    }

    private Writer getWriter( Writer writer )
    {
        if ( LOGGER.isLoggable( Level.FINE ) )
        {
            return new MultiWriter( writer, new StringWriter() );
        }
        return writer;
    }

    protected boolean isUseTemplateCache( String reportId )
    {
        return isNotEmpty( reportId ) && getTemplateCacheInfoProvider() != null
            && getTemplateCacheInfoProvider().existsReport( reportId );

    }

    protected abstract void processWithCache( String templateName, IContext context, Writer writer )
        throws XDocReportException, IOException;

    protected abstract void processNoCache( String entryName, IContext context, Reader reader, Writer writer )
        throws XDocReportException, IOException;
}
