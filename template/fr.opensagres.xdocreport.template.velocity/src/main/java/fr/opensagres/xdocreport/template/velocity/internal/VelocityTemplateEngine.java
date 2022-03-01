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
package fr.opensagres.xdocreport.template.velocity.internal;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import fr.opensagres.xdocreport.core.EncodingConstants;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.template.AbstractTemplateEngine;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.config.ITemplateEngineConfiguration;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityConstants;
import fr.opensagres.xdocreport.template.velocity.VelocityDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityFieldsExtractor;

/**
 * Velocity template engine implementation.
 */
public class VelocityTemplateEngine
    extends AbstractTemplateEngine
    implements VelocityConstants
{

    private static String ID = TemplateEngineKind.Velocity.name();

    private VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();

    private VelocityEngine velocityEngine;

    private final Properties velocityEngineProperties;

    public VelocityTemplateEngine( Properties velocityEngineProperties )
    {
        this.velocityEngineProperties = velocityEngineProperties;
    }

    public String getKind()
    {
        return TemplateEngineKind.Velocity.name();
    }

    public String getId()
    {
        return ID;
    }

    public IContext createContext()
    {
        return new XDocVelocityContext();
    }

    public IContext createContext( Map<String, Object> contextMap )
    {
        return new XDocVelocityContext( contextMap );
    }

    @Override
    protected void processWithCache( String templateName, IContext context, Writer writer )
        throws XDocReportException, IOException
    {
        VelocityEngine velocityEngine = getVelocityEngine();
        Template template = velocityEngine.getTemplate( templateName, EncodingConstants.UTF_8.name() );
        if ( template != null )
        {
            template.merge( (VelocityContext) context, writer );
        }
    }

    @Override
    protected void processNoCache( String templateName, IContext context, Reader reader, Writer writer )
        throws XDocReportException, IOException
    {
        VelocityEngine velocityEngine = getVelocityEngine();
        velocityEngine.evaluate( (VelocityContext) context, writer, templateName, reader );
    }

    protected synchronized VelocityEngine getVelocityEngine()
        throws XDocReportException
    {
        if ( velocityEngine == null )
        {
            velocityEngine = new VelocityEngine();
            initializeVelocityEngine( velocityEngineProperties );
        }
        return velocityEngine;
    }

    public void initializeVelocityEngine( Properties velocityEngineProperties )
        throws XDocReportException
    {
        ClassLoader backupCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader( VelocityTemplateEngine.class.getClassLoader() );
        try
        {
            velocityEngine.setProperty( VELOCITY_TEMPLATE_ENGINE_KEY, this );
            velocityEngine.init( velocityEngineProperties );

        }
        catch ( Exception e )
        {
            throw new XDocReportException( e );
        }
        Thread.currentThread().setContextClassLoader( backupCL );
    }

    @Override
    public void setConfiguration( ITemplateEngineConfiguration configuration )
    {
        super.setConfiguration( configuration );
        if ( configuration != null && configuration.escapeXML() )
        {
            velocityEngineProperties.setProperty( "event_handler.reference_insertion.class",
                                                  XDocReportEscapeReference.class.getName() );
        }

    }

    public IDocumentFormatter getDocumentFormatter()
    {
        return formatter;
    }

    public void extractFields( Reader reader, String entryName, FieldsExtractor extractor )
        throws XDocReportException
    {
        VelocityFieldsExtractor.getInstance().extractFields( reader, entryName, extractor );
    }

    public void process( String templateName, IContext context, Writer writer )
        throws IOException, XDocReportException
    {
        // TODO : Improve it, cache the JavaMainDump.vm
        templateName = templateName + ".vm";
        Reader reader = new InputStreamReader( VelocityTemplateEngine.class.getResourceAsStream( templateName ) );

        try
        {
            VelocityEngine velocityEngine = getVelocityEngine();
            velocityEngine.evaluate( (VelocityContext) context, writer, "", reader );
        }
        finally
        {
            if ( reader != null )
            {
                IOUtils.closeQuietly( reader );
            }
        }

    }

    public boolean isFieldNameStartsWithUpperCase()
    {
        return false;
    }
}
