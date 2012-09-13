/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
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
package org.apache.poi.xwpf.converter.internal.itext.stylable;

import static org.apache.poi.xwpf.converter.internal.DxaUtil.dxa2points;

import java.io.OutputStream;

import org.apache.poi.xwpf.converter.XWPFConverterException;
import org.apache.poi.xwpf.converter.internal.itext.StyleEngineForIText;
import org.apache.poi.xwpf.converter.internal.itext.styles.Style;
import org.apache.poi.xwpf.converter.internal.itext.styles.StylePageLayoutProperties;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import fr.opensagres.xdocreport.itext.extension.ExtendedDocument;
import fr.opensagres.xdocreport.itext.extension.ExtendedHeaderFooter;
import fr.opensagres.xdocreport.itext.extension.IMasterPage;
import fr.opensagres.xdocreport.itext.extension.IParagraphFactory;
import fr.opensagres.xdocreport.itext.extension.PageOrientation;

public class StylableDocument
    extends ExtendedDocument
    implements IStylableContainer, IStylableFactory, IParagraphFactory
{

    private final StyleEngineForIText styleEngine;

    private Style lastStyleApplied = null;

    private int titleNumber = 1;

    private StylableChapter currentChapter;

    private StylableMasterPage activeMasterPage;

    private boolean masterPageJustChanged;

    private boolean documentEmpty = true;

    private PdfPTable layoutTable;

    private ColumnText text;

    private int colIdx;

    private int nbPages;

    public StylableDocument( OutputStream out, StyleEngineForIText styleEngine )
        throws DocumentException
    {
        super( out );
        this.styleEngine = styleEngine;
        this.nbPages = 0;
    }

    //
    // this amazing and awesome algorithm
    // which lay out content in columns on page
    // was written by Leszek Piotrowicz <leszekp@safe-mail.net>
    //

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
        simulateText();
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

    public StylableParagraph createParagraph( IStylableContainer parent )
    {
        return new StylableParagraph( this, parent );
    }

    public Paragraph createParagraph()
    {
        return createParagraph( (IStylableContainer) null );
    }

    public Paragraph createParagraph( Paragraph title )
    {
        return new StylableParagraph( this, title, null );
    }

    public StylablePhrase createPhrase( IStylableContainer parent )
    {
        return new StylablePhrase( this, parent );
    }

    public StylableAnchor createAnchor( IStylableContainer parent )
    {
        return new StylableAnchor( this, parent );
    }

    public StylableList createList( IStylableContainer parent )
    {
        return new StylableList( this, parent );
    }

    public StylableListItem createListItem( IStylableContainer parent )
    {
        return new StylableListItem( this, parent );
    }

    public StylableTable createTable( IStylableContainer parent, int numColumns )
    {
        return new StylableTable( this, parent, numColumns );
    }

    public StylableTableCell createTableCell( IStylableContainer parent )
    {
        return new StylableTableCell( this, parent );
    }

    public StylableChapter createChapter( IStylableContainer parent, StylableParagraph title )
    {
        currentChapter = new StylableChapter( this, parent, title, titleNumber++ );
        return currentChapter;
    }

    public StylableChunk createChunk( IStylableContainer parent, String textContent )
    {
        return new StylableChunk( this, parent, textContent );
    }

    public StylableSection createSection( IStylableContainer parent, StylableParagraph title, int numberDepth )
    {
        return new StylableSection( this, parent, title, numberDepth );
    }

    public StylableChapter getCurrentChapter()
    {
        return currentChapter;
    }

    @Override
    public void setActiveMasterPage( IMasterPage masterPage )
    {
        // flush pending content
        flushTable();
        // activate master page in three steps

        // Style style = getStyleMasterPage( masterPage );
        // if ( style != null )
        // {
        // step 1 - apply styles like page dimensions and orientation
        this.applySectPr( ( (StylableMasterPage) masterPage ).getSectPr() );
        // }
        // step 2 - set header/footer if any, it needs page dimensions from step 1
        super.setActiveMasterPage( masterPage );
        if ( activeMasterPage != null )
        {
            // set a flag used by addElement/pageBreak
            masterPageJustChanged = true;
        }
        activeMasterPage = (StylableMasterPage) masterPage;
        // step 3 - initialize column layout, it needs page dimensions which may be lowered by header/footer in step 2
        layoutTable = StylableDocumentSection.createLayoutTable( getPageWidth(), getAdjustedPageHeight(), (Style) null );
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
        org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.Enum orientation =
            pageSize.getOrient();
        if ( orientation != null )
        {
            if ( org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.LANDSCAPE.equals( orientation ) )
            {
                super.setOrientation( PageOrientation.Landscape );
            }
            else
            {
                super.setOrientation( PageOrientation.Portrait );
            }
        }

        // Set page margin
        CTPageMar pageMar = sectPr.getPgMar();
        if ( pageMar != null )
        {
            super.setOriginalMargins( dxa2points( pageMar.getLeft() ), dxa2points( pageMar.getRight() ),
                                      dxa2points( pageMar.getTop() ), dxa2points( pageMar.getBottom() ) );
        }

    }

    // private Style setNextActiveMasterPageIfNecessary()
    // {
    // // called on page break
    // // return new page style if changed
    // if ( activeMasterPage != null )
    // {
    // String nextMasterPageStyleName = null;// activeMasterPage.getNextStyleName();
    // if ( nextMasterPageStyleName != null && nextMasterPageStyleName.length() > 0 )
    // {
    // IMasterPage nextMasterPage = getMasterPage( nextMasterPageStyleName );
    // if ( nextMasterPage != null )
    // {
    // // activate next master page
    // Style style = getStyleMasterPage( nextMasterPage );
    // if ( style != null )
    // {
    // // step 1 - apply styles like page dimensions and orientation
    // this.applyStyles( style );
    // }
    // // step 2 - set header/footer if any, it needs page dimensions from step 1
    // super.setActiveMasterPage( nextMasterPage );
    // //
    // activeMasterPage = nextMasterPage;
    // return style;
    // }
    // }
    // }
    // return null;
    // }

    public IMasterPage getActiveMasterPage()
    {
        return activeMasterPage;
    }

    public Style getStyleMasterPage( IMasterPage masterPage )
    {
        // Style style = styleEngine.getStyle( OdfStyleFamily.List.getName(), masterPage.getPageLayoutName(), null );
        // return style;
        return null;
    }

    @Override
    public IMasterPage getMasterPage( String masterPageName )
    {
        return (IMasterPage) super.getMasterPage( masterPageName );
    }

    @Override
    public IMasterPage getDefaultMasterPage()
    {
        return (IMasterPage) super.getDefaultMasterPage();
    }

    //
    // IStylableContainer implementation
    //

    public void applyStyles( Style style )
    {
        this.lastStyleApplied = style;

        StylePageLayoutProperties pageLayoutProperties = style.getPageLayoutProperties();
        if ( pageLayoutProperties != null )
        {
            // width/height
            Float width = pageLayoutProperties.getWidth();
            Float height = pageLayoutProperties.getHeight();
            if ( width != null && height != null )
            {
                Rectangle pageSize = new Rectangle( width, height );
                super.setPageSize( pageSize );
            }

            // margin
            if ( pageLayoutProperties.getMarginTop() != null )
            {
                originMarginTop = pageLayoutProperties.getMarginTop();
            }
            if ( pageLayoutProperties.getMarginBottom() != null )
            {
                originMarginBottom = pageLayoutProperties.getMarginBottom();
            }
            if ( pageLayoutProperties.getMarginLeft() != null )
            {
                originMarginLeft = pageLayoutProperties.getMarginLeft();
            }
            if ( pageLayoutProperties.getMarginRight() != null )
            {
                originMarginRight = pageLayoutProperties.getMarginRight();
            }
            super.setMargins( originMarginLeft, originMarginRight, originMarginTop, originMarginBottom );

            // orientation
            PageOrientation orientation = pageLayoutProperties.getOrientation();
            if ( orientation != null )
            {
                super.setOrientation( orientation );
            }
        }
    }

    public Style getLastStyleApplied()
    {
        return lastStyleApplied;
    }

    public StyleEngineForIText getStyleEngine()
    {
        return styleEngine;
    }

    public IStylableContainer getParent()
    {
        return null;
    }

    public void applyStyles( Object ele, Style style )
    {
        this.lastStyleApplied = style;

        StylePageLayoutProperties pageLayoutProperties = style.getPageLayoutProperties();
        if ( pageLayoutProperties != null )
        {
            // width/height
            Float width = pageLayoutProperties.getWidth();
            Float height = pageLayoutProperties.getHeight();
            if ( width != null && height != null )
            {
                Rectangle pageSize = new Rectangle( width, height );
                super.setPageSize( pageSize );
            }

            // margin
            if ( pageLayoutProperties.getMarginTop() != null )
            {
                originMarginTop = pageLayoutProperties.getMarginTop();
            }
            if ( pageLayoutProperties.getMarginBottom() != null )
            {
                originMarginBottom = pageLayoutProperties.getMarginBottom();
            }
            if ( pageLayoutProperties.getMarginLeft() != null )
            {
                originMarginLeft = pageLayoutProperties.getMarginLeft();
            }
            if ( pageLayoutProperties.getMarginRight() != null )
            {
                originMarginRight = pageLayoutProperties.getMarginRight();
            }
            super.setMargins( originMarginLeft, originMarginRight, originMarginTop, originMarginBottom );

            // orientation
            PageOrientation orientation = pageLayoutProperties.getOrientation();
            if ( orientation != null )
            {
                super.setOrientation( orientation );
            }
        }

    }

    public Element getElement()
    {
        return null;
    }

    private void flushTable()
    {
        if ( layoutTable != null )
        {
            // force calculate height because it may be zero
            // and nothing will be flushed
            layoutTable.calculateHeights( true );
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
            }
        };
    }

}
