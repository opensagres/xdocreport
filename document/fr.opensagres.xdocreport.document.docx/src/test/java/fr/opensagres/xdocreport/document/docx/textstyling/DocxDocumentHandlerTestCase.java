/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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

import junit.framework.Assert;

import org.junit.Test;

import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkInfo;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkUtils;
import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;
import fr.opensagres.xdocreport.document.textstyling.html.HTMLTextStylingTransformer;
import fr.opensagres.xdocreport.template.IContext;

public class DocxDocumentHandlerTestCase
{

    @Test
    public void testBoldWithB()
        throws Exception
    {
        IContext context = null;
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<b>text</b>", handler );
        Assert.assertEquals( "<w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
    }

    @Test
    public void testBoldWithStrong()
        throws Exception
    {
        IContext context = null;
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<strong>text</strong>", handler );
        Assert.assertEquals( "<w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
    }

    @Test
    public void testItalicWithI()
        throws Exception
    {
        IContext context = null;
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<i>text</i>", handler );
        Assert.assertEquals( "<w:r><w:rPr><w:i /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
    }

    @Test
    public void testItalicWithEm()
        throws Exception
    {
        IContext context = null;
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<em>text</em>", handler );
        Assert.assertEquals( "<w:r><w:rPr><w:i /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
    }

    @Test
    public void testHyperlink()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<a href=\"http://code.google.com/p/xdocreport/\" >XDocReport</a>", handler );
        Assert.assertEquals( "<w:hyperlink r:id=\"___rId0\" w:history=\"1\"> <w:proofErr w:type=\"spellStart\" /><w:r w:rsidRPr=\"001D30B5\"><w:rPr><w:rStyle w:val=\"XDocReport_Hyperlink\" /></w:rPr><w:t>XDocReport</w:t></w:r><w:proofErr w:type=\"spellEnd\" /></w:hyperlink>",
                             handler.getTextBody() );

        HyperlinkRegistry registry = DocxContextHelper.getHyperlinkRegistry( context, "word/document.xml" );
        Assert.assertNotNull( registry );
        Assert.assertEquals( 1, registry.getHyperlinks().size() );

        HyperlinkInfo hyperlinkInfo = registry.getHyperlinks().get( 0 );
        Assert.assertEquals( "___rId0", hyperlinkInfo.getId() );
        Assert.assertEquals( "http://code.google.com/p/xdocreport/", hyperlinkInfo.getTarget() );
        Assert.assertEquals( "External", hyperlinkInfo.getTargetMode() );
    }
}
