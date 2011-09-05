/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package org.odftoolkit.odfdom.converter.internal.itext;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.odftoolkit.odfdom.converter.internal.AbstractStyleEngine;
import org.odftoolkit.odfdom.converter.internal.itext.styles.Style;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleBorder;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleHeaderFooterProperties;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleMargin;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StylePadding;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StylePageLayoutProperties;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleParagraphProperties;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleTableCellProperties;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleTableProperties;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleTableRowProperties;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleTextProperties;
import org.odftoolkit.odfdom.converter.internal.utils.ODFUtils;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.OdfStyleBase;
import org.odftoolkit.odfdom.dom.element.office.OfficeAutomaticStylesElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeMasterStylesElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeStylesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleDefaultStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StyleFooterStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StyleHeaderFooterPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StylePageLayoutElement;
import org.odftoolkit.odfdom.dom.element.style.StylePageLayoutPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleParagraphPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableCellPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTablePropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableRowPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement;
import org.odftoolkit.odfdom.dom.element.text.TextListStyleElement;
import org.w3c.dom.Node;

import com.lowagie.text.Element;
import com.lowagie.text.Font;

import fr.opensagres.xdocreport.itext.extension.PageOrientation;
import fr.opensagres.xdocreport.utils.BorderType;
import fr.opensagres.xdocreport.utils.StringUtils;

public class StyleEngineForIText extends AbstractStyleEngine {

	private static final String PORTRAIT = "portrait";
	private static final String LANDSCAPE = "landscape";
	private static final String BOTTOM = "bottom";
	private static final String MIDDLE = "middle";
	private static final String TOP = "top";
	private static final String BASELINE = "baseline";
	private static final String NONE = "none";
	private static final String PAGE_BREAK = "page";
	protected static final String BOLD = "bold";
	protected static final String ITALIC = "italic";

	private Style currentStyle = null;
	// private StyleForItext currentStyle;
	private final Map<String, Style> stylesMap = new HashMap<String, Style>();

	public StyleEngineForIText(OdfDocument odfDocument) {
		super(odfDocument);
	}

	public void visit(OfficeStylesElement ele) {
		super.visit(ele);
	}

	public void visit(OfficeAutomaticStylesElement ele) {
		super.visit(ele);
	}

	public void visit(OfficeMasterStylesElement ele) {
		super.visit(ele);
	}

	public void visit(StyleDefaultStyleElement ele) {
		computeStyle(ele, true);
	}

	public void visit(StyleStyleElement ele) {
		computeStyle(ele, false);
	}

	public void visit(StylePageLayoutElement ele) {
		computeStyle(ele, false);
	}

	private Style computeStyle(OdfStyleBase styleBase, boolean isDefaultStyle) {
		if (styleBase == null) {
			return null;
		}
		// 1) Check if style is in the cache
		String familyName = styleBase.getFamilyName();
		String styleName = null;
		String masterPageName = null;
		if (!isDefaultStyle) {
			if (styleBase instanceof StyleStyleElement) {
				StyleStyleElement styleElement = (StyleStyleElement) styleBase;
				styleName = styleElement.getStyleNameAttribute();
				masterPageName = styleElement.getStyleMasterPageNameAttribute();
			} else if (styleBase instanceof StylePageLayoutElement) {
				styleName = ((StylePageLayoutElement) styleBase)
						.getStyleNameAttribute();
			}
		}

		String styleId = getStyleId(familyName, styleName);
		Style style = stylesMap.get(styleId);
		if (style != null) {
			// style already computed
			return style;
		}

		// 3) Create style
		style = new Style(styleName, familyName, masterPageName);

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
		OdfStyleBase parentStyleBase = styleBase.getParentStyle();
		while (parentStyleBase != null) {
			Style parentStyle = computeStyle(parentStyleBase, isDefaultStyle);
			if (parentStyle != null) {
				if (parentsStyles == null) {
					parentsStyles = new ArrayList<Style>();
				}
				parentsStyles.add(0, parentStyle);
			}
			parentStyleBase = parentStyleBase.getParentStyle();
		}

		if (parentsStyles != null) {
			for (Style parentsStyle : parentsStyles) {
				style.merge(parentsStyle);
			}
		}
		// 6) Apply current style
		currentStyle = style;
		super.visit(styleBase);

		// 7) register style in the cache
		stylesMap.put(styleId, style);
		return style;
	}

