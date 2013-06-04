package fr.opensagres.xdocreport.itext.extension.font;

import java.awt.Color;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

public class ITextFontRegistry2
    extends ITextFontRegistry
{

    private static final ITextFontRegistry2 INSTANCE = new ITextFontRegistry2();

    public ITextFontRegistry2()
    {
        FontFactory.setFontImp( new ExtendedFontFactoryImp() );
    }
    public static ITextFontRegistry2 getRegistry()
    {
        return INSTANCE;
    }

    @Override
    public Font getFont( String familyName, String encoding, float size, int style, Color color )
    {
        if ( "Symbol".equals( familyName ) || "Wingdings".equals( familyName ) )
        {
            familyName = familyName + "-Exp";
            encoding = BaseFont.IDENTITY_H;
        }
        return super.getFont( familyName, encoding, size, style, color );
    }
}
