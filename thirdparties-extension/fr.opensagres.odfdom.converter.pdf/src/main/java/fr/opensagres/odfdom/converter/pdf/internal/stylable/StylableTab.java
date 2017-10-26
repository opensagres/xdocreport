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

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.draw.DrawInterface;

import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleLineHeight;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleParagraphProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTabStopProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTextProperties;

public class StylableTab
    implements IStylableElement, DrawInterface
{
    private IStylableContainer parent;

    private boolean inTableOfContent;

    private Style lastStyleApplied = null;

    private Font font = new Font();

    private float lineHeight = StylableParagraph.DEFAULT_LINE_HEIGHT;

    private boolean lineHeightProportional = true;

    private String leaderText = " ";

    private Chunk chunk;

    public StylableTab( StylableDocument ownerDocument, IStylableContainer parent, boolean inTableOfContent )
    {
        this.parent = parent;
        this.inTableOfContent = inTableOfContent;
    }

    public void applyStyles( Style style )
    {
        // iText is not able to handle tab stops - it's an architectural limitation
        // we don't know our x coordinate in current line to determine which tab stop to use
        // neither we know width of text remaining to the end of current line (to handle tab stop alignment)

        // use some heuristics
        // translate tabs to iText separators in table of content
        // most often it has one tab stop right aligned to the end of line
        // in ordinary text replace tab by space, the results may be not as expected

        this.lastStyleApplied = style;

        StyleTextProperties textProperties = style.getTextProperties();
        if ( textProperties != null )
        {
            // Font
            Font font = textProperties.getFont();
            if ( font != null )
            {
                this.font = font;
            }
        }

        StyleParagraphProperties paragraphProperties = style.getParagraphProperties();
        if ( paragraphProperties != null )
        {
            // line height
            StyleLineHeight lineHeight = paragraphProperties.getLineHeight();
            if ( lineHeight != null && lineHeight.getLineHeight() != null )
            {
                this.lineHeight = lineHeight.getLineHeight();
                this.lineHeightProportional = lineHeight.isLineHeightProportional();
            }
        }

        List<StyleTabStopProperties> tabStopPropertiesList = style.getTabStopPropertiesList();
        if ( tabStopPropertiesList != null && !tabStopPropertiesList.isEmpty() )
        {
            // we don't know which one to use, so use the first one
            StyleTabStopProperties tabStopProperties = tabStopPropertiesList.get( 0 );

            String leaderText = tabStopProperties.getLeaderText();
            if ( leaderText != null )
            {
                this.leaderText = leaderText;
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
        if ( chunk == null )
        {
            if ( inTableOfContent )
            {
                chunk = new Chunk( this );
            }
            else
            {
                chunk = new Chunk( leaderText, font );
            }
        }
        return chunk;
    }

    public void draw( PdfContentByte canvas, float llx, float lly, float urx, float ury, float y )
    {
        if ( font.getBaseFont() != null && font.getSize() > 0.0 && leaderText.trim().length() > 0 )
        {
            // create text to fit tab width
            float width = urx - llx;
            String txt = "";
            while ( true )
            {
                Chunk tmp = new Chunk( txt + leaderText, font );
                if ( tmp.getWidthPoint() <= width )
                {
                    txt += leaderText;
                }
                else
                {
                    break;
                }
            }
            // compute x offset - as if tab were right aligned
            float xoffset = width - new Chunk( txt, font ).getWidthPoint();
            // compute y offset - use StylableParagraph mechanism
            Chunk tmp = StylableParagraph.createAdjustedChunk( txt, font, lineHeight, lineHeightProportional );
            float yoffset = tmp.getTextRise();
            // draw
            canvas.saveState();
            canvas.beginText();
            canvas.setFontAndSize( font.getBaseFont(), font.getSize() );
            canvas.showTextAligned( Element.ALIGN_LEFT, txt, llx + xoffset, y + yoffset, 0.0f );
            canvas.endText();
            canvas.restoreState();
        }
    }
}
