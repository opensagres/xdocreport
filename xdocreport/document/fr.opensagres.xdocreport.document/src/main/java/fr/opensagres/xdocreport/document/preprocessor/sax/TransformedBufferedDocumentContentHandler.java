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

import java.util.Collection;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * Document transformed to manage lazy loop for row table and dynamic image.
 * 
 */
public abstract class TransformedBufferedDocumentContentHandler extends
		BufferedDocumentContentHandler {

	private final FieldsMetadata fieldsMetadata;
	private final IDocumentFormatter formatter;
	private final Map<String, Object> sharedContext;

	// Table stack
	private final Stack<TableBufferedRegion> tableStack = new Stack<TableBufferedRegion>();
	private RowBufferedRegion currentRow;

	protected TransformedBufferedDocumentContentHandler(
			FieldsMetadata fieldsMetadata, IDocumentFormatter formater,
			Map<String, Object> sharedContext) {
		this.fieldsMetadata = fieldsMetadata;
		this.formatter = formater;
		this.sharedContext = sharedContext;
	}

	@Override
	public void startDocument() throws SAXException {
		String directive = formatter != null ? formatter
				.getStartDocumentDirective() : null;
		if (StringUtils.isNotEmpty(directive)) {
			this.bufferedDocument.append(directive);
		}
		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		String directive = formatter != null ? formatter
				.getEndDocumentDirective() : null;
		if (StringUtils.isNotEmpty(directive)) {
			this.bufferedDocument.append(directive);
		}
		super.endDocument();
	}

	@Override
	public boolean doStartElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (isTable(uri, localName, name)) {
			TableBufferedRegion currentTable = new TableBufferedRegion(
					currentRegion);
			currentRegion = currentTable;
			// Add table in the stack
			tableStack.add(currentTable);

		} else if (isRow(uri, localName, name)) {
			// Check if currentRow belong to a table
			if (tableStack.size() < 1) {
				throw new SAXException(
						"XML mal formatted. XML Row must be included in a XML Table");
			}
			currentRow = new RowBufferedRegion(currentRegion);
			currentRegion = currentRow;
		}
		return super.doStartElement(uri, localName, name, attributes);
	}

	@Override
	public void doEndElement(String uri, String localName, String name)
			throws SAXException {
		super.doEndElement(uri, localName, name);
		if (isTable(uri, localName, name)) {
			// end of table, remove the last table which was added
			TableBufferedRegion table = tableStack.pop();
			currentRegion = table.getParent();
			if (currentRegion instanceof RowBufferedRegion) {
				// Table was included in a row
				currentRow = (RowBufferedRegion) currentRegion;
			}
		} else if (isRow(uri, localName, name)) {
			// end of row, current region is the last table which was added.
			TableBufferedRegion table = tableStack.peek();
			currentRegion = table;
			currentRow = null;
		}
	}

	@Override
	protected void flushCharacters(String characters) {
		super.flushCharacters(processRowIfNeeded(characters));
	}

	public String processRowIfNeeded(String content) {
		return processRowIfNeeded(content, false);
	}

	/**
	 * If a row parsing, replace fields name with well script to manage lazy
	 * loop for table row.
	 * 
	 * @param content
	 * @return
	 */
	public String processRowIfNeeded(String content, boolean forceAsField) {
		ProcessRowResult result = getProcessRowResult(content, forceAsField);
		return result.getContent();
	}

	/**
	 * If a row parsing, replace fields name with well script to manage lazy
	 * loop for table row.
	 * 
	 * @param content
	 * @return
	 */
	public ProcessRowResult getProcessRowResult(String content,
			boolean forceAsField) {
		if (currentRow != null && formatter != null) {
			// characters parsing belong to a row
			// search if it contains fields list from metadata
			Collection<String> fieldsAsList = fieldsMetadata.getFieldsAsList();
			if (!currentRow.isLoopTemplateDirectiveInitilalized()) {
				for (final String fieldName : fieldsAsList) {
					if (content.contains(fieldName)) {
						String itemNameList = formatter.extractItemNameList(
								content, fieldName, forceAsField);
						if (StringUtils.isNotEmpty(itemNameList)) {
							currentRow.initializeLoopTemplateDirective(
									itemNameList, formatter);
							break;
						}
					}
				}
			}

			if (currentRow.isLoopTemplateDirectiveInitilalized()) {
				for (final String fieldName : fieldsAsList) {
					if (content.contains(fieldName)) {
						String newContent = formatter.formatAsFieldItemList(
								content, fieldName, forceAsField);
						if (newContent != null) {
							return new ProcessRowResult(newContent, fieldName,
									currentRow.getItemNameList(),
									currentRow.getStartLoopDirective(),
									currentRow.getEndLoopDirective());
						}
					}
				}
			}
		}
		return new ProcessRowResult(content, null, null, null, null);
	}

	public Map<String, Object> getSharedContext() {
		return sharedContext;
	}

	public FieldsMetadata getFieldsMetadata() {
		return fieldsMetadata;
	}

	public IDocumentFormatter getFormatter() {
		return formatter;
	}

	/**
	 * Returns true if current element is a table and false otherwise.
	 * 
	 * @param uri
	 * @param localName
	 * @param name
	 * @return
	 */
	protected abstract boolean isTable(String uri, String localName, String name);

	/**
	 * Returns true if current element is a table row and false otherwise.
	 * 
	 * @param uri
	 * @param localName
	 * @param name
	 * @return
	 */
	protected abstract boolean isRow(String uri, String localName, String name);

}
