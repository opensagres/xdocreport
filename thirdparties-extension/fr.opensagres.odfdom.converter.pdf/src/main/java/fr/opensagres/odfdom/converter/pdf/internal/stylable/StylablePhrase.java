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

import java.util.ArrayList;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;

import fr.opensagres.odfdom.converter.core.utils.ODFUtils;
import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTextProperties;

public class StylablePhrase
    extends Phrase
    implements IStylableContainer
{
    private static final long serialVersionUID = 664309269352903329L;

    private IStylableContainer parent;

    private Style lastStyleApplied = null;

    public StylablePhrase( IStylableFactory ownerDocument, IStylableContainer parent )
    {
        this.parent = parent;
    }

    public void addElement( Element element )
    {
        // in function add(Element element) chunks are cloned
        // it is not correct for chunks with dynamic content (ie page number)
        // use function add(int index, Element element) because in this function chunks are added without cloning
        super.add( size(), element );
    }

    public void applyStyles( Style style )
    {
        this.lastStyleApplied = style;

        StyleTextProperties textProperties = style.getTextProperties();
        if ( textProperties != null )
        {
            // Font
            Font font = textProperties.getFont();
            if ( font != null )
            {
                super.setFont( font );
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

    @SuppressWarnings( "unchecked" )
    public Element getElement()
    {
        boolean empty = true;
        ArrayList<Chunk> chunks = getChunks();
        for ( Chunk chunk : chunks )
        {
            if ( chunk.getImage() == null && chunk.getContent() != null && chunk.getContent().length() > 0 )
            {
                empty = false;
                break;
            }
        }
        if ( empty )
        {
            super.add( new Chunk( ODFUtils.TAB_STR ) );
        }
        return this;
    }
}