	// visit //style:paragraph-properties

	@Override
	public void visit(StyleParagraphPropertiesElement ele) {

		StyleParagraphProperties paragraphProperties = currentStyle
				.getParagraphProperties();
		if (paragraphProperties == null) {
			paragraphProperties = new StyleParagraphProperties();
			currentStyle.setParagraphProperties(paragraphProperties);
		}

		Float indentation = null;

		// background-color
		String backgroundColor = ele.getFoBackgroundColorAttribute();
		if (StringUtils.isNotEmpty(backgroundColor)) {
			paragraphProperties.setBackgroundColor(ColorRegistry.getInstance()
					.getColor(backgroundColor));
		}

		// border
		String border = ele.getFoBorderAttribute();
		if (StringUtils.isNotEmpty(border)) {
			paragraphProperties.setBorder(new StyleBorder(border,
					BorderType.ALL));
		}
		// border-bottom
		String borderBottom = ele.getFoBorderBottomAttribute();
		if (StringUtils.isNotEmpty(borderBottom)) {
			paragraphProperties.setBorderBottom(new StyleBorder(borderBottom,
					BorderType.BOTTOM));
		}

		// border-left
		String borderLeft = ele.getFoBorderLeftAttribute();
		if (StringUtils.isNotEmpty(borderLeft)) {
			paragraphProperties.setBorderLeft(new StyleBorder(borderBottom,
					BorderType.LEFT));
		}

		// border-bottom
		String borderRight = ele.getFoBorderRightAttribute();
		if (StringUtils.isNotEmpty(borderRight)) {
			paragraphProperties.setBorderRight(new StyleBorder(borderRight,
					BorderType.RIGHT));
		}

		// border-top
		String borderTop = ele.getFoBorderTopAttribute();
		if (StringUtils.isNotEmpty(borderTop)) {
			paragraphProperties.setBorderTop(new StyleBorder(borderTop,
					BorderType.TOP));
		}

		// line-height
		String lineHeight = ele.getFoLineHeightAttribute();
		if (StringUtils.isNotEmpty(lineHeight)) {
			paragraphProperties.setLineHeight(ODFUtils
					.getDimensionAsPoint(lineHeight));
		}

		// margin
		String margin = ele.getFoMarginAttribute();
		if (StringUtils.isNotEmpty(margin)) {
			// cssStyleSheet.setCSSProperty("margin", margin);
		}

		// margin-bottom
		String marginBottom = ele.getFoMarginBottomAttribute();
		if (StringUtils.isNotEmpty(marginBottom)) {
			// cssStyleSheet.setCSSProperty("margin-bottom", marginBottom);
		}

		// margin-left
		String marginLeft = ele.getFoMarginLeftAttribute();
		if (StringUtils.isNotEmpty(marginLeft)) {
			indentation = ODFUtils.getDimensionAsPoint(marginLeft);

			// paragraphProperties.setIndentation(ODFUtils.getDimensionAsPoint(marginLeft));
		}

		// margin-bottom
		String marginRight = ele.getFoMarginRightAttribute();
		if (StringUtils.isNotEmpty(marginRight)) {
			// cssStyleSheet.setCSSProperty("margin-right", marginRight);
		}

		// margin-top
		String marginTop = ele.getFoMarginTopAttribute();
		if (StringUtils.isNotEmpty(marginTop)) {
			// cssStyleSheet.setCSSProperty("margin-top", marginTop);
		}

		// padding
		String padding = ele.getFoPaddingAttribute();
		if (StringUtils.isNotEmpty(padding)) {
			// cssStyleSheet.setCSSProperty("padding", padding);
		}

		// padding-bottom
		String paddingBottom = ele.getFoPaddingBottomAttribute();
		if (StringUtils.isNotEmpty(paddingBottom)) {
			// cssStyleSheet.setCSSProperty("padding-bottom", paddingBottom);
		}

		// padding-left
		String paddingLeft = ele.getFoPaddingLeftAttribute();
		if (StringUtils.isNotEmpty(paddingLeft)) {
			// cssStyleSheet.setCSSProperty("padding-left", paddingLeft);
		}

		// padding-bottom
		String paddingRight = ele.getFoPaddingRightAttribute();
		if (StringUtils.isNotEmpty(paddingRight)) {
			// cssStyleSheet.setCSSProperty("padding-right", paddingRight);
		}

		// padding-top
		String paddingTop = ele.getFoPaddingTopAttribute();
		if (StringUtils.isNotEmpty(paddingTop)) {
			// cssStyleSheet.setCSSProperty("padding-top", paddingTop);
		}

		// text-align
		String textAlign = ele.getFoTextAlignAttribute();
		if (StringUtils.isNotEmpty(textAlign)) {
			int alignment = Element.ALIGN_UNDEFINED;
			if ("start".equals(textAlign)) {
				alignment = Element.ALIGN_LEFT;
			} else if ("end".equals(textAlign)) {
				alignment = Element.ALIGN_RIGHT;
			} else if ("left".equals(textAlign)) {
				alignment = Element.ALIGN_LEFT;
			} else if ("right".equals(textAlign)) {
				alignment = Element.ALIGN_RIGHT;
			} else if ("center".equals(textAlign)) {
				alignment = Element.ALIGN_CENTER;
			} else if ("justify".equals(textAlign)) {
				alignment = Element.ALIGN_JUSTIFIED;
			}
			paragraphProperties.setAlignment(alignment);
		}

		Boolean autoTextIndent = ele.getStyleAutoTextIndentAttribute();
		if (autoTextIndent != null) {
			paragraphProperties.setAutoTextIndent(autoTextIndent);
		}
		
		// text-indent
		String textIndent = ele.getFoTextIndentAttribute();
		if (StringUtils.isNotEmpty(textIndent)) {
			Float textIndentAsFloat = ODFUtils.getDimensionAsPoint(textIndent);
			if (indentation == null || indentation < textIndentAsFloat) {
				// no margin-left defined
				indentation = textIndentAsFloat;
			}
		}
		paragraphProperties.setIndentation(indentation);

		// fo:break-before
		String breakBefore = ele.getFoBreakBeforeAttribute();
		if (PAGE_BREAK.equals(breakBefore)) {
			paragraphProperties.setBreakBeforePage(true);
		}
		
		String breakAfter = ele.getFoBreakAfterAttribute();
		if (PAGE_BREAK.equals(breakAfter)) {
			paragraphProperties.setBreakAfterPage(true);
		}
		
		super.visit(ele);
	}

