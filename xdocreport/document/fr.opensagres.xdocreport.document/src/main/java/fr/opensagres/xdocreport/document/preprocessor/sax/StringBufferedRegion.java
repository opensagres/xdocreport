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
package fr.opensagres.xdocreport.document.preprocessor.sax;

import java.io.IOException;
import java.io.Writer;

/**
 * String buffered region.
 * 
 */
public class StringBufferedRegion implements IBufferedRegion {

	private final StringBuilder buffer = new StringBuilder();
	private final IBufferedRegion parent;

	public StringBufferedRegion(IBufferedRegion parent) {
		this.parent = parent;
		parent.addRegion(this);
	}

	public boolean isString() {
		return true;
	}

	public void save(Writer writer) throws IOException {
		writer.write(buffer.toString());
	}

	public void append(String content) {
		buffer.append(content);
	}

	public void append(char[] ch, int start, int length) {
		buffer.append(ch, start, length);
	}

	public void append(char c) {
		buffer.append(c);
	}

	public void addRegion(IBufferedRegion region) {
		// Do noting
	}

	@Override
	public String toString() {
		return buffer.toString();
	}

	public void clear() {
		buffer.setLength(0);
	}

	public IBufferedRegion getParent() {
		return parent;
	}
}
