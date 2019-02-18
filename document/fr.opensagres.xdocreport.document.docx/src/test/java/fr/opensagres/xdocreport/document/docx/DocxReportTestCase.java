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
