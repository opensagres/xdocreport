/*
 * Copyright (c) 2013, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fr.opensagres.xdocreport.document.docx.preprocessor;

import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocxPreprocessor;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityDocumentFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * See https://github.com/opensagres/xdocreport/issues/89
 * <p>
 * User: Kevin Senechal <kevin@dooapp.com>
 * Date: 23/11/2015
 * Time: 10:10
 */
public class Issue89 {

    @Test
    public void testStylingHtmlInsideLoop() throws Exception{
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:document "+
                        "xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" "+
                        "xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "+
                        "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "+
                        "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "+
                        "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "+
                        "xmlns:v=\"urn:schemas-microsoft-com:vml\" "+
                        "xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" "+
                        "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "+
                        "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "+
                        "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "+
                        "xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" "+
                        "xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" "+
                        "xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" "+
                        "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" "+
                        "xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" "+
                        "mc:Ignorable=\"w14 wp14\">" +
                        "<w:p w:rsidR=\"00C7281A\" w:rsidRDefault=\"00CC4527\">" +
                                "<w:fldSimple w:instr=\" MERGEFIELD  &quot;#foreach($htmlInsideLoop in $htmls)&quot;  \\* MERGEFORMAT \">" +
                                        "<w:r>" +
                                                "<w:rPr>" +
                                                        "<w:noProof/>" +
                                                "</w:rPr>" +
                                                "<w:t>FOREACH HTML</w:t>" +
                                        "</w:r>"+
                                "</w:fldSimple>" +
                                "<w:r>" +
                                        "<w:fldChar w:fldCharType=\"begin\"/>" +
                                "</w:r>" +
                                "<w:r>" +
                                        "<w:instrText xml:space=\"preserve\"> MERGEFIELD  $htmlInsideLoop  \\* MERGEFORMAT </w:instrText>" +
                                "</w:r>" +
                                "<w:r>" +
                                        "<w:fldChar w:fldCharType=\"separate\"/>" +
                                "</w:r>" +
                                "<w:r>" +
                                        "<w:rPr>" +
                                                "<w:noProof/>" +
                                        "</w:rPr>" +
                                        "<w:t>HTML INSIDE LOOP</w:t>" +
                                "</w:r>"+
                                "<w:r>" +
                                        "<w:rPr>" +
                                                "<w:noProof/>" +
                                        "</w:rPr>" +
                                        "<w:fldChar w:fldCharType=\"end\"/>" +
                                "</w:r>" +
                        "</w:p>" +
                        "<w:p w:rsidR=\"00D2511B\" w:rsidRDefault=\"00410598\">" +
                                "<w:r>" +
                                        "<w:fldChar w:fldCharType=\"begin\"/>" +
                                "</w:r>" +
                                "<w:r>" +
                                        "<w:instrText xml:space=\"preserve\"> MERGEFIELD  #end  \\* MERGEFORMAT </w:instrText>" +
                                "</w:r>" +
                                "<w:r>" +
                                        "<w:fldChar w:fldCharType=\"separate\"/>" +
                                "</w:r>" +
                                "<w:r w:rsidR=\"00CC4527\">" +
                                        "<w:rPr>" +
                                                "<w:noProof/>" +
                                        "</w:rPr>" +
                                        "<w:t>END FOREACH</w:t>" +
                                "</w:r>" +
                                "<w:r>" +
                                        "<w:rPr>" +
                                                "<w:noProof/>" +
                                        "</w:rPr>" +
                                        "<w:fldChar w:fldCharType=\"end\"/>" +
                                "</w:r>" +
                        "</w:p>" +
                        "</w:document>", "UTF-8");

        StringWriter writer = new StringWriter();
        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addField("htmlInsideLoop", true, null, SyntaxKind.Html.name(), false);
        IDocumentFormatter formatter = new VelocityDocumentFormatter();

        preprocessor.preprocess( "word/document.xml", stream, writer, metadata, formatter, new HashMap<String, Object>() );

        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
                "<w:document xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" "+
                "xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "+
                "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "+
                "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "+
                "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "+
                "xmlns:v=\"urn:schemas-microsoft-com:vml\" "+
                "xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" "+
                "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "+
                "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "+
                "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "+
                "xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" "+
                "xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" "+
                "xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" "+
                "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" "+
                "xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" "+
                "mc:Ignorable=\"w14 wp14\">"+
                // this line should be inside loop. see issue 90
                "#set($___NoEscape0=${___TextStylingRegistry.transform($htmlInsideLoop,\"Html\",$false,\"DOCX\",\"0_elementId\",$___context,\"word/document.xml\")}) $___NoEscape0.TextBefore"+
                "<w:p w:rsidR=\"00C7281A\" w:rsidRDefault=\"00CC4527\">"+
                        "<w:r>"+
                                "<w:rPr>"+
                                        "<w:noProof/>"+
                                "</w:rPr>"+
                                "<w:t>#foreach($htmlInsideLoop in $htmls)</w:t>"+
                        "</w:r>"+
                        "<w:r>"+
                                "<w:rPr>"+
                                        "<w:noProof/>"+
                                "</w:rPr>"+
                                "<w:t>$___NoEscape0.TextBody</w:t>"+
                        "</w:r>"+
                "</w:p>"+
                "$___NoEscape0.TextEnd"+
                "<w:p w:rsidR=\"00D2511B\" w:rsidRDefault=\"00410598\">"+
                        "<w:r w:rsidR=\"00CC4527\">"+
                                "<w:rPr>"+
                                        "<w:noProof/>"+
                                "</w:rPr>"+
                                "<w:t>#end</w:t>"+
                        "</w:r>"+
                "</w:p>"+
                "</w:document>", writer.toString());
    }
}
