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

import junit.framework.TestCase;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocxPreprocessor;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerDocumentFormatter;

public class DocxPreprocessorImageWithFreemarkerTestCase
    extends TestCase
{

    private static final String LOGO_IMAGE_XML =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<w:document "
            + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
            + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
            + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
            + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
            + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
            + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
            + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
            + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
            + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
            + "<w:p w:rsidR=\"0064051C\" w:rsidRDefault=\"0064051C\">"
            + "<w:bookmarkStart w:id=\"0\" w:name=\"logo\"/>"
            + "<w:r>"
            + "<w:rPr>"
            + "<w:noProof/>"
            + "</w:rPr>"
            + "<w:drawing>"
            + "<wp:inline distT=\"0\" distB=\"0\" distL=\"0\" distR=\"0\">"
            + "<wp:extent cx=\"266700\" cy=\"285750\"/>"
            + "<wp:effectExtent l=\"19050\" t=\"0\" r=\"0\" b=\"0\"/>"
            + "<wp:docPr id=\"1\" name=\"Image 0\" descr=\"template.png\"/>"
            + "<wp:cNvGraphicFramePr>"
            + "<a:graphicFrameLocks xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" noChangeAspect=\"1\"/>"
            + "</wp:cNvGraphicFramePr>"
            + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
            + "<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
            + "<pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" + "<pic:nvPicPr>"
            + "<pic:cNvPr id=\"0\" name=\"template.png\"/>" + "<pic:cNvPicPr/>" + "</pic:nvPicPr>" + "<pic:blipFill>"

            + "<a:blip r:embed=\"rId5\"/>"

            + "<a:stretch>" + "<a:fillRect/>" + "</a:stretch>" + "</pic:blipFill>" + "<pic:spPr>" + "<a:xfrm>"
            + "<a:off x=\"0\" y=\"0\"/>" + "<a:ext cx=\"266700\" cy=\"285750\"/>" + "</a:xfrm>"
            + "<a:prstGeom prst=\"rect\">" + "<a:avLst/>" + "</a:prstGeom>" + "</pic:spPr>" + "</pic:pic>"
            + "</a:graphicData>" + "</a:graphic>" + "</wp:inline>" + "</w:drawing>" + "</w:r>"
            + "<w:bookmarkEnd w:id=\"0\"/>" + "</w:p>" + "</w:document>";

    public void testImageWithNullFieldsMetadata()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream(LOGO_IMAGE_XML, "UTF-8" );
        StringWriter writer = new StringWriter();

        FieldsMetadata metadata = null;
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();

        preprocessor.preprocess( "test", stream, writer, metadata, formatter, null );

        assertEquals( LOGO_IMAGE_XML, writer.toString() );
    }

    public void testImageWithBadFieldsMetadata()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream(LOGO_IMAGE_XML, "UTF-8" );
        StringWriter writer = new StringWriter();

        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsImage( "XXX" );
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();

        preprocessor.preprocess( "test", stream, writer, metadata, formatter, null );

        assertEquals( LOGO_IMAGE_XML, writer.toString() );
    }

    public void testImageWithSimpleField()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( LOGO_IMAGE_XML, "UTF-8" );
        StringWriter writer = new StringWriter();

        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsImage( "logo" );
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();

        preprocessor.preprocess( "test", stream, writer, metadata, formatter, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                          + "<w:document "
                          + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
                          + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
                          + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
                          + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
                          + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
                          + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                          + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
                          + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
                          + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
                          + "<w:p w:rsidR=\"0064051C\" w:rsidRDefault=\"0064051C\">"
                          
                          + "[#assign ___imageInfo=___ImageRegistry.registerImage(logo,'logo',___context)][#if ___imageInfo.notRemoveImageTemplate]"
                          
                          + "<w:bookmarkStart w:id=\"0\" w:name=\"logo\"/>"
                          + "<w:r>"
                          + "<w:rPr>"
                          + "<w:noProof/>"
                          + "</w:rPr>"
                          + "<w:drawing>"
                          + "<wp:inline distT=\"0\" distB=\"0\" distL=\"0\" distR=\"0\">"
                          
                          // + "<wp:extent cx=\"266700\" cy=\"285750\"/>"
                          + "<wp:extent cx=\"${___ImageRegistry.getWidth(___imageInfo,'266700','285750')}\" cy=\"${___ImageRegistry.getHeight(___imageInfo,'266700','285750')}\"/>"
                          
                          + "<wp:effectExtent l=\"19050\" t=\"0\" r=\"0\" b=\"0\"/>"
                          + "<wp:docPr id=\"1\" name=\"Image 0\" descr=\"template.png\"/>"
                          + "<wp:cNvGraphicFramePr>"
                          + "<a:graphicFrameLocks xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" noChangeAspect=\"1\"/>"
                          + "</wp:cNvGraphicFramePr>"
                          + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
                          + "<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                          + "<pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                          + "<pic:nvPicPr>" + "<pic:cNvPr id=\"0\" name=\"template.png\"/>" + "<pic:cNvPicPr/>"
                          + "</pic:nvPicPr>" + "<pic:blipFill>"

                          + "<a:blip r:embed=\"${___ImageRegistry.getPath(___imageInfo,'rId5')}\"/>"

                          + "<a:stretch>"
                          + "<a:fillRect/>"
                          + "</a:stretch>"
                          + "</pic:blipFill>"
                          + "<pic:spPr>"
                          + "<a:xfrm>"
                          + "<a:off x=\"0\" y=\"0\"/>"
                          // + "<a:ext cx=\"266700\" cy=\"285750\"/>"
                          + "<a:ext cx=\"${___ImageRegistry.getWidth(___imageInfo,'266700','285750')}\" cy=\"${___ImageRegistry.getHeight(___imageInfo,'266700','285750')}\"/>"
                          + "</a:xfrm>"
                          + "<a:prstGeom prst=\"rect\">"
                          + "<a:avLst/>"
                          + "</a:prstGeom>"
                          + "</pic:spPr>"
                          + "</pic:pic>"
                          + "</a:graphicData>"
                          + "</a:graphic>"
                          + "</wp:inline>"
                          + "</w:drawing>"
                          + "</w:r>" + "<w:bookmarkEnd w:id=\"0\"/>" 
                          
                          + "[/#if]"
                          
                          + "</w:p>" + "</w:document>", writer.toString() );
    }

    public void testImageWithListFieldInTable()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream =
                        IOUtils.toInputStream(
                              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                  + "<w:document "
                                  + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
                                  + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
                                  + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
                                  + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
                                  + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
                                  + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                                  + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
                                  + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
                                  + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
                                  + "<w:tbl>"
                                  + "<w:tblPr>"
                                  + "<w:tblStyle w:val=\"Grilledutableau\"/>"
                                  + "<w:tblW w:w=\"0\" w:type=\"auto\"/>"
                                  + "<w:tblLook w:val=\"04A0\"/>"
                                  + "</w:tblPr>"
                                  + "<w:tblGrid>"
                                  + "<w:gridCol w:w=\"2586\"/>"
                                  + "<w:gridCol w:w=\"2786\"/>"
                                  + "<w:gridCol w:w=\"2513\"/>"
                                  + "<w:gridCol w:w=\"1403\"/>"
                                  + "</w:tblGrid>"
                                  + "<w:tr w:rsidR=\"00855D40\" w:rsidTr=\"00855D40\">"
                                  + "<w:tc>"
                                  + "<w:tcPr>"
                                  + "<w:tcW w:w=\"2586\" w:type=\"dxa\"/>"
                                  + "<w:shd w:val=\"pct50\" w:color=\"auto\" w:fill=\"auto\"/>"
                                  + "</w:tcPr>"
                                  + "<w:p w:rsidR=\"00855D40\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00855D40\">"
                                  + "<w:pPr>"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                                  + "</w:rPr>"
                                  + "</w:pPr>"
                                  + "<w:r w:rsidRPr=\"005D6D71\">"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                                  + "</w:rPr>"
                                  + "<w:t>Name</w:t>"
                                  + "</w:r>"
                                  + "</w:p>"
                                  + "</w:tc>"
                                  + "<w:tc>"
                                  + "<w:tcPr>"
                                  + "<w:tcW w:w=\"2786\" w:type=\"dxa\"/>"
                                  + "<w:shd w:val=\"pct50\" w:color=\"auto\" w:fill=\"auto\"/>"
                                  + "</w:tcPr>"
                                  + "<w:p w:rsidR=\"00855D40\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00855D40\" w:rsidP=\"005D6D71\">"
                                  + "<w:pPr>"
                                  + "<w:jc w:val=\"center\"/>"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                                  + "</w:rPr>"
                                  + "</w:pPr>"
                                  + "<w:r>"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                                  + "</w:rPr>"
                                  + "<w:t xml:space=\"preserve\">Last</w:t>"
                                  + "</w:r>"
                                  + "<w:proofErr w:type=\"spellStart\"/>"
                                  + "<w:r>"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                                  + "</w:rPr>"
                                  + "<w:t>name</w:t>"
                                  + "</w:r>"
                                  + "<w:proofErr w:type=\"spellEnd\"/>"
                                  + "</w:p>"
                                  + "</w:tc>"
                                  + "<w:tc>"
                                  + "<w:tcPr>"
                                  + "<w:tcW w:w=\"2513\" w:type=\"dxa\"/>"
                                  + "<w:shd w:val=\"pct50\" w:color=\"auto\" w:fill=\"auto\"/>"
                                  + "</w:tcPr>"
                                  + "<w:p w:rsidR=\"00855D40\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00855D40\">"
                                  + "<w:pPr>"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                                  + "</w:rPr>"
                                  + "</w:pPr>"
                                  + "<w:r>"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                                  + "</w:rPr>"
                                  + "<w:t>Mail</w:t>"
                                  + "</w:r>"
                                  + "</w:p>"
                                  + "</w:tc>"
                                  + "<w:tc>"
                                  + "<w:tcPr>"
                                  + "<w:tcW w:w=\"1403\" w:type=\"dxa\"/>"
                                  + "<w:shd w:val=\"pct50\" w:color=\"auto\" w:fill=\"auto\"/>"
                                  + "</w:tcPr>"
                                  + "<w:p w:rsidR=\"00855D40\" w:rsidRDefault=\"00855D40\" w:rsidP=\"00855D40\">"
                                  + "<w:pPr>"
                                  + "<w:jc w:val=\"center\"/>"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                                  + "</w:rPr>"
                                  + "</w:pPr>"
                                  + "<w:r>"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                                  + "</w:rPr>"
                                  + "<w:t>Photo</w:t>"
                                  + "</w:r>"
                                  + "</w:p>"
                                  + "</w:tc>"
                                  + "</w:tr>"
                                  + "<w:tr w:rsidR=\"00855D40\" w:rsidTr=\"00855D40\">"
                                  + "<w:tc>"
                                  + "<w:tcPr>"
                                  + "<w:tcW w:w=\"2586\" w:type=\"dxa\"/>"
                                  + "</w:tcPr>"
                                  + "<w:p w:rsidR=\"00855D40\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00855D40\">"
                                  + "<w:fldSimple w:instr=\" MERGEFIELD  ${developers.name}  \\* MERGEFORMAT \">"
                                  + "<w:r>"
                                  + "<w:rPr>"
                                  + "<w:noProof/>"
                                  + "</w:rPr>"
                                  + "<w:t>�${developers.name}�</w:t>"
                                  + "</w:r>"
                                  + "</w:fldSimple>"
                                  + "</w:p>"
                                  + "</w:tc>"
                                  + "<w:tc>"
                                  + "<w:tcPr>"
                                  + "<w:tcW w:w=\"2786\" w:type=\"dxa\"/>"
                                  + "</w:tcPr>"
                                  + "<w:p w:rsidR=\"00855D40\" w:rsidRDefault=\"00855D40\">"
                                  + "<w:pPr>"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FF0000\"/>"
                                  + "</w:rPr>"
                                  + "</w:pPr>"
                                  + "<w:fldSimple	w:instr=\" MERGEFIELD  ${developers.lastName}  \\* MERGEFORMAT \">"
                                  + "<w:r>"
                                  + "<w:rPr>"
                                  + "<w:noProof/>"
                                  + "</w:rPr>"
                                  + "<w:t>${developers.lastName}�</w:t>"
                                  + "</w:r>"
                                  + "</w:fldSimple>"
                                  + "</w:p>"
                                  + "</w:tc>"
                                  + "<w:tc>"
                                  + "<w:tcPr>"
                                  + "<w:tcW w:w=\"2513\" w:type=\"dxa\"/>"
                                  + "</w:tcPr>"
                                  + "<w:p w:rsidR=\"00855D40\" w:rsidRDefault=\"00855D40\">"
                                  + "<w:pPr>"
                                  + "<w:rPr>"
                                  + "<w:color w:val=\"FF0000\"/>"
                                  + "</w:rPr>"
                                  + "</w:pPr>"
                                  + "<w:fldSimple w:instr=\" MERGEFIELD  ${developers.mail}  \\* MERGEFORMAT \">"
                                  + "<w:r>"
                                  + "<w:rPr>"
                                  + "<w:noProof/>"
                                  + "</w:rPr>"
                                  + "<w:t>�${developers.mail}�</w:t>"
                                  + "</w:r>"
                                  + "</w:fldSimple>"
                                  + "</w:p>"
                                  + "</w:tc>"
                                  + "<w:tc>"
                                  + "<w:tcPr>"
                                  + "<w:tcW w:w=\"1403\" w:type=\"dxa\"/>"
                                  + "</w:tcPr>"
                                  + "<w:p w:rsidR=\"00855D40\" w:rsidRDefault=\"00855D40\">"
                                  + "<w:bookmarkStart w:id=\"0\" w:name=\"photo\"/>"
                                  + "<w:r>"
                                  + "<w:rPr>"
                                  + "<w:noProof/>"
                                  + "</w:rPr>"
                                  + "<w:drawing>"
                                  + "<wp:inline distT=\"0\" distB=\"0\" distL=\"0\" distR=\"0\">"
                                  + "<wp:extent cx=\"266700\" cy=\"285750\"/>"
                                  + "<wp:effectExtent l=\"19050\" t=\"0\" r=\"0\" b=\"0\"/>"
                                  + "<wp:docPr id=\"1\" name=\"Image 0\" descr=\"template.png\"/>"
                                  + "<wp:cNvGraphicFramePr>"
                                  + "<a:graphicFrameLocks	xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" noChangeAspect=\"1\"/>"
                                  + "</wp:cNvGraphicFramePr>"
                                  + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
                                  + "<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                                  + "<pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                                  + "<pic:nvPicPr>" + "<pic:cNvPr id=\"0\" name=\"template.png\"/>" + "<pic:cNvPicPr/>"
                                  + "</pic:nvPicPr>" + "<pic:blipFill>" + "<a:blip r:embed=\"rId5\"/>" + "<a:stretch>"
                                  + "<a:fillRect/>" + "</a:stretch>" + "</pic:blipFill>" + "<pic:spPr>" + "<a:xfrm>"
                                  + "<a:off x=\"0\" y=\"0\"/>" + "<a:ext cx=\"266700\" cy=\"285750\"/>" + "</a:xfrm>"
                                  + "<a:prstGeom prst=\"rect\">" + "<a:avLst/>" + "</a:prstGeom>" + "</pic:spPr>"
                                  + "</pic:pic>" + "</a:graphicData>" + "</a:graphic>" + "</wp:inline>"
                                  + "</w:drawing>" + "</w:r>" + "<w:bookmarkEnd w:id=\"0\"/>" + "</w:p>" + "</w:tc>"
                                  + "</w:tr>" + "</w:tbl>" + "</w:document>", "UTF-8" );

        StringWriter writer = new StringWriter();

        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsList( "developers.photo" );
        metadata.addFieldAsImage( "photo", "developers.photo" );
        IDocumentFormatter formatter = new FreemarkerDocumentFormatter();

        preprocessor.preprocess( "test", stream, writer, metadata, formatter, null );

        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                          + "<w:document "
                          + "xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" "
                          + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
                          + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
                          + "xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" "
                          + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
                          + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                          + "xmlns:w10=\"urn:schemas-microsoft-com:office:word\" "
                          + "xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
                          + "xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
                          + "<w:tbl>"
                          + "<w:tblPr>"
                          + "<w:tblStyle w:val=\"Grilledutableau\"/>"
                          + "<w:tblW w:w=\"0\" w:type=\"auto\"/>"
                          + "<w:tblLook w:val=\"04A0\"/>"
                          + "</w:tblPr>"
                          + "<w:tblGrid>"
                          + "<w:gridCol w:w=\"2586\"/>"
                          + "<w:gridCol w:w=\"2786\"/>"
                          + "<w:gridCol w:w=\"2513\"/>"
                          + "<w:gridCol w:w=\"1403\"/>"
                          + "</w:tblGrid>"
                          + "<w:tr w:rsidR=\"00855D40\" w:rsidTr=\"00855D40\">"
                          + "<w:tc>"
                          + "<w:tcPr>"
                          + "<w:tcW w:w=\"2586\" w:type=\"dxa\"/>"
                          + "<w:shd w:val=\"pct50\" w:color=\"auto\" w:fill=\"auto\"/>"
                          + "</w:tcPr>"
                          + "<w:p w:rsidR=\"00855D40\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00855D40\">"
                          + "<w:pPr>"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                          + "</w:rPr>"
                          + "</w:pPr>"
                          + "<w:r w:rsidRPr=\"005D6D71\">"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                          + "</w:rPr>"
                          + "<w:t>Name</w:t>"
                          + "</w:r>"
                          + "</w:p>"
                          + "</w:tc>"
                          + "<w:tc>"
                          + "<w:tcPr>"
                          + "<w:tcW w:w=\"2786\" w:type=\"dxa\"/>"
                          + "<w:shd w:val=\"pct50\" w:color=\"auto\" w:fill=\"auto\"/>"
                          + "</w:tcPr>"
                          + "<w:p w:rsidR=\"00855D40\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00855D40\" w:rsidP=\"005D6D71\">"
                          + "<w:pPr>"
                          + "<w:jc w:val=\"center\"/>"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                          + "</w:rPr>"
                          + "</w:pPr>"
                          + "<w:r>"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                          + "</w:rPr>"
                          + "<w:t xml:space=\"preserve\">Last</w:t>"
                          + "</w:r>"
                          + "<w:proofErr w:type=\"spellStart\"/>"
                          + "<w:r>"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                          + "</w:rPr>"
                          + "<w:t>name</w:t>"
                          + "</w:r>"
                          + "<w:proofErr w:type=\"spellEnd\"/>"
                          + "</w:p>"
                          + "</w:tc>"
                          + "<w:tc>"
                          + "<w:tcPr>"
                          + "<w:tcW w:w=\"2513\" w:type=\"dxa\"/>"
                          + "<w:shd w:val=\"pct50\" w:color=\"auto\" w:fill=\"auto\"/>"
                          + "</w:tcPr>"
                          + "<w:p w:rsidR=\"00855D40\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00855D40\">"
                          + "<w:pPr>"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                          + "</w:rPr>"
                          + "</w:pPr>"
                          + "<w:r>"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                          + "</w:rPr>"
                          + "<w:t>Mail</w:t>"
                          + "</w:r>"
                          + "</w:p>"
                          + "</w:tc>"
                          + "<w:tc>"
                          + "<w:tcPr>"
                          + "<w:tcW w:w=\"1403\" w:type=\"dxa\"/>"
                          + "<w:shd w:val=\"pct50\" w:color=\"auto\" w:fill=\"auto\"/>"
                          + "</w:tcPr>"
                          + "<w:p w:rsidR=\"00855D40\" w:rsidRDefault=\"00855D40\" w:rsidP=\"00855D40\">"
                          + "<w:pPr>"
                          + "<w:jc w:val=\"center\"/>"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                          + "</w:rPr>"
                          + "</w:pPr>"
                          + "<w:r>"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FFFFFF\" w:themeColor=\"background1\"/>"
                          + "</w:rPr>"
                          + "<w:t>Photo</w:t>"
                          + "</w:r>"
                          + "</w:p>"
                          + "</w:tc>"
                          + "</w:tr>"

                          + "[#list developers as item_developers]"

                          + "<w:tr w:rsidR=\"00855D40\" w:rsidTr=\"00855D40\">"
                          + "<w:tc>"
                          + "<w:tcPr>"
                          + "<w:tcW w:w=\"2586\" w:type=\"dxa\"/>"
                          + "</w:tcPr>"
                          + "<w:p w:rsidR=\"00855D40\" w:rsidRPr=\"005D6D71\" w:rsidRDefault=\"00855D40\">"
                          // + "<w:fldSimple w:instr=\" MERGEFIELD  ${developers.name}  \\* MERGEFORMAT \">"
                          + "<w:r>"
                          + "<w:rPr>"
                          + "<w:noProof/>"
                          + "</w:rPr>"
                          // + "<w:t>�${developers.name}�</w:t>"
                          + "<w:t>${developers.name}</w:t>"
                          + "</w:r>"
                          // + "</w:fldSimple>"
                          + "</w:p>"
                          + "</w:tc>"
                          + "<w:tc>"
                          + "<w:tcPr>"
                          + "<w:tcW w:w=\"2786\" w:type=\"dxa\"/>"
                          + "</w:tcPr>"
                          + "<w:p w:rsidR=\"00855D40\" w:rsidRDefault=\"00855D40\">"
                          + "<w:pPr>"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FF0000\"/>"
                          + "</w:rPr>"
                          + "</w:pPr>"
                          // + "<w:fldSimple	w:instr=\" MERGEFIELD  ${developers.lastName}  \\* MERGEFORMAT \">"
                          + "<w:r>"
                          + "<w:rPr>"
                          + "<w:noProof/>"
                          + "</w:rPr>"
                          // + "<w:t>�${developers.lastName}�</w:t>"
                          + "<w:t>${developers.lastName}</w:t>"
                          + "</w:r>"
                          // + "</w:fldSimple>"
                          + "</w:p>"
                          + "</w:tc>"
                          + "<w:tc>"
                          + "<w:tcPr>"
                          + "<w:tcW w:w=\"2513\" w:type=\"dxa\"/>"
                          + "</w:tcPr>"
                          + "<w:p w:rsidR=\"00855D40\" w:rsidRDefault=\"00855D40\">"
                          + "<w:pPr>"
                          + "<w:rPr>"
                          + "<w:color w:val=\"FF0000\"/>"
                          + "</w:rPr>"
                          + "</w:pPr>"
                          // + "<w:fldSimple w:instr=\" MERGEFIELD  ${developers.mail}  \\* MERGEFORMAT \">"
                          + "<w:r>"
                          + "<w:rPr>"
                          + "<w:noProof/>"
                          + "</w:rPr>"
                          // + "<w:t>�${developers.mail}�</w:t>"
                          + "<w:t>${developers.mail}</w:t>"
                          + "</w:r>"
                          // + "</w:fldSimple>"
                          + "</w:p>"
                          + "</w:tc>"
                          + "<w:tc>"
                          + "<w:tcPr>"
                          + "<w:tcW w:w=\"1403\" w:type=\"dxa\"/>"
                          + "</w:tcPr>"
                          + "<w:p w:rsidR=\"00855D40\" w:rsidRDefault=\"00855D40\">"
                          
                          + "[#assign ___imageInfo=___ImageRegistry.registerImage(item_developers.photo,'photo',___context)][#if ___imageInfo.notRemoveImageTemplate]"
                          
                          + "<w:bookmarkStart w:id=\"0\" w:name=\"photo\"/>"
                          + "<w:r>"
                          + "<w:rPr>"
                          + "<w:noProof/>"
                          + "</w:rPr>"
                          + "<w:drawing>"
                          + "<wp:inline distT=\"0\" distB=\"0\" distL=\"0\" distR=\"0\">"
                          // + "<wp:extent cx=\"266700\" cy=\"285750\"/>"
                          + "<wp:extent cx=\"${___ImageRegistry.getWidth(___imageInfo,'266700','285750')}\" cy=\"${___ImageRegistry.getHeight(___imageInfo,'266700','285750')}\"/>"
                          
                          + "<wp:effectExtent l=\"19050\" t=\"0\" r=\"0\" b=\"0\"/>"
                          + "<wp:docPr id=\"1\" name=\"Image 0\" descr=\"template.png\"/>"
                          + "<wp:cNvGraphicFramePr>"
                          + "<a:graphicFrameLocks xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" noChangeAspect=\"1\"/>"
                          + "</wp:cNvGraphicFramePr>"
                          + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
                          + "<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                          + "<pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                          + "<pic:nvPicPr>" + "<pic:cNvPr id=\"0\" name=\"template.png\"/>" + "<pic:cNvPicPr/>"
                          + "</pic:nvPicPr>" + "<pic:blipFill>"

                          // + "<a:blip r:embed=\"rId5\"/>"
                          + "<a:blip r:embed=\"${___ImageRegistry.getPath(___imageInfo,'rId5')}\"/>"

                          + "<a:stretch>"
                          + "<a:fillRect/>"
                          + "</a:stretch>"
                          + "</pic:blipFill>"
                          + "<pic:spPr>"
                          + "<a:xfrm>"
                          + "<a:off x=\"0\" y=\"0\"/>"
                          // + "<a:ext cx=\"266700\" cy=\"285750\"/>"
                          + "<a:ext cx=\"${___ImageRegistry.getWidth(___imageInfo,'266700','285750')}\" cy=\"${___ImageRegistry.getHeight(___imageInfo,'266700','285750')}\"/>"
                          + "</a:xfrm>"
                          + "<a:prstGeom prst=\"rect\">"
                          + "<a:avLst/>"
                          + "</a:prstGeom>"
                          + "</pic:spPr>"
                          + "</pic:pic>"
                          + "</a:graphicData>"
                          + "</a:graphic>"
                          + "</wp:inline>"
                          + "</w:drawing>"
                          + "</w:r>" + "<w:bookmarkEnd w:id=\"0\"/>" 
                          
                          + "[/#if]"
                          
                          + "</w:p>" + "</w:tc>" + "</w:tr>"

                          + "[/#list]"

                          + "</w:tbl>" + "</w:document>", writer.toString() );
    }
}
