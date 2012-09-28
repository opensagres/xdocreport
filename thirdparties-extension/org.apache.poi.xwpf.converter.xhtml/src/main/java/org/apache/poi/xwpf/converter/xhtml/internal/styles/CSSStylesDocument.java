package org.apache.poi.xwpf.converter.xhtml.internal.styles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConstants;
import org.apache.poi.xwpf.converter.xhtml.internal.EmptyAttributes;
import org.apache.poi.xwpf.converter.xhtml.internal.SAXHelper;
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

    private void visitStyle( String className, CTPPr pPr, boolean defaultStyle )
    {
        if ( pPr != null )
        {
            CSSStyle style = new CSSStyle( "p", className );
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
            getCSSStyles().add( style );
        }
    }

    private void visitStyle( String className, CTRPr rPr, boolean defaultStyle )
    {
        if ( rPr != null )
        {
            CSSStyle style = new CSSStyle( XHTMLConstants.SPAN_ELEMENT, className );
            getCSSStyles().add( style );
        }
    }

    private void visitStyle( String className, CTTblPrBase tblPr, boolean defaultStyle )
    {
        CSSStyle style = new CSSStyle( XHTMLConstants.TABLE_ELEMENT, className );
        getCSSStyles().add( style );
    }

    private void visitStyle( String className, CTTrPr trPr, boolean defaultStyle )
    {
        if ( trPr != null )
        {
            CSSStyle style = new CSSStyle( XHTMLConstants.TR_ELEMENT, className );
            getCSSStyles().add( style );
        }
    }

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
            style.save( contentHandler );
        }
        SAXHelper.endElement( contentHandler, XHTMLConstants.STYLE_ELEMENT );
    }

    private String getValue( float value )
    {
        return new StringBuilder( String.valueOf( value ) ).append( "pt" ).toString();
    }

    public String getClassNames( String styleID )
    {
        if ( StringUtils.isEmpty( styleID ) )
        {
            return null;
        }
        StringBuilder classNames = new StringBuilder( getClassName(styleID) );
        CTStyle style = super.getStyle( styleID  );
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
}
