/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument;

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
        catch ( XmlException e )
        {
            LOGGER.severe( e.getMessage() );
        }
        catch ( IOException e )
        {
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
        CTSectPr sectPr = document.getDocument().getBody().getSectPr();
        visitHeadersFooters( sectPr, container );

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

    // ------------------------------ Header/Footer visitor -----------

    protected void visitHeadersFooters( CTSectPr sectPr, T container )
        throws Exception
    {
        Collection<CTHdrFtrRef> headersRef = sectPr.getHeaderReferenceList();
        Collection<CTHdrFtrRef> footersRef = sectPr.getFooterReferenceList();

        for ( CTHdrFtrRef headerRef : headersRef )
        {
            visitHeader( headerRef );
        }

        for ( CTHdrFtrRef footerRef : footersRef )
        {
            visitFooter( footerRef );
        }
    }

    protected XWPFHeader getXWPFHeader( CTHdrFtrRef headerRef )
        throws XmlException, IOException
    {
        PackagePart hdrPart = document.getPartById( headerRef.getId() );
        HdrDocument hdrDoc = HdrDocument.Factory.parse( hdrPart.getInputStream() );
        CTHdrFtr hdrFtr = hdrDoc.getHdr();

        XWPFHeader hdr = new XWPFHeader( document, hdrFtr);

        return hdr;
    }

    protected XWPFFooter getXWPFFooter( CTHdrFtrRef footerRef )
        throws XmlException, IOException
    {
        PackagePart hdrPart = document.getPartById( footerRef.getId() );
        FtrDocument hdrDoc = FtrDocument.Factory.parse( hdrPart.getInputStream() );
        CTHdrFtr hdrFtr = hdrDoc.getFtr();

        XWPFFooter ftr = new XWPFFooter(document, hdrFtr);

        return ftr;
    }

    protected abstract void visitHeader( CTHdrFtrRef headerRef )
        throws Exception;

    protected abstract void visitFooter( CTHdrFtrRef footerRef )
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
        List<XWPFTableRow> rows = table.getRows();
        for ( XWPFTableRow row : rows )
        {
            visitTableRow( row, tableContainer );
        }
    }

    protected abstract T startVisitTable( XWPFTable table, T tableContainer )
        throws Exception;

    protected abstract void endVisitTable( XWPFTable table, T parentContainer, T tableContainer )
        throws Exception;

    protected void visitTableRow( XWPFTableRow row, T tableContainer )
        throws Exception
    {
        // Process cell
        List<XWPFTableCell> cells = row.getTableCells();
        for ( XWPFTableCell cell : cells )
        {
            visitCell( cell, tableContainer );
        }
    }

    protected void visitCell( XWPFTableCell cell, T tableContainer )
        throws Exception
    {
        T tableCellContainer = startVisitTableCell( cell, tableContainer );
        visitTableCellBody( cell, tableCellContainer );
        endVisitTableCell( cell, tableContainer, tableCellContainer );
    }

    protected void visitTableCellBody( XWPFTableCell cell, T tableCellContainer )
        throws Exception
    {
        List<IBodyElement> bodyElements = cell.getBodyElements();
        visitBodyElements( bodyElements, tableCellContainer );
    }

    protected abstract T startVisitTableCell( XWPFTableCell cell, T tableContainer );

    protected abstract void endVisitTableCell( XWPFTableCell cell, T tableContainer, T tableCellContainer );

    protected void visitPictures( XWPFRun run, T parentContainer )
        throws Exception
    {
        List<XWPFPicture> embeddedPictures = run.getEmbeddedPictures();
        for ( XWPFPicture picture : embeddedPictures )
        {
            visitPicture( picture, parentContainer );
        }
    }

    protected abstract void visitPicture( XWPFPicture picture, T parentContainer )
        throws Exception;

    protected XWPFStyle getXWPFStyle( String styleID )
    {
        if ( styleID == null )
            return null;
        else
            return document.getStyles().getStyle( styleID );
    }

}