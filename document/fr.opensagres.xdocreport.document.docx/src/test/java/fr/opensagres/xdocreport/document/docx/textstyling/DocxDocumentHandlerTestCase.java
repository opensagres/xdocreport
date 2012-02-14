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

import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkInfo;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkRegistry;
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
        Assert.assertEquals( "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_1\" /></w:pPr><w:r><w:t>Title1</w:t></w:r></w:p><w:p></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_2\" /></w:pPr><w:r><w:t>Title2</w:t></w:r></w:p><w:p></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_3\" /></w:pPr><w:r><w:t>Title3</w:t></w:r></w:p><w:p></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_4\" /></w:pPr><w:r><w:t>Title4</w:t></w:r></w:p><w:p></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_5\" /></w:pPr><w:r><w:t>Title5</w:t></w:r></w:p><w:p></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_6\" /></w:pPr><w:r><w:t>Title6</w:t></w:r></w:p><w:p></w:p>",
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
        Assert.assertEquals( "<w:p><w:pPr><w:pStyle w:val=\"Heading1\" /></w:pPr><w:r><w:t>Title1</w:t></w:r></w:p><w:p></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"Heading2\" /></w:pPr><w:r><w:t>Title2</w:t></w:r></w:p><w:p></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_3\" /></w:pPr><w:r><w:t>Title3</w:t></w:r></w:p><w:p></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_4\" /></w:pPr><w:r><w:t>Title4</w:t></w:r></w:p><w:p></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_5\" /></w:pPr><w:r><w:t>Title5</w:t></w:r></w:p><w:p></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_6\" /></w:pPr><w:r><w:t>Title6</w:t></w:r></w:p><w:p></w:p>",
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
    public void testOrderedList()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<ol><li>item1</li><li>item2</li></ol>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"2\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item1</w:t></w:r></w:p>"
                                 + "<w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"2\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item2</w:t></w:r></w:p>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testUnorderedList()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new DocxDocumentHandler( parent, context, "word/document.xml" );
        formatter.transform( "<ul><li>item1</li><li>item2</li></ul>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item1</w:t></w:r></w:p><w:p><w:pPr><w:pStyle w:val=\"Paragraphedeliste\" /><w:numPr><w:ilvl w:val=\"0\" /><w:numId w:val=\"1\" /></w:numPr></w:pPr><w:r><w:t xml:space=\"preserve\" >item2</w:t></w:r></w:p>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }
}
