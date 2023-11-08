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
package fr.opensagres.xdocreport.document.textstyling.html;

import fr.opensagres.xdocreport.document.textstyling.properties.Color;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Colors in CSS can be defined in a few different ways:
 * - As hexadecimal values (supported)
 * - As RGB values (supported)
 * - By color names (supported)
 * - As HSL values (CSS3) (supported)
 * - As HWB values (CSS4) (unsupported)
 * - With the currentcolor keyword (unsupported)
 */
public class HTMLColorParser
{
    private static final Map<String, String> COLORNAME_TO_HEXADECIMAL_VALUE = Collections.unmodifiableMap(getColornameToHexMapping());

    private static Map<String, String> getColornameToHexMapping() {
        final Map<String, String> colornameToHexMapping = new HashMap<>();
        colornameToHexMapping.put("aliceblue", "#F0F8FF");
        colornameToHexMapping.put("antiquewhite", "#FAEBD7");
        colornameToHexMapping.put("aqua", "#00FFFF");
        colornameToHexMapping.put("aquamarine", "#7FFFD4");
        colornameToHexMapping.put("azure", "#F0FFFF");
        colornameToHexMapping.put("beige", "#F5F5DC");
        colornameToHexMapping.put("bisque", "#FFE4C4");
        colornameToHexMapping.put("black", "#000000");
        colornameToHexMapping.put("blanchedalmond", "#FFEBCD");
        colornameToHexMapping.put("blue", "#0000FF");
        colornameToHexMapping.put("blueviolet", "#8A2BE2");
        colornameToHexMapping.put("brown", "#A52A2A");
        colornameToHexMapping.put("burlywood", "#DEB887");
        colornameToHexMapping.put("cadetblue", "#5F9EA0");
        colornameToHexMapping.put("chartreuse", "#7FFF00");
        colornameToHexMapping.put("chocolate", "#D2691E");
        colornameToHexMapping.put("coral", "#FF7F50");
        colornameToHexMapping.put("cornflowerblue", "#6495ED");
        colornameToHexMapping.put("cornsilk", "#FFF8DC");
        colornameToHexMapping.put("crimson", "#DC143C");
        colornameToHexMapping.put("cyan", "#00FFFF");
        colornameToHexMapping.put("darkblue", "#00008B");
        colornameToHexMapping.put("darkcyan", "#008B8B");
        colornameToHexMapping.put("darkgoldenrod", "#B8860B");
        colornameToHexMapping.put("darkgray", "#A9A9A9");
        colornameToHexMapping.put("darkgreen", "#006400");
        colornameToHexMapping.put("darkkhaki", "#BDB76B");
        colornameToHexMapping.put("darkmagenta", "#8B008B");
        colornameToHexMapping.put("darkolivegreen", "#556B2F");
        colornameToHexMapping.put("darkorange", "#FF8C00");
        colornameToHexMapping.put("darkorchid", "#9932CC");
        colornameToHexMapping.put("darkred", "#8B0000");
        colornameToHexMapping.put("darksalmon", "#E9967A");
        colornameToHexMapping.put("darkseagreen", "#8FBC8F");
        colornameToHexMapping.put("darkslateblue", "#483D8B");
        colornameToHexMapping.put("darkslategray", "#2F4F4F");
        colornameToHexMapping.put("darkturquoise", "#00CED1");
        colornameToHexMapping.put("darkviolet", "#9400D3");
        colornameToHexMapping.put("deeppink", "#FF1493");
        colornameToHexMapping.put("deepskyblue", "#00BFFF");
        colornameToHexMapping.put("dimgray", "#696969");
        colornameToHexMapping.put("dodgerblue", "#1E90FF");
        colornameToHexMapping.put("firebrick", "#B22222");
        colornameToHexMapping.put("floralwhite", "#FFFAF0");
        colornameToHexMapping.put("forestgreen", "#228B22");
        colornameToHexMapping.put("fuchsia", "#FF00FF");
        colornameToHexMapping.put("gainsboro", "#DCDCDC");
        colornameToHexMapping.put("ghostwhite", "#F8F8FF");
        colornameToHexMapping.put("gold", "#FFD700");
        colornameToHexMapping.put("goldenrod", "#DAA520");
        colornameToHexMapping.put("gray", "#808080");
        colornameToHexMapping.put("green", "#008000");
        colornameToHexMapping.put("greenyellow", "#ADFF2F");
        colornameToHexMapping.put("honeydew", "#F0FFF0");
        colornameToHexMapping.put("hotpink", "#FF69B4");
        colornameToHexMapping.put("indianred", "#CD5C5C");
        colornameToHexMapping.put("indigo", "#4B0082");
        colornameToHexMapping.put("ivory", "#FFFFF0");
        colornameToHexMapping.put("khaki", "#F0E68C");
        colornameToHexMapping.put("lavender", "#E6E6FA");
        colornameToHexMapping.put("lavenderblush", "#FFF0F5");
        colornameToHexMapping.put("lawngreen", "#7CFC00");
        colornameToHexMapping.put("lemonchiffon", "#FFFACD");
        colornameToHexMapping.put("lightblue", "#ADD8E6");
        colornameToHexMapping.put("lightcoral", "#F08080");
        colornameToHexMapping.put("lightcyan", "#E0FFFF");
        colornameToHexMapping.put("lightgoldenrodyellow", "#FAFAD2");
        colornameToHexMapping.put("lightgray", "#D3D3D3");
        colornameToHexMapping.put("lightgreen", "#90EE90");
        colornameToHexMapping.put("lightpink", "#FFB6C1");
        colornameToHexMapping.put("lightsalmon", "#FFA07A");
        colornameToHexMapping.put("lightseagreen", "#20B2AA");
        colornameToHexMapping.put("lightskyblue", "#87CEFA");
        colornameToHexMapping.put("lightslategray", "#778899");
        colornameToHexMapping.put("lightsteelblue", "#B0C4DE");
        colornameToHexMapping.put("lightyellow", "#FFFFE0");
        colornameToHexMapping.put("lime", "#00FF00");
        colornameToHexMapping.put("limegreen", "#32CD32");
        colornameToHexMapping.put("linen", "#FAF0E6");
        colornameToHexMapping.put("maroon", "#800000");
        colornameToHexMapping.put("mediumaquamarine", "#66CDAA");
        colornameToHexMapping.put("mediumblue", "#0000CD");
        colornameToHexMapping.put("mediumorchid", "#BA55D3");
        colornameToHexMapping.put("mediumpurple", "#9370DB");
        colornameToHexMapping.put("mediumseagreen", "#3CB371");
        colornameToHexMapping.put("mediumslateblue", "#7B68EE");
        colornameToHexMapping.put("mediumspringgreen", "#00FA9A");
        colornameToHexMapping.put("mediumturquoise", "#48D1CC");
        colornameToHexMapping.put("mediumvioletred", "#C71585");
        colornameToHexMapping.put("midnightblue", "#191970");
        colornameToHexMapping.put("mintcream", "#F5FFFA");
        colornameToHexMapping.put("mistyrose", "#FFE4E1");
        colornameToHexMapping.put("moccasin", "#FFE4B5");
        colornameToHexMapping.put("navajowhite", "#FFDEAD");
        colornameToHexMapping.put("navy", "#000080");
        colornameToHexMapping.put("oldlace", "#FDF5E6");
        colornameToHexMapping.put("olive", "#808000");
        colornameToHexMapping.put("olivedrab", "#6B8E23");
        colornameToHexMapping.put("orange", "#FFA500");
        colornameToHexMapping.put("orangered", "#FF4500");
        colornameToHexMapping.put("orchid", "#DA70D6");
        colornameToHexMapping.put("palegoldenrod", "#EEE8AA");
        colornameToHexMapping.put("palegreen", "#98FB98");
        colornameToHexMapping.put("paleturquoise", "#AFEEEE");
        colornameToHexMapping.put("palevioletred", "#DB7093");
        colornameToHexMapping.put("papayawhip", "#FFEFD5");
        colornameToHexMapping.put("peachpuff", "#FFDAB9");
        colornameToHexMapping.put("peru", "#CD853F");
        colornameToHexMapping.put("pink", "#FFC0CB");
        colornameToHexMapping.put("plum", "#DDA0DD");
        colornameToHexMapping.put("powderblue", "#B0E0E6");
        colornameToHexMapping.put("purple", "#800080");
        colornameToHexMapping.put("red", "#FF0000");
        colornameToHexMapping.put("rosybrown", "#BC8F8F");
        colornameToHexMapping.put("royalblue", "#4169E1");
        colornameToHexMapping.put("saddlebrown", "#8B4513");
        colornameToHexMapping.put("salmon", "#FA8072");
        colornameToHexMapping.put("sandybrown", "#F4A460");
        colornameToHexMapping.put("seagreen", "#2E8B57");
        colornameToHexMapping.put("seashell", "#FFF5EE");
        colornameToHexMapping.put("sienna", "#A0522D");
        colornameToHexMapping.put("silver", "#C0C0C0");
        colornameToHexMapping.put("skyblue", "#87CEEB");
        colornameToHexMapping.put("slateblue", "#6A5ACD");
        colornameToHexMapping.put("slategray", "#708090");
        colornameToHexMapping.put("snow", "#FFFAFA");
        colornameToHexMapping.put("springgreen", "#00FF7F");
        colornameToHexMapping.put("steelblue", "#4682B4");
        colornameToHexMapping.put("tan", "#D2B48C");
        colornameToHexMapping.put("teal", "#008080");
        colornameToHexMapping.put("thistle", "#D8BFD8");
        colornameToHexMapping.put("tomato", "#FF6347");
        colornameToHexMapping.put("turquoise", "#40E0D0");
        colornameToHexMapping.put("violet", "#EE82EE");
        colornameToHexMapping.put("wheat", "#F5DEB3");
        colornameToHexMapping.put("white", "#FFFFFF");
        colornameToHexMapping.put("whitesmoke", "#F5F5F5");
        colornameToHexMapping.put("yellow", "#FFFF00");
        colornameToHexMapping.put("yellowgreen", "#9ACD32");
        return colornameToHexMapping;
    }

