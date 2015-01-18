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

import org.apache.velocity.runtime.RuntimeConstants;

import fr.opensagres.xdocreport.core.utils.Assert;
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
        return new VelocityTemplateEngine( getVelocityEngineProperties( velocityDefaultProperties ) );
    }

    /**
     * Returns the {@link Properties} used to initialize Velocity Engine.
     * 
     * @return
     */
    private synchronized Properties getVelocityEngineProperties( Properties velocityDefaultProperties )
    {

        Properties velocityEngineProperties = new Properties();

        if ( velocityDefaultProperties != null )
        {
            // Use custom velocity.properties or default xdocreport-velocity.properties.
            velocityEngineProperties.putAll( velocityDefaultProperties );
        }

        // check if XDocReportEntryResourceLoader is defined?
        boolean hasReportLoaderDefined = velocityEngineProperties.containsKey( "report.resource.loader.class" );
        if ( !hasReportLoaderDefined )
        {
            // report.resource.loader.class is not defined, defines it.

            // Initialize properties to use XDocReportEntryResourceLoader to
            // load template from entry name of XDocArchive.
            velocityEngineProperties.setProperty( "resource.loader", "file, class, jar ,report" );
            velocityEngineProperties.setProperty( "report.resource.loader.class",
                                                  XDocReportEntryResourceLoader.class.getName() );
            velocityEngineProperties.setProperty( "report.resource.loader.cache", "true" );
            velocityEngineProperties.setProperty( "report.resource.loader.modificationCheckInterval", "1" );

            // Disable log for Velocity to avoid to generate velocity.log (by
            // default)
            try
            {
                if ( Class.forName( "org.apache.velocity.runtime.log.NullLogChute" ) != null )
                {
                    // Don't crash Velocity if NullLogChute doesn't exist
                    velocityEngineProperties.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
                                                          "org.apache.velocity.runtime.log.NullLogChute" );
                }
            }
            catch ( Throwable e )
            {
                // Do nothing
            }
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
        if ( is == null )
        {
            // custom velocity properties cannot be loaded, load xdocreport-velocity.properties
            is = classLoader.getResourceAsStream( "xdocreport-velocity.properties" );
        }
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
                return null;
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
