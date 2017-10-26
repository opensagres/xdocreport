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
package fr.opensagres.poi.xwpf.converter.core.utils;

import fr.opensagres.poi.xwpf.converter.core.Color;

/**
 * String Utilities.
 */
public class StringUtils
{
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * <p>
     * Checks if a String is empty ("") or null.
     * </p>
     * 
     * <pre>
     * StringUtils.isEmpty(null) = true StringUtils.isEmpty(&quot;&quot;) = true
     * StringUtils.isEmpty(&quot; &quot;) = false StringUtils.isEmpty(&quot;bob&quot;) = false
     * StringUtils.isEmpty(&quot; bob &quot;) = false
     * </pre>
     * 
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty( String str )
    {
        return str == null || str.length() == 0;
    }

    /**
     * <p>
     * Checks if a String is not empty ("") and not null.
     * </p>
     * 
     * <pre>
     * StringUtils.isNotEmpty(null) = false
     * StringUtils.isNotEmpty(&quot;&quot;) = false
     * StringUtils.isNotEmpty(&quot; &quot;) = true
     * StringUtils.isNotEmpty(&quot;bob&quot;) = true
     * StringUtils.isNotEmpty(&quot; bob &quot;) = true
     * </pre>
     * 
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty( String str )
    {
        return str != null && str.length() > 0;
    }

    /**
     * Replace the oldString by the newString in the line and returns the result.
     * 
     * @param line the line to replace.
     * @param oldString old token to replace.
     * @param newString new token to replace.
     * @return
     */
    public static final String replaceAll( String line, String oldString, String newString )
    {
        int i = 0;
        if ( ( i = line.indexOf( oldString, i ) ) >= 0 )
        {
            char line2[] = line.toCharArray();
            char newString2[] = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder( line2.length );
            buf.append( line2, 0, i ).append( newString2 );
            i += oLength;
            int j;
            for ( j = i; ( i = line.indexOf( oldString, i ) ) > 0; j = i )
            {
                buf.append( line2, j, i - j ).append( newString2 );
                i += oLength;
            }

            buf.append( line2, j, line2.length - j );
            return buf.toString();
        }
        else
        {
            return line;
        }
    }
    
    public static String replaceNonUnicodeChars(String text) {
		StringBuilder newString = new StringBuilder(text.length());
		for (int offset = 0; offset < text.length();)
		{
		    int codePoint = text.codePointAt(offset);
		    offset += Character.charCount(codePoint);

		    // Replace invisible control characters and unused code points
		    switch (Character.getType(codePoint))
		    {
		        case Character.CONTROL:     // \p{Cc}
		        case Character.FORMAT:      // \p{Cf}
		        case Character.PRIVATE_USE: // \p{Co}
		        case Character.SURROGATE:   // \p{Cs}
		        case Character.UNASSIGNED:  // \p{Cn}
		            newString.append('\u2022');
		            break;
		        default:
		            newString.append(Character.toChars(codePoint));
		            break;
		    }
		}
		return newString.toString();
	}
    
    /**
     * Color to hex representation
     * @param colour
     * @return
     */
    public static String toHexString(Color colour) {
    	if(colour == null)
    	{
    		return "";
    	}
		String hexColour = Integer.toHexString(colour.getRGB() & 0xffffff);
		if (hexColour.length() < 6) {
			hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
		 }
		 return "#" + hexColour;
	}

}
