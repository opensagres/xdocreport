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

import java.util.ArrayList;

import com.lowagie.text.Chunk;
import com.lowagie.text.MarkedSection;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class ExtendedSection
    extends Section
{

    private PdfPCell cell;

    private com.lowagie.text.Paragraph computedTitle;

    private Paragraph bookmarkTitleParagraph;

    public ExtendedSection( Paragraph title, int numberDepth )
    {
        super( title, numberDepth );
    }

    @Override
    public Paragraph getTitle()
    {
        if ( computedTitle == null )
        {
            bookmarkTitleParagraph =
                ExtendedSection.ancestorConstructTitle( getParagraphFactory(), title, numbers, numberDepth, numberStyle );
            computedTitle =
                ExtendedSection.constructTitle( getParagraphFactory(), bookmarkTitleParagraph, numbers, numberDepth,
                                                numberStyle, cell );
        }
        return computedTitle;
    }

    @Override
    public Paragraph getBookmarkTitle()
    {
        return bookmarkTitleParagraph;
    }

    public Section addSection( float indentation, Paragraph title, int numberDepth )
    {
        if ( isAddedCompletely() )
        {
            throw new IllegalStateException( "This LargeElement has already been added to the Document." );
        }
        Section section = new ExtendedSection( title, numberDepth );
        section.setIndentation( indentation );
        add( section );
        return section;
    }

    public MarkedSection addMarkedSection()
    {
        MarkedSection section = new MarkedSection( new ExtendedSection( null, numberDepth + 1 ) );
        add( section );
        return section;
    }

    public static Paragraph constructTitle( IParagraphFactory factory, Paragraph ancestorTitle, ArrayList numbers,
                                            int numberDepth, int numberStyle, PdfPCell cell )
    {
        if ( ancestorTitle != null && cell != null )
        {
            Paragraph newTitle = factory.createParagraph();
            PdfPTable table = new PdfPTable( 1 );
            table.setWidthPercentage( 100f );
            cell.addElement( ancestorTitle );
            table.addCell( cell );
            newTitle.add( table );
            return newTitle;
        }
        return ancestorTitle;
    }

    /**
     * Constructs a Paragraph that will be used as title for a Section or Chapter.
     * 
     * @param title the title of the section
     * @param numbers a list of sectionnumbers
     * @param numberDepth how many numbers have to be shown
     * @param numberStyle the numbering style
     * @return a Paragraph object
     * @since iText 2.0.8
     */
    public static Paragraph ancestorConstructTitle( IParagraphFactory factory, Paragraph title, ArrayList numbers,
                                                    int numberDepth, int numberStyle )
    {
        if ( title == null )
        {
            return null;
        }

        int depth = Math.min( numbers.size(), numberDepth );
        if ( depth < 1 )
        {
            return title;
        }
        StringBuffer buf = new StringBuffer( " " );
        for ( int i = 0; i < depth; i++ )
        {
            buf.insert( 0, "." );
            buf.insert( 0, ( (Integer) numbers.get( i ) ).intValue() );
        }
        if ( numberStyle == NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT )
        {
            buf.deleteCharAt( buf.length() - 2 );
        }
        Paragraph result = factory.createParagraph( title );
        result.add( 0, new Chunk( buf.toString(), title.getFont() ) );
        return result;
    }

    public PdfPCell getPdfPCell()
    {
        if ( cell != null )
        {
            return cell;
        }
        cell = createPdfPCell();
        return cell;
    }

    private synchronized PdfPCell createPdfPCell()
    {
        if ( cell != null )
        {
            return cell;
        }
        PdfPCell cell = new PdfPCell();
        cell.setBorder( Rectangle.NO_BORDER );
        cell.setPadding( 0 );
        return cell;
    }

    protected IParagraphFactory getParagraphFactory()
    {
        return ParagraphFactory.getDefault();
    }
}
