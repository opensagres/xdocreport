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
package org.apache.poi.xwpf.converter.internal.xhtml;

import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.getRStyle;

import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xwpf.converter.IURIResolver;
import org.apache.poi.xwpf.converter.internal.XWPFElementVisitor;
import org.apache.poi.xwpf.converter.internal.itext.StyleEngineForIText;
import org.apache.poi.xwpf.converter.internal.itext.XWPFPictureUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import fr.opensagres.xdocreport.utils.StringEscapeUtils;
import fr.opensagres.xdocreport.utils.StringUtils;
import fr.opensagres.xdocreport.xhtml.extension.CSSStylePropertyConstants;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLConstants;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLPageContentBuffer;

public class XHTMLMapper
    extends XWPFElementVisitor<XHTMLPageContentBuffer>
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

        // HTML style

        CTString rStyle = getRStyle( run );
        XWPFStyle runStyle = super.getXWPFStyle( rStyle != null ? rStyle.getVal() : null );

        StringBuilder htmlStyle =
            XHTMLStyleUtil.getStyle( run, runStyle, super.getXWPFStyle( run.getParagraph().getStyle() ), defaults );
        List<CTBr> brs = run.getCTR().getBrList();
        for ( @SuppressWarnings( "unused" )
        CTBr br : brs )
        {
            paragraphContainer.startEndElement( BR_ELEMENT );
        }

        List<CTText> texts = run.getCTR().getTList();
        for ( CTText ctText : texts )
        {
            paragraphContainer.startElementNotEnclosed( SPAN_ELEMENT );
            setAttributStyleIfNeeded( paragraphContainer, htmlStyle );
            paragraphContainer.endElementNotEnclosed();
            // Set the text by escaping it with HTML.
            paragraphContainer.setText( StringEscapeUtils.escapeHtml( ctText.getStringValue() ) );
            paragraphContainer.endElement( SPAN_ELEMENT );
        }

        super.visitPictures( run, paragraphContainer );
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
    protected void visitTableRow( XWPFTableRow row, XHTMLPageContentBuffer tableContainer )
        throws Exception
    {
        tableContainer.startElementNotEnclosed( TR_ELEMENT );
        tableContainer.endElementNotEnclosed();

        super.visitTableRow( row, tableContainer );

        tableContainer.endElement( TR_ELEMENT );
    }

    @Override
    protected XHTMLPageContentBuffer startVisitTableCell( XWPFTableCell tableCell, XHTMLPageContentBuffer tableContainer )
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
    protected void visitPicture( XWPFPicture picture, XHTMLPageContentBuffer parentContainer )
        throws Exception
    {
        parentContainer.startElementNotEnclosed( IMG_ELEMENT );

        CTPicture ctPic = picture.getCTPicture();
        String blipId = ctPic.getBlipFill().getBlip().getEmbed();
        // Src attribute
        XWPFPictureData pictureData = XWPFPictureUtil.getPictureData( document, blipId );
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
    protected void visitHeader( CTHdrFtrRef headerRef )
        throws Exception
    {
        // TODO Auto-generated method stub
    }

    @Override
    protected void visitFooter( CTHdrFtrRef footerRef )
        throws Exception
    {
        // TODO Auto-generated method stub
    }
}
