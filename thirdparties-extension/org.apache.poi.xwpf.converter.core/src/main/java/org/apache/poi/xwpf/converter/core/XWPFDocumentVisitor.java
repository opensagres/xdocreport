package org.apache.poi.xwpf.converter.core;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.core.utils.XWPFTableUtil;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.IBody;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTPosH;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTPosV;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromH;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;

public abstract class XWPFDocumentVisitor<T, O extends Options, E extends IXWPFMasterPage>
{

    protected final XWPFDocument document;

    private final MasterPageManager masterPageManager;

    private XWPFHeader currentHeader;

    private XWPFFooter currentFooter;

    protected final XWPFStylesDocument stylesDocument;

    protected final O options;

    private String currentInstrText;

    private boolean pageBreakOnNextParagraph;

    public XWPFDocumentVisitor( XWPFDocument document, O options )
        throws Exception
    {
        this.document = document;
        this.options = options;
        this.masterPageManager = new MasterPageManager( document, this );
        this.stylesDocument = createStylesDocument( document );
    }

    protected XWPFStylesDocument createStylesDocument( XWPFDocument document )
        throws XmlException, IOException
    {
        return new XWPFStylesDocument( document );
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
        T container = startVisitDocument();
        // Create IText, XHTML element for each XWPF elements from the w:body
        List<IBodyElement> bodyElements = document.getBodyElements();
        visitBodyElements( bodyElements, container );

        endVisitDocument();

    }

    protected abstract T startVisitDocument()
        throws Exception;

    protected abstract void endVisitDocument()
        throws Exception;

    // ------------------------------ XWPF Elements visitor -----------

    protected void visitBodyElements( List<IBodyElement> bodyElements, T container )
        throws Exception
    {
        if ( !masterPageManager.isInitialized() )
        {
            // master page manager which hosts each <:w;sectPr declared in the word/document.xml
            // must be initialized. The initialisation loop for each
            // <w:p paragraph to compute a list of <w:sectPr which contains information
            // about header/footer declared in the <w:headerReference/<w:footerReference
            masterPageManager.initialize();
        }
        for ( int i = 0; i < bodyElements.size(); i++ )
        {
            IBodyElement bodyElement = bodyElements.get( i );
            visitBodyElement( bodyElement, i, container );
        }

    }

    protected void visitBodyElement( IBodyElement bodyElement, int index, T container )
        throws Exception
    {
        switch ( bodyElement.getElementType() )
        {
            case PARAGRAPH:
                visitParagraph( (XWPFParagraph) bodyElement, index, container );
                break;
            case TABLE:
                visitTable( (XWPFTable) bodyElement, index, container );
                break;
        }
    }

    protected void visitParagraph( XWPFParagraph paragraph, int index, T container )
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
        this.currentInstrText = null;
        if ( pageBreakOnNextParagraph )
        {
            pageBreak();
        }
        this.pageBreakOnNextParagraph = false;

