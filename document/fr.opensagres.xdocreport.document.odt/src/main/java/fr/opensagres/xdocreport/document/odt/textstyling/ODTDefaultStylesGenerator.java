/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.document.odt.textstyling;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.opensagres.xdocreport.document.textstyling.properties.ContainerProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ContainerProperties.ContainerType;
import fr.opensagres.xdocreport.document.textstyling.properties.TextAlignment;

/**
 * Default implementation : - uses OOo default styles for headers - uses default but renamed styles for others
 * 
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 */
public class ODTDefaultStylesGenerator
    implements IODTStylesGenerator
{
    protected static final String HEADER_PREFIX = "Heading_20_";
    
    protected static final Pattern HEADER_PATTERN = Pattern.compile(HEADER_PREFIX + "(\\d+).*");

    protected static final String[] BULLET_CHARS = { "\u2022", "\u25e6", "\u25aa" };

    protected static final String OL_STYLE_NAME = "XDocReport_OL";

    protected static final String UL_STYLE_NAME = "XDocReport_UL";

    protected static final String LIST_P_STYLE_NAME_SUFFIX = "_P";

    protected static final double BULLET_STEP = 0.635;

    protected static final String BOLD_STYLE_NAME = "XDocReport_Bold";

    protected static final String ITALIC_STYLE_NAME = "XDocReport_Italic";

    protected static final String UNDERLINE_STYLE_NAME = "XDocReport_Underline";

    protected static final String STRIKE_STYLE_NAME = "XDocReport_Strike";

    protected static final String SUBSCRIPT_STYLE_NAME = "XDocReport_Subscript";

    protected static final String SUPERSCRIPT_STYLE_NAME = "XDocReport_Superscript";

    protected static final String PAGE_BREAK_BEFORE_PARAGRAPH_STYLE_NAME = "XDocReport_ParaBreakBefore";

    protected static final String PAGE_BREAK_AFTER_PARAGRAPH_STYLE_NAME = "XDocReport_ParaBreakAfter";

    protected static final String EMPTY_TEXT_STYLE_NAME = "XDocReport_EmptyText";

    // font size for the default OOo Headers
    protected static String[] TITLE_FONT_SIZE = { "115%", "14pt", "14pt", "85%", "85%", "75%" };

    private long dynamicParagraphStylesNb = 0;

    private long dynamicTextStylesNb = 0;

    private final StringBuilder dynamicStyles;

    private String styleName = null;

    private ODTStyleProperties propertiesKind;

    private boolean hasPropertiesKind;

    private enum ODTStyleProperties
    {
        TEXT, PARAGRAPH
    }

    /**
     * protected static String[] HEADING_STYLES = new String[] {
     * "<style:style style:name=\"Heading_20_1\" style:display-name=\"Heading 1\" style:family=\"paragraph\" style:parent-style-name=\"Heading\" style:next-style-name=\"Text_20_body\" style:default-outline-level=\"1\" style:class=\"text\"><style:text-properties "
     * +
     * "fo:font-size=\"115%\" fo:font-weight=\"bold\" style:font-size-asian=\"115%\" style:font-weight-asian=\"bold\" style:font-size-complex=\"115%\" style:font-weight-complex=\"bold\"/></style:style>"
     * ,
     * "<style:style style:name=\"Heading_20_2\" style:display-name=\"Heading 2\" style:family=\"paragraph\" style:parent-style-name=\"Heading\" style:next-style-name=\"Text_20_body\" style:default-outline-level=\"2\" style:class=\"text\"><style:text-properties fo:font-size=\"14pt\" fo:font-style=\"italic\" fo:font-weight=\"bold\" style:font-size-asian=\"14pt\" style:font-style-asian=\"italic\" style:font-weight-asian=\"bold\" style:font-size-complex=\"14pt\" style:font-style-complex=\"italic\" style:font-weight-complex=\"bold\"/></style:style>"
     * ,
     * "<style:style style:name=\"Heading_20_3\" style:display-name=\"Heading 3\" style:family=\"paragraph\" style:parent-style-name=\"Heading\" style:next-style-name=\"Text_20_body\" style:default-outline-level=\"3\" style:class=\"text\"><style:text-properties fo:font-size=\"14pt\" fo:font-weight=\"bold\" style:font-size-asian=\"14pt\" style:font-weight-asian=\"bold\" style:font-size-complex=\"14pt\" style:font-weight-complex=\"bold\"/></style:style>"
     * ,
     * "<style:style style:name=\"Heading_20_4\" style:display-name=\"Heading 4\" style:family=\"paragraph\" style:parent-style-name=\"Heading\" style:next-style-name=\"Text_20_body\" style:default-outline-level=\"4\" style:class=\"text\"><style:text-properties fo:font-size=\"85%\" fo:font-style=\"italic\" fo:font-weight=\"bold\" style:font-size-asian=\"85%\" style:font-style-asian=\"italic\" style:font-weight-asian=\"bold\" style:font-size-complex=\"85%\" style:font-style-complex=\"italic\" style:font-weight-complex=\"bold\"/></style:style>"
     * ,
     * "<style:style style:name=\"Heading_20_5\" style:display-name=\"Heading 5\" style:family=\"paragraph\" style:parent-style-name=\"Heading\" style:next-style-name=\"Text_20_body\" style:default-outline-level=\"5\" style:class=\"text\"><style:text-properties fo:font-size=\"85%\" fo:font-weight=\"bold\" style:font-size-asian=\"85%\" style:font-weight-asian=\"bold\" style:font-size-complex=\"85%\" style:font-weight-complex=\"bold\"/></style:style>"
     * ,
     * "<style:style style:name=\"Heading_20_6\" style:display-name=\"Heading 6\" style:family=\"paragraph\" style:parent-style-name=\"Heading\" style:next-style-name=\"Text_20_body\" style:default-outline-level=\"6\" style:class=\"text\"><style:text-properties fo:font-size=\"75%\" fo:font-weight=\"bold\" style:font-size-asian=\"75%\" style:font-weight-asian=\"bold\" style:font-size-complex=\"75%\" style:font-weight-complex=\"bold\"/></style:style>"
     * };
     */

    public ODTDefaultStylesGenerator()
    {
        this.dynamicParagraphStylesNb = 0;
        this.dynamicTextStylesNb = 0;
        this.dynamicStyles = new StringBuilder();
    }

    protected String getBulletChar( int level )
    {
        return BULLET_CHARS[( level - 1 ) % 3];
    }

    public String getHeaderStyleName( int level )
    {
        return HEADER_PREFIX + level;
    }

    public String generateHeaderStyle( int level )
    {
        StringBuilder style = new StringBuilder( "<style:style style:name=\"" );
        style.append( getHeaderStyleName( level ) );
        style.append( "\" style:display-name=\"" );
        style.append( "Heading " + level );
        style.append( "\" style:family=\"paragraph\" style:parent-style-name=\"Heading\" style:next-style-name=\"Text_20_body\" " );
        style.append( "style:default-outline-level=\"" );
        style.append( level + "\" " );
        style.append( "style:class=\"text\"><style:text-properties " );
        style.append( "fo:font-size=\"" );
        style.append( TITLE_FONT_SIZE[level - 1] );
        style.append( "\" fo:font-weight=\"bold\" " );
        if ( level % 2 == 0 )
        {
            style.append( " fo:font-style=\"italic\" " );
        }
        style.append( "/></style:style>" );
        return style.toString();
    }

    public String generateTextStyles()
    {
        StringBuilder region = new StringBuilder();
        region.append( generateStyle( EMPTY_TEXT_STYLE_NAME, "" ) );
        region.append( generateStyle( BOLD_STYLE_NAME, "fo:font-weight=\"bold\"" ) );
        region.append( generateStyle( ITALIC_STYLE_NAME, "fo:font-style=\"italic\"" ) );
        region.append( generateStyle( UNDERLINE_STYLE_NAME,
                                      "style:text-underline-style=\"solid\" style:text-underline-width=\"auto\" style:text-underline-color=\"font-color\"" ) );
        region.append( generateStyle( STRIKE_STYLE_NAME,
                                      "style:text-line-through-style=\"solid\" style:text-underline-style=\"none\"" ) );
        region.append( generateStyle( SUBSCRIPT_STYLE_NAME, "style:text-position=\"sub\"" ) );
        region.append( generateStyle( SUPERSCRIPT_STYLE_NAME, "style:text-position=\"super\"" ) );
        return region.toString();
    }

    public String generateParagraphStyles()
    {
        StringBuilder region = new StringBuilder();
        region.append( generateStylePageBreak( true ) );
        region.append( generateStylePageBreak( false ) );
        return region.toString();
    }

    private String generateStyle( String styleName, String style )
    {
        StringBuilder region = new StringBuilder();
        region.append( "<style:style style:name=\"" );
        region.append( styleName );
        region.append( "\" style:family=\"text\">" );
        region.append( "<style:text-properties " );
        region.append( style );
        region.append( "/></style:style>" );
        return region.toString();
    }

    public String generateListStyle()
    {
        StringBuilder region = new StringBuilder();
        region.append( generateListStyle( true ) );
        region.append( generateListStyle( false ) );
        return region.toString();
    }

    protected String generateListStyle( boolean ordered )
    {
        StringBuilder region = new StringBuilder();

        String styleName = UL_STYLE_NAME;
        if ( ordered )
        {
            styleName = OL_STYLE_NAME;
        }

        // generate Paragraph styles reference
        region.append( "<style:style style:name=\"" );
        region.append( styleName + LIST_P_STYLE_NAME_SUFFIX );
        region.append( "\" style:family=\"paragraph\" style:parent-style-name=\"Standard\" style:list-style-name=\"" );
        region.append( styleName );
        region.append( "\"/>" );

        // generate the list style
        region.append( "<text:list-style style:name=\"" );
        region.append( styleName );
        region.append( "\">" );

        // generate styles for 10 levels
        for ( int level = 1; level <= 10; level++ )
        {
            region.append( generateBulletStyle( level, ordered ) );
        }
        region.append( "</text:list-style>" );

        return region.toString();
    }

    protected String generateBulletStyle( Integer level, boolean ordered )
    {
        StringBuilder region = new StringBuilder();

        if ( ordered )
        {
            region.append( "<text:list-level-style-number text:level=\"" );
        }
        else
        {
            region.append( "<text:list-level-style-bullet text:level=\"" );
        }
        region.append( level.toString() );
        region.append( "\" text:style-name=\"" );
        if ( ordered )
        {
            region.append( "Numbering_20_Symbols" );
        }
        else
        {
            region.append( "Bullet_20_Symbols" );
        }
        region.append( "\" style:num-suffix=\".\" " );
        if ( ordered )
        {
            region.append( "style:num-format=\"1\">" );
        }
        else
        {
            region.append( "text:bullet-char=\"" + getBulletChar( level ) + "\">" );
        }
        region.append( "<style:list-level-properties text:list-level-position-and-space-mode=\"label-alignment\">" );
        region.append( "<style:list-level-label-alignment text:label-followed-by=\"listtab\" " );

        DecimalFormatSymbols decimalFormat = new DecimalFormatSymbols();
        decimalFormat.setDecimalSeparator( '.' );
        String offset = new DecimalFormat( "#.###", decimalFormat ).format( BULLET_STEP * ( level + 1 ) );

        region.append( " text:list-tab-stop-position=\"" );
        region.append( offset );
        region.append( "cm\" fo:text-indent=\"-0.635cm\" fo:margin-left=\"" );
        region.append( offset );
        region.append( "cm\"/>" );

        region.append( "</style:list-level-properties>" );

        if ( ordered )
        {
            region.append( "</text:list-level-style-number>" );
        }
        else
        {
            region.append( "</text:list-level-style-bullet>" );
        }
        return region.toString();
    }

    public int getHeaderStylesCount()
    {
        return 6;
    }

    public int getHeaderStyleNameLevel( String styleName )
    {
        if ( styleName == null ) {
            return -1;
        }

        Matcher matcher = HEADER_PATTERN.matcher(styleName);
        if ( !matcher.find() )
        {
            return -1;
        }
        String headerIdx = matcher.group(1);
        return Integer.parseInt( headerIdx );
    }

    public String getOLStyleName()
    {
        return OL_STYLE_NAME;
    }

    public String getULStyleName()
    {
        return UL_STYLE_NAME;
    }

    public String getListItemParagraphStyleNameSuffix()
    {
        return LIST_P_STYLE_NAME_SUFFIX;
    }

    public String getBoldStyleName()
    {
        return BOLD_STYLE_NAME;
    }

    public String getItalicStyleName()
    {
        return ITALIC_STYLE_NAME;
    }

    //@Override
    public String getUnderlineStyleName()
    {
        return UNDERLINE_STYLE_NAME;
    }

    //@Override
    public String getStrikeStyleName()
    {
        return STRIKE_STYLE_NAME;
    }

    //@Override
    public String getSubscriptStyleName()
    {
        return SUBSCRIPT_STYLE_NAME;
    }

    //@Override
    public String getSuperscriptStyleName()
    {
        return SUPERSCRIPT_STYLE_NAME;
    }

    public String getParaBreakBeforeStyleName()
    {
        return PAGE_BREAK_BEFORE_PARAGRAPH_STYLE_NAME;
    }

    public String getParaBreakAfterStyleName()
    {
        return PAGE_BREAK_AFTER_PARAGRAPH_STYLE_NAME;
    }

    private String generateStylePageBreak( boolean pageBreakBefore )
    {
        String styleName =
            pageBreakBefore ? PAGE_BREAK_BEFORE_PARAGRAPH_STYLE_NAME : PAGE_BREAK_AFTER_PARAGRAPH_STYLE_NAME;
        StringBuilder style = new StringBuilder();
        style.append( "<style:style style:name=\"" );
        style.append( styleName );
        style.append( "\" style:family=\"paragraph\" >" );
        style.append( "<style:paragraph-properties" );
        if ( pageBreakBefore )
        {
            style.append( " fo:break-before=\"page\"" );
        }
        else
        {
            style.append( " fo:break-after=\"page\"" );
        }
        style.append( " />" );
        style.append( "</style:style>" );
        return style.toString();
    }

    //@Override
    public String generateAllStyles( ODTDefaultStyle defaultStyle )
    {
        StringBuilder styles = new StringBuilder();
        // Add bold, italic, bold+italic styles for text styling.
        styles.append( generateTextStyles() );
        // Add paragraph styles for text styling.
        styles.append( generateParagraphStyles() );
        // Add styles for lists
        styles.append( generateListStyle() );
        styles.append( getDynamicStyles() );
        return styles.toString();
    }

    public String getDynamicStyles()
    {
        return dynamicStyles.toString();
    }

    //@Override
    public String getTextStyleName( ContainerProperties properties )
    {
        if ( properties == null )
        {
            return EMPTY_TEXT_STYLE_NAME;
        }
        this.styleName = null;

        // <style:text-properties
        setPropertiesKind( ODTStyleProperties.TEXT );
        if ( properties.isBold() )
        {
            startStyleIfNeeded(properties.getType(), properties.getStyleName());
            dynamicStyles.append( "fo:font-weight=\"bold\" " );
        }
        if ( properties.isItalic() )
        {
            startStyleIfNeeded(properties.getType(), properties.getStyleName());
            dynamicStyles.append( "fo:font-style=\"italic\" " );
        }
        if ( properties.isUnderline() )
        {
            startStyleIfNeeded(properties.getType(), properties.getStyleName());
            dynamicStyles.append( "style:text-underline-style=\"solid\" style:text-underline-width=\"auto\" style:text-underline-color=\"font-color\" " );
            if ( properties.isStrike() )
            {
                dynamicStyles.append( "style:text-line-through-style=\"solid\" " );
            }
        }
        else
        {
            if ( properties.isStrike() )
            {
                startStyleIfNeeded(properties.getType(), properties.getStyleName());
                dynamicStyles.append( "style:text-underline-style=\"none\" style:text-line-through-style=\"solid\" " );
            }
        }
        if ( properties.isSubscript() )
        {
            startStyleIfNeeded(properties.getType(), properties.getStyleName());
            dynamicStyles.append( "style:text-position=\"sub\" " );
        }
        if ( properties.isSuperscript() )
        {
            startStyleIfNeeded(properties.getType(), properties.getStyleName());
            dynamicStyles.append( "style:text-position=\"super\" " );
        }

        // <style:paragraph-properties fo:text-align="center" style:justify-single-word="false" />
        setPropertiesKind( ODTStyleProperties.PARAGRAPH );
        TextAlignment textAlignment = properties.getTextAlignment();
        if ( textAlignment != null )
        {
            switch ( textAlignment )
            {
                case Center:
                    startStyleIfNeeded(properties.getType(), properties.getStyleName());
                    dynamicStyles.append( "fo:text-align=\"center\" style:justify-single-word=\"false\" " );
                    break;
                case Justify:
                    startStyleIfNeeded(properties.getType(), properties.getStyleName());
                    dynamicStyles.append( "fo:text-align=\"justify\" " );
                    break;
                case Left:
                    startStyleIfNeeded(properties.getType(), properties.getStyleName());
                    dynamicStyles.append( "fo:text-align=\"left\" " );
                    break;
                case Right:
                    startStyleIfNeeded(properties.getType(), properties.getStyleName());
                    dynamicStyles.append( "fo:text-align=\"right\" " );
                    break;
            }
        }
        endStyleIfNeeded();
        if (styleName == null && properties.getStyleName() != null) {
            styleName = properties.getStyleName();
        }
        return styleName;
    }

    private void setPropertiesKind( ODTStyleProperties propertiesKind )
    {
        this.propertiesKind = propertiesKind;
        this.hasPropertiesKind = false;
    }

    private void startStyleIfNeeded(ContainerType type, String baseStyleName)    {
        if ( this.styleName == null )
        {

            if ( ContainerType.PARAGRAPH.equals( type ) )
            {
                this.styleName = "XDocReport_P" + dynamicParagraphStylesNb++;
            }
            else
            {
                this.styleName = "XDocReport_T" + dynamicTextStylesNb++;
            }
            dynamicStyles.append( "<style:style style:name=\"" );
            dynamicStyles.append( styleName );
            if (baseStyleName != null) {
                dynamicStyles.append("\" style:parent-style-name=\"");
                dynamicStyles.append(baseStyleName);
            }
            if (ContainerType.PARAGRAPH.equals(type))            {
                dynamicStyles.append( "\" style:family=\"paragraph\">" );
            }
            else
            {
                dynamicStyles.append( "\" style:family=\"text\">" );
            }
            
        }
        if ( !this.hasPropertiesKind )
        {
            switch ( propertiesKind )
            {
                case TEXT:
                    dynamicStyles.append( "<style:text-properties " );
                    break;
                case PARAGRAPH:
                    dynamicStyles.append( "/><style:paragraph-properties " );
                    break;
                default:
                    break;
            }
            this.hasPropertiesKind = true;
        }
    }

    private void endStyleIfNeeded()
    {
        if ( this.styleName != null )
        {
            dynamicStyles.append( "/></style:style>" );
        }
    }
}
