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
package fr.opensagres.odfdom.converter.internal.xhtml;

import java.util.HashMap;
import java.util.Map;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.OdfStyleBase;
import org.odftoolkit.odfdom.dom.element.office.OfficeAutomaticStylesElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeMasterStylesElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeStylesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleDefaultStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StyleFooterStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StyleGraphicPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleHeaderFooterPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleHeaderStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StylePageLayoutElement;
import org.odftoolkit.odfdom.dom.element.style.StylePageLayoutPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleParagraphPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableCellPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableColumnPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTablePropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableRowPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement;
import org.w3c.dom.Node;

import fr.opensagres.odfdom.converter.core.AbstractStyleEngine;
import fr.opensagres.odfdom.converter.core.IURIResolver;
import fr.opensagres.odfdom.converter.core.utils.StringUtils;
import fr.opensagres.xdocreport.xhtml.extension.CSSStylePropertyConstants;
import fr.opensagres.xdocreport.xhtml.extension.CSSStyleSheet;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLConstants;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLPageContentBuffer;

public class StyleEngineForXHTML
    extends AbstractStyleEngine
    implements XHTMLConstants, CSSStylePropertyConstants
{

    private static final String STYLE_NAME_HEADER = "Header";

    private static final String STYLE_NAME_FOOTER = "Footer";

    private CSSStyleSheet cssStyleSheet;

    private final boolean generateCSSComments;

    private final Map<String, String> classNamesMap = new HashMap<String, String>();

    private final IURIResolver resolver;

    public StyleEngineForXHTML( OdfDocument odfDocument, boolean generateCSSComments, int indent, IURIResolver resolver )
    {
        super( odfDocument );
        this.generateCSSComments = generateCSSComments;
        this.resolver = resolver;
        cssStyleSheet = new CSSStyleSheet( indent );
    }

    // -------------------------- Compute styles ---------------

    @Override
    public void visit( OfficeStylesElement ele )
    {
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "office:styles begin" );
        }
        super.visit( ele );
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "office:styles end" );
        }
    }

    @Override
    public void visit( OfficeAutomaticStylesElement ele )
    {
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "office:automatic-styles begin" );
        }
        super.visit( ele );
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "office:automatic-styles end" );
        }
    }

    @Override
    public void visit( OfficeMasterStylesElement ele )
    {
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "office:master-styles begin" );
        }
        super.visit( ele );
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "office:master-styles end" );
        }
    }

    @Override
    public void visit( StyleDefaultStyleElement ele )
    {
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "style:default-style @style:family=" + ele.getFamilyName() + " begin" );
        }
        cssStyleSheet.startCSSStyleDeclaration( computeCSSClassName( ele ) );
        super.visit( ele );
        cssStyleSheet.endCSSStyleDeclaration();
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "style:default-style @style:family=" + ele.getFamilyName() + " end" );
        }
    }

    @Override
    public void visit( StyleStyleElement ele )
    {
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "style:style @style:name= " + ele.getStyleNameAttribute() + ", @style:family="
                + ele.getFamilyName() + " begin" );
        }
        cssStyleSheet.startCSSStyleDeclaration( computeCSSClassName( ele ) );
        super.visit( ele );
        cssStyleSheet.endCSSStyleDeclaration();
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "style:style @style:name= " + ele.getStyleNameAttribute() + ", @style:family="
                + ele.getFamilyName() + " end" );
        }
    }

    // visit style:page-layout

    @Override
    public void visit( StylePageLayoutElement ele )
    {
        super.visit( ele );
    }

    public void visit( StylePageLayoutPropertiesElement ele )
    {
        String styleName = null;
        String styleFamilyName = null;
        OdfStyleBase styleBase = null;
        Node parentNode = ele.getParentNode();
        if ( parentNode instanceof StylePageLayoutElement )
        {
            styleName = ( (StylePageLayoutElement) parentNode ).getStyleNameAttribute();
            styleFamilyName = ( (StylePageLayoutElement) parentNode ).getFamilyName();
            styleBase = ( (StylePageLayoutElement) parentNode );
        }
        else
        {
            // TODO manage defaultstyle
            return;
        }
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "style:page-layout/style:page-layout-properties @style:page-layout-name= "
                + styleName + ", @style:family=" + styleFamilyName + " begin" );
        }
        cssStyleSheet.startCSSStyleDeclaration( computeCSSClassName( styleBase ) );

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            cssStyleSheet.setCSSProperty( BACKGROUND_COLOR, backgroundColor );
        }

        // border
        String border = ele.getFoBorderAttribute();
        if ( StringUtils.isNotEmpty( border ) )
        {
            cssStyleSheet.setCSSProperty( BORDER, border );
        }

        // border-bottom
        String borderBottom = ele.getFoBorderBottomAttribute();
        if ( StringUtils.isNotEmpty( borderBottom ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_BOTTOM, borderBottom );
        }

        // border-left
        String borderLeft = ele.getFoBorderLeftAttribute();
        if ( StringUtils.isNotEmpty( borderLeft ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_LEFT, borderLeft );
        }

        // border-bottom
        String borderRight = ele.getFoBorderRightAttribute();
        if ( StringUtils.isNotEmpty( borderRight ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_RIGHT, borderRight );
        }

        // border-top
        String borderTop = ele.getFoBorderTopAttribute();
        if ( StringUtils.isNotEmpty( borderTop ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_TOP, borderTop );
        }

        // margin
        String margin = ele.getFoMarginAttribute();
        if ( StringUtils.isNotEmpty( margin ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN, margin );
        }

        // margin-bottom
        String marginBottom = ele.getFoMarginBottomAttribute();
        if ( StringUtils.isNotEmpty( marginBottom ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_BOTTOM, marginBottom );
        }

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_LEFT, marginLeft );
        }

        // margin-bottom
        String marginRight = ele.getFoMarginRightAttribute();
        if ( StringUtils.isNotEmpty( marginRight ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_RIGHT, marginRight );
        }

        // margin-top
        String marginTop = ele.getFoMarginTopAttribute();
        if ( StringUtils.isNotEmpty( marginTop ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_TOP, marginTop );
        }

        // padding
        String padding = ele.getFoPaddingAttribute();
        if ( StringUtils.isNotEmpty( padding ) )
        {
            cssStyleSheet.setCSSProperty( PADDING, padding );
        }

        // padding-bottom
        String paddingBottom = ele.getFoPaddingBottomAttribute();
        if ( StringUtils.isNotEmpty( paddingBottom ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_BOTTOM, paddingBottom );
        }

        // padding-left
        String paddingLeft = ele.getFoPaddingLeftAttribute();
        if ( StringUtils.isNotEmpty( paddingLeft ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_LEFT, paddingLeft );
        }

        // padding-bottom
        String paddingRight = ele.getFoPaddingRightAttribute();
        if ( StringUtils.isNotEmpty( paddingRight ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_RIGHT, paddingRight );
        }

        // padding-top
        String paddingTop = ele.getFoPaddingTopAttribute();
        if ( StringUtils.isNotEmpty( paddingTop ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_TOP, paddingTop );
        }

        // height
        // Height page must be NOT done, because with XHTML there is no pages.
        // String height = ele.getFoPageHeightAttribute();
        // if (StringUtils.isNotEmpty(height)) {
        // cssStyleSheet.setCSSProperty("height", height);
        // }

        // width
        String width = ele.getFoPageWidthAttribute();
        if ( StringUtils.isNotEmpty( width ) )
        {
            cssStyleSheet.setCSSProperty( WIDTH, width );
        }
        super.visit( ele );

        cssStyleSheet.endCSSStyleDeclaration();
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "style:page-layout/style:page-layout-properties @style:page-layout-name="
                + styleName + ", @style:family=" + styleFamilyName + " end" );
        }
    }

    // visit style:header-style

    @Override
    public void visit( StyleHeaderStyleElement ele )
    {
        StylePageLayoutElement stylePageLayout = (StylePageLayoutElement) ele.getParentNode();
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "style:page-layout/style:header-style @style:page-layout-name="
                + stylePageLayout.getStyleNameAttribute() + ", @style:family=" + stylePageLayout.getFamilyName()
                + " begin" );
        }
        cssStyleSheet.startCSSStyleDeclaration( computeCSSClassName( stylePageLayout, STYLE_NAME_HEADER ) );

        super.visit( ele );

        cssStyleSheet.endCSSStyleDeclaration();
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "style:page-layout/style:header-style @style:page-layout-name"
                + stylePageLayout.getStyleNameAttribute() + ", @style:family=" + stylePageLayout.getFamilyName()
                + " end" );
        }

    }

    @Override
    public void visit( StyleFooterStyleElement ele )
    {
        StylePageLayoutElement stylePageLayout = (StylePageLayoutElement) ele.getParentNode();
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "style:page-layout/style:footer-style @style:page-layout-name="
                + stylePageLayout.getStyleNameAttribute() + ", @style:family=" + stylePageLayout.getFamilyName()
                + " begin" );
        }
        cssStyleSheet.startCSSStyleDeclaration( computeCSSClassName( stylePageLayout, STYLE_NAME_FOOTER ) );

        super.visit( ele );

        cssStyleSheet.endCSSStyleDeclaration();
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "style:page-layout/style:footer-style @style:page-layout-name"
                + stylePageLayout.getStyleNameAttribute() + ", @style:family=" + stylePageLayout.getFamilyName()
                + " end" );
        }

    }

    // visit style:header-footer-properties

    @Override
    public void visit( StyleHeaderFooterPropertiesElement ele )
    {

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            cssStyleSheet.setCSSProperty( BACKGROUND_COLOR, backgroundColor );
        }

        // border
        String border = ele.getFoBorderAttribute();
        if ( StringUtils.isNotEmpty( border ) )
        {
            cssStyleSheet.setCSSProperty( BORDER, border );
        }

        // border-bottom
        String borderBottom = ele.getFoBorderBottomAttribute();
        if ( StringUtils.isNotEmpty( borderBottom ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_BOTTOM, borderBottom );
        }

        // border-left
        String borderLeft = ele.getFoBorderLeftAttribute();
        if ( StringUtils.isNotEmpty( borderLeft ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_LEFT, borderLeft );
        }

        // border-bottom
        String borderRight = ele.getFoBorderRightAttribute();
        if ( StringUtils.isNotEmpty( borderRight ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_RIGHT, borderRight );
        }

        // border-top
        String borderTop = ele.getFoBorderTopAttribute();
        if ( StringUtils.isNotEmpty( borderTop ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_TOP, borderTop );
        }

        // margin
        String margin = ele.getFoMarginAttribute();
        if ( StringUtils.isNotEmpty( margin ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN, margin );
        }

        // margin-bottom
        String marginBottom = ele.getFoMarginBottomAttribute();
        if ( StringUtils.isNotEmpty( marginBottom ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_BOTTOM, marginBottom );
        }

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_LEFT, marginLeft );
        }

        // margin-bottom
        String marginRight = ele.getFoMarginRightAttribute();
        if ( StringUtils.isNotEmpty( marginRight ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_RIGHT, marginRight );
        }

        // margin-top
        String marginTop = ele.getFoMarginTopAttribute();
        if ( StringUtils.isNotEmpty( marginTop ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_TOP, marginTop );
        }

        // min-height
        String minHeight = ele.getFoMinHeightAttribute();
        if ( StringUtils.isNotEmpty( minHeight ) )
        {
            cssStyleSheet.setCSSProperty( MIN_HEIGHT, minHeight );
        }

        // padding
        String padding = ele.getFoPaddingAttribute();
        if ( StringUtils.isNotEmpty( padding ) )
        {
            cssStyleSheet.setCSSProperty( PADDING, padding );
        }

        // padding-bottom
        String paddingBottom = ele.getFoPaddingBottomAttribute();
        if ( StringUtils.isNotEmpty( paddingBottom ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_BOTTOM, paddingBottom );
        }

        // padding-left
        String paddingLeft = ele.getFoPaddingLeftAttribute();
        if ( StringUtils.isNotEmpty( paddingLeft ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_LEFT, paddingLeft );
        }

        // padding-bottom
        String paddingRight = ele.getFoPaddingRightAttribute();
        if ( StringUtils.isNotEmpty( paddingRight ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_RIGHT, paddingRight );
        }

        // padding-top
        String paddingTop = ele.getFoPaddingTopAttribute();
        if ( StringUtils.isNotEmpty( paddingTop ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_TOP, paddingTop );
        }
    }

    // visit //style:paragraph-properties

    @Override
    public void visit( StyleParagraphPropertiesElement ele )
    {

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            cssStyleSheet.setCSSProperty( BACKGROUND_COLOR, backgroundColor );
        }

        // border
        String border = ele.getFoBorderAttribute();
        if ( StringUtils.isNotEmpty( border ) )
        {
            cssStyleSheet.setCSSProperty( BORDER, border );
        }

        // border-bottom
        String borderBottom = ele.getFoBorderBottomAttribute();
        if ( StringUtils.isNotEmpty( borderBottom ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_BOTTOM, borderBottom );
        }

        // border-left
        String borderLeft = ele.getFoBorderLeftAttribute();
        if ( StringUtils.isNotEmpty( borderLeft ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_LEFT, borderLeft );
        }

        // border-bottom
        String borderRight = ele.getFoBorderRightAttribute();
        if ( StringUtils.isNotEmpty( borderRight ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_RIGHT, borderRight );
        }

        // border-top
        String borderTop = ele.getFoBorderTopAttribute();
        if ( StringUtils.isNotEmpty( borderTop ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_TOP, borderTop );
        }

        // line-height
        String lineHeight = ele.getFoLineHeightAttribute();
        if ( StringUtils.isNotEmpty( lineHeight ) )
        {
            cssStyleSheet.setCSSProperty( LINE_HEIGHT, lineHeight );
        }

        // margin
        String margin = ele.getFoMarginAttribute();
        if ( StringUtils.isNotEmpty( margin ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN, margin );
        }

        // margin-bottom
        String marginBottom = ele.getFoMarginBottomAttribute();
        if ( StringUtils.isNotEmpty( marginBottom ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_BOTTOM, marginBottom );
        }

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_LEFT, marginLeft );
        }

        // margin-bottom
        String marginRight = ele.getFoMarginRightAttribute();
        if ( StringUtils.isNotEmpty( marginRight ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_RIGHT, marginRight );
        }

        // margin-top
        String marginTop = ele.getFoMarginTopAttribute();
        if ( StringUtils.isNotEmpty( marginTop ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_TOP, marginTop );
        }

        // padding
        String padding = ele.getFoPaddingAttribute();
        if ( StringUtils.isNotEmpty( padding ) )
        {
            cssStyleSheet.setCSSProperty( PADDING, padding );
        }

        // padding-bottom
        String paddingBottom = ele.getFoPaddingBottomAttribute();
        if ( StringUtils.isNotEmpty( paddingBottom ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_BOTTOM, paddingBottom );
        }

        // padding-left
        String paddingLeft = ele.getFoPaddingLeftAttribute();
        if ( StringUtils.isNotEmpty( paddingLeft ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_LEFT, paddingLeft );
        }

        // padding-bottom
        String paddingRight = ele.getFoPaddingRightAttribute();
        if ( StringUtils.isNotEmpty( paddingRight ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_RIGHT, paddingRight );
        }

        // padding-top
        String paddingTop = ele.getFoPaddingTopAttribute();
        if ( StringUtils.isNotEmpty( paddingTop ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_TOP, paddingTop );
        }

        // text-align
        String textAlign = ele.getFoTextAlignAttribute();
        if ( StringUtils.isNotEmpty( textAlign ) )
        {
            if ( "start".equals( textAlign ) )
            {
                textAlign = TEXT_ALIGN_LEFT;
            }
            else if ( "end".equals( textAlign ) )
            {
                textAlign = TEXT_ALIGN_RIGHT;
            }
            cssStyleSheet.setCSSProperty( TEXT_ALIGN, textAlign );
        }

        // text-indent
        String textIndent = ele.getFoTextIndentAttribute();
        if ( StringUtils.isNotEmpty( textIndent ) )
        {
            cssStyleSheet.setCSSProperty( TEXT_INDENT, textIndent );
        }

        super.visit( ele );
    }

    @Override
    public void visit( StyleTextPropertiesElement ele )
    {

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            cssStyleSheet.setCSSProperty( BACKGROUND_COLOR, backgroundColor );
        }

        // color
        String color = ele.getFoColorAttribute();
        if ( StringUtils.isNotEmpty( color ) )
        {
            cssStyleSheet.setCSSProperty( COLOR, color );
        }

        // font-family
        String fontFamily = ele.getFoFontFamilyAttribute();
        if ( StringUtils.isNotEmpty( fontFamily ) )
        {
            cssStyleSheet.setCSSProperty( FONT_FAMILY, "\"" + fontFamily + "\"" );
        }
        String fontName = ele.getStyleFontNameAttribute();
        if ( StringUtils.isNotEmpty( fontName ) )
        {
            cssStyleSheet.setCSSProperty( FONT_FAMILY, "\"" + fontName + "\"" );
        }

        // font-size
        String fontSize = ele.getFoFontSizeAttribute();
        if ( StringUtils.isNotEmpty( fontSize ) )
        {
            cssStyleSheet.setCSSProperty( FONT_SIZE, fontSize );
        }

        // font-style
        String fontStyle = ele.getFoFontStyleAttribute();
        if ( StringUtils.isNotEmpty( fontStyle ) )
        {
            cssStyleSheet.setCSSProperty( FONT_STYLE, fontStyle );
        }

        // font-variant
        String fontVariant = ele.getFoFontVariantAttribute();
        if ( StringUtils.isNotEmpty( fontVariant ) )
        {
            cssStyleSheet.setCSSProperty( FONT_VARIANT, fontVariant );
        }

        // font-weight
        String fontWeight = ele.getFoFontWeightAttribute();
        if ( StringUtils.isNotEmpty( fontWeight ) )
        {
            cssStyleSheet.setCSSProperty( FONT_WEIGHT, fontWeight );
        }

        // text-underline-style
        String underlineStyle = ele.getStyleTextUnderlineStyleAttribute();
        if ( StringUtils.isNotEmpty( underlineStyle ) && !underlineStyle.equals( "none" ) )
        {
            cssStyleSheet.setCSSProperty( TEXT_DECORATION, TEXT_DECORATION_UNDERLINE );
        }

        // text-underline-type
        String underlineType = ele.getStyleTextUnderlineTypeAttribute();
        if ( StringUtils.isNotEmpty( underlineType ) && !underlineType.equals( "none" ) )
        {
            cssStyleSheet.setCSSProperty( TEXT_DECORATION, TEXT_DECORATION_UNDERLINE );
        }

        super.visit( ele );
    }

    // visit <style:style style:name="Tableau1"
    // style:family="table">/style:table-properties

    @Override
    public void visit( StyleTablePropertiesElement ele )
    {

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            cssStyleSheet.setCSSProperty( BACKGROUND_COLOR, backgroundColor );
        }

        // margin
        String margin = ele.getFoMarginAttribute();
        if ( StringUtils.isNotEmpty( margin ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN, margin );
        }

        // margin-bottom
        String marginBottom = ele.getFoMarginBottomAttribute();
        if ( StringUtils.isNotEmpty( marginBottom ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_BOTTOM, marginBottom );
        }

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_LEFT, marginLeft );
        }

        // margin-bottom
        String marginRight = ele.getFoMarginRightAttribute();
        if ( StringUtils.isNotEmpty( marginRight ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_RIGHT, marginRight );
        }

        // margin-top
        String marginTop = ele.getFoMarginTopAttribute();
        if ( StringUtils.isNotEmpty( marginTop ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_TOP, marginTop );
        }

        // border model (collapse...)
        String borderModel = ele.getTableBorderModelAttribute();
        if ( StringUtils.isNotEmpty( borderModel ) )
        {
            if ( "collapsing".equals( borderModel ) )
            {
                cssStyleSheet.setCSSProperty( BORDER_COLLAPSE, BORDER_COLLAPSE_COLLAPSE );
            }
            else
            {
                cssStyleSheet.setCSSProperty( BORDER_COLLAPSE, BORDER_COLLAPSE_SEPARATE );
            }
        }

        // width
        String width = ele.getStyleWidthAttribute();
        if ( StringUtils.isNotEmpty( width ) )
        {
            cssStyleSheet.setCSSProperty( WIDTH, width );
        }

        super.visit( ele );
    }

    // visit <style:style ...
    // style:family="table-column">/style:table-column-properties

    @Override
    public void visit( StyleTableColumnPropertiesElement ele )
    {

        // width
        String width = ele.getStyleColumnWidthAttribute();
        if ( StringUtils.isNotEmpty( width ) )
        {
            cssStyleSheet.setCSSProperty( WIDTH, width );
        }

        super.visit( ele );

    }

    // visit <style:style ...
    // style:family="table-cell">/style:table-row-properties

    @Override
    public void visit( StyleTableRowPropertiesElement ele )
    {

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            cssStyleSheet.setCSSProperty( BACKGROUND_COLOR, backgroundColor );
        }

        // min-height
        String minHeight = ele.getStyleMinRowHeightAttribute();
        if ( StringUtils.isNotEmpty( minHeight ) )
        {
            cssStyleSheet.setCSSProperty( MIN_HEIGHT, minHeight );
        }

        // height
        String height = ele.getStyleRowHeightAttribute();
        if ( StringUtils.isNotEmpty( height ) )
        {
            cssStyleSheet.setCSSProperty( HEIGHT, minHeight );
        }

        super.visit( ele );
    }

    // visit <style:style ...
    // style:family="table-cell">/style:table-cell-properties

    @Override
    public void visit( StyleTableCellPropertiesElement ele )
    {

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            cssStyleSheet.setCSSProperty( BACKGROUND_COLOR, backgroundColor );
        }

        // border
        String border = ele.getFoBorderAttribute();
        if ( StringUtils.isNotEmpty( border ) )
        {
            cssStyleSheet.setCSSProperty( BORDER, border );
        }

        // border-bottom
        String borderBottom = ele.getFoBorderBottomAttribute();
        if ( StringUtils.isNotEmpty( borderBottom ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_BOTTOM, borderBottom );
        }

        // border-left
        String borderLeft = ele.getFoBorderLeftAttribute();
        if ( StringUtils.isNotEmpty( borderLeft ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_LEFT, borderLeft );
        }

        // border-bottom
        String borderRight = ele.getFoBorderRightAttribute();
        if ( StringUtils.isNotEmpty( borderRight ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_RIGHT, borderRight );
        }

        // border-top
        String borderTop = ele.getFoBorderTopAttribute();
        if ( StringUtils.isNotEmpty( borderTop ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_TOP, borderTop );
        }

        // padding
        String padding = ele.getFoPaddingAttribute();
        if ( StringUtils.isNotEmpty( padding ) )
        {
            cssStyleSheet.setCSSProperty( PADDING, padding );
        }

        // padding-bottom
        String paddingBottom = ele.getFoPaddingBottomAttribute();
        if ( StringUtils.isNotEmpty( paddingBottom ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_BOTTOM, paddingBottom );
        }

        // padding-left
        String paddingLeft = ele.getFoPaddingLeftAttribute();
        if ( StringUtils.isNotEmpty( paddingLeft ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_LEFT, paddingLeft );
        }

        // padding-bottom
        String paddingRight = ele.getFoPaddingRightAttribute();
        if ( StringUtils.isNotEmpty( paddingRight ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_RIGHT, paddingRight );
        }

        // padding-top
        String paddingTop = ele.getFoPaddingTopAttribute();
        if ( StringUtils.isNotEmpty( paddingTop ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_TOP, paddingTop );
        }

        // vertical-align
        String verticalAlign = ele.getStyleVerticalAlignAttribute();
        if ( StringUtils.isNotEmpty( verticalAlign ) )
        {
            cssStyleSheet.setCSSProperty( VERTICAL_ALIGN, verticalAlign );
        }

        super.visit( ele );
    }

    // visit style:graphic-properties

    @Override
    public void visit( StyleGraphicPropertiesElement ele )
    {

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            cssStyleSheet.setCSSProperty( BACKGROUND_COLOR, backgroundColor );
        }

        // border
        String border = ele.getFoBorderAttribute();
        if ( StringUtils.isNotEmpty( border ) )
        {
            cssStyleSheet.setCSSProperty( BORDER, border );
        }

        // border-bottom
        String borderBottom = ele.getFoBorderBottomAttribute();
        if ( StringUtils.isNotEmpty( borderBottom ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_BOTTOM, borderBottom );
        }

        // border-left
        String borderLeft = ele.getFoBorderLeftAttribute();
        if ( StringUtils.isNotEmpty( borderLeft ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_LEFT, borderLeft );
        }

        // border-bottom
        String borderRight = ele.getFoBorderRightAttribute();
        if ( StringUtils.isNotEmpty( borderRight ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_RIGHT, borderRight );
        }

        // border-top
        String borderTop = ele.getFoBorderTopAttribute();
        if ( StringUtils.isNotEmpty( borderTop ) )
        {
            cssStyleSheet.setCSSProperty( BORDER_TOP, borderTop );
        }

        // margin
        String margin = ele.getFoMarginAttribute();
        if ( StringUtils.isNotEmpty( margin ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN, margin );
        }

        // margin-bottom
        String marginBottom = ele.getFoMarginBottomAttribute();
        if ( StringUtils.isNotEmpty( marginBottom ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_BOTTOM, marginBottom );
        }

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_LEFT, marginLeft );
        }

        // margin-bottom
        String marginRight = ele.getFoMarginRightAttribute();
        if ( StringUtils.isNotEmpty( marginRight ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_RIGHT, marginRight );
        }

        // margin-top
        String marginTop = ele.getFoMarginTopAttribute();
        if ( StringUtils.isNotEmpty( marginTop ) )
        {
            cssStyleSheet.setCSSProperty( MARGIN_TOP, marginTop );
        }

        // max-height
        String maxHeight = ele.getFoMaxHeightAttribute();
        if ( StringUtils.isNotEmpty( maxHeight ) )
        {
            cssStyleSheet.setCSSProperty( MAX_HEIGHT, maxHeight );
        }

        // max-width
        String maxWidth = ele.getFoMaxWidthAttribute();
        if ( StringUtils.isNotEmpty( maxWidth ) )
        {
            cssStyleSheet.setCSSProperty( MAX_WIDTH, maxWidth );
        }

        // min-height
        String minHeight = ele.getFoMinHeightAttribute();
        if ( StringUtils.isNotEmpty( minHeight ) )
        {
            cssStyleSheet.setCSSProperty( MIN_HEIGHT, minHeight );
        }

        // min-width
        String minWidth = ele.getFoMinWidthAttribute();
        if ( StringUtils.isNotEmpty( minWidth ) )
        {
            cssStyleSheet.setCSSProperty( MIN_WIDTH, minWidth );
        }

        // padding
        String padding = ele.getFoPaddingAttribute();
        if ( StringUtils.isNotEmpty( padding ) )
        {
            cssStyleSheet.setCSSProperty( PADDING, padding );
        }

        // padding-bottom
        String paddingBottom = ele.getFoPaddingBottomAttribute();
        if ( StringUtils.isNotEmpty( paddingBottom ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_BOTTOM, paddingBottom );
        }

        // padding-left
        String paddingLeft = ele.getFoPaddingLeftAttribute();
        if ( StringUtils.isNotEmpty( paddingLeft ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_LEFT, paddingLeft );
        }

        // padding-bottom
        String paddingRight = ele.getFoPaddingRightAttribute();
        if ( StringUtils.isNotEmpty( paddingRight ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_RIGHT, paddingRight );
        }

        // padding-top
        String paddingTop = ele.getFoPaddingTopAttribute();
        if ( StringUtils.isNotEmpty( paddingTop ) )
        {
            cssStyleSheet.setCSSProperty( PADDING_TOP, paddingTop );
        }

        super.visit( ele );
    }

    private String computeCSSClassName( OdfStyleBase style )
    {
        return computeCSSClassName( style, null );
    }

    private String computeCSSClassName( OdfStyleBase style, String prefix )
    {
        StringBuilder classNames = new StringBuilder();
        String className = compute( style, classNames, true, prefix );
        classNamesMap.put( className, classNames.toString() );
        return "." + className;
    }

    private String compute( OdfStyleBase style, StringBuilder classNames, boolean first, String prefix )
    {
        if ( style == null )
        {
            return null;
        }
        String familyName = style.getFamilyName();
        String styleName = null;
        if ( style instanceof StyleStyleElement )
        {
            styleName = ( (StyleStyleElement) style ).getStyleNameAttribute();
        }
        else if ( style instanceof StylePageLayoutElement )
        {
            styleName = ( (StylePageLayoutElement) style ).getStyleNameAttribute();
        }

        String className = getClassName( familyName, styleName );
        if ( prefix != null )
        {
            className = className + prefix;
        }
        if ( first )
        {
            classNames.append( className );
        }
        else
        {
            classNames.insert( 0, ' ' );
            classNames.insert( 0, className );
        }
        style = style.getParentStyle();
        if ( style != null )
        {
            compute( style, classNames, false, prefix );
        }
        return className;
    }

    public String getClassName( String familyName, String styleName )
    {
        StringBuilder className = new StringBuilder();
        // style:family
        className.append( familyName );
        // style:name
        if ( styleName != null )
        {
            className.append( '_' );
            className.append( StringUtils.replaceAll( styleName, ".", "_" ) );
        }
        return className.toString();

    }

    // -------------------------- Apply styles ---------------

    public CSSStyleSheet getCSSStyleSheet()
    {
        return cssStyleSheet;
    }

    public String getMasterPageHeaderStyleName( String masterPageLayoutName )
    {
        return masterPageLayoutName + STYLE_NAME_HEADER;
    }

    public String getMasterPageFooterStyleName( String masterPageLayoutName )
    {
        return masterPageLayoutName + STYLE_NAME_FOOTER;
    }

    public void applyStyles( String familyName, String styleName, XHTMLPageContentBuffer xhtml )
    {
        String className = classNamesMap.get( getClassName( familyName, styleName ) );
        if ( StringUtils.isNotEmpty( className ) )
        {
            xhtml.setAttribute( CLASS_ATTR, className );
        }
    }

    public IURIResolver getURIResolver()
    {
        return resolver;
    }
}
