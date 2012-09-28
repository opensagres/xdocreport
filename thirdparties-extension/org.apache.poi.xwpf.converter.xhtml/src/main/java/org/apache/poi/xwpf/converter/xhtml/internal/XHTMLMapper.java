package org.apache.poi.xwpf.converter.xhtml.internal;

import static org.apache.poi.xwpf.converter.xhtml.XHTMLConstants.BODY_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.XHTMLConstants.HEAD_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.XHTMLConstants.HTML_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.XHTMLConstants.P_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.XHTMLConstants.TABLE_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.XHTMLConstants.TR_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.XHTMLConstants.TD_ELEMENT;

import static org.apache.poi.xwpf.converter.xhtml.XHTMLConstants.CLASS_ATTR;

import java.io.IOException;

import org.apache.poi.xwpf.converter.core.IXWPFMasterPage;
import org.apache.poi.xwpf.converter.core.XWPFDocumentVisitor;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.converter.xhtml.internal.styles.CSSStylesDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XHTMLMapper
    extends XWPFDocumentVisitor<Object, XHTMLOptions, XHTMLMasterPage>
{

    private final ContentHandler contentHandler;

    private boolean generateStyles = true;

    public XHTMLMapper( XWPFDocument document, ContentHandler contentHandler, XHTMLOptions options )
        throws Exception
    {
        super( document, options != null ? options : XHTMLOptions.create() );
        this.contentHandler = contentHandler;
    }

    @Override
    protected XWPFStylesDocument createStylesDocument( XWPFDocument document )
        throws XmlException, IOException
    {
        return new CSSStylesDocument( document );
    }

    @Override
    protected Object startVisitDocument()
        throws Exception
    {
        if ( !options.isFragment() )
        {
            contentHandler.startDocument();
            // html start
            startElement( HTML_ELEMENT );
            // head start
            startElement( HEAD_ELEMENT );
            if ( generateStyles )
            {
                // styles
                ( (CSSStylesDocument) stylesDocument ).save( contentHandler );
            }
            // html end
            endElement( HEAD_ELEMENT );
            // body start
            startElement( BODY_ELEMENT );
        }
        return null;
    }

    @Override
    protected void endVisitDocument()
        throws Exception
    {
        if ( !options.isFragment() )
        {
            // body end
            endElement( BODY_ELEMENT );
            // html end
            endElement( HTML_ELEMENT );
            contentHandler.endDocument();
        }
    }

    @Override
    protected Object startVisitParagraph( XWPFParagraph paragraph, Object parentContainer )
        throws Exception
    {
        AttributesImpl attributes = null;
        // 1) create attributes
        // Create class attributes.
        String classNames = getStylesDocument().getClassNames( paragraph.getStyleID() );
        if ( StringUtils.isNotEmpty( classNames ) )
        {
            attributes = SAXHelper.addAttrValue( attributes, CLASS_ATTR, classNames );
        }

        // 2) create element
        startElement( P_ELEMENT, attributes );
        return null;
    }

    @Override
    protected void endVisitParagraph( XWPFParagraph paragraph, Object parentContainer, Object paragraphContainer )
        throws Exception
    {
        endElement( P_ELEMENT );
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
        startElement( TABLE_ELEMENT );
        return null;
    }

    @Override
    protected void endVisitTable( XWPFTable table, Object parentContainer, Object tableContainer )
        throws Exception
    {
        endElement( TABLE_ELEMENT );
    }

    @Override
    protected void startVisitTableRow( XWPFTableRow row, Object tableContainer, boolean firstRow, boolean lastRow )
        throws Exception
    {
        startElement( TR_ELEMENT );
    }

    @Override
    protected void endVisitTableRow( XWPFTableRow row, Object tableContainer, boolean firstRow, boolean lastRow )
        throws Exception
    {
        endElement( TR_ELEMENT );
    }

    @Override
    protected Object startVisitTableCell( XWPFTableCell cell, Object tableContainer, boolean firstRow, boolean lastRow,
                                          boolean firstCell, boolean lastCell )
        throws Exception
    {
        startElement( TD_ELEMENT );
        return null;
    }

    @Override
    protected void endVisitTableCell( XWPFTableCell cell, Object tableContainer, Object tableCellContainer )
        throws Exception
    {
        endElement( TD_ELEMENT );
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
        startElement( name, null );
    }

    private void startElement( String name, Attributes attributes )
        throws SAXException
    {
        SAXHelper.startElement( contentHandler, name, attributes );
    }

    private void endElement( String name )
        throws SAXException
    {
        SAXHelper.endElement( contentHandler, name );
    }

    private void characters( String content )
        throws SAXException
    {
        SAXHelper.characters( contentHandler, content );
    }

    @Override
    public CSSStylesDocument getStylesDocument()
    {
        return (CSSStylesDocument) super.getStylesDocument();
    }

}
