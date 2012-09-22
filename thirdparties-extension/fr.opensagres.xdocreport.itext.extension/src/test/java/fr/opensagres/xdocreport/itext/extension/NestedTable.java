package fr.opensagres.xdocreport.itext.extension;

import java.io.FileOutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class NestedTable
{
    public static void main( String[] args )
    {
        Document document = new Document( PageSize.A4.rotate(), 10, 10, 10, 10 );
        try
        {
            PdfWriter writer = PdfWriter.getInstance( document, new FileOutputStream( "NestedTable.pdf" ) );
            document.open();

            PdfPTable table = new PdfPTable( 1 );
                        
            PdfPTable nestedTable = new PdfPTable( 2 );
            
            
            PdfPCell cell = new PdfPCell(nestedTable);


            PdfPCell cell1 = new PdfPCell();
            cell1.addElement( new Chunk("cell1") );
            nestedTable.addCell( cell1 );
            
            PdfPCell cell2 = new PdfPCell();
            cell2.addElement( new Chunk("cell2") );
            nestedTable.addCell( cell2 );
            
            table.addCell( cell );
            
            document.add( table );
        }
        catch ( Exception de )
        {
            de.printStackTrace();
        }
        document.close();
    }
}
