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
package org.odftoolkit.odfdom.converter.internal.itext.stylable;

import org.odftoolkit.odfdom.converter.internal.itext.styles.Style;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleHeaderFooterProperties;

import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfContentByte;

import fr.opensagres.xdocreport.itext.extension.IMasterPageHeaderFooter;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StylableHeaderFooter
    extends StylableTable
    implements IMasterPageHeaderFooter
{

    private final StylableDocument ownerDocument;

    private final boolean header;

    private final StylableTableCell tableCell;

    public StylableHeaderFooter( StylableDocument ownerDocument, boolean header )
    {
        super( ownerDocument, null, 1 );
        this.ownerDocument = ownerDocument;
        this.header = header;
        tableCell = ownerDocument.createTableCell( this );
        tableCell.setBorder( Table.NO_BORDER );
        // set padding to zero for proper alignment
        tableCell.setPadding( 0.0f );
    }

    private void computeWidth()
    {
        // compute width as page width - margins
        float headerFooterWidth =
            ownerDocument.getPageSize().getWidth() - ownerDocument.getOriginMarginLeft()
                - ownerDocument.getOriginMarginRight();
        if ( getTotalWidth() != headerFooterWidth )
        {
            setTotalWidth( headerFooterWidth );
        }
    }

    public StylableTableCell getTableCell()
    {
        return tableCell;
    }

    @Override
    public void applyStyles( Style style )
    {
        StyleHeaderFooterProperties headerFooterProperties =
            header ? style.getHeaderProperties() : style.getFooterProperties();
        if ( headerFooterProperties != null )
        {
            Float minHeight = headerFooterProperties.getMinHeight();
            if ( minHeight != null )
            {
                tableCell.setMinimumHeight( minHeight );
            }
        }

    }

    public float getTotalHeight()
    {
        computeWidth();
        return super.getRowHeight( 0 );
    }

    public void flush()
    {
        super.addCell( tableCell );
    }

    @Override
    public float writeSelectedRows( int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas )
    {
        computeWidth();
        return super.writeSelectedRows( rowStart, rowEnd, xPos, yPos, canvas );
    }
}
