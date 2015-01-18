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
package fr.opensagres.xdocreport.converter.internal.xslt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.converter.internal.AbstractConverterEntriesSupport;
import fr.opensagres.xdocreport.core.io.IEntryInputStreamProvider;
import fr.opensagres.xdocreport.core.io.XDocArchive;

/**
 * Abstract converter for which use XSLT.
 */
public abstract class AbstractXSLTConverter
    extends AbstractConverterEntriesSupport
{

    public void convert( IEntryInputStreamProvider inProvider, OutputStream out, Options options )
        throws XDocConverterException
    {
        convert( inProvider, new StreamResult( out ), options );
    }

    public void convert( IEntryInputStreamProvider inProvider, Result result, Options options )
        throws XDocConverterException
    {
        try
        {
            // 1) Get XML source
            Source source = getXMLSource( inProvider );

            // 2) Get XSL Templates
            Templates templates = getXSLTemplates();

            // 3) Transform XML source with XSL Templates
            if ( templates != null )
            {
                Transformer transformer = templates.newTransformer();
                URIResolver resolver = createURIResolver( inProvider );
                if ( resolver != null )
                {
                    transformer.setURIResolver( resolver );
                }
                transformer.transform( source, result );
            }
        }
        catch ( Exception e )
        {
            throw new XDocConverterException( e );
        }
    }

    public void convert( InputStream in, Result result, Options options )
        throws XDocConverterException
    {
        try
        {
            convert( XDocArchive.readZip( in ), result, options );
        }
        catch ( IOException e )
        {
            throw new XDocConverterException( e );
        }
    }

    // public void convert(InputStream in, OutputStream out,
    // Options options) throws XDocConverterException {
    // convert(in, new StreamResult(out), options);
    // }

    protected XMLEntriesURIResolver createURIResolver( IEntryInputStreamProvider inProvider )
    {
        return new XMLEntriesURIResolver( inProvider );
    }

    private Source getXMLSource( IEntryInputStreamProvider provider )
        throws XDocConverterException
    {
        InputStream in = provider.getEntryInputStream( getMainEntryName() );
        if ( in == null )
        {
            throw new XDocConverterException( "Cannot find input stream for entry=" + getMainEntryName() );
        }
        return new StreamSource( in );
    }

    private Templates getXSLTemplates()
        throws TransformerConfigurationException
    {
        String uri = getXSLTemplateURI();
        Templates templates = XSLTemplatesRegistry.getRegistry().getTemplates( uri );
        if ( templates == null )
        {
            templates = XSLTemplatesRegistry.getRegistry().loadTemplates( uri, getXSLTStream(), getXSLTURIResolver() );
        }
        return templates;
    }

    protected abstract String getXSLTemplateURI();

    protected abstract InputStream getXSLTStream();

    protected abstract URIResolver getXSLTURIResolver();

    protected abstract String getMainEntryName();

    protected abstract String[] getEntryNames();

}
