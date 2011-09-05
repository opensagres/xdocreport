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
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;

import org.odftoolkit.odfdom.converter.ODFConverterException;
import org.odftoolkit.odfdom.converter.internal.ElementVisitorConverter;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.IStylableContainer;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.IStylableElement;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableAnchor;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableChapter;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableChunk;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableDocument;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableHeaderFooter;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableList;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableListItem;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableMasterPage;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableParagraph;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylablePhrase;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableSection;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableTable;
import org.odftoolkit.odfdom.converter.internal.itext.stylable.StylableTableCell;
import org.odftoolkit.odfdom.converter.internal.itext.styles.Style;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleTextProperties;
import org.odftoolkit.odfdom.converter.internal.utils.ODFUtils;
import org.odftoolkit.odfdom.converter.itext.PDFViaITextOptions;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.OdfStylableElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawImageElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.odftoolkit.odfdom.dom.element.style.StyleFooterElement;
import org.odftoolkit.odfdom.dom.element.style.StyleFooterLeftElement;
import org.odftoolkit.odfdom.dom.element.style.StyleHeaderElement;
import org.odftoolkit.odfdom.dom.element.style.StyleHeaderLeftElement;
import org.odftoolkit.odfdom.dom.element.style.StyleMasterPageElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableRowElement;
import org.odftoolkit.odfdom.dom.element.text.TextAElement;
import org.odftoolkit.odfdom.dom.element.text.TextHElement;
import org.odftoolkit.odfdom.dom.element.text.TextLineBreakElement;
import org.odftoolkit.odfdom.dom.element.text.TextListElement;
import org.odftoolkit.odfdom.dom.element.text.TextListItemElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSoftPageBreakElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.odftoolkit.odfdom.dom.element.text.TextTabElement;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;

import fr.opensagres.xdocreport.utils.StringUtils;

public class ElementVisitorForIText extends ElementVisitorConverter {

	private final StyleEngineForIText styleEngine;
	private IStylableContainer currentContainer;

	private StylableMasterPage currentMasterPage;
	private StylableDocument document;
	private StylableMasterPage defaultMasterPage;
	private boolean parseOfficeTextElement = false;
	private Style currentRowStyle;

	
	public ElementVisitorForIText(OdfDocument odfDocument, OutputStream out,
			Writer writer, StyleEngineForIText styleEngine,
			PDFViaITextOptions options) {
		super(odfDocument, out, writer);
		this.styleEngine = styleEngine;
		// Create document
		try {
			document = new StylableDocument(out, styleEngine);
		} catch (DocumentException e) {
			throw new ODFConverterException(e);
		}
	}

	// ---------------------- visit root
	// styles.xml//office:document-styles/office:master-styles/style:master-page

	/**
	 * Generate XHTML page footer + header
	 */
	@Override
	public void visit(StyleMasterPageElement ele) {
		String name = ele.getStyleNameAttribute();
		String pageLayoutName = ele.getStylePageLayoutNameAttribute();
		currentMasterPage = new StylableMasterPage(name, pageLayoutName);
		if (defaultMasterPage == null) {
			defaultMasterPage = currentMasterPage;
		}
		document.addMasterPage(currentMasterPage);
		super.visit(ele);
		currentMasterPage = null;
	}

	// ---------------------- visit
	// styles.xml//office:document-styles/office:master-styles/style:master-page/style-header

	@Override
	public void visit(StyleHeaderElement ele) {
		StylableHeaderFooter header = new StylableHeaderFooter(document, true);
		Style style = document.getStyleMasterPage(currentMasterPage);
		if (style != null) {
			header.applyStyles(style);
		}
		currentMasterPage.setHeader(header);
		StylableTableCell tableCell = header.getTableCell();
		currentContainer = tableCell;
		super.visit(ele);
		header.flush();
		currentContainer = null;
	}

	@Override
	public void visit(StyleHeaderLeftElement ele) {
		// TODO : implement it.
	}
	
