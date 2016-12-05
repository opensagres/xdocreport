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
package fr.opensagres.odfdom.converter.pdf.internal.stylable;

import java.util.List;
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.factories.RomanAlphabetFactory;
import com.itextpdf.text.factories.RomanNumberFactory;

import fr.opensagres.odfdom.converter.core.utils.ODFUtils;
import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleListProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleNumFormat;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTextProperties;

public class StylableHeading
    extends StylableParagraph
{
    private static final long serialVersionUID = 664309269352903329L;

    public static final String IMPLICIT_REFERENCE_SUFFIX = "|outline";

    private List<Integer> headingNumbering;

    public StylableHeading( StylableDocument ownerDocument, IStylableContainer parent, List<Integer> headingNumbering )
    {
        super( ownerDocument, parent );
        this.headingNumbering = headingNumbering;
    }

    public static int getFirst( Style style, int outlineLevel )
    {
        int first = 1;
        Map<Integer, StyleListProperties> listPropertiesMap = style != null ? style.getOutlinePropertiesMap() : null;
        if ( listPropertiesMap != null )
        {
            StyleListProperties listProperties = StylableList.getListProperties( listPropertiesMap, outlineLevel );
            if ( listProperties != null )
            {
                // start-value
                Integer startValue = listProperties.getStartValue();
                if ( startValue != null )
                {
                    first = startValue;
                }
            }
        }
        return first;
    }

    @Override
    public void applyStyles( Style style )
    {
        super.applyStyles( style );

        int outlineLevel = headingNumbering.size();
        Map<Integer, StyleListProperties> listPropertiesMap = style.getOutlinePropertiesMap();
        if ( listPropertiesMap != null )
        {
            int displayCount = 1;
            StyleListProperties listProperties = StylableList.getListProperties( listPropertiesMap, outlineLevel );
            if ( listProperties != null )
            {
                // display-levels
                Integer displayLevels = listProperties.getDisplayLevels();
                if ( displayLevels != null && displayLevels > 1 )
                {
                    displayCount = Math.min( displayLevels, outlineLevel );
                }
            }

            // add leading numbering ie 1.2.3.
            addNumbering( listPropertiesMap, displayCount );
        }

        // add implicit bookmark
        Chunk chunk = new Chunk( ODFUtils.TAB_STR );
        chunk.setLocalDestination( generateImplicitDestination( headingNumbering ) );
        addElement( chunk );
    }

    private void addNumbering( Map<Integer, StyleListProperties> listPropertiesMap, int displayCount )
    {
        for ( int i = 0; i < displayCount; i++ )
        {
            StyleListProperties listProperties = StylableList.getListProperties( listPropertiesMap, i + 1 );
            int value = headingNumbering.get( i );
            if ( listProperties != null )
            {
                Chunk chunk = formatNumber( listProperties, value );
                addElement( chunk );
            }
        }
    }

    private Chunk formatNumber( StyleListProperties listProperties, int value )
    {
        Chunk symbol = new Chunk( "", getFont() );

        StyleTextProperties textProperties = listProperties.getTextProperties();
        if ( textProperties != null )
        {
            Font font = textProperties.getFont();
            if ( font != null )
            {
                symbol.setFont( font );
            }
        }

        StyleNumFormat numFormat = listProperties.getNumFormat();
        if ( numFormat != null )
        {
            StringBuilder sbuf = new StringBuilder();

            // num-prefix
            String numPrefix = listProperties.getNumPrefix();
            if ( numPrefix != null )
            {
                sbuf.append( numPrefix );
            }

            // number
            if ( numFormat.isAlphabetical() )
            {
                sbuf.append( RomanAlphabetFactory.getString( value, numFormat.isLowercase() ) );
            }
            else if ( numFormat.isRoman() )
            {
                sbuf.append( RomanNumberFactory.getString( value, numFormat.isLowercase() ) );
            }
            else
            {
                sbuf.append( value );
            }

            // num-suffix
            String numSuffix = listProperties.getNumSuffix();
            if ( numSuffix != null )
            {
                sbuf.append( numSuffix );
            }

            symbol.append( sbuf.toString() );
        }
        return symbol;
    }

    public static String generateImplicitDestination( String s )
    {
        if ( s.startsWith( "#" ) )
        {
            s = s.substring( 1 );
        }
        StringBuilder sbuf = new StringBuilder();
        for ( char ch : s.toCharArray() )
        {
            if ( Character.isDigit( ch ) || ch == '.' )
            {
                sbuf.append( ch );
            }
            else
            {
                break;
            }
        }
        sbuf.append( IMPLICIT_REFERENCE_SUFFIX );
        return sbuf.toString();
    }

    private static String generateImplicitDestination( List<Integer> headingNumbering )
    {
        StringBuilder sbuf = new StringBuilder();
        for ( Integer nr : headingNumbering )
        {
            sbuf.append( nr.toString() );
            sbuf.append( "." );
        }
        sbuf.append( IMPLICIT_REFERENCE_SUFFIX );
        return sbuf.toString();
    }
}
