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
package fr.opensagres.odfdom.converter.pdf.internal.styles;

import java.awt.Color;

import com.lowagie.text.Font;

import fr.opensagres.xdocreport.itext.extension.font.IFontProvider;

public class StyleTextProperties
{
    private Color backgroundColor;

    private Boolean fontBold;

    private Boolean fontBoldAsian;

    private Boolean fontBoldComplex;

    private Color fontColor;

    private String fontEncoding;

    private Boolean fontItalic;

    private Boolean fontItalicAsian;

    private Boolean fontItalicComplex;

    private String fontName;

    private String fontNameAsian;

    private String fontNameComplex;

    private float fontSize = Font.UNDEFINED;

    private float fontSizeAsian = Font.UNDEFINED;

    private float fontSizeComplex = Font.UNDEFINED;

    private Boolean fontStrikeThru;

    private Boolean fontUnderline;

    private Float textPosition;

    private final IFontProvider fontProvider;

    public StyleTextProperties( IFontProvider fontProvider )
    {
        this.fontProvider = fontProvider;
    }

    public StyleTextProperties( IFontProvider fontProvider, StyleTextProperties textProperties )
    {
        if ( textProperties != null )
        {
            merge( textProperties );
        }
        this.fontProvider = fontProvider;
    }

    public StyleTextProperties( IFontProvider fontProvider, StyleTextProperties textProperties1,
                                StyleTextProperties textProperties2 )
    {
        if ( textProperties1 != null )
        {
            merge( textProperties1 );
        }
        if ( textProperties2 != null )
        {
            merge( textProperties2 );
        }
        this.fontProvider = fontProvider;
    }

    public void merge( StyleTextProperties textProperties )
    {
        if ( textProperties.getBackgroundColor() != null )
        {
            backgroundColor = textProperties.getBackgroundColor();
        }
        if ( textProperties.getFontBold() != null )
        {
            fontBold = textProperties.getFontBold();
        }
        if ( textProperties.getFontBoldAsian() != null )
        {
            fontBoldAsian = textProperties.getFontBoldAsian();
        }
        if ( textProperties.getFontBoldComplex() != null )
        {
            fontBoldComplex = textProperties.getFontBoldComplex();
        }
        if ( textProperties.getFontColor() != null )
        {
            fontColor = textProperties.getFontColor();
        }
        if ( textProperties.getFontEncoding() != null )
        {
            fontEncoding = textProperties.getFontEncoding();
        }
        if ( textProperties.getFontItalic() != null )
        {
            fontItalic = textProperties.getFontItalic();
        }
        if ( textProperties.getFontItalicAsian() != null )
        {
            fontItalicAsian = textProperties.getFontItalicAsian();
        }
        if ( textProperties.getFontItalicComplex() != null )
        {
            fontItalicComplex = textProperties.getFontItalicComplex();
        }
        if ( textProperties.getFontName() != null )
        {
            fontName = textProperties.getFontName();
        }
        if ( textProperties.getFontNameAsian() != null )
        {
            fontNameAsian = textProperties.getFontNameAsian();
        }
        if ( textProperties.getFontNameComplex() != null )
        {
            fontNameComplex = textProperties.getFontNameComplex();
        }
        if ( textProperties.getFontSize() != Font.UNDEFINED )
        {
            fontSize = textProperties.getFontSize();
        }
        if ( textProperties.getFontSizeAsian() != Font.UNDEFINED )
        {
            fontSizeAsian = textProperties.getFontSizeAsian();
        }
        if ( textProperties.getFontSizeComplex() != Font.UNDEFINED )
        {
            fontSizeComplex = textProperties.getFontSizeComplex();
        }
        if ( textProperties.getFontStrikeThru() != null )
        {
            fontStrikeThru = textProperties.getFontStrikeThru();
        }
        if ( textProperties.getFontUnderline() != null )
        {
            fontUnderline = textProperties.getFontUnderline();
        }
        if ( textProperties.getTextPosition() != null )
        {
            textPosition = textProperties.getTextPosition();
        }
    }

    public int getStyleFlag()
    {
        int style = Font.NORMAL;
        if ( Boolean.TRUE.equals( fontItalic ) )
        {
            style |= Font.ITALIC;
        }
        if ( Boolean.TRUE.equals( fontBold ) )
        {
            style |= Font.BOLD;
        }
        if ( Boolean.TRUE.equals( fontUnderline ) )
        {
            style |= Font.UNDERLINE;
        }
        if ( Boolean.TRUE.equals( fontStrikeThru ) )
        {
            style |= Font.STRIKETHRU;
        }
        return style;
    }

    public int getStyleFlagAsian()
    {
        int style = Font.NORMAL;
        if ( Boolean.TRUE.equals( fontItalicAsian ) )
        {
            style |= Font.ITALIC;
        }
        if ( Boolean.TRUE.equals( fontBoldAsian ) )
        {
            style |= Font.BOLD;
        }
        if ( Boolean.TRUE.equals( fontUnderline ) )
        {
            style |= Font.UNDERLINE;
        }
        if ( Boolean.TRUE.equals( fontStrikeThru ) )
        {
            style |= Font.STRIKETHRU;
        }
        return style;
    }

    public int getStyleFlagComplex()
    {
        int style = Font.NORMAL;
        if ( Boolean.TRUE.equals( fontItalicComplex ) )
        {
            style |= Font.ITALIC;
        }
        if ( Boolean.TRUE.equals( fontBoldComplex ) )
        {
            style |= Font.BOLD;
        }
        if ( Boolean.TRUE.equals( fontUnderline ) )
        {
            style |= Font.UNDERLINE;
        }
        if ( Boolean.TRUE.equals( fontStrikeThru ) )
        {
            style |= Font.STRIKETHRU;
        }
        return style;
    }

