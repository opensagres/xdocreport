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
package org.apache.poi.xwpf.converter.internal.itext.stylable;


import java.awt.Color;

import org.apache.poi.xwpf.converter.internal.itext.styles.Style;
import org.apache.poi.xwpf.converter.internal.itext.styles.StyleBorder;
import org.apache.poi.xwpf.converter.internal.itext.styles.StylePadding;
import org.apache.poi.xwpf.converter.internal.itext.styles.StyleTableCellProperties;
import org.apache.poi.xwpf.converter.internal.itext.styles.StyleTableRowProperties;

import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;

import fr.opensagres.xdocreport.itext.extension.IITextContainer;


public class StylableTableCell extends PdfPCell implements IStylableContainer {

	private static final long serialVersionUID = 664309269352903329L;

	private final IStylableFactory ownerDocument;
	private final IStylableContainer parent;
	private Style lastStyleApplied = null;

	public StylableTableCell(IStylableFactory ownerDocument,
			IStylableContainer parent) {
		this.ownerDocument = ownerDocument;
		this.parent = parent;
	}

	public void addElement(Element element) {
		super.addElement(element);
	}

	public void applyStyles(Object ele,Style style) {
		this.lastStyleApplied = style;

		StyleTableRowProperties tableRowProperties = style
				.getTableRowProperties();
		if (tableRowProperties != null) {
			Float rowHeight = tableRowProperties.getRowHeight();
			if (rowHeight != null) {
				super.setFixedHeight(rowHeight);
			}
		}
		StyleTableCellProperties tableCellProperties = style
				.getTableCellProperties();
		if (tableCellProperties != null) {

			// background-color
			Color backgroundColor = tableCellProperties.getBackgroundColor();
			if (backgroundColor != null) {
				super.setBackgroundColor(backgroundColor);
			}

			// border
			StyleBorder border = tableCellProperties.getBorder();
			StyleUtils.applyStyles(border, this);

			// border-top
			StyleBorder borderTop = tableCellProperties.getBorderTop();
			StyleUtils.applyStyles(borderTop, this);

			// border-bottom
			StyleBorder borderBottom = tableCellProperties.getBorderBottom();
			StyleUtils.applyStyles(borderBottom, this);
			
			// border-left
			StyleBorder borderLeft = tableCellProperties.getBorderLeft();
			StyleUtils.applyStyles(borderLeft, this);

			// border-right
			StyleBorder borderRight = tableCellProperties.getBorderRight();
			StyleUtils.applyStyles(borderRight, this);

			// padding
			StylePadding padding = tableCellProperties.getPadding();
			if (padding != null) {

				// padding
				if (padding.getPadding() != null) {
					super.setPadding(padding.getPadding());
				}

				// padding-top
				if (padding.getPaddingTop() != null) {
					super.setPaddingTop(padding.getPaddingTop());
				}

				// padding-bottom
				if (padding.getPaddingBottom() != null) {
					super.setPaddingBottom(padding.getPaddingBottom());
				}

				// padding-right
				if (padding.getPaddingRight() != null) {
					super.setPaddingRight(padding.getPaddingRight());
				}

				// padding-left
				if (padding.getPaddingLeft() != null) {
					super.setPaddingLeft(padding.getPaddingLeft());
				}
			}

			// Alignment
			int verticalAlignment = tableCellProperties.getVerticalAlignment();
			if (verticalAlignment != Element.ALIGN_UNDEFINED) {
				super.setVerticalAlignment(verticalAlignment);
			}

		}
	}

	public Style getLastStyleApplied() {
		return lastStyleApplied;
	}

	public IStylableContainer getParent() {
		return parent;
	}

	public Element getElement() {
		return this;
	}
	
	public IITextContainer getITextContainer() {
		return parent;
	}

	public void setITextContainer(IITextContainer container) {
		
	}
}

