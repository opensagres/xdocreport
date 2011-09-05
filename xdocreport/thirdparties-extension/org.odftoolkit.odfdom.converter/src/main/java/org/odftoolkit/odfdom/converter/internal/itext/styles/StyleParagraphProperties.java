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
package org.odftoolkit.odfdom.converter.internal.itext.styles;

import java.awt.Color;

import com.lowagie.text.Element;

public class StyleParagraphProperties {

	private StyleBorder border;
	private StyleBorder borderTop;
	private StyleBorder borderBottom;
	private StyleBorder borderLeft;
	private StyleBorder borderRight;
	private Color backgroundColor;
	private int alignment = Element.ALIGN_UNDEFINED;
	private Float indentation;
	private Float lineHeight;
	private boolean autoTextIndent = false;
	private boolean breakBeforePage = false;
	private boolean breakAfterPage = false;

	public StyleParagraphProperties() {

	}

	public StyleParagraphProperties(StyleParagraphProperties paragraphProperties) {
		if (paragraphProperties == null) {
			return;
		}
		backgroundColor = paragraphProperties.backgroundColor;
		alignment = paragraphProperties.alignment;
		indentation = paragraphProperties.indentation;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getAlignment() {
		return alignment;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	public Float getIndentation() {
		return indentation;
	}

	public void setIndentation(Float indentation) {
		this.indentation = indentation;
	}

	public Float getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(Float lineHeight) {
		this.lineHeight = lineHeight;
	}

	public void merge(StyleParagraphProperties paragraphProperties) {
		if (paragraphProperties.getBackgroundColor() != null) {
			backgroundColor = paragraphProperties.getBackgroundColor();
		}
		if (paragraphProperties.getAlignment() != Element.ALIGN_UNDEFINED) {
			alignment = paragraphProperties.getAlignment();
		}
		if (paragraphProperties.getIndentation() != null) {
			indentation = paragraphProperties.getIndentation();
		}
		if (paragraphProperties.getLineHeight() != null) {
			lineHeight = paragraphProperties.getLineHeight();
		}
		if (paragraphProperties.getBorder() != null) {
			border = paragraphProperties.getBorder();
		}
		if (paragraphProperties.getBorderBottom() != null) {
			borderBottom = paragraphProperties.getBorderBottom();
		}
		if (paragraphProperties.getBorderLeft() != null) {
			borderLeft = paragraphProperties.getBorderLeft();
		}
		if (paragraphProperties.getBorderRight() != null) {
			borderRight = paragraphProperties.getBorderRight();
		}
		if (paragraphProperties.getBorderTop() != null) {
			borderTop = paragraphProperties.getBorderTop();
		}
		if (paragraphProperties.isAutoTextIndent() != autoTextIndent) {
			autoTextIndent = paragraphProperties.isAutoTextIndent();
		}
	}

	public StyleBorder getBorder() {
		return border;
	}

	public void setBorder(StyleBorder border) {
		this.border = border;
	}

	public StyleBorder getBorderTop() {
		return borderTop;
	}

	public void setBorderTop(StyleBorder borderTop) {
		this.borderTop = borderTop;
	}

	public StyleBorder getBorderBottom() {
		return borderBottom;
	}

	public void setBorderBottom(StyleBorder borderBottom) {
		this.borderBottom = borderBottom;
	}

	public StyleBorder getBorderLeft() {
		return borderLeft;
	}

	public void setBorderLeft(StyleBorder borderLeft) {
		this.borderLeft = borderLeft;
	}

	public StyleBorder getBorderRight() {
		return borderRight;
	}

	public void setBorderRight(StyleBorder borderRight) {
		this.borderRight = borderRight;
	}

	public void setAutoTextIndent(boolean autoTextIndent) {
		this.autoTextIndent = autoTextIndent;
	}

	public boolean isAutoTextIndent() {
		return autoTextIndent;
	}

	public void setBreakAfterPage(boolean breakAfterPage) {
		this.breakAfterPage = breakAfterPage;
	}

	public boolean isBreakAfterPage() {
		return breakAfterPage;
	}

	public void setBreakBeforePage(boolean breakBeforePage) {
		this.breakBeforePage = breakBeforePage;
	}

	public boolean isBreakBeforePage() {
		return breakBeforePage;
	}

}
