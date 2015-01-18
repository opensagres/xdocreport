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
package fr.opensagres.xdocreport.document.odt;

import static fr.opensagres.xdocreport.document.odt.ODTConstants.CONTENT_XML_ENTRY;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.METAINF_MANIFEST_XML_ENTRY;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.MIME_MAPPING;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.STYLES_XML_ENTRY;

import java.util.Map;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.io.IEntryOutputStreamProvider;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.AbstractXDocReport;
import fr.opensagres.xdocreport.document.images.IImageRegistry;
import fr.opensagres.xdocreport.document.odt.images.ODTImageRegistry;
import fr.opensagres.xdocreport.document.odt.preprocessor.ODTManifestXMLProcessor;
import fr.opensagres.xdocreport.document.odt.preprocessor.ODTPreprocessor;
import fr.opensagres.xdocreport.document.odt.preprocessor.ODTStylesPreprocessor;
import fr.opensagres.xdocreport.document.odt.template.ODTContextHelper;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTDefaultStyle;
import fr.opensagres.xdocreport.template.IContext;

/**
 * Open Office ODT report. For mime mapping please see {@see
 * http://framework.openoffice.org/documentation/mimetypes/mimetypes.html}.
 */
public class ODTReport
    extends AbstractXDocReport
{

    private static final long serialVersionUID = 5974669564624835649L;

    private static final String[] DEFAULT_XML_ENTRIES = { CONTENT_XML_ENTRY, STYLES_XML_ENTRY,
        METAINF_MANIFEST_XML_ENTRY };

    private ODTDefaultStyle defaultStyle;

    public ODTReport()
    {
        this.defaultStyle = new ODTDefaultStyle();
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.IXDocReport#getKind()
     */
    public String getKind()
    {
        return DocumentKind.ODT.name();
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.IXDocReport#getMimeMapping()
     */
    public MimeMapping getMimeMapping()
    {
        return MIME_MAPPING;
    }

    @Override
    protected void registerPreprocessors()
    {
        // processor to modify content.xml
        super.addPreprocessor( CONTENT_XML_ENTRY, ODTPreprocessor.INSTANCE );
        // processor to modify META-INF/manifest.xml
        super.addPreprocessor( METAINF_MANIFEST_XML_ENTRY, ODTManifestXMLProcessor.INSTANCE );
        // processor to modify global styles
        super.addPreprocessor( STYLES_XML_ENTRY, ODTStylesPreprocessor.INSTANCE );
    }

    @Override
    protected void onBeforePreprocessing( Map<String, Object> sharedContext, XDocArchive preprocessedArchive )
        throws XDocReportException
    {
        super.onBeforePreprocessing( sharedContext, preprocessedArchive );
        // Default style
        sharedContext.put( ODTContextHelper.DEFAULT_STYLE_KEY, defaultStyle );
    }

    @Override
    protected void onBeforeProcessTemplateEngine( IContext context, XDocArchive outputArchive )
        throws XDocReportException
    {
        // 1) Register commons Java model in the context
        super.onBeforeProcessTemplateEngine( context, outputArchive );
        // 2) Register default style instance
        ODTContextHelper.putDefaultStyle( context, defaultStyle );
        // 3) Register styles generator if not exists.
        ODTContextHelper.getStylesGenerator( context ); 

    }

    @Override
    protected String[] getDefaultXMLEntries()
    {
        return DEFAULT_XML_ENTRIES;
    }

    @Override
    protected IImageRegistry createImageRegistry( IEntryReaderProvider readerProvider,
                                                  IEntryWriterProvider writerProvider,
                                                  IEntryOutputStreamProvider outputStreamProvider )
    {
        return new ODTImageRegistry( readerProvider, writerProvider, outputStreamProvider, getFieldsMetadata() );
    }
}
