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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.Type3Font;

public abstract class AbstractFontRegistry
    implements IFontProvider
{
    private boolean systemEncodingDetermined;

    private String systemEncoding;

    private static boolean fontRegistryInitialized = false;

    public Font getFont( String familyName, String encoding, float size, int style, BaseColor color )
    {
        initFontRegistryIfNeeded();
        if ( familyName != null )
        {
            familyName = resolveFamilyName( familyName, style );
        }
        try
        {
        	
            return FontFactory.getFont( familyName, encoding, size, style, color );
        }
        catch ( ExceptionConverter e )
        {
            // TODO manage options of font not found + add some logs
        	
        	return new Font( FontFamily.UNDEFINED, size, style, color );
        }
    }

    private void initFontRegistryIfNeeded()
    {
        if ( !fontRegistryInitialized )
        {
            // clear built-in fonts which may clash with document fonts
            ExtendedBaseFont.clearBuiltinFonts();
            // register fonts from files (ex : for windows, load files from C:\WINDOWS\Fonts)
            FontFactory.registerDirectories();
            fontRegistryInitialized = true;
        }
    }

    public static class ExtendedBaseFont
        extends Type3Font
    {
        public ExtendedBaseFont()
        {
            super( null, false );
        }

        public static void clearBuiltinFonts()
        {
            BuiltinFonts14.clear();
            // keep default font - used if some font cannot be found
            BuiltinFonts14.put( HELVETICA, PdfName.HELVETICA );
            BuiltinFonts14.put( HELVETICA_BOLD, PdfName.HELVETICA_BOLD );
            BuiltinFonts14.put( HELVETICA_BOLDOBLIQUE, PdfName.HELVETICA_BOLDOBLIQUE );
            BuiltinFonts14.put( HELVETICA_OBLIQUE, PdfName.HELVETICA_OBLIQUE );
        }
    }

    /**
     * checks if this font is Bold.
     * 
     * @return a <CODE>boolean</CODE>
     */
    public boolean isBold( int style )
    {
        if ( style == Font.UNDEFINED )
        {
            return false;
        }
        return ( style & Font.BOLD ) == Font.BOLD;
    }

    /**
     * checks if this font is Bold.
     * 
     * @return a <CODE>boolean</CODE>
     */
    public boolean isItalic( int style )
    {
        if ( style == Font.UNDEFINED )
        {
            return false;
        }
        return ( style & Font.ITALIC ) == Font.ITALIC;
    }

    /**
     * checks if this font is underlined.
     * 
     * @return a <CODE>boolean</CODE>
     */
    public boolean isUnderlined( int style )
    {
        if ( style == Font.UNDEFINED )
        {
            return false;
        }
        return ( style & Font.UNDERLINE ) == Font.UNDERLINE;
    }

    /**
     * checks if the style of this font is STRIKETHRU.
     * 
     * @return a <CODE>boolean</CODE>
     */
    public boolean isStrikethru( int style )
    {
        if ( style == Font.UNDEFINED )
        {
            return false;
        }
        return ( style & Font.STRIKETHRU ) == Font.STRIKETHRU;
    }

    public String getSystemEncoding()
    {
        if ( systemEncodingDetermined )
        {
            return systemEncoding;
        }
        // don't rely on file.encoding property because
        // it may be changed if application is launched inside an ide
        systemEncoding = System.getProperty( "sun.jnu.encoding" );
        if ( systemEncoding != null && systemEncoding.length() > 0 )
        {
            systemEncodingDetermined = true;
            return systemEncoding;
        }
        systemEncoding = System.getProperty( "ibm.system.encoding" );
        if ( systemEncoding != null && systemEncoding.length() > 0 )
        {
            systemEncodingDetermined = true;
            return systemEncoding;
        }
        systemEncoding = FontFactory.defaultEncoding;
        systemEncodingDetermined = true;
        return systemEncoding;
    }

    protected abstract String resolveFamilyName( String familyName, int style );
}
