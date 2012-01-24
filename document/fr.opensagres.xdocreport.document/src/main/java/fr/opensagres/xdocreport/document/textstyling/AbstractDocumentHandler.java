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
package fr.opensagres.xdocreport.document.textstyling;

import java.io.StringWriter;
import java.util.Stack;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.template.IContext;

/**
 * Abstract class for document handler {@link IDocumentHandler}.
 * 
 */
public abstract class AbstractDocumentHandler implements IDocumentHandler {

	private final BufferedElement parent;
	private final IContext context;
	protected final StringWriter writer;
	private final Stack<Boolean> listStack;

	public AbstractDocumentHandler(BufferedElement parent, IContext context) {
		this.parent = parent;
		this.context = context;
		this.writer = new StringWriter();
		// Stack of boolean (ordered or not) for the ordered/unordered list
		this.listStack = new Stack<Boolean>();
	}

	public void handleString(String s) {
		writer.write(s);
	}

	@Override
	public String toString() {
		return writer.toString();
	}

	public final void startOrderedList() {
		listStack.push(true);
		doStartOrderedList();
	}

	public final void endOrderedList() {
		listStack.pop();
		doEndOrderedList();
	}

	public final void startUnorderedList() {
		listStack.push(false);
		doStartUnorderedList();
	}

	public final void endUnorderedList() {
		listStack.pop();
		doEndUnorderedList();
	}

	protected boolean getCurrentListOrder() {
		if (listStack.isEmpty()) {
			return false;
		}
		return listStack.peek();
	}

	protected int getCurrentListIndex() {
		if (listStack.isEmpty()) {
			return 0;
		}
		return listStack.size() - 1;
	}

	protected void doEndUnorderedList() {

	}

	protected void doEndOrderedList() {

	}

	protected void doStartUnorderedList() {

	}

	protected void doStartOrderedList() {

	}
}
