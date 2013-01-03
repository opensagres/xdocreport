package fr.opensagres.xdocreport.itext.extension;

import java.io.FileOutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class ImageAbsolute
{
    public static void main( String[] args )
    {
        Document document = new Document( PageSize.A4.rotate(), 10, 10, 10, 10 );
        try
        {
            PdfWriter writer = ExtendedPdfWriter.getInstance( document, new FileOutputStream( "ImageAbsolute.pdf" ) );

            document.open();

            Paragraph p = new Paragraph();            
            Image img = Image.getInstance( "AngeloZERR.jpg" );
            //img.scaleAbsolute( 50, 50 );
            //p.setKeepTogether( true );
            //p.add( new Chunk(img, 10,- 50, false) );
            document.add( new ExtendedImage( img, 500 ) );
            p.add( new Chunk( "text text" ) );
            
            //p.setKeepTogether( true );
            document.add( p );
            
            Paragraph p2 = new Paragraph();            
            
            p2.add( new Chunk( "text text" ) );
            document.add( p2 );
        }
        catch ( Exception de )
        {
            de.printStackTrace();
        }
        document.close();
    }
}
