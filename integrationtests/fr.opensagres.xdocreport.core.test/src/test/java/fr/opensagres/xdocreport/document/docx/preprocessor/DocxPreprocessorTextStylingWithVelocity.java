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
import fr.opensagres.xdocreport.template.velocity.VelocityDocumentFormatter;

public class DocxPreprocessorTextStylingWithVelocity
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
        IDocumentFormatter formatter = new VelocityDocumentFormatter();
 
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
 
            + "#set($___NoEscape0=${___TextStylingRegistry.transform(${htmlText},\"Html\",$false,\"DOCX\",\"0_elementId\",$___context,\"word/document.xml\")}) $___NoEscape0.TextBefore"
             
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
             + "<w:t>$___NoEscape0.TextBody</w:t>"    
             + "</w:r>"
             //+ "<w:r>"
             //    + "<w:rPr>"
             //        + "<w:noProof/>"
             //    + "</w:rPr>"
             //    + "<w:fldChar w:fldCharType=\"end\"/>"
             //+ "</w:r>"
             + "</w:p>"
             + "$___NoEscape0.TextEnd"
             + "</w:document>", writer.toString() );
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

        IDocumentFormatter formatter = new VelocityDocumentFormatter();
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
                    +"#set($___NoEscape0=${___TextStylingRegistry.transform(${row.html},\"Html\",$false,\"DOCX\",\"0_elementId\",$___context,\"word/document.xml\")}) $___NoEscape0.TextBefore"
                    +"<w:p w:rsidR=\"00916516\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00916516\">"
                        //+"<w:fldSimple w:instr=\" MERGEFIELD  ${row.html}  \\* MERGEFORMAT \">"
                            +"<w:r>"
                                +"<w:rPr>"
                                    +"<w:noProof/>"
                                +"</w:rPr>"
                                //+"<w:t>«${row.html}»</w:t>"
                                +"<w:t>$___NoEscape0.TextBody</w:t>"
                            +"</w:r>"
                        //+"</w:fldSimple>"*/
                    +"</w:p>"
                    +"$___NoEscape0.TextEnd"
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

        IDocumentFormatter formatter = new VelocityDocumentFormatter();
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

            +"#foreach($item_row in $row)"
            +"<w:tr w:rsidR=\"00916516\" w:rsidTr=\"005D6D71\">"
                +"<w:tc>"
                    +"<w:tcPr>"
                        +"<w:tcW w:w=\"3070\" w:type=\"dxa\"/>"
                    +"</w:tcPr>"
                    +"#set($___NoEscape0=${___TextStylingRegistry.transform(${item_row.html},\"Html\",$false,\"DOCX\",\"0_elementId\",$___context,\"word/document.xml\")}) $___NoEscape0.TextBefore"
                    +"<w:p w:rsidR=\"00916516\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00916516\">"
                        //+"<w:fldSimple w:instr=\" MERGEFIELD  ${row.html}  \\* MERGEFORMAT \">"
                            +"<w:r>"
                                +"<w:rPr>"
                                    +"<w:noProof/>"
                                +"</w:rPr>"
                                //+"<w:t>«${row.html}»</w:t>"
                                +"<w:t>$___NoEscape0.TextBody</w:t>"
                            +"</w:r>"
                        //+"</w:fldSimple>"*/
                    +"</w:p>"
                    +"$___NoEscape0.TextEnd"
                +"</w:tc>"
            +"</w:tr>"
            +"#{end}"
            
        +"</w:tbl>"
        + "</w:document>", writer.toString() );
    }  
}
