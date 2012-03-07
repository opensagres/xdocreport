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

import org.odftoolkit.odfdom.converter.internal.itext.StyleEngineForIText;
import org.odftoolkit.odfdom.converter.internal.itext.styles.Style;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleMargin;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StylePageLayoutProperties;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;

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

    private boolean masterPageActivated;

    private boolean implicitPageBreakAfterMasterPageChange;

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

    //
    // master page handling
    //

    @Override
    public void setActiveMasterPage( MasterPage masterPage )
    {
        Style style = getStyleMasterPage( (StylableMasterPage) masterPage );
        if ( style != null )
        {
            this.applyStyles( style );
        }
        super.setActiveMasterPage( masterPage );
        if ( masterPageActivated )
        {
            implicitPageBreakAfterMasterPageChange = true;
        }
        masterPageActivated = true;
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

    public void addElement( Element element )
    {
        try
        {
            if ( !super.isOpen() )
            {
                super.open();
            }
            if ( implicitPageBreakAfterMasterPageChange )
            {
                // master page was changed but there was no explicit page break
                newPage();
            }
            super.add( element );
        }
        catch ( DocumentException e )
        {
            e.printStackTrace();
        }
    }

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

    @Override
    public boolean newPage()
    {
        implicitPageBreakAfterMasterPageChange = false;
        return super.newPage();
    }

}
