/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package org.odftoolkit.odfdom.converter.internal.itext.stylable;

import java.io.OutputStream;
import java.util.List;

import org.odftoolkit.odfdom.converter.ODFConverterException;
import org.odftoolkit.odfdom.converter.internal.itext.StyleEngineForIText;
import org.odftoolkit.odfdom.converter.internal.itext.styles.Style;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleMargin;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StylePageLayoutProperties;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import fr.opensagres.xdocreport.itext.extension.ExtendedDocument;
import fr.opensagres.xdocreport.itext.extension.IParagraphFactory;
import fr.opensagres.xdocreport.itext.extension.MasterPage;
import fr.opensagres.xdocreport.itext.extension.PageOrientation;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
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

    private PdfPTable layoutTable;

    private ColumnText text;

    private int colIdx;

    public StylableDocument( OutputStream out, StyleEngineForIText styleEngine )
        throws DocumentException
    {
        super( out );
        this.styleEngine = styleEngine;
    }

    //
    // IStylableFactory, IParagraphFactory implementation
    //

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

    public StylableHeading createHeading( IStylableContainer parent, List<Integer> headingNumbering )
    {
        return new StylableHeading( this, parent, headingNumbering );
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

    public StylableDocumentSection createDocumentSection( IStylableContainer parent, boolean inHeaderFooter )
    {
        return new StylableDocumentSection( this, parent, inHeaderFooter );
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

    //
    // master page handling
    //

    @Override
    public void setActiveMasterPage( MasterPage masterPage )
    {
        // flush pending content
        flushTable();
        // activate master page in three steps
        Style style = getStyleMasterPage( (StylableMasterPage) masterPage );
        if ( style != null )
        {
            // step 1 - apply styles like page dimensions and orientation
            this.applyStyles( style );
        }
        // step 2 - set header/footer if any, it needs page dimensions from step 1
        super.setActiveMasterPage( masterPage );
        if ( activeMasterPage != null )
        {
            // set a flag used by addElement/newPage
            masterPageJustChanged = true;
        }
        activeMasterPage = (StylableMasterPage) masterPage;
        // step 3 - initialize column layout, it needs page dimensions which may be lowered by header/footer in step 2
        layoutTable = StylableDocumentSection.createLayoutTable( getPageWidth(), getAdjustedPageHeight(), style );
        text = StylableDocumentSection.createColumnText();
        setColIdx( 0 );
    }

    public StylableMasterPage getActiveMasterPage()
    {
        return activeMasterPage;
    }

    public Style getStyleMasterPage( StylableMasterPage masterPage )
    {
        Style style = styleEngine.getStyle( OdfStyleFamily.List.getName(), masterPage.getPageLayoutName() );
        return style;
    }

    @Override
    public StylableMasterPage getMasterPage( String masterPageName )
    {
        return (StylableMasterPage) super.getMasterPage( masterPageName );
    }

    @Override
    public StylableMasterPage getDefaultMasterPage()
    {
        return (StylableMasterPage) super.getDefaultMasterPage();
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
            StyleMargin margin = pageLayoutProperties.getMargin();
            if ( margin != null )
            {

                if ( margin.getMarginTop() != null )
                {
                    originMarginTop = margin.getMarginTop();
                }
                if ( margin.getMarginBottom() != null )
                {
                    originMarginBottom = margin.getMarginBottom();
                }
                if ( margin.getMarginRight() != null )
                {
                    originMarginRight = margin.getMarginRight();
                }
                if ( margin.getMarginLeft() != null )
                {
                    originMarginLeft = margin.getMarginLeft();
                }

                super.setMargins( originMarginLeft, originMarginRight, originMarginTop, originMarginBottom );
            }

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

    public IStylableContainer getParent()
    {
        return null;
    }

    public Element getElement()
    {
        return null;
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
            newPage();
        }
        text.addElement( element );
        StylableDocumentSection.getCell( layoutTable, colIdx ).getColumn().addElement( element );
        simulateText();
    }

    public void newColumn()
    {
        if ( colIdx + 1 < layoutTable.getNumberOfColumns() )
        {
            setColIdx( colIdx + 1 );
            simulateText();
        }
        else
        {
            newPage();
        }
    }

    @Override
    public boolean newPage()
    {
        if ( masterPageJustChanged )
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
            // document new page
            super.newPage();
            // initialize column layout for new page
            layoutTable = StylableDocumentSection.cloneAndClearTable( layoutTable );
            setColIdx( 0 );
            simulateText();
        }
        return true;
    }

    @Override
    public void close()
    {
        flushTable();
        super.close();
    }

    public float getCurrentColumnWidth()
    {
        PdfPCell cell = StylableDocumentSection.getCell( layoutTable, colIdx );
        return cell.getRight() - cell.getPaddingRight() - cell.getLeft() - cell.getPaddingLeft();
    }

    public float getCurrentColumnAvailableHeight()
    {
        // yLine is negative
        return StylableDocumentSection.getCell( layoutTable, colIdx ).getFixedHeight() + text.getYLine();
    }

    public float getPageWidth()
    {
        return right() - left();
    }

    public float getAdjustedPageHeight()
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
            throw new ODFConverterException( e );
        }
        if ( ColumnText.hasMoreText( res ) )
        {
            // text does not fit into current column
            // split it to a new column
            newColumn();
        }
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
                throw new ODFConverterException( e );
            }
        }
    }
}
