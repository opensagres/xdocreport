package org.apache.poi.xwpf.converters.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xwpf.converters.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument;

public abstract class XWPFDocumentVisitor<E extends IXWPFMasterPage>
{

    private final XWPFDocument document;

    private final MasterPageManager masterPageManager;

    private XWPFHeader currentHeader;

    private XWPFFooter currentFooter;

    protected final XWPFStylesDocument stylesDocument;

    public XWPFDocumentVisitor( XWPFDocument document )
        throws Exception
    {
        this.document = document;
        this.masterPageManager = new MasterPageManager( document, this );
        this.stylesDocument = new XWPFStylesDocument( document );
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
    public void visit( OutputStream out )
        throws Exception
    {
        T container = startVisitDocument( out );

        // Create IText, XHTML element for each XWPF elements from the w:body
        List<IBodyElement> bodyElements = document.getBodyElements();
        visitBodyElements( bodyElements, container );

        endVisitDocument();
        out.close();
    }

    protected abstract T startVisitDocument( OutputStream out )
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
        for ( IBodyElement bodyElement : bodyElements )
        {
            visitBodyElement( bodyElement, container );
        }
    }

    protected void visitBodyElement( IBodyElement bodyElement, T container )
        throws Exception
    {
        switch ( bodyElement.getElementType() )
        {
            case PARAGRAPH:
                visitParagraph( (XWPFParagraph) bodyElement, container );
                break;
            case TABLE:
                visitTable( (XWPFTable) bodyElement, container );
                break;
        }
    }

    protected void visitParagraph( XWPFParagraph paragraph, T container )
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
        T paragraphContainer = startVisitPargraph( paragraph, container );
        visitParagraphBody( paragraph, paragraphContainer );
        endVisitPargraph( paragraph, container, paragraphContainer );
    }

    protected abstract T startVisitPargraph( XWPFParagraph paragraph, T parentContainer )
        throws Exception;

    protected abstract void endVisitPargraph( XWPFParagraph paragraph, T parentContainer, T paragraphContainer )
        throws Exception;

    protected void visitParagraphBody( XWPFParagraph paragraph, T paragraphContainer )
        throws Exception
    {
        List<XWPFRun> runs = paragraph.getRuns();
        if ( runs.isEmpty() )
        {
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

            visitEmptyRun( paragraphContainer );
        }
        else
        {
            for ( XWPFRun run : paragraph.getRuns() )
            {
                visitRun( run, paragraphContainer );
            }
        }
    }

    protected abstract void visitEmptyRun( T paragraphContainer )
        throws Exception;

    protected abstract void visitRun( XWPFRun run, T paragraphContainer )
        throws Exception;

    protected void visitTable( XWPFTable table, T container )
        throws Exception
    {
        T tableContainer = startVisitTable( table, container );
        visitTableBody( table, tableContainer );
        endVisitTable( table, container, tableContainer );
    }

    protected void visitTableBody( XWPFTable table, T tableContainer )
        throws Exception
    {
        // Proces Row
        boolean firstRow = false;
        boolean lastRow = false;
        List<XWPFTableRow> rows = table.getRows();
        for ( int i = 0; i < rows.size(); i++ )
        {
            firstRow = ( i == 0 );
            lastRow = ( i == rows.size() - 1 );
            XWPFTableRow row = rows.get( i );
            visitTableRow( row, tableContainer, firstRow, lastRow );
        }
    }

    protected abstract T startVisitTable( XWPFTable table, T tableContainer )
        throws Exception;

    protected abstract void endVisitTable( XWPFTable table, T parentContainer, T tableContainer )
        throws Exception;

    protected void visitTableRow( XWPFTableRow row, T tableContainer, boolean firstRow, boolean lastRow )
        throws Exception
    {
        // Process cell
        boolean firstCell = false;
        boolean lastCell = false;
        List<XWPFTableCell> cells = row.getTableCells();
        for ( int i = 0; i < cells.size(); i++ )
        {
            firstCell = ( i == 0 );
            lastCell = ( i == cells.size() - 1 );
            XWPFTableCell cell = cells.get( i );
            visitCell( cell, tableContainer, firstRow, lastRow, firstCell, lastCell );
        }
    }

    protected void visitCell( XWPFTableCell cell, T tableContainer, boolean firstRow, boolean lastRow,
                              boolean firstCell, boolean lastCell )
        throws Exception
    {
        T tableCellContainer = startVisitTableCell( cell, tableContainer, firstRow, lastRow, firstCell, lastCell );
        visitTableCellBody( cell, tableCellContainer );
        endVisitTableCell( cell, tableContainer, tableCellContainer );
    }

    protected void visitTableCellBody( XWPFTableCell cell, T tableCellContainer )
        throws Exception
    {
        List<IBodyElement> bodyElements = cell.getBodyElements();
        visitBodyElements( bodyElements, tableCellContainer );
    }

    protected abstract T startVisitTableCell( XWPFTableCell cell, T tableContainer, boolean firstRow, boolean lastRow,
                                              boolean firstCell, boolean lastCell );

    protected abstract void endVisitTableCell( XWPFTableCell cell, T tableContainer, T tableCellContainer );

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
                        visitPicture( (CTPicture) o, parentContainer );
                    }
                }
                c.dispose();
            }
        }
    }

    protected void visitInline( CTInline inline, T parentContainer )
        throws Exception
    {
        CTGraphicalObject graphic = inline.getGraphic();
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
                        visitPicture( (CTPicture) o, parentContainer );
                    }
                }
                c.dispose();
            }
        }
    }

    protected abstract void visitPicture( CTPicture picture, T parentContainer )
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
