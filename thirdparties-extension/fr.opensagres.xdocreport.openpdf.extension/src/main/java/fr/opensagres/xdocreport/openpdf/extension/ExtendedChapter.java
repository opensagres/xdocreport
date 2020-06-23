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

import com.lowagie.text.Chapter;
import com.lowagie.text.MarkedSection;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfPCell;

public class ExtendedChapter
    extends Chapter
{

    private Paragraph computedTitle = null;

    private PdfPCell cell;

    private Paragraph bookmarkTitleParagraph;

    public ExtendedChapter( int number )
    {
        super( number );
    }

    public ExtendedChapter( Paragraph title, int number )
    {
        super( title, number );
    }

    /**
     * Constructs a new <CODE>Chapter</CODE>.
     * 
     * @param title the Chapter title (as a <CODE>String</CODE>)
     * @param number the Chapter number
     */
    public ExtendedChapter( String title, int number )
    {
        super( title, number );
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
