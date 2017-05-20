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
package fr.opensagres.poi.xwpf.converter.xhtml.internal.styles;

import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.P_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.SPAN_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TABLE_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TD_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants.TR_ELEMENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.BACKGROUND_COLOR;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.COLOR;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.FONT_FAMILY;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.FONT_SIZE;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.FONT_STYLE;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.FONT_STYLE_ITALIC;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.FONT_WEIGHT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.FONT_WEIGHT_BOLD;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.HEIGHT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_LEFT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_RIGHT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_BOTTOM;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MARGIN_TOP;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.MIN_HEIGHT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.TEXT_ALIGN;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.TEXT_ALIGN_CENTER;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.TEXT_ALIGN_JUSTIFIED;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.TEXT_ALIGN_LEFT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.TEXT_ALIGN_RIGHT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.TEXT_DECORATION;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.TEXT_DECORATION_UNDERLINE;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.TEXT_INDENT;
import static fr.opensagres.poi.xwpf.converter.xhtml.internal.styles.CSSStylePropertyConstants.WIDTH;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextDirection;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import fr.opensagres.poi.xwpf.converter.core.Color;
import fr.opensagres.poi.xwpf.converter.core.TableHeight;
import fr.opensagres.poi.xwpf.converter.core.TableWidth;
import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.utils.StringUtils;
import fr.opensagres.poi.xwpf.converter.core.utils.XWPFUtils;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.utils.SAXHelper;

