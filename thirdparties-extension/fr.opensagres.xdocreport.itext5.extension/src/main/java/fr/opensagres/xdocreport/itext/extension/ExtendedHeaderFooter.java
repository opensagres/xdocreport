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
package fr.opensagres.xdocreport.itext.extension;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

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

    /**
     * Adds a header/footer to every page
     * 
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
     *      com.itextpdf.text.Document)
     */
    public void onStartPage( PdfWriter writer, Document doc )
    {
        if ( masterPage == null )
        {
            masterPage = document.getDefaultMasterPage();
        }

        if ( masterPage != null )
        {
            // Master page is defined

            IMasterPageHeaderFooter header = masterPage.getHeader();
            IMasterPageHeaderFooter footer = masterPage.getFooter();

            // Add header
            if ( header != null )
            {
                // compute upper-left corner coordinates
                float x = document.getOriginMarginLeft();
                float y = getHeaderY( header );

                header.writeSelectedRows( 0, -1, x, y, writer.getDirectContentUnder() );
            }

            // Add footer
            if ( footer != null )
            {
                // compute upper-left corner coordinates
                float x = document.getOriginMarginLeft();
                float y = getFooterY( footer );

                footer.writeSelectedRows( 0, -1, x, y, writer.getDirectContentUnder() );
            }
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
