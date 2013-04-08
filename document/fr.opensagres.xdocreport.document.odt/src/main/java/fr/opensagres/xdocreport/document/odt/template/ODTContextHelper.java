package fr.opensagres.xdocreport.document.odt.template;

import java.util.Map;

import fr.opensagres.xdocreport.document.odt.textstyling.IODTStylesGenerator;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTDefaultStyle;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTDefaultStylesGenerator;
import fr.opensagres.xdocreport.template.IContext;

public class ODTContextHelper
{

    public static final String DEFAULT_STYLE_KEY = "___DefaultStyle";

    public static final String STYLES_GENERATOR_KEY = "___NoEscapeStylesGenerator";

    public static void putStylesGenerator( IContext context, IODTStylesGenerator stylesGenerator )
    {
        context.put( STYLES_GENERATOR_KEY, stylesGenerator );
    }

    public static IODTStylesGenerator getStylesGenerator( IContext context )
    {
        IODTStylesGenerator stylesGenerator = (IODTStylesGenerator) context.get( STYLES_GENERATOR_KEY );
        if ( stylesGenerator == null )
        {
            stylesGenerator = new ODTDefaultStylesGenerator();
            putStylesGenerator( context, stylesGenerator );
        }
        return stylesGenerator;
    }

    public static void putDefaultStyle( IContext context, ODTDefaultStyle defaultStyle )
    {
        context.put( DEFAULT_STYLE_KEY, defaultStyle );
    }

    public static ODTDefaultStyle getDefaultStyle( IContext context )
    {
        return (ODTDefaultStyle) context.get( DEFAULT_STYLE_KEY );
    }

    public static ODTDefaultStyle getDefaultStyle( Map<String, Object> sharedContext )
    {
        ODTDefaultStyle defaultStyle = (ODTDefaultStyle) sharedContext.get( ODTContextHelper.DEFAULT_STYLE_KEY );
        if ( defaultStyle == null )
        {
            defaultStyle = new ODTDefaultStyle();
            sharedContext.put( ODTContextHelper.DEFAULT_STYLE_KEY, defaultStyle );
        }
        return defaultStyle;
    }

}
