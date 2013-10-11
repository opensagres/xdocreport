[#if packageName ?? ]package ${packageName};[/#if]

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
import fr.opensagres.xdocreport.document.json.JSONTokener;
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
            [#if document ?? ]
            String document = "${document}";
            InputStream in = new ByteArrayInputStream( Base64Utility.decode( document ) );
            [#else]
            InputStream in = ${className}.class.getResourceAsStream("${documentFile}");
            [/#if]
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport( in, null, "${templateEngineKind}" );
            [#if xmlFields ?? ]
            
            // Set FieldsMetadata
            StringReader fieldsReader = new StringReader(${xmlFields});
            FieldsMetadata metadata = FieldsMetadataXMLSerializer.getInstance().load( fieldsReader );
            report.setFieldsMetadata( metadata );
            [#else]
            [#if xmlFieldsFile ?? ]
            
            // Set FieldsMetadata
            InputStream fieldsStream = ${className}.class.getResourceAsStream("${xmlFieldsFile}");
            FieldsMetadata metadata = FieldsMetadataXMLSerializer.getInstance().load( fieldsStream );
            report.setFieldsMetadata( metadata );
            [/#if]
            [/#if]
            			 
            // Create context
            [#if json ?? ]
            String json = "${json}";
            Map context = new JSONObject( json );
            [#else]
            InputStream jsonStream = ${className}.class.getResourceAsStream("${jsonFile}");
            Map context = new JSONObject( new JSONTokener(jsonStream) );
            [/#if]

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
