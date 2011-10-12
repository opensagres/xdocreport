package fr.opensagres.xdocreport.document.docx.textstyling;

import junit.framework.Assert;

import org.junit.Test;

import fr.opensagres.xdocreport.template.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingTransformer;
import fr.opensagres.xdocreport.template.textstyling.html.HTMLTextStylingTransformer;

public class DocxDocumentHandlerTestCase {

	@Test
	public void testHTML2Docx() throws Exception {
		ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
		IDocumentHandler handler = new DocxDocumentHandler();
		formatter
				.transform(
						"<p>\r\n\tHere are severals styles :</p>\r\n<ul>\r\n\t<li>\r\n\t\t<strong>Bold</strong> style.</li>\r\n\t<li>\r\n\t\t<em>Italic</em> style.</li>\r\n\t<li>\r\n\t\t<strong><em>BoldAndItalic</em></strong> style.</li>\r\n</ul>\r\n<p>\r\n\tHere are 3 styles :</p>\r\n<ol>\r\n\t<li>\r\n\t\t<strong>Bold</strong> style.</li>\r\n\t<li>\r\n\t\t<em>Italic</em> style.</li>\r\n\t<li>\r\n\t\t<strong><em>BoldAndItalic</em></strong> style.</li>\r\n</ol>\r\n<p>\r\n\tXDocReport can manage thoses styles.</p>\r\n",
						handler);
		Assert.assertEquals(
				"<w:p><w:r><w:t xml:space=\"preserve\" >Here are severals styles :</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >Bold</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:i /></w:rPr><w:t xml:space=\"preserve\" >Italic</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:b /><w:i /></w:rPr><w:t xml:space=\"preserve\" >BoldAndItalic</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:r><w:t xml:space=\"preserve\" >Here are 3 styles :</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"2\" /></w:numPr></w:pPr><w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >Bold</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"2\" /></w:numPr></w:pPr><w:r><w:rPr><w:i /></w:rPr><w:t xml:space=\"preserve\" >Italic</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"2\" /></w:numPr></w:pPr><w:r><w:rPr><w:b /><w:i /></w:rPr><w:t xml:space=\"preserve\" >BoldAndItalic</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:r><w:t xml:space=\"preserve\" >XDocReport can manage thoses styles.</w:t></w:r></w:p>",
				handler.toString());
	}
}
