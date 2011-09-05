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


import org.apache.poi.xwpf.converter.internal.itext.styles.Style;
import org.apache.poi.xwpf.converter.internal.itext.styles.StyleTableProperties;

import com.lowagie.text.Element;

import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPTable;

public class StylableTable extends ExtendedPdfPTable implements IStylableContainer {

	private static final long serialVersionUID = 664309269352903329L;

	private final StylableDocument ownerDocument;
	private IStylableContainer parent;
	private Style lastStyleApplied = null;

	public StylableTable(StylableDocument ownerDocument,
			IStylableContainer parent, int numColumns) {
		super(numColumns);
		this.ownerDocument = ownerDocument;
		this.parent = parent;
	}

	public void applyStyles(Object ele,Style style) {
		this.lastStyleApplied = style;

		StyleTableProperties tableProperties = style.getTableProperties();
		if (tableProperties != null) {
			if (tableProperties.getWidth() != null) {
				super.setTotalWidth(tableProperties.getWidth());
				super.setLockedWidth(true);
			}
		}
	}

	public Style getLastStyleApplied() {
		return lastStyleApplied;
	}

	public IStylableContainer getParent() {
		return parent;
	}

	public StylableDocument getOwnerDocument() {
		return ownerDocument;
	}

	public Element getElement() {
		return this;
	}
}
