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

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import fr.opensagres.odfdom.converter.core.utils.ODFUtils;
import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleBreak;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleLineHeight;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleParagraphProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTextProperties;
import fr.opensagres.xdocreport.openpdf.extension.ExtendedParagraph;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * fixes for paragraph pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StylableParagraph
    extends ExtendedParagraph
    implements IStylableContainer
{
    private static final long serialVersionUID = 664309269352903329L;

    public static final float DEFAULT_LINE_HEIGHT = 1.0f;

    private final StylableDocument ownerDocument;

    private IStylableContainer parent;

    private Style lastStyleApplied = null;

    private boolean elementPostProcessed = false;

    public StylableParagraph( StylableDocument ownerDocument, IStylableContainer parent )
    {
        super();
        this.ownerDocument = ownerDocument;
        this.parent = parent;
        super.setMultipliedLeading( DEFAULT_LINE_HEIGHT );
    }

    public StylableParagraph( StylableDocument ownerDocument, Paragraph title, IStylableContainer parent )
    {
        super( title );
        this.ownerDocument = ownerDocument;
        this.parent = parent;
        super.setMultipliedLeading( DEFAULT_LINE_HEIGHT );
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
        }

        StyleParagraphProperties paragraphProperties = style.getParagraphProperties();
        if ( paragraphProperties != null )
        {
            // break-before
            StyleBreak breakBefore = paragraphProperties.getBreakBefore();
            if ( breakBefore != null )
            {
                handleBreak( breakBefore );
            }

            // alignment
            int alignment = paragraphProperties.getAlignment();
            if ( alignment != Element.ALIGN_UNDEFINED )
            {
                super.setAlignment( alignment );
            }

            // first line indentation
            Boolean autoTextIndent = paragraphProperties.getAutoTextIndent();
            if ( Boolean.TRUE.equals( autoTextIndent ) )
            {
                float fontSize = font != null ? font.getCalculatedSize() : Font.DEFAULTSIZE;
                super.setFirstLineIndent( 1.3f * fontSize );
            }
            else
            {
                Float textIndent = paragraphProperties.getTextIndent();
                if ( textIndent != null)
                {
                    // text indent can be negative. 
                    // See https://code.google.com/p/xdocreport/issues/detail?id=366 
                    super.setFirstLineIndent( textIndent );
                }
            }

            // line height
            StyleLineHeight lineHeight = paragraphProperties.getLineHeight();
            if ( lineHeight != null && lineHeight.getLineHeight() != null )
            {
                if ( lineHeight.isLineHeightProportional() )
                {
                    super.setMultipliedLeading( lineHeight.getLineHeight() );
                }
                else
                {
                    super.setLeading( lineHeight.getLineHeight() );
                }
            }

            // keep together on the same page
            Boolean keepTogether = paragraphProperties.getKeepTogether();
            if ( keepTogether != null )
            {
                super.setKeepTogether( keepTogether );
            }
        }
    }

    private void handleBreak( StyleBreak styleBreak )
    {
        if ( styleBreak.isColumnBreak() || styleBreak.isPageBreak() )
        {
            IBreakHandlingContainer b = StylableDocumentSection.getIBreakHandlingContainer( parent );
            if ( b != null )
            {
                if ( styleBreak.isColumnBreak() )
                {
                    b.columnBreak();
                }
                else if ( styleBreak.isPageBreak() )
                {
                    b.pageBreak();
                }
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

    public StylableDocument getOwnerDocument()
    {
        return ownerDocument;
    }

    public static Chunk createAdjustedChunk( String content, Font font, float lineHeight, boolean lineHeightProportional )
    {
        // adjust chunk attributes like text rise
        // use StylableParagraph mechanism
        StylableParagraph p = new StylableParagraph( null, null );
        p.setFont( font );
        if ( lineHeightProportional )
        {
            p.setMultipliedLeading( lineHeight );
        }
        else
        {
            p.setLeading( lineHeight );
        }
        p.addElement( new Chunk( content, font ) );
        p.getElement(); // post-processing here
        return (Chunk) p.getChunks().get( 0 );
    }

    @Override
    public Element getElement()
    {
        if ( !elementPostProcessed )
        {
            elementPostProcessed = true;
            postProcessEmptyParagraph();
            postProcessBookmarks();
            postProcessLineHeightAndBaseline();
        }
        return super.getElement();
    }

    @SuppressWarnings( "unchecked" )
    private void postProcessEmptyParagraph()
    {
        // add space if this paragraph is empty
        // otherwise its height will be zero
        boolean empty = true;
        ArrayList<Element> elements = getChunks();
        for ( Element element : elements )
        {
            Chunk chunk = (Chunk)element;
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
    }

    @SuppressWarnings( "unchecked" )
    private void postProcessBookmarks()
    {
        // add space if last chunk is a bookmark
        // otherwise the bookmark will disappear from pdf
        ArrayList<Element> elements = getChunks();
        if ( elements.size() > 0 )
        {

            Chunk lastChunk = (Chunk) elements.get( elements.size() - 1 );
            String localDestination = null;
            if ( lastChunk.getChunkAttributes() != null )
            {
                localDestination = (String) lastChunk.getChunkAttributes().get( Chunk.LOCALDESTINATION );
            }
            if ( localDestination != null )
            {
                super.add( new Chunk( ODFUtils.NON_BREAKING_SPACE_STR ) );
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    private void postProcessLineHeightAndBaseline()
    {
        // adjust line height and baseline
        Font font = getMostOftenUsedFont();
        if ( font == null || font.getBaseFont() == null )
        {
            font = this.font;
        }
        if ( font != null && font.getBaseFont() != null )
        {
            // iText and open office computes proportional line height differently
            // [iText] line height = coefficient * font size
            // [open office] line height = coefficient * (font ascender + font descender + font extra margin)
            // we have to increase paragraph line height to generate pdf similar to open office document
            // this algorithm may be inaccurate if fonts with different multipliers are used in this paragraph
            float size = font.getSize();
            float ascender = font.getBaseFont().getFontDescriptor( BaseFont.AWT_ASCENT, size );
            float descender = -font.getBaseFont().getFontDescriptor( BaseFont.AWT_DESCENT, size ); // negative value
            float margin = font.getBaseFont().getFontDescriptor( BaseFont.AWT_LEADING, size );
            float multiplier = ( ascender + descender + margin ) / size;
            if ( multipliedLeading > 0.0f )
            {
                setMultipliedLeading( getMultipliedLeading() * multiplier );
            }

            // iText seems to output text with baseline lower than open office
            // we raise all paragraph text by some amount
            // again this may be inaccurate if fonts with different size are used in this paragraph
            float itextdescender = -font.getBaseFont().getFontDescriptor( BaseFont.DESCENT, size ); // negative
            float textRise = itextdescender + getTotalLeading() - font.getSize() * multiplier;
            ArrayList<Element> elements = getChunks();
            for ( Element element : elements )
            {
                Chunk chunk = (Chunk)element;
                Font f = chunk.getFont();
                if ( f != null )
                {
                    // have to raise underline and strikethru as well
                    float s = f.getSize();
                    if ( f.isUnderlined() )
                    {
                        f.setStyle( f.getStyle() & ~Font.UNDERLINE );
                        chunk.setUnderline( s * 1 / 17, s * -1 / 7 + textRise );
                    }
                    if ( f.isStrikethru() )
                    {
                        f.setStyle( f.getStyle() & ~Font.STRIKETHRU );
                        chunk.setUnderline( s * 1 / 17, s * 1 / 4 + textRise );
                    }
                }
                chunk.setTextRise( chunk.getTextRise() + textRise );
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    private Font getMostOftenUsedFont()
    {
        // determine font most often used in this paragraph
        // font with the highest count of non-whitespace characters
        // is considered to be most often used
        Map<String, Font> fontMap = new LinkedHashMap<String, Font>();
        Map<String, Integer> countMap = new LinkedHashMap<String, Integer>();
        Font mostUsedFont = null;
        int mostUsedCount = -1;
        ArrayList<Element> elements = getChunks();
        for ( Element element : elements )
        {
            Chunk chunk = (Chunk) element;
            Font font = chunk.getFont();
            int count = 0;
            String text = chunk.getContent();
            if ( text != null )
            {
                // count non-whitespace characters in a chunk
                for ( int i = 0; i < text.length(); i++ )
                {
                    char ch = text.charAt( i );
                    if ( !Character.isWhitespace( ch ) )
                    {
                        count++;
                    }
                }
            }
            if ( font != null )
            {
                // update font and its count
                String fontKey = font.getFamilyname() + "_" + (int) font.getSize();
                Font fontTmp = fontMap.get( fontKey );
                if ( fontTmp == null )
                {
                    fontMap.put( fontKey, font );
                }
                Integer countTmp = countMap.get( fontKey );
                int totalCount = countTmp == null ? count : countTmp + count;
                countMap.put( fontKey, totalCount );
                // update most used font
                if ( totalCount > mostUsedCount )
                {
                    mostUsedCount = totalCount;
                    mostUsedFont = font;
                }
            }
        }
        return mostUsedFont;
    }
}
