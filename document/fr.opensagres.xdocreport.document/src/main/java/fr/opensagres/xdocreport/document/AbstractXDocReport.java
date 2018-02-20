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

import static fr.opensagres.xdocreport.core.utils.StringUtils.EMPTY_STRING_ARRAY;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IEntryOutputStreamProvider;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.dump.DumperOptions;
import fr.opensagres.xdocreport.document.dump.DumperRegistry;
import fr.opensagres.xdocreport.document.dump.IDumper;
import fr.opensagres.xdocreport.document.images.DefaultImageHandler;
import fr.opensagres.xdocreport.document.images.IImageRegistry;
import fr.opensagres.xdocreport.document.preprocessor.IXDocPreprocessor;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.registry.TextStylingRegistry;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.TemplateContextHelper;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * Abstract class for {@link IXDocReport} to implement to manage docx, odt...
 */
public abstract class AbstractXDocReport
    implements IXDocReport
{

    private static final Logger LOGGER = LogUtils.getLogger( AbstractXDocReport.class.getName() );

    private static final long serialVersionUID = -6632379345569386476L;

    /**
     * Map of {@link IXDocPreprocessor} to execute for an entry of the zipped XML Document (odt, docx...).
     */
    private Map<String, Collection<IXDocPreprocessor>> preprocessors =
        new LinkedHashMap<String, Collection<IXDocPreprocessor>>();

/**
     * id of the {@link IXDocReport}. This id is used to cache an instance of {@link IXDocReport} with
     * {@link XDocReportRegistry#loadReport(InputStream) and get instance from cache with
     * 
     * @link XDocReportRegistry#getReport(String)}.
     */
    private String id;

    /**
     * Template engine to use to merge Java model with the XML files of the from entries of the zipped XML Document
     * (odt, docx...) defined by {@link AbstractXDocReport#getXMLEntries()}.
     */
    private ITemplateEngine templateEngine;

    /**
     * Zipped XML Document (odt, docx) which is the original XML Document..
     */
    private XDocArchive originalArchive = null;

    /**
     * Zipped XML Document (odt, docx) which is the original XML Document preprocessed.
     */
    private XDocArchive preprocessedArchive = null;

    /**
     * XML entries which define XML document to merge with Java model with template engine.
     */
    private String[] xmlEntries;

    /**
     * Fields metadata used to manage lazy loop for table row.
     */
    private FieldsMetadata fieldsMetadata;

    /**
     * True if preprocessing was done et false otherwise.
     */
    private boolean preprocessed;

    /**
     * Cache or not for original document archive.
     */
    private boolean cacheOriginalDocument = false;

    /**
     * Custom data.
     */
    private Map<String, Object> data = null;

    private long lastModified;

    /**
     * Map of {@link BufferedElement} used for text styling to
     */
    private Map<String, BufferedElement> elementsCache;

    protected AbstractXDocReport()
    {
        // Register preprocessor.
        registerPreprocessors();
        this.preprocessed = false;
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.core.document.IXDocReport#getId()
     */
    public String getId()
    {
        return id;
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.core.document.IXDocReport#setId(java.lang.String )
     */
    public void setId( String id )
    {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.core.document.IXDocReport#load(java.io.InputStream )
     */
    public void load( InputStream sourceStream )
        throws IOException, XDocReportException
    {
        if ( preprocessed )
        {
            preprocessed = false;
        }
        // Load zipped XML Document (odt, docx...)
        setDocumentArchive( XDocArchive.readZip( sourceStream ) );
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.core.document.IXDocReport#setDocumentArchive (fr.opensagres
     * .xdocreport.core.document.XDocArchive)
     */
    public void setDocumentArchive( XDocArchive documentArchive )
        throws IOException, XDocReportException
    {
        this.lastModified = System.currentTimeMillis();
        this.preprocessed = false;
        if ( cacheOriginalDocument )
        {
            this.originalArchive = documentArchive;
            this.preprocessedArchive = documentArchive.createCopy();
        }
        else
        {
            this.originalArchive = null;
            this.preprocessedArchive = documentArchive;
        }
        // Execute preprocessor and modify the original zipped XML Document
        doPreprocessorIfNeeded();
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.IXDocReport#getOriginalDocumentArchive ()
     */
    public XDocArchive getOriginalDocumentArchive()
    {
        return originalArchive;
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.core.document.IXDocReport#getDocumentArchive()
     */
    public XDocArchive getPreprocessedDocumentArchive()
    {
        return preprocessedArchive;
    }

    /**
     * Returns template engine (velocity, freemarker..) to use to merge Java model with the XML files of the from
     * entries of the zipped XML Document (odt, docx...) defined by {@link AbstractXDocReport#getXMLEntries()}.
     * 
     * @return
     */
    public ITemplateEngine getTemplateEngine()
    {
        return templateEngine;
    }

    /**
     * Register template engine (velocity, freemarker..) to use to merge Java model with the XML files of the from
     * entries of the zipped XML Document (odt, docx...) defined by {@link AbstractXDocReport#getXMLEntries()}.
     */
    public void setTemplateEngine( ITemplateEngine templateEngine )
    {
        this.templateEngine = templateEngine;
    }

    /**
     * Register a processor for the entry name.
     * 
     * @param entryName
     * @param preprocessor
     */
    public void addPreprocessor( String entryName, IXDocPreprocessor preprocessor )
    {
        Collection<IXDocPreprocessor> entryPreprocessors = preprocessors.get( entryName );
        if ( entryPreprocessors == null )
        {
            entryPreprocessors = new ArrayList<IXDocPreprocessor>();
            preprocessors.put( entryName, entryPreprocessors );
        }
        entryPreprocessors.add( preprocessor );
    }

    /**
     * Remove processor for the entry name.
     * 
     * @param entryName
     */
    public void removePreprocessor( String entryName )
    {
        preprocessors.remove( entryName );
    }

    /**
     * Clear processor.
     */
    public void removeAllPreprocessors()
    {
        preprocessors.clear();
    }

    /**
     * Set fields metadata used to manage lazy loop for table row.
     */
    public void setFieldsMetadata( FieldsMetadata fieldsMetadata )
    {
        this.fieldsMetadata = fieldsMetadata;
        ITemplateEngine templateEngine = this.getTemplateEngine();
        if ( templateEngine != null && fieldsMetadata != null )
        {
            this.fieldsMetadata.setTemplateEngineKind( templateEngine.getKind() );
        }
    }

    /**
     * Returns fields metadata used to manage lazy loop for table row.
     * 
     * @return
     */
    public FieldsMetadata getFieldsMetadata()
    {
        return fieldsMetadata;
    }

    /**
     * Create fields metadata.
     * 
     * @return
     */
    public FieldsMetadata createFieldsMetadata()
    {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        setFieldsMetadata( fieldsMetadata );
        return fieldsMetadata;
    }

    public void preprocess()
        throws XDocReportException, IOException
    {
        doPreprocessorIfNeeded();
    }

    /**
     * Execute processors registered to modify entry names of the original document archive. Processors use
     * {@link FieldsMetadata} and {@link IDocumentFormatter} (coming from {@link ITemplateEngine#getDocumentFormatter()}
     * to manage lazy loop for table row.
     * 
     * @throws XDocReportException
     * @throws IOException
     */
    private void doPreprocessorIfNeeded()
        throws XDocReportException, IOException
    {
        if ( preprocessed )
        {
            // preprocessing is already done
            return;
        }
        if ( templateEngine == null )
        {
            // template engine is not set, so preprocessing cannot be done
            return;
        }
        Map<String, Object> sharedContext = new HashMap<String, Object>();
        if ( fieldsMetadata != null && fieldsMetadata.getFieldsAsTextStyling().size() > 0 )
        {
            elementsCache = new HashMap<String, BufferedElement>();
            sharedContext.put( DocumentContextHelper.ELEMENTS_KEY, elementsCache );
        }
        onBeforePreprocessing( sharedContext, preprocessedArchive );
        try
        {
            IDocumentFormatter formatter = internalGetTemplateEngine().getDocumentFormatter();
            
            // Loop for each preprocessor registered
            for ( Entry<String, Collection<IXDocPreprocessor>> entry : preprocessors.entrySet() )
            {
                String preprocessorName = entry.getKey();
                Collection<IXDocPreprocessor> entryPreprocessors = entry.getValue();
                if ( preprocessedArchive.hasEntry( preprocessorName ) )
                {
                    for ( IXDocPreprocessor preprocessor : entryPreprocessors )
                    {
                        // XML Document contains a XML file which must be
                        // preprocessed
                        preprocessor.preprocess( preprocessorName, preprocessedArchive, fieldsMetadata, formatter,
                                                 sharedContext );
                    }
                }
                else
                {
                    // Test if it's wilcard?
                    Set<String> entriesNameFromWilcard = preprocessedArchive.getEntryNames( preprocessorName );
                    if ( entriesNameFromWilcard.size() > 0 )
                    {
                        for ( String entryNameFromWilcard : entriesNameFromWilcard )
                        {
                            for ( IXDocPreprocessor preprocessor : entryPreprocessors )
                            {
                                preprocessor.preprocess( entryNameFromWilcard, preprocessedArchive, fieldsMetadata,

                                formatter, sharedContext );
                            }
                        }
                    }
                    else
                    {
                        // entry not found, create it?
                        entryPreprocessors = entry.getValue();
                        for ( IXDocPreprocessor preprocessor : entryPreprocessors )
                        {
                            if ( preprocessor.create( preprocessorName, preprocessedArchive, fieldsMetadata, formatter,
                                                      sharedContext ) )
                            {
                                break;
                            }
                        }
                    }
                }
            }
        }
        finally
        {
            onAfterPreprocessing( sharedContext, preprocessedArchive );
            // Preprocessing is done
            preprocessed = true;
            sharedContext.clear();
            sharedContext = null;
        }
    }

    /**
     * On before preprocessing.
     * 
     * @param sharedContext
     * @param preprocessedArchive
     */
    protected void onBeforePreprocessing( Map<String, Object> sharedContext, XDocArchive preprocessedArchive )
        throws XDocReportException
    {
        // Do nothing
    }

    /**
     * On after preprocessing.
     * 
     * @param sharedContext
     * @param preprocessedArchive
     */
    protected void onAfterPreprocessing( Map<String, Object> sharedContext, XDocArchive preprocessedArchive )
        throws XDocReportException
    {
        // Do nothing
    }

    /**
     * Returns XML entries which define XML document to merge with Java model with template engine.
     * 
     * @return
     */
    public String[] getXMLEntries()
    {
        return xmlEntries;
    }

    /**
     * Set XML entries which define XML document to merge with Java model with template engine.
     * 
     * @param xmlEntries
     */
    public void setXMLEntries( String[] xmlEntries )
    {
        this.xmlEntries = xmlEntries;
    }

    /**
     * Create an empty context to register Java model.
     */
    public IContext createContext()
        throws XDocReportException
    {
        return internalGetTemplateEngine().createContext();
    }

    //@Override
    public IContext createContext( Map<String, Object> contextMap )
        throws XDocReportException
    {
        return internalGetTemplateEngine().createContext( contextMap );
    }

    public void process( Map<String, Object> contextMap, OutputStream out )
        throws XDocReportException, IOException
    {
        process( contextMap, null, out );
    }

    public void process( Map<String, Object> contextMap, String entryName, OutputStream out )
        throws XDocReportException, IOException
    {
        process( createContext( contextMap ), entryName, out );
    }

    public void process( IContext context, OutputStream out )
        throws XDocReportException, IOException
    {
        process( context, null, out );
    }

    /**
     * Generate report by merging Java model frm the context with XML Document (odt, docx...) preprocessed and store the
     * result into output stream.
     */
    public void process( IContext context, String entryName, OutputStream out )
        throws XDocReportException, IOException
    {
        // 1) Start process report generation
        long startTime = -1;
        if ( LOGGER.isLoggable( Level.FINE ) )
        {
            startTime = System.currentTimeMillis();
            LOGGER.fine( "Start process report " );
        }
        XDocArchive outputArchive = null;
        try
        {
            // 2) Force getting the template engine (freemarker, velocity...)
            internalGetTemplateEngine();

            // 3) Execute preprocessors to modify original XML Document (odt,
            // docx..) only if preprocessing was not done.
            doPreprocessorIfNeeded();

            // 4) Copy original archive to returns
            outputArchive = internalGetDocumentArchive().createCopy();

            // 5) Loop for each entries (XML file from the zipped XML
            // document (odt, docx...)
            // to merge it with Java model from the context with template
            // engine (freemarker, velocity).
            processTemplateEngine( context, outputArchive );

            doPostprocessIfNeeded( outputArchive );

            if ( StringUtils.isNotEmpty( entryName ) )
            {
                if ( !outputArchive.hasEntry( entryName ) )
                {
                    throw new XDocReportException( "No entry for the entry name=" + entryName );
                }
                // 6) save the merged XML entry into ouput stream
                XDocArchive.writeEntry( outputArchive, entryName, out );
            }
            else
            {
                // 6) save the merged XML document archive into ouput stream
                XDocArchive.writeZip( outputArchive, out );
            }
            // 7) End process report generation
            if ( LOGGER.isLoggable( Level.FINE ) )
            {

                LOGGER.fine( "End process report done with " + ( System.currentTimeMillis() - startTime ) + "(ms)." );
            }

        }
        catch ( Throwable e )
        {
            // Error while report generation
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( "End process report with error done with " + ( System.currentTimeMillis() - startTime )
                    + "(ms)." );
                LOGGER.throwing( getClass().getName(), "process", e );
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
            if ( outputArchive != null )
            {
                outputArchive.dispose();
            }
            outputArchive = null;
        }
    }

    protected void doPostprocessIfNeeded( XDocArchive outputArchive )
    {
        // Empty default impl to avoid breaking compat
    }

    public void save( ProcessState processState, OutputStream out )
        throws IOException, XDocReportException
    {
        if ( processState == ProcessState.PREPROCESSED )
        {
            XDocArchive.writeZip( preprocessedArchive, out );
        }
        else
        {
            checkOriginalArchive();
            XDocArchive.writeZip( originalArchive, out );
        }

    }

    public void saveEntry( String entryName, ProcessState processState, OutputStream out )
        throws IOException, XDocReportException
    {
        if ( processState == ProcessState.PREPROCESSED )
        {
            XDocArchive.writeEntry( preprocessedArchive, entryName, out );
        }
        else
        {
            checkOriginalArchive();
            XDocArchive.writeEntry( originalArchive, entryName, out );
        }
    }

    private void checkOriginalArchive()
        throws XDocReportException
    {
        if ( originalArchive == null )
        {
            throw new XDocReportException(
                                           "Original document archive is not available. Call IXDocReport#setCacheOriginalDocument(true) before loading document." );
        }
    }

    public void extractFields( FieldsExtractor extractor )
        throws XDocReportException, IOException
    {
        extractFields( extractor, internalGetTemplateEngine() );
    }

    public void extractFields( FieldsExtractor extractor, ITemplateEngine templateEngine )
        throws XDocReportException, IOException
    {
        if ( templateEngine == null )
        {
            throw new XDocReportException( "ItemplateEngine cannot be null to extract fields." );
        }
        // force the preprocessing (see http://code.google.com/p/xdocreport/issues/detail?id=186)
        doPreprocessorIfNeeded();
        // Loop for each entries (XML file from the zipped XML
        // document (odt, docx...) to extract declared fields .
        String[] xmlEntries = internalGetXMLEntries();
        String entryName = null;
        for ( int i = 0; i < xmlEntries.length; i++ )
        {
            entryName = xmlEntries[i];
            if ( preprocessedArchive.hasEntry( entryName ) )
            {
                // extract fields
                templateEngine.extractFields( preprocessedArchive, entryName, extractor );
            }
            else
            {
                // Test if it's wilcard?
                Set<String> entriesNameFromWilcard = preprocessedArchive.getEntryNames( entryName );
                for ( String entryNameFromWilcard : entriesNameFromWilcard )
                {
                    // extract fields
                    templateEngine.extractFields( preprocessedArchive, entryNameFromWilcard, extractor );
                }
            }
        }
    }

    public IConverter getConverter( Options options )
        throws XDocConverterException
    {
        return ConverterRegistry.getRegistry().findConverter( getKind(), options.getTo(), options.getVia() );
    }

    public void convert( Map<String, Object> contextMap, Options options, OutputStream out )
        throws XDocReportException, XDocConverterException, IOException
    {
        convert( createContext( contextMap ), options, out );
    }

    public void convert( final IContext context, Options options, OutputStream out )
        throws XDocReportException, XDocConverterException, IOException
    {
        // 1) Start process report generation
        long startTime = -1;
        if ( LOGGER.isLoggable( Level.FINE ) )
        {
            // Debug start process
            startTime = System.currentTimeMillis();
            LOGGER.fine( "Start convert report " );
        }
        XDocArchive outputArchive = null;
        try
        {
            IConverter converter = getConverter( options );
            // 2) Execute preprocessors to modify original XML Document
            // (odt,
            // docx..) only if preprocessing was not done.
            doPreprocessorIfNeeded();

            // 4) Copy original arhvive to returns
            outputArchive = internalGetDocumentArchive().createCopy();

            // 5) Loop for each entries (XML file from the zipped XML
            // document (odt, docx...)
            // to merge it with Java model from the context with template
            // engine (freemarker, velocity).
            processTemplateEngine( context, outputArchive );

            if ( converter.canSupportEntries() )
            {
                converter.convert( outputArchive, out, options );
            }
            else
            {
                // Converter cannot supper input entries provider,
                // rebuild a zip and set it as input stream.
                converter.convert( XDocArchive.getInputStream( outputArchive ), out, options );
            }

            // 7) End process report generation
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( "End convert report done with " + ( System.currentTimeMillis() - startTime )
                    + "(ms)." );
            }
        }
        catch ( Throwable e )
        {
            // Error while report generation
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( "End convert report with error done with " + ( System.currentTimeMillis() - startTime )
                    + "(ms)." );
                LOGGER.throwing( getClass().getName(), "convert", e );
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
            if ( e instanceof XDocConverterException )
            {
                throw (XDocConverterException) e;
            }
            throw new XDocConverterException( e );

        }
        finally
        {
            if ( outputArchive != null )
            {
                outputArchive.dispose();
            }
            outputArchive = null;
        }
    }

    private void processTemplateEngine( final IContext context, XDocArchive outputArchive )
        throws XDocReportException, IOException
    {
        String[] xmlEntries = internalGetXMLEntries();

        onBeforeProcessTemplateEngine( context, outputArchive );
        String entryName = null;
        for ( int i = 0; i < xmlEntries.length; i++ )
        {
            entryName = xmlEntries[i];
            if ( outputArchive.hasEntry( entryName ) )
            {
                // 5.1) merge current XML file with Java model from the
                // context with template engine (freemarker, velocity).
                templateEngine.process( getId(), entryName, outputArchive, outputArchive, context );
            }
            else
            {
                // Test if it's wilcard?
                Set<String> entriesNameFromWilcard = internalGetDocumentArchive().getEntryNames( entryName );
                for ( String entryNameFromWilcard : entriesNameFromWilcard )
                {
                    // 5.2) merge current XML file with Java model from the
                    // context with template engine (freemarker, velocity).
                    templateEngine.process( getId(), entryNameFromWilcard, outputArchive, outputArchive, context );
                }
            }
        }
        onAfterProcessTemplateEngine( context, outputArchive );
    }

    /**
     * On before process template engine.
     * 
     * @param context
     * @param outputArchive
     * @throws XDocReportException
     */
    protected void onBeforeProcessTemplateEngine( final IContext context, XDocArchive outputArchive )
        throws XDocReportException
    {

        // 1) Register text styling registry
        DocumentContextHelper.putTextStylingRegistry( context, TextStylingRegistry.getRegistry() );

        // 2) Register ImageRegistry if needed
        IImageRegistry imageRegistry = null;
        if ( fieldsMetadata != null && fieldsMetadata.hasFieldsAsImage() )
        {
            imageRegistry = createImageRegistry( outputArchive, outputArchive, outputArchive );
            if ( imageRegistry != null )
            {
                DocumentContextHelper.putImageRegistry( context, imageRegistry );

                // Register default image handler if needed.
                if ( DocumentContextHelper.getImageHandler( context ) == null )
                {
                    DocumentContextHelper.putImageHandler( context, DefaultImageHandler.getInstance() );
                }
                imageRegistry.preProcess();
            }
        }

        // 3) Register context
        TemplateContextHelper.putContext( context );

        // 4) Add Bufferered element cache used for text styling
        if ( elementsCache != null )
        {
            DocumentContextHelper.putElementsCache( context, elementsCache );
        }

        // 5) Register template engine
        TemplateContextHelper.putTemplateEngine( context, templateEngine );
    }

    /**
     * On after process template engine.
     * 
     * @param context
     * @param outputArchive
     * @throws XDocReportException
     */
    protected void onAfterProcessTemplateEngine( IContext context, XDocArchive outputArchive )
        throws XDocReportException
    {
        IImageRegistry imageRegistry = DocumentContextHelper.getImageRegistry( context );
        if ( imageRegistry != null )
        {
            imageRegistry.postProcess();
        }
    }

    /**
     * Returns template engine (freemarker, velocity...) to use for the report generation and throws
     * {@link XDocReportException} if template engien is not defined.
     * 
     * @return
     * @throws XDocReportException
     */
    private ITemplateEngine internalGetTemplateEngine()
        throws XDocReportException
    {
        if ( templateEngine == null )
        {
            throw new XDocReportException(
                                           "Null template engine. Set template engine with IXDocReport#setTemplateEngine." );
        }
        return templateEngine;
    }

    /**
     * Retruns zipped XML document (odt, docx...) and throws {@link XDocReportException} if it is not defined.
     * 
     * @return
     * @throws XDocReportException
     */
    private XDocArchive internalGetDocumentArchive()
        throws XDocReportException
    {
        if ( preprocessedArchive == null )
        {
            throw new XDocReportException(
                                           "Null document archive (odt, docx). Load document archive (docx, odt....file) with IXDocReport#load." );
        }
        return preprocessedArchive;
    }

    /**
     * Returns XML entries which define XML document to merge with Java model with template engine. If custom XML
     * entries is not defined, {@link AbstractXDocReport#getDefaultXMLEntries()} are used.
     * 
     * @return
     */
    private String[] internalGetXMLEntries()
    {
        String[] entries = getXMLEntries();
        if ( entries != null )
        {
            return entries;
        }

        entries = getDefaultXMLEntries();
        if ( entries != null )
        {
            return entries;
        }
        return EMPTY_STRING_ARRAY;
    }

    /**
     * Returns default entries which define XML document to merge with Java model with template engine for odt, docx...
     * 
     * @return
     */
    protected abstract String[] getDefaultXMLEntries();

    /**
     * Returns XML entries which define XML document to merge with Java model with template engine. If custom XML
     * entries is not defined, {@link AbstractXDocReport#getDefaultXMLEntries()} are used.
     * 
     * @return
     */
    // private String[] internalGetXMLEntriesForConversion() {
    // String[] entries = getDefaultXMLEntriesForConversion();
    // if (entries != null) {
    // return entries;
    // }
    // return EMPTY_STRING_ARRAY;
    // }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.core.document.IXDocReport#setData(java.lang. String, java.lang.Object)
     */
    public void setData( String key, Object value )
    {
        if ( data == null )
        {
            data = new HashMap<String, Object>();
        }
        data.put( key, value );
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.core.document.IXDocReport#getData(java.lang. String)
     */
    @SuppressWarnings( "unchecked" )
    public <T> T getData( String key )
    {
        if ( data == null )
        {
            return null;
        }
        return (T) data.get( key );
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.core.document.IXDocReport#clearData(java.lang .String)
     */
    public void clearData( String key )
    {
        if ( data == null )
        {
            return;
        }
        data.remove( key );
    }

    public void setCacheOriginalDocument( boolean cacheOriginalDocument )
    {
        this.cacheOriginalDocument = cacheOriginalDocument;
        if ( cacheOriginalDocument )
        {
            if ( originalArchive == null && preprocessedArchive != null )
            {
                originalArchive = preprocessedArchive.createCopy();
            }
        }
        else
        {
            originalArchive = null;
        }
    }

    /**
     * Returns default entries which define XML document to use for conversion.
     * 
     * @return
     */
    // protected abstract String[] getDefaultXMLEntriesForConversion();

    /**
     * Register preprocessors.
     */
    protected abstract void registerPreprocessors();

    /**
     * Returns true if report was processed and false otherwise.
     * 
     * @return
     */
    public boolean isPreprocessed()
    {
        return preprocessed;
    }

    /**
     * Returns the last modified time when the report is loaded.
     * 
     * @return
     */
    public long getLastModified()
    {
        return lastModified;
    }

    /**
     * Create an image registry.
     * 
     * @return
     */
    protected abstract IImageRegistry createImageRegistry( IEntryReaderProvider readerProvider,
                                                           IEntryWriterProvider writerProvider,
                                                           IEntryOutputStreamProvider outputStreamProvider );

    public void dump( IContext context, DumperOptions options, OutputStream out )
        throws IOException, XDocReportException
    {
        XDocArchive archive = getOriginalDocumentArchive();
        if ( archive == null )
        {
            throw new XDocReportException(
                                           "Dump cannot be done. IXDocReport#getOriginalDocumentArchive is null. Please call IXDocReport#setCacheOriginalDocument(true) after the loads of the report." );
        }
        dump( context, XDocArchive.getInputStream( archive ), options, out );
    }

    public void dump( IContext context, InputStream documentIn, DumperOptions options, OutputStream out )
        throws IOException, XDocReportException
    {
        if ( documentIn == null )
        {
            throw new XDocConverterException( "Dump cannot be done. Dump fo the report input stream cannot be null." );
        }
        getDumper( options ).dump( this, documentIn, context, options, out );

    }

    public IDumper getDumper( DumperOptions options )
        throws XDocReportException
    {
        return DumperRegistry.getRegistry().findDumper( options.getKind() );
    }
}
