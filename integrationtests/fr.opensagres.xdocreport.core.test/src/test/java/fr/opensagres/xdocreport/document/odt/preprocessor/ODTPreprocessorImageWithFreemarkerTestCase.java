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
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerDocumentFormatter;

public class ODTPreprocessorImageWithFreemarkerTestCase
    extends TestCase
{

    private static final String LOGO_IMAGE_XML =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
            + "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" "
            + "xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
            + "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\">"
            + "<text:p text:style-name=\"Standard\">"
            + "<draw:frame draw:style-name=\"fr1\" draw:name=\"logo\" text:anchor-type=\"paragraph\" svg:x=\"69.96pt\" svg:y=\"18.31pt\" svg:width=\"21pt\" svg:height=\"22.51pt\" draw:z-index=\"0\">"
            + "<draw:image xlink:href=\"Pictures/100000000000001C0000001EE8812A78.png\" xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>"
            + "</draw:frame>Project logo :</text:p>" + "</office:document-content>";

    public void testImageWithNullFieldsMetadata()
        throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( LOGO_IMAGE_XML, "UTF-8" );
        StringWriter writer = new StringWriter();

        FieldsMetadata metadata = null;
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();

        preprocessor.preprocess( "test", stream, writer, metadata, formatter, null );

        assertEquals( LOGO_IMAGE_XML, writer.toString() );
    }

    public void testImageWithBadFieldsMetadata()
        throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( LOGO_IMAGE_XML, "UTF-8" );
        StringWriter writer = new StringWriter();

        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsImage( "XXX" );
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();

        preprocessor.preprocess( "test", stream, writer, metadata, formatter, null );

        assertEquals( LOGO_IMAGE_XML, writer.toString() );
    }

    public void testImageWithSimpleField()
        throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( LOGO_IMAGE_XML, "UTF-8" );
        StringWriter writer = new StringWriter();

        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsImage( "logo" );
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();

        preprocessor.preprocess( "test", stream, writer, metadata, formatter, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                          + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                          + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                          + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
                          + "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" "
                          + "xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                          + "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\">"
                          + "<text:p text:style-name=\"Standard\">"
                          
                          + "[#assign ___imageInfo=___ImageRegistry.registerImage(logo,'logo',___context)][#if ___imageInfo.notRemoveImageTemplate]"
                          
                          + "<draw:frame draw:style-name=\"fr1\" draw:name=\"logo\" text:anchor-type=\"paragraph\" svg:x=\"69.96pt\" svg:y=\"18.31pt\" "
                          // + "svg:width=\"21pt\" "
                          + "svg:width=\"${___ImageRegistry.getWidth(___imageInfo,'21pt','22.51pt')}\" "
                          // + "svg:height=\"22.51pt\" "
                          + "svg:height=\"${___ImageRegistry.getHeight(___imageInfo,'21pt','22.51pt')}\" " + "draw:z-index=\"0\">"
                          + "<draw:image "

                          + "xlink:href=\"${___ImageRegistry.getPath(___imageInfo,'Pictures/100000000000001C0000001EE8812A78.png')}\" "

                          + "xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>"
                          + "</draw:frame>" 
                          
                          + "[/#if]"
                          
                          + "Project logo :</text:p>" + "</office:document-content>", writer.toString() );
    }

    public void testImageWithListFieldInTable()
        throws Exception
    {
        ODTPreprocessor preprocessor = new ODTPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream(
                              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                  + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
                                  + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
                                  + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
                                  + "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" "
                                  + "xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                                  + "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\">"
                                  + "<table:table table:name=\"Tableau1\" table:style-name=\"Tableau1\">"
                                  + "<table:table-column table:style-name=\"Tableau1.A\"/>"
                                  + "<table:table-column table:style-name=\"Tableau1.B\"/>"
                                  + "<table:table-column table:style-name=\"Tableau1.C\"/>"
                                  + "<table:table-column table:style-name=\"Tableau1.D\"/>"
                                  + "<table:table-row table:style-name=\"Tableau1.1\">"
                                  + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
                                  + "<text:p text:style-name=\"P1\">Name</text:p>"
                                  + "</table:table-cell>"
                                  + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
                                  + "<text:p text:style-name=\"P2\">Last name</text:p>"
                                  + "</table:table-cell>"
                                  + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
                                  + "<text:p text:style-name=\"P1\">Mail</text:p>"
                                  + "</table:table-cell>"
                                  + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
                                  + "<text:p text:style-name=\"P1\">Photo</text:p>"
                                  + "</table:table-cell>"
                                  + "</table:table-row>"
                                  + "<table:table-row table:style-name=\"Tableau1.1\">"
                                  + "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
                                  + "<text:p text:style-name=\"Standard\">"
                                  + "<text:text-input text:description=\"\">$developers.Name</text:text-input>"
                                  + "</text:p>"
                                  + "</table:table-cell>"
                                  + "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
                                  + "<text:p text:style-name=\"Standard\">"
                                  + "<text:text-input text:description=\"\">$developers.LastName</text:text-input>"
                                  + "</text:p>"
                                  + "</table:table-cell>"
                                  + "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
                                  + "<text:p text:style-name=\"Standard\">"
                                  + "<text:text-input text:description=\"\">$developers.Mail</text:text-input>"
                                  + "</text:p>"
                                  + "</table:table-cell>"
                                  + "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
                                  + "<text:p text:style-name=\"Standard\">"
                                  + "<draw:frame draw:style-name=\"fr1\" draw:name=\"developers.Photo\" text:anchor-type=\"paragraph\" svg:width=\"21pt\" svg:height=\"22.51pt\" draw:z-index=\"0\">"
                                  + "<draw:image xlink:href=\"Pictures/100000000000001C0000001EE8812A78.png\" xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>"
                                  + "</draw:frame>" + "</text:p>" + "</table:table-cell>" + "</table:table-row>"
                                  + "</table:table>" + "</office:document-content>", "UTF-8" );
        StringWriter writer = new StringWriter();

        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsList( "developers.Photo" );
        metadata.addFieldAsImage( "developers.Photo" );
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();

        preprocessor.preprocess( "test", stream, writer, metadata, formatter, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
            + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
            + "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" "
            + "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" "
            + "xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
            + "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\">"
            + "<table:table table:name=\"Tableau1\" table:style-name=\"Tableau1\">"
            + "<table:table-column table:style-name=\"Tableau1.A\"/>"
            + "<table:table-column table:style-name=\"Tableau1.B\"/>"
            + "<table:table-column table:style-name=\"Tableau1.C\"/>"
            + "<table:table-column table:style-name=\"Tableau1.D\"/>"
            + "<table:table-row table:style-name=\"Tableau1.1\">"
            + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
            + "<text:p text:style-name=\"P1\">Name</text:p>"
            + "</table:table-cell>"
            + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
            + "<text:p text:style-name=\"P2\">Last name</text:p>"
            + "</table:table-cell>"
            + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
            + "<text:p text:style-name=\"P1\">Mail</text:p>"
            + "</table:table-cell>"
            + "<table:table-cell table:style-name=\"Tableau1.A1\" office:value-type=\"string\">"
            + "<text:p text:style-name=\"P1\">Photo</text:p>"
            + "</table:table-cell>"
            + "</table:table-row>"

            + "[#list developers as item_developers]"

            + "<table:table-row table:style-name=\"Tableau1.1\">"
            + "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
            + "<text:p text:style-name=\"Standard\">"
            // +
            // "<text:text-input text:description=\"\">$developers.Name</text:text-input>"
            + "$developers.Name"
            + "</text:p>"
            + "</table:table-cell>"
            + "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
            + "<text:p text:style-name=\"Standard\">"
            // +
            // "<text:text-input text:description=\"\">$developers.LastName</text:text-input>"
            + "$developers.LastName"
            + "</text:p>"
            + "</table:table-cell>"
            + "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
            + "<text:p text:style-name=\"Standard\">"
            // +
            // "<text:text-input text:description=\"\">$developers.Mail</text:text-input>"
            + "$developers.Mail"
            + "</text:p>"
            + "</table:table-cell>"
            + "<table:table-cell table:style-name=\"Tableau1.A2\" office:value-type=\"string\">"
            + "<text:p text:style-name=\"Standard\">"
            
            + "[#assign ___imageInfo=___ImageRegistry.registerImage(item_developers.Photo,'developers.Photo',___context)][#if ___imageInfo.notRemoveImageTemplate]"
            
            + "<draw:frame draw:style-name=\"fr1\" draw:name=\"developers.Photo\" text:anchor-type=\"paragraph\" "
            // + "svg:width=\"21pt\" "
            + "svg:width=\"${___ImageRegistry.getWidth(___imageInfo,'21pt','22.51pt')}\" "
            // + "svg:height=\"22.51pt\" "
            + "svg:height=\"${___ImageRegistry.getHeight(___imageInfo,'21pt','22.51pt')}\" " + "draw:z-index=\"0\">"
            + "<draw:image "

            + "xlink:href=\"${___ImageRegistry.getPath(___imageInfo,'Pictures/100000000000001C0000001EE8812A78.png')}\" "

            + "xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>" 
            + "</draw:frame>" 
            
            + "[/#if]"
            
            + "</text:p>"
            + "</table:table-cell>" + "</table:table-row>"

            + "[/#list]"

            + "</table:table>" + "</office:document-content>", writer.toString() );

    }
}
