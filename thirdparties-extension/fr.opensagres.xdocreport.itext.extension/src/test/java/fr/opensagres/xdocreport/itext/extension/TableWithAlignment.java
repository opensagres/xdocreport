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

public class TableWithAlignment
{

    public static void main( String[] args )
    {
        Document document = new Document( PageSize.A4.rotate(), 10, 10, 10, 10 );
        try
        {
            PdfWriter writer = PdfWriter.getInstance( document, new FileOutputStream( "TableWithAlignment.pdf" ) );

            document.open();

            PdfPTable table = new PdfPTable( 2 );
            table.getDefaultCell().setUseAscender(true);
            table.getDefaultCell().setUseDescender(true);
            
            
            table.setWidths( new float[] { 5, 10 } );
            table.setWidthPercentage( 10 );
            
            PdfPCell cell1 = new PdfPCell();
            cell1.addElement( new Chunk( "cell1" ) );
            table.addCell( cell1 );

            PdfPCell cell2 = new PdfPCell();
            cell2.addElement( new Chunk( "cell2" ) );
            table.addCell( cell2 );

            table.setSpacingBefore( -100 );
            
            document.add( table );
        }
        catch ( Exception de )
        {
            de.printStackTrace();
        }
        document.close();
    }
}