	// ---------------------- visit
	// styles.xml//office:document-styles/office:master-styles/style:master-page/style-footer

	@Override
	public void visit(StyleFooterElement ele) {
		StylableHeaderFooter footer = new StylableHeaderFooter(document, false);
		Style style = document.getStyleMasterPage(currentMasterPage);
		if (style != null) {
			footer.applyStyles(style);
		}
		currentMasterPage.setFooter(footer);
		StylableTableCell tableCell = footer.getTableCell();
		currentContainer = tableCell;
		super.visit(ele);
		footer.flush();
		currentContainer = null;

	}

	@Override
	public void visit(StyleFooterLeftElement ele) {
		// TODO : implement it. 
	}
	
	// ---------------------- visit root //office-body/office-text

	@Override
	public void visit(OfficeTextElement ele) {
		this.parseOfficeTextElement  = true;
		currentContainer = document;
		super.visit(ele);
		addCurrentChapterIfNeeded();
		this.parseOfficeTextElement = false;
	}

	// ---------------------- visit //text:h

	@Override
	public void visit(TextHElement ele) {
		int level = ele.getTextOutlineLevelAttribute() != null ? ele
				.getTextOutlineLevelAttribute() : 1;

		// 1) Create title of the chapter
		StylableParagraph title = document.createParagraph((IStylableContainer)null);
		// apply style for the title font, color, bold style...
		applyStyles(ele, title);
		// loop for Text node to add text in the title without add the paragraph
		// to the current container.
		addITextContainer(ele, title, false);

		// 2) Create a chapter or section according level title
		if (level == 1) {
			// It's text:h with level one, create a chapter

			// Create chapter without adding it to the current container.
			// Chapter must be added to the container AFTER it is filled.
			currentContainer = addCurrentChapterIfNeeded();
			StylableChapter chapter = document.createChapter(currentContainer,
					title);
			// apply style for the chapter title border, background color...
			applyStyles(ele, chapter);
			// Current container is chapter
			currentContainer = chapter;
		} else {
			// It's text:h with another level, create a section and add it to
			// the current chapter
			StylableChapter currentChapter = document.getCurrentChapter();

			// Current container is section
			StylableSection section = (StylableSection) currentChapter
					.addSection(title);
			// apply style for the section title border, background color...
			applyStyles(ele, section);
			currentContainer = section;

		}
	}

	/**
	 * This method add the current chapter to the parent container of teh
	 * chapter if current container is chapter.
	 * 
	 * @return parent container of the current chapter otherwise returns current
	 *         container.
	 */
	private IStylableContainer addCurrentChapterIfNeeded() {
		StylableChapter currentChapter = document.getCurrentChapter();
		if (currentChapter != null) {
			IStylableContainer parentContainer = currentChapter.getParent();
			parentContainer.addElement(currentChapter);
			return parentContainer;
		}
		return currentContainer;
	}

	// ---------------------- visit //text:p

	@Override
	public void visit(TextPElement ele) {
		StylableParagraph paragraph = document
				.createParagraph(currentContainer);
		applyStyles(ele, paragraph);		
		if (ele.getFirstChild() == null) {
			// no content in the paragraph
			// ex : <text:p text:style-name="Standard"></text:p>
			// add blank Chunk
			paragraph.add(Chunk.NEWLINE);
		}		
		addITextContainer(ele, paragraph);		
		
	}

	// ---------------------- visit //text:tab

	@Override
	public void visit(TextTabElement ele) {
		//ele.getParentNode();
		//System.err.println(ele);
	}
	// ---------------------- visit //text:span

	@Override
	public void visit(TextSpanElement ele) {
		StylablePhrase phrase = document.createPhrase(currentContainer);
		applyStyles(ele, phrase);
		if (ele.getFirstChild() == null) {
			// no content in the paragraph
			// ex : <text:span text:style-name="Standard"></text:span>
			// add blank Chunk
			phrase.add(Chunk.NEWLINE);
		}
		addITextContainer(ele, phrase);
	}

