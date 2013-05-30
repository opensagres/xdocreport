/**
 * Copyright (C) 2011-2012 The XDocReport Team <xdocreport@googlegroups.com>
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
package org.apache.poi.xwpf.converter.xhtml.internal;

import static org.apache.poi.xwpf.converter.core.utils.DxaUtil.emu2points;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.A_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.BODY_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.BR_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.CLASS_ATTR;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.COLSPAN_ATTR;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.DIV_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.HEAD_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.HREF_ATTR;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.HTML_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.ID_ATTR;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.IMG_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.P_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.ROWSPAN_ATTR;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.SPAN_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.SRC_ATTR;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.STYLE_ATTR;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TABLE_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TD_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TH_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TR_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.HEIGHT;
import static org.apache.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_BOTTOM;
import static org.apache.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_LEFT;
import static org.apache.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_RIGHT;
import static org.apache.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_TOP;
import static org.apache.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.WIDTH;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.poi.xwpf.converter.core.IURIResolver;
import org.apache.poi.xwpf.converter.core.IXWPFMasterPage;
import org.apache.poi.xwpf.converter.core.ListItemContext;
import org.apache.poi.xwpf.converter.core.XWPFDocumentVisitor;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.converter.xhtml.internal.styles.CSSStyle;
import org.apache.poi.xwpf.converter.xhtml.internal.styles.CSSStylesDocument;
import org.apache.poi.xwpf.converter.xhtml.internal.utils.SAXHelper;
import org.apache.poi.xwpf.converter.xhtml.internal.utils.StringEscapeUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromH.Enum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XHTMLMapper
    extends XWPFDocumentVisitor<Object, XHTMLOptions, XHTMLMasterPage>
{

    private static final String WORD_MEDIA = "word/media/";

    private final ContentHandler contentHandler;

    private boolean generateStyles = true;

    private final IURIResolver resolver;

    private AttributesImpl currentRunAttributes;

    private boolean pageDiv;

    public XHTMLMapper( XWPFDocument document, ContentHandler contentHandler, XHTMLOptions options )
        throws Exception
    {
        super( document, options != null ? options : XHTMLOptions.getDefault() );
        this.contentHandler = contentHandler;
        this.resolver = getOptions().getURIResolver();
        this.pageDiv = false;
    }

    @Override
    protected XWPFStylesDocument createStylesDocument( XWPFDocument document )
        throws XmlException, IOException
    {
        return new CSSStylesDocument( document, options.isIgnoreStylesIfUnused(), options.getIndent() );
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
        if ( pageDiv )
        {
            endElement( DIV_ELEMENT );
        }
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
    protected Object startVisitParagraph( XWPFParagraph paragraph, ListItemContext itemContext, Object parentContainer )
        throws Exception
    {     
        // 1) create attributes

        // 1.1) Create "class" attributes.
        AttributesImpl attributes = createClassAttribute( paragraph.getStyleID() );

        // 1.2) Create "style" attributes.
        CTPPr pPr = paragraph.getCTP().getPPr();
        CSSStyle cssStyle = getStylesDocument().createCSSStyle( pPr );
        attributes = createStyleAttribute( cssStyle, attributes );

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
    protected void visitRun( XWPFRun run, boolean pageNumber, String url, Object paragraphContainer )
        throws Exception
    {

        XWPFParagraph paragraph = run.getParagraph();
        // 1) create attributes

        // 1.1) Create "class" attributes.
        this.currentRunAttributes = createClassAttribute( paragraph.getStyleID() );

        // 1.2) Create "style" attributes.
        CTRPr rPr = run.getCTR().getRPr();
        CSSStyle cssStyle = getStylesDocument().createCSSStyle( rPr );
        this.currentRunAttributes = createStyleAttribute( cssStyle, currentRunAttributes );

        if ( url != null )
        {
            // url is not null, generate a HTML a.
            AttributesImpl hyperlinkAttributes = new AttributesImpl();
            SAXHelper.addAttrValue( hyperlinkAttributes, HREF_ATTR, url );
            startElement( A_ELEMENT, hyperlinkAttributes );
        }

        super.visitRun( run, pageNumber, url, paragraphContainer );

        if ( url != null )
        {
            // url is not null, close the HTML a.
            // TODO : for the moment generate space to be ensure that a has some content.
            characters( " " );
            endElement( A_ELEMENT );
        }
        this.currentRunAttributes = null;
    }

    @Override
    protected void visitEmptyRun( Object paragraphContainer )
        throws Exception
    {
        startElement( BR_ELEMENT );
        endElement( BR_ELEMENT );
    }

    @Override
    protected void visitText( CTText ctText, boolean pageNumber, Object paragraphContainer )
        throws Exception
    {
        if ( currentRunAttributes != null )
        {
            startElement( SPAN_ELEMENT, currentRunAttributes );
        }
        String text = ctText.getStringValue();
        if ( StringUtils.isNotEmpty( text ) )
        {
            // Escape with HTML characters
            characters( StringEscapeUtils.escapeHtml( text ) );
        }
        // else
        // {
        // characters( SPACE_ENTITY );
        // }
        if ( currentRunAttributes != null )
        {
            endElement( SPAN_ELEMENT );
        }
    }

    @Override
    protected void visitTab( CTPTab o, Object paragraphContainer )
        throws Exception
    {
    }

    @Override
    protected void visitTabs( CTTabs tabs, Object paragraphContainer )
        throws Exception
    {
    }

    @Override
    protected void addNewLine( CTBr br, Object paragraphContainer )
        throws Exception
    {
        startElement( BR_ELEMENT );
        endElement( BR_ELEMENT );
    }

    @Override
    protected void pageBreak()
        throws Exception
    {
    }

    @Override
    protected void visitBookmark( CTBookmark bookmark, XWPFParagraph paragraph, Object paragraphContainer )
        throws Exception
    {
        AttributesImpl attributes = new AttributesImpl();
        SAXHelper.addAttrValue( attributes, ID_ATTR, bookmark.getName() );
        startElement( SPAN_ELEMENT, attributes );
        endElement( SPAN_ELEMENT );
    }

    @Override
    protected Object startVisitTable( XWPFTable table, float[] colWidths, Object tableContainer )
        throws Exception
    {
        // 1) create attributes
        // 1.1) Create class attributes.
        AttributesImpl attributes = createClassAttribute( table.getStyleID() );

        // 1.2) Create "style" attributes.
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CSSStyle cssStyle = getStylesDocument().createCSSStyle( tblPr );
        attributes = createStyleAttribute( cssStyle, attributes );

        // 2) create element
        startElement( TABLE_ELEMENT, attributes );
        return null;
    }

    @Override
    protected void endVisitTable( XWPFTable table, Object parentContainer, Object tableContainer )
        throws Exception
    {
        endElement( TABLE_ELEMENT );
    }

    @Override
    protected void startVisitTableRow( XWPFTableRow row, Object tableContainer, int rowIndex, boolean headerRow )
        throws Exception
    {

        // 1) create attributes
        // Create class attributes.
        XWPFTable table = row.getTable();
        AttributesImpl attributes = createClassAttribute( table.getStyleID() );

        // 2) create element
        if ( headerRow )
        {
            startElement( TH_ELEMENT, attributes );
        }
        else
        {
            startElement( TR_ELEMENT, attributes );
        }
    }

    @Override
    protected void endVisitTableRow( XWPFTableRow row, Object tableContainer, boolean firstRow, boolean lastRow,
                                     boolean headerRow )
        throws Exception
    {
        if ( headerRow )
        {
            endElement( TH_ELEMENT );
        }
        else
        {
            endElement( TR_ELEMENT );
        }
    }

    @Override
    protected Object startVisitTableCell( XWPFTableCell cell, Object tableContainer, boolean firstRow, boolean lastRow,
                                          boolean firstCell, boolean lastCell, List<XWPFTableCell> vMergeCells )
        throws Exception
    {
        // 1) create attributes
        // 1.1) Create class attributes.
        XWPFTableRow row = cell.getTableRow();
        XWPFTable table = row.getTable();
        AttributesImpl attributes = createClassAttribute( table.getStyleID() );

        // 1.2) Create "style" attributes.
        CTTcPr tcPr = cell.getCTTc().getTcPr();
        CSSStyle cssStyle = getStylesDocument().createCSSStyle( tcPr );
        attributes = createStyleAttribute( cssStyle, attributes );

        // colspan attribute
        BigInteger gridSpan = stylesDocument.getTableCellGridSpan( cell );
        if ( gridSpan != null )
        {
            attributes = SAXHelper.addAttrValue( attributes, COLSPAN_ATTR, gridSpan.intValue() );
        }

        if ( vMergeCells != null )
        {
            attributes = SAXHelper.addAttrValue( attributes, ROWSPAN_ATTR, vMergeCells.size() );
        }

        // 2) create element
        startElement( TD_ELEMENT, attributes );

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
    protected void visitPicture( CTPicture picture,
                                 Float offsetX,
                                 Enum relativeFromH,
                                 Float offsetY,
                                 org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.Enum relativeFromV,
                                 org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapText.Enum wrapText,
                                 Object parentContainer )
        throws Exception
    {
     

        AttributesImpl attributes = null;
        // Src attribute
        XWPFPictureData pictureData = super.getPictureData( picture );
        if ( pictureData != null )
        {
            // img/@src
            String src = pictureData.getFileName();
            if ( StringUtils.isNotEmpty( src ) )
            {
                src = resolver.resolve( WORD_MEDIA + src );
                attributes = SAXHelper.addAttrValue( attributes, SRC_ATTR, src );
            }

            CTPositiveSize2D ext = picture.getSpPr().getXfrm().getExt();

            // img/@width
            float width = emu2points( ext.getCx() );
            attributes = SAXHelper.addAttrValue( attributes, WIDTH, getStylesDocument().getValueAsPoint( width ) );

            // img/@height
            float height = emu2points( ext.getCy() );
            attributes = SAXHelper.addAttrValue( attributes, HEIGHT, getStylesDocument().getValueAsPoint( height ) );
        }
        if ( attributes != null )
        {
            startElement( IMG_ELEMENT, attributes );
        }
    }

    @Override
    protected void setActiveMasterPage( XHTMLMasterPage masterPage )
    {
        if ( pageDiv )
        {
            try
            {
                endElement( DIV_ELEMENT );
            }
            catch ( SAXException e )
            {
                e.printStackTrace();
            }
        }
        AttributesImpl attributes = new AttributesImpl();
        CSSStyle style = new CSSStyle( DIV_ELEMENT, null );
        CTSectPr sectPr = masterPage.getSectPr();
        CTPageSz pageSize = sectPr.getPgSz();
        if ( pageSize != null )
        {
            // Width
            BigInteger width = pageSize.getW();
            if ( width != null )
            {
                style.addProperty( WIDTH, getStylesDocument().getValueAsPoint( DxaUtil.dxa2points( width ) ) );
            }
        }

        CTPageMar pageMargin = sectPr.getPgMar();
        if ( pageMargin != null )
        {
            // margin bottom
            BigInteger marginBottom = pageMargin.getBottom();
            if ( marginBottom != null )
            {
                float marginBottomPt = DxaUtil.dxa2points( marginBottom );
                style.addProperty( MARGIN_BOTTOM, getStylesDocument().getValueAsPoint( marginBottomPt ) );
            }
            // margin top
            BigInteger marginTop = pageMargin.getTop();
            if ( marginTop != null )
            {
                float marginTopPt = DxaUtil.dxa2points( marginTop );
                style.addProperty( MARGIN_TOP, getStylesDocument().getValueAsPoint( marginTopPt ) );
            }
            // margin left
            BigInteger marginLeft = pageMargin.getLeft();
            if ( marginLeft != null )
            {
                float marginLeftPt = DxaUtil.dxa2points( marginLeft );
                style.addProperty( MARGIN_LEFT, getStylesDocument().getValueAsPoint( marginLeftPt ) );
            }
            // margin right
            BigInteger marginRight = pageMargin.getRight();
            if ( marginRight != null )
            {
                float marginRightPt = DxaUtil.dxa2points( marginRight );
                style.addProperty( MARGIN_RIGHT, getStylesDocument().getValueAsPoint( marginRightPt ) );
            }
        }
        String s = style.getInlineStyles();
        if ( StringUtils.isNotEmpty( s ) )
        {
            SAXHelper.addAttrValue( attributes, STYLE_ATTR, s );
        }
        try
        {
            startElement( DIV_ELEMENT, attributes );
        }
        catch ( SAXException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        pageDiv = true;

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

    private AttributesImpl createClassAttribute( String styleID )
    {
        String classNames = getStylesDocument().getClassNames( styleID );
        if ( StringUtils.isNotEmpty( classNames ) )
        {
            return SAXHelper.addAttrValue( null, CLASS_ATTR, classNames );
        }
        return null;
    }

    private AttributesImpl createStyleAttribute( CSSStyle cssStyle, AttributesImpl attributes )
    {
        if ( cssStyle != null )
        {
            String inlineStyles = cssStyle.getInlineStyles();
            if ( StringUtils.isNotEmpty( inlineStyles ) )
            {
                attributes = SAXHelper.addAttrValue( attributes, STYLE_ATTR, inlineStyles );
            }
        }
        return attributes;
    }
}
