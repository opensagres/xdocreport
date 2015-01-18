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

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

/**
 * XSLT {@link Templates} registry which is used to compute XSLT and cache it.
 */
public class XSLTemplatesRegistry
{

    private static final XSLTemplatesRegistry INSTANCE = new XSLTemplatesRegistry();

    private Map<String, Templates> cachedTemplates = new HashMap<String, Templates>();

    private ITransformerFactory transformerFactory = DefaultTransformerFactory.getInstance();

    private XSLTemplatesRegistry()
    {

    }

    public static XSLTemplatesRegistry getRegistry()
    {
        return INSTANCE;
    }

    public Templates getTemplates( String uri )
    {
        return cachedTemplates.get( uri );
    }

    public Templates loadTemplates( String uri, Reader reader, URIResolver resolver )
        throws TransformerConfigurationException
    {
        return loadTemplates( uri, new StreamSource( reader ), resolver );
    }

    public Templates loadTemplates( String uri, InputStream stream, URIResolver resolver )
        throws TransformerConfigurationException
    {
        return loadTemplates( uri, new StreamSource( stream ), resolver );
    }

    public synchronized Templates loadTemplates( String uri, Source source, URIResolver resolver )
        throws TransformerConfigurationException
    {
        Templates templates = getTemplates( uri );
        if ( templates != null )
        {
            return templates;
        }
        TransformerFactory factory = getTransformerFactory().createTransformerFactory();
        if ( resolver != null )
        {
            factory.setURIResolver( resolver );
        }
        templates = factory.newTemplates( source );
        cachedTemplates.put( uri, templates );
        return templates;
    }

    public void setTransformerFactory( ITransformerFactory transformerFactory )
    {
        this.transformerFactory = transformerFactory;
    }

    public ITransformerFactory getTransformerFactory()
    {
        return transformerFactory;
    }

    public void registerTemplates( String uri, Templates templates )
    {
        cachedTemplates.put( uri, templates );
    }
}
