/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
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
package org.apache.poi.xwpf.converter.internal.xhtml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.xwpf.converter.internal.itext.StyleEngineForIText;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;

import fr.opensagres.xdocreport.xhtml.extension.XHTMLConstants;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLPage;

public class OLDXHTMLMapper
    implements XHTMLConstants
{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( StyleEngineForIText.class.getName() );

    private CTDocDefaults defaults;

    private XWPFDocument document;

    private XHTMLPage writer = new XHTMLPage( 1 );

    public OLDXHTMLMapper( XWPFDocument document )
    {
        super();
        this.document = document;
        try
        {
            defaults = document.getStyle().getDocDefaults();
        }
        catch ( XmlException e )
        {
            LOGGER.severe( e.getMessage() );
        }
        catch ( IOException e )
        {
            LOGGER.severe( e.getMessage() );
        }
    }

    /**
     * @param blipId
     * @return XXX in some case XWPFPicture.getPictureData throw a NullPointerException because the getDocument() of
     *         It's paragraph is null
     */
    private XWPFPictureData getPictureData( String blipId )
    {
        for ( POIXMLDocumentPart part : document.getRelations() )
        {
            if ( part.getPackageRelationship().getId().equals( blipId ) )
            {
                return (XWPFPictureData) part;
            }
        }
        return null;
    }

    /**
     * Create a pdf version of the document, using XSL FO.
     * 
     * @param wmlStyles
     * @param wmlDocument
     * @param os The OutputStream to write the pdf to
     */
    public void output( OutputStream os )
        throws Exception
    {

        try
        {
            // body.get
            List<IBodyElement> bodyElement = document.getBodyElements();
            for ( IBodyElement iBodyElement : bodyElement )
            {
                visit( iBodyElement );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            throw new Exception( "iTextissues", e );
        }
        finally
        {
            // Flush
            writer.save( os );
        }

    }

    private void visit( IBodyElement iBodyElement )
        throws Exception
    {

        if ( iBodyElement instanceof XWPFParagraph )
        {
            visit( (XWPFParagraph) iBodyElement );
            // ((Document)parent).add(pdfParagraph);
        }
        else if ( iBodyElement instanceof XWPFTable )
        {
            // TODO : visit((XWPFTable) iBodyElement);
        }

        // return pdfParagraph;

    }

    // private void visit(XWPFParagraph p) throws Exception {
    //
    // StringBuilder inlineStringValues = new StringBuilder();
    // int left = p.getIndentationLeft();
    // int right = p.getIndentationRight();
    // writer.startElementNotEnclosed(P_ELEMENT);
    // // ILVs = "puce et numero" ...
    //
    // if (p.getCTP().getPPr().getNumPr() != null) {
    // // TODO:
    // // CTDecimalNumber ilv = p.getCTP().getPPr().getNumPr().getIlvl();
    // // BigInteger val = ilv.getVal();
    // // pdfParagraph = new ListItem();
    // // // FIXME : must be improved
    // // pdfParagraph.setIndentationLeft(10 + 10 * val.intValue());
    //
    // } else {
    //
    // inlineStringValues.append("margin-right: " + dxa2points(right)
    // + "pt;");
    // inlineStringValues.append("margin-left: " + dxa2points(left)
    // + "pt;");
    // //
    // writer.addAttributeValue("style='margin-left: "+dxa2points(right)+"pt;'",false);
    // // pdfParagraph.setIndentationRight(dxa2points(right));
    // // pdfParagraph.setIndentationLeft(dxa2points(left));
    //
    // int firstLineIndent = p.getIndentationFirstLine();
    // inlineStringValues.append("text-indent: "
    // + dxa2points(firstLineIndent) + "pt;");
    // // pdfParagraph.setFirstLineIndent(dxa2points(firstLineIndent));
    //
    // }
    //
    // // pdfParagraph.setSpacingBefore(dxa2points(p.getSpacingBefore()));
    // // TODO:
    // // if (p.getSpacingBefore() >= 0) {
    // // pdfParagraph.setSpacingBefore(dxa2points(p.getSpacingBefore()));
    // // } else if (defaults != null) {
    // // BigInteger begore = defaults.getPPrDefault().getPPr().getSpacing()
    // // .getBefore();
    // // if (begore != null) {
    // // pdfParagraph.setSpacingBefore(dxa2points(begore));
    // // }
    // //
    // // }
    // // if (p.getSpacingAfter() >= 0) {
    // // pdfParagraph.setSpacingAfter(dxa2points(p.getSpacingAfter()));
    // // } else if (defaults != null) {
    // // BigInteger after = defaults.getPPrDefault().getPPr().getSpacing()
    // // .getAfter();
    // // if (after != null) {
    // // pdfParagraph.setSpacingAfter(dxa2points(after));
    // // }
    // //
    // // } else {
    // // // Words 'default if not set :
    // // pdfParagraph.setSpacingAfter(10f);
    // // }
    //
    // // pdfParagraph.setSpacingAfter(10f);
    // if (p.getCTP().getPPr() != null) {
    // if (p.getCTP().getPPr().getSpacing() != null) {
    // // PLQ : why 180 ???
    // float leading = (p.getCTP().getPPr().getSpacing().getLine()
    // .floatValue() / 240);
    // inlineStringValues.append("line-height: " + leading + "pt;");
    // // pdfParagraph.setMultipliedLeading(leading);
    // // CTHpsMeasure meas = p.getCTP().getPPr().getRPr().getSz();
    // // if (meas != null) {
    // // // PLQ ??? looks better this way ut I don't know why....
    // // System.err.println(meas.getVal());
    // // pdfParagraph.setSpacingBefore(meas.getVal().intValue() / 4);
    // // }
    //
    // }
    // }
    // // TextAlignment vAlignment=p.getVerticalAlignment();
    //
    // ParagraphAlignment alignment = p.getAlignment();
    // switch (alignment) {
    // case LEFT:
    // inlineStringValues.append("text-align: left;");
    // // pdfParagraph.setAlignment(ElementTags.ALIGN_LEFT);
    // break;
    // case RIGHT:
    // inlineStringValues.append("text-align: right;");
    // // pdfParagraph.setAlignment(ElementTags.ALIGN_RIGHT);
    // break;
    //
    // case CENTER:
    // inlineStringValues.append("text-align: center;");
    // // pdfParagraph.setAlignment(ElementTags.ALIGN_CENTER);
    // break;
    //
    // case BOTH:
    // inlineStringValues.append("text-align:justify;");
    // // pdfParagraph.setAlignment(ElementTags.ALIGN_JUSTIFIED);
    // break;
    //
    // default:
    // break;
    // }
    //
    // writer.setAttribute("style", inlineStringValues.toString());
    // writer.endElementNotEnclosed();
    //
    // // TODO:
    // for (XWPFRun o : p.getRuns()) {
    // visit(o);
    // }
    // // PLQ : I don't know why but I looks better this way...
    //
    // if (p.getRuns().isEmpty()) {
    // writer.startEndElement(BR_ELEMENT);
    // }
    //
    // writer.endElement(P_ELEMENT);
    //
    // }
    //
    // private void visit(XWPFRun run) throws Exception {
    //
    // // Font font = null;
    // boolean hasFont = false;
    // // FIXME : should I cache the fonts ?
    // String docFontFamilly = run.getFontFamily();
    // if (docFontFamilly != null) {
    // hasFont = true;
    // // TODO: discover appropiate font from platform....
    // // BaseFont bf = BaseFont.createFont(docFontFamilly,
    // // BaseFont.MACROMAN, BaseFont.EMBEDDED);
    // writer.startElementNotEnclosed("font");
    // writer.setAttribute("face", docFontFamilly);
    // writer.setAttribute("size", String.valueOf(run.getFontSize()));
    // // TODO : font...
    // // font.setSize(run.getFontSize());
    // // if (run.isBold() && run.isItalic()) {
    // // font.setStyle(Font.BOLDITALIC);
    // // } else if (run.isBold()) {
    // // font.setStyle(Font.BOLD);
    // // } else if (run.isItalic()) {
    // // font.setStyle(Font.ITALIC);
    // // }
    // // XXX not here
    //
    // }
    // if (run.getCTR().getRPr() != null) {
    //
    // CTColor ctColor = run.getCTR().getRPr().getColor();
    //
    // if (ctColor != null) {
    // STHexColor col = ctColor.xgetVal();
    // String hexColor = col.getStringValue();
    // if (hexColor != null && !"auto".equals(hexColor)) {
    // // font.setColor(Color.decode("0x" + hexColor));
    // writer.setAttribute("color", hexColor);
    // }
    // }
    // }
    //
    // boolean italic = false;
    // if (run.isItalic()) {
    // writer.startElementNotEnclosed("I");
    // italic = true;
    // }
    // boolean bold = false;
    // if (run.isBold()) {
    // writer.startElementNotEnclosed("B");
    // bold = true;
    // }
    // UnderlinePatterns underlinePatterns = run.getUnderline();
    //
    // boolean underlined = false;
    // switch (underlinePatterns) {
    // case SINGLE:
    // underlined = true;
    // writer.startElementNotEnclosed("u");
    // break;
    //
    // default:
    // break;
    // }
    // List<CTBr> brs = run.getCTR().getBrList();
    //
    // for (Iterator<CTBr> iterator = brs.iterator(); iterator.hasNext();) {
    // writer.startElement("br");
    // }
    //
    // List<CTText> texts = run.getCTR().getTList();
    // for (CTText ctText : texts) {
    // writer.setText(ctText.getStringValue());
    // }
    // List<XWPFPicture> embeddedPictures = run.getEmbeddedPictures();
    // for (XWPFPicture xwpfPicture : embeddedPictures) {
    // // TODO: image..
    // // Image img = visit(xwpfPicture);
    // // pdfParagraph.add(img);
    // }
    //
    // if (underlined)
    // writer.endElement("u");
    // if (bold)
    // writer.endElement("B");
    // if (italic)
    // writer.endElement("I");
    // if (hasFont)
    // writer.endElement("font");
    // }
    // //
    // // private Image visit(XWPFPicture xwpfPicture)
    // // throws BadElementException, MalformedURLException, IOException {
    // // CTPositiveSize2D ext = xwpfPicture.getCTPicture().getSpPr().getXfrm()
    // // .getExt();
    // // long x = ext.getCx();
    // // long y = ext.getCy();
    // //
    // // CTPicture ctPic = xwpfPicture.getCTPicture();
    // // String blipId = ctPic.getBlipFill().getBlip().getEmbed();
    // //
    // // XWPFPictureData pictureData = getPictureData(blipId);
    // //
    // // if (pictureData != null) {
    // // try {
    // // Image img = Image.getInstance(pictureData.getData());
    // // System.out.println(img);
    // //
    // // img.scaleAbsolute(dxa2points(x) / 635, dxa2points(y) / 635);
    // //
    // // return img;
    // // //
    // // } catch (Exception e) {
    // // LOG.error(e.getMessage());
    // // }
    // //
    // // }
    // // return null;
    // // }
    // //
    // // private Paragraph visit(XWPFTable table) throws Exception {
    // // Paragraph aParagraph = new Paragraph();
    // // // XXX 1f sounds reasonable...
    // //
    // // boolean left = true, right = true, bottom = true, top = true;
    // //
    // // if (table.getCTTbl().getTblPr().getTblBorders() != null) {
    // //
    // // CTTblBorders borders = table.getCTTbl().getTblPr().getTblBorders();
    // // if (borders.getLeft() != null) {
    // // left = !(STBorder.NONE == borders.getLeft().getVal());
    // // }
    // // if (borders.getRight() != null) {
    // // right = !(STBorder.NONE == borders.getRight().getVal());
    // // }
    // // if (borders.getBottom() != null) {
    // // bottom = !(STBorder.NONE == borders.getBottom().getVal());
    // // }
    // // if (borders.getTop() != null) {
    // // top = !(STBorder.NONE == borders.getTop().getVal());
    // // }
    // // }
    // // List<XWPFTableRow> rows = table.getRows();
    // //
    // // CTTblGrid grid = table.getCTTbl().getTblGrid();
    // // List<CTTblGridCol> cols = grid.getGridColList();
    // //
    // // float total = 0;
    // // float[] colWidths = new float[cols.size()];
    // // for (int i = 0; i < colWidths.length; i++) {
    // // total += dxa2points(cols.get(i).getW());
    // // colWidths[i] = dxa2points(cols.get(i).getW());
    // // }
    // //
    // // PdfPTable aTable = new PdfPTable(cols.size());
    // //
    // // aTable.setTotalWidth(colWidths);
    // // aTable.setLockedWidth(true);
    // //
    // // for (XWPFTableRow xwpfTableRow : rows) {
    // //
    // // List<XWPFTableCell> cells = xwpfTableRow.getTableCells();
    // // int height = xwpfTableRow.getHeight();
    // //
    // // for (XWPFTableCell xwpfTableCell : cells) {
    // // CTShd shd = xwpfTableCell.getCTTc().getTcPr().getShd();
    // // String hexColor = null;
    // // if (shd != null) {
    // // hexColor = shd.xgetFill().getStringValue();
    // // }
    // // List<IBodyElement> elements = xwpfTableCell.getBodyElements();
    // // PdfPCell aCell = new PdfPCell();
    // // if (!left)
    // // aCell.disableBorderSide(PdfPCell.LEFT);
    // // if (!right)
    // // aCell.disableBorderSide(PdfPCell.RIGHT);
    // // if (!bottom)
    // // aCell.disableBorderSide(PdfPCell.BOTTOM);
    // // if (!top)
    // // aCell.disableBorderSide(PdfPCell.TOP);
    // //
    // // if (!elements.isEmpty()) {
    // // if (elements.size() == 1) {
    // // // single content
    // // IBodyElement iBodyElement = elements.get(0);
    // // if (iBodyElement instanceof XWPFParagraph) {
    // // XWPFParagraph paragraph = (XWPFParagraph) iBodyElement;
    // // if (paragraph.getRuns().size() == 1) {
    // // // iText can only handle one picture in a
    // // // Cell...
    // // XWPFRun run = paragraph.getRuns().get(0);
    // // if (run.getEmbeddedPictures().size() > 0) {
    // // Image img = visit(run
    // // .getEmbeddedPictures().get(0));
    // // aCell.setImage(img);
    // // } else {
    // // Paragraph pdfParagraph = visit(paragraph);
    // // aCell.addElement(pdfParagraph);
    // // }
    // // }
    // // // TODO ???
    // // // else {
    // // // Paragraph
    // // // pdfParagraph=processParagraph(paragraph);
    // // // aCell.setPhrase(pdfParagraph);
    // // // }
    // // }
    // // } else {
    // //
    // // for (IBodyElement iBodyElement : elements) {
    // //
    // // Paragraph singleParagraph = visit(iBodyElement);
    // // aCell.addElement(singleParagraph);
    // // }
    // //
    // // }
    // //
    // // }
    // //
    // // if (hexColor != null && !"auto".equals(hexColor)) {
    // // aCell.setBackgroundColor(Color.decode("0x" + hexColor));
    // //
    // // }
    // // aCell.setMinimumHeight(dxa2points(height));
    // // aTable.addCell(aCell);
    // //
    // // }
    // //
    // // }
    // //
    // // aParagraph.add(aTable);
    // // // pdfDoc.add(aParagraph);
    // // return aParagraph;
    // // }

}
