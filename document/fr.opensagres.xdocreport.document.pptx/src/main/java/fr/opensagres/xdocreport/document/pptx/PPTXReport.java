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
package fr.opensagres.xdocreport.document.pptx;

import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.CONTENT_TYPES_XML;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.MIME_MAPPING;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.PRESENTATION_PRESENTATION_XML_ENTRY;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.PRESENTATION_SLIDES_XML_ENTRY;
import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.io.IEntryOutputStreamProvider;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.AbstractXDocReport;
import fr.opensagres.xdocreport.document.images.IImageRegistry;
import fr.opensagres.xdocreport.document.pptx.images.PPTXImageRegistry;
import fr.opensagres.xdocreport.document.pptx.preprocessor.PPTXSlidePreprocessor;

/**
 * Open Office ODS report.
 */
public class PPTXReport
    extends AbstractXDocReport
{

    private static final String PPT_REGEXP = "ppt*";

    private static final long serialVersionUID = -8323654563409226895L;

    private static final String[] DEFAULT_XML_ENTRIES = { PRESENTATION_PRESENTATION_XML_ENTRY,
        PRESENTATION_SLIDES_XML_ENTRY };

    public String getKind()
    {
        return DocumentKind.PPTX.name();
    }

    @Override
    protected void registerPreprocessors()
    {
        super.addPreprocessor( PRESENTATION_SLIDES_XML_ENTRY, PPTXSlidePreprocessor.INSTANCE );
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

    public static boolean isPPTX( XDocArchive documentArchive )
    {
        if ( !documentArchive.hasEntry( CONTENT_TYPES_XML ) )
        {
            return false;
        }
        // <Override PartName="/ppt/presentation.xml"
        // ContentType="application/vnd.openxmlformats-officedocument.presentationml.presentation.main+xml"
        // />
        return documentArchive.getEntryNames( PPT_REGEXP ).size() > 0;
    }

    @Override
    protected IImageRegistry createImageRegistry( IEntryReaderProvider readerProvider,
                                                  IEntryWriterProvider writerProvider,
                                                  IEntryOutputStreamProvider outputStreamProvider )
    {
        return new PPTXImageRegistry( readerProvider, writerProvider, outputStreamProvider, getFieldsMetadata() );
    }
}
