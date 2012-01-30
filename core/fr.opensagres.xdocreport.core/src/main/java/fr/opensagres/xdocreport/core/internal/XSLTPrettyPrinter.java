/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 * 
 * All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS  IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.core.internal;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * XML Pretty Printer implemented with XSLT.
 */
public class XSLTPrettyPrinter
    implements IXMLPrettyPrinter
{

    public static final IXMLPrettyPrinter INSTANCE = new XSLTPrettyPrinter();

    private static final String XSLT_PRETTY_PRINTER =
        "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\" "
            + " xmlns:xalan=\"http://xml.apache.org/xslt\" " + " exclude-result-prefixes=\"xalan\">"
            + "  <xsl:output method=\"xml\" indent=\"yes\" xalan:indent-amount=\"{0}\" omit-xml-declaration=\"yes\"/>"
            + "  <xsl:strip-space elements=\"*\"/>" + "  <xsl:template match=\"/\">" + "    <xsl:apply-templates/>"
            + "  </xsl:template>" + "  <xsl:template match=\"node() | @*\">" + "        <xsl:copy>"
            + "          <xsl:apply-templates select=\"node() | @*\"/>" + "        </xsl:copy>" + "  </xsl:template>"
            + "</xsl:stylesheet>";

    private final Map<Integer, Templates> templates = new HashMap<Integer, Templates>();

    public String prettyPrint( String xml, int indent )
        throws Exception
    {

        Transformer transformer = getTemplates( indent ).newTransformer();

        final StringWriter out = new StringWriter();
        transformer.transform( new StreamSource( new StringReader( xml ) ), new StreamResult( out ) );
        return out.toString();
    }

    private Templates getTemplates( int indent )
        throws TransformerConfigurationException
    {
        Templates t = templates.get( indent );
        if ( t == null )
        {
            synchronized ( templates )
            {
                String xsl = MessageFormat.format( XSLT_PRETTY_PRINTER, indent );
                Source stylesheetSource = new StreamSource( new ByteArrayInputStream( xsl.getBytes() ) );
                TransformerFactory factory = TransformerFactory.newInstance();
                t = factory.newTemplates( stylesheetSource );
                templates.put( indent, t );
            }
        }
        return t;
    }
}
