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
package fr.opensagres.xdocreport.document.docx.preprocessor.notes.endnotes;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocxPreprocessor;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.InitialNoteInfoMap;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.NoteInfo;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.NoteUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.endnotes.DocxEndnotesPreprocessor;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityDocumentFormatter;

public class DocxEndnotesPreprocessorWithVelocityTestCase
{

    @Test
    public void testEndnote()
        throws Exception
    {
        DocxEndnotesPreprocessor  preprocessor = new DocxEndnotesPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<w:endnotes"
                + " xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\""
                + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
                + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                + " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\""
                + " xmlns:v=\"urn:schemas-microsoft-com:vml\""
                + " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\""
                + " xmlns:w10=\"urn:schemas-microsoft-com:office:word\""
                + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\""
                + " xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"

                    + "<w:endnote w:id=\"2\">"
                    + "<w:p w:rsidR=\"00D31606\" w:rsidRDefault=\"00D31606\" w:rsidP=\"000A7B59\">"
                        + "<w:r>"
                            + "<w:rPr>"
                                + "<w:rStyle w:val=\"Appelnotedebasdep\"/>"
                            + "</w:rPr>"
                            + "<w:endnoteRef/>"
                        + "</w:r>"
                        + "<w:r w:rsidR=\"009E6D11\">"
                            + "<w:t xml:space=\"preserve\"></w:t>"
                        + "</w:r>"
                        + "<w:fldSimple w:instr=\" MERGEFIELD  ${d.mail}  \\* MERGEFORMAT \">"
                            + "<w:r w:rsidR=\"009E6D11\">"
                                + "<w:rPr>"
                                    + "<w:noProof/>"
                                + "</w:rPr>"
                                + "<w:t>«${d.mail}»</w:t>"
                            + "</w:r>"
                        + "</w:fldSimple>"
                    + "</w:p>"
                    + "</w:endnote>"

                 + " </w:endnotes>" ,"UTF-8");

        StringWriter writer = new StringWriter();
        IDocumentFormatter formatter = new VelocityDocumentFormatter();
        Map<String, Object> sharedContext = new HashMap<String, Object>();
        preprocessor.preprocess( "word/endnotes.xml", stream, writer, null, formatter, sharedContext );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        + "<w:endnotes"
                        + " xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\""
                        + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
                        + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                        + " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\""
                        + " xmlns:v=\"urn:schemas-microsoft-com:vml\""
                        + " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\""
                        + " xmlns:w10=\"urn:schemas-microsoft-com:office:word\""
                        + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\""
                        + " xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"

                        + "#foreach($___NoEscapeNoteInfo in $___EndnoteRegistry.getNotes('2'))"

                            //+ "<w:endnote w:id=\"2\">"
                            + "<w:endnote w:id=\"$___NoEscapeNoteInfo.Id\">"

                            //+ "<w:p w:rsidR=\"00D31606\" w:rsidRDefault=\"00D31606\" w:rsidP=\"000A7B59\">"
                            //    + "<w:r>"
                            //        + "<w:rPr>"
                            //            + "<w:rStyle w:val=\"Appelnotedebasdep\"/>"
                            //        + "</w:rPr>"
                            //        + "<w:endnoteRef/>"
                            //    + "</w:r>"
                            //    + "<w:r w:rsidR=\"009E6D11\">"
                            //        + "<w:t xml:space=\"preserve\"></w:t>"
                            //    + "</w:r>"
                            //    + "<w:fldSimple w:instr=\" MERGEFIELD  ${d.mail}  \\* MERGEFORMAT \">"
                            //        + "<w:r w:rsidR=\"009E6D11\">"
                            //            + "<w:rPr>"
                            //                + "<w:noProof/>"
                            //            + "</w:rPr>"
                            //            + "<w:t>«${d.mail}»</w:t>"
                            //        + "</w:r>"
                            //    + "</w:fldSimple>"
                            //+ "</w:p>"

                            + "$___NoEscapeNoteInfo.Content"
                            + "</w:endnote>"

                        + "#{end}"

                         + ""
                         + " </w:endnotes>", writer.toString() );

        InitialNoteInfoMap  infoMap = NoteUtils.getInitialEndNoteInfoMap( sharedContext );
        Assert.assertNotNull( infoMap );
        Assert.assertEquals(1, infoMap.values().size() );
        Assert.assertEquals("<w:p w:rsidR=\"00D31606\" w:rsidRDefault=\"00D31606\" w:rsidP=\"000A7B59\">"
                        + "<w:r>"
                        + "<w:rPr>"
                            + "<w:rStyle w:val=\"Appelnotedebasdep\"/>"
                        + "</w:rPr>"
                        + "<w:endnoteRef/>"
                    + "</w:r>"
                    + "<w:r w:rsidR=\"009E6D11\">"
                        + "<w:t xml:space=\"preserve\"/>"
                    + "</w:r>"
                    //+ "<w:fldSimple w:instr=\" MERGEFIELD  ${d.mail}  \\* MERGEFORMAT \">"
                        + "<w:r w:rsidR=\"009E6D11\">"
                            + "<w:rPr>"
                                + "<w:noProof/>"
                            + "</w:rPr>"
                    //        + "<w:t>«${d.mail}»</w:t>"
                            + "<w:t>${d.mail}</w:t>"
                        + "</w:r>"
                    //+ "</w:fldSimple>"
                + "</w:p>", ((NoteInfo)infoMap.values().toArray( )[0]).getContent());
    }

    @Test
    public void testEndnoteReference()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<w:document"
                + " xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\""
                + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
                + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                + " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\""
                + " xmlns:v=\"urn:schemas-microsoft-com:vml\""
                + " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\""
                + " xmlns:w10=\"urn:schemas-microsoft-com:office:word\""
                + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\""
                + " xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"

                    + "<w:r w:rsidR=\"00D31606\">"
                    + "<w:rPr>"
                        + "<w:rStyle w:val=\"Appelnotedebasdep\"/>"
                    + "</w:rPr>"
                    + "<w:endnoteReference w:id=\"2\"/>"
                    + "</w:r>"

                 + " </w:document>" );

        StringWriter writer = new StringWriter();
        IDocumentFormatter formatter = new VelocityDocumentFormatter();
        Map<String, Object> sharedContext = new HashMap<String, Object>();

        InitialNoteInfoMap  infoMap = new  InitialNoteInfoMap();
        NoteUtils.putInitialEndNoteInfoMap( sharedContext, infoMap   );
        infoMap.put( "2", new NoteInfo( "2", "XXXX" ) );

        preprocessor.preprocess ( "word/document.xml", stream, writer, null, formatter, sharedContext );

        Assert.assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        + "<w:document"
                        + " xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\""
                        + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
                        + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                        + " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\""
                        + " xmlns:v=\"urn:schemas-microsoft-com:vml\""
                        + " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\""
                        + " xmlns:w10=\"urn:schemas-microsoft-com:office:word\""
                        + " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\""
                        + " xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"

                            + "<w:r w:rsidR=\"00D31606\">"
                            + "<w:rPr>"
                                + "<w:rStyle w:val=\"Appelnotedebasdep\"/>"
                            + "</w:rPr>"
                            //+ "<w:endnoteReference w:id=\"2\"/>"
                            + "<w:endnoteReference w:id=\"#define($___note)XXXX#{end}${___EndnoteRegistry.registerNote('2',$___note)}\"/>"
                            + "</w:r>"

                         + " </w:document>", writer.toString() );

    }

}
