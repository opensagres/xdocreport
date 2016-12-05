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
package fr.opensagres.poi.xwpf.converter.core.openxmlformats;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.poi.util.IOUtils;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlTokenSource;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTPosH;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTPosV;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapSquare;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromH;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;

import fr.opensagres.poi.xwpf.converter.core.IImageExtractor;
import fr.opensagres.poi.xwpf.converter.core.IMasterPageHandler;
import fr.opensagres.poi.xwpf.converter.core.IXWPFMasterPage;
import fr.opensagres.poi.xwpf.converter.core.ListItemContext;
import fr.opensagres.poi.xwpf.converter.core.MasterPageManager;
import fr.opensagres.poi.xwpf.converter.core.Options;
import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil;
import fr.opensagres.poi.xwpf.converter.core.utils.StringUtils;
import fr.opensagres.poi.xwpf.converter.core.utils.XWPFRunHelper;
import fr.opensagres.poi.xwpf.converter.core.utils.XWPFTableUtil;

public abstract class OpenXMlFormatsVisitor<T, O extends Options, E extends IXWPFMasterPage>
    implements IMasterPageHandler<E>
{

    private final IOpenXMLFormatsPartProvider provider;

    protected final O options;

    private final CTDocument1 document;

    private final MasterPageManager masterPageManager;

    private CTHdrFtr currentHeader;

    private CTHdrFtrRef currentHeaderRef;

    private CTHdrFtr currentFooter;

    private CTHdrFtrRef currentFooterRef;

    private boolean pageBreakOnNextParagraph;

    protected final XWPFStylesDocument stylesDocument;

    private final Stack<CTTbl> tables;

    public OpenXMlFormatsVisitor( IOpenXMLFormatsPartProvider provider, O options )
        throws Exception
    {
        this.provider = provider;
        this.options = options;
        this.tables = new Stack<CTTbl>();

        this.document = provider.getDocument();
        this.stylesDocument = createStylesDocument( provider );
        this.masterPageManager = new MasterPageManager( document, this );
    }

    protected XWPFStylesDocument createStylesDocument( IOpenXMLFormatsPartProvider provider )
        throws Exception
    {
        return new XWPFStylesDocument( provider );
    }

    public XWPFStylesDocument getStylesDocument()
    {
        return stylesDocument;
    }

    public O getOptions()
    {
        return options;
    }

    public MasterPageManager getMasterPageManager()
    {
        return masterPageManager;
    }

    // ------------------------------ Start/End document visitor -----------

    /**
     * Main entry for visit XWPFDocument.
     * 
     * @param out
     * @throws Exception
     */
    public void start()
        throws Exception
    {
        // start document
        T container = startVisitDocument();
        // Create IText, XHTML element for each XWPF elements from the w:body
        visitBodyElements( document, container );
        // end document
        endVisitDocument();
    }

    /**
     * Start of visit document.
     * 
     * @return
     * @throws Exception
     */
    protected abstract T startVisitDocument()
        throws Exception;

    /**
     * End of visit document.
     * 
     * @throws Exception
     */
    protected abstract void endVisitDocument()
        throws Exception;

    // ------------------------------ OpenXMLFormats Elements visitor -----------

    private void visitBodyElements( CTDocument1 document, T container )
        throws Exception
    {
        visitBodyElements( document.getBody(), container );
    }

    protected void visitBodyElements( XmlTokenSource token, T container )
        throws Exception
    {
        if ( !masterPageManager.isInitialized() )
        {
            // master page manager which hosts each <:w;sectPr declared in the word/document.xml
            // must be initialized. The initialization loop for each
            // <w:p paragraph to compute a list of <w:sectPr which contains information
            // about header/footer declared in the <w:headerReference/<w:footerReference
            masterPageManager.initialize();
        }

        int i = 0;
        XmlCursor cursor = null;
        try
        {
            cursor = token.newCursor();
            cursor.selectPath( "./*" );
            while ( cursor.toNextSelection() )
            {
                XmlObject o = cursor.getObject();
                if ( o instanceof CTP )
                {
                    visitParagraph( (CTP) o, i, container );
                    i++;
                }
                else if ( o instanceof CTTbl )
                {
                    visitTable( (CTTbl) o, i, container );
                    i++;
                }
                else if ( o instanceof CTSdtBlock )
                {
                    // <w:sdt><w:sdtContent><p...
                    CTSdtBlock block = (CTSdtBlock) o;
                    CTSdtContentBlock contentBlock = block.getSdtContent();
                    if ( contentBlock != null )
                    {
                        visitBodyElements( contentBlock, container );
                    }
                }
            }
        }
        finally
        {
            if ( cursor != null )
            {
                cursor.dispose();
            }
        }
    }

    protected void visitParagraph( CTP paragraph, int index, T container )
        throws Exception
    {
        if ( isWordDocumentPartParsing() )
        {
            // header/footer is not parsing.
            // It's the word/document.xml which is parsing
            // test if the current paragraph define a <w:sectPr
            // to update the header/footer declared in the <w:headerReference/<w:footerReference
            masterPageManager.update( paragraph );
        }
        if ( pageBreakOnNextParagraph )
        {
            pageBreak();
        }
        this.pageBreakOnNextParagraph = false;

        ListItemContext itemContext = null;
        T paragraphContainer = startVisitParagraph( paragraph, itemContext, container );
        visitParagraphBody( paragraph, index, paragraphContainer );
        endVisitParagraph( paragraph, container, paragraphContainer );
    }

    protected abstract void endVisitParagraph( CTP paragraph, T container, T paragraphContainer )
        throws Exception;

    protected void visitParagraphBody( CTP paragraph, int index, T paragraphContainer )
        throws Exception
    {
        boolean fldCharTypeParsing = false;
        boolean pageNumber = false;
        String url = null;
        List<XmlObject> rListAfterSeparate = null;

        XmlCursor cursor = null;
        try
        {
            cursor = paragraph.newCursor();
            cursor.selectPath( "./*" );
            while ( cursor.toNextSelection() )
            {
                XmlObject o = cursor.getObject();
                if ( o instanceof CTR )
                {
                    /*
                     * Test if it's : <w:r> <w:rPr /> <w:fldChar w:fldCharType="begin" /> </w:r>
                     */
                    CTR r = (CTR) o;
                    STFldCharType.Enum fldCharType = XWPFRunHelper.getFldCharType( r );
                    if ( fldCharType != null )
                    {
                        if ( fldCharType.equals( STFldCharType.BEGIN ) )
                        {
                            process( paragraph, paragraphContainer, pageNumber, url, rListAfterSeparate );
                            fldCharTypeParsing = true;
                            rListAfterSeparate = new ArrayList<XmlObject>();
                            pageNumber = false;
                            url = null;
                        }
                        else if ( fldCharType.equals( STFldCharType.END ) )
                        {

                            process( paragraph, paragraphContainer, pageNumber, url, rListAfterSeparate );
                            fldCharTypeParsing = false;
                            rListAfterSeparate = null;
                            pageNumber = false;
                            url = null;
                        }
                    }
                    else
                    {
                        if ( fldCharTypeParsing )
                        {
                            String instrText = XWPFRunHelper.getInstrText( r );
                            if ( instrText != null )
                            {
                                if ( StringUtils.isNotEmpty( instrText ) )
                                {
                                    // test if it's <w:r><w:instrText>PAGE</w:instrText></w:r>
                                    boolean instrTextPage = XWPFRunHelper.isInstrTextPage( instrText );
                                    if ( !instrTextPage )
                                    {
                                        // test if it's <w:instrText>HYPERLINK
                                        // "http://code.google.com/p/xdocrepor"</w:instrText>
                                        String instrTextHyperlink = XWPFRunHelper.getInstrTextHyperlink( instrText );
                                        if ( instrTextHyperlink != null )
                                        {
                                            url = instrTextHyperlink;
                                        }
                                    }
                                    else
                                    {
                                        pageNumber = true;
                                    }
                                }
                            }
                            else
                            {
                                rListAfterSeparate.add( r );
                            }
                        }
                        else
                        {
                            visitRun( r, paragraph, false, null, paragraphContainer );
                        }
                    }
                }
                else
                {
                    if ( fldCharTypeParsing )
                    {
                        rListAfterSeparate.add( o );
                    }
                    else
                    {
                        visitRun( paragraph, o, paragraphContainer );
                    }
                }
            }
        }
        finally
        {
            if ( cursor != null )
            {
                cursor.dispose();
            }
        }
    }

    private void process( CTP paragraph, T paragraphContainer, boolean pageNumber, String url,
                          List<XmlObject> rListAfterSeparate )
        throws Exception
    {
        if ( rListAfterSeparate != null )
        {
            for ( XmlObject oAfterSeparate : rListAfterSeparate )
            {
                if ( oAfterSeparate instanceof CTR )
                {
                    CTR ctr = (CTR) oAfterSeparate;
                    visitRun( ctr, paragraph, pageNumber, url, paragraphContainer );
                }
                else
                {
                    visitRun( paragraph, oAfterSeparate, paragraphContainer );
                }
            }
        }
    }

    private void visitRun( CTP paragraph, XmlObject o, T paragraphContainer )
        throws Exception
    {
        if ( o instanceof CTHyperlink )
        {
            CTHyperlink link = (CTHyperlink) o;
            String anchor = link.getAnchor();
            String href = null;
            // Test if the is an id for hyperlink
            String hyperlinkId = link.getId();
            if ( StringUtils.isNotEmpty( hyperlinkId ) )
            {
                // TODO
                // XWPFHyperlink hyperlink = document.getHyperlinkByID( hyperlinkId );
                // href = hyperlink.getURL();
            }
            for ( CTR r : link.getRList() )
            {
                visitRun( r, paragraph, false, href != null ? href : "#" + anchor, paragraphContainer );
            }
        }
        else if ( o instanceof CTSdtRun )
        {
            CTSdtContentRun run = ( (CTSdtRun) o ).getSdtContent();
            for ( CTR r : run.getRList() )
            {
                visitRun( r, paragraph, false, null, paragraphContainer );
            }
        }
        else if ( o instanceof CTRunTrackChange )
        {
            for ( CTR r : ( (CTRunTrackChange) o ).getRList() )
            {
                visitRun( r, paragraph, false, null, paragraphContainer );
            }
        }
        else if ( o instanceof CTSimpleField )
        {
            CTSimpleField simpleField = (CTSimpleField) o;
            String instr = simpleField.getInstr();
            // 1) test if it's page number
            // <w:fldSimple w:instr=" PAGE \* MERGEFORMAT "> <w:r> <w:rPr> <w:noProof/>
            // </w:rPr> <w:t>- 1 -</w:t> </w:r> </w:fldSimple>
            boolean fieldPageNumber = XWPFRunHelper.isInstrTextPage( instr );
            String fieldHref = null;
            if ( !fieldPageNumber )
            {
                // not page number, test if it's hyperlink :
                // <w:instrText>HYPERLINK "http://code.google.com/p/xdocrepor"</w:instrText>
                fieldHref = XWPFRunHelper.getInstrTextHyperlink( instr );
            }
            for ( CTR r : simpleField.getRList() )
            {
                visitRun( r, paragraph, fieldPageNumber, fieldHref, paragraphContainer );
            }
        }
        else if ( o instanceof CTSmartTagRun )
        {
            // Smart Tags can be nested many times.
            // This implementation does not preserve the tagging information
            // buildRunsInOrderFromXml(o);
        }
        else if ( o instanceof CTBookmark )
        {
            CTBookmark bookmark = (CTBookmark) o;
            visitBookmark( bookmark, paragraph, paragraphContainer );
        }
    }

    protected abstract T startVisitParagraph( CTP paragraph, ListItemContext itemContext, T container )
        throws Exception;

    protected abstract void pageBreak()
        throws Exception;

    protected void visitRun( CTR run, CTP paragraph, boolean pageNumber, String url, T paragraphContainer )
        throws Exception

    {

        // Loop for each element of <w:run text, tab, image etc
        // to keep the order of thoses elements.
        XmlCursor c = run.newCursor();
        c.selectPath( "./*" );
        while ( c.toNextSelection() )
        {
            XmlObject o = c.getObject();

            if ( o instanceof CTText )
            {
                CTText ctText = (CTText) o;
                String tagName = o.getDomNode().getNodeName();
                // Field Codes (w:instrText, defined in spec sec. 17.16.23)
                // come up as instances of CTText, but we don't want them
                // in the normal text output
                if ( "w:instrText".equals( tagName ) )
                {

                }
                else
                {
                    visitText( ctText, pageNumber, paragraphContainer );
                }
            }
            else if ( o instanceof CTPTab )
            {
                visitTab( (CTPTab) o, paragraphContainer );
            }
            else if ( o instanceof CTBr )
            {
                visitBR( (CTBr) o, paragraphContainer );
            }
            else if ( o instanceof CTEmpty )
            {
                // Some inline text elements get returned not as
                // themselves, but as CTEmpty, owing to some odd
                // definitions around line 5642 of the XSDs
                // This bit works around it, and replicates the above
                // rules for that case
                String tagName = o.getDomNode().getNodeName();
                if ( "w:tab".equals( tagName ) )
                {
                    CTTabs tabs = null;// stylesDocument.getParagraphTabs( paragraph );
                    visitTabs( tabs, paragraphContainer );
                }
                if ( "w:br".equals( tagName ) )
                {
                    visitBR( null, paragraphContainer );
                }
                if ( "w:cr".equals( tagName ) )
                {
                    visitBR( null, paragraphContainer );
                }
            }
            else if ( o instanceof CTDrawing )
            {
                visitDrawing( (CTDrawing) o, paragraphContainer );
            }
        }
        c.dispose();
    }

    protected abstract void visitText( CTText ctText, boolean pageNumber, T paragraphContainer )
        throws Exception;

    protected abstract void visitTab( CTPTab o, T paragraphContainer )
        throws Exception;

    protected abstract void visitTabs( CTTabs tabs, T paragraphContainer )
        throws Exception;

    protected void visitBR( CTBr br, T paragraphContainer )
        throws Exception
    {
        STBrType.Enum brType = XWPFRunHelper.getBrType( br );
        if ( brType.equals( STBrType.PAGE ) )
        {
            pageBreakOnNextParagraph = true;
        }
        else
        {
            addNewLine( br, paragraphContainer );
        }
    }

    protected abstract void visitBookmark( CTBookmark bookmark, CTP paragraph, T paragraphContainer )
        throws Exception;

    protected abstract void addNewLine( CTBr br, T paragraphContainer )
        throws Exception;

    // --------------------- Table

    protected void visitTable( CTTbl table, int i, T container )
        throws Exception
    {
        tables.push( table );
        float[] colWidths = XWPFTableUtil.computeColWidths( table );
        T tableContainer = startVisitTable( table, colWidths, container );
        visitTableBody( table, colWidths, tableContainer );
        endVisitTable( table, container, tableContainer );
        tables.pop();
    }

    protected abstract T startVisitTable( CTTbl table, float[] colWidths, T tableContainer )
        throws Exception;

    protected abstract void endVisitTable( CTTbl table, T parentContainer, T tableContainer )
        throws Exception;

    protected void visitTableBody( CTTbl table, float[] colWidths, T tableContainer )
        throws Exception
    {
        XmlCursor cursor = null;
        try
        {
            cursor = table.newCursor();
            cursor.selectPath( "./*" );
            while ( cursor.toNextSelection() )
            {
                XmlObject o = cursor.getObject();
                if ( o instanceof CTRow )
                {
                    visitTableRow( (CTRow) o, tableContainer );
                }
            }
        }
        finally
        {
            if ( cursor != null )
            {
                cursor.dispose();
            }
        }
    }

    protected void visitTableRow( CTRow row, T tableContainer )
        throws Exception
    {
        boolean headerRow = stylesDocument.isTableRowHeader( row );
        startVisitTableRow( row, tableContainer, headerRow );

        XmlCursor cursor = null;
        try
        {
            cursor = row.newCursor();
            cursor.selectPath( "./*" );
            while ( cursor.toNextSelection() )
            {
                XmlObject o = cursor.getObject();
                if ( o instanceof CTTc )
                {
                    if ( o instanceof CTTc )
                    {
                        CTTc cell = (CTTc) o;
                        visitCell( cell, tableContainer );
                    }
                    else if ( o instanceof CTSdtCell )
                    {
                        CTSdtCell sdtCell = (CTSdtCell) o;
                        List<CTTc> tcList = sdtCell.getSdtContent().getTcList();
                        for ( CTTc cell : tcList )
                        {
                            visitCell( cell, tableContainer );
                        }
                    }
                }
            }
        }
        finally
        {
            if ( cursor != null )
            {
                cursor.dispose();
            }
        }

    }

    protected abstract void startVisitTableRow( CTRow row, T tableContainer, boolean headerRow )
        throws Exception;

    protected void visitCell( CTTc cell, T tableContainer )
        throws Exception
    {
        T tableCellContainer = startVisitTableCell( cell, tableContainer );
        visitTableCellBody( cell, tableCellContainer );
        endVisitTableCell( cell, tableContainer, tableCellContainer );
    }

    protected abstract T startVisitTableCell( CTTc cell, T tableContainer )
        throws Exception;

    protected void visitTableCellBody( CTTc cell, T tableCellContainer )
        throws Exception
    {
        visitBodyElements( cell, tableCellContainer );
    }

    protected abstract void endVisitTableCell( CTTc cell, T tableContainer, T tableCellContainer )
        throws Exception;

    // ------------------------ Image --------------

    protected void visitDrawing( CTDrawing drawing, T parentContainer )
        throws Exception
    {
        List<CTInline> inlines = drawing.getInlineList();
        for ( CTInline inline : inlines )
        {
            visitInline( inline, parentContainer );
        }
        List<CTAnchor> anchors = drawing.getAnchorList();
        for ( CTAnchor anchor : anchors )
        {
            visitAnchor( anchor, parentContainer );
        }
    }

    protected void visitAnchor( CTAnchor anchor, T parentContainer )
        throws Exception
    {
        CTGraphicalObject graphic = anchor.getGraphic();

        /*
         * wp:positionH relativeFrom="column"> <wp:posOffset>-898525</wp:posOffset> </wp:positionH>
         */
        STRelFromH.Enum relativeFromH = null;
        Float offsetX = null;
        CTPosH positionH = anchor.getPositionH();
        if ( positionH != null )
        {
            relativeFromH = positionH.getRelativeFrom();
            //offsetX = DxaUtil.emu2points( positionH.getPosOffset() );
        }

        STRelFromV.Enum relativeFromV = null;
        Float offsetY = null;
        CTPosV positionV = anchor.getPositionV();
        if ( positionV != null )
        {
            relativeFromV = positionV.getRelativeFrom();
            offsetY = DxaUtil.emu2points( positionV.getPosOffset() );
        }

        STWrapText.Enum wrapText = null;
        CTWrapSquare wrapSquare = anchor.getWrapSquare();
        if ( wrapSquare != null )
        {
            wrapText = wrapSquare.getWrapText();
        }

        visitGraphicalObject( parentContainer, graphic, offsetX, relativeFromH, offsetY, relativeFromV, wrapText );
    }

    protected void visitInline( CTInline inline, T parentContainer )
        throws Exception
    {
        CTGraphicalObject graphic = inline.getGraphic();
        visitGraphicalObject( parentContainer, graphic, null, null, null, null, null );
    }

    private void visitGraphicalObject( T parentContainer, CTGraphicalObject graphic, Float offsetX,
                                       STRelFromH.Enum relativeFromH, Float offsetY, STRelFromV.Enum relativeFromV,
                                       STWrapText.Enum wrapText )
        throws Exception
    {
        if ( graphic != null )
        {
            CTGraphicalObjectData graphicData = graphic.getGraphicData();
            if ( graphicData != null )
            {
                XmlCursor c = graphicData.newCursor();
                c.selectPath( "./*" );
                while ( c.toNextSelection() )
                {
                    XmlObject o = c.getObject();
                    if ( o instanceof CTPicture )
                    {
                        CTPicture picture = (CTPicture) o;
                        // extract the picture if needed
                        IImageExtractor extractor = getImageExtractor();
                        if ( extractor != null )
                        {
                            /*
                             * XWPFPictureData pictureData = getPictureData( picture ); if ( pictureData != null ) { try
                             * { extractor.extract( WORD_MEDIA + pictureData.getFileName(), pictureData.getData() ); }
                             * catch ( Throwable e ) { LOGGER.log( Level.SEVERE, "Error while extracting the image " +
                             * pictureData.getFileName(), e ); } }
                             */
                        }
                        // visit the picture.

                        visitPicture( picture, offsetX, relativeFromH, offsetY, relativeFromV, wrapText,
                                      parentContainer );
                    }
                }
                c.dispose();
            }
        }
    }

    protected abstract void visitPicture( CTPicture picture,
                                          Float offsetX,
                                          org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromH.Enum relativeFromH,
                                          Float offsetY,
                                          org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.Enum relativeFromV,
                                          STWrapText.Enum wrapText, T parentContainer )
        throws Exception;

    // ------------------------------ Header/Footer visitor -----------

    public void visitHeaderRef( CTHdrFtrRef headerRef, CTSectPr sectPr, E masterPage )
        throws Exception
    {
        this.currentHeaderRef = headerRef;
        this.currentHeader = getHeader( headerRef );
        visitHeader( currentHeader, headerRef, sectPr, masterPage );
        this.currentHeader = null;
        this.currentHeaderRef = null;
    }

    protected abstract void visitHeader( CTHdrFtr currentHeader, CTHdrFtrRef headerRef, CTSectPr sectPr, E masterPage )
        throws Exception;

    private CTHdrFtr getHeader( CTHdrFtrRef headerRef )
        throws Exception
    {
        String relId = headerRef.getId();
        HdrDocument hdrDoc = provider.getHdrDocumentByPartId( relId );
        CTHdrFtr hdrFtr = hdrDoc.getHdr();
        return hdrFtr;
    }

    public void visitFooterRef( CTHdrFtrRef footerRef, CTSectPr sectPr, E masterPage )
        throws Exception
    {
        this.currentFooterRef = footerRef;
        this.currentFooter = getFooter( footerRef );
        visitFooter( currentFooter, footerRef, sectPr, masterPage );
        this.currentFooter = null;
        this.currentFooterRef = null;
    }

    protected abstract void visitFooter( CTHdrFtr currentFooter, CTHdrFtrRef footerRef, CTSectPr sectPr, E masterPage )
        throws Exception;

    private CTHdrFtr getFooter( CTHdrFtrRef footerRef )
        throws Exception
    {
        String relId = footerRef.getId();
        FtrDocument hdrDoc = provider.getFtrDocumentByPartId( relId );
        CTHdrFtr hdrFtr = hdrDoc.getFtr();
        return hdrFtr;
    }

    /**
     * Returns the image extractor and null otherwise.
     * 
     * @return
     */
    protected IImageExtractor getImageExtractor()
    {
        return options.getExtractor();
    }

    /**
     * Returns true if word/document.xml is parsing and false otherwise.
     * 
     * @return true if word/document.xml is parsing and false otherwise.
     */
    protected boolean isWordDocumentPartParsing()
    {
        return currentHeader == null && currentFooter == null;
    }

    public CTTbl getParentTable()
    {
        if ( tables.isEmpty() )
        {
            return null;
        }
        return tables.peek();

    }

    public byte[] getPictureBytes( CTPicture picture )
        throws Exception
    {
        CTBlipFillProperties blipProps = picture.getBlipFill();

        if ( blipProps == null || !blipProps.isSetBlip() )
        {
            // return null if Blip data is missing
            return null;
        }

        String blipId = blipProps.getBlip().getEmbed();
        InputStream in = provider.getInputStreamByRelId( getPartRelIdParsing(), blipId );
        if ( in == null )
        {
            return null;
        }
        return IOUtils.toByteArray( in );
    }

    private String getPartRelIdParsing()
    {
        if ( currentHeaderRef != null )
        {
            return currentHeaderRef.getId();
        }
        if ( currentFooterRef != null )
        {
            return currentFooterRef.getId();
        }
        return null;
    }
}
