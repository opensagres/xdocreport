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
package fr.opensagres.poi.xwpf.converter.pdf.internal.elements;

import java.math.BigInteger;

import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfContentByte;

import fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil;
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

    private StylableTableCell tableCell;

    private boolean flushed;

    private final Float y;

    private Float totalHeight;

    public StylableHeaderFooter( StylableDocument ownerDocument, BigInteger dxaY, boolean header )
    {
        super( ownerDocument, null, 1 );
        this.ownerDocument = ownerDocument;
        this.header = header;
        tableCell = ownerDocument.createTableCell( this );
        tableCell.setBorder( Table.NO_BORDER );
        // set padding to zero for proper alignment
        //tableCell.setPadding( 0.0f );
        this.y = dxaY != null ? DxaUtil.dxa2points( dxaY ) : null;
        this.totalHeight = null;
    }

    public StylableTableCell getTableCell()
    {
        return tableCell;
    }

    private void setWidthIfNecessary()
    {
        // compute width as page width - margins
        float headerFooterWidth = ownerDocument.right() - ownerDocument.left();
        if ( getTotalWidth() != headerFooterWidth )
        {
            setTotalWidth( headerFooterWidth );
        }
    }

    public float getTotalHeight()
    {
        if ( totalHeight == null )
        {
            totalHeight = computeTotalHeight();
        }
        return totalHeight;
    }

    private float computeTotalHeight()
    {
        setWidthIfNecessary();
        float height = super.getRowHeight( 0 );
        if ( y != null )
        {
            return height + y;
        }
        return height;
    }

    public void flush()
    {
        if ( flushed )
        {
            return;
        }
        super.addCell( tableCell );
        flushed = true;
    }

    @Override
    public float writeSelectedRows( int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas )
    {
        setWidthIfNecessary();
        return super.writeSelectedRows( rowStart, rowEnd, xPos, yPos, canvas );
    }

    public Float getY()
    {
        return y;
    }
}
