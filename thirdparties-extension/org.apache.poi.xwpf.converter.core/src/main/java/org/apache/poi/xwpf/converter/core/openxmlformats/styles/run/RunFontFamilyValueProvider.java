package org.apache.poi.xwpf.converter.core.openxmlformats.styles.run;

import java.util.List;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme;
import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme.Enum;

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
