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
package fr.opensagres.xdocreport.document.docx.textstyling;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;
import fr.opensagres.xdocreport.document.textstyling.html.HTMLTextStylingTransformer;
import fr.opensagres.xdocreport.template.MockContext;

public class DocxDocumentHandlerTestCase
{

    @Test
    public void testHTML2Docx()
        throws Exception
    {
        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( null, new MockContext(), null );
        formatter.transform( "<p>\r\n\tHere are severals&nbsp;styles :</p>\r\n<ul>\r\n\t<li>\r\n\t\t<strong>Bold</strong> style.</li>\r\n\t<li>\r\n\t\t<em>Italic</em> style.</li>\r\n\t<li>\r\n\t\t<strong><em>BoldAndItalic</em></strong> style.</li>\r\n</ul>\r\n<p>\r\n\tHere are 3 styles :</p>\r\n<ol>\r\n\t<li>\r\n\t\t<strong>Bold</strong> style.</li>\r\n\t<li>\r\n\t\t<em>Italic</em> style.</li>\r\n\t<li>\r\n\t\t<strong><em>BoldAndItalic</em></strong> style.</li>\r\n</ol>\r\n<p>\r\n\tXDocReport can manage thoses styles.</p>\r\n",
                             handler );
        // FIXME : !!!!
        
        //Assert.assertEquals( "<w:p><w:r><w:t xml:space=\"preserve\" >Here are severals styles :</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >Bold</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:i /></w:rPr><w:t xml:space=\"preserve\" >Italic</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:b /><w:i /></w:rPr><w:t xml:space=\"preserve\" >BoldAndItalic</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:r><w:t xml:space=\"preserve\" >Here are 3 styles :</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"1\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >Bold</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:i /></w:rPr><w:t xml:space=\"preserve\" >Italic</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:b /><w:i /></w:rPr><w:t xml:space=\"preserve\" >BoldAndItalic</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:r><w:t xml:space=\"preserve\" >XDocReport can manage thoses styles.</w:t></w:r></w:p>",
         //                    handler.getTextEnd() );
    }

    @Test
    public void testListsFollowingLinebreaks()
        throws Exception
    {
        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( null, new MockContext(), null );
        formatter.transform( "Some text followed by linebreak in front of an unordered list<br/><ul><li>first</li><li>second</li></ul>"
        				   + "Some text followed by linebreak in front of an   ordered list<br/><ol><li>first</li><li>second</li></ol>",
                             handler );
        Assert.assertEquals( "", handler.getTextBefore());
        Assert.assertEquals( "<w:r><w:t xml:space=\"preserve\" >Some text followed by linebreak in front of an unordered list</w:t></w:r><w:r><w:br/></w:r>", handler.getTextBody());
        Assert.assertEquals( "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >first</w:t></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >second</w:t></w:r></w:p><w:p><w:r><w:t xml:space=\"preserve\" >Some text followed by linebreak in front of an   ordered list</w:t></w:r><w:r><w:br/></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"2\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >first</w:t></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"2\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >second</w:t></w:r></w:p>", handler.getTextEnd());
    }
}
