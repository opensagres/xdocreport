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
package fr.opensagres.xdocreport.document.docx.preprocessor;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocxPreprocessor;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerDocumentFormatter;

public class DocxPreprocessorTextStylingWithFreemarker
{

    @Test
    public void test2InstrText()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:document "
                                        + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
                                        + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
                                        + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
                                        + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
                                        + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
                                        + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                                        + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
                                        + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
                                        + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
                                        
                                        + "<w:p w:rsidR=\"008E751F\" w:rsidRPr=\"00C20656\" w:rsidRDefault=\"00E4086C\" w:rsidP=\"00AA3AE5\">"
                                        + "<w:pPr>"
                                            + "<w:spacing w:after=\"0\"/>"
                                            + "<w:rPr>"
                                                + "<w:noProof/>"
                                                + "<w:lang w:val=\"en-US\"/>"
                                            + "</w:rPr>"
                                        + "</w:pPr>"
                                        + "<w:r>"
                                            + "<w:fldChar w:fldCharType=\"begin\"/>"
                                        + "</w:r>"
                                        + "<w:r w:rsidRPr=\"00C20656\">"
                                            + "<w:rPr>"
                                                + "<w:lang w:val=\"en-US\"/>"
                                            + "</w:rPr>"
                                            + "<w:instrText xml:space=\"preserve\"> MERGEFIELD  ${htmlText}  \\* MERGEFORMAT </w:instrText>"
                                        + "</w:r>"
                                        + "<w:r>"
                                            + "<w:fldChar w:fldCharType=\"separate\"/>"
                                        + "</w:r>"
                                        + "<w:r w:rsidR=\"006E20E5\" w:rsidRPr=\"00C20656\">"
                                            + "<w:rPr>"
                                                + "<w:noProof/>"
                                                + "<w:lang w:val=\"en-US\"/>"
                                            + "</w:rPr>"
                                            + "<w:t>«${htmlText}»</w:t>"
                                        + "</w:r>"
                                        + "<w:r>"
                                            + "<w:rPr>"
                                                + "<w:noProof/>"
                                            + "</w:rPr>"
                                            + "<w:fldChar w:fldCharType=\"end\"/>"
                                        + "</w:r>"
                                        + "</w:p>"
                                        
