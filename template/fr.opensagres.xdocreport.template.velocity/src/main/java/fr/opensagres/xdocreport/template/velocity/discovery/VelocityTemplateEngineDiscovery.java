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
package fr.opensagres.xdocreport.template.velocity.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.discovery.ITemplateEngineDiscovery;
import fr.opensagres.xdocreport.template.velocity.VelocityConstants;
import fr.opensagres.xdocreport.template.velocity.cache.XDocReportEntryResourceLoader;
import fr.opensagres.xdocreport.template.velocity.internal.VelocityTemplateEngine;

/**
 * Velocity template engine discovery used to returns {@link VelocityTemplateEngine}.
 */
public class VelocityTemplateEngineDiscovery
    implements ITemplateEngineDiscovery, VelocityConstants
{

    public ITemplateEngine createTemplateEngine()
    {
        Properties velocityDefaultProperties = getVelocityDefaultProperties();
        Properties xDocReportDefaultProperties = getXDocReportDefaultProperties();
        return new VelocityTemplateEngine( getVelocityEngineProperties( velocityDefaultProperties, xDocReportDefaultProperties ) );
    }

    /**
     * Returns the {@link Properties} used to initialize Velocity Engine.
     * 
     * @return
     */
    private synchronized Properties getVelocityEngineProperties( Properties velocityDefaultProperties, Properties xDocReportDefaultProperties )
    {
        Properties velocityEngineProperties = new Properties();

        if ( velocityDefaultProperties != null )
        {
            // Use custom velocity.properties.
            velocityEngineProperties.putAll( velocityDefaultProperties );
        }

        // check if XDocReportEntryResourceLoader is defined?
        boolean hasReportLoaderDefined = velocityEngineProperties.containsKey( "resource.loader.report.class" );
        if ( !hasReportLoaderDefined )
        {
            // resource.loader.report.class is not defined, defines it.

            // Initialize properties to use XDocReportEntryResourceLoader to
            // load template from entry name of XDocArchive.
            velocityEngineProperties.setProperty( "resource.loaders", "file, class, jar ,report" );
            velocityEngineProperties.setProperty( "resource.loader.report.class",
                                                  XDocReportEntryResourceLoader.class.getName() );
            velocityEngineProperties.setProperty( "resource.loader.report.cache", "true" );
            velocityEngineProperties.setProperty( "resource.loader.file.modification_check_interval", "1" );
        }

        // Set properties to maximize backward compatibility of previous XDoxReports Velocity 1 templates with Velocity 2:

        // No automatic conversion of methods arguments
        velocityEngineProperties.setProperty( "introspector.conversion_handler.class", "none" );
        // Use backward compatible space gobbling
        velocityEngineProperties.setProperty( "parser.space_gobbling", "bc" );
        // Have #if($foo) only returns false if $foo is false or null
        velocityEngineProperties.setProperty( "directive.if.empty_check", "false" );
        // Allow '-' in identifiers
        velocityEngineProperties.setProperty( "parser.allow_hyphen_in_identifiers", "true" );
        // Enable backward compatibility mode for Velocimacros
        velocityEngineProperties.setProperty( "velocimacro.enable_bc_mode", "true" );
        // When using an invalid reference handler, also include quiet references
        velocityEngineProperties.setProperty( "event_handler.invalid_references.quiet", "true" );
        // When using an invalid reference handler, also include null references
        velocityEngineProperties.setProperty( "event_handler.invalid_references.null", "true" );
        // When using an invalid reference handler, also include tested references
        velocityEngineProperties.setProperty( "event_handler.invalid_references.tested", "true" );

        if ( xDocReportDefaultProperties != null )
        {
            // Use custom xdocreport-velocity.properties.
            velocityEngineProperties.putAll( xDocReportDefaultProperties );
        }

        return velocityEngineProperties;
    }

    /**
     * Reads 'velocity.properties' from classpath
     * 
     * @return <code>Properties</code> loaded or <code>null</code> if is not found
     */
    private synchronized Properties getVelocityDefaultProperties()
    {
        ClassLoader classLoader = this.getClass().getClassLoader();
        // try to load custom velocity.properties.
        InputStream is = classLoader.getResourceAsStream( "velocity.properties" );
        if ( is != null )
        {
            try
            {
                Properties p = new Properties();
                p.load( is );
                return p;
            }
            catch ( IOException e )
            {

            }
        }
        return null;
    }

    /**
     * Reads 'velocity.properties' from classpath
     * 
     * @return <code>Properties</code> loaded or <code>null</code> if is not found
     */
    private synchronized Properties getXDocReportDefaultProperties()
    {
        ClassLoader classLoader = this.getClass().getClassLoader();
        // try to load xdocreport-velocity.properties
        InputStream is = classLoader.getResourceAsStream( "xdocreport-velocity.properties" );
        if ( is != null )
        {
            try
            {
                Properties p = new Properties();
                p.load( is );
                return p;
            }
            catch ( IOException e )
            {

            }
        }
        return null;
    }

    public String getDescription()
    {
        return DESCRIPTION_DISCOVERY;
    }

    public String getId()
    {
        return TemplateEngineKind.Velocity.name();
    }
}
