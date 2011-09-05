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

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * Table row buffered region.
 * 
 */
public class RowBufferedRegion extends BufferedRegion {

	private String itemNameList;
	private String startLoopDirective;
	private String endLoopDirective;

	public RowBufferedRegion(IBufferedRegion parent) {
		super(parent);
	}

	public void setLoopTemplateDirective(String startLoopDirective,
			String endLoopDirective) {
		this.startLoopDirective = startLoopDirective;
		this.endLoopDirective = endLoopDirective;
	}

	public boolean isLoopTemplateDirectiveInitilalized() {
		return StringUtils.isNotEmpty(startLoopDirective)
				&& StringUtils.isNotEmpty(endLoopDirective);
	}

	public void initializeLoopTemplateDirective(String itemNameList,
			IDocumentFormatter formatter) {
		this.itemNameList = itemNameList;
		this.startLoopDirective = formatter.getStartLoopDirective(itemNameList);
		this.endLoopDirective = formatter.getEndLoopDirective(itemNameList);
	}

	public String getItemNameList() {
		return itemNameList;
	}

	public String getStartLoopDirective() {
		return startLoopDirective;
	}

	public String getEndLoopDirective() {
		return endLoopDirective;
	}

	@Override
	public void save(Writer writer) throws IOException {
		if (StringUtils.isNotEmpty(startLoopDirective)) {
			writer.write(startLoopDirective);
		}
		super.save(writer);
		if (StringUtils.isNotEmpty(endLoopDirective)) {
			writer.write(endLoopDirective);
		}
	}

}
