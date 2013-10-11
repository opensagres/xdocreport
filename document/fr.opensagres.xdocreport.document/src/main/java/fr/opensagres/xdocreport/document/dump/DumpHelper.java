package fr.opensagres.xdocreport.document.dump;

import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.utils.Base64Utility;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.json.JSONObject;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;

public class DumpHelper
{

    private static final String ESCAPED_DOUBLE_QUOTE = "\\\"";

    private static final String DOUBLE_QUOTE = "\"";

    public static String toDocumentAsBinary64( IXDocReport report )
        throws IOException
    {
        InputStream in = getDocument( report );
        return Base64Utility.encode( in );
    }

    public static InputStream getDocument( IXDocReport report )
        throws IOException
    {
        InputStream in = XDocArchive.getInputStream( report.getOriginalDocumentArchive() );
        return in;
    }

    public static String getClassName( IXDocReport report )
    {
        String className = report.getId();
        className = StringUtils.replaceEach( className, new String[] { "@", "." }, new String[] { "_", "_" } );
        return className.substring( 0, 1 ).toUpperCase() + className.substring( 1, className.length() );
    }

    public static String toJSON( IContext context )
    {
        return toJSON( context, false );
    }

    public static String toJSON( IContext context, boolean formatAsJavaString )
    {
        JSONObject json = new JSONObject( context.getContextMap() );
        String jsonString = json.toString();
        if ( !formatAsJavaString )
        {
            return jsonString;
        }
        return jsonString.replace( DOUBLE_QUOTE, ESCAPED_DOUBLE_QUOTE );
    }

    public static IContext createDumpContext( IXDocReport report, ITemplateEngine templateEngine, DumperOption option )
        throws IOException
    {
        IContext dumpContext = templateEngine.createContext();

        String packageName = option.getPackageName();
        dumpContext.put( "packageName", packageName );

        String className = DumpHelper.getClassName( report );
        dumpContext.put( "className", className );

        dumpContext.put( "templateEngineKind", templateEngine.getKind() );
        
        String outFileName = report.getId() + " _Out"+ "." + report.getKind().toLowerCase();
        dumpContext.put( "outFileName", outFileName );
        
        return dumpContext;
    }
}
