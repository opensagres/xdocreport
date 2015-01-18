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
package fr.opensagres.xdocreport.itext.extension.font;


public class ITextFontRegistry
    extends AbstractFontRegistry
{

//    private static final String TIMES_NEW_ROMAN_FONT_FAMILY_NAME = "Times New Roman";
//
//    private static final String COURRIER_NEW_FONT_FAMILY_NAME = "Courier New";

    private static final ITextFontRegistry INSTANCE = new ITextFontRegistry();

    public static ITextFontRegistry getRegistry()
    {
        return INSTANCE;
    }

    @Override
    protected String resolveFamilyName( String familyName, int style )
    {
        // commented out
        // incorrect font is retrieved for translated names - probably iText internal font instead of system font
        //
        /*boolean bold = isBold( style );
        boolean italic = isItalic( style );

        if ( COURRIER_NEW_FONT_FAMILY_NAME.equals( familyName ) )
        {
            if ( bold && italic )
            {
                return BaseFont.COURIER_BOLDOBLIQUE;
            }
            else if ( bold )
            {
                return BaseFont.COURIER_BOLD;
            }
            else if ( italic )
            {
                return BaseFont.COURIER_OBLIQUE;
            }
            return BaseFont.COURIER;
        }

        if ( TIMES_NEW_ROMAN_FONT_FAMILY_NAME.equals( familyName ) )
        {
            if ( bold && italic )
            {
                return BaseFont.TIMES_BOLDITALIC;
            }
            else if ( bold )
            {
                return BaseFont.TIMES_BOLD;
            }
            else if ( italic )
            {
                return BaseFont.TIMES_ITALIC;
            }
            return BaseFont.TIMES_ROMAN;
        }*/

        return familyName;
    }
}
