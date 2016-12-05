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
package fr.opensagres.poi.xwpf.converter.core.openxmlformats.styles.run;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;

import fr.opensagres.poi.xwpf.converter.core.Color;
import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;

/**
 * 
 This element specifies a highlighting color which is applied as a background behind the contents of this run.
 * 
 * <pre>
 * <w:rPr> 
 *  <w:highlight w:val="yellow" /> 
 * </w:rPr>
 * </pre>
 * 
 * @see 17.3.2.15 highlight (Text Highlighting)
 */
public class RunTextHighlightingValueProvider
    extends AbstractRunValueProvider<Color>
{

    public static final RunTextHighlightingValueProvider INSTANCE = new RunTextHighlightingValueProvider();

    // See http://www.99colors.net/rgb-colors

    private static final Color DARK_BLUE = new Color( 0, 0, 128 );

    private static final Color DARK_YELLOW = new Color( 128, 128, 0 );

    private static final Color DARK_GREEN = new Color( 0, 51, 0 );

    private static final Color DARK_CYAN = new Color( 0, 139, 139 );

    private static final Color DARK_MAGENTA = new Color( 139, 0, 139 );

    private static final Color DARK_RED = new Color( 139, 0, 0 );

    @Override
    public Color getValue( CTRPr rPr, XWPFStylesDocument stylesDocument )
    {
        if ( rPr == null )
        {
            return null;
        }
        CTHighlight highlight = rPr.getHighlight();
        return getHighlight( highlight );
    }

    @Override
    public Color getValue( CTParaRPr rPr, XWPFStylesDocument stylesDocument )
    {
        if ( rPr == null )
        {
            return null;
        }
        CTHighlight highlight = rPr.getHighlight();
        return getHighlight( highlight );
    }

    private Color getHighlight( CTHighlight highlight )
    {
        if ( highlight == null )
        {
            return null;
        }
        org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor.Enum val = highlight.getVal();
        if ( STHighlightColor.BLACK.equals( val ) )
        {
            return Color.BLACK;
        }
        if ( STHighlightColor.BLUE.equals( val ) )
        {
            return Color.BLUE;
        }
        if ( STHighlightColor.CYAN.equals( val ) )
        {
            return Color.CYAN;
        }
        if ( STHighlightColor.DARK_BLUE.equals( val ) )
        {
            return DARK_BLUE;
        }
        if ( STHighlightColor.DARK_CYAN.equals( val ) )
        {
            return DARK_CYAN;
        }
        if ( STHighlightColor.DARK_GRAY.equals( val ) )
        {
            return Color.DARK_GRAY;
        }
        if ( STHighlightColor.DARK_GREEN.equals( val ) )
        {
            return DARK_GREEN;
        }
        if ( STHighlightColor.DARK_MAGENTA.equals( val ) )
        {
            return DARK_MAGENTA;
        }
        if ( STHighlightColor.DARK_RED.equals( val ) )
        {
            return DARK_RED;
        }
        if ( STHighlightColor.DARK_YELLOW.equals( val ) )
        {
            return DARK_YELLOW;
        }
        if ( STHighlightColor.GREEN.equals( val ) )
        {
            return Color.GREEN;
        }
        if ( STHighlightColor.LIGHT_GRAY.equals( val ) )
        {
            return Color.LIGHT_GRAY;
        }
        if ( STHighlightColor.MAGENTA.equals( val ) )
        {
            return Color.MAGENTA;
        }
        if ( STHighlightColor.NONE.equals( val ) )
        {
            return null;
        }
        if ( STHighlightColor.RED.equals( val ) )
        {
            return Color.RED;
        }
        if ( STHighlightColor.WHITE.equals( val ) )
        {
            return Color.WHITE;
        }
        if ( STHighlightColor.YELLOW.equals( val ) )
        {
            return Color.YELLOW;
        }
        return null;
    }

}
