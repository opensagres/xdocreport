package fr.opensagres.xdocreport.document.docx.preprocessor;

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.TestCase;

public class DocxPreprocessorWith2InstrText extends TestCase {

	
	public void test2InstrText() throws Exception {
		DocXPreprocessor preprocessor = new DocXPreprocessor();		
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<w:document "
						+ "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
						+ "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
						+ "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
						+ "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
						+ "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
						+ "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
						+ "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
						+ "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
						+ "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
						+ "<w:p w:rsidR=\"002407B7\" w:rsidRDefault=\"008A6FB5\" w:rsidP=\"002407B7\">"
						+ "<w:r>"
							+ "<w:fldChar w:fldCharType=\"begin\" />"
						+ "</w:r>"
						+ "<w:r>"
							+ "<w:instrText xml:space=\"preserve\"> MERGEFIELD  ${ctx.serviceclientdeparture}  \\* M</w:instrText>"
						+ "</w:r>"
						+ "<w:r>"
							+ "<w:instrText xml:space=\"preserve\">ERGEFORMAT </w:instrText>"
						+ "</w:r>"
						+ "<w:r>"
							+ "<w:fldChar w:fldCharType=\"separate\" />"
						+ "</w:r>"
						+ "<w:r w:rsidR=\"002407B7\">"
							+ "<w:rPr>"
								+ "<w:noProof/>"
							+ "</w:rPr>"
							+ "<w:t>«${ctx.serviceclientdeparture}»</w:t>"
						+ "</w:r>"
						+ "<w:r>"
							+ "<w:rPr>"
								+ "<w:noProof/>"
							+ "</w:rPr>"
							+ "<w:fldChar w:fldCharType=\"end\" />"
						+ "</w:r>"
					+ "</w:p>"
					+ "</w:document>");
		

		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null,
				null, null);
		
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<w:document "
						+ "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
						+ "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
						+ "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
						+ "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
						+ "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
						+ "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
						+ "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
						+ "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
						+ "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
						+ "<w:p w:rsidR=\"002407B7\" w:rsidRDefault=\"008A6FB5\" w:rsidP=\"002407B7\">"
						//+ "<w:r>"
						//	+ "<w:fldChar w:fldCharType=\"begin\" />"
						//+ "</w:r>"
						//+ "<w:r>"
						//	+ "<w:instrText xml:space=\"preserve\"> MERGEFIELD  ${ctx.serviceclientdeparture}  \\* M</w:instrText>"
						//+ "</w:r>"
						//+ "<w:r>"
						//	+ "<w:instrText xml:space=\"preserve\">ERGEFORMAT </w:instrText>"
						//+ "</w:r>"
						//+ "<w:r>"
						//	+ "<w:fldChar w:fldCharType=\"separate\" />"
						//+ "</w:r>"
						+ "<w:r w:rsidR=\"002407B7\">"
							+ "<w:rPr>"
								+ "<w:noProof/>"
							+ "</w:rPr>"
							//+ "<w:t>«${ctx.serviceclientdeparture}»</w:t>"
							+ "<w:t>${ctx.serviceclientdeparture}</w:t>"
						+ "</w:r>"
						//+ "<w:r>"
						//	+ "<w:rPr>"
						//		+ "<w:noProof />"
						//	+ "</w:rPr>"
						//	+ "<w:fldChar w:fldCharType=\"end\" />"
						//+ "</w:r>"
					+ "</w:p>"
					+ "</w:document>", writer.toString());
	}
}