                                        + "</w:document>", "UTF-8"  );

        StringWriter writer = new StringWriter();
        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsTextStyling( "htmlText", SyntaxKind.Html );
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();
 
        preprocessor.preprocess( "word/document.xml", stream, writer, metadata, formatter, new HashMap<String, Object>() );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:document "
            + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
            + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
            + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
            + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
            + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
            + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
            + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
            + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
            + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
 
            + "[#assign ___NoEscape0=___TextStylingRegistry.transform(htmlText,\"Html\",false,\"DOCX\",\"0_elementId\",___context,\"word/document.xml\")] [#noescape]${___NoEscape0.textBefore}[/#noescape]"
             
             + "<w:p w:rsidR=\"008E751F\" w:rsidRPr=\"00C20656\" w:rsidRDefault=\"00E4086C\" w:rsidP=\"00AA3AE5\">"
             + "<w:pPr>"
                 + "<w:spacing w:after=\"0\"/>"
                 + "<w:rPr>"
                     + "<w:noProof/>"
                     + "<w:lang w:val=\"en-US\"/>"
                 + "</w:rPr>"
             + "</w:pPr>"
             //+ "<w:r>"
             //    + "<w:fldChar w:fldCharType=\"begin\"/>"
             //+ "</w:r>"
             //+ "<w:r w:rsidRPr=\"00C20656\">"
             //    + "<w:rPr>"
             //        + "<w:lang w:val=\"en-US\"/>"
             //    + "</w:rPr>"
             //    + "<w:instrText xml:space=\"preserve\"> MERGEFIELD  ${htmlText}  \\* MERGEFORMAT </w:instrText>"
             //+ "</w:r>"
             //+ "<w:r>"
             //    + "<w:fldChar w:fldCharType=\"separate\"/>"
             //+ "</w:r>"
             + "<w:r w:rsidR=\"006E20E5\" w:rsidRPr=\"00C20656\">"
                 + "<w:rPr>"
                     + "<w:noProof/>"
                     + "<w:lang w:val=\"en-US\"/>"
                 + "</w:rPr>"
             //    + "<w:t>«${htmlText}»</w:t>"
             + "<w:t>[#noescape]${___NoEscape0.textBody}[/#noescape]</w:t>"    
             + "</w:r>"
             //+ "<w:r>"
             //    + "<w:rPr>"
             //        + "<w:noProof/>"
             //    + "</w:rPr>"
             //    + "<w:fldChar w:fldCharType=\"end\"/>"
             //+ "</w:r>"
             + "</w:p>"
             + "[#noescape]${___NoEscape0.textEnd}[/#noescape]"
             + "</w:document>", writer.toString() );
    }

    @Test
    public void test2InstrTextWith2MD()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                                "<w:document xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" xmlns:mo=\"http://schemas.microsoft.com/office/mac/office/2008/main\" xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:mv=\"urn:schemas-microsoft-com:mac:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" xmlns:w15=\"http://schemas.microsoft.com/office/word/2012/wordml\" xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" mc:Ignorable=\"w14 w15 wp14\">\n" +
                                "    <w:body>\n" +
                                "        <w:p w14:paraId=\"1EFB801B\" w14:textId=\"77777777\" w:rsidR=\"00187DBF\" w:rsidRDefault=\"00187DBF\">\n" +
                                "            <w:pPr>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "            </w:pPr>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:fldChar w:fldCharType=\"begin\"/>\n" +
                                "            </w:r>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:instrText xml:space=\"preserve\"> </w:instrText>\n" +
                                "            </w:r>\n" +
                                "            <w:r w:rsidR=\"00DD1465\">\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:instrText>MERGEFIELD ${field1}</w:instrText>\n" +
                                "            </w:r>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:instrText xml:space=\"preserve\"> \\* MERGEFORMAT </w:instrText>\n" +
                                "            </w:r>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:fldChar w:fldCharType=\"separate\"/>\n" +
                                "            </w:r>\n" +
                                "            <w:r w:rsidR=\"00DD1465\">\n" +
                                "                <w:rPr>\n" +
                                "                    <w:noProof/>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:t>«${field1}»</w:t>\n" +
                                "            </w:r>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:fldChar w:fldCharType=\"end\"/>\n" +
                                "            </w:r>\n" +
                                "        </w:p>\n" +
                                "        <w:p w14:paraId=\"316E5D55\" w14:textId=\"77777777\" w:rsidR=\"00DD1465\" w:rsidRDefault=\"00DD1465\">\n" +
                                "            <w:pPr>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "            </w:pPr>\n" +
                                "        </w:p>\n" +
                                "        <w:p w14:paraId=\"71FF5154\" w14:textId=\"77777777\" w:rsidR=\"00DD1465\" w:rsidRDefault=\"00DD1465\" w:rsidP=\"00DD1465\">\n" +
                                "            <w:pPr>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "            </w:pPr>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:fldChar w:fldCharType=\"begin\"/>\n" +
                                "            </w:r>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:instrText xml:space=\"preserve\"> </w:instrText>\n" +
                                "            </w:r>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:instrText>MERGEFIELD ${field2</w:instrText>\n" +
                                "            </w:r>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:instrText xml:space=\"preserve\">} \\* MERGEFORMAT </w:instrText>\n" +
                                "            </w:r>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:fldChar w:fldCharType=\"separate\"/>\n" +
                                "            </w:r>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:noProof/>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:t>«${field2}»</w:t>\n" +
                                "            </w:r>\n" +
                                "            <w:r>\n" +
                                "                <w:rPr>\n" +
                                "                    <w:lang w:val=\"en-US\"/>\n" +
                                "                </w:rPr>\n" +
                                "                <w:fldChar w:fldCharType=\"end\"/>\n" +
                                "            </w:r>\n" +
                                "        </w:p>\n" +
                                "    </w:body>\n" +
                                "</w:document>"  );

        StringWriter writer = new StringWriter();
        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsTextStyling( "field1", SyntaxKind.Html );
        metadata.addFieldAsTextStyling( "field2", SyntaxKind.Html );
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();

        preprocessor.preprocess( "word/document.xml", stream, writer, metadata, formatter, new HashMap<String, Object>() );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><w:document xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" xmlns:mo=\"http://schemas.microsoft.com/office/mac/office/2008/main\" xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:mv=\"urn:schemas-microsoft-com:mac:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" xmlns:w15=\"http://schemas.microsoft.com/office/word/2012/wordml\" xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" mc:Ignorable=\"w14 w15 wp14\">\n" +
                "    <w:body>\n" +
                "        [#assign ___NoEscape0=___TextStylingRegistry.transform(field1,\"Html\",false,\"DOCX\",\"0_elementId\",___context,\"word/document.xml\")] [#noescape]${___NoEscape0.textBefore}[/#noescape]<w:p w14:paraId=\"1EFB801B\" w14:textId=\"77777777\" w:rsidR=\"00187DBF\" w:rsidRDefault=\"00187DBF\">\n" +
                "            <w:pPr>\n" +
                "                <w:rPr>\n" +
                "                    <w:lang w:val=\"en-US\"/>\n" +
                "                </w:rPr>\n" +
                "            </w:pPr>\n" +
                "            \n" +
                "            \n" +
                "            \n" +
                "            \n" +
                "            \n" +
                "            <w:r w:rsidR=\"00DD1465\">\n" +
                "                <w:rPr>\n" +
                "                    <w:noProof/>\n" +
                "                    <w:lang w:val=\"en-US\"/>\n" +
                "                </w:rPr>\n" +
                "                <w:t>[#noescape]${___NoEscape0.textBody}[/#noescape]</w:t>\n" +
                "            </w:r>\n" +
                "            \n" +
                "        </w:p>[#noescape]${___NoEscape0.textEnd}[/#noescape]\n" +
                "        <w:p w14:paraId=\"316E5D55\" w14:textId=\"77777777\" w:rsidR=\"00DD1465\" w:rsidRDefault=\"00DD1465\">\n" +
                "            <w:pPr>\n" +
                "                <w:rPr>\n" +
                "                    <w:lang w:val=\"en-US\"/>\n" +
                "                </w:rPr>\n" +
                "            </w:pPr>\n" +
                "        </w:p>\n" +
                "        [#assign ___NoEscape1=___TextStylingRegistry.transform(field2,\"Html\",false,\"DOCX\",\"1_elementId\",___context,\"word/document.xml\")] [#noescape]${___NoEscape1.textBefore}[/#noescape][#assign ___NoEscape2=___TextStylingRegistry.transform(field2,\"Html\",false,\"DOCX\",\"2_elementId\",___context,\"word/document.xml\")] [#noescape]${___NoEscape2.textBefore}[/#noescape]<w:p w14:paraId=\"71FF5154\" w14:textId=\"77777777\" w:rsidR=\"00DD1465\" w:rsidRDefault=\"00DD1465\" w:rsidP=\"00DD1465\">\n" +
                "            <w:pPr>\n" +
                "                <w:rPr>\n" +
                "                    <w:lang w:val=\"en-US\"/>\n" +
                "                </w:rPr>\n" +
                "            </w:pPr>\n" +
                "            \n" +
                "            \n" +
                "            \n" +
                "            \n" +
                "            \n" +
                "            <w:r>\n" +
                "                <w:rPr>\n" +
                "                    <w:noProof/>\n" +
                "                    <w:lang w:val=\"en-US\"/>\n" +
                "                </w:rPr>\n" +
                "                <w:t>[#noescape]${___NoEscape1.textBody}[/#noescape]</w:t>\n" +
                "            </w:r>\n" +
                "            \n" +
                "        </w:p>[#noescape]${___NoEscape1.textEnd}[/#noescape][#noescape]${___NoEscape2.textEnd}[/#noescape]\n" +
                "    </w:body>\n" +
                "</w:document>", writer.toString() );
    }
    
    @Test
    public void textStylingWithSimpleField()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:document "
                                        + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
                                        + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
                                        + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
                                        + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
                                        + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
                                        + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                                        + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
                                        + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
                                        + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
                                        
                                        +"<w:tbl>"
                                            +"<w:tblPr>"
                                                +"<w:tblStyle w:val=\"Grilledutableau\"/>"
                                                +"<w:tblW w:w=\"0\" w:type=\"auto\"/>"
                                                +"<w:tblLook w:val=\"04A0\" w:firstRow=\"1\" w:lastRow=\"0\" w:firstColumn=\"1\" w:lastColumn=\"0\" w:noHBand=\"0\" w:noVBand=\"1\"/>"
                                            +"</w:tblPr>"
                                            +"<w:tblGrid>"
                                                +"<w:gridCol w:w=\"3070\"/>"
                                            +"</w:tblGrid>"
                                            +"<w:tr w:rsidR=\"00916516\" w:rsidTr=\"005D6D71\">"
                                                +"<w:tc>"
                                                    +"<w:tcPr>"
                                                        +"<w:tcW w:w=\"3070\" w:type=\"dxa\"/>"
                                                    +"</w:tcPr>"
                                                    +"<w:p w:rsidR=\"00916516\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00916516\">"
                                                        +"<w:fldSimple w:instr=\" MERGEFIELD  ${row.html}  \\* MERGEFORMAT \">"
                                                            +"<w:r>"
                                                                +"<w:rPr>"
                                                                    +"<w:noProof/>"
                                                                +"</w:rPr>"
                                                                +"<w:t>«${row.html}»</w:t>"
                                                            +"</w:r>"
                                                        +"</w:fldSimple>"
                                                    +"</w:p>"
                                                +"</w:tc>"
                                            +"</w:tr>"
                                        +"</w:tbl>"
                                        
                                        + "</w:document>", "UTF-8"  );

        StringWriter writer = new StringWriter();

        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();
        FieldsMetadata metadata = new FieldsMetadata();        
        metadata.addFieldAsTextStyling( "row.html", SyntaxKind.Html );
 
        preprocessor.preprocess( "word/document.xml", stream, writer, metadata, formatter, new HashMap<String, Object>() );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:document "
            + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
            + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
            + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
            + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
            + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
            + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
            + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
            + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
            + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
 
            +"<w:tbl>"
            +"<w:tblPr>"
                +"<w:tblStyle w:val=\"Grilledutableau\"/>"
                +"<w:tblW w:w=\"0\" w:type=\"auto\"/>"
                +"<w:tblLook w:val=\"04A0\" w:firstRow=\"1\" w:lastRow=\"0\" w:firstColumn=\"1\" w:lastColumn=\"0\" w:noHBand=\"0\" w:noVBand=\"1\"/>"
            +"</w:tblPr>"
            +"<w:tblGrid>"
                +"<w:gridCol w:w=\"3070\"/>"
            +"</w:tblGrid>"
            +"<w:tr w:rsidR=\"00916516\" w:rsidTr=\"005D6D71\">"
                +"<w:tc>"
                    +"<w:tcPr>"
                        +"<w:tcW w:w=\"3070\" w:type=\"dxa\"/>"
                    +"</w:tcPr>"
                    +"[#assign ___NoEscape0=___TextStylingRegistry.transform(row.html,\"Html\",false,\"DOCX\",\"0_elementId\",___context,\"word/document.xml\")] [#noescape]${___NoEscape0.textBefore}[/#noescape]"
                    +"<w:p w:rsidR=\"00916516\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00916516\">"
                        //+"<w:fldSimple w:instr=\" MERGEFIELD  ${row.html}  \\* MERGEFORMAT \">"
                            +"<w:r>"
                                +"<w:rPr>"
                                    +"<w:noProof/>"
                                +"</w:rPr>"
                                //+"<w:t>«${row.html}»</w:t>"
                                +"<w:t>[#noescape]${___NoEscape0.textBody}[/#noescape]</w:t>"
                            +"</w:r>"
                        //+"</w:fldSimple>"*/
                    +"</w:p>"
                    +"[#noescape]${___NoEscape0.textEnd}[/#noescape]"
                +"</w:tc>"
            +"</w:tr>"
        +"</w:tbl>"
        + "</w:document>", writer.toString() );
    }
    
    @Test
    public void textStylingInsideTableRow()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:document "
                                        + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
                                        + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
                                        + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
                                        + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
                                        + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
                                        + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                                        + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
                                        + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
                                        + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
                                        
                                        +"<w:tbl>"
                                            +"<w:tblPr>"
                                                +"<w:tblStyle w:val=\"Grilledutableau\"/>"
                                                +"<w:tblW w:w=\"0\" w:type=\"auto\"/>"
                                                +"<w:tblLook w:val=\"04A0\" w:firstRow=\"1\" w:lastRow=\"0\" w:firstColumn=\"1\" w:lastColumn=\"0\" w:noHBand=\"0\" w:noVBand=\"1\"/>"
                                            +"</w:tblPr>"
                                            +"<w:tblGrid>"
                                                +"<w:gridCol w:w=\"3070\"/>"
                                            +"</w:tblGrid>"
                                            +"<w:tr w:rsidR=\"00916516\" w:rsidTr=\"005D6D71\">"
                                                +"<w:tc>"
                                                    +"<w:tcPr>"
                                                        +"<w:tcW w:w=\"3070\" w:type=\"dxa\"/>"
                                                    +"</w:tcPr>"
                                                    +"<w:p w:rsidR=\"00916516\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00916516\">"
                                                        +"<w:fldSimple w:instr=\" MERGEFIELD  ${row.html}  \\* MERGEFORMAT \">"
                                                            +"<w:r>"
                                                                +"<w:rPr>"
                                                                    +"<w:noProof/>"
                                                                +"</w:rPr>"
                                                                +"<w:t>«${row.html}»</w:t>"
                                                            +"</w:r>"
                                                        +"</w:fldSimple>"
                                                    +"</w:p>"
                                                +"</w:tc>"
                                            +"</w:tr>"
                                        +"</w:tbl>"
                                        
                                        + "</w:document>", "UTF-8"  );

        StringWriter writer = new StringWriter();

        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();
        FieldsMetadata metadata = new FieldsMetadata();        
        metadata.addFieldAsTextStyling( "row.html", SyntaxKind.Html );
        metadata.addFieldAsList( "row.html" );
 
        preprocessor.preprocess( "word/document.xml", stream, writer, metadata, formatter, new HashMap<String, Object>() );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:document "
            + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
            + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
            + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
            + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
            + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
            + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
            + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
            + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
            + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
 
            +"<w:tbl>"
            +"<w:tblPr>"
                +"<w:tblStyle w:val=\"Grilledutableau\"/>"
                +"<w:tblW w:w=\"0\" w:type=\"auto\"/>"
                +"<w:tblLook w:val=\"04A0\" w:firstRow=\"1\" w:lastRow=\"0\" w:firstColumn=\"1\" w:lastColumn=\"0\" w:noHBand=\"0\" w:noVBand=\"1\"/>"
            +"</w:tblPr>"
            +"<w:tblGrid>"
                +"<w:gridCol w:w=\"3070\"/>"
            +"</w:tblGrid>"

            +"[#list row as item_row]"
            +"<w:tr w:rsidR=\"00916516\" w:rsidTr=\"005D6D71\">"
                +"<w:tc>"
                    +"<w:tcPr>"
                        +"<w:tcW w:w=\"3070\" w:type=\"dxa\"/>"
                    +"</w:tcPr>"
                    +"[#assign ___NoEscape0=___TextStylingRegistry.transform(item_row.html,\"Html\",false,\"DOCX\",\"0_elementId\",___context,\"word/document.xml\")] [#noescape]${___NoEscape0.textBefore}[/#noescape]"
                    +"<w:p w:rsidR=\"00916516\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00916516\">"
                        //+"<w:fldSimple w:instr=\" MERGEFIELD  ${row.html}  \\* MERGEFORMAT \">"
                            +"<w:r>"
                                +"<w:rPr>"
                                    +"<w:noProof/>"
                                +"</w:rPr>"
                                //+"<w:t>«${row.html}»</w:t>"
                                +"<w:t>[#noescape]${___NoEscape0.textBody}[/#noescape]</w:t>"
                            +"</w:r>"
                        //+"</w:fldSimple>"*/
                    +"</w:p>"
                    +"[#noescape]${___NoEscape0.textEnd}[/#noescape]"
                +"</w:tc>"
            +"</w:tr>"
            +"[/#list]"
            
        +"</w:tbl>"
        + "</w:document>", writer.toString() );
    }  
}
