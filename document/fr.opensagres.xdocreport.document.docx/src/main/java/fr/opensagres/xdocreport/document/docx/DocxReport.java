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
package fr.opensagres.xdocreport.document.docx;

import static fr.opensagres.xdocreport.document.docx.DocxConstants.CONTENT_TYPES_XML_ENTRY;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.MIME_MAPPING;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.WORD_DOCUMENT_XML_ENTRY;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.WORD_FOOTER_XML_ENTRY;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.WORD_HEADER_XML_ENTRY;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.WORD_NUMBERING_XML_ENTRY;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.WORD_RELS_XMLRELS_XML_ENTRY;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.WORD_STYLES_XML_ENTRY;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.io.IEntryOutputStreamProvider;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.AbstractXDocReport;
import fr.opensagres.xdocreport.document.docx.images.DocxImageRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocxPreprocessor;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkContentHandler;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.InitialHyperlinkMap;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.contenttypes.DocxContentTypesPreprocessor;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering.DocxNumberingPreprocessor;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.rels.DocxDocumentXMLRelsPreprocessor;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.styles.DocxStylesPreprocessor;
import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.document.images.IImageRegistry;
import fr.opensagres.xdocreport.template.IContext;

/**
 * MS Word DOCX report.
 */
public class DocxReport
    extends AbstractXDocReport
{

    private static final long serialVersionUID = -2323716817951928168L;

    private static final String[] DEFAULT_XML_ENTRIES = { WORD_DOCUMENT_XML_ENTRY, WORD_STYLES_XML_ENTRY,
        WORD_HEADER_XML_ENTRY, WORD_FOOTER_XML_ENTRY, WORD_RELS_XMLRELS_XML_ENTRY, WORD_NUMBERING_XML_ENTRY };

    private Set<String> allEntryNamesHyperlinks;

    private Set<String> modifiedEntryNamesHyperlinks;

    private DefaultStyle defaultStyle;

    public DocxReport()
    {
        this.defaultStyle = new DefaultStyle();
    }

    public String getKind()
    {
        return DocumentKind.DOCX.name();
    }

    @Override
    protected void registerPreprocessors()
    {
        super.addPreprocessor( WORD_STYLES_XML_ENTRY, DocxStylesPreprocessor.INSTANCE );
        // super.addPreprocessor( WORD_DOCUMENT_XML_ENTRY, DocxDocumentPreprocessor.INSTANCE );
        // super.addPreprocessor( WORD_HEADER_XML_ENTRY, DocxDocumentPreprocessor.INSTANCE );
        // super.addPreprocessor( WORD_FOOTER_XML_ENTRY, DocxDocumentPreprocessor.INSTANCE );
        super.addPreprocessor( WORD_DOCUMENT_XML_ENTRY, DocxPreprocessor.INSTANCE );
        super.addPreprocessor( WORD_HEADER_XML_ENTRY, DocxPreprocessor.INSTANCE );
        super.addPreprocessor( WORD_FOOTER_XML_ENTRY, DocxPreprocessor.INSTANCE );
        super.addPreprocessor( CONTENT_TYPES_XML_ENTRY, DocxContentTypesPreprocessor.INSTANCE );
        super.addPreprocessor( WORD_RELS_XMLRELS_XML_ENTRY, DocxDocumentXMLRelsPreprocessor.INSTANCE );
        super.addPreprocessor( WORD_NUMBERING_XML_ENTRY, DocxNumberingPreprocessor.INSTANCE );
    }

    @Override
    protected String[] getDefaultXMLEntries()
    {
        return DEFAULT_XML_ENTRIES;
    }

    public MimeMapping getMimeMapping()
    {
        return MIME_MAPPING;
    }

    @Override
    protected IImageRegistry createImageRegistry( IEntryReaderProvider readerProvider,
                                                  IEntryWriterProvider writerProvider,
                                                  IEntryOutputStreamProvider outputStreamProvider )
    {
        return new DocxImageRegistry( readerProvider, writerProvider, outputStreamProvider );
    }

    @Override
    protected void onBeforePreprocessing( Map<String, Object> sharedContext, XDocArchive preprocessedArchive )
        throws XDocReportException
    {
        super.onBeforePreprocessing( sharedContext, preprocessedArchive );
        // Before starting preprocessing, Hyperlink must be getted from
        // the whole entries (*.xml.rels like "word/_rels/document.xml.rels") in
        // the shared
        // context.
        Set<String> xmlRelsEntryNames = preprocessedArchive.getEntryNames( WORD_RELS_XMLRELS_XML_ENTRY );
        this.allEntryNamesHyperlinks = new HashSet<String>();
        String entryName = null;
        // Loop for each entries *.xml.rels
        for ( String relsEntryName : xmlRelsEntryNames )
        {
            HyperlinkContentHandler contentHandler = new HyperlinkContentHandler();
            Reader reader = preprocessedArchive.getEntryReader( relsEntryName );
            try
            {
                XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                xmlReader.setContentHandler( contentHandler );
                xmlReader.parse( new InputSource( reader ) );
                if ( contentHandler.getHyperlinks() != null )
                {
                    // Current *.xml.rels document has hyperlinks, store it in
                    // the
                    // sharedContext with the key *.xml (ex : if the current
                    // entry is "word/_rels/document.xml.rels", key used will be
                    // * "word/document.xml").
                    entryName = HyperlinkUtils.getEntryNameWithoutRels( relsEntryName );
                    HyperlinkUtils.putInitialHyperlinkMap( entryName, sharedContext, contentHandler.getHyperlinks() );
                    allEntryNamesHyperlinks.add( entryName );
                }
            }
            catch ( SAXException e )
            {
                throw new XDocReportException( e );
            }
            catch ( IOException e )
            {
                throw new XDocReportException( e );
            }
        }
        sharedContext.put( DocxContextHelper.DEFAULT_STYLE_KEY, defaultStyle );
    }

    @Override
    protected void onAfterPreprocessing( Map<String, Object> sharedContext, XDocArchive preprocessedArchive )
        throws XDocReportException
    {
        super.onAfterPreprocessing( sharedContext, preprocessedArchive );
        // Compute if the docx has dynamic hyperlink
        if ( sharedContext != null )
        {
            InitialHyperlinkMap hyperlinkMap = null;
            modifiedEntryNamesHyperlinks = new HashSet<String>();
            for ( String entryName : allEntryNamesHyperlinks )
            {
                hyperlinkMap = HyperlinkUtils.getInitialHyperlinkMap( entryName, sharedContext );
                if ( hyperlinkMap != null && hyperlinkMap.isModified() )
                {
                    modifiedEntryNamesHyperlinks.add( entryName );
                }
            }
        }
    }

    @Override
    protected void onBeforeProcessTemplateEngine( IContext context, XDocArchive outputArchive )
        throws XDocReportException
    {
        // 1) Register commons Java model in the context
        super.onBeforeProcessTemplateEngine( context, outputArchive );
        // 2) Register for each entries (word/document.xml, ... which definies hyperlink a hyperlink registry.
        for ( String entryName : modifiedEntryNamesHyperlinks )
        {
            // docx has dynamic hyperlink, put an instance of HyperlinkRegistry
            // in the context.
            DocxContextHelper.putHyperlinkRegistry( context, entryName, new HyperlinkRegistry() );
        }
        // 3) Register default style instance
        DocxContextHelper.putDefaultStyle( context, defaultStyle );
        // 4) Register styles generator if not exists.
        DocxContextHelper.getStylesGenerator( context );
    }

    @Override
    protected void onAfterProcessTemplateEngine( IContext context, XDocArchive outputArchive )
        throws XDocReportException
    {
        super.onAfterProcessTemplateEngine( context, outputArchive );
    }
}
