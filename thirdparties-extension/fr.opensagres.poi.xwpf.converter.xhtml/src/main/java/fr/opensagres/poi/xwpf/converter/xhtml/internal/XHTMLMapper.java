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
package fr.opensagres.poi.xwpf.converter.xhtml.internal;

import static fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil.emu2points;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.*;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.HEIGHT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_BOTTOM;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_LEFT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_RIGHT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_TOP;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.WIDTH;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.poi.xwpf.usermodel.*;
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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalAlignRun;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.poi.xwpf.converter.core.BorderSide;
import fr.opensagres.poi.xwpf.converter.core.Color;
import fr.opensagres.poi.xwpf.converter.core.IURIResolver;
import fr.opensagres.poi.xwpf.converter.core.ListItemContext;
import fr.opensagres.poi.xwpf.converter.core.TableCellBorder;
import fr.opensagres.poi.xwpf.converter.core.XWPFDocumentVisitor;
import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.styles.run.RunFontStyleStrikeValueProvider;
import fr.opensagres.poi.xwpf.converter.core.styles.run.RunTextHighlightingValueProvider;
import fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil;
import fr.opensagres.poi.xwpf.converter.core.utils.StringUtils;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStyle;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylesDocument;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.utils.SAXHelper;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.utils.StringEscapeUtils;

