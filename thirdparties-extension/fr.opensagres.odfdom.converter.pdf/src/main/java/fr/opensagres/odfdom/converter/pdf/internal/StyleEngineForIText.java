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
package fr.opensagres.odfdom.converter.pdf.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.attribute.fo.FoBreakBeforeAttribute;
import org.odftoolkit.odfdom.dom.attribute.fo.FoFontStyleAttribute;
import org.odftoolkit.odfdom.dom.attribute.fo.FoFontWeightAttribute;
import org.odftoolkit.odfdom.dom.attribute.fo.FoKeepTogetherAttribute;
import org.odftoolkit.odfdom.dom.attribute.fo.FoTextAlignAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleFontStyleAsianAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleFontStyleComplexAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleFontWeightAsianAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleFontWeightComplexAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleNumFormatAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StylePrintOrientationAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleTextLineThroughStyleAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleTextUnderlineStyleAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleTypeAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleVerticalAlignAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleWrapAttribute;
import org.odftoolkit.odfdom.dom.attribute.table.TableAlignAttribute;
import org.odftoolkit.odfdom.dom.element.OdfStyleBase;
import org.odftoolkit.odfdom.dom.element.office.OfficeAutomaticStylesElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeMasterStylesElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeStylesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleBackgroundImageElement;
import org.odftoolkit.odfdom.dom.element.style.StyleColumnElement;
import org.odftoolkit.odfdom.dom.element.style.StyleColumnsElement;
import org.odftoolkit.odfdom.dom.element.style.StyleDefaultStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StyleFooterStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StyleGraphicPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleHeaderFooterPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleListLevelLabelAlignmentElement;
import org.odftoolkit.odfdom.dom.element.style.StyleListLevelPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StylePageLayoutElement;
import org.odftoolkit.odfdom.dom.element.style.StylePageLayoutPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleParagraphPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleSectionPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTabStopElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTabStopsElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableCellPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTablePropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableRowPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement;
import org.odftoolkit.odfdom.dom.element.text.TextListLevelStyleBulletElement;
import org.odftoolkit.odfdom.dom.element.text.TextListLevelStyleImageElement;
import org.odftoolkit.odfdom.dom.element.text.TextListLevelStyleNumberElement;
import org.odftoolkit.odfdom.dom.element.text.TextListStyleElement;
import org.odftoolkit.odfdom.dom.element.text.TextOutlineLevelStyleElement;
import org.odftoolkit.odfdom.dom.element.text.TextOutlineStyleElement;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.w3c.dom.Node;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;

