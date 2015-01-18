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
package fr.opensagres.xdocreport.template.freemarker;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.core.EncodingConstants;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.template.AbstractTemplateEngine;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.config.ITemplateEngineConfiguration;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.freemarker.cache.XDocReportEntryTemplateLoader;
import fr.opensagres.xdocreport.template.freemarker.internal.XDocFreemarkerContext;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.core.TemplateElement;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * Freemarker template engine implementation.
 */
public class FreemarkerTemplateEngine
    extends AbstractTemplateEngine
    implements FreemarkerConstants
{

    private static final String DOLLAR_VARIABLE = "DollarVariable";

    private static Configuration DEFAULT_FREEMARKER_CONFIGURATION = null;

    private FreemarkerDocumentFormatter formatter = new FreemarkerDocumentFormatter();

    private Configuration freemarkerConfiguration = null;

    private boolean forceModifyReader = false;

    private final List<TemplateLoader> templateLoaders;

    public FreemarkerTemplateEngine()
    {
        this.templateLoaders = new ArrayList<TemplateLoader>();
        this.templateLoaders.add( new XDocReportEntryTemplateLoader( this ) );
    }

    public String getKind()
    {
        return TemplateEngineKind.Freemarker.name();
    }

    public String getId()
    {
        return ID_DISCOVERY;
    }

    public IContext createContext()
    {
        return new XDocFreemarkerContext();
    }

    // @Override
    public IContext createContext( Map<String, Object> contextMap )
    {
        return new XDocFreemarkerContext( contextMap );
    }

    @Override
    protected void processWithCache( String templateName, IContext context, Writer writer )
        throws XDocReportException, IOException
    {
        // Get template from cache.
        Template template = getFreemarkerConfiguration().getTemplate( templateName );
        // Merge template with Java model
        process( context, writer, template );
    }

    @Override
    protected void processNoCache( String templateName, IContext context, Reader reader, Writer writer )
        throws XDocReportException, IOException
    {
        // Create a new template.
        Template template = new Template( templateName, getReader( reader ), getFreemarkerConfiguration() );
        // Merge template with Java model
        process( context, writer, template );
    }

    /**
     * Returns Reader to use for process template merge.
     * 
     * @param reader
     * @return
     * @throws IOException
     */
    private Reader getReader( Reader reader )
        throws IOException
    {
        if ( forceModifyReader && isEscapeTemplate() )
        {
            // reader must be modify and escape must be done (add [#escape... at
            // first of the template
            StringBuilder newTemplate = new StringBuilder( formatter.getStartDocumentDirective() );
            String oldTemplate = IOUtils.toString( reader );
            newTemplate.append( oldTemplate );
            newTemplate.append( formatter.getEndDocumentDirective() );
            return new StringReader( newTemplate.toString() );
        }
        return reader;
    }

    /**
     * Merge template with Java model.
     * 
     * @param context
     * @param writer
     * @param template
     * @throws IOException
     * @throws XDocReportException
     */
    private void process( IContext context, Writer writer, Template template )
        throws IOException, XDocReportException
    {
        try
        {
            Environment environment = template.createProcessingEnvironment( context, writer );
            environment.process();
        }
        catch ( TemplateException e )
        {
            throw new XDocReportException( e );
        }
    }

    public Configuration getFreemarkerConfiguration()
    {
        if ( freemarkerConfiguration == null )
        {
            return getDefaultConfiguration();
        }
        return freemarkerConfiguration;
    }

    public void setFreemarkerConfiguration( Configuration freemarkerConfiguration )
    {
        this.freemarkerConfiguration = freemarkerConfiguration;
        // Force square bracket syntax to write [#list instead of <#list.
        // Square bracket is used because <#list is not well XML.
        this.freemarkerConfiguration.setTagSyntax( Configuration.SQUARE_BRACKET_TAG_SYNTAX );
        // Force template loader with XDocReportEntryLoader to use
        // XDocReportRegistry.
        this.freemarkerConfiguration.setTemplateLoader( new MultiTemplateLoader(
                                                                                 templateLoaders.toArray( new TemplateLoader[0] ) ) );
        // as soon as report changes when source (odt, docx,...) change,
        // template entry must be refreshed.
        try
        {
            this.freemarkerConfiguration.setSetting( Configuration.TEMPLATE_UPDATE_DELAY_KEY, "0" );
        }
        catch ( TemplateException e )
        {
        }
        this.freemarkerConfiguration.setLocalizedLookup( false );
    }

    public void extractFields( Reader reader, String entryName, FieldsExtractor extractor )
        throws XDocReportException
    {
        try
        {
            Template template = new Template( entryName, reader, getFreemarkerConfiguration() );
            TemplateElement templateElement = template.getRootTreeNode();
            extractVariables( templateElement, extractor );
            templateElement.getChildNodes();
        }
        catch ( IOException e )
        {
            throw new XDocReportException( e );
        }
        catch ( TemplateModelException e )
        {
            throw new XDocReportException( e );
        }
    }

    private void extractVariables( TemplateElement templateElement, FieldsExtractor extractor )
        throws TemplateModelException
    {
        if ( DOLLAR_VARIABLE.equals( templateElement.getClass().getSimpleName() ) )
        {
            String fieldName = templateElement.getCanonicalForm();
            fieldName = fieldName.substring( 2, fieldName.length() - 1 );
            extractor.addFieldName( fieldName, false );
        }
        Enumeration<TemplateElement> enums = templateElement.children();
        while ( enums.hasMoreElements() )
        {
            TemplateElement element = (TemplateElement) enums.nextElement();
            extractVariables( element, extractor );
        }
    }

    public IDocumentFormatter getDocumentFormatter()
    {
        return formatter;
    }

    /**
     * Get the default Freemarker configuration
     * 
     * @return
     */
    private Configuration getDefaultConfiguration()
    {
        if ( DEFAULT_FREEMARKER_CONFIGURATION == null )
        {
            DEFAULT_FREEMARKER_CONFIGURATION = new Configuration();
            DEFAULT_FREEMARKER_CONFIGURATION.setDefaultEncoding( EncodingConstants.UTF_8.name() );
            DEFAULT_FREEMARKER_CONFIGURATION.setOutputEncoding( EncodingConstants.UTF_8.name() );
            DEFAULT_FREEMARKER_CONFIGURATION.setObjectWrapper( new DefaultObjectWrapper() );
            setFreemarkerConfiguration( DEFAULT_FREEMARKER_CONFIGURATION );
        }
        return DEFAULT_FREEMARKER_CONFIGURATION;
    }

    @Override
    public void setConfiguration( ITemplateEngineConfiguration configuration )
    {
        super.setConfiguration( configuration );
        if ( isEscapeTemplate() )
        {
            formatter.setConfiguration( configuration );
        }
    }

    private boolean isEscapeTemplate()
    {
        return getConfiguration() != null
            && ( getConfiguration().escapeXML() || ( getConfiguration().getReplacment() != null && getConfiguration().getReplacment().size() > 0 ) );
    }

    public void setForceModifyReader( boolean forceModifyReader )
    {
        this.forceModifyReader = forceModifyReader;
    }

    public boolean isForceModifyReader()
    {
        return forceModifyReader;
    }

    public void process( String templateName, IContext context, Writer writer )
        throws IOException, XDocReportException
    {
        // TODO : Improve it, cache the JavaMainDump.ftl
        templateName = templateName + ".ftl";
        Reader reader = new InputStreamReader( FreemarkerTemplateEngine.class.getResourceAsStream( templateName ) );

        try
        {
            // Create a new template.
            Template template = new Template( templateName, reader, getFreemarkerConfiguration() );
            // Merge template with Java model
            process( context, writer, template );
        }
        finally
        {
            if ( reader != null )
            {
                IOUtils.closeQuietly( reader );
            }
        }
    }

    // @Override
    public boolean isFieldNameStartsWithUpperCase()
    {
        return true;
    }

    /**
     * Add a Freemarker template loader.
     * 
     * @param loader the freemarker template loader to add.
     */
    public void addTemplateLoader( TemplateLoader loader )
    {
        templateLoaders.add( loader );
    }
}
