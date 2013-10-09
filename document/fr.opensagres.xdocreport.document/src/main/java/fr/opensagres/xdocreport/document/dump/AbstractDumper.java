package fr.opensagres.xdocreport.document.dump;

import java.io.IOException;
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

public abstract class AbstractDumper
    implements IDumper
{

    public static final String JAVA_MAIN_DUMP_TEMPLATE = "JavaMainDump";

    public final void dump( IXDocReport report, IContext context, DumperOption option, OutputStream out )
        throws IOException, XDocReportException
    {
        ITemplateEngine templateEngine =
            TemplateEngineRegistry.getRegistry().getTemplateEngine( report.getTemplateEngine().getKind() );
        doDump( report, context, option, templateEngine, out );
    }

    protected abstract void doDump( IXDocReport report, IContext context, DumperOption option,
                                    ITemplateEngine templateEngine, OutputStream out )
        throws IOException, XDocReportException;
}
