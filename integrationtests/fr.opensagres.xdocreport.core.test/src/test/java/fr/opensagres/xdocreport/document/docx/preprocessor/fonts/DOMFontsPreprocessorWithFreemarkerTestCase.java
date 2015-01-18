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
package fr.opensagres.xdocreport.document.docx.preprocessor.fonts;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import fr.opensagres.xdocreport.core.utils.DOMUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.dom.DOMFontsPreprocessor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerDocumentFormatter;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;
import fr.opensagres.xdocreport.template.freemarker.internal.XDocFreemarkerContext;

public class DOMFontsPreprocessorWithFreemarkerTestCase
{
    @Test
    public void testPreprocessFontsAndMergeTempplate()
        throws Exception
    {
        String xml =    " <w:document"
                        + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                            + "<w:body>"
                                + "<w:p>"
                                    + "<w:pPr>"
                                        + "<w:spacing w:after=\"0\" w:line=\"360\" w:lineRule=\"auto\"/>"
                                        + "<w:jc w:val=\"both\"/>"
                                        + "<w:rPr>"
                                            + "<w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>"
                                            + "<w:sz w:val=\"24\"/>"
                                        + "</w:rPr>"
                                    + "</w:pPr>"
                                + "</w:p>"
                            + "</w:body>"
                        + "</w:document>";
        
        
        Document document = DOMUtils.load( xml );
        
        // 1) Test Fonts preprocessing with Freemarker
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();
        StringWriter writer = new StringWriter();
        DOMFontsPreprocessor.INSTANCE.preprocess( "word/document.xml", document, writer, null, formatter, null );     
        
        String s = writer.toString();
        int index = s.indexOf( "<w:document" );
        if ( index != -1 )
        {
            s = s.substring( index, s.length() );
        }
        Assert.assertEquals( "<w:document"
                        + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                        
                            + "[#if ___fontSize??][#assign ___fontSizeTwo=___fontSize * 2][/#if]"
                            
                            + "<w:body>"
                                + "<w:p>"
                                    + "<w:pPr>"
                                        + "<w:spacing w:after=\"0\" w:line=\"360\" w:lineRule=\"auto\"/>"
                                        + "<w:jc w:val=\"both\"/>"
                                        + "<w:rPr>"
                                            //+ "<w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>"
                                            + "<w:rFonts w:ascii=\"[#if ___fontName??]${___fontName}[#else]Arial[/#if]\""                                                       
                                                      + " w:cs=\"[#if ___fontName??]${___fontName}[#else]Arial[/#if]\"" 
                                                      + " w:hAnsi=\"[#if ___fontName??]${___fontName}[#else]Arial[/#if]\""
                                                      + "/>"                                                                                                
                                            //+ "<w:sz w:val=\"24\"/>"
                                            + "<w:sz w:val=\"[#if ___fontSize??]${___fontSize}[#else]24[/#if]\"/>"                                                      
                                        + "</w:rPr>"
                                    + "</w:pPr>"
                                + "</w:p>"
                            + "</w:body>"
                        + "</w:document>", s );

        // 2) Test merge template with Java model 
        ITemplateEngine templateEngine = new FreemarkerTemplateEngine();
        
        IContext context = new XDocFreemarkerContext();
        context.put( "name", "word" );

        // Change every font name+size with Magneto + 40
        context.put( DOMFontsPreprocessor.FONT_NAME_KEY, "Magneto" );
        context.put( DOMFontsPreprocessor.FONT_SIZE_KEY, 40 );
        
        Reader reader=new StringReader( s );
        StringWriter mergedWriter = new StringWriter(); 
        templateEngine.process( "word/document.xml", context, reader, mergedWriter );
        
        Assert.assertEquals( "<w:document"
                        + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                            + "<w:body>"
                                + "<w:p>"
                                    + "<w:pPr>"
                                        + "<w:spacing w:after=\"0\" w:line=\"360\" w:lineRule=\"auto\"/>"
                                        + "<w:jc w:val=\"both\"/>"
                                        + "<w:rPr>"
                                          //+ "<w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>"
                                          + "<w:rFonts w:ascii=\"Magneto\""                                                       
                                                    + " w:cs=\"Magneto\"" 
                                                    + " w:hAnsi=\"Magneto\""
                                                    + "/>"      
                                            //+ "<w:sz w:val=\"24\"/>"
                                            + "<w:sz w:val=\"40\"/>"
                                        + "</w:rPr>"
                                    + "</w:pPr>"
                                + "</w:p>"
                            + "</w:body>"
                        + "</w:document>", mergedWriter.toString() );

    }
    
