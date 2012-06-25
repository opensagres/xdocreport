package fr.opensagres.xdocreport.document.textstyling.html;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.textstyling.properties.ContainerProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.HeaderProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListItemProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ParagraphProperties;

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
            processContainerproperties( properties, stylesMap );
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
    
    private static void processContainerproperties( ContainerProperties properties, Map<String, String> stylesMap )
    {
        properties.setPageBreakBefore( false );
        if ( "always".equals( stylesMap.get( "page-break-before" ) ) )
        {
            properties.setPageBreakBefore( true );
        }
        properties.setPageBreakAfter( false );
        if ( "always".equals( stylesMap.get( "page-break-after" ) ) )
        {
            properties.setPageBreakAfter( true );
        }
    }

}
