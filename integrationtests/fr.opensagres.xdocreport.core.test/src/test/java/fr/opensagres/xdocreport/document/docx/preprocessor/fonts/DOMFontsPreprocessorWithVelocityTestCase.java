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
import fr.opensagres.xdocreport.template.velocity.VelocityDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityTemplateEngine;
import fr.opensagres.xdocreport.template.velocity.internal.XDocVelocityContext;

public class DOMFontsPreprocessorWithVelocityTestCase
{
    @Test
    public void testPreprocessFontsAndMergeTempplate()
        throws Exception
    {
        String xml =    " <w:document"
                        + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                            + " <w:body>"
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
                            + " </w:body>"
                        + " </w:document>";
        
        
        Document document = DOMUtils.load( xml );
        
        // 1) Test Fonts preprocessing with Velocity
        IDocumentFormatter formatter = new VelocityDocumentFormatter();
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
                            + " <w:body>"
                                + "<w:p>"
                                    + "<w:pPr>"
                                        + "<w:spacing w:after=\"0\" w:line=\"360\" w:lineRule=\"auto\"/>"
                                        + "<w:jc w:val=\"both\"/>"
                                        + "<w:rPr>"
                                            //+ "<w:rFonts w:ascii=\"Arial\" w:hAnsi=\"Arial\" w:cs=\"Arial\"/>"
                                            + "<w:rFonts w:ascii=\"#if($___fontName)$___fontName#{else}Arial#end\""                                                       
                                                      + " w:cs=\"#if($___fontName)$___fontName#{else}Arial#end\"" 
                                                      + " w:hAnsi=\"#if($___fontName)$___fontName#{else}Arial#end\""
                                                      + "/>"                                                                                                
                                            //+ "<w:sz w:val=\"24\"/>"
                                            + "<w:sz w:val=\"#if($___fontSize)$___fontSize#{else}24#end\"/>"                                                      
                                            //+ "<w:szCs w:val=\"24\"/>"
                                            + "<w:szCs w:val=\"#if($___fontSize)$___fontSize#{else}24#end\"/>"
                                        + "</w:rPr>"
                                    + "</w:pPr>"
                                + "</w:p>"
                            + " </w:body>"
                        + " </w:document>", s );

        // 2) Test merge template with Java model 
        ITemplateEngine templateEngine = new VelocityTemplateEngine();
        
        IContext context = new XDocVelocityContext();
        context.put( "name", "word" );

        // Change every font name+size with Magneto + 40
        context.put( DOMFontsPreprocessor.FONT_NAME_KEY, "Magneto" );
        context.put( DOMFontsPreprocessor.FONT_SIZE_KEY, "40" );
        
        Reader reader = new StringReader( s );
        StringWriter mergedWriter = new StringWriter(); 
        templateEngine.process( "word/document.xml", context, reader, mergedWriter );
        
        Assert.assertEquals( "<w:document"
                        + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                            + " <w:body>"
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
                                            //+ "<w:szCs w:val=\"24\"/>"
                                            + "<w:szCs w:val=\"40\"/>"
                                        + "</w:rPr>"
                                    + "</w:pPr>"
                                + "</w:p>"
                            + " </w:body>"
                        + " </w:document>", mergedWriter.toString() );

    }
}
