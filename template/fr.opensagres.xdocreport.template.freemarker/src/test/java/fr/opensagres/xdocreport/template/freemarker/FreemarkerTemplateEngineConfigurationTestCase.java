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
package fr.opensagres.xdocreport.template.freemarker;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.config.AbstractTemplateEngineConfiguration;
import fr.opensagres.xdocreport.template.config.ITemplateEngineConfiguration;
import fr.opensagres.xdocreport.template.config.ReplaceText;

public class FreemarkerTemplateEngineConfigurationTestCase
    extends TestCase
{

    public void testEscapeXMLContext()
        throws Exception
    {
        ITemplateEngine templateEngine = new FreemarkerTemplateEngine();
        // Important to set this flag to true because Freemarker escape
        // character with [#escape
        // directive, escaping can be done with reader modification.
        // In the XDocReport process report, no need to set this flag, it's SAX
        // parser (TransformedBufferedDocumentContentHandler) which manage that
        // with IDocumentFormatter#getStart/EndDocumentDirective(.
        ( (FreemarkerTemplateEngine) templateEngine ).setForceModifyReader( true );

        ITemplateEngineConfiguration configuration = new JUnitTemplateEngineConfiguration();
        templateEngine.setConfiguration( configuration );

        Reader reader = new StringReader( "Project: ${project.name}." );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();
        context.put( "project.name", "A&B" );

        templateEngine.process( "", context, reader, writer );
        assertEquals( "Project: A&amp;B.", writer.toString() );
    }

    public void testSpecialCharacterContext()
        throws Exception
    {
        ITemplateEngine templateEngine = new FreemarkerTemplateEngine();
        // Important to set this flag to true because Freemarker escape
        // character with [#escape
        // directive, escaping can be done with reader modification.
        // In the XDocReport process report, no need to set this flag, it's SAX
        // parser (TransformedBufferedDocumentContentHandler) which manage that
        // with IDocumentFormatter#getStart/EndDocumentDirective(.
        ( (FreemarkerTemplateEngine) templateEngine ).setForceModifyReader( true );

        ITemplateEngineConfiguration configuration = new JUnitTemplateEngineConfiguration();
        templateEngine.setConfiguration( configuration );

        Reader reader = new StringReader( "Project: ${project.name}." );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();
        context.put( "project.name", "A\nB" );

        templateEngine.process( "", context, reader, writer );
        assertEquals( "Project: A<text:line-break>B.", writer.toString() );
    }

    private static class JUnitTemplateEngineConfiguration
        extends AbstractTemplateEngineConfiguration
    {

        @Override
        public boolean escapeXML()
        {
            return true;
        }

        @Override
        protected void populate( Collection<ReplaceText> replacment )
        {
            replacment.add( new ReplaceText( "\n", "<text:line-break>" ) );

        }

    }
}
