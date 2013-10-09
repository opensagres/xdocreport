package fr.opensagres.xdocreport.document.dump;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class DumperWithFreemarkerTest {

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

            Writer writer = new PrintWriter(System.err);

            DumperWithFreemarker.dump( report, context, writer );

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
}