	public void visit(StyleTextPropertiesElement ele) {
		StyleTextProperties textProperties = currentStyle.getTextProperties();
		if (textProperties == null) {
			textProperties = new StyleTextProperties();
			currentStyle.setTextProperties(textProperties);
		}

		// background-color
		String backgroundColor = ele.getFoBackgroundColorAttribute();
		if (StringUtils.isNotEmpty(backgroundColor)) {
			textProperties.setBackgroundColor(ColorRegistry.getInstance()
					.getColor(backgroundColor));
		}

		boolean hasFontProperty = false;
		// color
		Color fontColor = null;
		String color = ele.getFoColorAttribute();
		if (StringUtils.isNotEmpty(color)) {
			fontColor = ColorRegistry.getInstance().getColor(color);
			hasFontProperty = true;
		}

		// font-family
		String familyName = null;
		String fontFamily = ele.getFoFontFamilyAttribute();
		if (StringUtils.isNotEmpty(fontFamily)) {
			familyName = fontFamily;
			hasFontProperty = true;
		}

		// font-name
		String fontName = ele.getStyleFontNameAttribute();
		if (StringUtils.isNotEmpty(fontName)) {
			familyName = fontName;
			hasFontProperty = true;
		}

		// font-size
		float size = Font.UNDEFINED;
		String fontSize = ele.getFoFontSizeAttribute();
		if (StringUtils.isNotEmpty(fontSize)) {
			size = ODFUtils.getDimensionAsPoint(fontSize);
			hasFontProperty = true;
		}

		int style = Font.NORMAL;
		// font-style
		String fontStyle = ele.getFoFontStyleAttribute();
		if (StringUtils.isNotEmpty(fontStyle)) {
			if (ITALIC.equals(fontStyle)) {
				style |= Font.ITALIC;
				hasFontProperty = true;
			}
		}

		// font-variant
		String fontVariant = ele.getFoFontVariantAttribute();
		if (StringUtils.isNotEmpty(fontVariant)) {
			// cssStyleSheet.setCSSProperty("font-variant", fontVariant);
		}

		// font-weight
		String fontWeight = ele.getFoFontWeightAttribute();
		if (StringUtils.isNotEmpty(fontWeight)) {
			if (BOLD.equals(fontWeight)) {
				style |= Font.BOLD;
				hasFontProperty = true;
			}
		}

		// text-underline-style
		String underlineStyle = ele.getStyleTextUnderlineStyleAttribute();
		if (StringUtils.isNotEmpty(underlineStyle)
				&& !underlineStyle.equals(NONE)) {
			style |= Font.UNDERLINE;
			hasFontProperty = true;
		}

		// text-underline-type
		String underlineType = ele.getStyleTextUnderlineTypeAttribute();
		if (StringUtils.isNotEmpty(underlineType)
				&& !underlineType.equals(NONE)) {
			// cssStyleSheet.setCSSProperty("text-decoration", "underline");
		}

		if (hasFontProperty) {
			Font font = ODFFontRegistry.getRegistry().getFont(familyName, size,
					style, fontColor);
			Font oldFont = textProperties.getFont();
			if (oldFont == null) {
				textProperties.setFont(font);
			} else {
				if (oldFont.compareTo(font) != 0) {
					textProperties.setFont(oldFont.difference(font));
				}
			}
		}

		super.visit(ele);
	}

