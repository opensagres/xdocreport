/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com>, Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
