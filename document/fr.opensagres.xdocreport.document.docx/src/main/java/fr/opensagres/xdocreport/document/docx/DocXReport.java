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

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.io.IEntryOutputStreamProvider;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.AbstractXDocReport;
import fr.opensagres.xdocreport.document.docx.images.DocxImageRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocXPreprocessor;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocxContentTypesPreprocessor;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocxDocumentXMLRelsPreprocessor;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkContentHandler;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.InitialHyperlinkMap;
import fr.opensagres.xdocreport.document.images.IImageRegistry;
import fr.opensagres.xdocreport.template.IContext;

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

	private boolean hasDynamicHyperlinks = false;

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

	@Override
	protected void onBeforePreprocessing(Map<String, Object> sharedContext,
			XDocArchive preprocessedArchive) throws XDocReportException {
		super.onBeforePreprocessing(sharedContext, preprocessedArchive);
		// Before starting preprocessing, Hyperlink must be getted from
		// "word/_rels/document.xml.rels" in the shared
		// context.
		HyperlinkContentHandler contentHandler = new HyperlinkContentHandler();
		Reader reader = preprocessedArchive
				.getEntryReader(WORD_RELS_DOCUMENTXMLRELS_XML_ENTRY);
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(contentHandler);
			xmlReader.parse(new InputSource(reader));
			if (contentHandler.getHyperlinks() != null) {
				sharedContext.put(HYPERLINKS_SHARED_CONTEXT,
						contentHandler.getHyperlinks());
			}
		} catch (SAXException e) {
			throw new XDocReportException(e);
		} catch (IOException e) {
			throw new XDocReportException(e);
		}
	}

	@Override
	protected void onAfterPreprocessing(Map<String, Object> sharedContext,
			XDocArchive preprocessedArchive) throws XDocReportException {
		super.onAfterPreprocessing(sharedContext, preprocessedArchive);
		// Compute if the docx has dynamic hyperlink
		if (sharedContext != null) {
			InitialHyperlinkMap hyperlinkMap = (InitialHyperlinkMap) sharedContext
					.get(HYPERLINKS_SHARED_CONTEXT);
			hasDynamicHyperlinks = hyperlinkMap == null ? false : hyperlinkMap
					.isModified();
		}
	}

	@Override
	protected void onBeforeProcessTemplateEngine(IContext context,
			XDocArchive outputArchive) throws XDocReportException {
		super.onBeforeProcessTemplateEngine(context, outputArchive);
		if (hasDynamicHyperlinks) {
			// docx has dynamic hyperlink, put an instance of HyperlinkRegistry
			// in the context.
			context.put(HyperlinkRegistry.KEY, new HyperlinkRegistry());
		}
	}

	@Override
	protected void onAfterProcessTemplateEngine(IContext context,
			XDocArchive outputArchive) throws XDocReportException {
		super.onAfterProcessTemplateEngine(context, outputArchive);
	}
}
