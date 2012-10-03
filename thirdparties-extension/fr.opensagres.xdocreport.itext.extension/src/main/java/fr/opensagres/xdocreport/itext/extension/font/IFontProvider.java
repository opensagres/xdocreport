package fr.opensagres.xdocreport.itext.extension.font;

import java.awt.Color;

import com.lowagie.text.Font;

public interface IFontProvider
{

    Font getFont( String familyName, String encoding, float size, int style, Color color );

}
