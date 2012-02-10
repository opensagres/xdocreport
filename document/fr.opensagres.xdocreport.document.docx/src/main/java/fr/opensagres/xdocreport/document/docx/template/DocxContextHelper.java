package fr.opensagres.xdocreport.document.docx.template;

import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkUtils;
import fr.opensagres.xdocreport.document.docx.textstyling.DocxDefaultStylesGenerator;
import fr.opensagres.xdocreport.document.docx.textstyling.IDocxStylesGenerator;
import fr.opensagres.xdocreport.template.IContext;

public class DocxContextHelper
{
    public static final String DEFAULT_STYLE_KEY = "___DefaultStyle";

    public static final String STYLES_GENERATOR_KEY = "___StylesGenerator";

    public static void putHyperlinkRegistry( IContext context, String entryName, HyperlinkRegistry registry )
    {
        String key = HyperlinkUtils.getHyperlinkRegistryKey( entryName );
        context.put( key, registry );
    }

    public static HyperlinkRegistry getHyperlinkRegistry( IContext context, String entryName )
    {
        String key = HyperlinkUtils.getHyperlinkRegistryKey( entryName );
        return (HyperlinkRegistry) context.get( key );
    }

    public static void putStylesGenerator( IContext context, IDocxStylesGenerator stylesGenerator )
    {
        context.put( STYLES_GENERATOR_KEY, stylesGenerator );
    }

    public static IDocxStylesGenerator getStylesGenerator( IContext context )
    {
        IDocxStylesGenerator stylesGenerator = (IDocxStylesGenerator) context.get( STYLES_GENERATOR_KEY );
        if ( stylesGenerator == null )
        {
            stylesGenerator = DocxDefaultStylesGenerator.INSTANCE;
            context.put( STYLES_GENERATOR_KEY, stylesGenerator );
        }
        return stylesGenerator;
    }

    public static void putDefaultStyle( IContext context, DefaultStyle defaultStyle )
    {
        context.put( DEFAULT_STYLE_KEY, defaultStyle );
    }

    public static DefaultStyle getDefaultStyle( IContext context )
    {
        return (DefaultStyle) context.get( DEFAULT_STYLE_KEY );
    }

}
