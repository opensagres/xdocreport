package fr.opensagres.xdocreport.itext.extension;

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
            cell.setBackgroundColor( Color.red );
            
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