    @Test
    public void testPreprocessFontsWithCSAndMergeTempplate()
        throws Exception
    {
        String xml =    " <w:document"
                        + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                            + "<w:body>"
                                + "<w:p>"
                                    + "<w:pPr>"
                                        + "<w:spacing w:after=\"0\" w:line=\"360\" w:lineRule=\"auto\"/>"
                                        + "<w:jc w:val=\"both\"/>"
                                        + "<w:rPr>"
                                            + "<w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>"
                                            + "<w:sz w:val=\"24\"/>"
                                            + "<w:szCs w:val=\"24\"/>"
                                        + "</w:rPr>"
                                    + "</w:pPr>"
                                + "</w:p>"
                            + "</w:body>"
                        + "</w:document>";
        
        
        Document document = DOMUtils.load( xml );
        
        // 1) Test Fonts preprocessing with Freemarker
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();
        StringWriter writer = new StringWriter();
        DOMFontsPreprocessor.INSTANCE.preprocess( "word/document.xml", document, writer, null, formatter, null );     
        
        String s = writer.toString();
        int index = s.indexOf( "<w:document" );
        if ( index != -1 )
        {
            s = s.substring( index, s.length() );
        }
        Assert.assertEquals( "<w:document"
                        + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                        
                            + "[#if ___fontSize??][#assign ___fontSizeTwo=___fontSize * 2][/#if]"
                            
                            + "<w:body>"
                                + "<w:p>"
                                    + "<w:pPr>"
                                        + "<w:spacing w:after=\"0\" w:line=\"360\" w:lineRule=\"auto\"/>"
                                        + "<w:jc w:val=\"both\"/>"
                                        + "<w:rPr>"
                                            //+ "<w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>"
                                            + "<w:rFonts w:ascii=\"[#if ___fontName??]${___fontName}[#else]Arial[/#if]\""                                                       
                                                      + " w:cs=\"[#if ___fontName??]${___fontName}[#else]Arial[/#if]\"" 
                                                      + " w:hAnsi=\"[#if ___fontName??]${___fontName}[#else]Arial[/#if]\""
                                                      + "/>"                                                                                                
                                            //+ "<w:sz w:val=\"24\"/>"
                                            + "<w:sz w:val=\"[#if ___fontSizeTwo??]${___fontSizeTwo}[#else]24[/#if]\"/>"                                                      
                                            //+ "<w:szCs w:val=\"24\"/>"
                                            + "<w:szCs w:val=\"[#if ___fontSize??]${___fontSize}[#else]24[/#if]\"/>"
                                        + "</w:rPr>"
                                    + "</w:pPr>"
                                + "</w:p>"
                            + "</w:body>"
                        + "</w:document>", s );

        // 2) Test merge template with Java model 
        ITemplateEngine templateEngine = new FreemarkerTemplateEngine();
        
        IContext context = new XDocFreemarkerContext();
        context.put( "name", "word" );

        // Change every font name+size with Magneto + 40
        context.put( DOMFontsPreprocessor.FONT_NAME_KEY, "Magneto" );
        context.put( DOMFontsPreprocessor.FONT_SIZE_KEY, 40 );
        
        Reader reader=new StringReader( s );
        StringWriter mergedWriter = new StringWriter(); 
        templateEngine.process( "word/document.xml", context, reader, mergedWriter );
        
        Assert.assertEquals( "<w:document"
                        + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                            + "<w:body>"
                                + "<w:p>"
                                    + "<w:pPr>"
                                        + "<w:spacing w:after=\"0\" w:line=\"360\" w:lineRule=\"auto\"/>"
                                        + "<w:jc w:val=\"both\"/>"
                                        + "<w:rPr>"
                                          //+ "<w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>"
                                          + "<w:rFonts w:ascii=\"Magneto\""                                                       
                                                    + " w:cs=\"Magneto\"" 
                                                    + " w:hAnsi=\"Magneto\""
                                                    + "/>"      
                                            //+ "<w:sz w:val=\"24\"/>"
                                            + "<w:sz w:val=\"80\"/>"
                                            //+ "<w:szCs w:val=\"24\"/>"
                                            + "<w:szCs w:val=\"40\"/>"
                                        + "</w:rPr>"
                                    + "</w:pPr>"
                                + "</w:p>"
                            + "</w:body>"
                        + "</w:document>", mergedWriter.toString() );

    }
}
