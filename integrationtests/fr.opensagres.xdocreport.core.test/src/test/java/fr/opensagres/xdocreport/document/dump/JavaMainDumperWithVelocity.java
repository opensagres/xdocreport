package fr.opensagres.xdocreport.document.dump;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.dump.java.JavaMainDumper.JavaMainDumperOptions;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class JavaMainDumperWithVelocity
{

    public static void main( String[] args )
    {
        try
        {
            // 1) Load Docx file by filling Velocity template engine and cache
            // it to the registry
            String reportId = "DumTest";
            InputStream in = JavaMainDumperWithVelocity.class.getResourceAsStream( "DocxProjectWithVelocityList.docx" );
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( in, reportId, TemplateEngineKind.Velocity );

            // IMPORTANT : cache the original document to use dump.
            report.setCacheOriginalDocument( true );

            FieldsMetadata metadata = report.createFieldsMetadata();
            metadata.addFieldAsList( "developers.Name" );
            metadata.addFieldAsList( "developers.LastName" );
            metadata.addFieldAsList( "developers.Mail" );

            // 2) Create context Java model
            IContext context = report.createContext();
            // populateWithMap( context );
            populateWithPojo( context );

            OutputStream out = System.err;

            // JavaMainDumper.getInstance().dump( report, context, null, out );
            JavaMainDumperOptions options = new JavaMainDumperOptions();
            report.dump( context, options, out );

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

    private static void populateWithPojo( IContext context )
    {
        Project project = new Project( "XDocReport" );
        context.put( "project", project );

        List<Developer> developers = new ArrayList<Developer>();
        developers.add( new Developer( "ZERR", "Angelo", "angelo.zerr@gmail.com" ) );
        developers.add( new Developer( "Leclercq", "Pascal", "pascal.leclercq@gmail.com" ) );
        context.put( "developers", developers );
    }

    private static void populateWithMap( IContext context )
    {
        Map<String, String> project = new HashMap<String, String>();
        project.put( "name", "XDocReport" );
        context.put( "project", project );

        List<Map<String, String>> developers = new ArrayList<Map<String, String>>();
        Map<String, String> developer1 = new HashMap<String, String>();
        developer1.put( "name", "ZERR" );
        developer1.put( "lastName", "Angelo" );
        developer1.put( "mail", "angelo.zerr@gmail.com" );
        developers.add( developer1 );
        Map<String, String> developer2 = new HashMap<String, String>();
        developer2.put( "name", "Leclercq" );
        developer2.put( "lastName", "Pascal" );
        developer2.put( "mail", "pascal.leclercq@gmail.com" );
        developers.add( developer2 );
        context.put( "developers", developers );
    }
}
