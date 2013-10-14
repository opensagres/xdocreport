package fr.opensagres.xdocreport.document.dump.eclipse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.dump.AbstractProjectDumper;
import fr.opensagres.xdocreport.document.dump.DumpHelper;
import fr.opensagres.xdocreport.document.dump.DumperKind;
import fr.opensagres.xdocreport.document.dump.IDumper;
import fr.opensagres.xdocreport.document.dump.AbstractProjectDumper.ProjectDumperOption;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;

public class EclipseProjectDumper
    extends AbstractProjectDumper
{

    private static final String SRC_FOLDER = "src";

    private static final IDumper INSTANCE = new EclipseProjectDumper();

    public static final String ECLIPSE_PROJECT_TEMPLATE = ".project";

    public static IDumper getInstance()
    {
        return INSTANCE;
    }

    public static class EclipseProjectDumperOptions
        extends ProjectDumperOption
    {

        public EclipseProjectDumperOptions()
        {
            super( DumperKind.EclipseProject );
        }
    }

    @Override
    protected void dumpToFile( IXDocReport report, InputStream documentIn, IContext context,
                               ITemplateEngine templateEngine, IContext dumpContext, File baseDir )
        throws FileNotFoundException, IOException, XDocReportException
    {
        super.dumpToFile( report, documentIn, context, templateEngine, dumpContext, baseDir );

        // .project
        String projectName = baseDir.getName();
        dumpContext.put( "projectName", projectName );
        File projectFile = new File( baseDir, ".project" );
        DumpHelper.generateFile( templateEngine, "project", dumpContext, projectFile );

        // .classpath
        File classpathFile = new File( baseDir, ".classpath" );
        DumpHelper.generateFile( templateEngine, "classpath", dumpContext, classpathFile );

    }

    @Override
    protected void dumpToZip( IXDocReport report, InputStream documentIn, IContext context,
                              ITemplateEngine templateEngine, IContext dumpContext, ZipOutputStream zipOutputStream )
        throws FileNotFoundException, IOException, XDocReportException
    {
        super.dumpToZip( report, documentIn, context, templateEngine, dumpContext, zipOutputStream );

        // .project
        String projectName = report.getId();
        dumpContext.put( "projectName", projectName );
        DumpHelper.generateZipEntry( templateEngine, "project", dumpContext, ".project", zipOutputStream );

        // .classpath
        DumpHelper.generateZipEntry( templateEngine, "classpath", dumpContext, ".classpath", zipOutputStream );
    }

    @Override
    protected String getJavaSrcPath()
    {
        return SRC_FOLDER;
    }

    @Override
    protected String getResourcesSrcPath()
    {
        return getJavaSrcPath();
    }
    
}
