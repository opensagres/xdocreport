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
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.preprocessor.AbstractXDocPreprocessor;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public abstract class SAXXDocPreprocessor extends AbstractXDocPreprocessor {

	@Override
	public boolean preprocess(String entryName, Reader reader, Writer writer,
			Writer debugWriter, FieldsMetadata fieldsMetadata,
			IDocumentFormatter formater, Map<String, Object> sharedContext)
			throws XDocReportException, IOException {
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			BufferedDocumentContentHandler contentHandler = createBufferedDocumentContentHandler(
					fieldsMetadata, formater, sharedContext);
			xmlReader.setContentHandler(contentHandler);
			xmlReader.parse(new InputSource(reader));
			BufferedDocument document = contentHandler.getBufferedDocument();
			if (document != null) {
				document.save(writer);
				if (debugWriter != null) {
					document.save(debugWriter);
				}
				return true;
			}
			return false;
		} catch (SAXException e) {
			throw new XDocReportException(e);
		}
	}

	protected abstract BufferedDocumentContentHandler createBufferedDocumentContentHandler(
			FieldsMetadata fieldsMetadata, IDocumentFormatter formatter,
			Map<String, Object> sharedContext);

}
