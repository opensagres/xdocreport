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

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class TableWithVerticalAlignment
{

    public static void main( String[] args )
    {
        Document document = new Document( PageSize.A4.rotate(), 10, 10, 10, 10 );
        try
        {
            PdfWriter writer =
                PdfWriter.getInstance( document, new FileOutputStream( "TableWithVerticalAlignment.pdf" ) );

            document.open();

            PdfPTable table = new PdfPTable( 1 );
            // PdfPCell cell1 = new PdfPCell();
            // cell1.setVerticalAlignment( Element.ALIGN_BOTTOM );
            // //cell1.setMinimumHeight( 100f );
            // cell1.addElement( new Chunk( "cell1" ) );
            // table.addCell( cell1 );

            PdfPCell cell2 = new PdfPCell();
            Paragraph p = new Paragraph();
            p.add( new Chunk( "cellp&" ) );

            cell2.addElement( p );
            cell2.setVerticalAlignment( Element.ALIGN_BOTTOM );
            cell2.setMinimumHeight( 38f );
            table.addCell( cell2 );

            document.add( table );
        }
        catch ( Exception de )
        {
            de.printStackTrace();
        }
        document.close();
    }
}
