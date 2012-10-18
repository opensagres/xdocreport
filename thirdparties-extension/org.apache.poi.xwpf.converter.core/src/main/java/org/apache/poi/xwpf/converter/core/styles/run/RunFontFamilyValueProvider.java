package org.apache.poi.xwpf.converter.core.styles.run;

import java.util.List;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme;
import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum;

public class RunFontFamilyValueProvider
    extends AbstractRunValueProvider<String>
{

    public static RunFontFamilyValueProvider INSTANCE = new RunFontFamilyValueProvider();

    @Override
    public String getValue( CTRPr ppr )
    {
        CTFonts fonts = getRFonts( ppr );
        if ( fonts != null )
        {
            return fonts.getAscii();
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
        org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum asciiTheme = fonts.getAsciiTheme();
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return fonts.getAscii();
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
