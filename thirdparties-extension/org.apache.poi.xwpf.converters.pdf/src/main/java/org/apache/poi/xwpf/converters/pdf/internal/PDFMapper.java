package org.apache.poi.xwpf.converters.pdf.internal;

import java.io.OutputStream;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xwpf.converters.core.IXWPFMasterPage;
import org.apache.poi.xwpf.converters.core.XWPFDocumentVisitor;
import org.apache.poi.xwpf.converters.pdf.PDFViaITextOptions;
import org.apache.poi.xwpf.converters.pdf.internal.elements.StylableMasterPage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

public class PDFMapper
    extends XWPFDocumentVisitor<StylableMasterPage>
{

    private final PDFViaITextOptions options;

    public PDFMapper( XWPFDocument document, PDFViaITextOptions options )
        throws Exception
    {
        super( document );
        this.options = options != null ? options : PDFViaITextOptions.create();
    }

    @Override
    protected T startVisitDocument( OutputStream out )
        throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void endVisitDocument()
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected T startVisitPargraph( XWPFParagraph paragraph, T parentContainer )
        throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void endVisitPargraph( XWPFParagraph paragraph, T parentContainer, T paragraphContainer )
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void visitEmptyRun( T paragraphContainer )
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void visitRun( XWPFRun run, T paragraphContainer )
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected T startVisitTable( XWPFTable table, T tableContainer )
        throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void endVisitTable( XWPFTable table, T parentContainer, T tableContainer )
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected T startVisitTableCell( XWPFTableCell cell, T tableContainer, boolean firstRow, boolean lastRow,
                                     boolean firstCell, boolean lastCell )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void endVisitTableCell( XWPFTableCell cell, T tableContainer, T tableCellContainer )
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void visitHeader( XWPFHeader header, CTHdrFtrRef headerRef, CTSectPr sectPr, StylableMasterPage masterPage )
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void visitFooter( XWPFFooter footer, CTHdrFtrRef footerRef, CTSectPr sectPr, StylableMasterPage masterPage )
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void visitPicture( CTPicture picture, T parentContainer )
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void setActiveMasterPage( StylableMasterPage masterPage )
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected IXWPFMasterPage createMasterPage( CTSectPr sectPr )
    {
        return new StylableMasterPage( sectPr );
    }

}
