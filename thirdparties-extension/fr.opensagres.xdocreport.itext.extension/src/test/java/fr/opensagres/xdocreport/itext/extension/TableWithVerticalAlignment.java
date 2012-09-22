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