    public boolean hasFontProperties()
    {
        return fontName != null || fontSize != Font.UNDEFINED || fontItalic != null || fontBold != null
            || fontUnderline != null || fontStrikeThru != null || fontColor != null;
    }

    public boolean hasFontPropertiesAsian()
    {
        return fontNameAsian != null || fontSizeAsian != Font.UNDEFINED || fontItalicAsian != null
            || fontBoldAsian != null || fontUnderline != null || fontStrikeThru != null || fontColor != null;
    }

    public boolean hasFontPropertiesComplex()
    {
        return fontNameComplex != null || fontSizeComplex != Font.UNDEFINED || fontItalicComplex != null
            || fontBoldComplex != null || fontUnderline != null || fontStrikeThru != null || fontColor != null;
    }

    public Font getFont()
    {
        Float adjustedFontSize;

        if ( hasFontProperties() )
        {
            adjustedFontSize = fontSize;

            // Shrink font size if sub or super scripted
            if ( textPosition != null )
            {
                adjustedFontSize *= 0.63f;
            }

            return fontProvider.getFont( fontName, fontEncoding, adjustedFontSize, getStyleFlag(), fontColor );
        }
        else
        {
            return null;
        }
    }

    public Font getFontAsian()
    {
        Float adjustedFontSize;

        if ( hasFontPropertiesAsian() )
        {
            adjustedFontSize = fontSizeAsian;

            // Shrink font size if sub or super scripted
            if ( textPosition != null )
            {
                adjustedFontSize *= 0.63f;
            }

            return fontProvider.getFont( fontNameAsian, fontEncoding, adjustedFontSize, getStyleFlagAsian(), fontColor );
        }
        else
        {
            return null;
        }
    }

    public Font getFontComplex()
    {
        Float adjustedFontSize;

        if ( hasFontPropertiesComplex() )
        {
            adjustedFontSize = fontSizeComplex;

            // Shrink font size if sub or super scripted
            if ( textPosition != null )
            {
                adjustedFontSize *= 0.63f;
            }

            return fontProvider.getFont( fontNameComplex, fontEncoding, adjustedFontSize, getStyleFlagComplex(),
                                         fontColor );
        }
        else
        {
            return null;
        }
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor( Color backgroundColor )
    {
        this.backgroundColor = backgroundColor;
    }

    public Boolean getFontBold()
    {
        return fontBold;
    }

    public void setFontBold( Boolean fontBold )
    {
        this.fontBold = fontBold;
    }

    public Boolean getFontBoldAsian()
    {
        return fontBoldAsian;
    }

    public void setFontBoldAsian( Boolean fontBoldAsian )
    {
        this.fontBoldAsian = fontBoldAsian;
    }

    public Boolean getFontBoldComplex()
    {
        return fontBoldComplex;
    }

    public void setFontBoldComplex( Boolean fontBoldComplex )
    {
        this.fontBoldComplex = fontBoldComplex;
    }

    public Color getFontColor()
    {
        return fontColor;
    }

    public void setFontColor( Color fontColor )
    {
        this.fontColor = fontColor;
    }

    public String getFontEncoding()
    {
        return fontEncoding;
    }

    public void setFontEncoding( String fontEncoding )
    {
        this.fontEncoding = fontEncoding;
    }

    public Boolean getFontItalic()
    {
        return fontItalic;
    }

    public void setFontItalic( Boolean fontItalic )
    {
        this.fontItalic = fontItalic;
    }

    public Boolean getFontItalicAsian()
    {
        return fontItalicAsian;
    }

    public void setFontItalicAsian( Boolean fontItalicAsian )
    {
        this.fontItalicAsian = fontItalicAsian;
    }

    public Boolean getFontItalicComplex()
    {
        return fontItalicComplex;
    }

    public void setFontItalicComplex( Boolean fontItalicComplex )
    {
        this.fontItalicComplex = fontItalicComplex;
    }

    public String getFontName()
    {
        return fontName;
    }

    public void setFontName( String fontName )
    {
        this.fontName = fontName;
    }

    public String getFontNameAsian()
    {
        return fontNameAsian;
    }

    public void setFontNameAsian( String fontNameAsian )
    {
        this.fontNameAsian = fontNameAsian;
    }

    public String getFontNameComplex()
    {
        return fontNameComplex;
    }

    public void setFontNameComplex( String fontNameComplex )
    {
        this.fontNameComplex = fontNameComplex;
    }

    public float getFontSize()
    {
        return fontSize;
    }

    public void setFontSize( float fontSize )
    {
        this.fontSize = fontSize;
    }

    public float getFontSizeAsian()
    {
        return fontSizeAsian;
    }

    public void setFontSizeAsian( float fontSizeAsian )
    {
        this.fontSizeAsian = fontSizeAsian;
    }

    public float getFontSizeComplex()
    {
        return fontSizeComplex;
    }

    public void setFontSizeComplex( float fontSizeComplex )
    {
        this.fontSizeComplex = fontSizeComplex;
    }

    public Boolean getFontStrikeThru()
    {
        return fontStrikeThru;
    }

    public void setFontStrikeThru( Boolean fontStrikeThru )
    {
        this.fontStrikeThru = fontStrikeThru;
    }

    public Boolean getFontUnderline()
    {
        return fontUnderline;
    }

    public void setFontUnderline( Boolean fontUnderline )
    {
        this.fontUnderline = fontUnderline;
    }

    public Float getTextPosition()
    {
        return textPosition;
    }

    public void setTextPosition( Float textPosition )
    {
        this.textPosition = textPosition;
    }
}
