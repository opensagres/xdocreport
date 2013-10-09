package fr.opensagres.xdocreport.document.dump;

import java.io.File;
import java.io.OutputStream;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;

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
    {
        File baseDir = null;
        if ( option != null )
        {
            baseDir = (File) option.get( EclipseProjectDumperOption.BASEDIR );
        }

        if ( baseDir != null )
        {

        }
    }

}
