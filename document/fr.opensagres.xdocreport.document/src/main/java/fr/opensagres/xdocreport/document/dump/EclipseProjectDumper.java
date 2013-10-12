package fr.opensagres.xdocreport.document.dump;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.zip.ZipOutputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;

public class EclipseProjectDumper
    extends AbstractDumper
{

    private static final String SRC_FOLDER = "src";

    private static final IDumper INSTANCE = new EclipseProjectDumper();

    public static final String ECLIPSE_PROJECT_TEMPLATE = ".project";

    public static IDumper getInstance()
    {
        return INSTANCE;
    }

    public static class EclipseProjectDumperOption
        extends DumperOption
    {

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
    protected void doDump( IXDocReport report, InputStream documentIn, IContext context, DumperOption option,
                           ITemplateEngine templateEngine, OutputStream out )
        throws IOException, XDocReportException
    {
        IContext dumpContext = DumpHelper.createDumpContext( report, templateEngine, option );
        String projectName = null;
        File baseDir = null;
        String packageName = null;
        if ( option != null )
        {
            baseDir = (File) option.get( EclipseProjectDumperOption.BASEDIR );
            packageName = option.getPackageName();
        }

        if ( baseDir != null )
        {
            baseDir.mkdirs();
            projectName = baseDir.getName();

            // src
            File srcDir = new File( baseDir, SRC_FOLDER );
            srcDir.mkdirs();

            // Document (docx, odt)
            DumpHelper.generateDocumentFile( report, documentIn, dumpContext, srcDir );

            // JSON
            DumpHelper.generateJSONFile( report, context, dumpContext, srcDir );

            // XML Fields
            DumpHelper.generateFieldsMetadataFile( report, dumpContext, srcDir );

            // Java
            DumpHelper.generateJavaMainFile( templateEngine, dumpContext, srcDir );

            // .project
            dumpContext.put( "projectName", projectName );
            File projectFile = new File( baseDir, ".project" );
            Writer projectWriter = null;
            try
            {
                projectWriter = new FileWriter( projectFile );
                templateEngine.process( "project", dumpContext, projectWriter );
            }
            finally
            {
                if ( projectWriter != null )
                {
                    IOUtils.closeQuietly( projectWriter );
                }
            }

            // .classpath
            File classpathFile = new File( baseDir, ".classpath" );
            Writer classpathWriter = null;
            try
            {
                classpathWriter = new FileWriter( classpathFile );
                templateEngine.process( "classpath", dumpContext, classpathWriter );
            }
            finally
            {
                if ( classpathWriter != null )
                {
                    IOUtils.closeQuietly( classpathWriter );
                }
            }

        }
        else
        {
            ZipOutputStream zipOutputStream = null;
            try
            {
                zipOutputStream = new ZipOutputStream( out );

                // Document (docx, odt)
                DumpHelper.generateDocumentZipEntry( report, documentIn, dumpContext, zipOutputStream, SRC_FOLDER );

                // JSON data
                DumpHelper.generateJSONZipEntry( report, context, dumpContext, zipOutputStream, SRC_FOLDER );

                // XML Fields
                DumpHelper.generateFieldsMetadataZipEntry( report, dumpContext, zipOutputStream, SRC_FOLDER );

                // Java
                DumpHelper.generateJavaMainZipEntry( report, templateEngine, dumpContext, zipOutputStream, SRC_FOLDER );

                // .project
                projectName = report.getId();
                dumpContext.put( "projectName", projectName );
                DumpHelper.generateZipEntry( templateEngine, "project", dumpContext, ".project", zipOutputStream );

                // .classpath
                DumpHelper.generateZipEntry( templateEngine, "classpath", dumpContext, ".classpath", zipOutputStream );

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

}