public class CSSStylesDocument
    extends XWPFStylesDocument
{

    private static final String CR = "\n";

    private static final String PT_UNIT = "pt";

    private static final Object PERCENT_UNIT = "%";

    private List<CSSStyle> cssStyles;

    private final boolean ignoreStylesIfUnused;

    private final Integer indent;

    public CSSStylesDocument( XWPFDocument document, boolean ignoreStylesIfUnused, Integer indent )
        throws XmlException, IOException
    {
        super( document, false );
        this.ignoreStylesIfUnused = ignoreStylesIfUnused;
        this.indent = indent;
        this.initialize();
    }

    @Override
    protected void initialize()
        throws XmlException, IOException
    {
        // default paragraph
        CSSStyle defaultPStyle = new CSSStyle( P_ELEMENT, null );
        defaultPStyle.addProperty( MARGIN_TOP, "0pt" );
        defaultPStyle.addProperty( MARGIN_BOTTOM, "1pt" );
        getCSSStyles().add( defaultPStyle );

        super.initialize();
    }

    @Override
    protected void visitStyle( CTStyle style, boolean defaultStyle )
    {
        String styleId = style.getStyleId();
        if ( StringUtils.isNotEmpty( styleId ) )
        {
            String className = getClassName( styleId );
            visitStyle( className, style.getPPr(), defaultStyle );
            visitStyle( className, style.getRPr(), defaultStyle );
            visitStyle( className, style.getTblPr(), defaultStyle );
            visitStyle( className, style.getTrPr(), defaultStyle );
            visitStyle( className, style.getTcPr(), defaultStyle );
        }
    }

    // -------------------------------- Paragraph styles

    /**
     * @param className
     * @param pPr
     * @param defaultStyle
     */
    private void visitStyle( String className, CTPPr pPr, boolean defaultStyle )
    {
        CSSStyle style = createCSSStyle( pPr, className );
        if ( style != null )
        {
            getCSSStyles().add( style );
        }
    }

    public CSSStyle createCSSStyle( CTPPr pPr )
    {
        return createCSSStyle( pPr, null );
    }

    public CSSStyle createCSSStyle( CTPPr pPr, String className )
    {
        if ( pPr != null )
        {
            String tagName = P_ELEMENT;
            CSSStyle style = ignoreStylesIfUnused ? null : getOrCreateStyle( null, tagName, className );
            
            // indentation left
            Float indentationLeft = super.getIndentationLeft( pPr );
            if ( indentationLeft != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( MARGIN_LEFT, getValueAsPoint( indentationLeft ) );
            }
            // indentation right
            Float indentationRight = super.getIndentationRight( pPr );
            if ( indentationRight != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( MARGIN_RIGHT, getValueAsPoint( indentationRight ) );
            }
            // indentation first line
            Float indentationFirstLine = super.getIndentationFirstLine( pPr );
            if ( indentationFirstLine != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( TEXT_INDENT, getValueAsPoint( indentationFirstLine ) );
            }

            // Aligment
            ParagraphAlignment alignment = super.getParagraphAlignment( pPr );
            if ( alignment != null )
            {
                switch ( alignment )
                {
                    case LEFT:
                        style = getOrCreateStyle( style, tagName, className );
                        style.addProperty( TEXT_ALIGN, TEXT_ALIGN_LEFT );
                        break;
                    case RIGHT:
                        style = getOrCreateStyle( style, tagName, className );
                        style.addProperty( TEXT_ALIGN, TEXT_ALIGN_RIGHT );
                        break;
                    case CENTER:
                        style = getOrCreateStyle( style, tagName, className );
                        style.addProperty( TEXT_ALIGN, TEXT_ALIGN_CENTER );
                        break;

                    case BOTH:
                        style = getOrCreateStyle( style, tagName, className );
                        style.addProperty( TEXT_ALIGN, TEXT_ALIGN_JUSTIFIED );
                        break;
                }
            }
            // Margin bottom/top
            Float spacingBefore = super.getSpacingBefore( pPr );
            if ( spacingBefore != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( MARGIN_TOP, getValueAsPoint( spacingBefore ) );
            }
            Float spacingAfter = super.getSpacingAfter( pPr );
            if ( spacingAfter != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( MARGIN_BOTTOM, getValueAsPoint( spacingAfter ) );
            }
            // Background color
            Color backgroundColor = super.getBackgroundColor( pPr );
            if ( backgroundColor != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( BACKGROUND_COLOR, XWPFUtils.toHexString( backgroundColor ) );
            }
            return style;
        }
        return null;
    }

    // -------------------------------- Run styles

    private void visitStyle( String className, CTRPr rPr, boolean defaultStyle )
    {

        CSSStyle style = createCSSStyle( rPr, className );
        if ( style != null )
        {
            getCSSStyles().add( style );
        }
    }

    public CSSStyle createCSSStyle( CTRPr rPr )
    {
        return createCSSStyle( rPr, null );
    }

    public CSSStyle createCSSStyle( CTRPr rPr, String className )
    {
        if ( rPr != null )
        {
            String tagName = SPAN_ELEMENT;
            CSSStyle style = ignoreStylesIfUnused ? null : getOrCreateStyle( null, tagName, className );

            // Font family
            String fontFamily = super.getFontFamilyAscii( rPr );
            if ( StringUtils.isNotEmpty( fontFamily ) )
            {
                fontFamily = new StringBuilder( "'" ).append( fontFamily ).append( "'" ).toString();
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( FONT_FAMILY, fontFamily );
            }
            // Font size
            Float fontSize = super.getFontSize( rPr );
            if ( fontSize != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( FONT_SIZE, getValueAsPoint( fontSize ) );
            }
            // Font Bold
            Boolean bold = super.getFontStyleBold( rPr );
            if ( bold != null && bold )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( FONT_WEIGHT, FONT_WEIGHT_BOLD );
            }
            // Font Italic
            Boolean italic = super.getFontStyleItalic( rPr );
            if ( italic != null && italic )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( FONT_STYLE, FONT_STYLE_ITALIC );
            }
            // Font color
            Color fontColor = super.getFontColor( rPr );
            if ( fontColor != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( COLOR, XWPFUtils.toHexString( fontColor ) );
            }
            // Underlines
            UnderlinePatterns underlinePatterns = super.getUnderline( rPr );
            if ( underlinePatterns != null )
            {
                switch ( underlinePatterns )
                {
                    case SINGLE:
                        style = getOrCreateStyle( style, tagName, className );
                        style.addProperty( TEXT_DECORATION, TEXT_DECORATION_UNDERLINE );
                        break;
                    default:
                        break;
                }
            }
            // Background color
            Color backgroundColor = super.getBackgroundColor( rPr );
            if ( backgroundColor != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( BACKGROUND_COLOR, XWPFUtils.toHexString( backgroundColor ) );
            }
            return style;
        }
        return null;
    }

    // -------------------------------- Table styles

    /**
     * @param className
     * @param tblPr
     * @param defaultStyle
     */
    private void visitStyle( String className, CTTblPrBase tblPr, boolean defaultStyle )
    {
        CSSStyle style = createCSSStyle( tblPr, className );
        if ( style != null )
        {
            getCSSStyles().add( style );
        }
    }

    public CSSStyle createCSSStyle( CTTblPrBase tblPr )
    {
        return createCSSStyle( tblPr, null );
    }

    public CSSStyle createCSSStyle( CTTblPrBase tblPr, String className )
    {
        if ( tblPr != null )
        {
            String tagName = TABLE_ELEMENT;
            CSSStyle style = ignoreStylesIfUnused ? null : getOrCreateStyle( null, tagName, className );

            TableWidth tableWidth = super.getTableWidth( tblPr );
            if ( tableWidth != null && tableWidth.width > 0 )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( WIDTH, getStyleWidth( tableWidth ) );
            }

            return style;
        }
        return null;
    }

    private String getStyleWidth( TableWidth tableWidth )
    {
        return tableWidth.percentUnit ? getValueAsPercent( tableWidth.width ) : getValueAsPoint( tableWidth.width );
    }

    // -------------------------------- Table row styles

    /**
     * @param className
     * @param trPr
     * @param defaultStyle
     */
    private void visitStyle( String className, CTTrPr trPr, boolean defaultStyle )
    {
        CSSStyle style = createCSSStyle( trPr, className );
        if ( style != null )
        {
            getCSSStyles().add( style );
        }
    }

    public CSSStyle createCSSStyle( CTTrPr trPr )
    {
        return createCSSStyle( trPr, null );
    }

    public CSSStyle createCSSStyle( CTTrPr trPr, String className )
    {
        if ( trPr != null )
        {
            String tagName = TR_ELEMENT;
            CSSStyle style = ignoreStylesIfUnused ? null : getOrCreateStyle( null, tagName, className );

            // min-height / height
            TableHeight tableHeight = super.getTableRowHeight( trPr );
            if ( tableHeight != null )
            {
                if ( tableHeight.minimum )
                {
                    style = getOrCreateStyle( style, tagName, className );
                    style.addProperty( MIN_HEIGHT, getValueAsPoint( tableHeight.height ) );
                }
                else
                {
                    style = getOrCreateStyle( style, tagName, className );
                    style.addProperty( HEIGHT, getValueAsPoint( tableHeight.height ) );
                }
            }
            return style;
        }
        return null;
    }

    // -------------------------------- Table cell styles

    /**
     * @param className
     * @param tcPr
     * @param defaultStyle
     */
    private void visitStyle( String className, CTTcPr tcPr, boolean defaultStyle )
    {
        CSSStyle style = createCSSStyle( tcPr, className );
        if ( style != null )
        {
            getCSSStyles().add( style );
        }
    }

    public CSSStyle createCSSStyle( CTTcPr tcPr )
    {
        return createCSSStyle( tcPr, null );
    }

    public CSSStyle createCSSStyle( CTTcPr tcPr, String className )
    {
        if ( tcPr != null )
        {
            String tagName = TD_ELEMENT;
            CSSStyle style = ignoreStylesIfUnused ? null : getOrCreateStyle( null, tagName, className );

            // cell width
            TableWidth cellWidth = super.getTableCellWith( tcPr );
            if ( cellWidth != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( WIDTH, getStyleWidth( cellWidth ) );
            }
            // background color
            Color backgroundColor = super.getTableCellBackgroundColor( tcPr );
            if ( backgroundColor != null )
            {
                style = getOrCreateStyle( style, tagName, className );
                style.addProperty( BACKGROUND_COLOR, XWPFUtils.toHexString( backgroundColor ) );
            }
            CTTextDirection direction = super.getTextDirection( tcPr );
            if ( direction != null )
            {
                // see http://www.css3maker.com/text-rotation.html
                int dir = direction.getVal().intValue();
                switch ( dir )
                {
                    case STTextDirection.INT_BT_LR:
                        style = getOrCreateStyle( style, tagName, className );
                        style.addProperty( "-webkit-transform", "rotate(270deg)" );
                        style.addProperty( "-moz-transform", "rotate(270deg)" );
                        style.addProperty( "-o-transform", "rotate(270deg)" );
                        style.addProperty( "writing-mode", "bt-lr" ); // For IE
                        break;
                    case STTextDirection.INT_TB_RL:
                        style = getOrCreateStyle( style, tagName, className );
                        style.addProperty( "-webkit-transform", "rotate(90deg)" );
                        style.addProperty( "-moz-transform", "rotate(90deg)" );
                        style.addProperty( "-o-transform", "rotate(90deg)" );                        
                        style.addProperty( "writing-mode", "tb-rl" ); // For IE
                        break;
                }
            }
            return style;
        }
        return null;
    }

    public List<CSSStyle> getCSSStyles()
    {
        if ( cssStyles == null )
        {
            cssStyles = new ArrayList<CSSStyle>();
        }
        return cssStyles;
    }

    public void save( ContentHandler contentHandler )
        throws SAXException
    {
        List<CSSStyle> cssStyles = getCSSStyles();
        if ( ignoreStylesIfUnused && cssStyles.size() < 1 )
        {
            return;
        }

        SAXHelper.startElement( contentHandler, XHTMLConstants.STYLE_ELEMENT, null );
        for ( CSSStyle style : cssStyles )
        {
            if ( indent != null )
            {
                SAXHelper.characters( contentHandler, CR );
            }
            style.save( contentHandler );
        }
        SAXHelper.endElement( contentHandler, XHTMLConstants.STYLE_ELEMENT );
    }

    public String getValueAsPoint( float value )
    {
        return new StringBuilder( String.valueOf( value ) ).append( PT_UNIT ).toString();
    }

    private String getValueAsPercent( float value )
    {
        return new StringBuilder( String.valueOf( value ) ).append( PERCENT_UNIT ).toString();
    }

    public String getClassNames( String styleID )
    {
        if ( StringUtils.isEmpty( styleID ) )
        {
            return null;
        }
        StringBuilder classNames = new StringBuilder( getClassName( styleID ) );
        CTStyle style = super.getStyle( styleID );
        while ( style != null )
        {
            style = super.getStyle( style.getBasedOn() );
            if ( style != null )
            {
                classNames.insert( 0, getClassName( style.getStyleId(), true ) );
            }
        }

        return classNames.toString();
    }

    private String getClassName( String styleId )
    {
        return getClassName( styleId, false );
    }

    private String getClassName( String styleId, boolean spaceAfter )
    {
        StringBuilder className = new StringBuilder();
        char firstChar = styleId.charAt( 0 );
        if ( Character.isDigit( firstChar ) )
        {
            // class name must not started with a number (Chrome doesn't works with that).
            className.append( 'X' ).toString();
        }
        if ( spaceAfter )
        {
            return className.append( styleId ).append( ' ' ).toString();
        }
        return className.append( styleId ).toString();
    }

    private CSSStyle getOrCreateStyle( CSSStyle style, String tagName, String className )
    {
        if ( style != null )
        {
            return style;
        }
        return new CSSStyle( tagName, className );
    }

    // private String () {
    // // see http://www.css3maker.com/text-rotation.html
    // -webkit-transform: rotate(90deg);
    // -moz-transform: rotate(90deg);
    // -ms-transform: matrix(1.239,-0.124,-0.547,0.843,0,0);
    // -o-transform: matrix(1.239,-0.124,-0.547,0.843,0,0);
    // transform: matrix(1.239,-0.124,-0.547,0.843,0,0);
    // }
}
