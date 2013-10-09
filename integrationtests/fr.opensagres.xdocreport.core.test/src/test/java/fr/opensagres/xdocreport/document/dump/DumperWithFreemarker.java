package fr.opensagres.xdocreport.document.dump;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.utils.Base64Utility;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.json.JSONObject;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

public class DumperWithFreemarker
{

    public static void main( String[] args )
    {
        try
        {
            // 1) Load Docx file by filling Velocity template engine and cache
            // it to the registry
            String reportId = "DumTest";
            InputStream in = DumperWithFreemarker.class.getResourceAsStream( "DocxProjectWithFreemarkerList.docx" );
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( in, reportId, TemplateEngineKind.Freemarker );

            // IMPORTANT : cache the original document to use dump.
            report.setCacheOriginalDocument( true );

            FieldsMetadata metadata = report.createFieldsMetadata();
            metadata.addFieldAsList( "developers.name" );
            metadata.addFieldAsList( "developers.lastName" );
            metadata.addFieldAsList( "developers.mail" );

            // 2) Create context Java model
            IContext context = report.createContext();
            context.put( "name", "XDocReport" );
            List<Map<String, String>> developers = new ArrayList<Map<String, String>>();
            Map<String, String> developer1 = new HashMap<String, String>();
            developer1.put( "Name", "ZERR" );
            developer1.put( "LastName", "Angelo" );
            developer1.put( "Mail", "angelo.zerr@gmail.com" );
            developers.add( developer1 );
            Map<String, String> developer2 = new HashMap<String, String>();
            developer2.put( "Name", "Leclercq" );
            developer2.put( "LastName", "Pascal" );
            developer2.put( "Mail", "pascal.leclercq@gmail.com" );
            developers.add( developer2 );
            context.put( "developers", developers );

            Writer writer = new StringWriter();

            dump( report, context, writer );

            System.err.println( writer );

        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        catch ( XDocReportException e )
        {
            e.printStackTrace();
        }

    }

    private static void dump( IXDocReport report, IContext context, Writer writer )
        throws XDocReportException, IOException
    {

        String templateEngineKind = report.getTemplateEngine().getKind();
        ITemplateEngine templateEngine =
            TemplateEngineRegistry.getRegistry().getTemplateEngine( report.getTemplateEngine().getKind() );

        IContext dumpContext = templateEngine.createContext();
        String packageName = "dump";
        dumpContext.put( "packageName", packageName );
        String className = getClassName( report );
        dumpContext.put( "className", className );

        String documentAsBinaryB4 = getDocument( report );
        dumpContext.put( "document", documentAsBinaryB4 );
        dumpContext.put( "templateEngineKind", templateEngineKind );

        String xmlFields = null;
        FieldsMetadata metadata = report.getFieldsMetadata();
        if ( metadata != null )
        {
            StringWriter xmlFieldsWriter = new StringWriter();
            metadata.saveXML( xmlFieldsWriter );
            xmlFields = xmlFieldsWriter.toString();
            xmlFields = StringUtils.replaceAll( xmlFields, "\"", "\\\"" );
        }
        dumpContext.put( "xmlFields", xmlFields );

        String json = getJSON( context );
        dumpContext.put( "json", json );

        String outFileName = report.getId() + "." + report.getKind().toLowerCase();
        dumpContext.put( "outFileName", outFileName );

        Reader reader = new InputStreamReader( DumperWithFreemarker.class.getResourceAsStream( "JavaMainDump.ftl" ) );
        templateEngine.process( "dump", dumpContext, reader, writer );
    }

    private static String getDocument( IXDocReport report )
        throws IOException
    {
        InputStream in = XDocArchive.getInputStream( report.getOriginalDocumentArchive() );
        byte[] bytes = IOUtils.toByteArray( in );
        String encoded = Base64Utility.encode( bytes );
        return encoded;
    }

    private static String getClassName( IXDocReport report )
    {
        String className = report.getId();
        className = StringUtils.replaceEach( className, new String[] { "@", "." }, new String[] { "_", "_" } );
        return className.substring( 0, 1 ).toUpperCase() + className.substring( 1, className.length() );
    }

    private static String getJSON( IContext context )
    {
    	JSONObject json =new JSONObject(context.getContextMap()); 
    	return json.toString();
    }
}
