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
package fr.opensagres.xdocreport.document.textstyling.html;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.textstyling.properties.ContainerProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.HeaderProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListItemProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ParagraphProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.SpanProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableCellProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableRowProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TextAlignment;

/**
 * Styles Helper.
 */
public class StylesHelper
{

    public static Map<String, String> parse( String style )
    {
        if ( StringUtils.isEmpty( style ) )
        {
            return Collections.emptyMap();
        }
        Map<String, String> stylesMap = new HashMap<String, String>();
        String[] styles = style.split( ";" );
        String s = null;
        int index = 0;
        String name = null;
        String value = null;
        for ( int i = 0; i < styles.length; i++ )
        {
            name = null;
            value = null;
            s = styles[i];
            index = s.indexOf( ':' );
            if ( index != -1 )
            {
                name = s.substring( 0, index ).trim();
                value = s.substring( index + 1, s.length() ).trim();
                stylesMap.put( name, value );
            }
        }
        return stylesMap;
    }

    /**
     * Create {@link ParagraphProperties} from inline style.
     * 
     * @param style
     * @return
     */
    public static ParagraphProperties createParagraphProperties( String style )
    {
        Map<String, String> stylesMap = StylesHelper.parse( style );
        if ( !stylesMap.isEmpty() )
        {
            ParagraphProperties properties = new ParagraphProperties();
            processContainerproperties(properties, stylesMap);
            return properties;
        }
        return null;
    }

    /**
     * Create {@link HeaderProperties} from inline style.
     * 
     * @param style
     * @return
     */
    public static HeaderProperties createHeaderProperties( String style )
    {
        Map<String, String> stylesMap = StylesHelper.parse( style );
        if ( !stylesMap.isEmpty() )
        {
            HeaderProperties properties = new HeaderProperties();
            processContainerproperties( properties, stylesMap );
            return properties;
        }
        return null;
    }

    /**
     * Create {@link ListItemProperties} from inline style.
     * 
     * @param style
     * @return
     */
    public static ListItemProperties createListItemProperties( String style )
    {
        Map<String, String> stylesMap = StylesHelper.parse( style );
        if ( !stylesMap.isEmpty() )
        {
            ListItemProperties properties = new ListItemProperties();
            processContainerproperties( properties, stylesMap );
            return properties;
        }
        return null;
    }

    /**
     * Create {@link ListProperties} from inline style.
     * 
     * @param style
     * @return
     */
    public static ListProperties createListProperties( String style )
    {
        Map<String, String> stylesMap = StylesHelper.parse( style );
        if ( !stylesMap.isEmpty() )
        {
            ListProperties properties = new ListProperties();
            processContainerproperties( properties, stylesMap );
            return properties;
        }
        return null;
    }

    /**
     * Create {@link SpanProperties} from inline style.
     * 
     * @param style
     * @return
     */
    public static SpanProperties createSpanProperties( String style )
    {
        Map<String, String> stylesMap = StylesHelper.parse( style );
        if ( !stylesMap.isEmpty() )
        {
            SpanProperties properties = new SpanProperties();
            processContainerproperties( properties, stylesMap );
            return properties;
        }
        return null;
    }

    private static void processContainerproperties( ContainerProperties properties, Map<String, String> stylesMap )
    {
        // page-break-before
        properties.setPageBreakBefore( false );
        if ( "always".equals( stylesMap.get( "page-break-before" ) ) )
        {
            properties.setPageBreakBefore( true );
        }

        // page-break-after
        properties.setPageBreakAfter( false );
        if ( "always".equals( stylesMap.get( "page-break-after" ) ) )
        {
            properties.setPageBreakAfter( true );
        }

        // font-weight
        String fontWeight = stylesMap.get( "font-weight" );
        properties.setBold( false );
        if ( fontWeight != null )
        {
            if ( "bold".equals( fontWeight ) || "700".equals( fontWeight ) )
            {
                properties.setBold( true );
            }
        }

        // font-style
        String fontStyle = stylesMap.get( "font-style" );
        properties.setItalic( false );
        if ( fontStyle != null )
        {
            if ( "italic".equals( fontStyle ) )
            {
                properties.setItalic( true );
            }
        }

        // text-decoration
        String textDecoration = stylesMap.get( "text-decoration" );
        properties.setStrike( false );
        properties.setUnderline( false );
        if ( textDecoration != null )
        {
            if ( textDecoration.contains( "underline" ) )
            {
                properties.setUnderline( true );
            }

            if ( textDecoration.contains( "line-through" ) )
            {
                properties.setStrike( true );
            }
        }

        // vertical-align
        String verticalAlign = stylesMap.get( "vertical-align" );
        properties.setSuperscript( false );
        properties.setSubscript( false );
        if ( verticalAlign != null )
        {
            if ( "sub".equals( verticalAlign ) )
            {
                properties.setSubscript( true );
            }
            else if ( "super".equals( verticalAlign ) )
            {
                properties.setSuperscript( true );
            }
        }

        // text-align
        String textAlignment = stylesMap.get( "text-align" );
        if ( textAlignment != null )
        {
            if ( "left".equals( textAlignment ) )
            {
                properties.setTextAlignment( TextAlignment.Left );
            }
            else if ( "center".equals( textAlignment ) )
            {
                properties.setTextAlignment( TextAlignment.Center );
            }
            else if ( "right".equals( textAlignment ) )
            {
                properties.setTextAlignment( TextAlignment.Right );
            }
            else if ( "justify".equals( textAlignment ) )
            {
                properties.setTextAlignment( TextAlignment.Justify );
            }
            else if ( "inherit".equals( textAlignment ) )
            {
                properties.setTextAlignment( TextAlignment.Inherit );
            }

        }
        // style
        String styleName = stylesMap.get("name");
        if (styleName != null) {
            properties.setStyleName(styleName);
        }
    }

    public static TableProperties createTableProperties( Attributes attributes )
    {
        TableProperties properties = new TableProperties();
        return properties;
    }

    public static TableRowProperties createTableRowProperties( Attributes attributes )
    {
        TableRowProperties properties = new TableRowProperties();
        return properties;
    }

    public static TableCellProperties createTableCellProperties( Attributes attributes )
    {
        TableCellProperties properties = new TableCellProperties();
        return properties;
    }
}
