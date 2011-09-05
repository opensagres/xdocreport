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
package org.odftoolkit.odfdom.converter.internal.itext.stylable;

import java.awt.Color;

import org.odftoolkit.odfdom.converter.internal.itext.styles.Style;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleBorder;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleParagraphProperties;
import org.odftoolkit.odfdom.converter.internal.utils.StyleUtils;

import com.lowagie.text.Element;

import fr.opensagres.xdocreport.itext.extension.ExtendedSection;
import fr.opensagres.xdocreport.itext.extension.IITextContainer;
import fr.opensagres.xdocreport.itext.extension.IParagraphFactory;

public class StylableSection extends ExtendedSection implements
		IStylableContainer {

	private static final long serialVersionUID = 664309269352903329L;

	private final StylableDocument ownerDocument;
	private final IStylableContainer parent;
	private Style lastStyleApplied = null;

	public StylableSection(StylableDocument ownerDocument,
			IStylableContainer parent, StylableParagraph title, int numberDepth) {
		super(title, numberDepth);
		this.ownerDocument = ownerDocument;
		this.parent = parent;
	}

	public void addElement(Element element) {
		super.add(element);
	}

	public void applyStyles(Style style) {
		this.lastStyleApplied = style;
		StyleParagraphProperties paragraphProperties = style
				.getParagraphProperties();
		if (paragraphProperties != null) {

			Color backgroundColor = paragraphProperties.getBackgroundColor();
			if (backgroundColor != null) {
				super.getPdfPCell().setBackgroundColor(backgroundColor);
			}
			
			// border
			StyleBorder border = paragraphProperties.getBorder();
			StyleUtils.applyStyles(border, this.getPdfPCell());

			// border-top
			StyleBorder borderTop = paragraphProperties.getBorderTop();
			StyleUtils.applyStyles(borderTop, this.getPdfPCell());

			// border-bottom
			StyleBorder borderBottom = paragraphProperties.getBorderBottom();
			StyleUtils.applyStyles(borderBottom, this.getPdfPCell());
			
			// border-left
			StyleBorder borderLeft = paragraphProperties.getBorderLeft();
			StyleUtils.applyStyles(borderLeft, this.getPdfPCell());

			// border-right
			StyleBorder borderRight = paragraphProperties.getBorderRight();
			StyleUtils.applyStyles(borderRight, this.getPdfPCell());
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

	@Override
	protected IParagraphFactory getParagraphFactory() {
		return ownerDocument;
	}
	
	public IITextContainer getITextContainer() {
		return parent;
	}

	public void setITextContainer(IITextContainer container) {
		
	}
}
