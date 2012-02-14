package fr.opensagres.xdocreport.document.docx.textstyling;

import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.textstyling.AbstractStylesGenerator;
import fr.opensagres.xdocreport.document.textstyling.Style;

public class DocxDefaultStylesGenerator
    extends AbstractStylesGenerator<DefaultStyle>
    implements IDocxStylesGenerator
{

    public static final IDocxStylesGenerator INSTANCE = new DocxDefaultStylesGenerator();

    private static final Style XDocReport_Hyperlink_Style;

    private static final List<Style> XDocReport_Headings_Style;

    static
    {
        XDocReport_Headings_Style = new ArrayList<Style>();
        for ( int i = 1; i < getHeaderStylesCount() + 1; i++ )
        {
            XDocReport_Headings_Style.add( Style.load( "XDocReport_Heading_" + i, DocxDefaultStylesGenerator.class ) );
        }
        XDocReport_Hyperlink_Style = Style.load( "XDocReport_Hyperlink", DocxDefaultStylesGenerator.class );
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
            style.append( XDocReport_Hyperlink_Style.getContent() );
        }
    }

    public String getHyperLinkStyleId( DefaultStyle defaultStyle )
    {
        if ( defaultStyle == null )
        {
            return XDocReport_Hyperlink_Style.getId();
        }
        return StringUtils.isNotEmpty( defaultStyle.getHyperLinkStyleId() ) ? defaultStyle.getHyperLinkStyleId()
                        : XDocReport_Hyperlink_Style.getId();
    }

    public void generateHeadersStyle( StringBuilder styles, DefaultStyle defaultStyle )
    {
        for ( int i = 1; i < getHeaderStylesCount() + 1; i++ )
        {
            generateHeaderStyle( styles, defaultStyle, i );
        }

    }

    public void generateHeaderStyle( StringBuilder styles, DefaultStyle defaultStyle, int level )
    {
        if ( !defaultStyle.hasHeaderStyle( level ) )
        {
            styles.append( XDocReport_Headings_Style.get( level - 1 ).getContent() );
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
        return XDocReport_Headings_Style.get( level - 1 ).getId();
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
