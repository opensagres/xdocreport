package fr.opensagres.xdocreport.document.docx.preprocessor;

import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocxPreprocessor;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerDocumentFormatter;
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
    public void testStylingHtmlInsideLoop() throws Exception {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:document " +
                        "xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" " +
                        "xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" " +
                        "xmlns:o=\"urn:schemas-microsoft-com:office:office\" " +
                        "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" " +
                        "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" " +
                        "xmlns:v=\"urn:schemas-microsoft-com:vml\" " +
                        "xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" " +
                        "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" " +
                        "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" " +
                        "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" " +
                        "xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" " +
                        "xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" " +
                        "xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" " +
                        "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" " +
                        "xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" " +
                        "mc:Ignorable=\"w14 wp14\">" +
                        "<w:p w:rsidR=\"00C7281A\" w:rsidRDefault=\"00CC4527\">" +
                        "<w:fldSimple w:instr=\" MERGEFIELD  &quot;#foreach($htmlInsideLoop in $htmls)&quot;  \\* MERGEFORMAT \">" +
                        "<w:r>" +
                        "<w:rPr>" +
                        "<w:noProof/>" +
                        "</w:rPr>" +
                        "<w:t>FOREACH HTML</w:t>" +
                        "</w:r>" +
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
                        "</w:r>" +
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

        preprocessor.preprocess("word/document.xml", stream, writer, metadata, formatter, new HashMap<String, Object>());

        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<w:document xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" " +
                "xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" " +
                "xmlns:o=\"urn:schemas-microsoft-com:office:office\" " +
                "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" " +
                "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" " +
                "xmlns:v=\"urn:schemas-microsoft-com:vml\" " +
                "xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" " +
                "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" " +
                "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" " +
                "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" " +
                "xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" " +
                "xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" " +
                "xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" " +
                "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" " +
                "xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" " +
                "mc:Ignorable=\"w14 wp14\">" +
                // this line should be inside loop, but it's not the scope of the issue. for more information see issue 93 (Issue93Test)
                "#set($___NoEscape0=${___TextStylingRegistry.transform($htmlInsideLoop,\"Html\",$false,\"DOCX\",\"0_elementId\",$___context,\"word/document.xml\")}) $___NoEscape0.TextBefore" +
                "<w:p w:rsidR=\"00C7281A\" w:rsidRDefault=\"00CC4527\">" +
                "<w:r>" +
                "<w:rPr>" +
                "<w:noProof/>" +
                "</w:rPr>" +
                "<w:t>#foreach($htmlInsideLoop in $htmls)</w:t>" +
                "</w:r>" +
                "<w:r>" +
                "<w:rPr>" +
                "<w:noProof/>" +
                "</w:rPr>" +
                "<w:t>$___NoEscape0.TextBody</w:t>" +
                "</w:r>" +
                "</w:p>" +
                "$___NoEscape0.TextEnd" +
                "<w:p w:rsidR=\"00D2511B\" w:rsidRDefault=\"00410598\">" +
                "<w:r w:rsidR=\"00CC4527\">" +
                "<w:rPr>" +
                "<w:noProof/>" +
                "</w:rPr>" +
                "<w:t>#end</w:t>" +
                "</w:r>" +
                "</w:p>" +
                "</w:document>", writer.toString());
    }


    @Test
    public void testStylingHtmlInsideLoopWithFreeMarker() throws Exception {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                        "<w:document xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" " +
                        "xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" " +
                        "xmlns:o=\"urn:schemas-microsoft-com:office:office\" " +
                        "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" " +
                        "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" " +
                        "xmlns:v=\"urn:schemas-microsoft-com:vml\" " +
                        "xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" " +
                        "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" " +
                        "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" " +
                        "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" " +
                        "xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" " +
                        "xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" " +
                        "xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" " +
                        "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" " +
                        "xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" mc:Ignorable=\"w14 wp14\">" +
                        "<w:p w:rsidR=\"00676964\" w:rsidRDefault=\"00676964\">" +
                        "<w:fldSimple w:instr=\" MERGEFIELD  &quot;[#list htmls as htmlInsideLoop]&quot;  \\* MERGEFORMAT \">" +
                        "<w:r>" +
                        "<w:rPr>" +
                        "<w:noProof/>" +
                        "</w:rPr>" +
                        "<w:t>«[#list htmls as htmlInsideLoop]»</w:t>" +
                        "</w:r>" +
                        "</w:fldSimple>" +
                        "<w:r>" +
                        "<w:t xml:space=\"preserve\">Name: </w:t>" +
                        "</w:r>" +
                        "<w:fldSimple w:instr=\" MERGEFIELD  ${htmlInsideLoop}  \\* MERGEFORMAT \">" +
                        "<w:r>" +
                        "<w:rPr>" +
                        "<w:noProof/>" +
                        "</w:rPr>" +
                        "<w:t>«${htmlInsideLoop}»</w:t>" +
                        "</w:r>" +
                        "</w:fldSimple>" +
                        "<w:r>" +
                        "<w:fldChar w:fldCharType=\"begin\"/>" +
                        "</w:r>" +
                        "<w:r>" +
                        "<w:instrText xml:space=\"preserve\"> MERGEFIELD  [/#list]  \\* MERGEFORMAT </w:instrText>" +
                        "</w:r>" +
                        "<w:r>" +
                        "<w:fldChar w:fldCharType=\"separate\"/>" +
                        "</w:r>" +
                        "<w:r>" +
                        "<w:rPr>" +
                        "<w:noProof/>" +
                        "</w:rPr>" +
                        "<w:t>«[/#list]»</w:t>" +
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
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();

        preprocessor.preprocess("word/document.xml", stream, writer, metadata, formatter, new HashMap<String, Object>());

        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<w:document xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" " +
                "xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" " +
                "xmlns:o=\"urn:schemas-microsoft-com:office:office\" " +
                "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" " +
                "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" " +
                "xmlns:v=\"urn:schemas-microsoft-com:vml\" " +
                "xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" " +
                "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" " +
                "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" " +
                "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" " +
                "xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" " +
                "xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" " +
                "xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" " +
                "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" " +
                "xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" mc:Ignorable=\"w14 wp14\">" +
                "[#assign ___NoEscape0=___TextStylingRegistry.transform(htmlInsideLoop,\"Html\",false,\"DOCX\",\"0_elementId\",___context,\"word/document.xml\")] " +
                "[#noescape]${___NoEscape0.textBefore}[/#noescape]" +
                "<w:p w:rsidR=\"00676964\" w:rsidRDefault=\"00676964\">" +
                "<w:r>" +
                "<w:rPr>" +
                "<w:noProof/>" +
                "</w:rPr>" +
                "<w:t>" +
                "[#list htmls as htmlInsideLoop]" +
                "</w:t>" +
                "</w:r>" +
                "<w:r>" +
                "<w:t xml:space=\"preserve\">Name: </w:t>" +
                "</w:r>" +
                "<w:r>" +
                "<w:rPr>" +
                "<w:noProof/>" +
                "</w:rPr>" +
                "<w:t>" +
                "[#noescape]${___NoEscape0.textBody}[/#noescape]" +
                "</w:t>" +
                "</w:r>" +
                "<w:r>" +
                "<w:rPr>" +
                "<w:noProof/>" +
                "</w:rPr>" +
                "<w:t>" +
                "[/#list]" +
                "</w:t>" +
                "</w:r>" +
                "</w:p>" +
                "[#noescape]${___NoEscape0.textEnd}[/#noescape]" +
                "</w:document>", writer.toString());
    }

}
