/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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

	private static final Tools INSTANCE = new Tools();

	public static Tools getInstance() {
		return INSTANCE;
	}

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

    }

    @Override
    protected void prepareHTTPResponse( String reportId, String entryName, Request request, Response response )
    {

    }

    @Override
    protected FieldsMetadata getFieldsMetadata( String reportId, Request request )
    {
        return request.getFieldsMetadata();
    }

}
