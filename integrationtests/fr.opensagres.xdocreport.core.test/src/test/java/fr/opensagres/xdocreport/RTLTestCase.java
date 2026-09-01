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
package fr.opensagres.xdocreport;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RTLTestCase extends TestCase {

    /**
     * Verifies that processing an RTL DOCX template preserves the substituted
     * Arabic and RTL content.
     *
     * <p>For manual verification of the generated document in Microsoft Word,
     * uncomment the call to {@code writeProcessedDocx(output)}. The processed
     * document will be written to {@code target/RTLTestCase-output.docx}, allowing
     * the RTL layout and formatting to be visually inspected.</p>
     */
    public void testProcessRTLDocx() throws Exception {

        final String customerName = "ديباك شارما";
        final String accountNumber = "888333666";
        final String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        try (InputStream input = RTLTestCase.class.getResourceAsStream("/RTLTestCase.docx")) {

            assertNotNull("RTLTestCase.docx not found.", input);

            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(input, TemplateEngineKind.Freemarker);

            IContext context = report.createContext();
            context.put("customer_name", customerName);
            context.put("account_number", accountNumber);
            context.put("date", currentDate);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            report.process(context, output);

            // writeProcessedDocx(output);

            XDocArchive archive = XDocArchive.readZip(new ByteArrayInputStream(output.toByteArray()));

            assertFalse("Archive should contain entries.", archive.getEntryNames().isEmpty());

            String document = IOUtils.toString(archive.getEntryReader("word/document.xml"));

            assertNotNull(document);
            assertTrue(document.contains(customerName));
            assertTrue(document.contains(accountNumber));
            assertTrue(document.contains(currentDate));
        }
    }

    /**
     * Writes the processed DOCX to the target directory for manual inspection.
     * This helper is intended only for debugging or visual verification of the
     * generated document.
     */
    private void writeProcessedDocx(ByteArrayOutputStream output) throws IOException {

        File outputFile = new File("target/RTLTestCase-output.docx");

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(output.toByteArray());
        }

        assertTrue(outputFile.exists());
    }
}
