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
package fr.opensagres.xdocreport.template.formatter;

public abstract class AbstractDocumentFormatter implements IDocumentFormatter {

	private String startDocumentDirective;
	private String endDocumentDirective;

	public String extractItemNameList(String content, String fieldName, boolean forceAsField) {
		if (!forceAsField && !isModelField(content, fieldName)) {
			return null;
		}
		int dotIndex = fieldName.indexOf('.');
		if (dotIndex != -1) {
			return getItemToken() + fieldName.substring(0, dotIndex);
		}
		return getItemToken() + fieldName;
	}

	public String getStartDocumentDirective() {
		return startDocumentDirective;
	}

	public void setStartDocumentDirective(String startDocumentDirective) {
		this.startDocumentDirective = startDocumentDirective;
	}

	public String getEndDocumentDirective() {
		return endDocumentDirective;
	}

	public void setEndDocumentDirective(String endDocumentDirective) {
		this.endDocumentDirective = endDocumentDirective;
	}

	public String getStartLoopDirective(String itemNameList) {
		return getStartLoopDirective(itemNameList,
				itemNameList.substring(getItemToken().length()));
	}
	
	protected abstract String getItemToken();

	protected abstract boolean isModelField(String content, String fieldName);
}
