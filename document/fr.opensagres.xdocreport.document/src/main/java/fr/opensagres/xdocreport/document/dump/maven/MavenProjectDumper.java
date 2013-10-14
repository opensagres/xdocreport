package fr.opensagres.xdocreport.document.dump.maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.dump.AbstractProjectDumper;
import fr.opensagres.xdocreport.document.dump.DumperKind;
import fr.opensagres.xdocreport.document.dump.IDumper;
import fr.opensagres.xdocreport.document.dump.AbstractProjectDumper.ProjectDumperOption;
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

    public static class MavenProjectDumperOption
        extends ProjectDumperOption
    {

        public MavenProjectDumperOption()
        {
            super( DumperKind.MavenProject );
        }
    }

    @Override
    protected void dumpToFile( IXDocReport report, InputStream documentIn, IContext context,
                               ITemplateEngine templateEngine, IContext dumpContext, File baseDir )
        throws FileNotFoundException, IOException, XDocReportException
    {
        super.dumpToFile( report, documentIn, context, templateEngine, dumpContext, baseDir );

        // TODO : pom.xml
    }

    @Override
    protected void dumpToZip( IXDocReport report, InputStream documentIn, IContext context,
                              ITemplateEngine templateEngine, IContext dumpContext, ZipOutputStream zipOutputStream )
        throws FileNotFoundException, IOException, XDocReportException
    {
        super.dumpToZip( report, documentIn, context, templateEngine, dumpContext, zipOutputStream );

        // TODO : pom.xml
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
