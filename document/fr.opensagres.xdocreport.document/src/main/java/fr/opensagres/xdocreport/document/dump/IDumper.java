package fr.opensagres.xdocreport.document.dump;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public interface IDumper
{

    void dump( IXDocReport report, IContext context, DumperOption option, OutputStream out )
                    throws IOException, XDocReportException;
    
    void dump( IXDocReport report, InputStream documentIn, IContext context, DumperOption option, OutputStream out )
        throws IOException, XDocReportException;

}
