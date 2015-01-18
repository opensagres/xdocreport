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

import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class SimpleTable
{
    public static void main( String[] args )
    {
        Document document = new Document( PageSize.A4.rotate(), 10, 10, 10, 10 );
        try
        {
            PdfWriter writer = PdfWriter.getInstance( document, new FileOutputStream( "SimpleTable.pdf" ) );

            document.open();

            PdfPTable table = new PdfPTable( 2 );
            PdfPCell cell;
            Paragraph p = new Paragraph( "Text Text Text " );
            table.addCell( "cell" );
            table.addCell( p );
            table.addCell( "cell" );
            cell = new PdfPCell( p );
            
            p = new Paragraph( "Text Text Text " );
            
            //Chunk c = new Chunk( "zzzzzzzzzz" );   
            
            //cell.setPadding( 0f );
            //cell.getColumn().setAdjustFirstLine( false );
            // make a room for borders
            //cell.setUseBorderPadding( false );
            
            
            cell.setHorizontalAlignment( Element.ALIGN_RIGHT );
            cell.setVerticalAlignment( Element.ALIGN_MIDDLE);
            cell.setBackgroundColor( BaseColor.RED );
            
            cell.addElement( p );
            
            cell.setUseAscender( true );
            //cell.addElement(c );
            // cell.setPaddingTop( 0f );
            // cell.setPaddingLeft( 20f );

            table.addCell( cell );
            //table.addCell( "cell" );
            document.add( table );
        }
        catch ( Exception de )
        {
            de.printStackTrace();
        }
        document.close();
    }
}
