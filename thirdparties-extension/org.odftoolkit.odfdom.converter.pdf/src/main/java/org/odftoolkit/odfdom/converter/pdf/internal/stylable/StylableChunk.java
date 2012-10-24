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
package org.odftoolkit.odfdom.converter.pdf.internal.stylable;

import java.awt.Color;

import org.odftoolkit.odfdom.converter.pdf.internal.styles.Style;
import org.odftoolkit.odfdom.converter.pdf.internal.styles.StyleTextProperties;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;

public class StylableChunk
    extends Chunk
    implements IStylableElement
{
    private final StylableDocument ownerDocument;

    private final IStylableContainer parent;

    private Style lastStyleApplied = null;

    private boolean pageNumberChunk;

    public StylableChunk( StylableDocument ownerDocument, IStylableContainer parent, String textContent )
    {
        super( textContent );
        this.ownerDocument = ownerDocument;
        this.parent = parent;
    }

    public boolean isPageNumberChunk()
    {
        return pageNumberChunk;
    }

    public void setPageNumberChunk( boolean pageNumberChunk )
    {
        this.pageNumberChunk = pageNumberChunk;
    }

    @Override
    public String getContent()
    {
        return pageNumberChunk ? String.valueOf( ownerDocument.getPageNumber() ) : super.getContent();
    }

    public void applyStyles( Style style )
    {
        this.lastStyleApplied = style;

        StyleTextProperties textProperties = style.getTextProperties();
        if ( textProperties != null )
        {
            // font
            Font font = textProperties.getFont();
            if ( font != null )
            {
                super.setFont( font );
            }

            // background-color
            Color backgroundColor = textProperties.getBackgroundColor();
            if ( backgroundColor != null )
            {
                super.setBackground( backgroundColor );
            }

            // sub/super script
            Float textRise = textProperties.getTextPosition();
            if ( textRise != null )
            {
                super.setTextRise( textRise );
            }
        }
    }

    public Style getLastStyleApplied()
    {
        return lastStyleApplied;
    }

    public IStylableContainer getParent()
    {
        return parent;
    }

    public Element getElement()
    {
        return this;
    }
}
