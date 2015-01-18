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

public class ODTPreprocessorWithTableTestCase
    extends TestCase
{

    private static final String TABLE_INSIDE_ROW = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
        + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
        + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
        + "<table:table table:name=\"Table1\" table:style-name=\"Table1\">"
        + "<table:table-column table:style-name=\"Table1.A\"/>" + "<table:table-column table:style-name=\"Table1.B\"/>"
        + "<table:table-row>" + "<table:table-cell table:style-name=\"Table1.A1\" office:value-type=\"string\">"
        + "<text:p text:style-name=\"P4\"/>" + "</table:table-cell>"
        + "<table:table-cell table:style-name=\"Table1.A1\" office:value-type=\"string\">"
        + "<table:table table:name=\"Table2\" table:style-name=\"Table2\">"
        + "<table:table-column table:style-name=\"Table2.A\"/>" + "<table:table-row>"
        + "<table:table-cell table:style-name=\"Table2.A1\" office:value-type=\"string\">"
        + "<text:p text:style-name=\"P3\">This table is causing problems" + "</text:p>" + "</table:table-cell>"
        + "</table:table-row>" + "</table:table>" + "<text:p text:style-name=\"P3\"/>" + "</table:table-cell>"
        + "</table:table-row>" + "</table:table>" + "</office:document-content>";

    public void testNestedTable()
        throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( TABLE_INSIDE_ROW, "UTF-8" );

        StringWriter writer = new StringWriter();

        preprocessor.preprocess( "test", stream, writer, null, null, null );

        assertEquals( TABLE_INSIDE_ROW, writer.toString() );
    }
}
