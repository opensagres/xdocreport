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
 * Interface for buffered region.
 * 
 */
public interface IBufferedRegion {

	/**
	 * Write the content of the buffer in the given writer.
	 * 
	 * @param writer
	 * @throws IOException
	 */
	void save(Writer writer) throws IOException;

	/**
	 * Returns true if buffered region is a String and false otherwise.
	 * 
	 * @return
	 */
	boolean isString();

	/**
	 * Append the given content to the buffer.
	 * 
	 * @param content
	 */
	void append(String content);

	/**
	 * Append the given char array to the buffer.
	 * 
	 * @param ch
	 * @param start
	 * @param length
	 */
	void append(char[] ch, int start, int length);

	/**
	 * Append the given content to the buffer.
	 * 
	 * @param content
	 */
	void append(char c);

	/**
	 * Add a the given buffered region to the buffer.
	 * 
	 * @param region
	 */
	void addRegion(IBufferedRegion region);

	/**
	 * Returns the parent buffered region.
	 * 
	 * @return
	 */
	IBufferedRegion getParent();
}
