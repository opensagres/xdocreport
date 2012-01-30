package fr.opensagres.xdocreport.document.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.Generator;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class Tools
    extends Generator<Request, Response>
{

    public void process( File in, File out, TemplateEngineKind templateEngineKind, FieldsMetadata fieldsMetadata,
                         Iterable<IDataProvider> dataProviders )
        throws Exception
    {
        process( in, out, templateEngineKind.name(), fieldsMetadata, dataProviders );
    }

    public void process( File in, File out, String templateEngineKind, FieldsMetadata fieldsMetadata,
                         Iterable<IDataProvider> dataProviders )
        throws Exception
    {
        process( new FileInputStream( in ), new FileOutputStream( out ), templateEngineKind, fieldsMetadata,
                 dataProviders );
    }

    public void process( InputStream in, OutputStream out, String templateEngineKind, FieldsMetadata fieldsMetadata,
                         Iterable<IDataProvider> dataProviders )
        throws Exception
    {
        Request request = new Request( in, templateEngineKind, fieldsMetadata, dataProviders );
        Response response = new Response( out );
        processRequest( request, response );
    }

    @Override
    protected void error( Request request, Response response, Exception cause )
    {
        throw new RuntimeException( cause );
    }

    @Override
    protected InputStream getSourceStream( String reportId, Request request )
        throws IOException, XDocReportException
    {
        return request.getIn();
    }

    @Override
    protected void populateContext( IContext context, String reportId, Request request )
        throws IOException, XDocReportException
    {
        Iterable<IDataProvider> dataProviders = request.getDataProviders();
        if ( dataProviders != null )
        {
            for ( IDataProvider dataProvider : dataProviders )
            {
                dataProvider.populateContext( getReport( request ), context );
            }
        }
    }

    @Override
    protected OutputStream getOutputStream( Response response )
        throws IOException
    {
        return response.getOut();
    }

    @Override
    protected String getParameter( Request request, String name )
    {
        return request.get( name );
    }

    @Override
    protected void prepareHTTPResponse( String id, MimeMapping mimeMapping, Request request, Response response )
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void prepareHTTPResponse( String reportId, String entryName, Request request, Response response )
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected FieldsMetadata getFieldsMetadata( String reportId, Request request )
    {
        return request.getFieldsMetadata();
    }

}