        T paragraphContainer = startVisitParagraph( paragraph, container );
        visitParagraphBody( paragraph, index, paragraphContainer );
        endVisitParagraph( paragraph, container, paragraphContainer );
        this.currentInstrText = null;
    }

    protected abstract T startVisitParagraph( XWPFParagraph paragraph, T parentContainer )
        throws Exception;

    protected abstract void endVisitParagraph( XWPFParagraph paragraph, T parentContainer, T paragraphContainer )
        throws Exception;

    protected void visitParagraphBody( XWPFParagraph paragraph, int index, T paragraphContainer )
        throws Exception
    {
        List<XWPFRun> runs = paragraph.getRuns();
        if ( runs.isEmpty() )
        {
            // a new line must be generated if :
            // - there is next paragraph/table
            // - if the body is a cell (with none vMerge) and contains just this paragraph
            if ( isAddNewLine( paragraph, index ) )
            {
                visitEmptyRun( paragraphContainer );
            }

            // sometimes, POI tells that run is empty
            // but it can be have w:r in the w:pPr
            // <w:p><w:pPr .. <w:r> => See the header1.xml of DocxBig.docx ,
            // => test if it exist w:r
            // CTP p = paragraph.getCTP();
            // CTPPr pPr = p.getPPr();
            // if (pPr != null) {
            // XmlObject[] wRuns =
            // pPr.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//w:r");
            // if (wRuns != null) {
            // for ( int i = 0; i < wRuns.length; i++ )
            // {
            // XmlObject o = wRuns[i];
            // o.getDomNode().getParentNode()
            // if (o instanceof CTR) {
            // System.err.println(wRuns[i]);
            // }
            //
            // }
            // }
            // }
            // //XmlObject[] t =
            // o.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//w:t");
            // //paragraph.getCTP().get
        }
        else
        {
            // Loop for each element of <w:r, w:fldSimple
            // to keep the order of thoses elements.
            CTP ctp = paragraph.getCTP();
            visitRuns( paragraph, ctp, paragraphContainer );

            // for ( XWPFRun run : paragraph.getRuns() )
            // {
            // Node parent = run.getCTR().getDomNode().getParentNode();
            // // XmlObject u = (XmlObject)parent;
            // // parent.getUser() ;
            //
            // System.err.println( parent );
            // visitRun( run, paragraphContainer );
            // }
        }

        // Page Break
        // Cannot use paragraph.isPageBreak() because it throws NPE because
        // pageBreak.getVal() can be null.
        CTPPr ppr = paragraph.getCTP().getPPr();
        if ( ppr != null )
        {
            if ( ppr.isSetPageBreakBefore() )
            {
                CTOnOff pageBreak = ppr.getPageBreakBefore();
                if ( pageBreak != null
                    && ( pageBreak.getVal() == null || pageBreak.getVal().intValue() == STOnOff.INT_TRUE ) )
                {
                    pageBreak();
                }
            }
        }
    }

    private boolean isAddNewLine( XWPFParagraph paragraph, int index )
    {
        // a new line must be generated if :
        // - there is next paragraph/table
        // - if the body is a cell (with none vMerge) and contains just this paragraph
        IBody body = paragraph.getBody();
        List<IBodyElement> bodyElements = body.getBodyElements();
        if ( body.getPartType() == BodyType.TABLECELL && bodyElements.size() == 1 )
        {
            XWPFTableCell cell = (XWPFTableCell) body;
            STMerge.Enum vMerge = stylesDocument.getTableCellVMerge( cell );
            if ( vMerge != null && vMerge.equals( STMerge.CONTINUE ) )
            {
                // here a new line must not be generated because the body is a cell (with none vMerge) and contains just
                // this paragraph
                return false;
            }
            // Loop for each cell of the row : if all cells are empty, new line must be generated otherwise none empty
            // line must be generated.
            XWPFTableRow row = cell.getTableRow();
            List<XWPFTableCell> cells = row.getTableCells();
            for ( int i = 0; i < cells.size(); i++ ) 
            {
                XWPFTableCell c = cells.get( i );
                if (c.getBodyElements().size() != 1) {
                    return false;
                }
                IBodyElement element = c.getBodyElements().get( 0 );
                if (element.getElementType() != BodyElementType.PARAGRAPH) {
                    return false;
                }
                return ((XWPFParagraph)element).getRuns().size() == 0;
            }
            return true;

        }
        // here a new line must be generated if there is next paragraph/table
        return bodyElements.size() > index + 1;
    }

    private void visitRuns( XWPFParagraph paragraph, CTP ctp, T paragraphContainer )
        throws Exception
    {
        XmlCursor c = ctp.newCursor();
        c.selectPath( "child::*" );
        while ( c.toNextSelection() )
        {
            XmlObject o = c.getObject();
            if ( o instanceof CTR )
            {
                XWPFRun run = new XWPFRun( (CTR) o, paragraph );
                visitRun( run, false, paragraphContainer );
            }
            if ( o instanceof CTHyperlink )
            {
                CTHyperlink link = (CTHyperlink) o;
                for ( CTR r : link.getRList() )
                {
                    XWPFRun run = new XWPFHyperlinkRun( link, r, paragraph );
                    visitRun( run, false, paragraphContainer );
                }
            }
            if ( o instanceof CTSdtRun )
            {
                CTSdtContentRun run = ( (CTSdtRun) o ).getSdtContent();
                for ( CTR r : run.getRList() )
                {
                    XWPFRun ru = new XWPFRun( r, paragraph );
                    visitRun( ru, false, paragraphContainer );
                }
            }
            if ( o instanceof CTRunTrackChange )
            {
                for ( CTR r : ( (CTRunTrackChange) o ).getRList() )
                {
                    XWPFRun run = new XWPFRun( r, paragraph );
                    visitRun( run, false, paragraphContainer );
                }
            }
            if ( o instanceof CTSimpleField )
            {
                CTSimpleField simpleField = (CTSimpleField) o;
                /*
                 * Support page numbering<w:fldSimple w:instr=" PAGE \* MERGEFORMAT "> <w:r> <w:rPr> <w:noProof/>
                 * </w:rPr> <w:t>- 1 -</w:t> </w:r> </w:fldSimple>
                 */
                String instr = simpleField.getInstr();
                boolean pageNumber = instr.toLowerCase().contains( "page" );
                for ( CTR r : simpleField.getRList() )
                {
                    XWPFRun run = new XWPFRun( r, paragraph );
                    visitRun( run, pageNumber, paragraphContainer );
                }
            }
            if ( o instanceof CTSmartTagRun )
            {
                // Smart Tags can be nested many times.
                // This implementation does not preserve the tagging information
                // buildRunsInOrderFromXml(o);
            }
        }
        c.dispose();
    }

    protected abstract void visitEmptyRun( T paragraphContainer )
        throws Exception;

    protected void visitRun( XWPFRun run, boolean pageNumber, T paragraphContainer )
        throws Exception
    {
        CTR ctr = run.getCTR();

        // <w:lastRenderedPageBreak />
        // List<CTEmpty> lastRenderedPageBreakList = ctr.getLastRenderedPageBreakList();
        // if ( lastRenderedPageBreakList != null && lastRenderedPageBreakList.size() > 0 )
        // {
        // for ( CTEmpty lastRenderedPageBreak : lastRenderedPageBreakList )
        // {
        // pageBreak();
        // }
        // }

        // Loop for each element of <w:run text, tab, image etc
        // to keep the order of thoses elements.
        XmlCursor c = ctr.newCursor();
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
                    currentInstrText = ctText.getStringValue();
                }
                else
                {
                    // Check if it's hyperlink
                    String hrefHyperlink = getCurrentHRefHyperLink();
                    if ( hrefHyperlink != null )
                    {
                        visitHyperlink( ctText, hrefHyperlink, paragraphContainer );
                    }
                    else
                    {
                        visitText( ctText, pageNumber, paragraphContainer );
                    }
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
                    CTTabs tabs = stylesDocument.getParagraphTabs( run.getParagraph() );
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

    private String getCurrentHRefHyperLink()
    {
        if ( StringUtils.isEmpty( currentInstrText ) )
        {
            return null;
        }
        currentInstrText = currentInstrText.trim();
        // test if it's <w:instrText>HYPERLINK "http://code.google.com/p/xdocrepor"</w:instrText>
        if ( currentInstrText.startsWith( "HYPERLINK" ) )
        {
            currentInstrText = currentInstrText.substring( "HYPERLINK".length(), currentInstrText.length() );
            currentInstrText = currentInstrText.trim();
            if ( currentInstrText.startsWith( "\"" ) )
            {
                currentInstrText = currentInstrText.substring( 1, currentInstrText.length() );
            }
            if ( currentInstrText.endsWith( "\"" ) )
            {
                currentInstrText = currentInstrText.substring( 0, currentInstrText.length() - 1 );
            }
            return currentInstrText;
        }
        return null;
    }

    protected abstract void visitHyperlink( CTText ctText, String hrefHyperlink, T paragraphContainer )
        throws Exception;

    protected abstract void visitText( CTText ctText, boolean pageNumber, T paragraphContainer )
        throws Exception;

    protected abstract void visitTab( CTPTab o, T paragraphContainer )
        throws Exception;

    protected abstract void visitTabs( CTTabs tabs, T paragraphContainer )
        throws Exception;

    protected void visitBR( CTBr br, T paragraphContainer )
        throws Exception
    {
        org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType.Enum brType = getBrType( br );
        if ( brType.equals( STBrType.PAGE ) )
        {
            pageBreakOnNextParagraph = true;
        }
        else
        {
            addNewLine( br, paragraphContainer );
        }
    }

    private org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType.Enum getBrType( CTBr br )
    {
        if ( br != null )
        {
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType.Enum brType = br.getType();
            if ( brType != null )
            {
                return brType;
            }
        }
        return STBrType.TEXT_WRAPPING;
    }

    protected abstract void addNewLine( CTBr br, T paragraphContainer )
        throws Exception;

    protected abstract void pageBreak()
        throws Exception;

    protected void visitTable( XWPFTable table, int index, T container )
        throws Exception
    {
        // 1) Compute colWidth
        float[] colWidths = XWPFTableUtil.computeColWidths( table );
        T tableContainer = startVisitTable( table, colWidths, container );
        visitTableBody( table, colWidths, tableContainer );
        endVisitTable( table, container, tableContainer );
    }

    protected void visitTableBody( XWPFTable table, float[] colWidths, T tableContainer )
        throws Exception
    {
        // Proces Row
        boolean firstRow = false;
        boolean lastRow = false;

        List<XWPFTableRow> rows = table.getRows();
        int rowsSize = rows.size();
        for ( int i = 0; i < rowsSize; i++ )
        {
            firstRow = ( i == 0 );
            lastRow = isLastRow( i, rowsSize );
            XWPFTableRow row = rows.get( i );
            visitTableRow( row, colWidths, tableContainer, firstRow, lastRow, i, rowsSize );
        }
    }

    private boolean isLastRow( int rowIndex, int rowsSize )
    {
        return rowIndex == rowsSize - 1;
    }

    protected abstract T startVisitTable( XWPFTable table, float[] colWidths, T tableContainer )
        throws Exception;

    protected abstract void endVisitTable( XWPFTable table, T parentContainer, T tableContainer )
        throws Exception;

    protected void visitTableRow( XWPFTableRow row, float[] colWidths, T tableContainer, boolean firstRow,
                                  boolean lastRowIfNoneVMerge, int rowIndex, int rowsSize )
        throws Exception
    {
        startVisitTableRow( row, tableContainer, rowIndex );

        int nbColumns = colWidths.length;
        // Process cell
        boolean firstCol = true;
        boolean lastCol = false;
        boolean lastRow = false;
        List<XWPFTableCell> vMergedCells = null;
        List<XWPFTableCell> cells = row.getTableCells();
        if ( nbColumns > cells.size() )
        {
            // Columns number is not equal to cells number.
            // POI have a bug with
            // <w:tr w:rsidR="00C55C20">
            // <w:tc>
            // <w:tc>...
            // <w:sdt>
            // <w:sdtContent>
            // <w:tc> <= this tc which is a XWPFTableCell is not included in the row.getTableCells();

            firstCol = true;
            int cellIndex = -1;
            CTRow ctRow = row.getCtRow();
            XmlCursor c = ctRow.newCursor();
            c.selectPath( "./*" );
            while ( c.toNextSelection() )
            {
                XmlObject o = c.getObject();
                if ( o instanceof CTTc )
                {
                    CTTc tc = (CTTc) o;
                    XWPFTableCell cell = row.getTableCell( tc );
                    cellIndex = getCellIndex( cellIndex, cell );
                    lastCol = ( cellIndex == nbColumns );
                    vMergedCells = getVMergedCells( cell, rowIndex, cellIndex );
                    if ( vMergedCells == null || vMergedCells.size() > 0 )
                    {
                        lastRow = isLastRow( lastRowIfNoneVMerge, rowIndex, rowsSize, vMergedCells );
                        visitCell( cell, tableContainer, firstRow, lastRow, firstCol, lastCol, rowIndex, cellIndex,
                                   vMergedCells );
                    }
                    firstCol = false;
                }
                else if ( o instanceof CTSdtCell )
                {
                    // Fix bug of POI
                    CTSdtCell sdtCell = (CTSdtCell) o;
                    List<CTTc> tcList = sdtCell.getSdtContent().getTcList();
                    for ( CTTc ctTc : tcList )
                    {
                        XWPFTableCell cell = new XWPFTableCell( ctTc, row, row.getTable().getBody() );
                        cellIndex = getCellIndex( cellIndex, cell );
                        lastCol = ( cellIndex == nbColumns );
                        vMergedCells = getVMergedCells( cell, rowIndex, cellIndex );
                        if ( vMergedCells == null || vMergedCells.size() > 0 )
                        {
                            lastRow = isLastRow( lastRowIfNoneVMerge, rowIndex, rowsSize, vMergedCells );
                            visitCell( cell, tableContainer, firstRow, lastRow, firstCol, lastCol, rowIndex, cellIndex,
                                       vMergedCells );
                        }
                        firstCol = false;
                    }
                }
            }
            c.dispose();
        }
        else
        {
            // Column number is equal to cells number.
            for ( int i = 0; i < cells.size(); i++ )
            {
                lastCol = ( i == cells.size() - 1 );
                XWPFTableCell cell = cells.get( i );
                vMergedCells = getVMergedCells( cell, rowIndex, i );
                if ( vMergedCells == null || vMergedCells.size() > 0 )
                {
                    lastRow = isLastRow( lastRowIfNoneVMerge, rowIndex, rowsSize, vMergedCells );
                    visitCell( cell, tableContainer, firstRow, lastRow, firstCol, lastCol, rowIndex, i, vMergedCells );
                }
                firstCol = false;
            }
        }

        endVisitTableRow( row, tableContainer, firstRow, lastRow );
    }

    private boolean isLastRow( boolean lastRowIfNoneVMerge, int rowIndex, int rowsSize, List<XWPFTableCell> vMergedCells )
    {
        if ( vMergedCells == null )
        {
            return lastRowIfNoneVMerge;
        }
        return isLastRow( rowIndex - 1 + vMergedCells.size(), rowsSize );
    }

    private int getCellIndex( int cellIndex, XWPFTableCell cell )
    {
        BigInteger gridSpan = stylesDocument.getTableCellGridSpan( cell.getCTTc().getTcPr() );
        if ( gridSpan != null )
        {
            cellIndex = cellIndex + gridSpan.intValue();
        }
        else
        {
            cellIndex++;
        }
        return cellIndex;
    }

    protected void startVisitTableRow( XWPFTableRow row, T tableContainer, int rowIndex )
        throws Exception
    {

    }

    protected void endVisitTableRow( XWPFTableRow row, T tableContainer, boolean firstRow, boolean lastRow )
        throws Exception
    {

    }

    protected void visitCell( XWPFTableCell cell, T tableContainer, boolean firstRow, boolean lastRow,
                              boolean firstCol, boolean lastCol, int rowIndex, int cellIndex,
                              List<XWPFTableCell> vMergedCells )
        throws Exception
    {
        T tableCellContainer =
            startVisitTableCell( cell, tableContainer, firstRow, lastRow, firstCol, lastCol, vMergedCells );
        visitTableCellBody( cell, vMergedCells, tableCellContainer );
        endVisitTableCell( cell, tableContainer, tableCellContainer );
    }

    private List<XWPFTableCell> getVMergedCells( XWPFTableCell cell, int rowIndex, int cellIndex )
    {
        List<XWPFTableCell> vMergedCells = null;
        STMerge.Enum vMerge = stylesDocument.getTableCellVMerge( cell );
        if ( vMerge != null )
        {
            if ( vMerge.equals( STMerge.RESTART ) )
            {
                // vMerge="restart"
                // Loop for each table cell of each row upon vMerge="restart" was found or cell without vMerge
                // was declared.
                vMergedCells = new ArrayList<XWPFTableCell>();
                vMergedCells.add( cell );

                XWPFTableRow row = null;
                XWPFTableCell c;
                XWPFTable table = cell.getTableRow().getTable();
                for ( int i = rowIndex + 1; i < table.getRows().size(); i++ )
                {
                    row = table.getRow( i );
                    c = row.getCell( cellIndex );
                    if ( c == null )
                    {
                        break;
                    }
                    vMerge = stylesDocument.getTableCellVMerge( c );
                    if ( vMerge != null && vMerge.equals( STMerge.CONTINUE ) )
                    {

                        vMergedCells.add( c );
                    }
                    else
                    {
                        return vMergedCells;
                    }
                }
            }
            else
            {
                // vMerge="continue", ignore the cell because it was already processed
                return Collections.emptyList();
            }
        }
        return vMergedCells;
    }

    protected void visitTableCellBody( XWPFTableCell cell, List<XWPFTableCell> vMergeCells, T tableCellContainer )
        throws Exception
    {
        if ( vMergeCells != null )
        {

            for ( XWPFTableCell mergedCell : vMergeCells )
            {
                List<IBodyElement> bodyElements = mergedCell.getBodyElements();
                visitBodyElements( bodyElements, tableCellContainer );
            }
        }
        else
        {
            List<IBodyElement> bodyElements = cell.getBodyElements();
            visitBodyElements( bodyElements, tableCellContainer );
        }
    }

    protected abstract T startVisitTableCell( XWPFTableCell cell, T tableContainer, boolean firstRow, boolean lastRow,
                                              boolean firstCol, boolean lastCol, List<XWPFTableCell> vMergeCells )
        throws Exception;

    protected abstract void endVisitTableCell( XWPFTableCell cell, T tableContainer, T tableCellContainer )
        throws Exception;

    protected XWPFStyle getXWPFStyle( String styleID )
    {
        if ( styleID == null )
            return null;
        else
            return document.getStyles().getStyle( styleID );
    }

    /**
     * Return true if word/document.xml is parsing and false otherwise.
     * 
     * @return
     */
    protected boolean isWordDocumentPartParsing()
    {
        return currentHeader == null && currentFooter == null;
    }

    // ------------------------------ Header/Footer visitor -----------

    protected void visitHeaderRef( CTHdrFtrRef headerRef, CTSectPr sectPr, E masterPage )
        throws Exception
    {
        this.currentHeader = getXWPFHeader( headerRef );
        visitHeader( currentHeader, headerRef, sectPr, masterPage );
        this.currentHeader = null;
    }

    protected abstract void visitHeader( XWPFHeader header, CTHdrFtrRef headerRef, CTSectPr sectPr, E masterPage )
        throws Exception;

    protected void visitFooterRef( CTHdrFtrRef footerRef, CTSectPr sectPr, E masterPage )
        throws Exception
    {
        this.currentFooter = getXWPFFooter( footerRef );
        visitFooter( currentFooter, footerRef, sectPr, masterPage );
        this.currentFooter = null;
    }

    protected abstract void visitFooter( XWPFFooter footer, CTHdrFtrRef footerRef, CTSectPr sectPr, E masterPage )
        throws Exception;

    protected XWPFHeader getXWPFHeader( CTHdrFtrRef headerRef )
        throws XmlException, IOException
    {
        PackagePart hdrPart = document.getPartById( headerRef.getId() );
        List<XWPFHeader> headers = document.getHeaderList();
        for ( XWPFHeader header : headers )
        {
            if ( header.getPackagePart().equals( hdrPart ) )
            {
                return header;
            }
        }
        HdrDocument hdrDoc = HdrDocument.Factory.parse( hdrPart.getInputStream() );
        CTHdrFtr hdrFtr = hdrDoc.getHdr();
        XWPFHeader hdr = new XWPFHeader( document, hdrFtr );
        return hdr;
    }

    protected XWPFFooter getXWPFFooter( CTHdrFtrRef footerRef )
        throws XmlException, IOException
    {
        PackagePart hdrPart = document.getPartById( footerRef.getId() );
        List<XWPFFooter> footers = document.getFooterList();
        for ( XWPFFooter footer : footers )
        {
            if ( footer.getPackagePart().equals( hdrPart ) )
            {
                return footer;
            }
        }

        FtrDocument hdrDoc = FtrDocument.Factory.parse( hdrPart.getInputStream() );
        CTHdrFtr hdrFtr = hdrDoc.getFtr();
        XWPFFooter ftr = new XWPFFooter( document, hdrFtr );
        return ftr;
    }

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
            offsetX = DxaUtil.emu2points( positionH.getPosOffset() );
        }

        STRelFromV.Enum relativeFromV = null;
        Float offsetY = null;
        CTPosV positionV = anchor.getPositionV();
        if ( positionV != null )
        {
            relativeFromV = positionV.getRelativeFrom();
            offsetY = DxaUtil.emu2points( positionV.getPosOffset() );
        }
        visitGraphicalObject( parentContainer, graphic, offsetX, relativeFromH, offsetY, relativeFromV );
    }

    protected void visitInline( CTInline inline, T parentContainer )
        throws Exception
    {
        CTGraphicalObject graphic = inline.getGraphic();
        visitGraphicalObject( parentContainer, graphic, null, null, null, null );
    }

    private void visitGraphicalObject( T parentContainer, CTGraphicalObject graphic, Float offsetX,
                                       STRelFromH.Enum relativeFromH, Float offsetY, STRelFromV.Enum relativeFromV )
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
                        visitPicture( (CTPicture) o, offsetX, relativeFromH, offsetY, relativeFromV, parentContainer );
                    }
                }
                c.dispose();
            }
        }
    }

    protected abstract void visitPicture( CTPicture picture, Float offsetX, STRelFromH.Enum relativeFromH,
                                          Float offsetY, STRelFromV.Enum relativeFromV, T parentContainer )
        throws Exception;

    protected XWPFPictureData getPictureDataByID( String blipId )
    {
        if ( currentHeader != null )
        {
            return currentHeader.getPictureDataByID( blipId );
        }
        if ( currentFooter != null )
        {
            return currentFooter.getPictureDataByID( blipId );
        }
        return document.getPictureDataByID( blipId );
    }

    /**
     * Set active master page.
     * 
     * @param masterPage
     */
    protected abstract void setActiveMasterPage( E masterPage );

    /**
     * Create an instance of master page.
     * 
     * @param sectPr
     * @return
     */
    protected abstract IXWPFMasterPage createMasterPage( CTSectPr sectPr );

}
