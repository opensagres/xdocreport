/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package fr.opensagres.xdocreport.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.core.EncodingConstants;
import fr.opensagres.xdocreport.core.logging.LogUtils;

/**
 * String Utilities.
 */
public class StringUtils
{
    /**
     * Logger for this class
     */
    private static final Logger logger = LogUtils.getLogger( StringUtils.class.getName() );

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static final String TRUE = "true";

    public static final String FALSE = "false";

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

    public static boolean asBoolean( String value )
    {
        return asBoolean( value, false );
    }

    public static boolean asBoolean( String value, boolean defaultValue )
    {
        if ( value == null )
            return defaultValue;
        value = value.trim();
        if ( defaultValue )
            return !( FALSE.equals( value.toLowerCase() ) );
        return TRUE.equals( value.toLowerCase() );
    }

    public static Boolean asBooleanObject( String value )
    {
        return asBooleanObject( value, null );
    }

    public static Boolean asBooleanObject( String value, Boolean defaultValue )
    {
        if ( value == null )
            return defaultValue;
        value = value.trim();
        if ( defaultValue )
            return !( FALSE.equals( value.toLowerCase() ) );
        return TRUE.equals( value.toLowerCase() );
    }

    /**
     * Returns the given value String as Integer.
     * 
     * @param value
     * @return
     */
    public static Integer asInteger( String value )
    {
        return asInteger( value, null );
    }

    /**
     * Returns the given value String as Integer.
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static Integer asInteger( String value, Integer defaultValue )
    {
        try
        {
            return Integer.parseInt( value );
        }
        catch ( Throwable e )
        {
            return defaultValue;
        }
    }

    /**
     * Returns the given value String as Long.
     * 
     * @param value
     * @return
     */
    public static Long asLong( String value )
    {
        return asLong( value, null );
    }

