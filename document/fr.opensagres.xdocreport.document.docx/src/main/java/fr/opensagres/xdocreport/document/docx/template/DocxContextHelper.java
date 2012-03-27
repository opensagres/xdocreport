package fr.opensagres.xdocreport.document.docx.template;

import java.util.Map;

import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks.HyperlinkRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks.HyperlinkUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.NoteRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering.NumberingRegistry;
import fr.opensagres.xdocreport.document.docx.textstyling.DocxDefaultStylesGenerator;
import fr.opensagres.xdocreport.document.docx.textstyling.IDocxStylesGenerator;
import fr.opensagres.xdocreport.template.IContext;

public class DocxContextHelper
{
    public static final String DEFAULT_STYLE_KEY = "___DefaultStyle";

    public static final String STYLES_GENERATOR_KEY = "___NoEscapeStylesGenerator";

    public static final String NUMBERING_REGISTRY_KEY = "___NumberingRegistry";
    
    public static final String FOOTNOTE_REGISTRY_KEY = "___FootnoteRegistry";

    public static final String ENDNOTE_REGISTRY_KEY = "___EndnoteRegistry";
    
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

    public static DefaultStyle getDefaultStyle( Map<String, Object> sharedContext )
    {
        DefaultStyle defaultStyle = (DefaultStyle) sharedContext.get( DocxContextHelper.DEFAULT_STYLE_KEY );
        if ( defaultStyle == null )
        {
            defaultStyle = new DefaultStyle();
            sharedContext.put( DocxContextHelper.DEFAULT_STYLE_KEY, defaultStyle );
        }
        return defaultStyle;
    }

    public static void putNumberingRegistry( IContext context, NumberingRegistry registry )
    {
        context.put( NUMBERING_REGISTRY_KEY, registry );
    }

    public static NumberingRegistry getNumberingRegistry( IContext context )
    {
        return (NumberingRegistry) context.get( NUMBERING_REGISTRY_KEY );
    }

    public static void putFootnoteRegistry( IContext context, NoteRegistry registry )
    {
        context.put( FOOTNOTE_REGISTRY_KEY, registry );

    }

    public static void putEndnoteRegistry( IContext context, NoteRegistry registry )
    {
        context.put( ENDNOTE_REGISTRY_KEY, registry );

    }
}
