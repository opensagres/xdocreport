package fr.opensagres.xdocreport.document.dump;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

public abstract class AbstractDumper
    implements IDumper
{

    public void dump( IXDocReport report, IContext context, DumperOptions option, OutputStream out )
        throws IOException, XDocReportException
    {
        dump( report, null, context, option, out );

    }

    public final void dump( IXDocReport report, InputStream documentIn, IContext context, DumperOptions option,
                            OutputStream out )
        throws IOException, XDocReportException
    {
        try
        {
            ITemplateEngine templateEngine =
                TemplateEngineRegistry.getRegistry().getTemplateEngine( report.getTemplateEngine().getKind() );
            if ( documentIn == null )
            {
                documentIn = DumpHelper.getDocument( report );
            }
            doDump( report, documentIn, context, option, templateEngine, out );
        }
        finally
        {
            if ( documentIn != null )
            {
                IOUtils.closeQuietly( documentIn );
            }
            if ( out != null )
            {
                IOUtils.closeQuietly( out );
            }
        }
    }

    protected abstract void doDump( IXDocReport report, InputStream documentIn, IContext context, DumperOptions option,
                                    ITemplateEngine templateEngine, OutputStream out )
        throws IOException, XDocReportException;
}
