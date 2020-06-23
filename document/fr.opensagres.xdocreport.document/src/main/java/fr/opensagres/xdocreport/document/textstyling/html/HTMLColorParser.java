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

/**
 * Colors in CSS can be defined in a few different ways:
 * - As hexadecimal values (supported)
 * - As RGB values (supported)
 * - By color names (unsupported)
 * - As HSL values (CSS3) (unsupported)
 * - As HWB values (CSS4) (unsupported)
 * - With the currentcolor keyword (unsupported)
 */
public class HTMLColorParser
{

    /**
     * @param colorString an rgb color string
     *                    i.e. rgb(255,0,0);
     * @return null if colorString invalid or not implemented, so that invalid arguments won't crash the program
     */
    public static Color parse( String colorString )
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
        else
        {
            // other formats not implemented
            return null;
        }
    }

    private static Color parseRgb( String colorString )
    {
        String color = colorString.replaceAll( "[^\\d,]*", "" );
        String[] rgbColor = color.split( "," );
        if ( rgbColor.length == 3 )
        {
            try
            {
                return new Color( Integer.parseInt( rgbColor[0] ), Integer.parseInt( rgbColor[1] ),
                                  Integer.parseInt( rgbColor[2] ) );
            }
            catch ( Exception e )
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

    private static Color parseHexadecimalValues( String colorString )
    {
        String red = colorString.substring( 1, 3 );
        String green = colorString.substring( 3, 5 );
        String blue = colorString.substring( 5, 7 );
        try
        {
            return new Color( Integer.parseInt( red, 16 ), Integer.parseInt( green, 16 ),
                              Integer.parseInt( blue, 16 ) );
        }
        catch ( Exception e )
        {
            // invalid arguments will be ignored
            return null;
        }
    }
}
