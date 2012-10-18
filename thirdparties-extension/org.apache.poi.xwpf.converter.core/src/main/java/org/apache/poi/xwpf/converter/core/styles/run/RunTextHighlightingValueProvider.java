package org.apache.poi.xwpf.converter.core.styles.run;

import java.awt.Color;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;

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

    private static final Color DARK_CYAN = new Color(0, 139, 139);

    private static final Color DARK_MAGENTA = new Color(139, 0, 139);

    private static final Color DARK_RED = new Color(139, 0, 0);

    @Override
    public Color getValue( CTRPr rPr )
    {
        if ( rPr == null )
        {
            return null;
        }
        CTHighlight highlight = rPr.getHighlight();
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
