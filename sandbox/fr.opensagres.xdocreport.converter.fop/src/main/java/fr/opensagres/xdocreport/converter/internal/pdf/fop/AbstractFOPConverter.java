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
package fr.opensagres.xdocreport.converter.internal.pdf.fop;

import java.io.OutputStream;

import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.converter.internal.xslfo.AbstractXSLFOConverter;
import fr.opensagres.xdocreport.core.io.IEntryInputStreamProvider;

/**
 * Abstract converter for FOP which use XSLT to generate FO.
 */
public abstract class AbstractFOPConverter
    extends AbstractXSLFOConverter
{

    @Override
    public void convert( IEntryInputStreamProvider inProvider, OutputStream outputStream, Options options )
        throws XDocConverterException
    {
        try
        {
            FopFactory fopFactory = FopFactory.newInstance();
            Fop fop = fopFactory.newFop( MimeConstants.MIME_PDF, outputStream );
            URIResolver fopResolver = createURIResolver( inProvider );
            fop.getUserAgent().setURIResolver( fopResolver );
            convert2FO( inProvider, new SAXResult( fop.getDefaultHandler() ), options );
        }
        catch ( FOPException e )
        {
            throw new XDocConverterException( e );
        }

    }

    public void convert( IEntryInputStreamProvider inProvider, OutputStream outputStream, OutputStream outForFO,
                         Options options )
        throws XDocConverterException
    {
        try
        {
            if ( outForFO != null )
            {
                convert2FO( inProvider, new StreamResult( outForFO ), options );
            }
            FopFactory fopFactory = FopFactory.newInstance();
            Fop fop = fopFactory.newFop( MimeConstants.MIME_PDF, outputStream );
            URIResolver fopResolver = createURIResolver( inProvider );
            fop.getUserAgent().setURIResolver( fopResolver );
            convert2FO( inProvider, new SAXResult( fop.getDefaultHandler() ), options );
        }
        catch ( FOPException e )
        {
            throw new XDocConverterException( e );
        }

    }

    @Override
    public MimeMapping getMimeMapping()
    {
        return PDF_MIME_MAPPING;
    }

}
