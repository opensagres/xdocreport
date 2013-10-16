package fr.opensagres.xdocreport.document.dump.eclipse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.dump.AbstractProjectDumper;
import fr.opensagres.xdocreport.document.dump.DumpHelper;
import fr.opensagres.xdocreport.document.dump.DumperKind;
import fr.opensagres.xdocreport.document.dump.IDumper;
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
    protected void doDump( IXDocReport report, InputStream documentIn, IContext context,
                           ITemplateEngine templateEngine, IContext dumpContext, File baseDir, ZipOutputStream out )
        throws IOException, XDocReportException
    {
        super.doDump( report, documentIn, context, templateEngine, dumpContext, baseDir, out );

        // .project
        String projectName = baseDir != null ? baseDir.getName() : (String) dumpContext.get( "className" );
        dumpContext.put( "projectName", projectName );
        DumpHelper.generateEntry( templateEngine, "project", dumpContext, ".project", baseDir, out );
        // .classpath
        DumpHelper.generateEntry( templateEngine, "classpath", dumpContext, ".classpath", baseDir, out );
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
