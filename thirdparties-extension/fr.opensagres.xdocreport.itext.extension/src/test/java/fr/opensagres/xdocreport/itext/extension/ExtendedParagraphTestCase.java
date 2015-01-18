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

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import junit.framework.TestCase;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ExtendedParagraphTestCase
    extends TestCase
{

    public void testExtendedParagraph()
    {

        Document document = new Document();
        try
        {
            PdfWriter.getInstance( document, new FileOutputStream( "output.pdf" ) );
            document.open();

            // Simple border
            ExtendedParagraph p1 = new ExtendedParagraph();
            // BaseFont.createFont(BaseFont.TIMES_ROMAN, encoding, embedded)

            p1.setFont( FontFactory.getFont( BaseFont.TIMES_ROMAN, 10f, Font.BOLD, Color.BLUE ) );
            // p1.setFont(new Font(FontFamily.TIMES_ROMAN, 10f, Font.BOLD, Color.BLUE));
            p1.getWrapperCell().setBorder( Rectangle.BOX );
            p1.add( new Chunk( "Title 1" ) );
            document.add( p1.getElement() );

            Paragraph separator = new Paragraph();
            separator.add( Chunk.NEWLINE );
            document.add( separator );

            // Custom border
            ExtendedParagraph p2 = new ExtendedParagraph();
            p2.getWrapperCell().setCellEvent( new RoundRectangle( new int[] { 0xFF, 0x00, 0xFF, 0x00 } ) );
            p2.add( new Chunk( "Title 2" ) );
            document.add( p2.getElement() );

            separator = new Paragraph();
            separator.add( Chunk.NEWLINE );
            document.add( separator );

            // Chapter title with border + style
            ExtendedParagraph title = new ExtendedParagraph();

            title.setFont( FontFactory.getFont( BaseFont.TIMES_ROMAN, 10f, Font.BOLD, Color.BLUE ) );
            // title.setFont(new Font(FontFamily.TIMES_ROMAN, 10f, Font.BOLD, Color.BLUE));
            title.add( new Chunk( "Chapter 1" ) );

            ExtendedChapter chapter1 = new ExtendedChapter( title, 1 );
            chapter1.setTriggerNewPage( false );
            chapter1.getPdfPCell().setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT );
            chapter1.add( new Paragraph( "bla bla bla" ) );

            // Section title with border
            ExtendedSection section1 = (ExtendedSection) chapter1.addSection( "Section 1.1" );
            section1.getPdfPCell().setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT );
            section1.add( new Paragraph( "bla bla bla" ) );

            document.add( chapter1 );

            separator = new Paragraph();
            separator.add( Chunk.NEWLINE );
            document.add( separator );

            // Chapter AutoNumber title with border
            ExtendedChapterAutoNumber chapterAutoNumber1 = new ExtendedChapterAutoNumber( "Chapter-Auto 1" );
            chapterAutoNumber1.setTriggerNewPage( false );
            chapterAutoNumber1.getPdfPCell().setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT
                                                            | Rectangle.RIGHT );

            // Section AutoNumber title with border
            ExtendedSection sectionAutoNumber1 = (ExtendedSection) chapter1.addSection( "Section-Auto 1.1" );
            sectionAutoNumber1.getPdfPCell().setBorder( Rectangle.BOTTOM | Rectangle.TOP | Rectangle.LEFT
                                                            | Rectangle.RIGHT );

            document.add( chapterAutoNumber1 );

            document.close();

        }
        catch ( FileNotFoundException e1 )
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        catch ( DocumentException e1 )
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Inner class with a cell event that draws a border with rounded corners.
     */
    class RoundRectangle
        implements PdfPCellEvent
    {
        /** the border color described as CMYK values. */
        protected int[] color;

        /** Constructs the event using a certain color. */
        public RoundRectangle( int[] color )
        {
            this.color = color;
        }

        public void cellLayout( PdfPCell cell, Rectangle rect, PdfContentByte[] canvas )
        {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle( rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                               rect.getHeight() - 3, 4 );
            cb.setLineWidth( 1.5f );
            cb.setCMYKColorStrokeF( color[0], color[1], color[2], color[3] );
            cb.stroke();
        }
    }

}
