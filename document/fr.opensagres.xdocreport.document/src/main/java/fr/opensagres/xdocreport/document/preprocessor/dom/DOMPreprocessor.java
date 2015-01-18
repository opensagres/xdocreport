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
package fr.opensagres.xdocreport.document.preprocessor.dom;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.utils.DOMUtils;
import fr.opensagres.xdocreport.document.preprocessor.AbstractXDocPreprocessor;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public abstract class DOMPreprocessor
    extends AbstractXDocPreprocessor<Document>
{

    @Override
    protected Document getSource( XDocArchive documentArchive, String entryName )
        throws XDocReportException, IOException
    {

        try
        {
            return DOMUtils.load( documentArchive.getEntryInputStream( entryName ) );
        }
        catch ( ParserConfigurationException e )
        {
            throw new XDocReportException( e );
        }
        catch ( SAXException e )
        {
            throw new XDocReportException( e );
        }
    }

    @Override
    protected void closeSource( Document reader )
    {

    }

    @Override
    public boolean preprocess( String entryName, Document document, Writer writer, FieldsMetadata fieldsMetadata,
                               IDocumentFormatter formatter, Map<String, Object> sharedContext )
        throws XDocReportException, IOException
    {
        try
        {
            visit( document, entryName, fieldsMetadata, formatter, sharedContext );
            DOMUtils.save( document, writer );
            return true;
        }
        catch ( TransformerException e )
        {
            throw new XDocReportException( e );
        }
    }

    protected abstract void visit( Document document, String entryName, FieldsMetadata fieldsMetadata,
                                   IDocumentFormatter formatter, Map<String, Object> sharedContext )
        throws XDocReportException;

    /**
     * @param element
     * @param attrName
     * @param contextKey
     * @param formatter
     */
    protected void updateDynamicAttr( Element element, String attrName, String contextKey, IDocumentFormatter formatter )
    {
        if ( element.hasAttribute( attrName ) )
        {

            element.setAttribute( attrName, getDynamicAttr( element, attrName, contextKey, contextKey, formatter ) );

        }
    }

    /**
     * @param element
     * @param attrName
     * @param contextKey
     * @param formatter
     */
    protected void updateDynamicAttr( Element element, String attrName, String contextIfKey, String contextValueKey,
                                      IDocumentFormatter formatter )
    {
        if ( element.hasAttribute( attrName ) )
        {

            element.setAttribute( attrName,
                                  getDynamicAttr( element, attrName, contextIfKey, contextValueKey, formatter ) );

        }
    }

    /**
     * Generate directive (ex with FM: [#if ___font??]${___font}[#else]Arial[/#if])
     * 
     * @param element
     * @param attrName
     * @param formatter
     * @return
     */
    protected String getDynamicAttr( Element element, String attrName, String contextIfKey, String contextValueKey,
                                     IDocumentFormatter formatter )
    {

        StringBuilder value = new StringBuilder();
        value.append( formatter.getStartIfDirective( contextIfKey ) );       
        value.append( formatter.formatAsSimpleField( true, contextValueKey ) );
        value.append( formatter.getElseDirective() );
        value.append( element.getAttribute( attrName ) );
        value.append( formatter.getEndIfDirective( contextIfKey ) );
        return value.toString();
    }
}
