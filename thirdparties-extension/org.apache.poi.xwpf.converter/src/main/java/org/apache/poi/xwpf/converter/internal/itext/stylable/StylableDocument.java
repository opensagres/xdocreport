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

import java.io.OutputStream;

import org.apache.poi.xwpf.converter.internal.itext.StyleEngineForIText;
import org.apache.poi.xwpf.converter.internal.itext.styles.Style;
import org.apache.poi.xwpf.converter.internal.itext.styles.StyleMargin;
import org.apache.poi.xwpf.converter.internal.itext.styles.StylePageLayoutProperties;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;

import fr.opensagres.xdocreport.itext.extension.ExtendedDocument;
import fr.opensagres.xdocreport.itext.extension.IParagraphFactory;


public class StylableDocument extends ExtendedDocument implements
		IStylableContainer, IStylableFactory, IParagraphFactory {

	private final StyleEngineForIText styleEngine;
	private Style lastStyleApplied = null;

	private int titleNumber = 1;
	private StylableChapter currentChapter;

	public StylableDocument(OutputStream out, StyleEngineForIText styleEngine)
			throws DocumentException {
		super(out);
		this.styleEngine = styleEngine;
	}

	public void addElement(Element element) {
		try {
			if (!super.isOpen()) {
				super.open();
			}
			super.add(element);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public StylableParagraph createParagraph(IStylableContainer parent) {
		return new StylableParagraph(this, parent);
	}

	public Paragraph createParagraph() {
		return createParagraph((IStylableContainer) null);
	}

	public Paragraph createParagraph(Paragraph title) {
		return new StylableParagraph(this, title, null);
	}

	public StylablePhrase createPhrase(IStylableContainer parent) {
		return new StylablePhrase(this, parent);
	}

	public StylableAnchor createAnchor(IStylableContainer parent) {
		return new StylableAnchor(this, parent);
	}

	public StylableList createList(IStylableContainer parent) {
		return new StylableList(this, parent);
	}

	public StylableListItem createListItem(IStylableContainer parent) {
		return new StylableListItem(this, parent);
	}

	public StylableTable createTable(IStylableContainer parent, int numColumns) {
		return new StylableTable(this, parent, numColumns);
	}

	public StylableTableCell createTableCell(IStylableContainer parent) {
		return new StylableTableCell(this, parent);
	}

	public StylableChapter createChapter(IStylableContainer parent,
			StylableParagraph title) {
		currentChapter = new StylableChapter(this, parent, title, titleNumber++);
		return currentChapter;
	}

	public StylableChunk createChunk(IStylableContainer parent,
			String textContent) {
		return new StylableChunk(this, parent, textContent);
	}

	public StylableSection createSection(IStylableContainer parent,
			StylableParagraph title, int numberDepth) {
		return new StylableSection(this, parent, title, numberDepth);
	}

	public StylableChapter getCurrentChapter() {
		return currentChapter;
	}

	public StyleEngineForIText getStyleEngine() {
		return styleEngine;
	}

	public Style getLastStyleApplied() {
		return lastStyleApplied;
	}

	public IStylableContainer getParent() {
		return null;
	}


	public void applyStyles(Object ele,Style style) {
		this.lastStyleApplied = style;

		StylePageLayoutProperties pageLayoutProperties = style
				.getPageLayoutProperties();
		if (pageLayoutProperties != null) {

			// width/height
			Float width = pageLayoutProperties.getWidth();
			Float height = pageLayoutProperties.getHeight();
			if (width != null && height != null) {
				super.setPageSize(new Rectangle(width, height));
			}

			// margin
			StyleMargin margin = pageLayoutProperties.getMargin();
			if (margin != null) {

				if (margin.getMarginTop() != null) {
					originMarginTop = margin.getMarginTop();
				}
				if (margin.getMarginBottom() != null) {
					originMarginBottom = margin.getMarginBottom();
				}
				if (margin.getMarginRight() != null) {
					originMarginRight = margin.getMarginRight();
				}
				if (margin.getMarginLeft() != null) {
					originMarginLeft = margin.getMarginLeft();
				}
				// if (defaultMasterPage != null) {
				// StylableHeaderFooter header = defaultMasterPage
				// .getHeader();
				// if (header != null) {
				// marginTop += header.getTotalHeight();
				// }
				// StylableHeaderFooter footer = defaultMasterPage
				// .getHeader();
				// if (footer != null) {
				// marginBottom += footer.getTotalHeight();
				// }
				// }

				super.setMargins(originMarginLeft, originMarginRight,
						originMarginTop, originMarginBottom);
			}

		}

	}

	public Element getElement() {
		return null;
	}
	
//	@Override
//	public StylableMasterPage getMasterPage(String masterPageName) {
//		return (StylableMasterPage)super.getMasterPage(masterPageName);
//	}
}

