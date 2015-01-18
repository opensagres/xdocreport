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
package fr.opensagres.xdocreport.document.odt.textstyling;

import junit.framework.Assert;

import org.junit.Test;

import fr.opensagres.xdocreport.document.odt.template.ODTContextHelper;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;
import fr.opensagres.xdocreport.document.textstyling.html.HTMLTextStylingTransformer;
import fr.opensagres.xdocreport.template.IContext;

public class ODTDocumentHandlerTestCase
{

    @Test
    public void testSpecialCharacter()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "&auml; &uuml; &eacute;", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_EmptyText\" >ä </text:span><text:span text:style-name=\"XDocReport_EmptyText\" >ü </text:span><text:span text:style-name=\"XDocReport_EmptyText\" >é</text:span>",
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
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "&amp;&lt;", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_EmptyText\" >&amp;</text:span><text:span text:style-name=\"XDocReport_EmptyText\" >&lt;</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testNbsp()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<b>A&nbsp;B</b>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_Bold\" >A B</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testBoldWithB()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<b>text</b>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_Bold\" >text</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testBoldWithStrong()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<strong>text</strong>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_Bold\" >text</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testBoldWithSpanFontWeightBold()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<span style=\"font-weight: bold\">text</span>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_T0\" style:family=\"text\"><style:text-properties fo:font-weight=\"bold\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_T0\" >text</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );

    }

    @Test
    public void testBoldWithSpanFontWeight700()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<span style=\"font-weight: 700\">text</span>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_T0\" style:family=\"text\"><style:text-properties fo:font-weight=\"bold\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_T0\" >text</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testBoldWithPFontWeightBold()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"font-weight: bold\">text</p>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_P0\" style:family=\"paragraph\"><style:text-properties fo:font-weight=\"bold\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_P0\"><text:span text:style-name=\"XDocReport_EmptyText\" >text</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testBoldWithPFontWeight700()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"font-weight: 700\">text</p>", handler );
        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_P0\" style:family=\"paragraph\"><style:text-properties fo:font-weight=\"bold\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_P0\"><text:span text:style-name=\"XDocReport_EmptyText\" >text</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testItalicWithI()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<i>text</i>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_Italic\" >text</text:span>",
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
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<em>text</em>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_Italic\" >text</text:span>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testItalicWithSpanFontStyleItalic()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<span style=\"font-style: italic\">text</span>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_T0\" style:family=\"text\"><style:text-properties fo:font-style=\"italic\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_T0\" >text</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testItalicWithPFontStyleItalic()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"font-style: italic\">text</p>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_P0\" style:family=\"paragraph\"><style:text-properties fo:font-style=\"italic\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_P0\"><text:span text:style-name=\"XDocReport_EmptyText\" >text</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testUnderline()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<u>text</u>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_Underline\" >text</text:span>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testUnderLineWithSpanTextDecorationUnderline()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<span style=\"text-decoration: underline\">text</span>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_T0\" style:family=\"text\"><style:text-properties style:text-underline-style=\"solid\" style:text-underline-width=\"auto\" style:text-underline-color=\"font-color\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_T0\" >text</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testUnderLineWithPTextDecorationUnderline()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"text-decoration: underline\">text</p>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_P0\" style:family=\"paragraph\"><style:text-properties style:text-underline-style=\"solid\" style:text-underline-width=\"auto\" style:text-underline-color=\"font-color\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_P0\"><text:span text:style-name=\"XDocReport_EmptyText\" >text</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testStrikeWithS()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<s>text</s>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_Strike\" >text</text:span>",
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
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<strike>text</strike>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_Strike\" >text</text:span>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testStrikeWithSpanTextDecorationLineThrough()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<span style=\"text-decoration: line-through\">text</span>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_T0\" style:family=\"text\"><style:text-properties style:text-underline-style=\"none\" style:text-line-through-style=\"solid\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_T0\" >text</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testStrikeWithPTextDecorationLineThrough()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"text-decoration: line-through\">text</p>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_P0\" style:family=\"paragraph\"><style:text-properties style:text-underline-style=\"none\" style:text-line-through-style=\"solid\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_P0\"><text:span text:style-name=\"XDocReport_EmptyText\" >text</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testUnderlineStrikeWithSpanTextDecorationUnderlineLineThrough()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<span style=\"text-decoration: underline line-through\">text</span>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_T0\" style:family=\"text\"><style:text-properties style:text-underline-style=\"solid\" style:text-underline-width=\"auto\" style:text-underline-color=\"font-color\" style:text-line-through-style=\"solid\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_T0\" >text</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testUnderlineStrikeWithPTextDecorationUnderlineLineThrough()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"text-decoration: underline line-through\">text</p>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_P0\" style:family=\"paragraph\"><style:text-properties style:text-underline-style=\"solid\" style:text-underline-width=\"auto\" style:text-underline-color=\"font-color\" style:text-line-through-style=\"solid\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_P0\"><text:span text:style-name=\"XDocReport_EmptyText\" >text</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testSubscriptWithSub()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<sub>text</sub>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_Subscript\" >text</text:span>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testSubscriptWithSpanVerticalAlignSub()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<span style=\"vertical-align: sub\">text</span>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_T0\" style:family=\"text\"><style:text-properties style:text-position=\"sub\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_T0\" >text</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testSubscriptWithPVerticalAlignSub()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"vertical-align: sub\">text</p>", handler );
        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_P0\" style:family=\"paragraph\"><style:text-properties style:text-position=\"sub\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_P0\"><text:span text:style-name=\"XDocReport_EmptyText\" >text</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testSuperscriptWithSup()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<sup>text</sup>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_Superscript\" >text</text:span>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testSuperscriptWithSpanVerticalAlignSuper()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<span style=\"vertical-align: super\">text</span>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_T0\" style:family=\"text\"><style:text-properties style:text-position=\"super\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_T0\" >text</text:span>", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testSuperscriptWithPVerticalAlignSuper()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"vertical-align: super\">text</p>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_P0\" style:family=\"paragraph\"><style:text-properties style:text-position=\"super\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_P0\"><text:span text:style-name=\"XDocReport_EmptyText\" >text</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testHyperlinkByUsingXDocReport_HyperlinkStyle()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<a href=\"http://code.google.com/p/xdocreport/\" >XDocReport</a>", handler );

        // Assert.assertEquals( "", handler.getTextBefore() );
        // Assert.assertEquals(
        // "<w:hyperlink r:id=\"___rId0\" w:history=\"1\"> <w:proofErr w:type=\"spellStart\" /><w:r w:rsidRPr=\"001D30B5\"><w:rPr><w:rStyle w:val=\"XDocReport_Hyperlink\" /></w:rPr><w:t>XDocReport</w:t></w:r><w:proofErr w:type=\"spellEnd\" /></w:hyperlink>",
        // handler.getTextBody() );
        // Assert.assertEquals( "", handler.getTextEnd() );

        // HyperlinkRegistry registry = ODTContextHelper.getHyperlinkRegistry( context, "content.xml" );
        // Assert.assertNotNull( registry );
        // Assert.assertEquals( 1, registry.getHyperlinks().size() );
        //
        // HyperlinkInfo hyperlinkInfo = registry.getHyperlinks().get( 0 );
        // Assert.assertEquals( "___rId0", hyperlinkInfo.getId() );
        // Assert.assertEquals( "http://code.google.com/p/xdocreport/", hyperlinkInfo.getTarget() );
        // Assert.assertEquals( "External", hyperlinkInfo.getTargetMode() );
    }

    @Test
    public void testHyperlinkByUsingDefaultHyperlinkStyle()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by ODTStylesPreprocessor which search
        // hyperlink style from the word/styles.xml entry of the docx)
        // DefaultStyle defaultStyle = new DefaultStyle();
        // defaultStyle.setHyperLinkStyleId( "DefaultHyperlink" );
        // ODTContextHelper.putDefaultStyle( context, defaultStyle );
        //
        // BufferedElement parent = null;
        //
        // ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        // IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        // formatter.transform( "<a href=\"http://code.google.com/p/xdocreport/\" >XDocReport</a>", handler );
        //
        // Assert.assertEquals( "", handler.getTextBefore() );
        // Assert.assertEquals(
        // "<w:hyperlink r:id=\"___rId0\" w:history=\"1\"> <w:proofErr w:type=\"spellStart\" /><w:r w:rsidRPr=\"001D30B5\"><w:rPr><w:rStyle w:val=\"DefaultHyperlink\" /></w:rPr><w:t>XDocReport</w:t></w:r><w:proofErr w:type=\"spellEnd\" /></w:hyperlink>",
        // handler.getTextBody() );
        // Assert.assertEquals( "", handler.getTextEnd() );
        //
        // HyperlinkRegistry registry = ODTContextHelper.getHyperlinkRegistry( context, "content.xml" );
        // Assert.assertNotNull( registry );
        // Assert.assertEquals( 1, registry.getHyperlinks().size() );
        //
        // HyperlinkInfo hyperlinkInfo = registry.getHyperlinks().get( 0 );
        // Assert.assertEquals( "___rId0", hyperlinkInfo.getId() );
        // Assert.assertEquals( "http://code.google.com/p/xdocreport/", hyperlinkInfo.getTarget() );
        // Assert.assertEquals( "External", hyperlinkInfo.getTargetMode() );
    }

    @Test
    public void testHeaderByUsingXDocReport_HeadingStyle()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "xxx<h1>Title1</h1><h2>Title2</h2><h3>Title3</h3><h4>Title4</h4><h5>Title5</h5><h6>Title6</h6>",
                             handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_EmptyText\" >xxx</text:span>", handler.getTextBody() );
        Assert.assertEquals( "<text:h text:style-name=\"Heading_20_1\" text:outline-level=\"1\">Title1</text:h>"
            + "<text:h text:style-name=\"Heading_20_2\" text:outline-level=\"2\">Title2</text:h>"
            + "<text:h text:style-name=\"Heading_20_3\" text:outline-level=\"3\">Title3</text:h>"
            + "<text:h text:style-name=\"Heading_20_4\" text:outline-level=\"4\">Title4</text:h>"
            + "<text:h text:style-name=\"Heading_20_5\" text:outline-level=\"5\">Title5</text:h>"
            + "<text:h text:style-name=\"Heading_20_6\" text:outline-level=\"6\">Title6</text:h>", handler.getTextEnd() );
    }

    @Test
    public void testHeaderByUsingDefaultHeadingStyle()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by ODTStylesPreprocessor which search
        // heading style from the word/styles.xml entry of the docx)
        // DefaultStyle defaultStyle = new DefaultStyle();
        // defaultStyle.addHeaderStyle( 1, "Heading1" );
        // defaultStyle.addHeaderStyle( 2, "Heading2" );
        // ODTContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "xxx<h1>Title1</h1><h2>Title2</h2><h3>Title3</h3><h4>Title4</h4><h5>Title5</h5><h6>Title6</h6>",
                             handler );

        // Assert.assertEquals( "", handler.getTextBefore() );
        // Assert.assertEquals( "<w:r><w:t xml:space=\"preserve\" >xxx</w:t></w:r>", handler.getTextBody() );
        // Assert.assertEquals( "<w:p><w:pPr><w:pStyle w:val=\"Heading1\" /></w:pPr><w:r><w:t>Title1</w:t></w:r></w:p>"
        // + "<w:p><w:pPr><w:pStyle w:val=\"Heading2\" /></w:pPr><w:r><w:t>Title2</w:t></w:r></w:p>"
        // + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_3\" /></w:pPr><w:r><w:t>Title3</w:t></w:r></w:p>"
        // + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_4\" /></w:pPr><w:r><w:t>Title4</w:t></w:r></w:p>"
        // + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_5\" /></w:pPr><w:r><w:t>Title5</w:t></w:r></w:p>"
        // + "<w:p><w:pPr><w:pStyle w:val=\"XDocReport_Heading_6\" /></w:pPr><w:r><w:t>Title6</w:t></w:r></w:p>",
        // handler.getTextEnd() );
    }

    @Test
    public void testHeaderAndText()
        throws Exception
    {
        IContext context = new MockContext();

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<h1>Title1</h1>paragraph1", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:h text:style-name=\"Heading_20_1\" text:outline-level=\"1\">Title1</text:h>"
            + "<text:p><text:span text:style-name=\"XDocReport_EmptyText\" >paragraph1</text:span></text:p>", handler.getTextEnd() );
    }

    @Test
    public void testHeaderAndTextInParagraph()
        throws Exception
    {
        IContext context = new MockContext();

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<h1>Title1</h1><p>paragraph1</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:h text:style-name=\"Heading_20_1\" text:outline-level=\"1\">Title1</text:h>"
            + "<text:p><text:span text:style-name=\"XDocReport_EmptyText\" >paragraph1</text:span></text:p>", handler.getTextEnd() );
    }

    @Test
    public void testHeaderAndTextAndParagraph()
        throws Exception
    {
        IContext context = new MockContext();

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<h1>Title1</h1>text<p>paragraph</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:h text:style-name=\"Heading_20_1\" text:outline-level=\"1\">Title1</text:h><text:p><text:span text:style-name=\"XDocReport_EmptyText\" >text</text:span><text:p><text:span text:style-name=\"XDocReport_EmptyText\" >paragraph</text:span></text:p></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testParagraphAtFirst()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p>bla bla bla</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p><text:span text:style-name=\"XDocReport_EmptyText\" >bla bla bla</text:span></text:p>", handler.getTextEnd() );
    }

    @Test
    public void testParagraph()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "some <strong>text</strong><p>paragraph1</p><p>paragraph2</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_EmptyText\" >some </text:span><text:span text:style-name=\"XDocReport_Bold\" >text</text:span>",
                             handler.getTextBody() );
        Assert.assertEquals( "<text:p><text:span text:style-name=\"XDocReport_EmptyText\" >paragraph1</text:span></text:p><text:p><text:span text:style-name=\"XDocReport_EmptyText\" >paragraph2</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testOrderedList()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by ODTNumberingPreprocessor which search
        // numbering from the word/numbering.xml entry of the docx)
        // DefaultStyle defaultStyle = new DefaultStyle();
        // //defaultStyle.setNumIdForOrdererList( 2 );
        // ODTContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<ol><li>item1</li><li>item2</li></ol>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:list text:style-name=\"XDocReport_OL\">"
            + "<text:list-item text:style-name=\"XDocReport_OL\">" + "<text:p text:style-name=\"XDocReport_OL_P\">"
            + "<text:span text:style-name=\"XDocReport_EmptyText\" >item1</text:span>" + "</text:p>" + "</text:list-item>"
            + "<text:list-item text:style-name=\"XDocReport_OL\">" + "<text:p text:style-name=\"XDocReport_OL_P\">"
            + "<text:span text:style-name=\"XDocReport_EmptyText\" >item2</text:span>" + "</text:p>" + "</text:list-item>" + "</text:list>", handler.getTextEnd() );
    }

    @Test
    public void testOrderedListWithSpaceAfterOL()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by ODTNumberingPreprocessor which search
        // numbering from the word/numbering.xml entry of the docx)
        // DefaultStyle defaultStyle = new DefaultStyle();
        // //defaultStyle.setNumIdForOrdererList( 2 );
        // ODTContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<ol> <li>item1</li><li>item2</li></ol>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:list text:style-name=\"XDocReport_OL\">"
            + "<text:list-item text:style-name=\"XDocReport_OL\">" + "<text:p text:style-name=\"XDocReport_OL_P\">"
            + "<text:span text:style-name=\"XDocReport_EmptyText\" >item1</text:span>" + "</text:p>" + "</text:list-item>"
            + "<text:list-item text:style-name=\"XDocReport_OL\">" + "<text:p text:style-name=\"XDocReport_OL_P\">"
            + "<text:span text:style-name=\"XDocReport_EmptyText\" >item2</text:span>" + "</text:p>" + "</text:list-item>" + "</text:list>", handler.getTextEnd() );
    }

    @Test
    public void testUnorderedList()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by ODTNumberingPreprocessor which search
        // numbering from the word/numbering.xml entry of the docx)
        // DefaultStyle defaultStyle = new DefaultStyle();
        // //defaultStyle.setNumIdForUnordererList( 1 );
        // ODTContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<ol><li>item1</li><li>item2</li></ol><ul><li>item1</li><li>item2</li></ul>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:list text:style-name=\"XDocReport_OL\"><text:list-item text:style-name=\"XDocReport_OL\"><text:p text:style-name=\"XDocReport_OL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >item1</text:span></text:p></text:list-item><text:list-item text:style-name=\"XDocReport_OL\"><text:p text:style-name=\"XDocReport_OL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >item2</text:span></text:p></text:list-item></text:list><text:list text:style-name=\"XDocReport_UL\"><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >item1</text:span></text:p></text:list-item><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >item2</text:span></text:p></text:list-item></text:list>",
                             handler.getTextEnd() );
    }

    @Test
    public void testOrderedAndUnorderedList()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by ODTNumberingPreprocessor which search
        // numbering from the word/numbering.xml entry of the docx)
        // DefaultStyle defaultStyle = new DefaultStyle();
        // //defaultStyle.setNumIdForUnordererList( 1 );
        // ODTContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<ul><li>item1</li><li>item2</li></ul>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:list text:style-name=\"XDocReport_UL\"><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >item1</text:span></text:p></text:list-item><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >item2</text:span></text:p></text:list-item></text:list>",
                             handler.getTextEnd() );
    }

    @Test
    public void testComplexList()
        throws Exception
    {
        IContext context = new MockContext();
        // Add default style (in real context, this DefaultStyle is added by ODTNumberingPreprocessor which search
        // numbering from the word/numbering.xml entry of the docx)
        // DefaultStyle defaultStyle = new DefaultStyle();
        // //defaultStyle.setNumIdForUnordererList( 1 );
        // ODTContextHelper.putDefaultStyle( context, defaultStyle );

        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<ul>" + "<li>" + "<strong>Bold</strong> style." + "<ul>" + "<li>zaza</li>"
            + "<li>zaza</li>" + "<li>zaza</li>" + "</ul>" + "</li>" + "<li><em>Italic</em> style." + "<ul>"
            + "<li>zazaaa</li>" + "<li>zzzzzzzzzzzz" + "<ul>" + "<li>ddddddddddddddddddd</li>" + "</ul>" + "</li>"
            + "</ul>" + "</li>" + "<li><strong><em>BoldAndItalic</em></strong> style." + "</li>" + "</ul>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:list text:style-name=\"XDocReport_UL\"><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_Bold\" >Bold</text:span><text:span text:style-name=\"XDocReport_EmptyText\" > style.</text:span></text:p><text:list text:style-name=\"XDocReport_UL\"><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >zaza</text:span></text:p></text:list-item><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >zaza</text:span></text:p></text:list-item><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >zaza</text:span></text:p></text:list-item></text:list></text:list-item><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_Italic\" >Italic</text:span><text:span text:style-name=\"XDocReport_EmptyText\" > style.</text:span></text:p><text:list text:style-name=\"XDocReport_UL\"><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >zazaaa</text:span></text:p></text:list-item><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >zzzzzzzzzzzz</text:span></text:p><text:list text:style-name=\"XDocReport_UL\"><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_EmptyText\" >ddddddddddddddddddd</text:span></text:p></text:list-item></text:list></text:list-item></text:list></text:list-item><text:list-item text:style-name=\"XDocReport_UL\"><text:p text:style-name=\"XDocReport_UL_P\"><text:span text:style-name=\"XDocReport_Bold\" ><text:span text:style-name=\"XDocReport_Italic\" >BoldAndItalic</text:span></text:span><text:span text:style-name=\"XDocReport_EmptyText\" > style.</text:span></text:p></text:list-item></text:list>",
                             handler.getTextEnd() );
    }

    @Test
    public void testPageBreakBefore()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"page-break-before:always;\">bla bla bla</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_ParaBreakBefore\"><text:span text:style-name=\"XDocReport_EmptyText\" >bla bla bla</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testPageBreakAfter()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"page-break-after:always;\">bla bla bla</p>", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_ParaBreakAfter\"><text:span text:style-name=\"XDocReport_EmptyText\" >bla bla bla</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testLineBreak()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "a<br/>b", handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "<text:span text:style-name=\"XDocReport_EmptyText\" >a</text:span><text:line-break /><text:span text:style-name=\"XDocReport_EmptyText\" >b</text:span>",
                             handler.getTextBody() );
        Assert.assertEquals( "", handler.getTextEnd() );
    }

    @Test
    public void testAll()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        context.put( "comments_html", "<b>Bold</b> text" );
        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p>\r\n\tHere are severals styles :</p>\r\n<ul>\r\n\t<li>\r\n\t\t<strong>Bold</strong> style.</li>\r\n\t<li>\r\n\t\t<em>Italic</em> style.</li>\r\n\t<li>\r\n\t\t<strong><em>BoldAndItalic</em></strong> style.</li>\r\n</ul>\r\n<p>\r\n\tHere are 3 styles :</p>\r\n<ol>\r\n\t<li>\r\n\t\t<strong>Bold</strong> style.</li>\r\n\t<li>\r\n\t\t<em>Italic</em> style.</li>\r\n\t<li>\r\n\t\t<strong><em>BoldAndItalic</em></strong> style.</li>\r\n</ol>\r\n<p>\r\n\tXDocReport can manage thoses styles.</p>\r\n<h1>\r\n\tsqsq</h1>\r\n<p>\r\n\tzazazaa</p>\r\n ",
                             handler );

        // System.err.println(handler.getTextEnd());

    }

    @Test
    public void testMultipleSpans()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p>Before <span style=\"text-decoration: underline;\">Underline <span style=\"font-weight: bold;\">and bold <span style=\"font-style: italic;\">and italic</span>"
                                 + " No italics</span> No bold</span> No Underline</p>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_T0\" style:family=\"text\"><style:text-properties style:text-underline-style=\"solid\" style:text-underline-width=\"auto\" style:text-underline-color=\"font-color\" /></style:style><style:style style:name=\"XDocReport_T1\" style:family=\"text\"><style:text-properties fo:font-weight=\"bold\" /></style:style><style:style style:name=\"XDocReport_T2\" style:family=\"text\"><style:text-properties fo:font-style=\"italic\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p><text:span text:style-name=\"XDocReport_EmptyText\" >Before </text:span><text:span text:style-name=\"XDocReport_T0\" >Underline <text:span text:style-name=\"XDocReport_T1\" >and bold <text:span text:style-name=\"XDocReport_T2\" >and italic</text:span> No italics</text:span> No bold</text:span><text:span text:style-name=\"XDocReport_EmptyText\" > No Underline</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testSpansInP()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<p style=\"font-weight:bold;font-style:italic;\" >AAA<span>BBB</span></p>", handler );

        // styles.xml
        IODTStylesGenerator styleGen = ODTContextHelper.getStylesGenerator( context );
        Assert.assertNotNull( styleGen );
        Assert.assertEquals( "<style:style style:name=\"XDocReport_P0\" style:family=\"paragraph\"><style:text-properties fo:font-weight=\"bold\" fo:font-style=\"italic\" /></style:style>",
                             styleGen.getDynamicStyles() );

        // content.xml
        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<text:p text:style-name=\"XDocReport_P0\"><text:span text:style-name=\"XDocReport_EmptyText\" >AAA</text:span><text:span text:style-name=\"XDocReport_EmptyText\" >BBB</text:span></text:p>",
                             handler.getTextEnd() );
    }

    @Test
    public void testTable()
        throws Exception
    {
        IContext context = new MockContext();
        BufferedElement parent = null;

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new ODTDocumentHandler( parent, context, "content.xml" );
        formatter.transform( "<table><tbody>"
            + "<tr><td>A</td><td>B</td></tr>"
            + "<tr><td>C</td><td>D</td></tr>"
            + "<tr><td>E</td><td>F</td></tr>"
            + "</tbody></table>",
                             handler );

        Assert.assertEquals( "", handler.getTextBefore() );
        Assert.assertEquals( "", handler.getTextBody() );
        Assert.assertEquals( "<table:table>"
            + "<table:table-column table:number-columns-repeated=\"2\" ></table:table-column>"
            + "<table:table-row><table:table-cell><text:p><text:span text:style-name=\"XDocReport_EmptyText\" >A</text:span></text:p></table:table-cell><table:table-cell><text:p><text:span text:style-name=\"XDocReport_EmptyText\" >B</text:span></text:p></table:table-cell></table:table-row>"
            + "<table:table-row><table:table-cell><text:p><text:span text:style-name=\"XDocReport_EmptyText\" >C</text:span></text:p></table:table-cell><table:table-cell><text:p><text:span text:style-name=\"XDocReport_EmptyText\" >D</text:span></text:p></table:table-cell></table:table-row>"
            + "<table:table-row><table:table-cell><text:p><text:span text:style-name=\"XDocReport_EmptyText\" >E</text:span></text:p></table:table-cell><table:table-cell><text:p><text:span text:style-name=\"XDocReport_EmptyText\" >F</text:span></text:p></table:table-cell></table:table-row>"
            + "</table:table>",
                             handler.getTextEnd() );
    }
}