    /**
     * @param colorString an rgb color string
     *                    i.e. rgb(255,0,0);
     * @return null if colorString invalid or not implemented, so that invalid arguments won't crash the program
     */
    public static Color parse(final String colorString )
    {
        if ( colorString == null || colorString.isEmpty() )
        {
            // invalid arguments won't crash the program and are ignored...
            return null;
        }

        // rgb( 123, 23, 1)
        if ( colorString.startsWith( "rgb" ) )
        {
            return parseRgb( colorString );
        }
        // #FFCCAA
        else if ( colorString.startsWith( "#" ) && colorString.length() == 7 )
        {
            return parseHexadecimalValues( colorString );
        }
        else if ( colorString.startsWith( "hsl" ) )
        {
            return parseHsl( colorString );
        }
        else if ( isColorname(colorString) )
        {
            return parseHexadecimalValues( colornameToHexadecimalValue( colorString ) );
        }
        else
        {
            // other formats not implemented
            return null;
        }
    }

    private static Color parseRgb(final String colorString )
    {
        final String color = colorString.replaceAll( "[^\\d,]*", "" );
        final String[] rgbColor = color.split( "," );
        if ( rgbColor.length == 3 )
        {
            try
            {
                return new Color(
                        Integer.parseInt( rgbColor[0] ),
                        Integer.parseInt( rgbColor[1] ),
                        Integer.parseInt( rgbColor[2] ) );
            }
            catch ( final Exception e )
            {
                // invalid arguments will be ignored
                return null;
            }
        }
        else
        {
            // invalid arguments will be ignored
            // opacity / alpha value not implemented yet
            return null;
        }
    }

