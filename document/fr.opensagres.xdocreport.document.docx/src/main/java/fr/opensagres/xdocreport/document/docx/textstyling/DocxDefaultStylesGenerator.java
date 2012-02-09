package fr.opensagres.xdocreport.document.docx.textstyling;

public class DocxDefaultStylesGenerator
    implements IDocxStylesGenerator
{

    private static final String HYPERLINK_STYLE = "XDocReport_Hyperlink";

    private static final String HYPERLINK_STYLE_ID = "XDocReport_Hyperlink";

    public String generateHyperlinkStyle()
    {
        StringBuilder style = new StringBuilder();
        style.append( "<w:style w:type=\"character\" w:styleId=\"" );
        style.append( getHyperLinkStyleId() );
        style.append( "\">" );
        style.append( "<w:name w:val=\"Hyperlink\" />" );
        //style.append( "<w:basedOn w:val=\"Policepardfaut\" />" );
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

    public String getHyperLinkStyleId()
    {
        return HYPERLINK_STYLE_ID;
    }
}
