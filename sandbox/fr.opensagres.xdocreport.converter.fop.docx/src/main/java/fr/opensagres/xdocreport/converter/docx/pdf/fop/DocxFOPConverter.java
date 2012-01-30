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
package fr.opensagres.xdocreport.converter.docx.pdf.fop;

import java.io.InputStream;

import javax.xml.transform.URIResolver;

import fr.opensagres.xdocreport.converter.docx.xslfo.DocxXSLFOConstants;
import fr.opensagres.xdocreport.converter.docx.xslfo.XSLFOURIResolver;
import fr.opensagres.xdocreport.converter.internal.pdf.fop.AbstractFOPConverter;

public class DocxFOPConverter
    extends AbstractFOPConverter
    implements DocxXSLFOConstants
{

    private static final DocxFOPConverter INSTANCE = new DocxFOPConverter();

    public static DocxFOPConverter getInstance()
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
        return WORD_DOCUMENT_XML_ENTRY;
    }

    @Override
    protected String[] getEntryNames()
    {
        return XML_ENTRIES;
    }
}
