package fr.opensagres.xdocreport.document.dispatcher;

import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public interface IXDocReportLoader
{

    InputStream getSourceStream( String reportId )
        throws IOException;

    String getTemplateEngineKind( String reportId );

    FieldsMetadata getFieldsMetadata( String reportId );
}
