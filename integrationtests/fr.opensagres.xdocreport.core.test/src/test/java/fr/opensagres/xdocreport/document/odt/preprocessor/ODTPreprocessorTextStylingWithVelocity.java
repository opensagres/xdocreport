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
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityDocumentFormatter;

public class ODTPreprocessorTextStylingWithVelocity

{

    @Test
    public void textStylingWithSimpleField()
                    throws Exception
                {
                    ODTPreprocessor preprocessor = new ODTPreprocessor();
                    InputStream stream =
                                    IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                                    + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                                                    + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                                                    + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
                                                    +"<table:table table:name=\"Compras\" table:style-name=\"Compras\">"
                                                    +"<table:table-column table:style-name=\"Compras.A\"/>"
                                                    +"<table:table-header-rows>"
                                                        +"<table:table-row>"
                                                            +"<table:table-cell table:style-name=\"Compras.A1\" office:value-type=\"string\">"
                                                                +"<text:p text:style-name=\"P3\">"
                                                                    +"<text:text-input text:description=\"\">$row.html</text:text-input>"
                                                                +"</text:p>"
                                                            +"</table:table-cell>"
                                                        +"</table:table-row>"
                                                    +"</table:table-header-rows>"
                                                +"</table:table>"
                                                + "</office:document-content>", "UTF-8" );
                    StringWriter writer = new StringWriter();

                    IDocumentFormatter formatter = new VelocityDocumentFormatter();
                    FieldsMetadata metadata = new FieldsMetadata();
                    metadata.addFieldAsTextStyling( "row.html", SyntaxKind.Html );
                    
                    preprocessor.preprocess( "content.xml", stream, writer, metadata, formatter, new HashMap<String, Object>() );

                    Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                        + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                        + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
                        +"<table:table table:name=\"Compras\" table:style-name=\"Compras\">"
                            +"<table:table-column table:style-name=\"Compras.A\"/>"
                            +"<table:table-header-rows>"
                                +"<table:table-row>"
                                    +"<table:table-cell table:style-name=\"Compras.A1\" office:value-type=\"string\">"
                                        +"#set($___NoEscape0=${___TextStylingRegistry.transform($row.html,\"Html\",$false,\"ODT\",\"0_elementId\",$___context,\"content.xml\")})$___NoEscape0.TextBefore"
                                        +"<text:p text:style-name=\"P3\">$___NoEscape0.TextBody</text:p>"
                                        +"$___NoEscape0.TextEnd"
                                        +"</table:table-cell>"
                                +"</table:table-row>"
                            +"</table:table-header-rows>"
                       +"</table:table>"
                    + "</office:document-content>", writer.toString() );
                }
    
    
    @Test
    public void textStylingInsideTableRow()
                    throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                        + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                                        + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                                        + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
                                        +"<table:table table:name=\"Compras\" table:style-name=\"Compras\">"
                                        +"<table:table-column table:style-name=\"Compras.A\"/>"
                                        +"<table:table-header-rows>"
                                            +"<table:table-row>"
                                                +"<table:table-cell table:style-name=\"Compras.A1\" office:value-type=\"string\">"
                                                    +"<text:p text:style-name=\"P3\">"
                                                        +"<text:text-input text:description=\"\">$row.html</text:text-input>"
                                                    +"</text:p>"
                                                +"</table:table-cell>"
                                            +"</table:table-row>"
                                        +"</table:table-header-rows>"
                                    +"</table:table>"
                                    + "</office:document-content>", "UTF-8" );
        StringWriter writer = new StringWriter();

        IDocumentFormatter formatter = new VelocityDocumentFormatter();
        FieldsMetadata metadata = new FieldsMetadata();        
        metadata.addFieldAsTextStyling( "row.html", SyntaxKind.Html );
        metadata.addFieldAsList( "row.html" );
        
        preprocessor.preprocess( "content.xml", stream, writer, metadata, formatter, new HashMap<String, Object>() );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\">"
            +"<table:table table:name=\"Compras\" table:style-name=\"Compras\">"
                +"<table:table-column table:style-name=\"Compras.A\"/>"
                +"<table:table-header-rows>"
                    +"#foreach($item_row in $row)"
                    +"<table:table-row>"
                        +"<table:table-cell table:style-name=\"Compras.A1\" office:value-type=\"string\">"
                            +"#set($___NoEscape0=${___TextStylingRegistry.transform($item_row.html,\"Html\",$false,\"ODT\",\"0_elementId\",$___context,\"content.xml\")})$___NoEscape0.TextBefore"
                            +"<text:p text:style-name=\"P3\">$___NoEscape0.TextBody</text:p>"
                            +"$___NoEscape0.TextEnd"
                            +"</table:table-cell>"
                    +"</table:table-row>"
                    +"#{end}"
                +"</table:table-header-rows>"
           +"</table:table>"
        + "</office:document-content>", writer.toString() );
    }

}
