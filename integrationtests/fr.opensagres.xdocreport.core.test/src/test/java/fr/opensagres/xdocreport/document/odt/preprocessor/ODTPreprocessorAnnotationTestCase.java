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

import fr.opensagres.xdocreport.core.io.IOUtils;
import junit.framework.TestCase;

/**
 * Test JUnit for point-annotation parsing.
 *
 * <p>Created on 2018-07-06</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 */
public class ODTPreprocessorAnnotationTestCase
    extends TestCase
{

    private static final String START_STOP_TABLE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
        + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
        + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
        + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
        + "<table:table table:name=\"Tableau1\" table:style-name=\"Tableau1\">"
        + "<table:table-column table:style-name=\"Tableau1.A\"/>" + "<table:table-row>"
        + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
        + "<text:p text:style-name=\"Table_20_Contents\">"
        + "<text:text-input text:description=\"\">$i.Field</text:text-input>"
        + "<office:annotation><dc:creator>unknown</dc:creator><dc:date>2018-07-05T15:09:04.894857589</dc:date>"
        + "<text:p text:style-name=\"P1\"><text:span text:style-name=\"T1\">$abc@before#foreach($i in $list)</text:span></text:p>"
        + "<text:p text:style-name=\"P1\"><text:span text:style-name=\"T1\">@after#end</text:span></text:p></office:annotation></text:p>"
        + "</table:table-cell>" + "</table:table-row>" + "</table:table>" + "</office:document-content>";

    private static final String START_STOP_LIST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
        + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
        + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
        + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
        + "<text:list xml:id=\"list4194995166531986203\" text:style-name=\"L1\">"
        + "<text:list-item><text:p text:style-name=\"P6\"><text:span text:style-name=\"T1\">"
        + "<office:annotation><dc:creator>unknown</dc:creator>"
        + "<dc:date>2018-07-05T15:09:04.894857589</dc:date>"
        + "<text:p text:style-name=\"P1\"><text:span text:style-name=\"T1\">$abc@before#foreach($i in $list)</text:span></text:p>"
        + "<text:p text:style-name=\"P1\"><text:span text:style-name=\"T1\">@after#end</text:span></text:p></office:annotation>"
        + "</text:span><text:span text:style-name=\"T1\">abc</text:span></text:p></text:list-item></text:list>"
        + "</office:document-content>";

    private static final String START_STOP_SECTION_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
        + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
        + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
        + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"

        + "<text:section text:style-name=\"Sect1\" text:name=\"Sekcja1\">"
        + "<text:p text:style-name=\"P3\"><text:span text:style-name=\"T9\">testtest</text:span><text:span text:style-name=\"T6\">"
        + "<office:annotation><dc:creator>unknown</dc:creator>"
        + "<dc:date>2018-07-05T15:09:04.894857589</dc:date>"
        + "<text:p text:style-name=\"P1\"><text:span text:style-name=\"T1\">$abc@before#foreach($i in $list)</text:span></text:p>"
        + "<text:p text:style-name=\"P1\"><text:span text:style-name=\"T1\">@after#end</text:span></text:p></office:annotation>"
        + "</text:span></text:p>"
        + "</text:section>"
        + "</office:document-content>";

    public void testNestedTable()
        throws Exception
    {
        final ODTPreprocessor preprocessor = new ODTPreprocessor();
        final InputStream stream =
                        IOUtils.toInputStream( START_STOP_TABLE_XML, "UTF-8" );
        final StringWriter writer = new StringWriter();

        preprocessor.preprocess( "test", stream, writer, null, null, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
            + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\"><table:table table:name=\"Tableau1\" "
            + "table:style-name=\"Tableau1\"><table:table-column table:style-name=\"Tableau1.A\"/>"

            + "#foreach($i in $list)\n"

            + "<table:table-row><table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
            + "<text:p text:style-name=\"Table_20_Contents\">$i.Field"

            + "$abc"

            + "</text:p></table:table-cell></table:table-row>"

            + "#end"

            + "</table:table></office:document-content>", writer.toString() );
    }

    public void testNestedList()
        throws Exception
    {
        final ODTPreprocessor preprocessor = new ODTPreprocessor();
        final InputStream stream =
                        IOUtils.toInputStream( START_STOP_LIST_XML, "UTF-8" );
        final StringWriter writer = new StringWriter();

        preprocessor.preprocess( "test", stream, writer, null, null, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
            + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
            + "<text:list xml:id=\"list4194995166531986203\" text:style-name=\"L1\">"

            + "#foreach($i in $list)\n"

            + "<text:list-item><text:p text:style-name=\"P6\"><text:span text:style-name=\"T1\">"

            + "$abc"

            + "</text:span><text:span text:style-name=\"T1\">abc</text:span></text:p></text:list-item>"

            + "#end"

            + "</text:list></office:document-content>", writer.toString() );
    }


    public void testNestedSection()
            throws Exception
    {
        final ODTPreprocessor preprocessor = new ODTPreprocessor();
        final InputStream stream =
                        IOUtils.toInputStream( START_STOP_SECTION_XML, "UTF-8" );
        final StringWriter writer = new StringWriter();

        preprocessor.preprocess( "test", stream, writer, null, null, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "

            + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
            + "#foreach($i in $list)\n"

            + "<text:section text:style-name=\"Sect1\" text:name=\"Sekcja1\"><text:p "
            + "text:style-name=\"P3\"><text:span text:style-name=\"T9\">testtest</text:span>"
            + "<text:span text:style-name=\"T6\">"

            + "$abc"

            + "</text:span></text:p></text:section>"

            + "#end"

            + "</office:document-content>", writer.toString() );
    }

}
