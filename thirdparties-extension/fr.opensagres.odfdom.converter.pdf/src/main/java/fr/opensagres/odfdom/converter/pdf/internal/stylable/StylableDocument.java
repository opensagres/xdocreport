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
package fr.opensagres.odfdom.converter.pdf.internal.stylable;

import java.io.OutputStream;
import java.util.List;

import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import fr.opensagres.odfdom.converter.core.ODFConverterException;
import fr.opensagres.odfdom.converter.pdf.internal.StyleEngineForIText;
import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StylePageLayoutProperties;
import fr.opensagres.xdocreport.itext.extension.ExtendedDocument;
import fr.opensagres.xdocreport.itext.extension.IMasterPage;
import fr.opensagres.xdocreport.itext.extension.IPdfWriterConfiguration;
import fr.opensagres.xdocreport.itext.extension.PageOrientation;
import fr.opensagres.xdocreport.itext.extension.font.FontGroup;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StylableDocument
    extends ExtendedDocument
    implements IStylableContainer, IBoundsLimitContainer, IBreakHandlingContainer, IStylableFactory
{
    private final StyleEngineForIText styleEngine;

    private Style lastStyleApplied = null;

    private StylableMasterPage activeMasterPage;

    private boolean masterPageJustChanged;

    private boolean documentEmpty = true;

    private PdfPTable layoutTable;

    private ColumnText text;

    private int colIdx;

    public StylableDocument( OutputStream out, IPdfWriterConfiguration configuration, StyleEngineForIText styleEngine )
        throws DocumentException
    {
        super( out, configuration );
        this.styleEngine = styleEngine;
    }

    //
    // IStylableFactory implementation
    //

    public StylableAnchor createAnchor( IStylableContainer parent )
    {
        return new StylableAnchor( this, parent );
    }

    public StylableChunk createChunk( IStylableContainer parent, String textContent, FontGroup fontGroup )
    {
        return new StylableChunk( this, parent, textContent, fontGroup );
    }

    public StylableDocumentSection createDocumentSection( IStylableContainer parent, boolean inHeaderFooter )
    {
        return new StylableDocumentSection( this, parent, inHeaderFooter );
    }

    public StylableHeaderFooter createHeaderFooter( boolean header )
    {
        return new StylableHeaderFooter( this, header );
    }

    public StylableHeading createHeading( IStylableContainer parent, List<Integer> headingNumbering )
    {
        return new StylableHeading( this, parent, headingNumbering );
    }

    public StylableImage createImage( IStylableContainer parent, Image image, Float x, Float y, Float width,
                                      Float height )
    {
        return new StylableImage( this, parent, image, x, y, width, height );
    }

    public StylableList createList( IStylableContainer parent, int listLevel )
    {
        return new StylableList( this, parent, listLevel );
    }

    public StylableListItem createListItem( IStylableContainer parent )
    {
        return new StylableListItem( this, parent );
    }

    public StylableParagraph createParagraph( IStylableContainer parent )
    {
        return new StylableParagraph( this, parent );
    }

    public StylablePhrase createPhrase( IStylableContainer parent )
    {
        return new StylablePhrase( this, parent );
    }

    public StylableTab createTab( IStylableContainer parent, boolean inTableOfContent )
    {
        return new StylableTab( this, parent, inTableOfContent );
    }

    public StylableTable createTable( IStylableContainer parent, int numColumns )
    {
        return new StylableTable( this, parent, numColumns );
    }

    public StylableTableCell createTableCell( IStylableContainer parent )
    {
        return new StylableTableCell( this, parent );
    }

    //
    // master page handling
    //

    @Override
    public void setActiveMasterPage( IMasterPage masterPage )
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
            // set a flag used by addElement/pageBreak
            masterPageJustChanged = true;
        }
        activeMasterPage = (StylableMasterPage) masterPage;
        // step 3 - initialize column layout, it needs page dimensions which may be lowered by header/footer in step 2
        layoutTable = StylableDocumentSection.createLayoutTable( getPageWidth(), getAdjustedPageHeight(), style );
        text = StylableDocumentSection.createColumnText();
        setColIdx( 0 );
    }

    private Style setNextActiveMasterPageIfNecessary()
    {
        // called on page break
        // return new page style if changed
        if ( activeMasterPage != null )
        {
            String nextMasterPageStyleName = activeMasterPage.getNextStyleName();
            if ( nextMasterPageStyleName != null && nextMasterPageStyleName.length() > 0 )
            {
                StylableMasterPage nextMasterPage = getMasterPage( nextMasterPageStyleName );
                if ( nextMasterPage != null )
                {
                    // activate next master page
                    Style style = getStyleMasterPage( nextMasterPage );
                    if ( style != null )
                    {
                        // step 1 - apply styles like page dimensions and orientation
                        this.applyStyles( style );
                    }
                    // step 2 - set header/footer if any, it needs page dimensions from step 1
                    super.setActiveMasterPage( nextMasterPage );
                    //
                    activeMasterPage = nextMasterPage;
                    return style;
                }
            }
        }
        return null;
    }

    public StylableMasterPage getActiveMasterPage()
    {
        return activeMasterPage;
    }

    public Style getStyleMasterPage( StylableMasterPage masterPage )
    {
        Style style = styleEngine.getStyle( OdfStyleFamily.List.getName(), masterPage.getPageLayoutName(), null );
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

    boolean first = true;

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
//        if (first) {
//        	first = false;
//        	try {
//				Image img = Image.getInstance("/Users/ben/Documents/1000000000000028000001135F95BFF1.png");
//				img.setAbsolutePosition(0f, 0f);
//				this.add(img);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
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
            Style nextStyle = setNextActiveMasterPageIfNecessary();
            // document new page
            super.newPage();
            // initialize column layout for new page
            if ( nextStyle == null )
            {
                // ordinary page break
                layoutTable = StylableDocumentSection.cloneAndClearTable( layoutTable, false );
            }
            else
            {
                // page break with new master page activation
                // style changed so recreate table
                layoutTable =
                    StylableDocumentSection.createLayoutTable( getPageWidth(), getAdjustedPageHeight(), nextStyle );
            }
            setColIdx( 0 );
            simulateText();
        }
    }

    @Override
    public boolean newPage()
    {
        throw new ODFConverterException( "internal error - do not call newPage directly" );
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
            throw new ODFConverterException( e );
        }
        if ( ColumnText.hasMoreText( res ) )
        {
            // text does not fit into current column
            // split it to a new column
            columnBreak();
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
