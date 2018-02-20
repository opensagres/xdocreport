package fr.opensagres.xdocreport.document.docx;

import static org.easymock.EasyMock.*;

import java.io.InputStream;

import org.junit.Test;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.docx.discovery.DocXReportFactoryDiscoveryTestCase;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import junit.framework.TestCase;

public class DocxReportTestCase extends TestCase
{
    @Test
    public void testPreprocess() throws Exception
    {
        InputStream is = DocXReportFactoryDiscoveryTestCase.class.getResourceAsStream( "DocxHelloWordWithFreemarker.docx" );
        try {
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport( is, true );
            
            assertFalse("Report should not yet be preprocessed", report.isPreprocessed());
            assertNull("Original document archive should be null, since cacheOriginalDocument is false", report.getOriginalDocumentArchive());
            assertNotNull("Preprocessed document archive should be set", report.getPreprocessedDocumentArchive());
            
            // we need to set some template engine to be able to preprocess
            report.setTemplateEngine(createTemplateEngine());
            
            report.preprocess();
            
            assertTrue("Report should be preprocessed now", report.isPreprocessed());
            assertNull("Original archive should still be null", report.getOriginalDocumentArchive());
            assertNotNull("Preprocessed document archive should still be set", report.getPreprocessedDocumentArchive());
        }
        finally
        {
            is.close();
        }
    }

    private ITemplateEngine createTemplateEngine()
    {
        IDocumentFormatter documentFormatter = createNiceMock(IDocumentFormatter.class);
        replay(documentFormatter);

        ITemplateEngine templateEngine = createNiceMock(ITemplateEngine.class);
        expect(templateEngine.getDocumentFormatter()).andStubReturn(documentFormatter);
        replay(templateEngine);
        return templateEngine;
    }
}