	public void visit(StyleTablePropertiesElement ele) {

		StyleTableProperties tableProperties = new StyleTableProperties();
		currentStyle.setTableProperties(tableProperties);

		// background-color
		String backgroundColor = ele.getFoBackgroundColorAttribute();
		if (StringUtils.isNotEmpty(backgroundColor)) {
			tableProperties.setBackgroundColor(ColorRegistry.getInstance()
					.getColor(backgroundColor));

		}

		// // margin
		// String margin = ele.getFoMarginAttribute();
		// if (StringUtils.isNotEmpty(margin)) {
		// cssStyleSheet.setCSSProperty("margin", margin);
		// }
		//
		// // margin-bottom
		// String marginBottom = ele.getFoMarginBottomAttribute();
		// if (StringUtils.isNotEmpty(marginBottom)) {
		// cssStyleSheet.setCSSProperty("margin-bottom", marginBottom);
		// }
		//
		// // margin-left
		// String marginLeft = ele.getFoMarginLeftAttribute();
		// if (StringUtils.isNotEmpty(marginLeft)) {
		// cssStyleSheet.setCSSProperty("margin-left", marginLeft);
		// }
		//
		// // margin-bottom
		// String marginRight = ele.getFoMarginRightAttribute();
		// if (StringUtils.isNotEmpty(marginRight)) {
		// cssStyleSheet.setCSSProperty("margin-right", marginRight);
		// }
		//
		// // margin-top
		// String marginTop = ele.getFoMarginTopAttribute();
		// if (StringUtils.isNotEmpty(marginTop)) {
		// cssStyleSheet.setCSSProperty("margin-top", marginTop);
		// }

		// width
		String width = ele.getStyleWidthAttribute();
		if (StringUtils.isNotEmpty(width)) {
			tableProperties.setWidth(ODFUtils.getDimensionAsPoint(width));
		}

		super.visit(ele);
	}

