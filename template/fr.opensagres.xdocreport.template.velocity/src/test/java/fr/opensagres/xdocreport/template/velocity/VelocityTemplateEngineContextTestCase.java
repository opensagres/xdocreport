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
package fr.opensagres.xdocreport.template.velocity;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.velocity.discovery.VelocityTemplateEngineDiscovery;

/**
 * Velocity context test case.
 */
public class VelocityTemplateEngineContextTestCase
    extends TestCase
{

    public void testSimpleContext()
        throws Exception
    {
        ITemplateEngine templateEngine = new  VelocityTemplateEngineDiscovery().createTemplateEngine();

        Reader reader = new StringReader( "Project: $projectName." );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();
        context.put( "projectName", "XDocReport" );

        templateEngine.process( "", context, reader, writer );
        assertEquals( "Project: XDocReport.", writer.toString() );
    }

    public void testContextWithOneDot()
        throws Exception
    {
        ITemplateEngine templateEngine = new  VelocityTemplateEngineDiscovery().createTemplateEngine();

        Reader reader = new StringReader( "Project: $project.name." );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();
        context.put( "project.name", "XDocReport" );

        templateEngine.process( "", context, reader, writer );
        assertEquals( "Project: XDocReport.", writer.toString() );
    }

    public void testContextWithTwoDot()
        throws Exception
    {
        ITemplateEngine templateEngine = new  VelocityTemplateEngineDiscovery().createTemplateEngine();

        Reader reader = new StringReader( "Project: $project.meta.name." );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();
        context.put( "project.meta.name", "XDocReport" );

        templateEngine.process( "", context, reader, writer );
        assertEquals( "Project: XDocReport.", writer.toString() );
    }

    public void testTwoContextWithTwoDot()
        throws Exception
    {
        ITemplateEngine templateEngine = new  VelocityTemplateEngineDiscovery().createTemplateEngine();

        Reader reader =
            new StringReader( "Project: $project.meta.name. Users: #foreach ($user in $project.meta.users)$user #end" );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();
        context.put( "project.meta.name", "XDocReport" );
        List<String> users = new ArrayList<String>();
        users.add( "Angelo" );
        users.add( "Pascal" );
        context.put( "project.meta.users", users );

        templateEngine.process( "", context, reader, writer );
        assertEquals( "Project: XDocReport. Users: Angelo Pascal ", writer.toString() );
    }
}
