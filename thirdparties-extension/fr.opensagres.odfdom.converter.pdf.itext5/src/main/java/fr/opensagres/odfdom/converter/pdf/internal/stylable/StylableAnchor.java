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

import java.util.List;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;

import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTextProperties;

public class StylableAnchor
    extends Anchor
    implements IStylableContainer
{
    private static final long serialVersionUID = 664309269352903329L;

    private final IStylableContainer parent;

    private Style lastStyleApplied = null;

    public StylableAnchor( IStylableFactory ownerDocument, IStylableContainer parent )
    {
        this.parent = parent;
    }

    public void addElement( Element element )
    {
        super.add( element );
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
                if ( !font.isUnderlined() )
                {
                    font = new Font( font );
                    font.setStyle( font.getStyle() | Font.UNDERLINE );
                }
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

    public Element getElement()
    {
        // underline font if not explicitly set
        List<Chunk> chunks = getChunks();
        for ( Chunk chunk : chunks )
        {
            Font f = chunk.getFont();
            if ( f != null && !f.isUnderlined() )
            {
                f = new Font( f );
                f.setStyle( f.getStyle() | Font.UNDERLINE );
                chunk.setFont( f );
            }
        }
        return this;
    }
}