    /**
     * Returns the given value String as Long.
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static Long asLong( String value, Long defaultValue )
    {
        try
        {
            return Long.parseLong( value );
        }
        catch ( Throwable e )
        {
            return defaultValue;
        }
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op, or if any "search string" or "string to replace"
     * is null, that replace will be ignored. This will not repeat. For repeating replaces, call the overloaded method.
     * </p>
     * 
     * <pre>
     *  StringUtils.replaceEach(null, *, *)        = null
     *  StringUtils.replaceEach("", *, *)          = ""
     *  StringUtils.replaceEach("aba", null, null) = "aba"
     *  StringUtils.replaceEach("aba", new String[0], null) = "aba"
     *  StringUtils.replaceEach("aba", null, new String[0]) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, null)  = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""})  = "b"
     *  StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"})  = "aba"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"})  = "wcte"
     *  (example of how it does not repeat)
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"})  = "dcte"
     * </pre>
     * 
     * @param text text to search and replace in, no-op if null
     * @param searchList the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @return the text with any replacements processed, <code>null</code> if null String input
     * @throws IndexOutOfBoundsException if the lengths of the arrays are not the same (null is ok, and/or size 0)
     * @since 2.4
     */
    public static String replaceEach( String text, String[] searchList, String[] replacementList )
    {
        return replaceEach( text, searchList, replacementList, false, 0 );
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op, or if any "search string" or "string to replace"
     * is null, that replace will be ignored.
     * </p>
     * 
     * <pre>
     *  StringUtils.replaceEach(null, *, *, *) = null
     *  StringUtils.replaceEach("", *, *, *) = ""
     *  StringUtils.replaceEach("aba", null, null, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[0], null, *) = "aba"
     *  StringUtils.replaceEach("aba", null, new String[0], *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
     *  StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
     *  (example of how it repeats)
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, *) = IllegalArgumentException
     * </pre>
     * 
     * @param text text to search and replace in, no-op if null
     * @param searchList the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @param repeat if true, then replace repeatedly until there are no more possible replacements or timeToLive < 0
     * @param timeToLive if less than 0 then there is a circular reference and endless loop
     * @return the text with any replacements processed, <code>null</code> if null String input
     * @throws IllegalArgumentException if the search is repeating and there is an endless loop due to outputs of one
     *             being inputs to another
     * @throws IndexOutOfBoundsException if the lengths of the arrays are not the same (null is ok, and/or size 0)
     * @since 2.4
     */
    private static String replaceEach( String text, String[] searchList, String[] replacementList, boolean repeat,
                                       int timeToLive )
    {

        // mchyzer Performance note: This creates very few new objects (one
        // major goal)
        // let me know if there are performance requests, we can create a
        // harness to measure

        if ( text == null || text.length() == 0 || searchList == null || searchList.length == 0
            || replacementList == null || replacementList.length == 0 )
        {
            return text;
        }

        // if recursing, this shouldnt be less than 0
        if ( timeToLive < 0 )
        {
            throw new IllegalStateException( "TimeToLive of " + timeToLive + " is less than 0: " + text );
        }

        int searchLength = searchList.length;
        int replacementLength = replacementList.length;

        // make sure lengths are ok, these need to be equal
        if ( searchLength != replacementLength )
        {
            throw new IllegalArgumentException( "Search and Replace array lengths don't match: " + searchLength
                + " vs " + replacementLength );
        }

        // keep track of which still have matches
        boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for ( int i = 0; i < searchLength; i++ )
        {
            if ( noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0
                || replacementList[i] == null )
            {
                continue;
            }
            tempIndex = text.indexOf( searchList[i] );

            // see if we need to keep searching for this
            if ( tempIndex == -1 )
            {
                noMoreMatchesForReplIndex[i] = true;
            }
            else
            {
                if ( textIndex == -1 || tempIndex < textIndex )
                {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if ( textIndex == -1 )
        {
            return text;
        }

        int start = 0;

        // get a good guess on the size of the result buffer so it doesnt have
        // to double if it goes over a bit
        int increase = 0;

        // count the replacement text elements that are larger than their
        // corresponding text being replaced
        for ( int i = 0; i < searchList.length; i++ )
        {
            int greater = replacementList[i].length() - searchList[i].length();
            if ( greater > 0 )
            {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min( increase, text.length() / 5 );

        StringBuffer buf = new StringBuffer( text.length() + increase );

        while ( textIndex != -1 )
        {

            for ( int i = start; i < textIndex; i++ )
            {
                buf.append( text.charAt( i ) );
            }
            buf.append( replacementList[replaceIndex] );

            start = textIndex + searchList[replaceIndex].length();

            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for ( int i = 0; i < searchLength; i++ )
            {
                if ( noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0
                    || replacementList[i] == null )
                {
                    continue;
                }
                tempIndex = text.indexOf( searchList[i], start );

                // see if we need to keep searching for this
                if ( tempIndex == -1 )
                {
                    noMoreMatchesForReplIndex[i] = true;
                }
                else
                {
                    if ( textIndex == -1 || tempIndex < textIndex )
                    {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            // NOTE: logic duplicated above END

        }
        int textLength = text.length();
        for ( int i = start; i < textLength; i++ )
        {
            buf.append( text.charAt( i ) );
        }
        String result = buf.toString();
        if ( !repeat )
        {
            return result;
        }

        return replaceEach( result, searchList, replacementList, repeat, timeToLive - 1 );
    }

    /**
     * Decode the given String to UTF-8.
     * 
     * @param content
     * @return
     */
    public static String decode( String s )
    {
        try
        {
            return URLDecoder.decode( s, EncodingConstants.UTF_8.name() );
        }
        catch ( UnsupportedEncodingException e )
        {
            logger.severe( "String - exception: " + e ); //$NON-NLS-1$

            return s;
        }
    }

    public static String xmlUnescape( String s )
    {
        if ( s == null )
        {
            return null;
        }
        s = s.replaceAll( EncodingConstants.AMP, "&" );
        s = s.replaceAll( EncodingConstants.LT, "<" );
        s = s.replaceAll( EncodingConstants.GT, ">" );
        s = s.replaceAll( EncodingConstants.QUOT, "\"" );
        s = s.replaceAll( EncodingConstants.APOS, "'" );
        return s;
    }

}
