package fr.opensagres.xdocreport.document.dump;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class JavaMainDumper
    extends AbstractDumper
{
    private static final IDumper INSTANCE = new JavaMainDumper();

    public static IDumper getInstance()
    {
        return INSTANCE;
    }

    public class JavaMainDumperOption
        extends DumperOption
    {

    }

    @Override
    protected void doDump( IXDocReport report, InputStream documentIn, IContext context, DumperOption option,
                           ITemplateEngine templateEngine, OutputStream out )
        throws IOException, XDocReportException
    {

        IContext dumpContext = DumpHelper.createDumpContext( report, templateEngine, option );

        // document (docx, odt).
        String documentAsBinaryB4 = DumpHelper.toDocumentAsBinary64( report );
        dumpContext.put( "document", documentAsBinaryB4 );

        // XML fields
        String xmlFields = null;
        FieldsMetadata metadata = report.getFieldsMetadata();
        if ( metadata != null )
        {
            StringWriter xmlFieldsWriter = new StringWriter();
            metadata.saveXML( xmlFieldsWriter, false, true );
            xmlFields = xmlFieldsWriter.toString();
        }
        dumpContext.put( "xmlFields", xmlFields );

        // JSON as data
        String json = DumpHelper.toJSON( context, true );
        dumpContext.put( "json", json );

        // Java Main
        OutputStreamWriter writer = new OutputStreamWriter( out );
        templateEngine.process( DumpHelper.JAVA_MAIN_DUMP_TEMPLATE, dumpContext, writer );
    }

}
