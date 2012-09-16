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
package org.apache.poi.xwpf.converter.internal.xhtml;

import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.getRStyle;

import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xwpf.converter.IURIResolver;
import org.apache.poi.xwpf.converter.internal.XWPFDocumentVisitor;
import org.apache.poi.xwpf.converter.internal.itext.StyleEngineForIText;
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
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import fr.opensagres.xdocreport.utils.StringEscapeUtils;
import fr.opensagres.xdocreport.utils.StringUtils;
import fr.opensagres.xdocreport.xhtml.extension.CSSStylePropertyConstants;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLConstants;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLPageContentBuffer;

public class XHTMLMapper
    extends XWPFDocumentVisitor<XHTMLPageContentBuffer, XHTMLMasterPage>
    implements XHTMLConstants, CSSStylePropertyConstants
{

    private static final String WORD_MEDIA = "word/media/";

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( StyleEngineForIText.class.getName() );

    private POIXHTMLPage xhtml = null;

    private final IURIResolver resolver;

    private final int indent;

    private StyleEngineForXHTML styleEngine;

    protected OutputStream out;

    public XHTMLMapper( XWPFDocument document, int indent, IURIResolver resolver )
        throws Exception
    {
        super( document );
        this.resolver = resolver;
        this.indent = indent;
        styleEngine = new StyleEngineForXHTML( document, false, indent, resolver );
    }

    @Override
    protected XHTMLPageContentBuffer startVisitDocument( OutputStream out )
        throws Exception
    {
        xhtml = new POIXHTMLPage( styleEngine, indent );
        this.out = out;
        xhtml.getPageBodyContentBody().startElementNotEnclosed( DIV_ELEMENT );

        // HTML style
        StringBuilder htmlStyle = XHTMLStyleUtil.getStyle( document, defaults );
        setAttributStyleIfNeeded( xhtml.getPageBodyContentBody(), htmlStyle );

        xhtml.getPageBodyContentBody().endElementNotEnclosed();
        return xhtml.getPageBodyContentBody();
    }

    @Override
    protected void endVisitDocument()
        throws Exception
    {
        xhtml.getPageBodyContentBody().endElement( DIV_ELEMENT );
        /*
         * Writer writer = xhtml.getWriter(); if (writer != null) { xhtml.save(writer); } else
         */
        {
            // OutputStream out = xhtml.getOutputStream();
            xhtml.save( out );
        }
    }

    protected XHTMLPageContentBuffer startVisitPargraph( XWPFParagraph paragraph, XHTMLPageContentBuffer parentContainer )
        throws Exception
    {
        styleEngine.startVisitPargraph( paragraph, null );
        parentContainer.startElementNotEnclosed( P_ELEMENT );
        if ( paragraph.getStyleID() != null )
        {
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( "StyleID " + paragraph.getStyleID() );
            }

            parentContainer.setAttribute( CLASS_ATTR, paragraph.getStyleID() );
        }

        // HTML style
        StringBuilder htmlStyle =
            XHTMLStyleUtil.getStyle( paragraph, super.getXWPFStyle( paragraph.getStyleID() ), defaults );

        setAttributStyleIfNeeded( parentContainer, htmlStyle );

        parentContainer.endElementNotEnclosed();
        return parentContainer;
    }

    @Override
    protected void endVisitPargraph( XWPFParagraph paragraph, XHTMLPageContentBuffer parentContainer,
                                     XHTMLPageContentBuffer paragraphContainer )
        throws Exception
    {
        paragraphContainer.endElement( P_ELEMENT );
    }

    @Override
    protected void visitEmptyRun( XHTMLPageContentBuffer paragraphContainer )
        throws Exception
    {
        paragraphContainer.startEndElement( BR_ELEMENT );
    }

    @Override
    protected void visitRun( XWPFRun run, XHTMLPageContentBuffer paragraphContainer )
        throws Exception
    {
        CTR ctr = run.getCTR();

        // HTML style

        CTString rStyle = getRStyle( run );
        XWPFStyle runStyle = super.getXWPFStyle( rStyle != null ? rStyle.getVal() : null );

        StringBuilder htmlStyle =
            XHTMLStyleUtil.getStyle( run, runStyle, super.getXWPFStyle( run.getParagraph().getStyle() ), defaults );

        // Grab the text and tabs of the text run
        // Do so in a way that preserves the ordering
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
                if ( !"w:instrText".equals( tagName ) )
                {
                    paragraphContainer.startElementNotEnclosed( SPAN_ELEMENT );
                    setAttributStyleIfNeeded( paragraphContainer, htmlStyle );
                    paragraphContainer.endElementNotEnclosed();
                    // Set the text by escaping it with HTML.
                    paragraphContainer.setText( StringEscapeUtils.escapeHtml( ctText.getStringValue() ) );
                    paragraphContainer.endElement( SPAN_ELEMENT );

                }
            }
            else if ( o instanceof CTPTab )
            {
                visitTab( paragraphContainer, (CTPTab) o );
            }
            else if ( o instanceof CTBr )
            {
                visitBR( paragraphContainer, (CTBr) o );
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
                    visitTab( paragraphContainer, null );
                }
                if ( "w:br".equals( tagName ) )
                {
                    visitBR( paragraphContainer, null );
                }
                if ( "w:cr".equals( tagName ) )
                {
                    visitBR( paragraphContainer, null );
                }
            }
            else if ( o instanceof CTDrawing )
            {
                visitDrawing( (CTDrawing) o, paragraphContainer );
            }
        }
        c.dispose();

        // super.visitPictures( run, paragraphContainer );
    }

    private void visitTab( XHTMLPageContentBuffer paragraphContainer, CTPTab o )
    {
        // TODO Auto-generated method stub

    }

    private void visitBR( XHTMLPageContentBuffer paragraphContainer, CTBr br )
    {
        paragraphContainer.startEndElement( BR_ELEMENT );
    }

    @Override
    protected XHTMLPageContentBuffer startVisitTable( XWPFTable table, XHTMLPageContentBuffer tableContainer )
        throws Exception
    {
        tableContainer.startElementNotEnclosed( TABLE_ELEMENT );

        // XWPFStyle tableStyle = super.getStyle(table.getStyleID());
        // HTML style
        // StringBuilder htmlStyle = XHTMLStyleUtil.getStyle(table, tableStyle, defaults);
        // setAttributStyleIfNeeded(xhtml.getPageBodyContentBody(), htmlStyle);

        tableContainer.endElementNotEnclosed();
        return tableContainer;
    }

    @Override
    protected void visitTableRow( XWPFTableRow row, XHTMLPageContentBuffer tableContainer, boolean firstRow,
                                  boolean lastRow )
        throws Exception
    {
        tableContainer.startElementNotEnclosed( TR_ELEMENT );
        tableContainer.endElementNotEnclosed();

        super.visitTableRow( row, tableContainer, firstRow, lastRow );

        tableContainer.endElement( TR_ELEMENT );
    }

    @Override
    protected XHTMLPageContentBuffer startVisitTableCell( XWPFTableCell tableCell,
                                                          XHTMLPageContentBuffer tableContainer, boolean firstRow,
                                                          boolean lastRow, boolean firstCell, boolean lastCell )
    {
        tableContainer.startElementNotEnclosed( TD_ELEMENT );

        CTTcPr tcPr = tableCell.getCTTc().getTcPr();

        // Colspan
        Integer colspan = null;
        CTDecimalNumber gridSpan = tcPr.getGridSpan();
        if ( gridSpan != null )
        {
            colspan = gridSpan.getVal().intValue();
        }
        if ( colspan != null )
        {
            tableContainer.setAttribute( COLSPAN_ATTR, colspan );
        }
        // HTML style
        StringBuilder htmlStyle = XHTMLStyleUtil.getStyle( tableCell, defaults );
        setAttributStyleIfNeeded( tableContainer, htmlStyle );

        tableContainer.endElementNotEnclosed();
        return tableContainer;
    }

    @Override
    protected void endVisitTableCell( XWPFTableCell cell, XHTMLPageContentBuffer tableContainer,
                                      XHTMLPageContentBuffer tableCellContainer )
    {
        tableContainer.endElement( TD_ELEMENT );
    }

    @Override
    protected void endVisitTable( XWPFTable table, XHTMLPageContentBuffer parentContainer,
                                  XHTMLPageContentBuffer tableContainer )
        throws Exception
    {
        tableContainer.endElement( TABLE_ELEMENT );
    }

    @Override
    protected void visitPicture( CTPicture picture, XHTMLPageContentBuffer parentContainer )
        throws Exception
    {
        parentContainer.startElementNotEnclosed( IMG_ELEMENT );

        String blipId = picture.getBlipFill().getBlip().getEmbed();
        // Src attribute
        XWPFPictureData pictureData = super.getPictureDataByID( blipId );
        if ( pictureData != null )
        {
            String src = pictureData.getFileName();
            if ( StringUtils.isNotEmpty( src ) )
            {
                src = resolver.resolve( WORD_MEDIA + src );
                parentContainer.setAttribute( SRC_ATTR, src );
            }
        }

        StringBuilder htmlStyle = XHTMLStyleUtil.getStyle( picture );
        setAttributStyleIfNeeded( parentContainer, htmlStyle );
        parentContainer.endElementNotEnclosed();
        parentContainer.endElement( IMG_ELEMENT );

    }

    private void setAttributStyleIfNeeded( XHTMLPageContentBuffer buffer, StringBuilder htmlStyle )
    {
        if ( htmlStyle.length() > 0 )
        {
            buffer.setAttribute( STYLE_ATTR, htmlStyle.toString() );
        }
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
    protected void setActiveMasterPage( XHTMLMasterPage masterPage )
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected XHTMLMasterPage createMasterPage( CTSectPr sectPr )
    {
        return new XHTMLMasterPage( sectPr );
    }

}
