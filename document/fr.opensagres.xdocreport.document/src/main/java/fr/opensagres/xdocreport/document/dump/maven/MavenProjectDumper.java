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
package fr.opensagres.xdocreport.document.dump.maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

import fr.opensagres.xdocreport.core.Platform;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.dump.AbstractProjectDumper;
import fr.opensagres.xdocreport.document.dump.DumpHelper;
import fr.opensagres.xdocreport.document.dump.DumperKind;
import fr.opensagres.xdocreport.document.dump.IDumper;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;

public class MavenProjectDumper
    extends AbstractProjectDumper
{

    private static final String JAVA_PATH = "src/main/java";

    private static final String RESOURCES_PATH = "src/main/resources";

    private static final IDumper INSTANCE = new MavenProjectDumper();

    public static IDumper getInstance()
    {
        return INSTANCE;
    }

    public static class MavenProjectDumperOptions
        extends ProjectDumperOption
    {

        public MavenProjectDumperOptions()
        {
            super( DumperKind.MavenProject );
        }
    }

    @Override
    protected void doDump( IXDocReport report, InputStream documentIn, IContext context,
                           ITemplateEngine templateEngine, IContext dumpContext, File baseDir, ZipOutputStream out )
        throws IOException, XDocReportException
    {
        super.doDump( report, documentIn, context, templateEngine, dumpContext, baseDir, out );

        // Generate pom.xml
        String artifactId = baseDir.getName();
        dumpContext.put( "artifactId", artifactId );

        String version = Platform.getVersion();
        dumpContext.put( "version", version );

        dumpContext.put( "documentExtension", report.getKind().toLowerCase() );

        dumpContext.put( "templateExtension", templateEngine.getKind().toLowerCase() );

        dumpContext.put( "isSnapshot", version.endsWith( "-SNAPSHOT" ) );

        DumpHelper.generateEntry( templateEngine, "pom.xml", dumpContext, "pom.xml", baseDir, out );
    }

    @Override
    protected String getJavaSrcPath()
    {
        return JAVA_PATH;
    }

    @Override
    protected String getResourcesSrcPath()
    {
        return RESOURCES_PATH;
    }

}