	// visit <style:style style:name="Tableau1.1"
	// style:family="table-row"><style:table-row-properties
	// style:row-height="0.801cm" style:keep-together="true" ...
	@Override
	public void visit(StyleTableRowPropertiesElement ele) {

		StyleTableRowProperties tableRowProperties = currentStyle
				.getTableRowProperties();
		if (tableRowProperties == null) {
			tableRowProperties = new StyleTableRowProperties();
			currentStyle.setTableRowProperties(tableRowProperties);
		}

		// height
		String height = ele.getStyleRowHeightAttribute();
		if (StringUtils.isNotEmpty(height)) {
			tableRowProperties.setRowHeight(ODFUtils
					.getDimensionAsPoint(height));
		}

	}

	// visit <style:style ...
	// style:family="table-cell">/style:table-cell-properties

	@Override
	public void visit(StyleTableCellPropertiesElement ele) {

		StyleTableCellProperties tableCellProperties = currentStyle
				.getTableCellProperties();
		if (tableCellProperties == null) {
			tableCellProperties = new StyleTableCellProperties();
			currentStyle.setTableCellProperties(tableCellProperties);
		}

		// background-color
		String backgroundColor = ele.getFoBackgroundColorAttribute();
		if (StringUtils.isNotEmpty(backgroundColor)) {
			tableCellProperties.setBackgroundColor(ColorRegistry.getInstance()
					.getColor(backgroundColor));
		}

		// border
		String border = ele.getFoBorderAttribute();
		if (StringUtils.isNotEmpty(border)) {
			tableCellProperties.setBorder(new StyleBorder(border,
					BorderType.ALL));
		}
		// border-bottom
		String borderBottom = ele.getFoBorderBottomAttribute();
		if (StringUtils.isNotEmpty(borderBottom)) {
			tableCellProperties.setBorderBottom(new StyleBorder(borderBottom,
					BorderType.BOTTOM));
		}

		// border-left
		String borderLeft = ele.getFoBorderLeftAttribute();
		if (StringUtils.isNotEmpty(borderLeft)) {
			tableCellProperties.setBorderLeft(new StyleBorder(borderBottom,
					BorderType.LEFT));
		}

		// border-bottom
		String borderRight = ele.getFoBorderRightAttribute();
		if (StringUtils.isNotEmpty(borderRight)) {
			tableCellProperties.setBorderRight(new StyleBorder(borderRight,
					BorderType.RIGHT));
		}

		// border-top
		String borderTop = ele.getFoBorderTopAttribute();
		if (StringUtils.isNotEmpty(borderTop)) {
			tableCellProperties.setBorderTop(new StyleBorder(borderTop,
					BorderType.TOP));
		}

		StylePadding stylePadding = null;
		// padding
		String padding = ele.getFoPaddingAttribute();
		if (StringUtils.isNotEmpty(padding)) {
			if (stylePadding == null) {
				stylePadding = new StylePadding();
			}
			stylePadding.setPadding(ODFUtils.getDimensionAsPoint(padding));
		}

		// padding-bottom
		String paddingBottom = ele.getFoPaddingBottomAttribute();
		if (StringUtils.isNotEmpty(paddingBottom)) {
			if (stylePadding == null) {
				stylePadding = new StylePadding();
			}
			stylePadding.setPaddingBottom(ODFUtils
					.getDimensionAsPoint(paddingBottom));
		}

		// padding-left
		String paddingLeft = ele.getFoPaddingLeftAttribute();
		if (StringUtils.isNotEmpty(paddingLeft)) {
			if (stylePadding == null) {
				stylePadding = new StylePadding();
			}
			stylePadding.setPaddingLeft(ODFUtils
					.getDimensionAsPoint(paddingLeft));
		}

		// padding-bottom
		String paddingRight = ele.getFoPaddingRightAttribute();
		if (StringUtils.isNotEmpty(paddingRight)) {
			if (stylePadding == null) {
				stylePadding = new StylePadding();
			}
			stylePadding.setPaddingRight(ODFUtils
					.getDimensionAsPoint(paddingRight));
		}

		// padding-top
		String paddingTop = ele.getFoPaddingTopAttribute();
		if (StringUtils.isNotEmpty(paddingTop)) {
			if (stylePadding == null) {
				stylePadding = new StylePadding();
			}
			stylePadding
					.setPaddingTop(ODFUtils.getDimensionAsPoint(paddingTop));
		}

		if (stylePadding != null) {
			tableCellProperties.setPadding(stylePadding);
		}

		// vertical-align
		String verticalAlign = ele.getStyleVerticalAlignAttribute();
		if (StringUtils.isNotEmpty(verticalAlign)) {
			if (BASELINE.equals(verticalAlign)) {
				tableCellProperties
						.setVerticalAlignment(Element.ALIGN_BASELINE);
			} else if (TOP.equals(verticalAlign)) {
				tableCellProperties.setVerticalAlignment(Element.ALIGN_TOP);
			} else if (MIDDLE.equals(verticalAlign)) {
				tableCellProperties.setVerticalAlignment(Element.ALIGN_MIDDLE);
			} else if (BOTTOM.equals(verticalAlign)) {
				tableCellProperties.setVerticalAlignment(Element.ALIGN_BOTTOM);
			}
		}

		super.visit(ele);
	}

