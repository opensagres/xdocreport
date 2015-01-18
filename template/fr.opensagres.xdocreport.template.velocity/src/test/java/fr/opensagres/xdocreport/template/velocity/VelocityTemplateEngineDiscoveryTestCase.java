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
package fr.opensagres.xdocreport.template.velocity;

import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.discovery.ITemplateEngineDiscovery;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

public class VelocityTemplateEngineDiscoveryTestCase
    extends TestCase
{

    @Test
    public void testTemplateEngineNoExists()
        throws Exception
    {
        String kind = "Unknown";
        Map<String, ITemplateEngineDiscovery> templateEnginesDiscoveryCache =
            TemplateEngineRegistry.getRegistry().getTemplateEnginesDiscoveryCache();
        Assert.assertEquals( 1, templateEnginesDiscoveryCache.values().size() );
        ITemplateEngineDiscovery discovery = templateEnginesDiscoveryCache.get( kind );
        Assert.assertNull( "ITemplateEngine with id='Unknown' must be null.", discovery );
    }

    @Test
    public void testGetTemplateEngine()
    {
        TemplateEngineKind kind = TemplateEngineKind.Velocity;
        Map<String, ITemplateEngineDiscovery> templateEnginesDiscoveryCache =
            TemplateEngineRegistry.getRegistry().getTemplateEnginesDiscoveryCache();
        Assert.assertEquals( 1, templateEnginesDiscoveryCache.values().size() );
        ITemplateEngineDiscovery discovery = templateEnginesDiscoveryCache.get( kind.name() );
        Assert.assertNotNull( "Cannot retrieve template engine discovery with kind=" + kind.name(), discovery );
        ITemplateEngine templateEngine = discovery.createTemplateEngine();
        Assert.assertNotNull( "Cannot retrieve template engine with kind=" + kind.name(), templateEngine );
    }
}
