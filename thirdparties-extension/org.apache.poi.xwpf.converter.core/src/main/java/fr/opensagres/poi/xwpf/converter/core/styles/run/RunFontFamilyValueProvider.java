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
package fr.opensagres.poi.xwpf.converter.core.styles.run;

import java.util.List;

import org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme;
import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;

public abstract class RunFontFamilyValueProvider
    extends AbstractRunValueProvider<String>
{

    @Override
    public String getValue( CTRPr ppr, XWPFStylesDocument stylesDocument )
    {
        CTFonts fonts = getRFonts( ppr );
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
    protected String getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument stylesDocument )
    {
        CTFonts fonts = getRFonts( getRPr( docDefaults ) );
        if ( fonts != null )
        {
            return getFontFamily( stylesDocument, fonts );
        }
        return "Times New Roman";
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
        return getFamily(fonts);
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

    // /**
    // * Returns default document font, by attempting to look at styles/docDefaults/rPrDefault/rPr/rFonts.
    // *
    // * @return default document font.
    // */
    // public String getDefaultFont()
    // {
    //
    // // First look at the defaults
    // // 3 look at styles/rPrDefault
    // // 3.1 if there is an rFonts element, do what it says (it may refer you to the theme part,
    // // in which case if there is no theme part, default to "internally stored settings"
    // // (there is no normal.dot; see http://support.microsoft.com/kb/924460/en-us )
    // // in this case Calibri and Cambria)
    // // 3.2 if there is no rFonts element, default to Times New Roman.
    //
    // org.docx4j.wml.RFonts rFonts = documentDefaultRPr.getRFonts();
    // if ( rFonts == null )
    // {
    // log.info( "No styles/docDefaults/rPrDefault/rPr/rFonts - default to Times New Roman" );
    // // Yes, Times New Roman is still buried in Word 2007
    // return "Times New Roman";
    // }
    // else
    // {
    // // Usual case
    // if ( rFonts.getAsciiTheme() != null )
    // {
    // // for example minorHAnsi, which I think translates to minorFont/latin
    // if ( rFonts.getAsciiTheme().equals( org.docx4j.wml.STTheme.MINOR_H_ANSI ) )
    // {
    // if ( themePart != null )
    // {
    // org.docx4j.dml.BaseStyles.FontScheme fontScheme = themePart.getFontScheme();
    // if ( fontScheme.getMinorFont() != null && fontScheme.getMinorFont().getLatin() != null )
    // {
    //
    // org.docx4j.dml.TextFont textFont = fontScheme.getMinorFont().getLatin();
    // log.debug( "minorFont/latin font is " + textFont.getTypeface() );
    // return textFont.getTypeface();
    // }
    // else
    // {
    // // No minorFont/latin in theme part - default to Calibri
    // log.info( "No minorFont/latin in theme part - default to Calibri" );
    // return "Calibri";
    // }
    // }
    // else
    // {
    // // No theme part - default to Calibri
    // log.info( "No theme part - default to Calibri" );
    // return "Calibri";
    // }
    // }
    // else
    // {
    // // TODO
    // log.error( "Don't know how to handle: " + rFonts.getAsciiTheme() );
    // return null;
    // }
    // }
    // else if ( rFonts.getAscii() != null )
    // {
    // log.info( "rPrDefault/rFonts referenced " + rFonts.getAscii() );
    // return rFonts.getAscii();
    // }
    // else
    // {
    // // TODO
    // log.error( "Neither ascii or asciTheme.  What to do? " );
    // return null;
    // }
    // }
    // }

}
