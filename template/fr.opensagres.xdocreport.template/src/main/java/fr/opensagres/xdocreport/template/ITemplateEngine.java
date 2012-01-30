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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.template.cache.ITemplateCacheInfoProvider;
import fr.opensagres.xdocreport.template.config.ITemplateEngineConfiguration;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * Template engine used to merge Java model with some entries (XML file) from an XML document archive.
 */
public interface ITemplateEngine
{

    /**
     * Returns the template engien kind (Freemarker, Velocity..)
     * 
     * @return
     */
    String getKind();

    void setTemplateCacheInfoProvider( ITemplateCacheInfoProvider templateCacheInfoProvider );

    ITemplateCacheInfoProvider getTemplateCacheInfoProvider();

    void setConfiguration( ITemplateEngineConfiguration configuration );

    ITemplateEngineConfiguration getConfiguration();

    /**
     * Return the identifier of the template engine.
     * 
     * @return
     */
    String getId();

    /**
     * Create a new context to register Java model.
     * 
     * @return
     */
    IContext createContext();

    /**
     * Merge Java model coming from the given context with the XML file entryName of the given document archive and
     * modify the entry of the document archive.
     * 
     * @param reportId {@link IXDocReport#getId()}.
     * @param entryName entry name of the XML file from the document archive which must be merged. This XML file is the
     *            template.
     * @param readerProvider entry reader provider.
     * @param context Java model context
     * @throws XDocReportException
     * @throws IOException
     */
    void process( String reportId, String entryName, IEntryReaderProvider readerProvider,
                  IEntryWriterProvider writerProvider, IContext context )
        throws XDocReportException, IOException;

    void process( String reportId, String entryName, IEntryReaderProvider readerProvider, Writer writer,
                  IContext context )
        throws XDocReportException, IOException;

    /**
     * Merge Java model coming from the given context with the given reader and register the merge result in the given
     * writer.
     * 
     * @param entryName template name
     * @param context Java model context
     * @param reader template reader to merge
     * @param writer merge result writer
     * @throws XDocReportException
     * @throws IOException
     */
    void process( String entryName, IContext context, Reader reader, Writer writer )
        throws XDocReportException, IOException;

    void extractFields( Reader reader, String entryName, FieldsExtractor extractor )
        throws XDocReportException;

    void extractFields( IEntryReaderProvider readerProvider, String entryName, FieldsExtractor extractor )
        throws XDocReportException;

    IDocumentFormatter getDocumentFormatter();

}