	// ---------------------- visit //text:a

	@Override
	public void visit(TextAElement ele) {
		StylableAnchor anchor = document.createAnchor(currentContainer);
		String reference = ele.getXlinkHrefAttribute();		
		applyStyles(ele, anchor);
		
		if (anchor.getFont().getColor() == null) {
			// if no color was applied to the link
			// get the font of the paragraph and set blue color.
			Font linkFont = anchor.getFont();
			Style style = currentContainer.getLastStyleApplied();
			if (style != null) {
				StyleTextProperties textProperties = style.getTextProperties();
				if (textProperties != null) {
					Font font = textProperties.getFont();
					if (font != null) {
						linkFont = new Font(font);
						anchor.setFont(linkFont);
					}
				}
			}
			//Color blueColor = ColorRegistryForIText.getInstance().getColor("#0000CC");
			linkFont.setColor(Color.BLUE);
		}
		
		// TODO ; manage internal link
		// set the link 
		anchor.setReference(reference);
		// Add to current container.
		addITextContainer(ele, anchor);
	}

	// ---------------------- visit table:table (ex : <table:table
	// table:name="Tableau1" table:style-name="Tableau1">)

	@Override
	public void visit(TableTableElement ele) {
		float[] columnWidth = ODFUtils.getColumnWidths(ele, odfDocument);
		StylableTable table = document.createTable(currentContainer,
				columnWidth.length);
		try {
			table.setTotalWidth(columnWidth);
		} catch (DocumentException e) {
			// Do nothing
		}
		applyStyles(ele, table);
		addITextContainer(ele, table);
	}

	// ---------------------- visit table:table-row

	@Override
	public void visit(TableTableRowElement ele) {
		currentRowStyle = getStyle(ele);
		super.visit(ele);
		currentRowStyle = null;
	}

	// ---------------------- visit table:table-cell

	@Override
	public void visit(TableTableCellElement ele) {
		StylableTableCell tableCell = document
				.createTableCell(currentContainer);

		// table:number-columns-spanned
		Integer colSpan = ele.getTableNumberColumnsSpannedAttribute();
		if (colSpan != null) {
			tableCell.setColspan(colSpan);
		}
		// table:number-rows-spanned
		Integer rowSpan = ele.getTableNumberRowsSpannedAttribute();
		if (rowSpan != null) {
			tableCell.setRowspan(rowSpan);
			
		}
		// Apply styles coming from table-row
		if (currentRowStyle != null) {
			tableCell.applyStyles(currentRowStyle);
		}
		// Apply styles coming from table-cell
		applyStyles(ele, tableCell);
		addITextContainer(ele, tableCell);
	}

	// ---------------------- visit text:list

	@Override
	public void visit(TextListElement ele) {
		// int level = 1;
		StylableList list = document.createList(currentContainer);
		// applyStyles(ele, list);
		addITextContainer(ele, list);
	}

	// ---------------------- visit text:listitem

	@Override
	public void visit(TextListItemElement ele) {
		// int level = 1;
		StylableListItem listItem = document.createListItem(currentContainer);
		// applyStyles(ele, list);
		addITextContainer(ele, listItem);

	}

	// ---------------------- visit draw:image

