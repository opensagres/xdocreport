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
package fr.opensagres.xdocreport.document.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.discovery.ITemplateEngineInitializerDiscovery;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.discovery.ITemplateEngineDiscovery;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

public class TemplateEngineInitializerRegistry
    extends AbstractRegistry<ITemplateEngineInitializerDiscovery>
{

    private static final TemplateEngineInitializerRegistry INSTANCE = new TemplateEngineInitializerRegistry();

    public TemplateEngineInitializerRegistry()
    {
        super( ITemplateEngineInitializerDiscovery.class );
    }

    private Map<String, ITemplateEngine> templateEnginesCache = new HashMap<String, ITemplateEngine>();

    private Map<String, ITemplateEngineInitializerDiscovery> templateEnginesInitializerDiscoveryCache =
        new HashMap<String, ITemplateEngineInitializerDiscovery>();

    @Override
    protected boolean registerInstance( ITemplateEngineInitializerDiscovery instance )
    {
        ITemplateEngineInitializerDiscovery discovery = (ITemplateEngineInitializerDiscovery) instance;
        templateEnginesInitializerDiscoveryCache.put( discovery.getId(), discovery );
        return true;
    }

    public static TemplateEngineInitializerRegistry getRegistry()
    {
        return INSTANCE;
    }

    @Override
    protected void doDispose()
    {
        this.templateEnginesCache.clear();
        this.templateEnginesInitializerDiscoveryCache.clear();
    }

    public ITemplateEngine getTemplateEngine( TemplateEngineKind templateEngineKind, DocumentKind documentKind )
    {
        return getTemplateEngine( templateEngineKind.name(), documentKind != null ? documentKind.name() : null );
    }

    public ITemplateEngine getTemplateEngine( String templateEngineKind, String documentKind )
    {
        initializeIfNeeded();
        String key = getKey( templateEngineKind, documentKind );
        return templateEnginesCache.get( key );
    }

    public boolean exists( TemplateEngineKind templateEngineKind, DocumentKind documentKind )
    {
        return exists( templateEngineKind.name(), documentKind != null ? documentKind.name() : null );
    }

    public boolean exists( String templateEngineKind, String documentKind )
    {
        initializeIfNeeded();
        String key = getKey( templateEngineKind, documentKind );
        return templateEnginesCache.containsKey( key );
    }

    private String getKey( String templateEngineKind, String documentKind )
    {
        StringBuilder key = new StringBuilder( templateEngineKind );
        if ( StringUtils.isNotEmpty( documentKind ) )
        {
            key.append( '_' );
            key.append( documentKind );
        }
        return key.toString();
    }

    public ITemplateEngine getTemplateEngine( String templateEngineId )
    {
        initializeIfNeeded();
        String templateEngineKind = templateEngineId;
        String documentKind = null;
        int index = templateEngineId.lastIndexOf( '_' );
        if ( index != -1 )
        {
            templateEngineKind = templateEngineId.substring( 0, index );
            documentKind = templateEngineId.substring( index + 1, templateEngineId.length() );
        }
        return getTemplateEngine( templateEngineKind, documentKind );
    }

    public void register( ITemplateEngine templateEngine, DocumentKind documentKind )
    {
        register( templateEngine, documentKind.name() );
    }

    private void register( ITemplateEngine templateEngine, String documentKind )
    {
        String templateEngineKind = templateEngine.getKind();
        String key = getKey( templateEngineKind, documentKind );
        templateEnginesCache.put( key, templateEngine );
        ITemplateEngine defaultTemplateEngine = TemplateEngineRegistry.getRegistry().getDefaultTemplateEngine();
        if ( defaultTemplateEngine == null )
        {
            defaultTemplateEngine = templateEngine;
        }
        Collection<String> templateEngineKinds = TemplateEngineRegistry.getRegistry().getTemplateEngineKinds();
        if ( !templateEngineKinds.contains( templateEngineKind ) )
        {
            templateEngineKinds.add( templateEngineKind );
        }
    }

    @Override
    protected void onEndInitialization()
    {
        // When registry end initialisation, loop for each initializer to
        // initialize each template engine.
        Collection<ITemplateEngineInitializerDiscovery> discoveriesForWholeTemplateEngine =
            new ArrayList<ITemplateEngineInitializerDiscovery>();
        Collection<ITemplateEngineInitializerDiscovery> discoveries = templateEnginesInitializerDiscoveryCache.values();
        String documentKind = null;
        for ( ITemplateEngineInitializerDiscovery discovery : discoveries )
        {
            documentKind = discovery.getDocumentKind();
            if ( StringUtils.isEmpty( documentKind ) )
            {
                discoveriesForWholeTemplateEngine.add( discovery );
            }
            else
            {
                Collection<String> kinds = TemplateEngineRegistry.getRegistry().getTemplateEngineKinds();
                Map<String, ITemplateEngineDiscovery> templateEnginesDiscoveryCache =
                    TemplateEngineRegistry.getRegistry().getTemplateEnginesDiscoveryCache();
                for ( String templateEngineKind : kinds )
                {
                    ITemplateEngineDiscovery templateEngineDiscovery =
                        templateEnginesDiscoveryCache.get( templateEngineKind );
                    ITemplateEngine templateEngine = templateEnginesCache.get( templateEngineKind );
                    if ( templateEngine == null )
                    {
                        // Create template engine for template/document kind
                        templateEngine = templateEngineDiscovery.createTemplateEngine();
                        register( templateEngine, documentKind );
                    }
                    discovery.initialize( templateEngine );
                }
            }
        }

        for ( ITemplateEngineInitializerDiscovery discovery : discoveriesForWholeTemplateEngine )
        {
            Collection<ITemplateEngine> templateEngines = templateEnginesCache.values();
            for ( ITemplateEngine templateEngine : templateEngines )
            {
                discovery.initialize( templateEngine );
            }
        }

    }

}
