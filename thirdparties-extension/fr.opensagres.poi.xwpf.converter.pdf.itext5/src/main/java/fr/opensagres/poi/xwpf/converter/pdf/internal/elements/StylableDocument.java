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
package fr.opensagres.poi.xwpf.converter.pdf.internal.elements;

import static fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil.dxa2points;

import java.io.OutputStream;
import java.math.BigInteger;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import fr.opensagres.poi.xwpf.converter.core.MasterPageManager;
import fr.opensagres.poi.xwpf.converter.core.PageOrientation;
import fr.opensagres.poi.xwpf.converter.core.XWPFConverterException;
import fr.opensagres.poi.xwpf.converter.core.utils.XWPFUtils;
import fr.opensagres.xdocreport.itext.extension.ExtendedDocument;
import fr.opensagres.xdocreport.itext.extension.ExtendedHeaderFooter;
import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPTable;
import fr.opensagres.xdocreport.itext.extension.IITextContainer;
import fr.opensagres.xdocreport.itext.extension.IMasterPage;
import fr.opensagres.xdocreport.itext.extension.IMasterPageHeaderFooter;
import fr.opensagres.xdocreport.itext.extension.IPdfWriterConfiguration;

public class StylableDocument
    extends ExtendedDocument
{

    private StylableMasterPage activeMasterPage;

    private boolean masterPageJustChanged;

    private boolean documentEmpty = true;

    private PdfPTable layoutTable;

    private StylableColumnText text;

    private int colIdx;

    private MasterPageManager masterPageManager;

    public StylableDocument( OutputStream out, IPdfWriterConfiguration configuration )
        throws DocumentException
    {
        super( out, configuration );
    }

    @Override
    public void addElement( Element element )
    {
        if ( !super.isOpen() )
        {
            super.open();
        }
        if ( masterPageJustChanged )
        {
            // master page was changed but there was no explicit page break
            pageBreak();
        }
        text.addElement( element );
        StylableDocumentSection.getCell( layoutTable, colIdx ).getColumn().addElement( element );
        try
        {
            if ( ColumnText.hasMoreText( text.go( true ) ) && !text.isBlank() )
            {
                columnBreak();
            }
        }
        catch ( DocumentException e )
        {
            throw new XWPFConverterException( e );
        }
        documentEmpty = false;
    }

    public void columnBreak()
    {
        if ( colIdx + 1 < layoutTable.getNumberOfColumns() )
        {
            setColIdx( colIdx + 1 );
            simulateText();
        }
        else
        {
            pageBreak();
        }
    }

    public void pageBreak()
    {
        if ( documentEmpty )
        {
            // no element was added - ignore page break
        }
        else if ( masterPageJustChanged )
        {
            // we are just after master page change
            // move to a new page but do not initialize column layout
            // because it is already done
            masterPageJustChanged = false;
            super.newPage();
        }
        else
        {
            // flush pending content
            flushTable();
            // check if master page change necessary
            // Style nextStyle = setNextActiveMasterPageIfNecessary();
            // document new page
            super.newPage();
            // initialize column layout for new page
            // if ( nextStyle == null )
            // {
            // ordinary page break
            layoutTable = StylableDocumentSection.cloneAndClearTable( layoutTable, false );
            // }
            // else
            // {
            // // page break with new master page activation
            // // style changed so recreate table
            // layoutTable =
            // StylableDocumentSection.createLayoutTable( getPageWidth(), getAdjustedPageHeight(), nextStyle );
            // }
            setColIdx( 0 );
            simulateText();
        }
    }

    @Override
    public boolean newPage()
    {
        throw new XWPFConverterException( "internal error - do not call newPage directly" );
    }

    @Override
    public int getPageNumber()
    {
        return writer.getPageNumber();
    }

    @Override
    public void close()
    {
        flushTable();
        super.close();
    }

    public float getWidthLimit()
    {
        PdfPCell cell = StylableDocumentSection.getCell( layoutTable, colIdx );
        return cell.getRight() - cell.getPaddingRight() - cell.getLeft() - cell.getPaddingLeft();
    }

    public float getHeightLimit()
    {
        // yLine is negative
        return StylableDocumentSection.getCell( layoutTable, colIdx ).getFixedHeight() + text.getYLine();
    }

    public float getPageWidth()
    {
        return right() - left();
    }

    private float getAdjustedPageHeight()
    {
        // subtract small value from height, otherwise table breaks to new page
        return top() - bottom() - 0.001f;
    }

    private void setColIdx( int idx )
    {
        colIdx = idx;
        PdfPCell cell = StylableDocumentSection.getCell( layoutTable, colIdx );
        text.setSimpleColumn( cell.getLeft() + cell.getPaddingLeft(), -getAdjustedPageHeight(),
                              cell.getRight() - cell.getPaddingRight(), 0.0f );
        cell.setColumn( ColumnText.duplicate( text ) );
    }

    private void simulateText()
    {
        int res = 0;
        try
        {
            res = text.go( true );
        }
        catch ( DocumentException e )
        {
            throw new XWPFConverterException( e );
        }
        if ( ColumnText.hasMoreText( res ) )
        {
            // text does not fit into current column
            // split it to a new column
            columnBreak();
        }
    }

    public StylableParagraph createParagraph( IITextContainer parent )
    {
        return new StylableParagraph( this, parent );
    }

    public Paragraph createParagraph()
    {
        return createParagraph( (IITextContainer) null );
    }

    public Paragraph createParagraph( Paragraph title )
    {
        return new StylableParagraph( this, title, null );
    }

    // public StylablePhrase createPhrase( IITextContainer parent )
    // {
    // return new StylablePhrase( this, parent );
    // }
    //
    // public StylableAnchor createAnchor( IITextContainer parent )
    // {
    // return new StylableAnchor( this, parent );
    // }
    //
    // public StylableList createList( IITextContainer parent )
    // {
    // return new StylableList( this, parent );
    // }
    //
    // public StylableListItem createListItem( IITextContainer parent )
    // {
    // return new StylableListItem( this, parent );
    // }

    public StylableTable createTable( IITextContainer parent, int numColumns )
    {
        return new StylableTable( this, parent, numColumns );
    }

    public StylableTableCell createTableCell( IITextContainer parent )
    {
        return new StylableTableCell( this, parent );
    }

    public StylableTableCell createTableCell( IITextContainer parent, ExtendedPdfPTable table )
    {
        return new StylableTableCell( this, parent, table );
    }

    @Override
    public void setActiveMasterPage( IMasterPage m )
    {
        StylableMasterPage masterPage = (StylableMasterPage) m;
        if ( activeMasterPage != null && XWPFUtils.isContinuousSection( masterPage.getSectPr() ) )
        {
            // ignore section with "continous" section <w:sectPr><w:type w:val="continuous" />
            // because continous section applies changes (ex: modify width/height)
            // for the paragraph and iText cannot support that (a new page must be added to
            // change the width/height of the page).

            // see explanation about "continous" at http://officeopenxml.com/WPsection.php
            return;
        }

        // flush pending content
        flushTable();
        // activate master page in three steps

        // Style style = getStyleMasterPage( masterPage );
        // if ( style != null )
        // {
        // step 1 - apply styles like page dimensions and orientation
        this.applySectPr( masterPage.getSectPr() );
        // }
        // step 2 - set header/footer if any, it needs page dimensions from step 1
        super.setActiveMasterPage( masterPage );
        if ( activeMasterPage != null )
        {
            // set a flag used by addElement/pageBreak
            masterPageJustChanged = true;
        }
        activeMasterPage = masterPage;
        // step 3 - initialize column layout, it needs page dimensions which may be lowered by header/footer in step 2
        layoutTable = StylableDocumentSection.createLayoutTable( getPageWidth(), getAdjustedPageHeight() );
        text = StylableDocumentSection.createColumnText();
        setColIdx( 0 );
    }

    private void applySectPr( CTSectPr sectPr )
    {
        // Set page size
        CTPageSz pageSize = sectPr.getPgSz();
        Rectangle pdfPageSize = new Rectangle( dxa2points( pageSize.getW() ), dxa2points( pageSize.getH() ) );
        super.setPageSize( pdfPageSize );

        // Orientation
        PageOrientation orientation = XWPFUtils.getPageOrientation( pageSize.getOrient() );
        if ( orientation != null )
        {
            switch ( orientation )
            {
                case Landscape:
                    super.setOrientation( fr.opensagres.xdocreport.itext.extension.PageOrientation.Landscape );
                    break;
                case Portrait:
                    super.setOrientation( fr.opensagres.xdocreport.itext.extension.PageOrientation.Portrait );
                    break;
            }
        }

        // Set page margin
        CTPageMar pageMar = sectPr.getPgMar();
        if ( pageMar != null )
        {
            super.setOriginalMargins( dxa2points( pageMar.getLeft() ), dxa2points( pageMar.getRight() ),
                                      dxa2points( pageMar.getTop() ), dxa2points( pageMar.getBottom() ) );
        }

        // see http://officeopenxml.com/WPSectionPgNumType.php
        CTPageNumber pageNumberType = sectPr.getPgNumType();
        if ( pageNumberType != null )
        {
            BigInteger start = pageNumberType.getStart();
            if ( start != null )
            {
                writer.setPageCount( start.intValue() - 1 );
            }
        }
    }

    private void flushTable()
    {
        if ( layoutTable != null )
        {
            // force calculate height because it may be zero
            // and nothing will be flushed
            layoutTable.calculateHeights();
            try
            {
                super.add( layoutTable );
            }
            catch ( DocumentException e )
            {
                throw new XWPFConverterException( e );
            }
        }
    }

    @Override
    protected ExtendedHeaderFooter createExtendedHeaderFooter()
    {
        return new ExtendedHeaderFooter( this )
        {
            @Override
            public void onStartPage( PdfWriter writer, Document doc )
            {
                super.onStartPage( writer, doc );
                StylableDocument.this.onStartPage();
            }

            @Override
            protected float getHeaderY( IMasterPageHeaderFooter header )
            {
                Float headerY = ( (StylableHeaderFooter) header ).getY();
                if ( headerY != null )
                {
                    return document.getPageSize().getHeight() - headerY;
                }
                return super.getHeaderY( header );
            }

            @Override
            protected float getFooterY( IMasterPageHeaderFooter footer )
            {
                Float footerY = ( (StylableHeaderFooter) footer ).getY();
                if ( footerY != null )
                {
                    return ( (StylableHeaderFooter) footer ).getTotalHeight();
                }
                return super.getFooterY( footer );
            }

            @Override
            protected float adjustMargin( float margin, IMasterPageHeaderFooter headerFooter )
            {
                if ( ( (StylableHeaderFooter) headerFooter ).getY() != null )
                {
                    // has page margin defined (PgMar)
                    if ( headerFooter.getTotalHeight() > margin )
                    {
                        return headerFooter.getTotalHeight();
                    }
                    return margin;
                }
                return super.adjustMargin( margin, headerFooter );
            }
        };
    }

    protected void onStartPage()
    {
        masterPageManager.onNewPage();
    }

    public void setMasterPageManager( MasterPageManager masterPageManager )
    {
        this.masterPageManager = masterPageManager;
    }
}
