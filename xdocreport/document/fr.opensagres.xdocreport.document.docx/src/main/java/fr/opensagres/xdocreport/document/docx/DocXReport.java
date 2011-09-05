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
package fr.opensagres.xdocreport.document.docx;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.io.IEntryOutputStreamProvider;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.document.AbstractXDocReport;
import fr.opensagres.xdocreport.document.docx.images.DocxImageRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocXPreprocessor;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocxContentTypesPreprocessor;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocxDocumentXMLRelsPreprocessor;
import fr.opensagres.xdocreport.document.images.IImageRegistry;

/**
 * MS Word DOCX report.
 * 
 */
public class DocXReport extends AbstractXDocReport implements DocXConstants {

	private static final long serialVersionUID = -2323716817951928168L;

	private static final String[] DEFAULT_XML_ENTRIES = {
			WORD_DOCUMENT_XML_ENTRY, WORD_STYLES_XML_ENTRY,
			WORD_HEADER_XML_ENTRY, WORD_FOOTER_XML_ENTRY,
			WORD_RELS_DOCUMENTXMLRELS_XML_ENTRY };

	static final String WORD_REGEXP = "word*";

	public String getKind() {
		return DocumentKind.DOCX.name();
	}

	@Override
	protected void registerPreprocessors() {
		super.addPreprocessor(WORD_DOCUMENT_XML_ENTRY,
				DocXPreprocessor.INSTANCE);
		super.addPreprocessor(WORD_HEADER_XML_ENTRY, DocXPreprocessor.INSTANCE);
		super.addPreprocessor(WORD_FOOTER_XML_ENTRY, DocXPreprocessor.INSTANCE);
		super.addPreprocessor(CONTENT_TYPES_XML_ENTRY,
				DocxContentTypesPreprocessor.INSTANCE);
		super.addPreprocessor(WORD_RELS_DOCUMENTXMLRELS_XML_ENTRY,
				DocxDocumentXMLRelsPreprocessor.INSTANCE);
	}

	@Override
	protected String[] getDefaultXMLEntries() {
		return DEFAULT_XML_ENTRIES;
	}

	public MimeMapping getMimeMapping() {
		return MIME_MAPPING;
	}

	@Override
	protected IImageRegistry createImageRegistry(
			IEntryReaderProvider readerProvider,
			IEntryWriterProvider writerProvider,
			IEntryOutputStreamProvider outputStreamProvider) {
		return new DocxImageRegistry(readerProvider, writerProvider,
				outputStreamProvider);
	}

}
