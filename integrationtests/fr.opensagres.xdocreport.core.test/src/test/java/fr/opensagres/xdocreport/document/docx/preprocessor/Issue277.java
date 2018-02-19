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

import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocxPreprocessor;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityDocumentFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * See https://github.com/opensagres/xdocreport/issues/277
 */
public class Issue277 {

    @Test
    public void testStylingForSplitMergefields()
            throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream = IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                + "<w:p w:rsidR=\"00EB1008\" w:rsidRDefault=\"00EB1008\" w:rsidP=\"00EB1008\">"
                + "<w:r>" + "<w:fldChar w:fldCharType=\"begin\"/>" + "</w:r>"
                // the follwing instr text field was split into several fields
                // this can happen if you edit the field code manually, since word may give the changed part a new RSID
                // in this case, the field is split into three parts
                //   MERGEFIELD $
                //   developer.name
                //   \* MERGEFORMAT
                // the preprocessor has to combine the fields to be able to detect '$developer.name'
                + "<w:r>" + "<w:instrText xml:space=\"preserve\">MERGEFIELD $</w:instrText>" + "</w:r>"
                + "<w:r w:rsidR=\"00C72DB5\">" + "<w:instrText>developer.name</w:instrText>" + "</w:r>"
                + "<w:r>" + "<w:instrText xml:space=\"preserve\">\\* MERGEFORMAT </w:instrText>" + "</w:r>"
                + "<w:r>" + "<w:fldChar w:fldCharType=\"separate\"/>" + "</w:r>"
                + "<w:r>" + "<w:rPr>" + "<w:noProof/>" + "</w:rPr>" + "<w:t>«$</w:t>" + "</w:r>"
                + "<w:r>" + "<w:rPr>" + "<w:noProof/>" + "</w:rPr>" + "<w:t>developer.</w:t>" + "</w:r>"
                + "<w:r>" + "<w:rPr>" + "<w:noProof/>" + "</w:rPr>" + "<w:t>name»</w:t>" + "</w:r>"
                + "<w:r>" + "<w:fldChar w:fldCharType=\"end\"/>" + "</w:r>"
                + "</w:p>" + "</w:document>" );

        StringWriter writer = new StringWriter();
        FieldsMetadata metadata = new FieldsMetadata(TemplateEngineKind.Velocity);
        metadata.addFieldAsTextStyling( "developer.name", SyntaxKind.Html );
        IDocumentFormatter formatter = new VelocityDocumentFormatter();
        preprocessor.preprocess( "word/document.xml", stream, writer, metadata, formatter, new HashMap<String, Object>() );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        + "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                        + "#set($___NoEscape0=${___TextStylingRegistry.transform($developer.name,\"Html\",$false,\"DOCX\",\"0_elementId\",$___context,\"word/document.xml\")}) $___NoEscape0.TextBefore"
                        + "<w:p w:rsidR=\"00EB1008\" w:rsidRDefault=\"00EB1008\" w:rsidP=\"00EB1008\">"
                        + "<w:r>" + "<w:rPr>" + "<w:noProof/>" + "</w:rPr>" + "<w:t>$___NoEscape0.TextBody</w:t>" + "</w:r>" + "</w:p>"
                        + "$___NoEscape0.TextEnd"
                        + "</w:document>", writer.toString() );
    }
}