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
package fr.opensagres.xdocreport.document.odt.preprocessor;

import java.io.InputStream;
import java.io.StringWriter;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.core.io.IOUtils;

public class ODTPreprocessorStartStopRowTestCase
    extends TestCase
{

    private static final String START_STOP_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
        + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
        + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
        + "<table:table table:name=\"Tableau1\" table:style-name=\"Tableau1\">"
        + "<table:table-column table:style-name=\"Tableau1.A\"/>" + "<table:table-row>"
        + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
        + "<text:p text:style-name=\"Table_20_Contents\">"
        + "<text:text-input text:description=\"\">@before-rowAAAAAAAAAA</text:text-input>"
        + "<text:text-input text:description=\"\">@after-rowZZZZZZZZZZ</text:text-input>" + "</text:p>"
        + "</table:table-cell>" + "</table:table-row>" + "</table:table>" + "</office:document-content>";

    public void testNestedTable()
        throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( START_STOP_XML, "UTF-8" );
        StringWriter writer = new StringWriter();

        preprocessor.preprocess( "test", stream, writer, null, null, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
            + "<table:table table:name=\"Tableau1\" table:style-name=\"Tableau1\">"
            + "<table:table-column table:style-name=\"Tableau1.A\"/>"

            + "AAAAAAAAAA"

            + "<table:table-row>" + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
            + "<text:p text:style-name=\"Table_20_Contents\">"
            // + "<text:text-input text:description=\"\">@before-rowAAAAAAAAAA</text:text-input>"
            // + "<text:text-input text:description=\"\">@after-rowZZZZZZZZZZ</text:text-input>"
            + "</text:p>" + "</table:table-cell>" + "</table:table-row>"

            + "ZZZZZZZZZZ"

            + "</table:table>" + "</office:document-content>", writer.toString() );
    }
}
