package org.apache.poi.xwpf.converter.xhtml.internal;

import org.apache.poi.xwpf.converter.core.IXWPFMasterPage;
import org.apache.poi.xwpf.converter.core.XWPFDocumentVisitor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class XHTMLMapper
    extends XWPFDocumentVisitor<Object, XHTMLOptions, XHTMLMasterPage>
{

    private final ContentHandler contentHandler;

    public XHTMLMapper( XWPFDocument document, ContentHandler contentHandler, XHTMLOptions options )
        throws Exception
    {
        super( document, options != null ? options : XHTMLOptions.create() );
        this.contentHandler = contentHandler;
    }

    @Override
    protected Object startVisitDocument()
        throws Exception
    {
        contentHandler.startDocument();
        if ( !options.isFragment() )
        {
            startElement( "html" );
            startElement( "body" );
        }
        return null;
    }

    @Override
    protected void endVisitDocument()
        throws Exception
    {
        if ( !options.isFragment() )
        {
            endElement( "body" );
            endElement( "html" );
        }
        contentHandler.endDocument();
    }

    @Override
    protected Object startVisitParagraph( XWPFParagraph paragraph, Object parentContainer )
        throws Exception
    {
        startElement( "p" );
        return null;
    }

    @Override
    protected void endVisitParagraph( XWPFParagraph paragraph, Object parentContainer, Object paragraphContainer )
        throws Exception
    {
        endElement( "p" );
    }

    @Override
    protected void visitEmptyRun( Object paragraphContainer )
        throws Exception
    {
        startElement( "br" );
    }

    @Override
    protected void visitText( CTText ctText, Object paragraphContainer )
        throws Exception
    {
        characters( ctText.getStringValue() );
    }

    @Override
    protected void visitTab( CTPTab o, Object paragraphContainer )
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void visitBR( CTBr o, Object paragraphContainer )
        throws Exception
    {
        startElement( "br" );
    }

    @Override
    protected void pageBreak()
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected Object startVisitTable( XWPFTable table, float[] colWidths, Object tableContainer )
        throws Exception
    {
        startElement( "table" );
        return null;
    }

    @Override
    protected void endVisitTable( XWPFTable table, Object parentContainer, Object tableContainer )
        throws Exception
    {
        endElement( "table" );
    }

    @Override
    protected void startVisitTableRow( XWPFTableRow row, Object tableContainer, boolean firstRow, boolean lastRow )
        throws Exception
    {
        startElement( "tr" );
    }

    @Override
    protected void endVisitTableRow( XWPFTableRow row, Object tableContainer, boolean firstRow, boolean lastRow )
        throws Exception
    {
        endElement( "tr" );
    }

    @Override
    protected Object startVisitTableCell( XWPFTableCell cell, Object tableContainer, boolean firstRow, boolean lastRow,
                                          boolean firstCell, boolean lastCell )
        throws Exception
    {
        startElement( "td" );
        return null;
    }

    @Override
    protected void endVisitTableCell( XWPFTableCell cell, Object tableContainer, Object tableCellContainer )
        throws Exception
    {
        endElement( "td" );
    }

    @Override
    protected void visitHeader( XWPFHeader header, CTHdrFtrRef headerRef, CTSectPr sectPr, XHTMLMasterPage masterPage )
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void visitFooter( XWPFFooter footer, CTHdrFtrRef footerRef, CTSectPr sectPr, XHTMLMasterPage masterPage )
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void visitPicture( CTPicture picture, Object parentContainer )
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setActiveMasterPage( XHTMLMasterPage masterPage )
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected IXWPFMasterPage createMasterPage( CTSectPr sectPr )
    {
        return new XHTMLMasterPage( sectPr );
    }

    private void startElement( String name )
        throws SAXException
    {
        contentHandler.startElement( "", name, name, EmptyAttributes.INSTANCE );
    }

    private void endElement( String name )
        throws SAXException
    {
        contentHandler.endElement( "", name, name );
    }

    private void characters( String content )
        throws SAXException
    {
        char[] chars = content.toCharArray();
        contentHandler.characters( chars, 0, chars.length );

    }
}
