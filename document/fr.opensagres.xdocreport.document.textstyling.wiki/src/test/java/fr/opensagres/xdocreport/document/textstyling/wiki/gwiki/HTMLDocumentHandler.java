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
package fr.opensagres.xdocreport.document.textstyling.wiki.gwiki;

import fr.opensagres.xdocreport.document.textstyling.AbstractDocumentHandler;

/**
 * 
 * Basic Document handler implementation to build html fragment content.
 * 
 */
public class HTMLDocumentHandler extends AbstractDocumentHandler {

	public HTMLDocumentHandler() {
		super(null, null);
	}

	public void startDocument() {
		// writer.write("<html>");
		// writer.write("<body>");
	}

	public void endDocument() {
		// writer.write("</body>");
		// writer.write("</html>");
	}

	public void startBold() {
		writer.write("<strong>");
	}

	public void endBold() {
		writer.write("</strong>");
	}

	public void startItalics() {
		writer.write("<i>");
	}

	public void endItalics() {
		writer.write("</i>");
	}

	public void startListItem() {
		writer.write("<li>");
	}

	public void endListItem() {
		writer.write("</li>");
	}

	@Override
	protected void doStartOrderedList() {
		writer.write("<ol>");
	}

	@Override
	protected void doEndOrderedList() {
		writer.write("</ol>");
	}

	@Override
	protected void doStartUnorderedList() {
		writer.write("<ul>");
	}

	@Override
	protected void doEndUnorderedList() {
		writer.write("</ul>");
	}

	public void startParagraph() {
		writer.write("<p>");
	}

	public void endParagraph() {
		writer.write("</p>");
	}

	public void startHeading(int level) {
		writer.write("<h");
		writer.write(level);
		writer.write(">");

	}

	public void endHeading(int level) {
		writer.write("</h");
		writer.write(level);
		writer.write(">");
	}

}
