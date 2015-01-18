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
package fr.opensagres.xdocreport.document.dump;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.MimeMappingConstants;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.dump.eclipse.EclipseProjectDumper.EclipseProjectDumperOptions;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;

public abstract class AbstractProjectDumper
    extends AbstractDumper
{

    public static class ProjectDumperOption
        extends DumperOptions
    {

        public ProjectDumperOption( DumperKind kind )
        {
            super( kind );
        }

        public static final String BASEDIR = "baseDir";

        public void setBaseDir( File baseDir )
        {
            super.put( BASEDIR, baseDir );
        }

        public File getBaseDir()
        {
            return (File) get( BASEDIR );
        }
    }

    @Override
    protected void doDump( IXDocReport report, InputStream documentIn, IContext context, DumperOptions option,
                           ITemplateEngine templateEngine, OutputStream out )
        throws IOException, XDocReportException
    {
        IContext dumpContext = DumpHelper.createDumpContext( report, templateEngine, option );

        File baseDir = null;
        String packageName = null;
        if ( option != null )
        {
            baseDir = (File) option.get( EclipseProjectDumperOptions.BASEDIR );
            packageName = option.getPackageName();
        }

        if ( baseDir != null )
        {
            baseDir.mkdirs();
            doDump( report, documentIn, context, templateEngine, dumpContext, baseDir, null );
        }
        else
        {
            ZipOutputStream zipOutputStream = null;
            try
            {
                zipOutputStream = new ZipOutputStream( out );
                doDump( report, documentIn, context, templateEngine, dumpContext, null, zipOutputStream );

            }
            finally
            {
                if ( zipOutputStream != null )
                {
                    IOUtils.closeQuietly( zipOutputStream );
                }
            }
        }
    }

    protected void doDump( IXDocReport report, InputStream documentIn, IContext context,
                           ITemplateEngine templateEngine, IContext dumpContext, File baseDir, ZipOutputStream out )
        throws IOException, XDocReportException
    {
        // Resources path
        String resourcesSrcPath = getResourcesSrcPath();

        // Document (docx, odt)
        DumpHelper.generateDocumentEntry( report, documentIn, dumpContext, resourcesSrcPath, baseDir, out );

        // JSON data
        boolean upperCaseFirstChar = templateEngine.isFieldNameStartsWithUpperCase();
        DumpHelper.generateJSONEntry( report, context, upperCaseFirstChar, dumpContext, resourcesSrcPath, baseDir, out );

        // XML Fields
        DumpHelper.generateFieldsMetadataEntry( report, dumpContext, resourcesSrcPath, baseDir, out );

        // Java path
        String javaSrcPath = getJavaSrcPath();
        String classNameFile = DumpHelper.getClassNameFile( dumpContext );
        String javaPath = javaSrcPath + "/" + classNameFile;
        DumpHelper.generateEntry( templateEngine, DumpHelper.JAVA_MAIN_DUMP_TEMPLATE, dumpContext, javaPath, baseDir,
                                  out );

    }

    public MimeMapping getMimeMapping()
    {
        return MimeMappingConstants.ZIP_MIME_MAPPING;
    }

    protected abstract String getJavaSrcPath();

    protected abstract String getResourcesSrcPath();
}
