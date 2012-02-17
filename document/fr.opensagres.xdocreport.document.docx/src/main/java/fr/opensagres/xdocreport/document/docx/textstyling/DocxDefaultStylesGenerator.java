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

    private static final int DEFAULT_NUMID_ORDERED = 0;

    private static final int DEFAULT_NUMID_UNORDERED = 1;

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

    /**
     * @return number of available header styles
     */
    public static int getHeaderStylesCount()
    {
        return 6;
    }

    public int getNumIdForList( boolean ordered, DefaultStyle defaultStyle )
    {
        if ( ordered )
        {
            if ( defaultStyle == null )
            {
                return DEFAULT_NUMID_ORDERED;
            }
            return defaultStyle.getNumIdForOrdererList() != DefaultStyle.DEFAULT_NUMID ? defaultStyle.getNumIdForOrdererList()
                            : DEFAULT_NUMID_ORDERED;
        }
        if ( defaultStyle == null )
        {
            return DEFAULT_NUMID_UNORDERED;
        }
        return defaultStyle.getNumIdForOrdererList() != DefaultStyle.DEFAULT_NUMID ? defaultStyle.getNumIdForUnordererList()
                        : DEFAULT_NUMID_UNORDERED;
    }
}
