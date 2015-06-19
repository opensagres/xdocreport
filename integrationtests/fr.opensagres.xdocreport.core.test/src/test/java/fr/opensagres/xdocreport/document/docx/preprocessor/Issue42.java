package fr.opensagres.xdocreport.document.docx.preprocessor;
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


import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocxPreprocessor;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerDocumentFormatter;

/**
 * See https://code.google.com/p/xdocreport/issues/detail?id=401
 *
 */
public class Issue42  {

	@Test
    public void issue42()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        
        String xml = 
        	"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + 
        	"<w:document xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" mc:Ignorable=\"w14 wp14\">" + 
        		"<w:body>" +         			
					"<w:p w:rsidR=\"003E1702\" w:rsidRPr=\"00E17175\" w:rsidRDefault=\"009E258F\" w:rsidP=\"00103428\">" + 
						"<w:pPr>" + 
							"<w:rPr>" + 
								"<w:color w:val=\"BFBFBF\" w:themeColor=\"background1\" w:themeShade=\"BF\"/>" + 
							"</w:rPr>" + 
						"</w:pPr>" + 
						"<w:r>" + 
							"<w:fldChar w:fldCharType=\"begin\"/>" + 
						"</w:r>" + 
						"<w:r>" + 
							"<w:instrText xml:space=\"preserve\"> MERGEFIELD  ${acuOther.cond}  \\* MER</w:instrText>" + 
						"</w:r>" + 
						"<w:r>" + 
							"<w:instrText xml:space=\"preserve\">GEFORMAT </w:instrText>" + 
						"</w:r>" + 
						"<w:r>" + 
							"<w:fldChar w:fldCharType=\"separate\"/>" + 
						"</w:r>" + 
						"<w:r w:rsidR=\"00D84AC9\" w:rsidRPr=\"00021809\">" + 
							"<w:rPr>" + 
								"<w:noProof/>" + 
							"</w:rPr>" + 
							"<w:t>«${acuOther.cond}»</w:t>" + 
						"</w:r>" + 
						"<w:r>" + 
							"<w:rPr>" + 
								"<w:noProof/>" + 
							"</w:rPr>" + 
							"<w:fldChar w:fldCharType=\"end\"/>" + 
						"</w:r>" + 
					"</w:p>" + 
        		"</w:body>" + 
        	"</w:document>";
        	
        	
        InputStream stream =
                        IOUtils.toInputStream( xml, "UTF-8"  );

        StringWriter writer = new StringWriter();
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();
 
        FieldsMetadata metadata = new FieldsMetadata(TemplateEngineKind.Freemarker);
        metadata.addFieldAsTextStyling("acuOther.cond", SyntaxKind.Html);
        
        preprocessor.preprocess( "word/document.xml", stream, writer, metadata, formatter, new HashMap<String, Object>() );
        
        System.err.println(writer.toString());
        
        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + 
        	"<w:document xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" mc:Ignorable=\"w14 wp14\">" + 
        		"<w:body>" +   
        	
					"[#assign ___NoEscape0=___TextStylingRegistry.transform(acuOther.cond,\"Html\",false,\"DOCX\",\"0_elementId\",___context,\"word/document.xml\")] " +
					"[#noescape]${___NoEscape0.textBefore}[/#noescape]" + 
        		
					"<w:p w:rsidR=\"003E1702\" w:rsidRPr=\"00E17175\" w:rsidRDefault=\"009E258F\" w:rsidP=\"00103428\">" + 
						"<w:pPr>" + 
							"<w:rPr>" + 
								"<w:color w:val=\"BFBFBF\" w:themeColor=\"background1\" w:themeShade=\"BF\"/>" + 
							"</w:rPr>" + 
						"</w:pPr>" + 
						//"<w:r>" + 
						//	"<w:fldChar w:fldCharType=\"begin\"/>" + 
						//"</w:r>" + 
						//"<w:r>" + 
						//	"<w:instrText xml:space=\"preserve\"> MERGEFIELD  ${acuOther.cond}  \\* MER</w:instrText>" + 
						//"</w:r>" + 
						//"<w:r>" + 
						//	"<w:instrText xml:space=\"preserve\">GEFORMAT </w:instrText>" + 
						//"</w:r>" + 
						//"<w:r>" + 
						//	"<w:fldChar w:fldCharType=\"separate\"/>" + 
						//"</w:r>" + 
						"<w:r w:rsidR=\"00D84AC9\" w:rsidRPr=\"00021809\">" + 
							"<w:rPr>" + 
								"<w:noProof/>" + 
							"</w:rPr>" + 
							//"<w:t>«${acuOther.cond}»</w:t>" + 
							"<w:t>[#noescape]${___NoEscape0.textBody}[/#noescape]</w:t>" +
						"</w:r>" + 
						//"<w:r>" + 
						//	"<w:rPr>" + 
						//		"<w:noProof/>" + 
						//	"</w:rPr>" + 
						//	"<w:fldChar w:fldCharType=\"end\"/>" + 
						//"</w:r>" + 
					"</w:p>" + 
						
					"[#noescape]${___NoEscape0.textEnd}[/#noescape]" +
					
        		"</w:body>" + 
        	"</w:document>", writer.toString() );
    }
}