	public void visit(StylePageLayoutPropertiesElement ele) {
		StylePageLayoutProperties pageLayoutProperties = currentStyle
				.getPageLayoutProperties();
		if (pageLayoutProperties == null) {
			pageLayoutProperties = new StylePageLayoutProperties();
			currentStyle.setPageLayoutProperties(pageLayoutProperties);
		}

		// background-color
		String backgroundColor = ele.getFoBackgroundColorAttribute();
		if (StringUtils.isNotEmpty(backgroundColor)) {
			// cssStyleSheet.setCSSProperty("background-color",
			// backgroundColor);
		}

		// border
		String border = ele.getFoBorderAttribute();
		if (StringUtils.isNotEmpty(border)) {
			// cssStyleSheet.setCSSProperty("border", border);
		}

		// border-bottom
		String borderBottom = ele.getFoBorderBottomAttribute();
		if (StringUtils.isNotEmpty(borderBottom)) {
			// cssStyleSheet.setCSSProperty("border-bottom", borderBottom);
		}

		// border-left
		String borderLeft = ele.getFoBorderLeftAttribute();
		if (StringUtils.isNotEmpty(borderLeft)) {
			// cssStyleSheet.setCSSProperty("border-left", borderLeft);
		}

		// border-bottom
		String borderRight = ele.getFoBorderRightAttribute();
		if (StringUtils.isNotEmpty(borderRight)) {
			// cssStyleSheet.setCSSProperty("border-right", borderRight);
		}

		// border-top
		String borderTop = ele.getFoBorderTopAttribute();
		if (StringUtils.isNotEmpty(borderTop)) {
			// cssStyleSheet.setCSSProperty("border-top", borderTop);
		}

		StyleMargin styleMargin = pageLayoutProperties.getMargin();
		if (styleMargin == null) {
			styleMargin = new StyleMargin();
			pageLayoutProperties.setMargin(styleMargin);
		}

		// margin
		String margin = ele.getFoMarginAttribute();
		if (StringUtils.isNotEmpty(margin)) {
			styleMargin.setMargin(ODFUtils.getDimensionAsPoint(margin));
		}

		// margin-bottom
		String marginBottom = ele.getFoMarginBottomAttribute();
		if (StringUtils.isNotEmpty(marginBottom)) {
			styleMargin.setMarginBottom(ODFUtils
					.getDimensionAsPoint(marginBottom));
		}

		// margin-left
		String marginLeft = ele.getFoMarginLeftAttribute();
		if (StringUtils.isNotEmpty(marginLeft)) {
			styleMargin.setMarginLeft(ODFUtils.getDimensionAsPoint(marginLeft));
		}

		// margin-bottom
		String marginRight = ele.getFoMarginRightAttribute();
		if (StringUtils.isNotEmpty(marginRight)) {
			styleMargin.setMarginRight(ODFUtils
					.getDimensionAsPoint(marginRight));
		}

		// margin-top
		String marginTop = ele.getFoMarginTopAttribute();
		if (StringUtils.isNotEmpty(marginTop)) {
			styleMargin.setMarginTop(ODFUtils.getDimensionAsPoint(marginTop));
		}

		// padding
		String padding = ele.getFoPaddingAttribute();
		if (StringUtils.isNotEmpty(padding)) {
			// cssStyleSheet.setCSSProperty("padding", padding);
		}

		// padding-bottom
		String paddingBottom = ele.getFoPaddingBottomAttribute();
		if (StringUtils.isNotEmpty(paddingBottom)) {
			// cssStyleSheet.setCSSProperty("padding-bottom", paddingBottom);
		}

		// padding-left
		String paddingLeft = ele.getFoPaddingLeftAttribute();
		if (StringUtils.isNotEmpty(paddingLeft)) {
			// cssStyleSheet.setCSSProperty("padding-left", paddingLeft);
		}

		// padding-bottom
		String paddingRight = ele.getFoPaddingRightAttribute();
		if (StringUtils.isNotEmpty(paddingRight)) {
			// cssStyleSheet.setCSSProperty("padding-right", paddingRight);
		}

		// padding-top
		String paddingTop = ele.getFoPaddingTopAttribute();
		if (StringUtils.isNotEmpty(paddingTop)) {
			// cssStyleSheet.setCSSProperty("padding-top", paddingTop);
		}

		// height
		String height = ele.getFoPageHeightAttribute();
		if (StringUtils.isNotEmpty(height)) {
			pageLayoutProperties
					.setHeight(ODFUtils.getDimensionAsPoint(height));
		}

		// width
		String width = ele.getFoPageWidthAttribute();
		if (StringUtils.isNotEmpty(width)) {
			pageLayoutProperties.setWidth(ODFUtils.getDimensionAsPoint(width));
		}
		
		// orientation
		String orientation = ele.getStylePrintOrientationAttribute();
		if (StringUtils.isNotEmpty(orientation)) {
			if (LANDSCAPE.equals(orientation)) {
				pageLayoutProperties.setOrientation(PageOrientation.Landscape);	
			} else if (PORTRAIT.equals(orientation)) {
				pageLayoutProperties.setOrientation(PageOrientation.Portrait);	
			}
			
		}
		super.visit(ele);

	}

