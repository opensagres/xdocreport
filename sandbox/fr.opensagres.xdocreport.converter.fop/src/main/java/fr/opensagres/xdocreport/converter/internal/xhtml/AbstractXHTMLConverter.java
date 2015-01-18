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
