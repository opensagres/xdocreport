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

import java.util.List;

import org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme;
import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;

public abstract class RunFontFamilyValueProvider
    extends AbstractRunValueProvider<String>
{

    @Override
    public String getValue( CTRPr rPr, XWPFStylesDocument stylesDocument )
    {
        CTFonts fonts = getRFonts( rPr );
        if ( fonts != null )
        {
            return getFontFamily( stylesDocument, fonts );
        }
        return null;
    }

    private CTFonts getRFonts( CTRPr ppr )
    {
        if ( ppr == null )
        {
            return null;
        }
        return ppr.getRFonts();
    }

    @Override
    public String getValue( CTParaRPr rPr, XWPFStylesDocument stylesDocument )
    {
        CTFonts fonts = getRFonts( rPr );
        if ( fonts != null )
        {
            return getFontFamily( stylesDocument, fonts );
        }
        return null;
    }

    private CTFonts getRFonts( CTParaRPr ppr )
    {
        if ( ppr == null )
        {
            return null;
        }
        return ppr.getRFonts();
    }

    private String getFontFamily( XWPFStylesDocument stylesDocument, CTFonts fonts )
    {
        org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum asciiTheme = getTheme( fonts );
        if ( asciiTheme != null )
        {
            try
            {
                List<ThemeDocument> themeDocuments = stylesDocument.getThemeDocuments();
                if ( themeDocuments.size() > 0 )
                {
                    for ( ThemeDocument themeDocument : themeDocuments )
                    {
                        CTFontCollection fontCollection = getFontCollection( themeDocument, asciiTheme );
                        if ( fontCollection != null && fontCollection.getLatin() != null )
                        {
                            return fontCollection.getLatin().getTypeface();
                        }
                    }
                }
                return getDefaultFontFamily( asciiTheme );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        return getFamily( fonts );
    }

    private String getDefaultFontFamily( Enum asciiTheme )
    {
        if ( asciiTheme.equals( STTheme.MINOR_H_ANSI ) )
        {
            return "Calibri";
        }
        return null;
    }

    private CTFontCollection getFontCollection( ThemeDocument themeDocument, Enum asciiTheme )
    {
        CTFontScheme fontScheme = themeDocument.getTheme().getThemeElements().getFontScheme();
        if ( fontScheme != null )
        {

            if ( asciiTheme.equals( STTheme.MINOR_ASCII ) || asciiTheme.equals( STTheme.MINOR_BIDI )
                || asciiTheme.equals( STTheme.MINOR_EAST_ASIA ) || asciiTheme.equals( STTheme.MINOR_H_ANSI ) )
            {
                return fontScheme.getMinorFont();
            }
            return fontScheme.getMajorFont();

        }
        return null;
    }

    protected abstract STTheme.Enum getTheme( CTFonts fonts );

    protected abstract String getFamily( CTFonts fonts );
}
