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
package fr.opensagres.xdocreport.converter.internal.xhtml;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.MimeMappingConstants;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.converter.internal.xslt.AbstractXSLTConverter;
import fr.opensagres.xdocreport.core.io.IEntryInputStreamProvider;

/**
 * Abstract converter for XHTML which use XSLT.
 */
public abstract class AbstractXHTMLConverter
    extends AbstractXSLTConverter
    implements IXHTMLConverter, MimeMappingConstants
{

    public void convert( IEntryInputStreamProvider inProvider, OutputStream out, Options options )
        throws XDocConverterException
    {
        convert2XHTML( inProvider, out, options );
    }

    public void convert2XHTML( IEntryInputStreamProvider inProvider, OutputStream out, Options options )
        throws XDocConverterException
    {
        super.convert( inProvider, new StreamResult( out ), options );
    }

    public void convert2XHTML( IEntryInputStreamProvider inProvider, Result result, Options options )
        throws XDocConverterException
    {
        super.convert( inProvider, result, options );
    }

    @Override
    protected String getXSLTemplateURI()
    {
        return getXHTMLTemplateURI();
    }

    @Override
    protected InputStream getXSLTStream()
    {
        return getXHTMLStream();
    }

    @Override
    protected URIResolver getXSLTURIResolver()
    {
        return getXHTMLURIResolver();
    }

    public MimeMapping getMimeMapping()
    {
        return XHTML_MIME_MAPPING;
    }

    protected abstract String getXHTMLTemplateURI();

    protected abstract InputStream getXHTMLStream();

    protected abstract URIResolver getXHTMLURIResolver();
}
