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

import junit.framework.Assert;

import org.junit.Test;

import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks.HyperlinkInfo;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks.HyperlinkRegistry;
import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;
import fr.opensagres.xdocreport.document.textstyling.html.HTMLTextStylingTransformer;
import fr.opensagres.xdocreport.template.IContext;

public class DocxDocumentHandlerTestCase
{

    @Test
    public void testSpecialCharacter()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "&auml; &uuml; &eacute;", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:t xml:space=\"preserve\" >ä </w:t></w:r><w:r><w:t xml:space=\"preserve\" >ü </w:t></w:r><w:r><w:t xml:space=\"preserve\" >é</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testSpecialCharacterAmp()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "&amp;&lt;", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:t xml:space=\"preserve\" >&amp;</w:t></w:r><w:r><w:t xml:space=\"preserve\" >&lt;</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testNbsp()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<b>A&nbsp;B</b>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >A B</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testBoldWithB()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<b>text</b>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testBoldWithStrong()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<strong>text</strong>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testBoldWithStyle()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<p style=\"font-weight:bold;\">text</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:rPr><w:b /></w:rPr></w:pPr><w:r><w:t xml:space=\"preserve\" >text</w:t></w:r></w:p>", handler.getTextEnd() );
    }
    
    @Test
    public void testItalicWithI()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<i>text</i>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:rPr><w:i /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testItalicWithEm()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<em>text</em>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:rPr><w:i /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testUnderline()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<u>text</u>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:rPr><w:u w:val=\"single\" /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testStrikeWithS()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<s>text</s>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:rPr><w:strike /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testStrikeWithStrike()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<strike>text</strike>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:rPr><w:strike /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testHyperlinkByUsingXDocReport_HyperlinkStyle()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<a href=\"http://code.google.com/p/xdocreport/\" >XDocReport</a>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:hyperlink r:id=\"___rId0\" w:history=\"1\"> <w:proofErr w:type=\"spellStart\" /><w:r w:rsidRPr=\"001D30B5\"><w:rPr><w:rStyle w:val=\"XDocReport_Hyperlink\" /></w:rPr><w:t>XDocReport</w:t></w:r><w:proofErr w:type=\"spellEnd\" /></w:hyperlink>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );

        HyperlinkRegistry registry = DocxContextHelper.getHyperlinkRegistry( context, "word/document.xml" );
        Assert.assertNotNull( registry );
        Assert.assertEquals( 1, registry.getHyperlinks().size() );

        HyperlinkInfo hyperlinkInfo = registry.getHyperlinks().get( 0 );
        Assert.assertEquals( "___rId0", hyperlinkInfo.getId() );
        Assert.assertEquals( "http://code.google.com/p/xdocreport/", hyperlinkInfo.getTarget() );
        Assert.assertEquals( "External", hyperlinkInfo.getTargetMode() );
    }

    @Test
    public void testHyperlinkByUsingDefaultHyperlinkStyle()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by DocxStylesPreprocessor which search
        // hyperlink style from the word/styles.xml entry of the docx)
        DefaultStyle defaultStyle = new DefaultStyle();
        defaultStyle.setHyperLinkStyleId( "DefaultHyperlink" );
        DocxContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<a href=\"http://code.google.com/p/xdocreport/\" >XDocReport</a>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:hyperlink r:id=\"___rId0\" w:history=\"1\"> <w:proofErr w:type=\"spellStart\" /><w:r w:rsidRPr=\"001D30B5\"><w:rPr><w:rStyle w:val=\"DefaultHyperlink\" /></w:rPr><w:t>XDocReport</w:t></w:r><w:proofErr w:type=\"spellEnd\" /></w:hyperlink>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );

        HyperlinkRegistry registry = DocxContextHelper.getHyperlinkRegistry( context, "word/document.xml" );
        Assert.assertNotNull( registry );
        Assert.assertEquals( 1, registry.getHyperlinks().size() );

        HyperlinkInfo hyperlinkInfo = registry.getHyperlinks().get( 0 );
        Assert.assertEquals( "___rId0", hyperlinkInfo.getId() );
        Assert.assertEquals( "http://code.google.com/p/xdocreport/", hyperlinkInfo.getTarget() );
        Assert.assertEquals( "External", hyperlinkInfo.getTargetMode() );
    }

    @Test
    public void testHyperlinkWithURLParameters()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<a href=\"https://www.google.com/search?q=xdocreport&amp;start=10\" >XDocReport</a>",
                             handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:hyperlink r:id=\"___rId0\" w:history=\"1\"> <w:proofErr w:type=\"spellStart\" /><w:r w:rsidRPr=\"001D30B5\"><w:rPr><w:rStyle w:val=\"XDocReport_Hyperlink\" /></w:rPr><w:t>XDocReport</w:t></w:r><w:proofErr w:type=\"spellEnd\" /></w:hyperlink>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );

        HyperlinkRegistry registry = DocxContextHelper.getHyperlinkRegistry( context, "word/document.xml" );
        Assert.assertNotNull( registry );
        Assert.assertEquals( 1, registry.getHyperlinks().size() );

        HyperlinkInfo hyperlinkInfo = registry.getHyperlinks().get( 0 );
        Assert.assertEquals( "___rId0", hyperlinkInfo.getId() );
        Assert.assertEquals( "https://www.google.com/search?q=xdocreport&amp;start=10", hyperlinkInfo.getTarget() );
        Assert.assertEquals( "External", hyperlinkInfo.getTargetMode() );
    }

    @Test
    public void testHyperlinkWithURLParametersAmp()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<a href=\"https://www.google.com/search?q=xdocreport&amp;start=10\" >XDocReport&amp;others</a>",
                             handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:hyperlink r:id=\"___rId0\" w:history=\"1\"> <w:proofErr w:type=\"spellStart\" /><w:r w:rsidRPr=\"001D30B5\"><w:rPr><w:rStyle w:val=\"XDocReport_Hyperlink\" /></w:rPr><w:t>XDocReport&amp;others</w:t></w:r><w:proofErr w:type=\"spellEnd\" /></w:hyperlink>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );

        HyperlinkRegistry registry = DocxContextHelper.getHyperlinkRegistry( context, "word/document.xml" );
        Assert.assertNotNull( registry );
        Assert.assertEquals( 1, registry.getHyperlinks().size() );

        HyperlinkInfo hyperlinkInfo = registry.getHyperlinks().get( 0 );
        Assert.assertEquals( "___rId0", hyperlinkInfo.getId() );
        Assert.assertEquals( "https://www.google.com/search?q=xdocreport&amp;start=10", hyperlinkInfo.getTarget() );
        Assert.assertEquals( "External", hyperlinkInfo.getTargetMode() );
    }

    @Test
    public void testHeaderByUsingXDocReport_HeadingStyle()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "xxx<h1>Title1</h1><h2>Title2</h2><h3>Title3</h3><h4>Title4</h4><h5>Title5</h5><h6>Title6</h6>",
                             handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:t xml:space=\"preserve\" >xxx</w:t></w:r>", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_1\" /></w:pPr><w:r><w:t>Title1</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_2\" /></w:pPr><w:r><w:t>Title2</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_3\" /></w:pPr><w:r><w:t>Title3</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_4\" /></w:pPr><w:r><w:t>Title4</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_5\" /></w:pPr><w:r><w:t>Title5</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_6\" /></w:pPr><w:r><w:t>Title6</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testHeaderByUsingDefaultHeadingStyle()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by DocxStylesPreprocessor which search
        // heading style from the word/styles.xml entry of the docx)
        DefaultStyle defaultStyle = new DefaultStyle();
        defaultStyle.addHeaderStyle( 1, "Heading1" );
        defaultStyle.addHeaderStyle( 2, "Heading2" );
        DocxContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "xxx<h1>Title1</h1><h2>Title2</h2><h3>Title3</h3><h4>Title4</h4><h5>Title5</h5><h6>Title6</h6>",
                             handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:t xml:space=\"preserve\" >xxx</w:t></w:r>", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:pStyle w:val=\"Heading1\" /></w:pPr><w:r><w:t>Title1</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"Heading2\" /></w:pPr><w:r><w:t>Title2</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_3\" /></w:pPr><w:r><w:t>Title3</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_4\" /></w:pPr><w:r><w:t>Title4</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_5\" /></w:pPr><w:r><w:t>Title5</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_6\" /></w:pPr><w:r><w:t>Title6</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testHeaderAndText()
        throws Exception
    {
        IContext context = new MockContext();

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<h1>Title1</h1>paragraph1", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_1\" /></w:pPr><w:r><w:t>Title1</w:t></w:r></w:p>"
                                 + "<w:p><w:r><w:t xml:space=\"preserve\" >paragraph1</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testHeaderAndTextInParagraph()
        throws Exception
    {
        IContext context = new MockContext();

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<h1>Title1</h1><p>paragraph1</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_1\" /></w:pPr><w:r><w:t>Title1</w:t></w:r></w:p>"
                                 + "<w:p><w:r><w:t xml:space=\"preserve\" >paragraph1</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testHeaderAndTextAndParagraph()
        throws Exception
    {
        IContext context = new MockContext();

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<h1>Title1</h1>" + "text" + "<p>paragraph</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_1\" /></w:pPr><w:r><w:t>Title1</w:t></w:r></w:p>"
                                 + "<w:p><w:r><w:t xml:space=\"preserve\" >text</w:t></w:r></w:p>"
                                 + "<w:p><w:r><w:t xml:space=\"preserve\" >paragraph</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testParagraphAtFirst()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<p>bla bla bla</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:r><w:t xml:space=\"preserve\" >bla bla bla</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testParagraph()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "some <strong>text</strong><p>paragraph1</p><p>paragraph2</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:r><w:t xml:space=\"preserve\" >some </w:t></w:r><w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                             handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:r><w:t xml:space=\"preserve\" >paragraph1</w:t></w:r></w:p><w:p><w:r><w:t xml:space=\"preserve\" >paragraph2</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testParagraphPageBreakBefore()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<p style=\"page-break-before:always;\">bla bla bla</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:r><w:br w:type=\"page\" /></w:r></w:p><w:p><w:r><w:t xml:space=\"preserve\" >bla bla bla</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testParagraphWithPageBreakAfter()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<p style=\"page-break-after:always;\">bla bla bla</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:r><w:t xml:space=\"preserve\" >bla bla bla</w:t></w:r></w:p><w:p><w:r><w:br w:type=\"page\" /></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testParagraphWithStyle()
            throws Exception {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler(parent, context, "word/document.xml");
        formatter.transform("some <strong>text</strong><p style=\"name:Standard\">paragraph1</p><p>paragraph2</p>", handler);

        Assert.assertEquals("", handler.getTextBefore());
        Assert.assertEquals("<w:r><w:t xml:space=\"preserve\" >some </w:t></w:r><w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >text</w:t></w:r>",
                handler.getTextBody());
        Assert.assertEquals("<w:p><w:pPr><w:pStyle w:val=\"Standard\" /></w:pPr><w:r><w:t xml:space=\"preserve\" >paragraph1</w:t></w:r></w:p><w:p><w:r><w:t xml:space=\"preserve\" >paragraph2</w:t></w:r></w:p>",
                handler.getTextEnd());
    }

    @Test
    public void testOrderedList()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by DocxNumberingPreprocessor which search
        // numbering from the word/numbering.xml entry of the docx)
        DefaultStyle defaultStyle = new DefaultStyle();
        // defaultStyle.setNumIdForOrdererList( 2 );
        DocxContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<ol><li>item1</li><li>item2</li></ol>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item1</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item2</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testUnorderedList()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by DocxNumberingPreprocessor which search
        // numbering from the word/numbering.xml entry of the docx)
        DefaultStyle defaultStyle = new DefaultStyle();
        // defaultStyle.setNumIdForUnordererList( 1 );
        DocxContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<ol><li>item1</li><li>item2</li></ol><ul><li>item1</li><li>item2</li></ul>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item1</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item2</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"2\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item1</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"2\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item2</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testUnorderedListAndText()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by DocxNumberingPreprocessor which search
        // numbering from the word/numbering.xml entry of the docx)
        DefaultStyle defaultStyle = new DefaultStyle();
        // defaultStyle.setNumIdForUnordererList( 1 );
        DocxContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<ol><li>item1</li><li>item2</li></ol>xxxx", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item1</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item2</w:t></w:r></w:p>"
                                 + "<w:p><w:r><w:t xml:space=\"preserve\" >xxxx</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }
    
    @Test
    public void testOrderedAndUnorderedList()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by DocxNumberingPreprocessor which search
        // numbering from the word/numbering.xml entry of the docx)
        DefaultStyle defaultStyle = new DefaultStyle();
        // defaultStyle.setNumIdForUnordererList( 1 );
        DocxContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<ul><li>item1</li><li>item2</li></ul>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item1</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item2</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testComplexList()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by DocxNumberingPreprocessor which search
        // numbering from the word/numbering.xml entry of the docx)
        DefaultStyle defaultStyle = new DefaultStyle();
        // defaultStyle.setNumIdForUnordererList( 1 );
        DocxContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<ul>" + "<li>" + "<strong>Bold</strong> style." + "<ul>" + "<li>zaza</li>"
            + "<li>zaza</li>" + "<li>zaza</li>" + "</ul>" + "</li>" + "<li><em>Italic</em> style." + "<ul>"
            + "<li>zazaaa</li>" + "<li>zzzzzzzzzzzz" + "<ul>" + "<li>ddddddddddddddddddd</li>" + "</ul>" + "</li>"
            + "</ul>" + "</li>" + "<li><strong><em>BoldAndItalic</em></strong> style." + "</li>" + "</ul>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:b /></w:rPr><w:t xml:space=\"preserve\" >Bold</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"1\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >zaza</w:t></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"1\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >zaza</w:t></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"1\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >zaza</w:t></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:i /></w:rPr><w:t xml:space=\"preserve\" >Italic</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"1\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >zazaaa</w:t></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"1\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >zzzzzzzzzzzz</w:t></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"2\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >ddddddddddddddddddd</w:t></w:r></w:p><w:p><w:pPr><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:rPr><w:b /><w:i /></w:rPr><w:t xml:space=\"preserve\" >BoldAndItalic</w:t></w:r><w:r><w:t xml:space=\"preserve\" > style.</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testLineBreak()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "a<br/>b", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        
        Assert.assertEquals( "<w:r><w:t xml:space=\"preserve\" >a</w:t></w:r><w:r><w:br/><w:t xml:space=\"preserve\" >b</w:t></w:r>",
                handler.getTextBody() );
//was:        Assert.assertEquals( "<w:r><w:t xml:space=\"preserve\" >a</w:t></w:r><w:r><w:t><w:br/></w:t></w:r><w:r><w:t xml:space=\"preserve\" >b</w:t></w:r>",
//                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testTextAlignment()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<p style=\"text-align:left\" >left</p><p style=\"text-align:right\" >right</p><p style=\"text-align:center\" >center</p><p style=\"text-align:justify\" >justify</p>",
                             handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:p><w:pPr><w:jc w:val=\"left\"/></w:pPr><w:r><w:t xml:space=\"preserve\" >left</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:jc w:val=\"right\"/></w:pPr><w:r><w:t xml:space=\"preserve\" >right</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:jc w:val=\"center\"/></w:pPr><w:r><w:t xml:space=\"preserve\" >center</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:jc w:val=\"both\"/></w:pPr><w:r><w:t xml:space=\"preserve\" >justify</w:t></w:r></w:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testTable()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<table><tbody>"
            + "<tr><td>A</td><td>B</td></tr>"
            + "<tr><td>C</td><td>D</td></tr>"
            + "<tr><td>E</td><td>F</td></tr>"
            + "</tbody></table>",
                             handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<w:tbl>"
            + "<w:tblGrid><w:gridCol w:w=\"2994\" /><w:gridCol w:w=\"2994\" /></w:tblGrid>"
            + "<w:tr><w:tc><w:p><w:r><w:t xml:space=\"preserve\" >A</w:t></w:r></w:p></w:tc><w:tc><w:p><w:r><w:t xml:space=\"preserve\" >B</w:t></w:r></w:p></w:tc></w:tr>"
            + "<w:tr><w:tc><w:p><w:r><w:t xml:space=\"preserve\" >C</w:t></w:r></w:p></w:tc><w:tc><w:p><w:r><w:t xml:space=\"preserve\" >D</w:t></w:r></w:p></w:tc></w:tr>"
            + "<w:tr><w:tc><w:p><w:r><w:t xml:space=\"preserve\" >E</w:t></w:r></w:p></w:tc><w:tc><w:p><w:r><w:t xml:space=\"preserve\" >F</w:t></w:r></w:p></w:tc></w:tr>"
            + "</w:tbl><w:p/>",
                             handler.getTextEnd() );
    }

    @Test
    public void testAll()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        context.put( "comments_html", "<b>Bold</b> text" );
        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<p>\r\n\tHere are severals styles :</p>\r\n<ul>\r\n\t<li>\r\n\t\t<strong>Bold</strong> style.</li>\r\n\t<li>\r\n\t\t<em>Italic</em> style.</li>\r\n\t<li>\r\n\t\t<strong><em>BoldAndItalic</em></strong> style.</li>\r\n</ul>\r\n<p>\r\n\tHere are 3 styles :</p>\r\n<ol>\r\n\t<li>\r\n\t\t<strong>Bold</strong> style.</li>\r\n\t<li>\r\n\t\t<em>Italic</em> style.</li>\r\n\t<li>\r\n\t\t<strong><em>BoldAndItalic</em></strong> style.</li>\r\n</ol>\r\n<p>\r\n\tXDocReport can manage thoses styles.</p>\r\n<h1>\r\n\tsqsq</h1>\r\n<p>\r\n\tzazazaa</p>\r\n ",
                             handler );

        // System.err.println(handler.getTextEnd());

    }
}
