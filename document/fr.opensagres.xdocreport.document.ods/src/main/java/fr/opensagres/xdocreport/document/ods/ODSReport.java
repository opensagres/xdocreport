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
package fr.opensagres.xdocreport.document.ods;

import java.io.IOException;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.io.IEntryOutputStreamProvider;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.AbstractXDocReport;
import fr.opensagres.xdocreport.document.images.IImageRegistry;
import fr.opensagres.xdocreport.document.ods.images.ODSImageRegistry;

/**
 * Open Office ODS report.
 */
public class ODSReport
    extends AbstractXDocReport
    implements ODSConstants
{

    private static final long serialVersionUID = 2483470391381344529L;

    private static final String[] DEFAULT_XML_ENTRIES = { CONTENT_XML_ENTRY, STYLES_XML_ENTRY };

    public String getKind()
    {
        return DocumentKind.ODS.name();
    }

    @Override
    protected void registerPreprocessors()
    {

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

    public static boolean isODS( XDocArchive documentArchive )
    {
        try
        {
            if ( !documentArchive.hasEntry( MIMETYPE ) )
            {
                return false;
            }
            return ODS_MIMETYPE.equals( IOUtils.toString( documentArchive.getEntryReader( MIMETYPE ) ) );
        }
        catch ( IOException e )
        {
        }
        return false;
    }

    @Override
    protected IImageRegistry createImageRegistry( IEntryReaderProvider readerProvider,
                                                  IEntryWriterProvider writerProvider,
                                                  IEntryOutputStreamProvider outputStreamProvider )
    {
        return new ODSImageRegistry( readerProvider, writerProvider, outputStreamProvider, getFieldsMetadata() );
    }
}
