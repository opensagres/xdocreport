package fr.opensagres.xdocreport.document.docx.textstyling;

import org.junit.Test;

public class DocxHTMLTextStylingFormatterTestCase {

	@Test
	public void testname() throws Exception {
		String html = "<strong>a</strong>";
		String docx = DocxHTMLTextStylingFormatter.INSTANCE.format(html);
		
		System.err.println(docx);
	}
}
