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
package fr.opensagres.xdocreport.document.docx.preprocessor.numbering;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering.DocxNumberingPreprocessor;
import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerDocumentFormatter;

public class DocxNumberingPreprocessorWithFreemarkerTestCase
{

    @Test
    public void testNumberingNoneAbstractNumber()
        throws Exception
    {
        DocxNumberingPreprocessor preprocessor = new DocxNumberingPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:numbering"
                + " xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\""
                + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
                + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                + " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\""
                + " xmlns:v=\"urn:schemas-microsoft-com:vml\""
                + " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\""
                + " xmlns:w10=\"urn:schemas-microsoft-com:office:word\""
                + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\""
                + " xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">" 
                + " </w:numbering>" );

        StringWriter writer = new StringWriter();
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();
        Map<String, Object> sharedContext = new HashMap<String, Object>();
        preprocessor.preprocess( "word/numbering.xml", stream, writer, null, formatter, sharedContext );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:numbering"
                + " xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\""
                + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
                + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                + " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\""
                + " xmlns:v=\"urn:schemas-microsoft-com:vml\""
                + " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\""
                + " xmlns:w10=\"urn:schemas-microsoft-com:office:word\""
                + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\""
                + " xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\"> " 
                
                    + "${___NoEscapeStylesGenerator.generateAbstractNumBullet(___DefaultStyle)}" 
                    + "[#if ___NumberingRegistry??]"
                	+ "[#list ___NumberingRegistry.numbers as ___NumberInfo]"
                		+ "[#if ___NumberInfo.ordered??]"
                			+ "${___NoEscapeStylesGenerator.generateAbstractNumDecimal(___DefaultStyle,___NumberInfo.abstractNumId)}"
                		+ "[/#if]"
                	+ "[/#list]"
                + "[/#if]" 
                    + "[#if ___NumberingRegistry??]" 
                        + "[#list ___NumberingRegistry.numbers as ___NumberInfo]" 
                            + "<w:num w:numId=\"${___NumberInfo.numId}\">" 
                                + "<w:abstractNumId w:val=\"${___NumberInfo.abstractNumId}\"/>" 
                            + "</w:num>" 
                        + "[/#list]" 
                     + "[/#if]"
                
