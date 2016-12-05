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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Element;
import com.lowagie.text.Font;

import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTextProperties;
import fr.opensagres.xdocreport.itext.extension.ExtendedChunk;
import fr.opensagres.xdocreport.itext.extension.font.FontGroup;

public class StylableChunk
    extends ExtendedChunk
    implements IStylableElement
{
    private final IStylableContainer parent;

    private final FontGroup fontGroup;

    private Style lastStyleApplied = null;

    public StylableChunk( StylableDocument ownerDocument, IStylableContainer parent, String textContent,
                          FontGroup fontGroup )
    {
        super( ownerDocument, textContent );
        this.parent = parent;
        this.fontGroup = fontGroup;
    }

    public void applyStyles( Style style )
    {
        this.lastStyleApplied = style;

        StyleTextProperties textProperties = style.getTextProperties();
        if ( textProperties != null )
        {
            // font
            Font font = null;
            if ( FontGroup.WESTERN.equals( fontGroup ) )
            {
                font = textProperties.getFont();
            }
            else if ( FontGroup.ASIAN.equals( fontGroup ) )
            {
                font = textProperties.getFontAsian();
            }
            else if ( FontGroup.COMPLEX.equals( fontGroup ) )
            {
                font = textProperties.getFontComplex();
            }
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

    public static List<StylableChunk> createChunks( StylableDocument ownerDocument, IStylableContainer parent,
                                                    String textContent )
    {
        List<StylableChunk> list = new ArrayList<StylableChunk>();
        StringBuilder sbuf = new StringBuilder();
        Font font = null;
        Font fontAsian = null;
        Font fontComplex = null;
        if ( parent != null && parent.getLastStyleApplied() != null )
        {
            StyleTextProperties textProperties = parent.getLastStyleApplied().getTextProperties();
            if ( textProperties != null )
            {
                font = textProperties.getFont();
                fontAsian = textProperties.getFontAsian();
                fontComplex = textProperties.getFontComplex();
            }
        }
        FontGroup currentGroup = FontGroup.WESTERN;
        for ( int i = 0; i < textContent.length(); i++ )
        {
            char ch = textContent.charAt( i );
            FontGroup group = FontGroup.getUnicodeGroup( ch, font, fontAsian, fontComplex );
            if ( sbuf.length() == 0 || currentGroup.equals( group ) )
            {
                // continue current chunk
                sbuf.append( ch );
            }
            else
            {
                // end chunk
                StylableChunk chunk = ownerDocument.createChunk( parent, sbuf.toString(), currentGroup );
                list.add( chunk );
                // start new chunk
                sbuf.setLength( 0 );
                sbuf.append( ch );
            }
            currentGroup = group;
        }
        // end chunk
        StylableChunk chunk = ownerDocument.createChunk( parent, sbuf.toString(), currentGroup );
        list.add( chunk );
        return list;
    }
}