import fr.opensagres.odfdom.converter.core.AbstractStyleEngine;
import fr.opensagres.odfdom.converter.core.utils.ODFUtils;
import fr.opensagres.odfdom.converter.pdf.PdfOptions;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableImage;
import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleBorder;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleBreak;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleColumnProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleGraphicProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleHeaderFooterProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleLineHeight;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleListProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleNumFormat;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StylePageLayoutProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleParagraphProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleSectionProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTabStopProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTableCellProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTableProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTableRowProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTextProperties;
import fr.opensagres.xdocreport.itext.extension.PageOrientation;
import fr.opensagres.xdocreport.utils.BorderType;
import fr.opensagres.xdocreport.utils.StringUtils;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StyleEngineForIText
    extends AbstractStyleEngine
{
    private static final String OUTLINE = "outline";

    private Style currentStyle = null;

    private Map<Integer, StyleListProperties> currentListPropertiesMap;

    private StyleListProperties currentListProperties;

    private List<StyleTabStopProperties> currentTabStopPropertiesList;

    private List<StyleColumnProperties> currentColumnPropertiesList;

    private BackgroundImage backgroundImage = null;

    private final PdfOptions options;

    private final Map<String, Style> stylesMap = new HashMap<String, Style>();

    public StyleEngineForIText( OdfDocument odfDocument, PdfOptions options )
    {
        super(odfDocument);
        this.options = options != null ? options : PdfOptions.getDefault();
    }

    public BackgroundImage getBackgroundImage() {
    	return backgroundImage;
    }

    public void visit(StyleBackgroundImageElement ele) {

    	String href = ele.getXlinkHrefAttribute();
        if (StringUtils.isNotEmpty(href)) {
            byte[] imageStream = odfDocument.getPackage().getBytes(href);
	         try {
		        if (imageStream != null) {
		        	BackgroundImage.Builder builder = new BackgroundImage.Builder(imageStream);
		            Node parentNode = ele.getParentNode();
		            builder.setRepeat(BackgroundImage.Repeat.fromODT(ele.getStyleRepeatAttribute()));
		            builder.setPosition(BackgroundImage.Position.fromODT(ele.getStylePositionAttribute()));
		            if (parentNode instanceof StylePageLayoutPropertiesElement) {
		            	StylePageLayoutPropertiesElement layout = (StylePageLayoutPropertiesElement) parentNode;
		                String svgWidth = layout.getFoPageWidthAttribute();
		                if (StringUtils.isNotEmpty(svgWidth)) builder.setPageWidth(ODFUtils.getDimensionAsPoint(svgWidth));
		                String svgHeight = layout.getFoPageHeightAttribute();
		                if (StringUtils.isNotEmpty(svgHeight)) builder.setPageHeight(ODFUtils.getDimensionAsPoint(svgHeight));
		                String leftMargin = layout.getFoMarginLeftAttribute();
	                	if (StringUtils.isNotEmpty(leftMargin)) builder.setLeftMargin(ODFUtils.getDimensionAsPoint(leftMargin));
	                	String rightMargin = layout.getFoMarginRightAttribute();
	                	if (StringUtils.isNotEmpty(rightMargin)) builder.setRightMargin(ODFUtils.getDimensionAsPoint(rightMargin));
	                	String topMargin = layout.getFoMarginTopAttribute();
	                	if (StringUtils.isNotEmpty(topMargin)) builder.setTopMargin(ODFUtils.getDimensionAsPoint(topMargin));
	                	String bottomMargin = layout.getFoMarginBottomAttribute();
	                	if (StringUtils.isNotEmpty(bottomMargin)) builder.setBottomMargin(ODFUtils.getDimensionAsPoint(bottomMargin));
		            }
		            backgroundImage = builder.build();
		        }
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
        }
    }

    public void visit( OfficeStylesElement ele )
    {
        super.visit( ele );
    }

    public void visit( OfficeAutomaticStylesElement ele )
    {
        super.visit( ele );
    }

    public void visit( OfficeMasterStylesElement ele )
    {
        super.visit( ele );
    }

    public void visit( StyleDefaultStyleElement ele )
    {
        computeStyle( ele, true );
    }

    public void visit( StyleStyleElement ele )
    {
        computeStyle( ele, false );
    }

    public void visit( StylePageLayoutElement ele )
    {
        computeStyle( ele, false );
    }

    private Style computeStyle( OdfElement odfElement, boolean isDefaultStyle )
    {
        if ( odfElement == null )
        {
            return null;
        }
        OdfStyleBase styleBase = odfElement instanceof OdfStyleBase ? (OdfStyleBase) odfElement : null;

        // 1) Check if style is in the cache
        String familyName = styleBase != null ? styleBase.getFamilyName() : OUTLINE;
        String styleName = null;
        String masterPageName = null;
        if ( !isDefaultStyle )
        {
            if ( styleBase instanceof StyleStyleElement )
            {
                StyleStyleElement styleElement = (StyleStyleElement) styleBase;
                styleName = styleElement.getStyleNameAttribute();
                masterPageName = styleElement.getStyleMasterPageNameAttribute();
            }
            else if ( styleBase instanceof StylePageLayoutElement )
            {
                styleName = ( (StylePageLayoutElement) styleBase ).getStyleNameAttribute();
            }
            else if ( styleBase instanceof TextListStyleElement )
            {
                styleName = ( (TextListStyleElement) styleBase ).getStyleNameAttribute();
            }
        }

        String styleId = getStyleId( familyName, styleName );
        Style style = stylesMap.get( styleId );
        if ( style != null )
        {
            // style already computed
            return style;
        }

        // 3) Create style
        style = new Style( options.getFontProvider(), styleName, familyName, masterPageName );

        // 4) Apply default style if needed
        // Style defaultStyle = null;
        // if (!isDefaultStyle) {
        // OdfDefaultStyle odfDefaultStyle = odfDocument.getDocumentStyles()
        // .getDefaultStyle(styleBase.getFamily());
        // if (odfDefaultStyle != null) {
        // defaultStyle = computeStyle(odfDefaultStyle, true);
        // }
        // if (defaultStyle != null) {
        // style.merge(defaultStyle);
        // }
        // }

        // 5) Apply Parent style
        List<Style> parentsStyles = null;
        OdfStyleBase parentStyleBase = styleBase != null ? styleBase.getParentStyle() : null;
        while ( parentStyleBase != null )
        {
            Style parentStyle = computeStyle( parentStyleBase, isDefaultStyle );
            if ( parentStyle != null )
            {
                if ( parentsStyles == null )
                {
                    parentsStyles = new ArrayList<Style>();
                }
                parentsStyles.add( 0, parentStyle );
            }
            parentStyleBase = parentStyleBase.getParentStyle();
        }
        if ( parentsStyles != null )
        {
            for ( Style parentsStyle : parentsStyles )
            {
                style.merge( parentsStyle, false );
            }
        }

        // 6) Apply current style
        currentStyle = style;
        super.visit( odfElement );

        // 7) register style in the cache
        stylesMap.put( styleId, style );
        return style;
    }

    public void visit( StylePageLayoutPropertiesElement ele )
    {
        StylePageLayoutProperties pageLayoutProperties = currentStyle.getPageLayoutProperties();
        if ( pageLayoutProperties == null )
        {
            pageLayoutProperties = new StylePageLayoutProperties();
            currentStyle.setPageLayoutProperties( pageLayoutProperties );
        }

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            // cssStyleSheet.setCSSProperty("background-color",
            // backgroundColor);
        }

        // border
        String border = ele.getFoBorderAttribute();
        if ( StringUtils.isNotEmpty( border ) )
        {
            // cssStyleSheet.setCSSProperty("border", border);
        }

        // border-bottom
        String borderBottom = ele.getFoBorderBottomAttribute();
        if ( StringUtils.isNotEmpty( borderBottom ) )
        {
            // cssStyleSheet.setCSSProperty("border-bottom", borderBottom);
        }

        // border-left
        String borderLeft = ele.getFoBorderLeftAttribute();
        if ( StringUtils.isNotEmpty( borderLeft ) )
        {
            // cssStyleSheet.setCSSProperty("border-left", borderLeft);
        }

        // border-bottom
        String borderRight = ele.getFoBorderRightAttribute();
        if ( StringUtils.isNotEmpty( borderRight ) )
        {
            // cssStyleSheet.setCSSProperty("border-right", borderRight);
        }

        // border-top
        String borderTop = ele.getFoBorderTopAttribute();
        if ( StringUtils.isNotEmpty( borderTop ) )
        {
            // cssStyleSheet.setCSSProperty("border-top", borderTop);
        }

        // margin
        String margin = ele.getFoMarginAttribute();
        if ( StringUtils.isNotEmpty( margin ) )
        {
            pageLayoutProperties.setMargin( ODFUtils.getDimensionAsPoint( margin ) );
        }

        // margin-bottom
        String marginBottom = ele.getFoMarginBottomAttribute();
        if ( StringUtils.isNotEmpty( marginBottom ) )
        {
            pageLayoutProperties.setMarginBottom( ODFUtils.getDimensionAsPoint( marginBottom ) );
        }

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            pageLayoutProperties.setMarginLeft( ODFUtils.getDimensionAsPoint( marginLeft ) );
        }

        // margin-right
        String marginRight = ele.getFoMarginRightAttribute();
        if ( StringUtils.isNotEmpty( marginRight ) )
        {
            pageLayoutProperties.setMarginRight( ODFUtils.getDimensionAsPoint( marginRight ) );
        }

        // margin-top
        String marginTop = ele.getFoMarginTopAttribute();
        if ( StringUtils.isNotEmpty( marginTop ) )
        {
            pageLayoutProperties.setMarginTop( ODFUtils.getDimensionAsPoint( marginTop ) );
        }

        // padding
        String padding = ele.getFoPaddingAttribute();
        if ( StringUtils.isNotEmpty( padding ) )
        {
            // cssStyleSheet.setCSSProperty("padding", padding);
        }

        // padding-bottom
        String paddingBottom = ele.getFoPaddingBottomAttribute();
        if ( StringUtils.isNotEmpty( paddingBottom ) )
        {
            // cssStyleSheet.setCSSProperty("padding-bottom", paddingBottom);
        }

        // padding-left
        String paddingLeft = ele.getFoPaddingLeftAttribute();
        if ( StringUtils.isNotEmpty( paddingLeft ) )
        {
            // cssStyleSheet.setCSSProperty("padding-left", paddingLeft);
        }

        // padding-bottom
        String paddingRight = ele.getFoPaddingRightAttribute();
        if ( StringUtils.isNotEmpty( paddingRight ) )
        {
            // cssStyleSheet.setCSSProperty("padding-right", paddingRight);
        }

        // padding-top
        String paddingTop = ele.getFoPaddingTopAttribute();
        if ( StringUtils.isNotEmpty( paddingTop ) )
        {
            // cssStyleSheet.setCSSProperty("padding-top", paddingTop);
        }

        // height
        String height = ele.getFoPageHeightAttribute();
        if ( StringUtils.isNotEmpty( height ) )
        {
            pageLayoutProperties.setHeight( ODFUtils.getDimensionAsPoint( height ) );
        }

        // width
        String width = ele.getFoPageWidthAttribute();
        if ( StringUtils.isNotEmpty( width ) )
        {
            pageLayoutProperties.setWidth( ODFUtils.getDimensionAsPoint( width ) );
        }

        // orientation
        String orientation = ele.getStylePrintOrientationAttribute();
        if ( StringUtils.isNotEmpty( orientation ) )
        {
            if ( StylePrintOrientationAttribute.Value.LANDSCAPE.toString().equals( orientation ) )
            {
                pageLayoutProperties.setOrientation( PageOrientation.Landscape );
            }
            else if ( StylePrintOrientationAttribute.Value.PORTRAIT.toString().equals( orientation ) )
            {
                pageLayoutProperties.setOrientation( PageOrientation.Portrait );
            }

        }
        super.visit( ele );

    }

    public void visit( StyleHeaderFooterPropertiesElement ele )
    {
        Node parentNode = ele.getParentNode();
        boolean footer = StyleFooterStyleElement.ELEMENT_NAME.getLocalName().equals( parentNode.getLocalName() );

        StyleHeaderFooterProperties headerFooterProperties = null;
        if ( !footer )
        {
            headerFooterProperties = currentStyle.getHeaderProperties();
            if ( headerFooterProperties == null )
            {
                headerFooterProperties = new StyleHeaderFooterProperties();
                currentStyle.setHeaderProperties( headerFooterProperties );
            }
        }
        else
        {
            headerFooterProperties = currentStyle.getFooterProperties();
            if ( headerFooterProperties == null )
            {
                headerFooterProperties = new StyleHeaderFooterProperties();
                currentStyle.setFooterProperties( headerFooterProperties );
            }
        }

        // min-height
        String minHeight = ele.getFoMinHeightAttribute();
        if ( StringUtils.isNotEmpty( minHeight ) )
        {
            headerFooterProperties.setMinHeight( ODFUtils.getDimensionAsPoint( minHeight ) );
        }

        // margin
        String margin = ele.getFoMarginAttribute();
        if ( StringUtils.isNotEmpty( margin ) )
        {
            headerFooterProperties.setMargin( ODFUtils.getDimensionAsPoint( margin ) );
        }

        // margin-bottom
        String marginBottom = ele.getFoMarginBottomAttribute();
        if ( StringUtils.isNotEmpty( marginBottom ) )
        {
            headerFooterProperties.setMarginBottom( ODFUtils.getDimensionAsPoint( marginBottom ) );
        }

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            headerFooterProperties.setMarginLeft( ODFUtils.getDimensionAsPoint( marginLeft ) );
        }

        // margin-right
        String marginRight = ele.getFoMarginRightAttribute();
        if ( StringUtils.isNotEmpty( marginRight ) )
        {
            headerFooterProperties.setMarginRight( ODFUtils.getDimensionAsPoint( marginRight ) );
        }
        // margin-top
        String marginTop = ele.getFoMarginTopAttribute();
        if ( StringUtils.isNotEmpty( marginTop ) )
        {
            headerFooterProperties.setMarginTop( ODFUtils.getDimensionAsPoint( marginTop ) );
        }
        // height
        String height = ele.getSvgHeightAttribute();
        if ( StringUtils.isNotEmpty( height ) )
        {
        	headerFooterProperties.setHeight(ODFUtils.getDimensionAsPoint(height));
        }
    }

    // visit //style:paragraph-properties
    @Override
    public void visit( StyleParagraphPropertiesElement ele )
    {

        StyleParagraphProperties paragraphProperties = currentStyle.getParagraphProperties();
        if ( paragraphProperties == null )
        {
            paragraphProperties = new StyleParagraphProperties();
            currentStyle.setParagraphProperties( paragraphProperties );
        }

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            paragraphProperties.setBackgroundColor( ColorRegistry.getInstance().getColor( backgroundColor ) );
        }

        // border
        String border = ele.getFoBorderAttribute();
        if ( StringUtils.isNotEmpty( border ) )
        {
            paragraphProperties.setBorder( new StyleBorder( border, BorderType.ALL ) );
        }
        // border-bottom
        String borderBottom = ele.getFoBorderBottomAttribute();
        if ( StringUtils.isNotEmpty( borderBottom ) )
        {
            paragraphProperties.setBorderBottom( new StyleBorder( borderBottom, BorderType.BOTTOM ) );
        }

        // border-left
        String borderLeft = ele.getFoBorderLeftAttribute();
        if ( StringUtils.isNotEmpty( borderLeft ) )
        {
            paragraphProperties.setBorderLeft( new StyleBorder( borderLeft, BorderType.LEFT ) );
        }

        // border-bottom
        String borderRight = ele.getFoBorderRightAttribute();
        if ( StringUtils.isNotEmpty( borderRight ) )
        {
            paragraphProperties.setBorderRight( new StyleBorder( borderRight, BorderType.RIGHT ) );
        }

        // border-top
        String borderTop = ele.getFoBorderTopAttribute();
        if ( StringUtils.isNotEmpty( borderTop ) )
        {
            paragraphProperties.setBorderTop( new StyleBorder( borderTop, BorderType.TOP ) );
        }

        // join-border
        Boolean joinBorder = ele.getStyleJoinBorderAttribute();
        if ( joinBorder != null )
        {
            paragraphProperties.setJoinBorder( joinBorder );
        }

        // line-height
        String lineHeight = ele.getFoLineHeightAttribute();
        if ( StringUtils.isNotEmpty( lineHeight ) )
        {
            paragraphProperties.setLineHeight( new StyleLineHeight( ODFUtils.getDimensionAsPoint( lineHeight ),
                                                                    ODFUtils.hasPercentUnit( lineHeight ) ) );
        }

        // margin
        String margin = ele.getFoMarginAttribute();
        if ( StringUtils.isNotEmpty( margin ) )
        {
            paragraphProperties.setMargin( ODFUtils.getDimensionAsPoint( margin ) );
        }

        // margin-bottom
        String marginBottom = ele.getFoMarginBottomAttribute();
        if ( StringUtils.isNotEmpty( marginBottom ) )
        {
            paragraphProperties.setMarginBottom( ODFUtils.getDimensionAsPoint( marginBottom ) );
        }

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            paragraphProperties.setMarginLeft( ODFUtils.getDimensionAsPoint( marginLeft ) );
        }

        // margin-right
        String marginRight = ele.getFoMarginRightAttribute();
        if ( StringUtils.isNotEmpty( marginRight ) )
        {
            paragraphProperties.setMarginRight( ODFUtils.getDimensionAsPoint( marginRight ) );
        }

        // margin-top
        String marginTop = ele.getFoMarginTopAttribute();
        if ( StringUtils.isNotEmpty( marginTop ) )
        {
            paragraphProperties.setMarginTop( ODFUtils.getDimensionAsPoint( marginTop ) );
        }

        // padding
        String padding = ele.getFoPaddingAttribute();
        if ( StringUtils.isNotEmpty( padding ) )
        {
            paragraphProperties.setPadding( ODFUtils.getDimensionAsPoint( padding ) );
        }

        // padding-bottom
        String paddingBottom = ele.getFoPaddingBottomAttribute();
        if ( StringUtils.isNotEmpty( paddingBottom ) )
        {
            paragraphProperties.setPaddingBottom( ODFUtils.getDimensionAsPoint( paddingBottom ) );
        }

        // padding-left
        String paddingLeft = ele.getFoPaddingLeftAttribute();
        if ( StringUtils.isNotEmpty( paddingLeft ) )
        {
            paragraphProperties.setPaddingLeft( ODFUtils.getDimensionAsPoint( paddingLeft ) );
        }

        // padding-right
        String paddingRight = ele.getFoPaddingRightAttribute();
        if ( StringUtils.isNotEmpty( paddingRight ) )
        {
            paragraphProperties.setPaddingRight( ODFUtils.getDimensionAsPoint( paddingRight ) );
        }

        // padding-top
        String paddingTop = ele.getFoPaddingTopAttribute();
        if ( StringUtils.isNotEmpty( paddingTop ) )
        {
            paragraphProperties.setPaddingTop( ODFUtils.getDimensionAsPoint( paddingTop ) );
        }

        // text-align
        String textAlign = ele.getFoTextAlignAttribute();
        if ( StringUtils.isNotEmpty( textAlign ) )
        {
            int alignment = Element.ALIGN_UNDEFINED;
            if ( FoTextAlignAttribute.Value.START.toString().equals( textAlign ) )
            {
                alignment = Element.ALIGN_LEFT;
            }
            else if ( FoTextAlignAttribute.Value.END.toString().equals( textAlign ) )
            {
                alignment = Element.ALIGN_RIGHT;
            }
            else if ( FoTextAlignAttribute.Value.LEFT.toString().equals( textAlign ) )
            {
                alignment = Element.ALIGN_LEFT;
            }
            else if ( FoTextAlignAttribute.Value.RIGHT.toString().equals( textAlign ) )
            {
                alignment = Element.ALIGN_RIGHT;
            }
            else if ( FoTextAlignAttribute.Value.CENTER.toString().equals( textAlign ) )
            {
                alignment = Element.ALIGN_CENTER;
            }
            else if ( FoTextAlignAttribute.Value.JUSTIFY.toString().equals( textAlign ) )
            {
                alignment = Element.ALIGN_JUSTIFIED;
            }
            paragraphProperties.setAlignment( alignment );
        }

        // auto-text-indent
        Boolean autoTextIndent = ele.getStyleAutoTextIndentAttribute();
        if ( autoTextIndent != null )
        {
            paragraphProperties.setAutoTextIndent( autoTextIndent );
        }

        // text-indent
        String textIndent = ele.getFoTextIndentAttribute();
        if ( StringUtils.isNotEmpty( textIndent ) )
        {
            paragraphProperties.setTextIndent( ODFUtils.getDimensionAsPoint( textIndent ) );
        }

        // keep-together
        String keepTogether = ele.getFoKeepTogetherAttribute();
        if ( StringUtils.isNotEmpty( keepTogether ) )
        {
            if ( FoKeepTogetherAttribute.Value.ALWAYS.toString().equals( keepTogether ) )
            {
                paragraphProperties.setKeepTogether( Boolean.TRUE );
            }
            else
            {
                paragraphProperties.setKeepTogether( Boolean.FALSE );
            }
        }

        // fo:break-before
        String breakBefore = ele.getFoBreakBeforeAttribute();
        if ( StringUtils.isNotEmpty( breakBefore ) )
        {
            if ( FoBreakBeforeAttribute.Value.PAGE.toString().equals( breakBefore ) )
            {
                paragraphProperties.setBreakBefore( StyleBreak.createWithPageBreak() );
            }
            else if ( FoBreakBeforeAttribute.Value.COLUMN.toString().equals( breakBefore ) )
            {
                paragraphProperties.setBreakBefore( StyleBreak.createWithColumnBreak() );
            }
            else
            {
                paragraphProperties.setBreakBefore( StyleBreak.createWithNoBreak() );
            }
        }

        super.visit( ele );
    }

    @Override
    public void visit( StyleTabStopsElement ele )
    {
        currentTabStopPropertiesList = new ArrayList<StyleTabStopProperties>();
        super.visit( ele );
        currentStyle.setTabStopPropertiesList( currentTabStopPropertiesList );
        currentTabStopPropertiesList = null;
    }

    @Override
    public void visit( StyleTabStopElement ele )
    {
        if ( currentTabStopPropertiesList == null )
        {
            return;
        }
        StyleTabStopProperties tabStopProperties = new StyleTabStopProperties();
        currentTabStopPropertiesList.add( tabStopProperties );

        // leader-text
        String leaderText = ele.getStyleLeaderTextAttribute();
        if ( StringUtils.isNotEmpty( leaderText ) )
        {
            tabStopProperties.setLeaderText( leaderText );
        }

        // position
        String position = ele.getStylePositionAttribute();
        if ( StringUtils.isNotEmpty( position ) )
        {
            tabStopProperties.setPosition( ODFUtils.getDimensionAsPoint( position ) );
        }

        // type
        String type = ele.getStyleTypeAttribute();
        if ( StringUtils.isNotEmpty( type ) )
        {
            if ( StyleTypeAttribute.Value.LEFT.toString().equals( type ) )
            {
                tabStopProperties.setType( Element.ALIGN_LEFT );
            }
            else if ( StyleTypeAttribute.Value.RIGHT.toString().equals( type ) )
            {
                tabStopProperties.setType( Element.ALIGN_RIGHT );
            }
            else
            {
                tabStopProperties.setType( Element.ALIGN_CENTER );
            }
        }

        super.visit( ele );
    }

    @Override
    public void visit( StyleTextPropertiesElement ele )
    {
        StyleTextProperties textProperties = currentStyle.getTextProperties();
        if ( textProperties == null )
        {
            textProperties = new StyleTextProperties( options.getFontProvider() );
            currentStyle.setTextProperties( textProperties );
        }

        // set font encoding from options
        textProperties.setFontEncoding( options.getFontEncoding() );

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            textProperties.setBackgroundColor( ColorRegistry.getInstance().getColor( backgroundColor ) );
        }

        // color
        String color = ele.getFoColorAttribute();
        if ( StringUtils.isNotEmpty( color ) )
        {
            textProperties.setFontColor( ColorRegistry.getInstance().getColor( color ) );
        }

        // font-family
        String fontFamily = ele.getFoFontFamilyAttribute();
        if ( StringUtils.isNotEmpty( fontFamily ) )
        {
            textProperties.setFontName( ODFUtils.stripTrailingDigits( fontFamily ) );
        }
        String fontFamilyAsian = ele.getStyleFontFamilyAsianAttribute();
        if ( StringUtils.isNotEmpty( fontFamilyAsian ) )
        {
            textProperties.setFontNameAsian( ODFUtils.stripTrailingDigits( fontFamilyAsian ) );
        }
        String fontFamilyComplex = ele.getStyleFontFamilyComplexAttribute();
        if ( StringUtils.isNotEmpty( fontFamilyComplex ) )
        {
            textProperties.setFontNameComplex( ODFUtils.stripTrailingDigits( fontFamilyComplex ) );
        }

        // font-name
        String fontName = ele.getStyleFontNameAttribute();
        if ( StringUtils.isNotEmpty( fontName ) )
        {
            textProperties.setFontName( ODFUtils.stripTrailingDigits( fontName ) );
        }
        String fontNameAsian = ele.getStyleFontNameAsianAttribute();
        if ( StringUtils.isNotEmpty( fontNameAsian ) )
        {
            textProperties.setFontNameAsian( ODFUtils.stripTrailingDigits( fontNameAsian ) );
        }
        String fontNameComplex = ele.getStyleFontNameComplexAttribute();
        if ( StringUtils.isNotEmpty( fontNameComplex ) )
        {
            textProperties.setFontNameComplex( ODFUtils.stripTrailingDigits( fontNameComplex ) );
        }

        // font-size
        String fontSize = ele.getFoFontSizeAttribute();
        if ( StringUtils.isNotEmpty( fontSize ) )
        {
            if ( ODFUtils.hasPercentUnit( fontSize ) )
            {
                // relative
                if ( textProperties.getFontSize() != Font.UNDEFINED )
                {
                    textProperties.setFontSize( textProperties.getFontSize() * ODFUtils.getDimensionAsPoint( fontSize ) );
                }
            }
            else
            {
                // absolute
                textProperties.setFontSize( ODFUtils.getDimensionAsPoint( fontSize ) );
            }
        }
        String fontSizeAsian = ele.getStyleFontSizeAsianAttribute();
        if ( StringUtils.isNotEmpty( fontSizeAsian ) )
        {
            if ( ODFUtils.hasPercentUnit( fontSizeAsian ) )
            {
                // relative
                if ( textProperties.getFontSizeAsian() != Font.UNDEFINED )
                {
                    textProperties.setFontSizeAsian( textProperties.getFontSizeAsian()
                        * ODFUtils.getDimensionAsPoint( fontSizeAsian ) );
                }
            }
            else
            {
                // absolute
                textProperties.setFontSizeAsian( ODFUtils.getDimensionAsPoint( fontSizeAsian ) );
            }
        }
        String fontSizeComplex = ele.getStyleFontSizeComplexAttribute();
        if ( StringUtils.isNotEmpty( fontSizeComplex ) )
        {
            if ( ODFUtils.hasPercentUnit( fontSizeComplex ) )
            {
                // relative
                if ( textProperties.getFontSizeComplex() != Font.UNDEFINED )
                {
                    textProperties.setFontSizeComplex( textProperties.getFontSizeComplex()
                        * ODFUtils.getDimensionAsPoint( fontSizeComplex ) );
                }
            }
            else
            {
                // absolute
                textProperties.setFontSizeComplex( ODFUtils.getDimensionAsPoint( fontSizeComplex ) );
            }
        }

        // font-style
        String fontStyle = ele.getFoFontStyleAttribute();
        if ( StringUtils.isNotEmpty( fontStyle ) )
        {
            if ( FoFontStyleAttribute.Value.NORMAL.toString().equals( fontStyle ) )
            {
                textProperties.setFontItalic( Boolean.FALSE );
            }
            else
            {
                // interpret other values as italic
                textProperties.setFontItalic( Boolean.TRUE );
            }
        }
        String fontStyleAsian = ele.getStyleFontStyleAsianAttribute();
        if ( StringUtils.isNotEmpty( fontStyleAsian ) )
        {
            if ( StyleFontStyleAsianAttribute.Value.NORMAL.toString().equals( fontStyleAsian ) )
            {
                textProperties.setFontItalicAsian( Boolean.FALSE );
            }
            else
            {
                // interpret other values as italic
                textProperties.setFontItalicAsian( Boolean.TRUE );
            }
        }
        String fontStyleComplex = ele.getStyleFontStyleComplexAttribute();
        if ( StringUtils.isNotEmpty( fontStyleComplex ) )
        {
            if ( StyleFontStyleComplexAttribute.Value.NORMAL.toString().equals( fontStyleComplex ) )
            {
                textProperties.setFontItalicComplex( Boolean.FALSE );
            }
            else
            {
                // interpret other values as italic
                textProperties.setFontItalicComplex( Boolean.TRUE );
            }
        }

        // font-variant
        String fontVariant = ele.getFoFontVariantAttribute();
        if ( StringUtils.isNotEmpty( fontVariant ) )
        {
        }

        // font-weight
        String fontWeight = ele.getFoFontWeightAttribute();
        if ( StringUtils.isNotEmpty( fontWeight ) )
        {
            if ( FoFontWeightAttribute.Value.NORMAL.toString().equals( fontWeight ) )
            {
                textProperties.setFontBold( Boolean.FALSE );
            }
            else
            {
                // interpret other values as bold
                textProperties.setFontBold( Boolean.TRUE );
            }
        }
        String fontWeightAsian = ele.getStyleFontWeightAsianAttribute();
        if ( StringUtils.isNotEmpty( fontWeightAsian ) )
        {
            if ( StyleFontWeightAsianAttribute.Value.NORMAL.toString().equals( fontWeightAsian ) )
            {
                textProperties.setFontBoldAsian( Boolean.FALSE );
            }
            else
            {
                // interpret other values as bold
                textProperties.setFontBoldAsian( Boolean.TRUE );
            }
        }
        String fontWeightComplex = ele.getStyleFontWeightComplexAttribute();
        if ( StringUtils.isNotEmpty( fontWeightComplex ) )
        {
            if ( StyleFontWeightComplexAttribute.Value.NORMAL.toString().equals( fontWeightComplex ) )
            {
                textProperties.setFontBoldComplex( Boolean.FALSE );
            }
            else
            {
                // interpret other values as bold
                textProperties.setFontBoldComplex( Boolean.TRUE );
            }
        }

        // text-underline-style
        String underlineStyle = ele.getStyleTextUnderlineStyleAttribute();
        if ( StringUtils.isNotEmpty( underlineStyle ) )
        {
            if ( StyleTextUnderlineStyleAttribute.Value.NONE.toString().equals( underlineStyle ) )
            {
                textProperties.setFontUnderline( Boolean.FALSE );
            }
            else
            {
                // interpret other values as underline
                textProperties.setFontUnderline( Boolean.TRUE );
            }
        }

        // text-underline-type
        String underlineType = ele.getStyleTextUnderlineTypeAttribute();
        if ( StringUtils.isNotEmpty( underlineType ) )
        {
        }

        // text-line-through-style
        String lineThroughStyle = ele.getStyleTextLineThroughStyleAttribute();
        if ( StringUtils.isNotEmpty( lineThroughStyle ) )
        {
            if ( StyleTextLineThroughStyleAttribute.Value.NONE.toString().equals( lineThroughStyle ) )
            {
                textProperties.setFontStrikeThru( Boolean.FALSE );
            }
            else
            {
                // interpret other values as strike thru
                textProperties.setFontStrikeThru( Boolean.TRUE );
            }
        }

        // text-position
        String textPositionStyle = ele.getStyleTextPositionAttribute();
        if ( StringUtils.isNotEmpty( textPositionStyle ) )
        {
            if ( textPositionStyle.contains( "super" ) )
            {
                textProperties.setTextPosition( 5.0f );
            }
            else if ( textPositionStyle.contains( "sub" ) )
            {
                textProperties.setTextPosition( -2.0f );
            }
        }

        super.visit( ele );
    }

    @Override
    public void visit( StyleTablePropertiesElement ele )
    {

        StyleTableProperties tableProperties = new StyleTableProperties();
        currentStyle.setTableProperties( tableProperties );

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            tableProperties.setBackgroundColor( ColorRegistry.getInstance().getColor( backgroundColor ) );

        }

        // width
        String width = ele.getStyleWidthAttribute();
        if ( StringUtils.isNotEmpty( width ) )
        {
            tableProperties.setWidth( ODFUtils.getDimensionAsPoint( width ) );
        }

        // align
        String align = ele.getTableAlignAttribute();
        if ( StringUtils.isNotEmpty( align ) )
        {
            if ( TableAlignAttribute.Value.LEFT.toString().equals( align ) )
            {
                tableProperties.setAlignment( Element.ALIGN_LEFT );
            }
            else if ( TableAlignAttribute.Value.RIGHT.toString().equals( align ) )
            {
                tableProperties.setAlignment( Element.ALIGN_RIGHT );
            }
            else
            {
                tableProperties.setAlignment( Element.ALIGN_CENTER );
            }
        }

        // margin
        String margin = ele.getFoMarginAttribute();
        if ( StringUtils.isNotEmpty( margin ) )
        {
            tableProperties.setMargin( ODFUtils.getDimensionAsPoint( margin ) );
        }

        // margin-bottom
        String marginBottom = ele.getFoMarginBottomAttribute();
        if ( StringUtils.isNotEmpty( marginBottom ) )
        {
            tableProperties.setMarginBottom( ODFUtils.getDimensionAsPoint( marginBottom ) );
        }

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            tableProperties.setMarginLeft( ODFUtils.getDimensionAsPoint( marginLeft ) );
        }

        // margin-right
        String marginRight = ele.getFoMarginRightAttribute();
        if ( StringUtils.isNotEmpty( marginRight ) )
        {
            tableProperties.setMarginRight( ODFUtils.getDimensionAsPoint( marginRight ) );
        }

        // margin-top
        String marginTop = ele.getFoMarginTopAttribute();
        if ( StringUtils.isNotEmpty( marginTop ) )
        {
            tableProperties.setMarginTop( ODFUtils.getDimensionAsPoint( marginTop ) );
        }

        // may-break-between-rows
        Boolean mayBreakBetweenRows = ele.getStyleMayBreakBetweenRowsAttribute();
        if ( mayBreakBetweenRows != null )
        {
            tableProperties.setMayBreakBetweenRows( mayBreakBetweenRows );
        }

        super.visit( ele );
    }

    @Override
    public void visit( StyleTableRowPropertiesElement ele )
    {

        StyleTableRowProperties tableRowProperties = currentStyle.getTableRowProperties();
        if ( tableRowProperties == null )
        {
            tableRowProperties = new StyleTableRowProperties();
            currentStyle.setTableRowProperties( tableRowProperties );
        }

        // min-height
        String minHeight = ele.getStyleMinRowHeightAttribute();
        if ( StringUtils.isNotEmpty( minHeight ) )
        {
            tableRowProperties.setMinRowHeight( ODFUtils.getDimensionAsPoint( minHeight ) );
        }

        // height
        String height = ele.getStyleRowHeightAttribute();
        if ( StringUtils.isNotEmpty( height ) )
        {
            tableRowProperties.setRowHeight( ODFUtils.getDimensionAsPoint( height ) );
        }

        // keep-together
        String keepTogether = ele.getFoKeepTogetherAttribute();
        if ( StringUtils.isNotEmpty( keepTogether ) )
        {
            if ( FoKeepTogetherAttribute.Value.ALWAYS.toString().equals( keepTogether ) )
            {
                tableRowProperties.setKeepTogether( Boolean.TRUE );
            }
            else
            {
                tableRowProperties.setKeepTogether( Boolean.FALSE );
            }
        }
    }

    @Override
    public void visit( StyleTableCellPropertiesElement ele )
    {
        StyleTableCellProperties tableCellProperties = currentStyle.getTableCellProperties();
        if ( tableCellProperties == null )
        {
            tableCellProperties = new StyleTableCellProperties();
            currentStyle.setTableCellProperties( tableCellProperties );
        }

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            tableCellProperties.setBackgroundColor( ColorRegistry.getInstance().getColor( backgroundColor ) );
        }

        // border
        String border = ele.getFoBorderAttribute();
        if ( StringUtils.isNotEmpty( border ) )
        {
            tableCellProperties.setBorder( new StyleBorder( border, BorderType.ALL ) );
        }

        // border-bottom
        String borderBottom = ele.getFoBorderBottomAttribute();
        if ( StringUtils.isNotEmpty( borderBottom ) )
        {
            tableCellProperties.setBorderBottom( new StyleBorder( borderBottom, BorderType.BOTTOM ) );
        }

        // border-left
        String borderLeft = ele.getFoBorderLeftAttribute();
        if ( StringUtils.isNotEmpty( borderLeft ) )
        {
            tableCellProperties.setBorderLeft( new StyleBorder( borderLeft, BorderType.LEFT ) );
        }

        // border-bottom
        String borderRight = ele.getFoBorderRightAttribute();
        if ( StringUtils.isNotEmpty( borderRight ) )
        {
            tableCellProperties.setBorderRight( new StyleBorder( borderRight, BorderType.RIGHT ) );
        }

        // border-top
        String borderTop = ele.getFoBorderTopAttribute();
        if ( StringUtils.isNotEmpty( borderTop ) )
        {
            tableCellProperties.setBorderTop( new StyleBorder( borderTop, BorderType.TOP ) );
        }

        // padding
        String padding = ele.getFoPaddingAttribute();
        if ( StringUtils.isNotEmpty( padding ) )
        {
            tableCellProperties.setPadding( ODFUtils.getDimensionAsPoint( padding ) );
        }

        // padding-bottom
        String paddingBottom = ele.getFoPaddingBottomAttribute();
        if ( StringUtils.isNotEmpty( paddingBottom ) )
        {
            tableCellProperties.setPaddingBottom( ODFUtils.getDimensionAsPoint( paddingBottom ) );
        }

        // padding-left
        String paddingLeft = ele.getFoPaddingLeftAttribute();
        if ( StringUtils.isNotEmpty( paddingLeft ) )
        {
            tableCellProperties.setPaddingLeft( ODFUtils.getDimensionAsPoint( paddingLeft ) );
        }

        // padding-right
        String paddingRight = ele.getFoPaddingRightAttribute();
        if ( StringUtils.isNotEmpty( paddingRight ) )
        {
            tableCellProperties.setPaddingRight( ODFUtils.getDimensionAsPoint( paddingRight ) );
        }

        // padding-top
        String paddingTop = ele.getFoPaddingTopAttribute();
        if ( StringUtils.isNotEmpty( paddingTop ) )
        {
            tableCellProperties.setPaddingTop( ODFUtils.getDimensionAsPoint( paddingTop ) );
        }

        // vertical-align
        String verticalAlign = ele.getStyleVerticalAlignAttribute();
        if ( StringUtils.isNotEmpty( verticalAlign ) )
        {
            if ( StyleVerticalAlignAttribute.Value.BASELINE.toString().equals( verticalAlign ) )
            {
                tableCellProperties.setVerticalAlignment( Element.ALIGN_BASELINE );
            }
            else if ( StyleVerticalAlignAttribute.Value.TOP.toString().equals( verticalAlign ) )
            {
                tableCellProperties.setVerticalAlignment( Element.ALIGN_TOP );
            }
            else if ( StyleVerticalAlignAttribute.Value.MIDDLE.toString().equals( verticalAlign ) )
            {
                tableCellProperties.setVerticalAlignment( Element.ALIGN_MIDDLE );
            }
            else if ( StyleVerticalAlignAttribute.Value.BOTTOM.toString().equals( verticalAlign ) )
            {
                tableCellProperties.setVerticalAlignment( Element.ALIGN_BOTTOM );
            }
        }

        super.visit( ele );
    }

    // visit //style:graphic-properties
    @Override
    public void visit( StyleGraphicPropertiesElement ele )
    {
        StyleGraphicProperties graphicProperties = currentStyle.getGraphicProperties();
        if ( graphicProperties == null )
        {
            graphicProperties = new StyleGraphicProperties();
            currentStyle.setGraphicProperties( graphicProperties );
        }

        // wrap
        String wrap = ele.getStyleWrapAttribute();
        if ( StringUtils.isNotEmpty( wrap ) )
        {
            if ( StyleWrapAttribute.Value.RUN_THROUGH.toString().equals( wrap ) )
            {
                graphicProperties.setRunThrough( Boolean.TRUE );
            }
            else
            {
                // other values are not supported
                graphicProperties.setRunThrough( Boolean.FALSE );
            }
        }

        super.visit( ele );
    }

    // list styling is a bit complicated because of nesting
    // we flatten the structure into a map of {list-level->StyleListProperties}
    //
    // 1st level - text:list-style --- enclosing style
    // 2nd level --- text:list-level-style-bullet --- if list item label is a char
    // 2nd level --- text:list-level-style-image --- if list item label is an image
    // 2nd level --- text:list-level-style-number --- if list item label is a number
    // 3rd level ----- style:list-level-properties --- image dimensions, indentation info
    // 4th level ------- style:list-level-label-alignment --- indentation info
    @Override
    public void visit( TextListStyleElement ele )
    {
        currentListPropertiesMap = new HashMap<Integer, StyleListProperties>();
        computeStyle( ele, false );
        currentStyle.setListPropertiesMap( currentListPropertiesMap );
        currentListPropertiesMap = null;
    }

    @Override
    public void visit( TextListLevelStyleBulletElement ele )
    {
        Integer textLevel = ele.getTextLevelAttribute();
        if ( currentListPropertiesMap == null || textLevel == null )
        {
            return;
        }
        StyleListProperties listProperties = new StyleListProperties();
        currentListPropertiesMap.put( textLevel, listProperties );

        // text style to apply to list item label
        String styleName = ele.getTextStyleNameAttribute();
        if ( StringUtils.isNotEmpty( styleName ) )
        {
            // TODO what if this style is not computed yet?
            Style style = getStyle( OdfStyleFamily.Text.getName(), styleName, null );
            if ( style != null )
            {
                listProperties.setTextProperties( style.getTextProperties() );
            }
        }

        // bullet-char
        String bulletChar = ele.getTextBulletCharAttribute();
        if ( StringUtils.isNotEmpty( bulletChar ) )
        {
            listProperties.setBulletChar( bulletChar );
        }

        // num-prefix
        String numPrefix = ele.getStyleNumPrefixAttribute();
        if ( StringUtils.isNotEmpty( numPrefix ) )
        {
            listProperties.setNumPrefix( numPrefix );
        }

        // num-suffix
        String numSuffix = ele.getStyleNumSuffixAttribute();
        if ( StringUtils.isNotEmpty( numSuffix ) )
        {
            listProperties.setNumSuffix( numSuffix );
        }

        currentListProperties = listProperties;
        super.visit( ele );
        currentListProperties = null;
    }

    @Override
    public void visit( TextListLevelStyleImageElement ele )
    {
        Integer textLevel = ele.getTextLevelAttribute();
        if ( currentListPropertiesMap == null || textLevel == null )
        {
            return;
        }
        StyleListProperties listProperties = new StyleListProperties();
        currentListPropertiesMap.put( textLevel, listProperties );

        // image href
        String href = ele.getXlinkHrefAttribute();
        if ( StringUtils.isNotEmpty( href ) )
        {
            byte[] imageStream = odfDocument.getPackage().getBytes( href );
            if ( imageStream != null )
            {
                Image imageObj = StylableImage.getImage( imageStream );
                if ( imageObj != null )
                {
                    listProperties.setImage( imageObj );
                }
            }
        }

        currentListProperties = listProperties;
        super.visit( ele );
        currentListProperties = null;
    }

    @Override
    public void visit( TextListLevelStyleNumberElement ele )
    {
        Integer textLevel = ele.getTextLevelAttribute();
        if ( currentListPropertiesMap == null || textLevel == null )
        {
            return;
        }
        StyleListProperties listProperties = new StyleListProperties();
        currentListPropertiesMap.put( textLevel, listProperties );

        // text style to apply to list item label
        String styleName = ele.getTextStyleNameAttribute();
        if ( StringUtils.isNotEmpty( styleName ) )
        {
            // TODO what if this style is not computed yet?
            Style style = getStyle( OdfStyleFamily.Text.getName(), styleName, null );
            if ( style != null )
            {
                listProperties.setTextProperties( style.getTextProperties() );
            }
        }

        // start-value
        Integer startValue = ele.getTextStartValueAttribute();
        if ( startValue != null )
        {
            listProperties.setStartValue( startValue );
        }

        // display-levels
        Integer displayLevels = ele.getTextDisplayLevelsAttribute();
        if ( displayLevels != null )
        {
            listProperties.setDisplayLevels( displayLevels );
        }

        // num-prefix
        String numPrefix = ele.getStyleNumPrefixAttribute();
        if ( StringUtils.isNotEmpty( numPrefix ) )
        {
            listProperties.setNumPrefix( numPrefix );
        }

        // num-suffix
        String numSuffix = ele.getStyleNumSuffixAttribute();
        if ( StringUtils.isNotEmpty( numSuffix ) )
        {
            listProperties.setNumSuffix( numSuffix );
        }

        // num-format
        String numFormat = ele.getStyleNumFormatAttribute();
        if ( StringUtils.isNotEmpty( numFormat ) )
        {
            if ( StyleNumFormatAttribute.Value.a.toString().equals( numFormat ) )
            {
                listProperties.setNumFormat( StyleNumFormat.createAlphabetical( true ) );
            }
            else if ( StyleNumFormatAttribute.Value.A.toString().equals( numFormat ) )
            {
                listProperties.setNumFormat( StyleNumFormat.createAlphabetical( false ) );
            }
            else if ( StyleNumFormatAttribute.Value.i.toString().equals( numFormat ) )
            {
                listProperties.setNumFormat( StyleNumFormat.createRoman( true ) );
            }
            else if ( StyleNumFormatAttribute.Value.I.toString().equals( numFormat ) )
            {
                listProperties.setNumFormat( StyleNumFormat.createRoman( false ) );
            }
            else
            {
                listProperties.setNumFormat( StyleNumFormat.createNumerical() );
            }
        }

        currentListProperties = listProperties;
        super.visit( ele );
        currentListProperties = null;
    }

    @Override
    public void visit( StyleListLevelPropertiesElement ele )
    {
        if ( currentListProperties == null )
        {
            return;
        }
        StyleListProperties listProperties = currentListProperties;

        // width
        String width = ele.getFoWidthAttribute();
        if ( StringUtils.isNotEmpty( width ) )
        {
            listProperties.setWidth( ODFUtils.getDimensionAsPoint( width ) );
        }

        // height
        String height = ele.getFoHeightAttribute();
        if ( StringUtils.isNotEmpty( height ) )
        {
            listProperties.setHeight( ODFUtils.getDimensionAsPoint( height ) );
        }

        // space-before
        String spaceBefore = ele.getTextSpaceBeforeAttribute();
        if ( StringUtils.isNotEmpty( spaceBefore ) )
        {
            listProperties.setSpaceBefore( ODFUtils.getDimensionAsPoint( spaceBefore ) );
        }

        // min-label-width
        String minLabelWidth = ele.getTextMinLabelWidthAttribute();
        if ( StringUtils.isNotEmpty( minLabelWidth ) )
        {
            listProperties.setMinLabelWidth( ODFUtils.getDimensionAsPoint( minLabelWidth ) );
        }

        super.visit( ele );
    }

    @Override
    public void visit( StyleListLevelLabelAlignmentElement ele )
    {
        if ( currentListProperties == null )
        {
            return;
        }
        StyleListProperties listProperties = currentListProperties;

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            listProperties.setMarginLeft( ODFUtils.getDimensionAsPoint( marginLeft ) );
        }

        // text-indent
        String textIndent = ele.getFoTextIndentAttribute();
        if ( StringUtils.isNotEmpty( textIndent ) )
        {
            listProperties.setTextIndent( ODFUtils.getDimensionAsPoint( textIndent ) );
        }

        super.visit( ele );
    }

    // outline styling is similar to list styling, use the same data structures
    // we flatten the structure into a map of {outline-level->StyleListProperties}
    // there may be only one default outline style
    //
    // 1st level - text:outline-style --- enclosing style
    // 2nd level --- text:outline-level-style --- number format, start value, ...
    // 3rd level ----- style:list-level-properties --- ignored

    @Override
    public void visit( TextOutlineStyleElement ele )
    {
        currentListPropertiesMap = new HashMap<Integer, StyleListProperties>();
        computeStyle( ele, true );
        currentStyle.setOutlinePropertiesMap( currentListPropertiesMap );
        currentListPropertiesMap = null;
    }

    @Override
    public void visit( TextOutlineLevelStyleElement ele )
    {
        Integer textLevel = ele.getTextLevelAttribute();
        if ( currentListPropertiesMap == null || textLevel == null )
        {
            return;
        }
        StyleListProperties listProperties = new StyleListProperties();
        currentListPropertiesMap.put( textLevel, listProperties );

        // text style to apply to list item label
        String styleName = ele.getTextStyleNameAttribute();
        if ( StringUtils.isNotEmpty( styleName ) )
        {
            // TODO what if this style is not computed yet?
            Style style = getStyle( OdfStyleFamily.Text.getName(), styleName, null );
            if ( style != null )
            {
                listProperties.setTextProperties( style.getTextProperties() );
            }
        }

        // start-value
        Integer startValue = ele.getTextStartValueAttribute();
        if ( startValue != null )
        {
            listProperties.setStartValue( startValue );
        }

        // display-levels
        Integer displayLevels = ele.getTextDisplayLevelsAttribute();
        if ( displayLevels != null )
        {
            listProperties.setDisplayLevels( displayLevels );
        }

        // num-prefix
        String numPrefix = ele.getStyleNumPrefixAttribute();
        if ( StringUtils.isNotEmpty( numPrefix ) )
        {
            listProperties.setNumPrefix( numPrefix );
        }

        // num-suffix
        String numSuffix = ele.getStyleNumSuffixAttribute();
        if ( StringUtils.isNotEmpty( numSuffix ) )
        {
            listProperties.setNumSuffix( numSuffix );
        }

        // num-format
        String numFormat = ele.getStyleNumFormatAttribute();
        if ( StringUtils.isNotEmpty( numFormat ) )
        {
            if ( StyleNumFormatAttribute.Value.a.toString().equals( numFormat ) )
            {
                listProperties.setNumFormat( StyleNumFormat.createAlphabetical( true ) );
            }
            else if ( StyleNumFormatAttribute.Value.A.toString().equals( numFormat ) )
            {
                listProperties.setNumFormat( StyleNumFormat.createAlphabetical( false ) );
            }
            else if ( StyleNumFormatAttribute.Value.i.toString().equals( numFormat ) )
            {
                listProperties.setNumFormat( StyleNumFormat.createRoman( true ) );
            }
            else if ( StyleNumFormatAttribute.Value.I.toString().equals( numFormat ) )
            {
                listProperties.setNumFormat( StyleNumFormat.createRoman( false ) );
            }
            else
            {
                listProperties.setNumFormat( StyleNumFormat.createNumerical() );
            }
        }

        currentListProperties = listProperties;
        super.visit( ele );
        currentListProperties = null;
    }

    // visit //style:section-properties
    @Override
    public void visit( StyleSectionPropertiesElement ele )
    {
        StyleSectionProperties sectionProperties = currentStyle.getSectionProperties();
        if ( sectionProperties == null )
        {
            sectionProperties = new StyleSectionProperties();
            currentStyle.setSectionProperties( sectionProperties );
        }

        // background-color
        String backgroundColor = ele.getFoBackgroundColorAttribute();
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            sectionProperties.setBackgroundColor( ColorRegistry.getInstance().getColor( backgroundColor ) );
        }

        // dont-balance-text-columns
        Boolean dontBalanceTextColumns = ele.getTextDontBalanceTextColumnsAttribute();
        if ( dontBalanceTextColumns != null )
        {
            sectionProperties.setDontBalanceTextColumns( dontBalanceTextColumns );
        }

        // margin-left
        String marginLeft = ele.getFoMarginLeftAttribute();
        if ( StringUtils.isNotEmpty( marginLeft ) )
        {
            sectionProperties.setMarginLeft( ODFUtils.getDimensionAsPoint( marginLeft ) );
        }

        // margin-right
        String marginRight = ele.getFoMarginRightAttribute();
        if ( StringUtils.isNotEmpty( marginRight ) )
        {
            sectionProperties.setMarginRight( ODFUtils.getDimensionAsPoint( marginRight ) );
        }

        super.visit( ele );
    }

    // visit //style:columns
    @Override
    public void visit( StyleColumnsElement ele )
    {
        StyleSectionProperties sectionProperties = currentStyle.getSectionProperties();
        if ( sectionProperties == null )
        {
            sectionProperties = new StyleSectionProperties();
            currentStyle.setSectionProperties( sectionProperties );
        }

        // column-count
        Integer columnCount = ele.getFoColumnCountAttribute();
        if ( columnCount != null )
        {
            sectionProperties.setColumnCount( columnCount );
        }

        // column-gap
        String columnGap = ele.getFoColumnGapAttribute();
        if ( StringUtils.isNotEmpty( columnGap ) )
        {
            sectionProperties.setColumnGap( ODFUtils.getDimensionAsPoint( columnGap ) );
        }

        currentColumnPropertiesList = new ArrayList<StyleColumnProperties>();
        super.visit( ele );
        currentStyle.setColumnPropertiesList( currentColumnPropertiesList );
        currentColumnPropertiesList = null;
    }

    // visit //style:column
    @Override
    public void visit( StyleColumnElement ele )
    {
        if ( currentColumnPropertiesList == null )
        {
            return;
        }
        StyleColumnProperties columnProperties = new StyleColumnProperties();
        currentColumnPropertiesList.add( columnProperties );

        // rel-width
        String relWidth = ele.getStyleRelWidthAttribute();
        if ( StringUtils.isNotEmpty( relWidth ) )
        {
            columnProperties.setRelWidth( ODFUtils.getRelativeSize( relWidth ) );
        }

        // start-indent
        String startIndent = ele.getFoStartIndentAttribute();
        if ( StringUtils.isNotEmpty( startIndent ) )
        {
            columnProperties.setStartIndent( ODFUtils.getDimensionAsPoint( startIndent ) );
        }

        // end-indent
        String endIndent = ele.getFoEndIndentAttribute();
        if ( StringUtils.isNotEmpty( endIndent ) )
        {
            columnProperties.setEndIndent( ODFUtils.getDimensionAsPoint( endIndent ) );
        }

        super.visit( ele );
    }

    private String getStyleId( String familyName, String styleName )
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

    private Style getStyle( String familyName, String styleName )
    {
        return stylesMap.get( getStyleId( familyName, styleName ) );
    }

    public Style getStyle( String familyName, String styleName, Style parentElementStyle )
    {
        String newStyleName = null;
        String newFamilyName = null;
        String newMasterPageName = null;
        Style style = getStyle( familyName, styleName );
        if ( style != null )
        {
            newStyleName = style.getStyleName();
            newFamilyName = style.getFamilyName();
            newMasterPageName = style.getMasterPageName();
        }
        else if ( parentElementStyle != null )
        {
            newStyleName = parentElementStyle.getStyleName();
            newFamilyName = parentElementStyle.getFamilyName();
            newMasterPageName = parentElementStyle.getMasterPageName();
        }
        // create style
        // merge order should be:
        // 1 default style
        // 2 parent element style
        // 3 current style
        Style newStyle = new Style( options.getFontProvider(), newStyleName, newFamilyName, newMasterPageName );
        if ( parentElementStyle != null )
        {
            // parent element style contains default style
            newStyle.merge( parentElementStyle, true );
        }
        else
        {
            // merge default styles
            for ( Style s : stylesMap.values() )
            {
                if ( s.getStyleName() == null )
                {
                    newStyle.merge( s, true );
                }
            }
        }
        // merge current style
        if ( style != null )
        {
            newStyle.merge( style, true );
        }
        return newStyle;
    }

}
