package ${packageName};

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Map;

import fr.opensagres.xdocreport.core.utils.Base64Utility;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.json.JSONObject;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadataXMLSerializer;

public class ${className}
{

    public static void main( String[] args )
    {
        try
        {
            // Load report
            String document = "${document}";
            InputStream in = new ByteArrayInputStream( Base64Utility.decode( document ) );
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport( in, null, "${templateEngineKind}" );
            [#if  xmlFields ?? ]
            
            // Set FieldsMetadata
            StringReader reader = new StringReader( "${xmlFields}" );
            FieldsMetadata metadata = FieldsMetadataXMLSerializer.getInstance().load( reader );
            report.setFieldsMetadata( metadata );            
            [/#if]
            			 
            // Create context
            String json = "${json}";
            Map context = new JSONObject( json );

            // Generate report by merging context and template report.
            OutputStream out = new FileOutputStream( new File( "${outFileName}" ) );
            report.process( context, out );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
