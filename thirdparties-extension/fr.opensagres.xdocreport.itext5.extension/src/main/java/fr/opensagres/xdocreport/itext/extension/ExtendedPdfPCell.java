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

import com.itextpdf.text.Element;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class ExtendedPdfPCell
    extends PdfPCell
    implements IITextContainer
{

    private boolean empty;

    private IITextContainer container;

    public ExtendedPdfPCell()
    {
        this.empty = true;
    }

    public ExtendedPdfPCell( PdfPTable table )
    {
        super( table );
        this.empty = true;
    }

    @Override
    public void addElement( Element element )
    {
        this.empty = false;
        if ( element instanceof ListItem )
        {
            List aList = new List();
            aList.setIndentationLeft( ( (ListItem) element ).getIndentationLeft() );
            aList.add( element );
            super.addElement( aList );
        }
        else
        {
            super.addElement( element );
        }
    }

    public IITextContainer getITextContainer()
    {
        return container;
    }

    public void setITextContainer( IITextContainer container )
    {
        this.container = container;
    }

    public boolean isEmpty()
    {
        ExtendedPdfPTable table = getTable();
        if ( table != null )
        {
            return table.isEmpty();
        }
        return empty;
    }

    @Override
    public ExtendedPdfPTable getTable()
    {
        return (ExtendedPdfPTable) super.getTable();
    }
}
