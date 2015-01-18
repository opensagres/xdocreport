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

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocxPreprocessor;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerDocumentFormatter;

/**
 * See https://code.google.com/p/xdocreport/issues/detail?id=401
 *
 */
public class Issue401  {

	@Test
    public void issue401()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:document "
                + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
                + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
                + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
                + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
                + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
                + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
                + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
                + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
                
					+ "<w:p w:rsidR=\"00BA7D5B\" w:rsidRPr=\"00FC53D8\" w:rsidRDefault=\"00FC53D8\">"
					+ "<w:pPr>"
						+ "<w:rPr>"
							+ "<w:lang w:val=\"fr-BE\"/>"
						+ "</w:rPr>"
					+ "</w:pPr>"
					+ "<w:r>"
						+ "<w:rPr>"
							+ "<w:lang w:val=\"fr-BE\"/>"
						+ "</w:rPr>"
						+ "<w:t xml:space=\"preserve\">Hello </w:t>"
					+ "</w:r>"
					+ "<w:r w:rsidR=\"000456CE\">"
						+ "<w:rPr>"
							+ "<w:noProof/>"
							+ "<w:lang w:val=\"fr-BE\"/>"
						+ "</w:rPr>"
						+ "<w:t>hey  </w:t>"
						+ "<w:br/>"
						+ "<w:t xml:space=\"preserve\">it&apos;s me !  </w:t>"
					+ "</w:r>"
					+ "<w:bookmarkStart w:id=\"0\" w:name=\"_GoBack\"/>"
					+ "<w:bookmarkEnd w:id=\"0\"/>"
					+ "</w:p>"
                
                + "</w:document>";
        
        InputStream stream =
                        IOUtils.toInputStream( xml, "UTF-8"  );

        StringWriter writer = new StringWriter();
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();
 
        preprocessor.preprocess( "word/document.xml", stream, writer, null, formatter, new HashMap<String, Object>() );
System.err.println(writer.toString());
        Assert.assertEquals( xml, writer.toString() );
    }
}
