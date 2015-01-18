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
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityDocumentFormatter;

public class ODTPreprocessorTextInputWithVelocityTestCase
    extends TestCase
{

    public void testInterpolationWithSimpleQuote()
        throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
                + "<text:p text:style-name=\"Table_20_Contents\">"
                + "<text:text-input text:description=\"\">${doc[&apos;dc:title&apos;]}</text:text-input>" + "</text:p>"
                + "</office:document-content>", "UTF-8" );
        StringWriter writer = new StringWriter();

        IDocumentFormatter formatter = new VelocityDocumentFormatter();
        preprocessor.preprocess( "test", stream, writer, null, formatter, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
            + "<text:p text:style-name=\"Table_20_Contents\">"
            // + "<text:text-input text:description=\"\">${doc[&apos;dc:title&apos;]}</text:text-input>"
            + "${doc['dc:title']}" + "</text:p>" + "</office:document-content>", writer.toString() );
    }

    public void testInterpolationWithDoubleQuote()
        throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
                + "<text:p text:style-name=\"Table_20_Contents\">"
                + "<text:text-input text:description=\"\">${doc[&quot;dc:title&quot;]}</text:text-input>" + "</text:p>"
                + "</office:document-content>", "UTF-8" );
        StringWriter writer = new StringWriter();

        IDocumentFormatter formatter = new VelocityDocumentFormatter();
        preprocessor.preprocess( "test", stream, writer, null, formatter, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
            + "<text:p text:style-name=\"Table_20_Contents\">"
            // + "<text:text-input text:description=\"\">${doc[&quot;dc:title&quot;]}</text:text-input>"
            + "${doc[\"dc:title\"]}" + "</text:p>" + "</office:document-content>", writer.toString() );
    }

    public void testDirectiveWithSimpleQuote()
        throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream(
                              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                  + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                                  + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                                  + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
                                  + "<text:p text:style-name=\"Table_20_Contents\">"
                                  + "<text:text-input text:description=\"\">#foreach($subject in $doc[&apos;dc:subjects&apos;])</text:text-input>"
                                  + "</text:p>" + "</office:document-content>", "UTF-8" );
        StringWriter writer = new StringWriter();

        IDocumentFormatter formatter = new VelocityDocumentFormatter();
        preprocessor.preprocess( "test", stream, writer, null, formatter, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                          + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                          + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                          + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
                          + "<text:p text:style-name=\"Table_20_Contents\">"
                          // +
                          // "<text:text-input text:description=\"\">#foreach($subject in $doc[&apos;dc:subjects&apos;])</text:text-input>"
                          + "#foreach($subject in $doc['dc:subjects'])" + "</text:p>" + "</office:document-content>",
                      writer.toString() );
    }

    public void testDirectiveWithDoubleQuote()
        throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream(
                              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                  + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                                  + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                                  + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
                                  + "<text:p text:style-name=\"Table_20_Contents\">"
                                  + "<text:text-input text:description=\"\">#foreach($subject in $doc[&quot;dc:subjects&quot;])</text:text-input>"
                                  + "</text:p>" + "</office:document-content>", "UTF-8" );
        StringWriter writer = new StringWriter();

        IDocumentFormatter formatter = new VelocityDocumentFormatter();
        preprocessor.preprocess( "test", stream, writer, null, formatter, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                          + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                          + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                          + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
                          + "<text:p text:style-name=\"Table_20_Contents\">"
                          // +
                          // "<text:text-input text:description=\"\">#foreach($subject in $doc[&quot;dc:subjects&quot;])</text:text-input>"
                          + "#foreach($subject in $doc[\"dc:subjects\"])" + "</text:p>" + "</office:document-content>",
                      writer.toString() );
    }

}