    private static Color parseHsl(final String colorString )
    {
        final String color = colorString.replaceAll( "[^\\d,]*", "" );
        final String[] hslColor = color.split( "," );
        if ( hslColor.length == 3 )
        {
            try
            {
                final int[] rgbColor = hslToRgb(new int[] {
                        Integer.parseInt( hslColor[0] ),
                        Integer.parseInt( hslColor[1] ),
                        Integer.parseInt( hslColor[2] )
                });
                return new Color( rgbColor[0], rgbColor[1], rgbColor[2] );
            }
            catch ( final Exception e )
            {
                // invalid arguments will be ignored
                return null;
            }
        }
        else
        {
            // invalid arguments will be ignored
            // opacity / alpha value not implemented yet
            return null;
        }
    }

    private static Color parseHexadecimalValues(final String colorString )
    {
        final String red = colorString.substring( 1, 3 );
        final String green = colorString.substring( 3, 5 );
        final String blue = colorString.substring( 5, 7 );
        try
        {
            return new Color(
                    Integer.parseInt( red, 16 ),
                    Integer.parseInt( green, 16 ),
                    Integer.parseInt( blue, 16 ) );
        }
        catch ( final Exception e )
        {
            // invalid arguments will be ignored
            return null;
        }
    }

    private static boolean isColorname(final String colorValueCssText)
    {
        return COLORNAME_TO_HEXADECIMAL_VALUE.containsKey(colorValueCssText.toLowerCase());
    }

    private static String colornameToHexadecimalValue(final String colorValueCssText)
    {
        return COLORNAME_TO_HEXADECIMAL_VALUE.get(colorValueCssText.toLowerCase());
    }

    /**
     * Converts an HSL color value to RGB. Conversion formula
     * adapted from <a href="http://en.wikipedia.org/wiki/HSL_color_space"></a>.
     * Assumes h, s, and l are contained in the set [0, 360] for h and [0, 100] for sl and
     * returns r, g, and b in the set [0, 255].
     *
     * @param hsl HSL color
     * @return RGB representation
     */
    static int[] hslToRgb(final int[] hsl) {
        final float h = (float) (hsl[0] % 360) / 360;
        final float s = (float) hsl[1] / 100;
        final float l = (float) hsl[2] / 100;

        final int r;
        final int g;
        final int b;

        if (s == 0) {
            // achromatic
            r = (int) (l * 255);
            g = (int) (l * 255);
            b = (int) (l * 255);
        } else {
            final float q = l < 0.5 ? l * (1 + s) : (l + s) - (l * s);
            final float p = 2 * l - q;
            r = (int) (hue2rgb(p, q, h + (1.0f / 3.0f)) * 255);
            g = (int) (hue2rgb(p, q, h) * 255);
            b = (int) (hue2rgb(p, q, h - (1.0f / 3.0f)) * 255);
        }

        return new int[] { r, g, b };
    }

    private static float hue2rgb(final float p, final float q, float t) {
        if (t < 0) {
            t += 1;
        }
        if (t > 1) {
            t -= 1;
        }
        if (t * 6 < 1) {
            return p + (q - p) * 6 * t;
        }
        if (t * 2 < 1) {
            return q;
        }
        if (t * 3 < 2) {
            return p + (q - p) * ((2.0f / 3.0f) - t) * 6;
        }
        return p;
    }
}
