/**
 * Copyright (C) 2011-2020 The XDocReport Team <xdocreport@googlegroups.com>
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
package fr.opensagres.xdocreport.document.docx.textstyling;

import fr.opensagres.xdocreport.document.textstyling.properties.Color;

/**
 * Helper class to generate Docx Color Codes
 */
public class DocxColorGenerator
{
    /**
     * generates a color code from a {@link fr.opensagres.xdocreport.document.textstyling.properties.Color}
     * @param color color to convert
     * @return docx color code
     */
    public static String generate( Color color )
    {
        String hexRed = Integer.toHexString( color.getRed() );
        String hexGreen = Integer.toHexString( color.getGreen() );
        String hexBlue = Integer.toHexString( color.getBlue() );

        return String.format( "%2s%2s%2s", hexRed, hexGreen, hexBlue ).replaceAll( " ", "0" ).toUpperCase();
    }
}
