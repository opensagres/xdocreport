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
package fr.opensagres.xdocreport.document.web.dispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.dispatcher.IXDocReportDispatcher;
import fr.opensagres.xdocreport.document.web.AbstractProcessXDocReportServlet;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class ProcessDispatcherXDocReportServlet
    extends AbstractProcessXDocReportServlet
{

    private static final long serialVersionUID = -1568586154827821538L;

    protected final List<IXDocReportDispatcher<?>> dispatchers = new ArrayList<IXDocReportDispatcher<?>>();

    @Override
    public void init( ServletConfig config )
        throws ServletException
    {
        super.init( config );
        String dispatchers = config.getInitParameter( "dispatchers" );
        if ( StringUtils.isNotEmpty( dispatchers ) )
        {
            String clazz = null;
            String[] classes = dispatchers.split( "," );
            for ( int i = 0; i < classes.length; i++ )
            {
                clazz = classes[i];
                try
                {
                    Object instance = getClass().getClassLoader().loadClass( clazz ).newInstance();
                    registerDispatcher( (IXDocReportDispatcher<?>) instance );
                }
                catch ( Throwable e )
                {
                    // TODO : manage error
                    e.printStackTrace();
                }
            }
        }
    }

    public void registerDispatcher( IXDocReportDispatcher<?> dispatcher )
    {
        dispatchers.add( dispatcher );
    }

    public void unregisterDispatcher( IXDocReportDispatcher<?> dispatcher )
    {
        dispatchers.remove( dispatcher );
    }

    @Override
    protected InputStream getSourceStream( String reportId, HttpServletRequest request )
        throws IOException, XDocReportException
    {
        for ( IXDocReportDispatcher<?> dispatcher : dispatchers )
        {
            InputStream in = dispatcher.getSourceStream( reportId );
            if ( in != null )
            {
                return in;
            }
        }
        return null;
    }

    @Override
    protected String getTemplateEngineKind( HttpServletRequest request )
    {
        for ( IXDocReportDispatcher<?> dispatcher : dispatchers )
        {
            String templateEngineId = dispatcher.getTemplateEngineKind( getReportId( request ) );
            if ( StringUtils.isNotEmpty( templateEngineId ) )
            {
                return templateEngineId;
            }
        }
        // Use default template engine
        return super.getTemplateEngineKind( request );
    }

    @Override
    protected FieldsMetadata getFieldsMetadata( String reportId, HttpServletRequest request )
    {
        for ( IXDocReportDispatcher<?> dispatcher : dispatchers )
        {
            FieldsMetadata fieldsMetadata = dispatcher.getFieldsMetadata( reportId );
            if ( fieldsMetadata != null )
            {
                return fieldsMetadata;
            }
        }
        return super.getFieldsMetadata( reportId, request );
    }
    
    @Override
    protected boolean isCacheReport( String reportId, HttpServletRequest request )
    {

        for ( IXDocReportDispatcher<?> dispatcher : dispatchers )
        {
            Boolean cacheReport = dispatcher.isCacheReport( reportId );
            if ( cacheReport != null )
            {
                return cacheReport;
            }
        }
        return super.isCacheReport( reportId, request );
    }

    @Override
    protected void populateContext( IContext context, String reportId, HttpServletRequest request )
        throws IOException, XDocReportException
    {
        IXDocReport report = super.getRegistry( request ).getReport( reportId );
        if ( report != null )
        {
            populateContext( context, report, request );
        }
    }

    protected void populateContext( IContext context, IXDocReport report, HttpServletRequest request )
    {
        String reportId = report.getId();
        for ( IXDocReportDispatcher<?> dispatcher : dispatchers )
        {
            IXDocReportWEBController controler = (IXDocReportWEBController) dispatcher.getReportController( reportId );
            if ( controler != null )
            {
                controler.populateContext( context, report, request );
            }
        }
    }
}
