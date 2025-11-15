package fr.opensagres.xdocreport.template.freemarker;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.IContext;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Test case to verify security fixes for Server-Side Template Injection (SSTI) vulnerabilities
 * in the FreeMarker template engine.
 */
public class FreemarkerTemplateEngineSecurityTestCase
    extends TestCase
{

    /**
     * Test that legitimate template operations still work after security fix.
     */
    public void testLegitimateTemplateOperationsStillWork()
        throws Exception
    {
        FreemarkerTemplateEngine templateEngine = new FreemarkerTemplateEngine();
        
        // Test basic variable substitution
        String legitimateTemplate = "Hello ${name}!";
        Reader reader = new StringReader( legitimateTemplate );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();
        context.put( "name", "World" );

        templateEngine.process( "", context, reader, writer );
        assertEquals( "Hello World!", writer.toString() );
    }


    /**
     * Test protection against SSTI payload with freemarker.template.utility.Execute.
     */
    public void testSSTIProtectionAgainstExecutePayload()
        throws Exception
    {
        FreemarkerTemplateEngine templateEngine = new FreemarkerTemplateEngine();
        
        String maliciousTemplate ="${\"freemarker.template.utility.Execute\"?new()(\"whoami\")}";
        Reader reader = new StringReader( maliciousTemplate );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();

        try
        {
            templateEngine.process( "", context, reader, writer );
            fail( "Security fix failed: Execute payload should be blocked" );
        }
        catch ( XDocReportException e )
        {
            assertTrue( "Expected security-related exception", 
                       e.getCause() instanceof TemplateException );
        }
    }
}