public class XHTMLMapper
    extends XWPFDocumentVisitor<Object, XHTMLOptions, XHTMLMasterPage>
{
	
	/**
	 * There is no HTML representation for tab. So apply 4 spaces by default
	 */
	static final String TAB_CHAR_SEQUENCE = "&nbsp;&nbsp;&nbsp;&nbsp;";

    private static final String WORD_MEDIA = "word/media/";

    private final ContentHandler contentHandler;
    
    /**
     * To hold paragraph reference and to be used while processing individual runs which has tabs
     */
    private XWPFParagraph currentParagraph;

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
    protected Object startVisitSDT(XWPFSDT contents, Object container) throws SAXException {

        startElement(DIV_ELEMENT, null);
//        startElement(PRE_ELEMENT, null);
        return null;
    }

//    @Override
//    protected void visitSDTBody(XWPFSDT contents, Object sdtContainer) throws SAXException {
//        characters(contents.getContent().getText());
//    }

    @Override
    protected void endVisitSDT(XWPFSDT contents, Object container, Object sdtContainer) throws SAXException {
//        endElement(PRE_ELEMENT);
        endElement(DIV_ELEMENT);
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
        if(cssStyle != null) {
            cssStyle.addProperty(CSSStylePropertyConstants.WHITE_SPACE, "pre-wrap");            
        }
        attributes = createStyleAttribute( cssStyle, attributes );

        // 2) create element
        startElement( P_ELEMENT, attributes );
        
        //To handle list items in paragraph
        if(itemContext != null)
        {			
	        startElement( SPAN_ELEMENT, attributes );
	        String text = itemContext.getText();	        
	        if ( StringUtils.isNotEmpty( text ) )
	        {	
	        	text = StringUtils.replaceNonUnicodeChars(text);
	        	text = text + "\u0020";
	        	SAXHelper.characters( contentHandler, StringEscapeUtils.escapeHtml( text ) );
	        }
	        endElement( SPAN_ELEMENT );
		}
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
		if(run.getParent() instanceof XWPFParagraph) {
			this.currentParagraph = (XWPFParagraph) run.getParent();
		}

        XWPFParagraph paragraph = run.getParagraph();
        // 1) create attributes

        // 1.1) Create "class" attributes.
        this.currentRunAttributes = createClassAttribute( paragraph.getStyleID() );

        // 1.2) Create "style" attributes.
        CTRPr rPr = run.getCTR().getRPr();
        CSSStyle cssStyle = getStylesDocument().createCSSStyle( rPr );
        if (cssStyle != null) {
            cssStyle.addProperty(CSSStylePropertyConstants.WHITE_SPACE, "pre-wrap");
        }
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
        this.currentParagraph = null;
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
    protected void visitStyleText(XWPFRun run, String text, Object parent, boolean pageNumber) throws Exception
    {
    	if(run.getFontFamily() == null) {
			run.setFontFamily(getStylesDocument().getFontFamilyAscii(run));
		}
    	
		if(run.getFontSize() <= 0) {
			run.setFontSize(getStylesDocument().getFontSize(run).intValue());
		}
		
		CTRPr rPr = run.getCTR().getRPr();
		
    	// 1) create attributes

        // 1.1) Create "class" attributes.
        AttributesImpl runAttributes = createClassAttribute( currentParagraph.getStyleID() );

        // 1.2) Create "style" attributes.
        CSSStyle cssStyle = getStylesDocument().createCSSStyle( rPr );
        if(cssStyle != null) {
        	Color color = RunTextHighlightingValueProvider.INSTANCE.getValue(rPr, getStylesDocument());
        	if(color != null) cssStyle.addProperty(CSSStylePropertyConstants.BACKGROUND_COLOR, StringUtils.toHexString(color));
        	if(Boolean.TRUE.equals(RunFontStyleStrikeValueProvider.INSTANCE.getValue(rPr, getStylesDocument())) ||
        			rPr.getDstrike() != null)
        		cssStyle.addProperty("text-decoration", "line-through");
        	if(rPr.getVertAlign() != null) {
        		int align = rPr.getVertAlign().getVal().intValue();
        		if(STVerticalAlignRun.INT_SUPERSCRIPT == align) {
        			cssStyle.addProperty("vertical-align", "super");
        		}
        		else if(STVerticalAlignRun.INT_SUBSCRIPT == align) {
        			cssStyle.addProperty("vertical-align", "sub");
        		}
        	}	        		
        }
        runAttributes = createStyleAttribute( cssStyle, runAttributes );
        if ( runAttributes != null )
        {
            startElement( SPAN_ELEMENT, runAttributes );
        }        
        if ( StringUtils.isNotEmpty( text ) )
        {
            // Escape with HTML characters
            characters( StringEscapeUtils.escapeHtml( text ) );
        }
        if ( runAttributes != null )
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
    	//For some reason tabs become null ???
    	//Add equivalent spaces in html render as no tab in html world
    	if(currentParagraph != null && tabs == null)
    	{
			startElement( SPAN_ELEMENT, null );
			characters(TAB_CHAR_SEQUENCE);
			endElement(SPAN_ELEMENT);
			return;
		}
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
        if(cssStyle != null) {
        	cssStyle.addProperty(CSSStylePropertyConstants.BORDER_COLLAPSE, CSSStylePropertyConstants.BORDER_COLLAPSE_COLLAPSE);
        }
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
        //At lease support solid borders for now
        if(cssStyle != null) {
        	TableCellBorder border = getStylesDocument().getTableBorder(table, BorderSide.TOP);
        	if(border != null)
        	{
        		String style = border.getBorderSize() + "px solid " +StringUtils.toHexString(border.getBorderColor()); 
            	cssStyle.addProperty(CSSStylePropertyConstants.BORDER_TOP, style);
        	}        	
        	
        	border = getStylesDocument().getTableBorder(table, BorderSide.BOTTOM);
        	if(border != null)
        	{
        		String style = border.getBorderSize() + "px solid " + StringUtils.toHexString(border.getBorderColor());         	
            	cssStyle.addProperty(CSSStylePropertyConstants.BORDER_BOTTOM, style);
        	}        	
        	
        	border = getStylesDocument().getTableBorder(table, BorderSide.LEFT);
        	if(border != null)
        	{
        		String style = border.getBorderSize() + "px solid " + StringUtils.toHexString(border.getBorderColor());
            	cssStyle.addProperty(CSSStylePropertyConstants.BORDER_LEFT, style);
        	}        	
        	
        	border = getStylesDocument().getTableBorder(table, BorderSide.RIGHT);
        	if(border != null)
        	{
        		String style = border.getBorderSize() + "px solid " + StringUtils.toHexString(border.getBorderColor());
            	cssStyle.addProperty(CSSStylePropertyConstants.BORDER_RIGHT, style);
        	}        	
        }
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
            CSSStyle style = new CSSStyle( IMG_ELEMENT, null );
            // img/@width
            float width = emu2points( ext.getCx() );
            // img/@height
            float height = emu2points( ext.getCy() );
            style.addProperty(WIDTH, getStylesDocument().getValueAsPoint( width ) );
            style.addProperty(HEIGHT, getStylesDocument().getValueAsPoint( height ) );
            attributes = SAXHelper.addAttrValue( attributes, STYLE_ATTR, style.getInlineStyles() );
        }
        else 
        {    
        	// external link images inserted
        	String link = picture.getBlipFill().getBlip().getLink();
            String src = document.getPackagePart().getRelationships().getRelationshipByID(link).getTargetURI().toString();
        	attributes = SAXHelper.addAttrValue( null, SRC_ATTR, src );
        	
        	CTPositiveSize2D ext = picture.getSpPr().getXfrm().getExt();
        	CSSStyle style = new CSSStyle( IMG_ELEMENT, null );
            // img/@width
            float width = emu2points( ext.getCx() );
            // img/@height
            float height = emu2points( ext.getCy() );
            style.addProperty(WIDTH, getStylesDocument().getValueAsPoint( width ) );
            style.addProperty(HEIGHT, getStylesDocument().getValueAsPoint( height ) );
            attributes = SAXHelper.addAttrValue( attributes, STYLE_ATTR, style.getInlineStyles() );
        }
        if ( attributes != null )
        {
            startElement( IMG_ELEMENT, attributes );
            endElement( IMG_ELEMENT );
        }
    }

    @Override
    protected void visitVmlPicture(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture picture,
                                   Object pdfParentContainer) throws Exception {
       //TODO: Add VML support if requited for XHTML
    }

    public void setActiveMasterPage( XHTMLMasterPage masterPage )
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

    public XHTMLMasterPage createMasterPage( CTSectPr sectPr )
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
