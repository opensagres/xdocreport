package fr.opensagres.xdocreport.document.docx.template;

import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkUtils;
import fr.opensagres.xdocreport.template.IContext;

public class DocxContextHelper
{
    public static final String FONT_KEY = "___Font";

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

    public static void putFont( IContext context, String font )
    {
        context.put( FONT_KEY, font );
    }
}