                + "</w:numbering>", writer.toString() );       
        
        DefaultStyle defaultStyle = DocxContextHelper.getDefaultStyle( sharedContext );
        Assert.assertNotNull( defaultStyle );
        Assert.assertNull( defaultStyle.getAbstractNumIdForOrdererList() );
        Assert.assertNull( defaultStyle.getAbstractNumIdForUnordererList() );        
    }

    @Test
    public void testNumberingWithAbstractNumberBullet()
        throws Exception
    {
        DocxNumberingPreprocessor preprocessor = new DocxNumberingPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:numbering"
                + " xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\""
                + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
                + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                + " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\""
                + " xmlns:v=\"urn:schemas-microsoft-com:vml\""
                + " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\""
                + " xmlns:w10=\"urn:schemas-microsoft-com:office:word\""
                + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\""
                + " xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
                    + "<w:abstractNum w:abstractNumId=\"10\">"
                        + "<w:lvl w:ilvl=\"0\">"
                            + "<w:numFmt w:val=\"bullet\"/>"
                        + "</w:lvl>"
                    + "</w:abstractNum>" 
                + "</w:numbering>" );

        StringWriter writer = new StringWriter();
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();
        Map<String, Object> sharedContext = new HashMap<String, Object>();
        preprocessor.preprocess( "word/numbering.xml", stream, writer, null, formatter, sharedContext );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:numbering"
                + " xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\""
                + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
                + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                + " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\""
                + " xmlns:v=\"urn:schemas-microsoft-com:vml\""
                + " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\""
                + " xmlns:w10=\"urn:schemas-microsoft-com:office:word\""
                + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\""
                + " xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">" 
                    + "<w:abstractNum w:abstractNumId=\"10\">"
                        + "<w:lvl w:ilvl=\"0\">"
                            + "<w:numFmt w:val=\"bullet\"/>"
                        + "</w:lvl>"
                    + "</w:abstractNum>" 
                
                //+" ${___NoEscapeStylesGenerator.generateAbstractNumBullet(___DefaultStyle)}" 
                + "[#if ___NumberingRegistry??]"
            	+ "[#list ___NumberingRegistry.numbers as ___NumberInfo]"
            		+ "[#if ___NumberInfo.ordered??]"
            			+ "${___NoEscapeStylesGenerator.generateAbstractNumDecimal(___DefaultStyle,___NumberInfo.abstractNumId)}"
            		+ "[/#if]"
            	+ "[/#list]"
            + "[/#if]" 
                + "[#if ___NumberingRegistry??]" 
                    + "[#list ___NumberingRegistry.numbers as ___NumberInfo]" 
                        + "<w:num w:numId=\"${___NumberInfo.numId}\">" 
                            + "<w:abstractNumId w:val=\"${___NumberInfo.abstractNumId}\"/>" 
                        + "</w:num>" 
                    + "[/#list]" 
                 + "[/#if]"
                
                + "</w:numbering>", writer.toString() );        
        
        DefaultStyle defaultStyle = DocxContextHelper.getDefaultStyle( sharedContext );
        Assert.assertNotNull( defaultStyle );
        Assert.assertNull( defaultStyle.getAbstractNumIdForOrdererList() );
        Assert.assertNotNull( defaultStyle.getAbstractNumIdForUnordererList() );
        Assert.assertEquals( 10, defaultStyle.getAbstractNumIdForUnordererList().intValue() );
    }
    
    @Test
    public void testNumberingWithAbstractNumberDecimal()
        throws Exception
    {
        DocxNumberingPreprocessor preprocessor = new DocxNumberingPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:numbering"
                + " xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\""
                + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
                + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                + " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\""
                + " xmlns:v=\"urn:schemas-microsoft-com:vml\""
                + " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\""
                + " xmlns:w10=\"urn:schemas-microsoft-com:office:word\""
                + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\""
                + " xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
                    + "<w:abstractNum w:abstractNumId=\"10\">"
                        + "<w:lvl w:ilvl=\"0\">"
                            + "<w:numFmt w:val=\"decimal\"/>"
                        + "</w:lvl>"
                    + "</w:abstractNum>" 
                + "</w:numbering>" );

        StringWriter writer = new StringWriter();
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();
        Map<String, Object> sharedContext = new HashMap<String, Object>();
        preprocessor.preprocess( "word/numbering.xml", stream, writer, null, formatter, sharedContext );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<w:numbering"
                + " xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\""
                + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
                + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                + " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\""
                + " xmlns:v=\"urn:schemas-microsoft-com:vml\""
                + " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\""
                + " xmlns:w10=\"urn:schemas-microsoft-com:office:word\""
                + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\""
                + " xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">" 
                    + "<w:abstractNum w:abstractNumId=\"10\">"
                        + "<w:lvl w:ilvl=\"0\">"
                            + "<w:numFmt w:val=\"decimal\"/>"
                        + "</w:lvl>"
                    + "</w:abstractNum>" 
                
                + "${___NoEscapeStylesGenerator.generateAbstractNumBullet(___DefaultStyle)}" 
                + "[#if ___NumberingRegistry??]"
                	+ "[#list ___NumberingRegistry.numbers as ___NumberInfo]"
                		+ "[#if ___NumberInfo.ordered??]"
                			+ "${___NoEscapeStylesGenerator.generateAbstractNumDecimal(___DefaultStyle,___NumberInfo.abstractNumId)}"
                		+ "[/#if]"
                	+ "[/#list]"
                + "[/#if]" 
                + "[#if ___NumberingRegistry??]" 
                    + "[#list ___NumberingRegistry.numbers as ___NumberInfo]" 
                        + "<w:num w:numId=\"${___NumberInfo.numId}\">" 
                            + "<w:abstractNumId w:val=\"${___NumberInfo.abstractNumId}\"/>" 
                        + "</w:num>" 
                    + "[/#list]" 
                 + "[/#if]"
                
                + "</w:numbering>", writer.toString() );        
        
        DefaultStyle defaultStyle = DocxContextHelper.getDefaultStyle( sharedContext );
        Assert.assertNotNull( defaultStyle );
        Assert.assertNotNull( defaultStyle.getAbstractNumIdForOrdererList() );
        Assert.assertEquals( 10, defaultStyle.getAbstractNumIdForOrdererList().intValue() );
        Assert.assertNull( defaultStyle.getAbstractNumIdForUnordererList() );        
    }    
}
