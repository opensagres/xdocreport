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
package org.apache.poi.xwpf.converter.internal;

import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;

public abstract class XWPFElementVisitor<T>
{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( XWPFElementVisitor.class.getName() );

    protected final XWPFDocument document;

    protected CTDocDefaults defaults;

    public XWPFElementVisitor( XWPFDocument document )
    {
        this.document = document;
        try
        {
            this.defaults = document.getStyle().getDocDefaults();
        }
        catch ( Exception e )
        {
            // this.sectPrStack = new Stack<XWPFParagraph>();
            LOGGER.severe( e.getMessage() );
        }
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

        // Create Header/Footer
        // CTSectPr sectPr = document.getDocument().getBody().getSectPr();
        // visitHeadersFooters( sectPr, container );

        // Create IText element for each XWPF elements from the w:body
        List<IBodyElement> bodyElements = document.getBodyElements();
        visitBodyElements( bodyElements, container );

        // Save
        // Clean-up

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
        //if ( paragraph.getRuns().size() > 0 )
        //{
            T paragraphContainer = startVisitPargraph( paragraph, container );
            visitParagraphBody( paragraph, paragraphContainer );
            endVisitPargraph( paragraph, container, paragraphContainer );
        //}
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

}