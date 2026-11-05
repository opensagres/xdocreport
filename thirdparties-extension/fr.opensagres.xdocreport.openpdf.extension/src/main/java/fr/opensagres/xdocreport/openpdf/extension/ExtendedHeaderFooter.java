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
package fr.opensagres.xdocreport.openpdf.extension;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class ExtendedHeaderFooter
    extends PdfPageEventHelper
{
    protected final ExtendedDocument document;

    private IMasterPage masterPage;

    public ExtendedHeaderFooter( ExtendedDocument document )
    {
        this.document = document;
    }

    private IMasterPageHeaderFooter pageHeader;

    private float pageHeaderX;

    private float pageHeaderY;

    private IMasterPageHeaderFooter pageFooter;

    private float pageFooterX;

    private float pageFooterY;

    /**
     * Determines the header/footer and the position of the page which is starting. They are drawn in
     * {@link #onEndPage(PdfWriter, Document)}: OpenPDF calls onStartPage for the page that follows the last one when
     * the document is closed, and links of a header/footer drawn on this phantom page would create annotations for a
     * page which does not exist.
     */
    public void onStartPage( PdfWriter writer, Document doc )
    {
        pageHeader = null;
        pageFooter = null;

        if ( masterPage == null )
        {
            masterPage = document.getDefaultMasterPage();
        }

        if ( masterPage != null )
        {
            // Master page is defined
            pageHeader = masterPage.getHeader();
            pageFooter = masterPage.getFooter();

            // compute upper-left corner coordinates
            if ( pageHeader != null )
            {
                pageHeaderX = document.getOriginMarginLeft();
                pageHeaderY = getHeaderY( pageHeader );
            }
            if ( pageFooter != null )
            {
                pageFooterX = document.getOriginMarginLeft();
                pageFooterY = getFooterY( pageFooter );
            }

            if ( pageHeader != null || pageFooter != null )
            {
                // A page with a header/footer is not empty, even when its body is
                PdfContentByte under = writer.getDirectContentUnder();
                under.saveState();
                under.restoreState();
            }
        }
    }

    /**
     * Adds the header/footer captured by {@link #onStartPage(PdfWriter, Document)} to the page which is ending.
     */
    public void onEndPage( PdfWriter writer, Document doc )
    {
        IMasterPageHeaderFooter header = pageHeader;
        IMasterPageHeaderFooter footer = pageFooter;
        pageHeader = null;
        pageFooter = null;

        if ( header != null )
        {
            header.writeSelectedRows( 0, -1, pageHeaderX, pageHeaderY, writer.getDirectContentUnder() );
        }
        if ( footer != null )
        {
            footer.writeSelectedRows( 0, -1, pageFooterX, pageFooterY, writer.getDirectContentUnder() );
        }
    }

    protected float getFooterY( IMasterPageHeaderFooter footer )
    {
        return document.getOriginMarginBottom() + footer.getTotalHeight();
    }

    protected float getHeaderY( IMasterPageHeaderFooter header )
    {
        return document.getPageSize().getHeight() - document.getOriginMarginTop();
    }

    public void setMasterPage( IMasterPage masterPage )
    {
        IMasterPageHeaderFooter header = masterPage.getHeader();
        IMasterPageHeaderFooter footer = masterPage.getFooter();

        float marginLeft = document.getOriginMarginLeft();
        float marginRight = document.getOriginMarginRight();
        float marginTop = document.getOriginMarginTop();
        if ( header != null )
        {
            marginTop = adjustMargin(marginTop, header);
        }
        float marginBottom = document.getOriginMarginBottom();
        if ( footer != null )
        {
            marginBottom = adjustMargin(marginBottom, footer);
        }
        document.setMargins( marginLeft, marginRight, marginTop, marginBottom );

        this.masterPage = masterPage;
    }
    
    protected float adjustMargin(float margin, IMasterPageHeaderFooter headerFooter) {
        return margin + headerFooter.getTotalHeight();
    }

    public IMasterPage getMasterPage()
    {
        return masterPage;
    }
}
