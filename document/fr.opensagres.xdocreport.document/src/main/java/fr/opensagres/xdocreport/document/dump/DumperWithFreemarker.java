package fr.opensagres.xdocreport.document.dump;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.utils.Base64Utility;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.json.JSONObject;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

public class DumperWithFreemarker
{

    private static final String ESCAPED_DOUBLE_QUOTE = "\\\"";
	private static final String DOUBLE_QUOTE = "\"";

    public static void dump( IXDocReport report, IContext context, Writer writer )
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
            xmlFields = StringUtils.replaceAll( xmlFields, DOUBLE_QUOTE, ESCAPED_DOUBLE_QUOTE );
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
    	String jsonString = json.toString();
    	return jsonString.replace(DOUBLE_QUOTE, ESCAPED_DOUBLE_QUOTE);
    }
}
