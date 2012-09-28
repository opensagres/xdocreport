package org.apache.poi.xwpf.converter.xhtml.internal.styles;

import static org.apache.poi.xwpf.converter.xhtml.XHTMLConstants.P_ELEMENT;
import static org.apache.poi.xwpf.converter.xhtml.XHTMLConstants.SPAN_ELEMENT;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.core.utils.XWPFUtils;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConstants;
import org.apache.poi.xwpf.converter.xhtml.internal.utils.SAXHelper;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class CSSStylesDocument
    extends XWPFStylesDocument
{

    private static final String PT = "pt";

    private List<CSSStyle> cssStyles;

    public CSSStylesDocument( XWPFDocument document )
        throws XmlException, IOException
    {
        super( document );
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
            CSSStyle style = new CSSStyle( P_ELEMENT, className );
            // indentation left
            Float indentationLeft = super.getIndentationLeft( pPr );
            if ( indentationLeft != null )
            {
                style.addProperty( "text-align", "left" );
                style.addProperty( "text-indent", getValue( indentationLeft ) );
            }
            // indentation right
            Float indentationRight = super.getIndentationRight( pPr );
            if ( indentationRight != null )
            {
                style.addProperty( "text-align", "right" );
                style.addProperty( "text-indent", getValue( indentationRight ) );
            }

            // Aligment
            ParagraphAlignment alignment = super.getParagraphAlignment( pPr );
            if ( alignment != null )
            {
                switch ( alignment )
                {
                    case LEFT:
                        style.addProperty( "text-align", "left" );
                        break;
                    case RIGHT:
                        style.addProperty( "text-align", "right" );
                        break;

                    case CENTER:
                        style.addProperty( "text-align", "center" );
                        break;

                    case BOTH:
                        style.addProperty( "text-align", "justified" );
                        break;
                }
            }
            // Margin bottom/top
            Float spacingBefore = super.getSpacingBefore( pPr );
            if ( spacingBefore != null )
            {
                style.addProperty( "margin-top", getValue( spacingBefore ) );
            }
            else
            {
                // By default p element are no margin top/bottom;
                style.addProperty( "margin-top", "0" );
            }
            Float spacingAfter = super.getSpacingAfter( pPr );
            if ( spacingAfter != null )
            {
                style.addProperty( "margin-bottom", getValue( spacingAfter ) );
            }
            else
            {
                // By default p element are no margin top/bottom;
                style.addProperty( "margin-bottom", "1pt" );
            }
            // Background color
            Color backgroundColor = super.getBackgroundColor( pPr );
            if ( backgroundColor != null )
            {
                style.addProperty( "background-color", XWPFUtils.toHexString( backgroundColor ) );
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
            CSSStyle style = new CSSStyle( SPAN_ELEMENT, className );

            // Font family
            String fontFamily = super.getFontFamily( rPr );
            if ( StringUtils.isNotEmpty( fontFamily ) )
            {
                style.addProperty( "font-family", fontFamily );
            }
            // Font size
            Float fontSize = super.getFontSize( rPr );
            if ( fontSize != null )
            {
                style.addProperty( "font-size", getValue( fontSize ) );
            }
            // Font Bold
            Boolean bold = super.getFontStyleBold( rPr );
            if ( bold != null && bold )
            {
                style.addProperty( "font-weight", "bold" );
            }
            // Font Italic
            Boolean italic = super.getFontStyleItalic( rPr );
            if ( italic != null && italic )
            {
                style.addProperty( "font-style", "italic" );
            }
            // Font color
            Color fontColor = super.getFontColor( rPr );
            if ( fontColor != null )
            {
                style.addProperty( "color", XWPFUtils.toHexString( fontColor ) );
            }
            // TODO Underlines
            UnderlinePatterns underlinePatterns = null;// supergetUnderline();
            if ( underlinePatterns != null )
            {
                switch ( underlinePatterns )
                {
                    case SINGLE:
                        style.addProperty( "text-decoration", "underline" );
                        break;
                    default:
                        break;
                }
            }
            // Background color
            Color backgroundColor = super.getBackgroundColor( rPr );
            if ( backgroundColor != null )
            {
                style.addProperty( "background-color", XWPFUtils.toHexString( backgroundColor ) );
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
        CSSStyle style = new CSSStyle( XHTMLConstants.TABLE_ELEMENT, className );
        getCSSStyles().add( style );
    }

    // -------------------------------- Table row styles

    /**
     * @param className
     * @param trPr
     * @param defaultStyle
     */
    private void visitStyle( String className, CTTrPr trPr, boolean defaultStyle )
    {
        if ( trPr != null )
        {
            CSSStyle style = new CSSStyle( XHTMLConstants.TR_ELEMENT, className );
            getCSSStyles().add( style );
        }
    }

    // -------------------------------- Table cell styles

    /**
     * @param className
     * @param tcPr
     * @param defaultStyle
     */
    private void visitStyle( String className, CTTcPr tcPr, boolean defaultStyle )
    {
        if ( tcPr != null )
        {
            CSSStyle style = new CSSStyle( XHTMLConstants.TD_ELEMENT, className );
            getCSSStyles().add( style );
        }
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
        if ( cssStyles.size() < 1 )
        {

        }
        SAXHelper.startElement( contentHandler, XHTMLConstants.STYLE_ELEMENT, null );
        for ( CSSStyle style : cssStyles )
        {
            SAXHelper.characters( contentHandler, "\n" );
            style.save( contentHandler );
        }
        SAXHelper.endElement( contentHandler, XHTMLConstants.STYLE_ELEMENT );
    }

    private String getValue( float value )
    {
        return new StringBuilder( String.valueOf( value ) ).append( PT ).toString();
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
        if ( spaceAfter )
        {
            return new StringBuilder( styleId ).append( ' ' ).toString();
        }
        return styleId;
    }
}
