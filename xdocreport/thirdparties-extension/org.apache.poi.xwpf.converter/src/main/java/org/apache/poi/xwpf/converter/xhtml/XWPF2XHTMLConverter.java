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
package org.apache.poi.xwpf.converter.xhtml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.poi.xwpf.converter.IURIResolver;
import org.apache.poi.xwpf.converter.IXWPFConverter;
import org.apache.poi.xwpf.converter.XWPFConverterException;
import org.apache.poi.xwpf.converter.internal.AbstractXWPFConverter;
import org.apache.poi.xwpf.converter.internal.xhtml.XHTMLMapper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class XWPF2XHTMLConverter extends AbstractXWPFConverter<XHTMLOptions> {

	private static final IXWPFConverter<XHTMLOptions> INSTANCE = new XWPF2XHTMLConverter();

	public static IXWPFConverter<XHTMLOptions> getInstance() {
		return INSTANCE;
	}

	@Override
	protected void doConvert(XWPFDocument document, OutputStream out,
			Writer writer, XHTMLOptions options) throws XWPFConverterException,
			IOException {

		int indent = 0;
		IURIResolver resolver = IURIResolver.DEFAULT;
		if (options != null) {
			indent = options.getIndent();
			if (options.getURIResolver() != null) {
				resolver = options.getURIResolver();
			}
		}
		XHTMLMapper mapper = new XHTMLMapper(document, indent, resolver);
		try {
			mapper.visit(out);
		} catch (Exception e) {
			throw new XWPFConverterException(e);
		}

	}

}