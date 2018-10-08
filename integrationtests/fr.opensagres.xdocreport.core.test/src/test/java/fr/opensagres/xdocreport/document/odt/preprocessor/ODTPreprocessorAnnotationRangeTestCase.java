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
 * Test JUnit for range-annotation parsing.
 *
 * <p>Created on 2018-07-06</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 */
public class ODTPreprocessorAnnotationRangeTestCase
    extends TestCase
{

    private static final String START_STOP_SIMPLE_RANGE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
            + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
            + "<office:body><office:text><text:sequence-decls>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/>"
            + "</text:sequence-decls>"
            + "<text:p text:style-name=\"Standard\">"
            +   "Lorem ipsum "
            +   "<office:annotation office:name=\"__Annotation__0_1510414244\">"
            +     "<dc:creator>nieznany</dc:creator>"
            +     "<dc:date>2018-07-06T14:58:51.977923708</dc:date>"
            +     "<text:p text:style-name=\"P1\">"
            +       "<text:span text:style-name=\"T1\">$replacement</text:span>"
            +     "</text:p>"
            +   "</office:annotation>"
            +   "dolor"
            +   "<office:annotation-end office:name=\"__Annotation__0_1510414244\"/>"
            +   " sit amet, consectetur adipiscing elit"
            + "</text:p>"
            + "</office:text></office:body></office:document-content>";

    public void testSimpleRangeAnnotation()
            throws Exception
    {
        final ODTPreprocessor preprocessor = new ODTPreprocessor();
        final InputStream stream =
                        IOUtils.toInputStream( START_STOP_SIMPLE_RANGE_XML, "UTF-8" );
        final StringWriter writer = new StringWriter();

        preprocessor.preprocess( "test", stream, writer, null, null, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        	+ "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
        	+ "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
        	+ "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
        	+ "xmlns:dc=\"http://purl.org/dc/elements/1.1/\"><office:body><office:text>"
        	+ "<text:sequence-decls><text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/>"
        	+ "</text:sequence-decls><text:p text:style-name=\"Standard\">Lorem ipsum "

        	+ "$replacement"

        	+ " sit amet, consectetur adipiscing elit</text:p></office:text></office:body>"
        	+ "</office:document-content>", writer.toString() );
    }

    private static final String START_STOP_BEFORE_AFTER_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
            + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
            + "<office:body><office:text><text:sequence-decls>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/>"
            + "</text:sequence-decls>"
            + "<text:p text:style-name=\"Standard\">"
            +   "Lorem ipsum "
            +   "<office:annotation office:name=\"__Annotation__0_1510414244\">"
            +     "<dc:creator>nieznany</dc:creator>"
            +     "<dc:date>2018-07-06T14:58:51</dc:date>"
            +     "<text:p text:style-name=\"P1\">"
            +       "<text:span text:style-name=\"T1\">$replacement</text:span>"
            +     "</text:p>"
            +     "<text:p text:style-name=\"P1\">"
            +     "<text:span text:style-name=\"T1\">"
            +       "@before#foreach($replacement in $list)</text:span></text:p>"
            +     "<text:p text:style-name=\"P1\">"
            +       "<text:span text:style-name=\"T1\">"
            +         "@after#end"
            +       "</text:span>"
            +     "</text:p>"
            +   "</office:annotation>"
            +   "dolor"
            +   "<office:annotation-end office:name=\"__Annotation__0_1510414244\"/>"
            +   " sit amet, consectetur adipiscing elit"
            + "</text:p>"
            + "</office:text></office:body></office:document-content>";

    public void testRangeBeforeAfterAnnotation()
            throws Exception
    {
        final ODTPreprocessor preprocessor = new ODTPreprocessor();
        final InputStream stream =
                        IOUtils.toInputStream( START_STOP_BEFORE_AFTER_XML, "UTF-8" );
        final StringWriter writer = new StringWriter();

        preprocessor.preprocess( "test", stream, writer, null, null, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        	+ "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
        	+ "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
        	+ "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
        	+ "xmlns:dc=\"http://purl.org/dc/elements/1.1/\"><office:body><office:text>"
        	+ "<text:sequence-decls><text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/>"
        	+ "</text:sequence-decls>"

        	+ "#foreach($replacement in $list)\n"

        	+ "<text:p text:style-name=\"Standard\">Lorem ipsum "

        	+ "$replacement\n"

        	+ " sit amet, consectetur adipiscing elit</text:p>"

        	+ "#end"

        	+ "</office:text></office:body>"
        	+ "</office:document-content>", writer.toString() );
    }

    private static final String START_STOP_ENCLOSED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
            + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
            + "<office:body><office:text><text:sequence-decls>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/>"
            + "</text:sequence-decls>"
            + "<text:p text:style-name=\"Standard\">"
            +   "Lorem ipsu"
            +   "<office:annotation office:name=\"__Annotation__4_1000540325\">"
            +     "<dc:creator>nieznany</dc:creator>"
            +     "<dc:date>2018-07-06T15:55:56.991618419</dc:date>"
            +     "<text:p text:style-name=\"P2\">"
            +       "<text:span text:style-name=\"T2\">$replacement</text:span>"
            +     "</text:p>"
            +   "</office:annotation>"
            +   "m "
            +   "<office:annotation office:name=\"__Annotation__5_1000540325\">"
            +     "<dc:creator>nieznany</dc:creator>"
            +     "<dc:date>2018-07-06T15:56:02.462353311</dc:date>"
            +     "<text:p text:style-name=\"P1\">"
            +       "<text:span text:style-name=\"T1\">$contentSkipped</text:span>"
            +     "</text:p>"
            +     "<text:p text:style-name=\"P1\">"
            +       "<text:span text:style-name=\"T1\"/>"
            +     "</text:p>"
            +   "</office:annotation>"
            +   "dolor"
            +   "<office:annotation-end office:name=\"__Annotation__5_1000540325\"/>"
            +   " sit "
            +   "<office:annotation-end office:name=\"__Annotation__4_1000540325\"/>"
            +   "amet, consectetur adipiscing elit"
            + "</text:p>"
            + "</office:text></office:body></office:document-content>";

    public void testRangeEncloseAnnotation()
            throws Exception
    {
        final ODTPreprocessor preprocessor = new ODTPreprocessor();
        final InputStream stream =
                        IOUtils.toInputStream( START_STOP_ENCLOSED_XML, "UTF-8" );
        final StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        	+ "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
        	+ "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
        	+ "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
        	+ "xmlns:dc=\"http://purl.org/dc/elements/1.1/\"><office:body><office:text>"
        	+ "<text:sequence-decls><text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/>"
        	+ "</text:sequence-decls>"
        	+ "<text:p text:style-name=\"Standard\">Lorem ipsu"

        	+ "$replacement"
        	+ "<dc:creator></dc:creator><dc:date></dc:date><text:p text:style-name=\"P1\"><text:span text:style-name=\"T1\">"
        	+ "</text:span></text:p><text:p text:style-name=\"P1\"><text:span text:style-name=\"T1\"/></text:p>"

        	+ "amet, consectetur adipiscing elit</text:p>"
        	+ "</office:text></office:body>"
        	+ "</office:document-content>", writer.toString() );
    }

    private static final String START_STOP_CROSSTAG_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
            + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
            + "<office:body><office:text><text:sequence-decls>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/>"
            + "</text:sequence-decls>"
            + "<text:p text:style-name=\"P2\">"
            +   "A"
            +   "<office:annotation office:name=\"__Annotation__62_1000540325\">"
            +     "<dc:creator>nieznany</dc:creator>"
            +     "<dc:date>2018-07-06T16:21:02.572291340</dc:date>"
            +     "<text:p text:style-name=\"P3\">"
            +       "<text:span text:style-name=\"T2\">$replacement</text:span>"
            +     "</text:p>"
            +   "</office:annotation>"
            +   "b"
            +   "<text:span text:style-name=\"T1\">c</text:span>"
            + "</text:p>"
            + "<text:p text:style-name=\"P2\">abd</text:p>"
            + "<text:p text:style-name=\"P2\"/>"
            + "<text:list xml:id=\"list8751044298719524852\" text:style-name=\"L1\">"
            +   "<text:list-item>"
            +     "<text:p text:style-name=\"P1\">"
            +       "ab"
            +       "<office:annotation-end office:name=\"__Annotation__62_1000540325\"/>"
            +       "c"
            +     "</text:p>"
            +   "</text:list-item>"
            +   "<text:list-item>"
            +     "<text:p text:style-name=\"P1\">"
            +       "abc"
            +     "</text:p>"
            +   "</text:list-item>"
            + "</text:list>"
            + "</office:text></office:body></office:document-content>";

    public void testRangeCrosstagAnnotation()
            throws Exception
    {
        final ODTPreprocessor preprocessor = new ODTPreprocessor();
        final InputStream stream =
                        IOUtils.toInputStream( START_STOP_CROSSTAG_XML, "UTF-8" );
        final StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        	+ "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
        	+ "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
        	+ "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
        	+ "xmlns:dc=\"http://purl.org/dc/elements/1.1/\"><office:body><office:text>"
        	+ "<text:sequence-decls><text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/>"
        	+ "</text:sequence-decls>"
        	+ "<text:p text:style-name=\"P2\">A"

        	+ "$replacement"

        	+ "<text:span text:style-name=\"T1\"></text:span></text:p><text:p text:style-name=\"P2\"></text:p>"
        	+ "<text:p text:style-name=\"P2\"/><text:list xml:id=\"list8751044298719524852\" text:style-name=\"L1\">"
        	+ "<text:list-item><text:p text:style-name=\"P1\">c</text:p></text:list-item><text:list-item>"
        	+ "<text:p text:style-name=\"P1\">abc</text:p></text:list-item></text:list>"
        	+ "</office:text></office:body>"
        	+ "</office:document-content>", writer.toString() );
    }

    private static final String START_STOP_ANNOTATIONS_SINGLE_ELEMENT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
            + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
            + "<office:body><office:text><text:sequence-decls>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/>"
            + "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/>"
            + "</text:sequence-decls>"
            + "<text:p text:style-name=\"Standard\">"
            +   "Lorem "
            +   "<text:span text:style-name=\"T3\">ipsum </text:span>"
            +   "<text:span text:style-name=\"T31\">"
            +     "<office:annotation office:name=\"__Annotation__500_1811748221\">"
            +       "<dc:creator>nieznany</dc:creator>"
            +       "<dc:date>2018-07-09T12:39:39.736349396</dc:date>"
            +       "<text:p text:style-name=\"P1\">"
            +         "<text:span text:style-name=\"T7\">test</text:span>"
            +       "</text:p>"
            +     "</office:annotation>"
            +   "</text:span>"
            +   "<text:span text:style-name=\"T3\">"
            +     "<text:span text:style-name=\"T44\">"
            +       "donor "
            +     "</text:span>"
            +   "</text:span>"
            +   "<text:span text:style-name=\"T5\">sit</text:span>"
            +   "<text:span text:style-name=\"T3\"> amet,</text:span>"
            +   "<office:annotation-end office:name=\"__Annotation__500_1811748221\"/>"
            +   "<text:span text:style-name=\"T3\"> </text:span>"
            +   "<text:span text:style-name=\"T4\"> consectetur</text:span>"
            +   "<text:span text:style-name=\"T6\"> adipiscing</text:span>"
            + "</text:p>"
            + "</office:text></office:body></office:document-content>";

    public void testRangeAnnotationSingleInContainer()
            throws Exception
    {
        final ODTPreprocessor preprocessor = new ODTPreprocessor();
        final InputStream stream =
                        IOUtils.toInputStream( START_STOP_ANNOTATIONS_SINGLE_ELEMENT_XML, "UTF-8" );
        final StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        	+ "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
        	+ "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
        	+ "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
        	+ "xmlns:dc=\"http://purl.org/dc/elements/1.1/\"><office:body><office:text>"
        	+ "<text:sequence-decls><text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/>"
        	+ "<text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/>"
        	+ "</text:sequence-decls>"
        	+ "<text:p text:style-name=\"Standard\">"
        	+   "Lorem "
        	+   "<text:span text:style-name=\"T3\">"
        	+     "ipsum "
        	+   "</text:span>"
        	+   "<text:span text:style-name=\"T31\"></text:span>"
        	+   "<text:span text:style-name=\"T3\">"
        	+     "<text:span text:style-name=\"T44\">"
        	+       "test"
        	+     "</text:span>"
        	+   "</text:span>"
        	+   "<text:span text:style-name=\"T5\"></text:span>"
        	+   "<text:span text:style-name=\"T3\"></text:span>"
        	+   "<text:span text:style-name=\"T3\"> </text:span>"
        	+   "<text:span text:style-name=\"T4\"> consectetur</text:span>"
        	+   "<text:span text:style-name=\"T6\"> adipiscing</text:span>"
        	+ "</text:p>"
        	+ "</office:text></office:body>"
        	+ "</office:document-content>", writer.toString() );
    }

}
