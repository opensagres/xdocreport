package fr.opensagres.xdocreport.document.dump;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.json.JSONObject;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class EclipseProjectDumper
    extends AbstractDumper
{

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
    protected void doDump( IXDocReport report, IContext context, DumperOption option, ITemplateEngine templateEngine,
                           OutputStream out )
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
            File srcDir = new File( baseDir, "src" );
            srcDir.mkdirs();

            // Document (docx, odt)
            String documentFileName = report.getId() + "." + report.getKind().toLowerCase();
            dumpContext.put( "documentFile", documentFileName );

            File documentFile = new File( srcDir, documentFileName );
            InputStream documentIn = null;
            OutputStream documentOut = null;
            try
            {
                documentIn = DumpHelper.getDocument( report );
                documentOut = new FileOutputStream( documentFile );
                IOUtils.copy( documentIn, documentOut );
            }
            finally
            {
                if ( documentIn != null )
                {
                    IOUtils.closeQuietly( documentIn );
                }
                if ( documentOut != null )
                {
                    IOUtils.closeQuietly( documentOut );
                }
            }

            // JSON
            String jsonFileName = "data.json";
            dumpContext.put( "jsonFile", jsonFileName );

            JSONObject jsonObject = new JSONObject( context.getContextMap() );

            File jsonFile = new File( srcDir, jsonFileName );
            Writer jsonWriter = null;
            try
            {
                jsonWriter = new FileWriter( jsonFile );
                String json = jsonObject.toString( 1 );
                IOUtils.write( json, jsonWriter );
            }
            finally
            {
                if ( jsonWriter != null )
                {
                    IOUtils.closeQuietly( jsonWriter );
                }
            }

            // XML Fields
            FieldsMetadata metadata = report.getFieldsMetadata();
            if ( metadata != null )
            {

                String xmlFieldsFileName = "fields.xml";
                dumpContext.put( "xmlFieldsFile", xmlFieldsFileName );

                File xmlFieldsFile = new File( srcDir, xmlFieldsFileName );
                Writer xmlFieldsWriter = null;
                try
                {
                    xmlFieldsWriter = new FileWriter( xmlFieldsFile );
                    metadata.saveXML( xmlFieldsWriter, true, false );
                }
                finally
                {
                    if ( xmlFieldsWriter != null )
                    {
                        IOUtils.closeQuietly( xmlFieldsWriter );
                    }
                }
            }

            // Java
            File javaFile = new File( srcDir, (String) dumpContext.get( "className" ) + ".java" );
            Writer javaWriter = null;
            try
            {
                javaWriter = new FileWriter( javaFile );
                templateEngine.process( JAVA_MAIN_DUMP_TEMPLATE, dumpContext, javaWriter );
            }
            finally
            {
                if ( javaWriter != null )
                {
                    IOUtils.closeQuietly( javaWriter );
                }
            }

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
    }

}
