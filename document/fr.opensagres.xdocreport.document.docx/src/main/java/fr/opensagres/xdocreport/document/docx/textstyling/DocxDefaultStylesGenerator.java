package fr.opensagres.xdocreport.document.docx.textstyling;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;

public class DocxDefaultStylesGenerator
    implements IDocxStylesGenerator
{

    public static final IDocxStylesGenerator INSTANCE = new DocxDefaultStylesGenerator();

    private static final String HYPERLINK_STYLE_ID = "XDocReport_Hyperlink";

    public String generateHyperlinkStyle( DefaultStyle defaultStyle )
    {
        String hyperLinkStyleId = getHyperLinkStyleId( defaultStyle );
        StringBuilder style = new StringBuilder();
        style.append( "<w:style w:type=\"character\" w:styleId=\"" );
        style.append( hyperLinkStyleId );
        style.append( "\">" );
        style.append( "<w:name w:val=\"Hyperlink\" />" );
        // style.append( "<w:basedOn w:val=\"Policepardfaut\" />" );
        style.append( "<w:uiPriority w:val=\"99\" />" );
        style.append( "<w:unhideWhenUsed />" );
        style.append( "<w:rsid w:val=\"001D30B5\" />" );
        style.append( "<w:rPr>" );
        style.append( "<w:color w:val=\"0000FF\" w:themeColor=\"hyperlink\" />" );
        style.append( "<w:u w:val=\"single\" />" );
        style.append( "</w:rPr>" );
        style.append( "</w:style>" );

        return style.toString();
    }

    public String getHyperLinkStyleId( DefaultStyle defaultStyle )
    {
        if ( defaultStyle == null )
        {
            return HYPERLINK_STYLE_ID;
        }
        return StringUtils.isNotEmpty( defaultStyle.getHyperLinkStyleId() ) ? defaultStyle.getHyperLinkStyleId()
                        : HYPERLINK_STYLE_ID;
    }

}
