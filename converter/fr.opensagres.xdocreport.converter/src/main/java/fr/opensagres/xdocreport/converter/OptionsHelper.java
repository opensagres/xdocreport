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
package fr.opensagres.xdocreport.converter;

/**
 * Helper to populate converter options.
 */
public class OptionsHelper
{

    private static final String URI_RESOLVER_PROPERTY = "___URIResolver";

    private static final String FONT_ENCODING_PROPERTY = "___FontEncoding";

    public static void setURIResolver( Options options, IURIResolver resolver )
    {
        options.setProperty( URI_RESOLVER_PROPERTY, resolver );
    }

    public static IURIResolver getURIResolver( Options options )
    {
        return (IURIResolver) options.getProperty( URI_RESOLVER_PROPERTY );
    }

    /**
     * Set font encoding to use when retrieving fonts. The default value is underlying operating system encoding
     * 
     * @param fontEncoding font encoding to use
     * @return this instance
     */

    public static void setFontEncoding( Options options, String fontEncoding )
    {
        options.setProperty( FONT_ENCODING_PROPERTY, fontEncoding );
    }

    public static String getFontEncoding( Options options )
    {
        return (String) options.getProperty( FONT_ENCODING_PROPERTY );
    }
}