	@Override
	public void visit(DrawImageElement ele) {
		String href = ele.getXlinkHrefAttribute();
		if (StringUtils.isNotEmpty(href)) {
			byte[] imageStream = odfDocument.getPackage().getBytes(href);
			if (imageStream != null) {
				try {
					Image image = Image.getInstance(imageStream);
					if (image != null) {
						String x = null;
						String y = null;
						String width = null;
						String height = null;
						// set width, height....image
						Node parentNode = ele.getParentNode();
						if (parentNode instanceof DrawFrameElement) {
							DrawFrameElement frame = (DrawFrameElement) parentNode;
							x = frame.getSvgXAttribute();
							y = frame.getSvgYAttribute();
							width = frame.getSvgWidthAttribute();
							height = frame.getSvgHeightAttribute();
						}
						if (StringUtils.isNotEmpty(x)
								&& StringUtils.isNotEmpty(y)) {
							image.setAbsolutePosition(ODFUtils.getDimensionAsPoint(x),
									ODFUtils.getDimensionAsPoint(y));
						}
						if (StringUtils.isNotEmpty(width)) {
							image.scaleAbsoluteWidth(ODFUtils
									.getDimensionAsPoint(width));
						}
						if (StringUtils.isNotEmpty(height)) {
							image.scaleAbsoluteHeight(ODFUtils
									.getDimensionAsPoint(height));
						}
						IStylableContainer parent = currentContainer
								.getParent();
						if (parent instanceof PdfPCell) {
							// When image is included into a Table Cell, we must
							// use PdfPCell#setImage
							// otherwise the image will not appear. Why???
							((PdfPCell) parent).setImage(image);
						} else {
							currentContainer.addElement(image);
						}
					}
				} catch (BadElementException e) {
					// TODO : display log
				} catch (MalformedURLException e) {
					// TODO : display log
				} catch (IOException e) {
					// TODO : display log
				}
			}
		}
	}

	// ---------------------- visit //text:soft-page-break

	@Override
	public void visit(TextSoftPageBreakElement ele) {
		document.newPage();
	}
	
	// ---------------------- visit text:line-break

	@Override
	public void visit(TextLineBreakElement ele) {
		currentContainer.addElement(Chunk.NEWLINE);
	}
	
	@Override
	protected void processTextNode(Text node) {
		createChunk(node);
	}

	private Chunk createChunk(Text node) {
		String textContent = node.getTextContent();
		StylableChunk chunk = document.createChunk(currentContainer,
				textContent);
		Style style = currentContainer.getLastStyleApplied();
		if (style != null) {
			chunk.applyStyles(style);
		}
		addITextElement(chunk);
		return chunk;
	}

	@Override
	public void save() throws IOException {
		if (document != null) {
			document.close();
		}
		super.save();
	}

	private void addITextContainer(OdfElement ele,
			IStylableContainer newContainer) {
		addITextContainer(ele, newContainer, true);
	}

	private void addITextContainer(OdfElement ele,
			IStylableContainer newContainer, boolean add) {
		IStylableContainer oldContainer = currentContainer;
		try {
			currentContainer = newContainer;
			super.visit(ele);
			if (add) {
				oldContainer.addElement(newContainer.getElement());
			}
		} finally {
			currentContainer = oldContainer;
		}
	}

	private void addITextElement(Element element) {
		currentContainer.addElement(element);
	}

	private StylableMasterPage applyStyles(OdfStylableElement ele, IStylableElement element) {
		StylableMasterPage newMasterPage = null;
		Style style = getStyle(ele);
		if (style != null) {
			if (parseOfficeTextElement) {				
				String masterPageName = style.getMasterPageName();
				if (StringUtils.isEmpty(masterPageName)) {
					if (!defaultMasterPage.equals(currentMasterPage)) {
						// Add page
						newMasterPage = defaultMasterPage;
					}
				} else {
					StylableMasterPage masterPage = document
							.getMasterPage(masterPageName);
					if (masterPage != null
							&& !masterPage.equals(currentMasterPage)) {
						newMasterPage = masterPage;
					}
				}
			}
			if (newMasterPage != null) {
				currentMasterPage = newMasterPage;
				document.setActiveMasterPage(newMasterPage);
			}
			element.applyStyles(style);
		}
		return newMasterPage;
	}

	private Style getStyle(OdfStylableElement ele) {
		String styleName = ele.getStyleName();
		String familyName = ele.getStyleFamily() != null ? ele.getStyleFamily()
				.getName() : null;

		Style style = styleEngine.getStyle(familyName, styleName);
		return style;
	}

}
