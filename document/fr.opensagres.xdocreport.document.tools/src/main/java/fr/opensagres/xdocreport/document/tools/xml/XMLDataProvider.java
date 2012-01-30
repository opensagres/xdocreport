package fr.opensagres.xdocreport.document.tools.xml;

import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.tools.AbstractDataProvider;
import fr.opensagres.xdocreport.template.IContext;

public class XMLDataProvider
    extends AbstractDataProvider
{

    public XMLDataProvider( InputStream data, InputStream properties )
    {
        super( data, properties );
    }

    public void populateContext( IXDocReport report, IContext context )
        throws IOException, XDocReportException
    {
        // TODO Auto-generated method stub

    }

}
