/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
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
package org.odftoolkit.odfdom.converter.internal.itext.styles;

import com.lowagie.text.Font;
import java.awt.Color;
import org.odftoolkit.odfdom.converter.internal.itext.ODFFontRegistry;

public class StyleTextProperties
{

    private Color backgroundColor;

    private Boolean fontBold;

    private Color fontColor;

    private String fontEncoding;

    private Boolean fontItalic;

    private String fontName;

    private float fontSize = Font.UNDEFINED;

    private Boolean fontStrikeThru;

    private Boolean fontUnderline;

    private Float textPosition;

    public StyleTextProperties()
    {
    }

    public StyleTextProperties( StyleTextProperties textProperties )
    {
        if ( textProperties != null )
        {
            merge( textProperties );
        }
    }

    public StyleTextProperties( StyleTextProperties textProperties1, StyleTextProperties textProperties2 )
    {
        if ( textProperties1 != null )
        {
            merge( textProperties1 );
        }
        if ( textProperties2 != null )
        {
            merge( textProperties2 );
        }
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
        if ( textProperties.getFontName() != null )
        {
            fontName = textProperties.getFontName();
        }
        if ( textProperties.getFontSize() != Font.UNDEFINED )
        {
            fontSize = textProperties.getFontSize();
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

    public boolean hasFontProperties()
    {
        return fontName != null || fontSize != Font.UNDEFINED || fontItalic != null || fontBold != null
            || fontUnderline != null || fontStrikeThru != null || fontColor != null;
    }

    public Font getFont()
    {
    	Float adjustedFontSize;

        if ( hasFontProperties() )
        {
        	adjustedFontSize = fontSize;

        	// Shrink font size if sub or super scripted
        	if( textPosition != null )
        	{
        		adjustedFontSize *= 0.63f;
        	}

            return ODFFontRegistry.getRegistry().getFont( fontName, fontEncoding, adjustedFontSize, getStyleFlag(), fontColor );
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

    public String getFontName()
    {
        return fontName;
    }

    public void setFontName( String fontName )
    {
        this.fontName = fontName;
    }

    public float getFontSize()
    {
        return fontSize;
    }

    public void setFontSize( float fontSize )
    {
        this.fontSize = fontSize;
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