	public void visit(StyleHeaderFooterPropertiesElement ele) {
		Node parentNode = ele.getParentNode();
		boolean footer = StyleFooterStyleElement.ELEMENT_NAME.getLocalName()
				.equals(parentNode.getLocalName());

		StyleHeaderFooterProperties headerFooterProperties = null;
		if (!footer) {
			headerFooterProperties = currentStyle.getHeaderProperties();
			if (headerFooterProperties == null) {
				headerFooterProperties = new StyleHeaderFooterProperties();
				currentStyle.setHeaderProperties(headerFooterProperties);
			}
		} else {
			headerFooterProperties = currentStyle.getFooterProperties();
			if (headerFooterProperties == null) {
				headerFooterProperties = new StyleHeaderFooterProperties();
				currentStyle.setFooterProperties(headerFooterProperties);
			}
		}

		// min-height
		String minHeight = ele.getFoMinHeightAttribute();
		if (StringUtils.isNotEmpty(minHeight)) {
			headerFooterProperties.setMinHeight(ODFUtils
					.getDimensionAsPoint(minHeight));
		}

	}

	public void visit(TextListStyleElement ele) {
		// visit((OdfElement) ele);
	}

	private String getStyleId(String familyName, String styleName) {
		StringBuilder className = new StringBuilder();
		// style:family
		className.append(familyName);
		// style:name
		if (styleName != null) {
			className.append('_');
			className.append(StringUtils.replaceAll(styleName, ".", "_"));
		}
		return className.toString();
	}

	public Style getStyle(String familyName, String styleName) {
		return stylesMap.get(getStyleId(familyName, styleName));
	}

}
