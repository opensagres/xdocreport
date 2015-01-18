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
package fr.opensagres.xdocreport.document.preprocessor;

import static fr.opensagres.xdocreport.core.utils.XMLUtils.prettyPrint;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.MultiWriter;
import fr.opensagres.xdocreport.core.io.StreamCancelable;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * Abstract class for {@link IXDocPreprocessor}.
 */
public abstract class AbstractXDocPreprocessor<T>
    implements IXDocPreprocessor
{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( AbstractXDocPreprocessor.class.getName() );

    public void preprocess( String entryName, XDocArchive documentArchive, FieldsMetadata fieldsMetadata,
                            IDocumentFormatter formater, Map<String, Object> sharedContext )
        throws XDocReportException, IOException
    {
        if ( fieldsMetadata == null )
        {
            fieldsMetadata = FieldsMetadata.EMPTY;
        }
        // 1) Start preprocess
        long startTime = -1;
        if ( LOGGER.isLoggable( Level.FINE ) )
        {

            startTime = System.currentTimeMillis();

            LOGGER.fine( "Start preprocess for the entry=" + entryName );
        }

        // 2) Do preprocess
        T reader = null;
        Writer writer = null;
        boolean result = false;
        try
        {
            // Get reader + writer for the entry name.
            reader = getSource( documentArchive, entryName );
            writer = getWriter( entryName, documentArchive );

            // Do preprocess which use reader and store the result of preprocess
            // in the writer.
            result = preprocess( entryName, reader, writer, fieldsMetadata, formater, sharedContext );

            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( "Result preprocess for the entry=" + entryName + ": "
                    + prettyPrint( ( (MultiWriter) writer ).getWriter( 1 ).toString() ) );
                LOGGER.fine( "End preprocess for the entry=" + entryName + " done with "
                    + ( System.currentTimeMillis() - startTime ) + "(ms)." );
            }
        }
        catch ( Throwable e )
        {
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( "End preprocess for the entry=" + entryName + " done with "
                    + ( System.currentTimeMillis() - startTime ) + "(ms)." );
                LOGGER.throwing( getClass().getName(), "preprocess", e );
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
            // Close reader + writer
            closeSource( reader );
            if ( writer != null )
            {
                if ( result )
                {
                    IOUtils.closeQuietly( writer );
                }
                else
                {
                    // preprocess was not done, don't modify the entry.
                    ( (StreamCancelable) writer ).cancel();
                }
            }
        }

    }

    protected abstract void closeSource( T reader )
        throws XDocReportException, IOException;

    private Writer getWriter( String entryName, XDocArchive documentArchive )
    {

        if ( LOGGER.isLoggable( Level.FINE ) )
        {
            return new MultiWriter( documentArchive.getEntryWriter( entryName ), new StringWriter() );
        }
        return documentArchive.getEntryWriter( entryName );
    }

    public boolean create( String entryName, XDocArchive outputArchive, FieldsMetadata fieldsMetadata,
                           IDocumentFormatter formatter, Map<String, Object> sharedContext )
        throws XDocReportException, IOException
    {
        return false;
    }

    protected void createAndProcess( String entryName, XDocArchive outputArchive, FieldsMetadata fieldsMetadata,
                                     IDocumentFormatter formatter, Map<String, Object> sharedContext, InputStream input )
        throws IOException, XDocReportException
    {
        XDocArchive.setEntry( outputArchive, entryName, input );

        // 2) preprocess it
        preprocess( entryName, outputArchive, fieldsMetadata, formatter, sharedContext );
    }

    public abstract boolean preprocess( String entryName, T reader, Writer writer, FieldsMetadata fieldsMetadata,
                                        IDocumentFormatter formater, Map<String, Object> context )
        throws XDocReportException, IOException;

    protected abstract T getSource( XDocArchive documentArchive, String entryName )
        throws XDocReportException, IOException;
}
