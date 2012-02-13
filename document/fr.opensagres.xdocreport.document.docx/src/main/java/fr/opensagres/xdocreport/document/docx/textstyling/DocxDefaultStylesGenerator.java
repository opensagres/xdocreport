package fr.opensagres.xdocreport.document.docx.textstyling;

import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;

public class DocxDefaultStylesGenerator
    implements IDocxStylesGenerator
{

    public static final IDocxStylesGenerator INSTANCE = new DocxDefaultStylesGenerator();

    private static final String DEFAULT_HYPERLINK_STYLE_ID = "XDocReport_Hyperlink";

    private static final List<String> DEFAULT_HEADERS_STYLE_ID;

    static
    {
        DEFAULT_HEADERS_STYLE_ID = new ArrayList<String>();
        for ( int i = 0; i < getHeaderStylesCount(); i++ )
        {
            DEFAULT_HEADERS_STYLE_ID.add( "XDocReport_Heading_" + i );
        }
    }

    public String generateAllStyles( DefaultStyle defaultStyle )
    {
        StringBuilder styles = new StringBuilder();
        generateHyperlinkStyle( styles, defaultStyle );
        generateHeadersStyle( styles, defaultStyle );
        return styles.toString();
    }

    public void generateHyperlinkStyle( StringBuilder style, DefaultStyle defaultStyle )
    {
        if ( !defaultStyle.hasHyperLinkStyleId() )
        {
            String hyperLinkStyleId = DEFAULT_HYPERLINK_STYLE_ID;
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
        }
    }

    public String getHyperLinkStyleId( DefaultStyle defaultStyle )
    {
        if ( defaultStyle == null )
        {
            return DEFAULT_HYPERLINK_STYLE_ID;
        }
        return StringUtils.isNotEmpty( defaultStyle.getHyperLinkStyleId() ) ? defaultStyle.getHyperLinkStyleId()
                        : DEFAULT_HYPERLINK_STYLE_ID;
    }

    public void generateHeadersStyle( StringBuilder styles, DefaultStyle defaultStyle )
    {
        for ( int i = 0; i < getHeaderStylesCount(); i++ )
        {
            generateHeaderStyle( styles, defaultStyle, i );
        }

    }

    public void generateHeaderStyle( StringBuilder styles, DefaultStyle defaultStyle, int level )
    {
        if ( !defaultStyle.hasHeaderStyle( level ) )
        {
            styles.append( "<w:style w:type=\"paragraph\" w:styleId=\"" );
            styles.append( getDefaultHeaderStyleId( level + 1 ) );
            styles.append( "\">" );
            styles.append( "<w:name w:val=\"heading " );
            styles.append( level + 1 );
            styles.append( "\" />" );

            styles.append( "<w:uiPriority w:val=\"9\" />" );
            styles.append( "<w:unhideWhenUsed />" );

            styles.append( "<w:pPr> " );
            styles.append( "<w:keepNext />" );
            styles.append( "<w:keepLines />" );
            styles.append( "<w:spacing w:before=\"480\" w:after=\"0\" />" );
            styles.append( "<w:outlineLvl w:val=\"" );
            styles.append( level );
            styles.append( "\"/>" );
            styles.append( "</w:pPr> " );

            styles.append( "<w:rPr>" );
            styles.append( "<w:rFonts w:asciiTheme=\"majorHAnsi\" w:eastAsiaTheme=\"majorEastAsia\"" );
            styles.append( " w:hAnsiTheme=\"majorHAnsi\" w:cstheme=\"majorBidi\" />" );

            if ( level < 4 )
            {
                styles.append( "<w:b />" );
                styles.append( "<w:bCs />" );
            }

            styles.append( "<w:color w:val=\"365F91\" w:themeColor=\"accent1\" w:themeShade=\"BF\" />" );

            switch ( level )
            {
                case 0:
                    styles.append( "<w:sz w:val=\"28\" />" );
                    styles.append( "<w:szCs w:val=\"28\" />" );
                    break;
                case 1:
                    styles.append( "<w:sz w:val=\"26\" />" );
                    styles.append( "<w:szCs w:val=\"26\" />" );
                    break;
            }

            styles.append( "</w:rPr>" );

            styles.append( "</w:style>" );

            // * <w:style w:type="paragraph" w:styleId="Titre1"> <w:name w:val="heading 1" /> <w:basedOn w:val="Normal"
            // />
            // * <w:next w:val="Normal" /> <w:link w:val="Titre1Car" /> <w:uiPriority w:val="9" /> <w:rsid
            // w:val="00285B63" />
            // * <w:pPr> <w:keepNext /> <w:keepLines /> <w:spacing w:before="480" w:after="0" /> <w:outlineLvl w:val="0"
            // />
            // * </w:pPr> <w:rPr> <w:rFonts w:asciiTheme="majorHAnsi" w:eastAsiaTheme="majorEastAsia"
            // * w:hAnsiTheme="majorHAnsi" w:cstheme="majorBidi" /> <w:b /> <w:bCs /> <w:color w:val="365F91"
            // * w:themeColor="accent1" w:themeShade="BF" /> <w:sz w:val="28" /> <w:szCs w:val="28" /> </w:rPr>
            // </w:style>
        }
    }

    public String getHeaderStyleId( int level, DefaultStyle defaultStyle )
    {
        if ( defaultStyle == null )
        {
            return getDefaultHeaderStyleId( level );
        }
        if ( !defaultStyle.hasHeaderStyle( level ) )
        {
            return getDefaultHeaderStyleId( level );
        }
        return defaultStyle.getHeaderStyle( level );
    }

    private String getDefaultHeaderStyleId( int level )
    {
        return DEFAULT_HEADERS_STYLE_ID.get( level - 1 );
    }

    public void f()
    {
        /**
         * <w:style w:type="paragraph" w:styleId="Titre1"> <w:name w:val="heading 1" /> <w:basedOn w:val="Normal" />
         * <w:next w:val="Normal" /> <w:link w:val="Titre1Car" /> <w:uiPriority w:val="9" /> <w:rsid w:val="00285B63" />
         * <w:pPr> <w:keepNext /> <w:keepLines /> <w:spacing w:before="480" w:after="0" /> <w:outlineLvl w:val="0" />
         * </w:pPr> <w:rPr> <w:rFonts w:asciiTheme="majorHAnsi" w:eastAsiaTheme="majorEastAsia"
         * w:hAnsiTheme="majorHAnsi" w:cstheme="majorBidi" /> <w:b /> <w:bCs /> <w:color w:val="365F91"
         * w:themeColor="accent1" w:themeShade="BF" /> <w:sz w:val="28" /> <w:szCs w:val="28" /> </w:rPr> </w:style>
         **/
    }

    /**
     * @return number of available header styles
     */
    public static int getHeaderStylesCount()
    {
        return 6;
    }
}
