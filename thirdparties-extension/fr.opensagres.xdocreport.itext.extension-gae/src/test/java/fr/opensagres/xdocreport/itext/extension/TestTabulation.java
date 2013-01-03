package fr.opensagres.xdocreport.itext.extension;

import java.io.FileOutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.lowagie.text.pdf.draw.VerticalPositionMark;

public class TestTabulation
{
    public static void main( String[] args )
    {
        Document document = new Document( PageSize.A4, 10, 10, 10, 10 );
        try
        {
            PdfWriter writer = PdfWriter.getInstance( document, new FileOutputStream( "TestTabulation.pdf" ) );

            document.open();

            Paragraph p = new Paragraph();
            VerticalPositionMark mark = new VerticalPositionMark();
            //mark.setOffset( 1f );
            Chunk tab1 = new Chunk( mark, 400f );
            p.add( tab1 );
            Chunk c=new Chunk( "After one tab" );
            p.add( c );
            
            mark = new VerticalPositionMark();
            //mark.setOffset( 500f );
            Chunk tab2 = new Chunk( mark, 400f + c.getWidthPoint() + 100f );
            p.add( tab2 );
            p.add( new Chunk( "X" ) );
            document.add( p );
        }
        catch ( Exception de )
        {
            de.printStackTrace();
        }
        document.close();
    }
}
