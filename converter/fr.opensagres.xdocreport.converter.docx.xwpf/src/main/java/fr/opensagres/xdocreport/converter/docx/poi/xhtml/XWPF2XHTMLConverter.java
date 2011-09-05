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
package fr.opensagres.xdocreport.converter.docx.poi.xhtml;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import fr.opensagres.xdocreport.converter.IURIResolver;
import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.MimeMappingConstants;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.converter.docx.poi.itext.XWPF2PDFViaITextConverter;
import fr.opensagres.xdocreport.converter.internal.AbstractConverterNoEntriesSupport;
import fr.opensagres.xdocreport.core.logging.LogUtils;

public class XWPF2XHTMLConverter  extends AbstractConverterNoEntriesSupport
		implements MimeMappingConstants {

	private static final XWPF2XHTMLConverter INSTANCE = new XWPF2XHTMLConverter();
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LogUtils.getLogger(XWPF2PDFViaITextConverter.class.getName());


	public static XWPF2XHTMLConverter getInstance() {
		return INSTANCE;
	}

	public void convert(InputStream in, OutputStream out, Options options)
			throws XDocConverterException {
		try {
			XWPFDocument document = new XWPFDocument(in);
			org.apache.poi.xwpf.converter.xhtml.XWPF2XHTMLConverter
					.getInstance().convert(document, out, toXWPFOptions(options));

		} catch (Exception e) {
			LOGGER.severe(e.getMessage());
			throw new XDocConverterException(e);
		}
	}

	

	private XHTMLOptions toXWPFOptions(Options o) {
		XHTMLOptions options = new XHTMLOptions();
		final IURIResolver resolver = (IURIResolver) o
				.getProperty(IURIResolver.class.getName());
		if (resolver != null) {
			
			options.setURIResolver(new org.apache.poi.xwpf.converter.IURIResolver() {

				public String resolve(String uri) {
					return resolver.resolve(uri);
				}
			});

		}
		return options;
	}

	public MimeMapping getMimeMapping() {
		return XHTML_MIME_MAPPING;
	}

}
