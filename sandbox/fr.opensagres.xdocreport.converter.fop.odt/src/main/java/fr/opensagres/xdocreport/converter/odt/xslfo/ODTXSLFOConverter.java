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
package fr.opensagres.xdocreport.converter.odt.xslfo;

import java.io.InputStream;

import javax.xml.transform.URIResolver;

import fr.opensagres.xdocreport.converter.internal.xslfo.AbstractXSLFOConverter;

public class ODTXSLFOConverter
    extends AbstractXSLFOConverter
    implements ODTXSLFOConstants
{

    private static ODTXSLFOConverter INSTANCE = new ODTXSLFOConverter();

    public static ODTXSLFOConverter getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected InputStream getXSLFOStream()
    {
        return XSLFOURIResolver.class.getResourceAsStream( getXSLFOTemplateURI() );
    }

    @Override
    protected String getXSLFOTemplateURI()
    {
        return XSL_FO_TEMPLATE_URI;
    }

    @Override
    protected URIResolver getXSLFOURIResolver()
    {
        return XSLFOURIResolver.INSTANCE;
    }

    @Override
    protected String getMainEntryName()
    {
        return CONTENT_XML_ENTRY;
    }

    @Override
    protected String[] getEntryNames()
    {
        return XML_ENTRIES;
    }
}
