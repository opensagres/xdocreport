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
package fr.opensagres.odfdom.converter.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.style.StyleTableColumnPropertiesElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableColumnElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableColumnsElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class ODFUtils
{
    public static final String NON_BREAKING_SPACE_STR = "\u00A0";

    public static final String TAB_STR = "\t";

    // Unit constants
    private static final String REL_SIZE_UNIT = "*";

    private static final String PERCENT_UNIT = "%";

    private static final String INCH_UNIT = "in";

    private static final String POINT_UNIT = "pt";

    private static final String MM_UNIT = "mm";

    private static final String CM_UNIT = "cm";

    public static float[] getColumnWidths( TableTableElement table, OdfDocument odfDocument )
    {
        Collection<String> colWidths = getColumnWidthsAsString( table, odfDocument );
        int i = 0;
        float[] widths = new float[colWidths.size()];
        for ( String s : colWidths )
        {
            widths[i++] = getDimensionAsPoint( s );
        }
        return widths;
    }

    public static List<String> getColumnWidthsAsString( Node table, OdfDocument odfDocument )
    {
        List<String> colWidths = new ArrayList<String>();
        Node node = null;
        NodeList tableColums = table.getChildNodes();
        for ( int i = 0; i < tableColums.getLength(); i++ )
        {
            node = tableColums.item( i );
            if ( TableTableColumnElement.ELEMENT_NAME.getLocalName().equals( node.getLocalName() ) )
            {
                // found table:column
                TableTableColumnElement tableColumn = (TableTableColumnElement) node;
                Integer numberColumnsRepeated = tableColumn.getTableNumberColumnsRepeatedAttribute();
                String styleName = tableColumn.getTableStyleNameAttribute();
                try
                {
                    String columnWidth = null;
                    OdfStyle style = getOdfStyle( odfDocument, styleName, OdfStyleFamily.TableColumn );
                    if ( style != null )
                    {
                        StyleTableColumnPropertiesElement tableColumnProperties =
                            getStyleTableColumnPropertiesElement( style );
                        if ( tableColumnProperties != null )
                        {
                            columnWidth = tableColumnProperties.getStyleColumnWidthAttribute();
                        }
                    }
                    if ( StringUtils.isEmpty( columnWidth ) )
                    {
                        columnWidth = "1";
                    }
                    if ( numberColumnsRepeated == null )
                    {
                        colWidths.add( columnWidth );
                    }
                    else
                    {
                        for ( int j = 0; j < numberColumnsRepeated; j++ )
                        {
                            colWidths.add( columnWidth );
                        }
                    }
                }
                catch ( Exception e )
                {
                    throw new RuntimeException( e );
                }
            }
            else if ( TableTableColumnsElement.ELEMENT_NAME.getLocalName().equals( node.getLocalName() ) )
            {
                // found table:columns, column info should be nested inside
                return getColumnWidthsAsString( node, odfDocument );
            }
            else
            {
                break;
            }
        }
        return colWidths;
    }

    public static OdfStyle getOdfStyle( OdfDocument odfDocument, String styleName, OdfStyleFamily styleFamily )
        throws Exception
    {
        OdfStyle style = null;
        if ( odfDocument.getContentDom() != null && odfDocument.getContentDom().getAutomaticStyles() != null )
        {
            // try content dom
            style = odfDocument.getContentDom().getAutomaticStyles().getStyle( styleName, styleFamily );
        }
        if ( style == null )
        {
            if ( odfDocument.getStylesDom() != null && odfDocument.getStylesDom().getAutomaticStyles() != null )
            {
                // try content dom
                style = odfDocument.getStylesDom().getAutomaticStyles().getStyle( styleName, styleFamily );
            }
        }
        return style;
    }

    public static StyleTableColumnPropertiesElement getStyleTableColumnPropertiesElement( OdfStyle style )
    {
        Node node = null;
        NodeList nodes = style.getChildNodes();
        for ( int i = 0; i < nodes.getLength(); i++ )
        {
            node = nodes.item( i );
            if ( StyleTableColumnPropertiesElement.ELEMENT_NAME.getLocalName().equals( node.getLocalName() ) )
            {
                return (StyleTableColumnPropertiesElement) node;
            }
        }
        return null;
    }

    public static Float getDimensionAsPoint( String s )
    {
        // IText works with point unit (1cm = 28.35 pt)
        // cm unit?
        int index = s.indexOf( CM_UNIT );
        if ( index != -1 )
        {
            s = s.substring( 0, index );
            return millimetersToPoints( (float) ( Float.valueOf( s ) * 10 ) );
        }
        // mm unit?
        index = s.indexOf( MM_UNIT );
        if ( index != -1 )
        {
            s = s.substring( 0, index );
            return millimetersToPoints( (float) ( Float.valueOf( s ) ) );
        }
        // point unit?
        index = s.indexOf( POINT_UNIT );
        if ( index != -1 )
        {
            s = s.substring( 0, index );
            return Float.valueOf( s );
        }
        // inch unit?
        index = s.indexOf( INCH_UNIT );
        if ( index != -1 )
        {
            s = s.substring( 0, index );
            return inchesToPoints( Float.valueOf( s ) );
        }
        // % unit?
        index = s.indexOf( PERCENT_UNIT );
        if ( index != -1 )
        {
            s = s.substring( 0, index );
            return Float.valueOf( s ) / 100;
        }
        return Float.valueOf( s );
    }

    public static Integer getRelativeSize( String s )
    {
        // * unit?
        int index = s.indexOf( REL_SIZE_UNIT );
        if ( index != -1 )
        {
            s = s.substring( 0, index );
            return Integer.valueOf( s );
        }
        return Integer.valueOf( s );
    }

    /**
     * Returns true if the given string has percent unit and false otherwise.
     *
     * @param s
     * @return
     */
    public static boolean hasPercentUnit( String s )
    {
        return s.indexOf( PERCENT_UNIT ) != -1;
    }

    /**
     * Measurement conversion from millimeters to points.
     *
     * @param value a value in millimeters
     * @return a value in points
     * @since 2.1.2
     */
    public static final float millimetersToPoints( float value )
    {
        return inchesToPoints( millimetersToInches( value ) );
    }

    /**
     * Measurement conversion from inches to points.
     *
     * @param value a value in inches
     * @return a value in points
     * @since 2.1.2
     */
    public static final float inchesToPoints( float value )
    {
        return value * 72f;
    }

    /**
     * Measurement conversion from millimeters to inches.
     *
     * @param value a value in millimeters
     * @return a value in inches
     * @since 2.1.2
     */
    public static final float millimetersToInches( float value )
    {
        return value / 25.4f;
    }

    public static String getDimensionAsPixel( String s )
    {
        // px: pixel units 1px is equal to 0.75pt.
        if ( s != null && s.endsWith( "*" ) )
        {
            return s;
        }
        return ( getDimensionAsPoint( s ) / 0.75f ) + "px";
    }

    public static String stripTrailingDigits( String fontName )
    {
        while ( fontName.length() > 0 && Character.isDigit( fontName.charAt( fontName.length() - 1 ) ) )
        {
            fontName = fontName.substring( 0, fontName.length() - 1 );
        }
        return fontName;
    }
}
