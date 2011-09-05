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
package fr.opensagres.xdocreport.document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.preprocessor.IXDocPreprocessor;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * XML Document reporting used to load XML document (odt, docx...) and generate
 * reporting by merging it with Java data model by using a template engine
 * {@link ITemplateEngine}.
 * 
 * <p>
 * To generate reporting, here steps to follow :
 * 
 * <ol>
 * <li><b>Create an XML Document</b> (docx with MS Word, odt with Open Office)
 * by writing your fields with syntax wich depends on template engine :
 * <ul>
 * <li>with Freemarker : you can write Hello ${name}! in your docx, odt
 * document.</li>
 * <li>with Velocity : you can write Hello $name! in your docx, odt document.</li>
 * </ul>
 * <li><b>Create an instance of {@link IXDocReport}</b> switch the type of the
 * document:
 * <ul>
 * <li>for docx :
 * <code>IXDocReport report = new fr.opensagres.xdocreport.document.docx.DocXReport();</code>
 * </li>
 * <li>for odt :
 * <code>IXDocReport report = new fr.opensagres.xdocreport.document.odt.ODTReport();</code>
 * </li>
 * </ul>
 * </li>
 * </li>
 * <li><b>Initialize template engine</b> by using
 * {@link IXDocReport#setTemplateEngine(ITemplateEngine)} to set the template
 * engine (Velocity, Freemarker..) to use :
 * <ul>
 * <li>for freemarker :
 * <code>report.setTemplateEngine(net.sourceforge.rtf.template.freemarker.FreemarkerTemplateEngine.DEFAULT);</code>
 * </li>
 * <li>for velocity :
 * <code>report.setTemplateEngine(net.sourceforge.rtf.template.velocity.VelocityTemplateEngine.DEFAULT);</code>
 * </li>
 * </ul>
 * </li>
 * <li><b>Load XML Document</b> (odt, docx) by using
 * {@link IXDocReport#load(InputStream)}.</li>
 * <li>
 * <b>Prepare your Java model</b> by calling {@link IXDocReport#createContext()}
 * and put your Java model with {@link IContext#put(String, Object)}. For
 * instance :
 * <p>
 * <code>IContext context = report.createContext();
 * context.put("name", "word");</code></li>
 * </p>
 * </li> <li><b>Merge the docx, odt document with Java model</b> by calling
 * {@link IXDocReport#process(IContext, OutputStream)}. For instance :
 * <p>
 * <code>report.process(context, new FileOutputStream(new File("myfile.odt")));</code>
 * </p>
 * This method call will generate myfile.odt file wich will contains Hello word!
 * </li> </ol> </p>
 * 
 */
public interface IXDocReport extends Serializable {

	/**
	 * Returns the kind of the report (ODT, docx...).
	 * 
	 * @return
	 */
	String getKind();

	/**
	 * Returns the id of the {@link IXDocReport}. This id is used to cache an
	 * instance of {@link IXDocReport} with
	 * {@link XDocReportRegistry#loadReport(InputStream) and get instance from
	 * cache with {@link XDocReportRegistry#getReport(String)}.
	 * 
	 * @return the id of the {@link IXDocReport}
	 */
	String getId();

	/**
	 * Set the id of the {@link IXDocReport}. This id is used to cache an
	 * instance of {@link IXDocReport} with
	 * {@link XDocReportRegistry#loadReport(InputStream) and get instance from
	 * cache with {@link XDocReportRegistry#getReport(String)}.
	 * 
	 * @param id
	 *            of the {@link IXDocReport}
	 */
	void setId(String id);

	/**
	 * Load XML document (odt, docx...) from input stream.
	 * 
	 * @param sourceStream
	 *            stream of the XML Document zip (odt, docx...).
	 * @throws IOException
	 *             when XML Document zip (odt, docx...) cannot be read.
	 * @throws XDocReportException
	 *             when preprocessing with {@link IXDocPreprocessor} cannot be
	 *             done.
	 */
	void load(InputStream sourceStream) throws IOException, XDocReportException;

	/**
	 * Save original|preprocessed XML document archive in the given output
	 * stream.
	 * 
	 * @param out
	 * @throws IOException
	 */
	void save(ProcessState processState, OutputStream out) throws IOException,
			XDocReportException;

	/**
	 * Save original|preprocessed entryName from XML document archive in the
	 * given output stream.
	 * 
	 * @param out
	 * @throws IOException
	 */
	void saveEntry(String entryName, ProcessState processState, OutputStream out)
			throws IOException, XDocReportException;

	/**
	 * Returns the zipped XML document (odt, docx...).
	 * 
	 * @param documentArchive
	 *            {@link XDocArchive}.
	 * @throws XDocReportException
	 *             when preprocessing with {@link IXDocPreprocessor} cannot be
	 *             done.
	 */
	void setDocumentArchive(XDocArchive documentArchive) throws IOException,
			XDocReportException;

	/**
	 * Returns the original zipped XML document (odt, docx...) and null if :
	 * 
	 * <ul>
	 * <li>no load was done.</li>
	 * <li>cache for original document is false (see
	 * {@link IXDocReport#setCacheOriginalDocument(boolean)}.</li>
	 * </ul>
	 * 
	 * @return
	 */
	XDocArchive getOriginalDocumentArchive();

	/**
	 * Returns the preprocessed zipped XML document (odt, docx...) and null if
	 * no load was done.
	 * 
	 * @return
	 */
	XDocArchive getPreprocessedDocumentArchive();

	/**
	 * Set the template engine (Velocity, Freemarker..) to use switch the syntax
	 * used in the XML document.
	 * 
	 * @param templateEngine
	 */
	void setTemplateEngine(ITemplateEngine templateEngine);

	/**
	 * Returns the template engine (Velocity, Freemarker..) to use switch the
	 * syntax used in the XML document.
	 * 
	 * @return
	 */
	ITemplateEngine getTemplateEngine();

	/**
	 * Set fields metadat used to manage lazy loop in table row.
	 * 
	 * @param metadata
	 */
	void setFieldsMetadata(FieldsMetadata metadata);

	/**
	 * Create a new instance of {@link IContext} to put the Java model.
	 * 
	 * @return
	 * @throws XDocReportException
	 */
	IContext createContext() throws XDocReportException;

	/**
	 * Merge the docx, odt document with Java model from the context and
	 * register the result to the output stream.
	 * 
	 * @param context
	 *            of the Java model.
	 * @param out
	 *            output stream where merge must be saved.
	 * @throws XDocReportException
	 * @throws IOException
	 */
	void process(IContext context, OutputStream out)
			throws XDocReportException, IOException;

	/**
	 * Merge the docx, odt document with Java model from the context and
	 * register the entryName transformed to the output stream.
	 * 
	 * @param context
	 *            of the Java model.
	 * @param entryName
	 *            entry name
	 * @param out
	 *            output stream where merge must be saved.
	 * @throws XDocReportException
	 * @throws IOException
	 */
	void process(IContext context, String entryName, OutputStream outputStream)
			throws XDocReportException, IOException;

	/**
	 * Returns converter for the report and options.
	 * 
	 * @param options
	 * @return
	 */
	IConverter getConverter(Options options) throws XDocConverterException;

	/**
	 * Convert report.
	 * 
	 * @param context
	 * @param options
	 * @param out
	 * @throws XDocReportException
	 * @throws XDocConverterException
	 * @throws IOException
	 */
	void convert(IContext context, Options options, OutputStream out)
			throws XDocReportException, XDocConverterException, IOException;


	/**
	 * Returns mime mapping switch the kind of the report (odt, docx...).
	 * 
	 * @return
	 */
	MimeMapping getMimeMapping();

	/**
	 * Store custom data.
	 * 
	 * @param key
	 * @param value
	 */
	void setData(String key, Object value);

	/**
	 * Clear data.
	 * 
	 * @param string
	 */
	void clearData(String key);

	/**
	 * Returns custom data.
	 * 
	 * @param key
	 * @return
	 */
	<T> T getData(String key);

	void extractFields(FieldsExtractor extractor, ITemplateEngine templateEngine)
			throws XDocReportException;

	void extractFields(FieldsExtractor extractor) throws XDocReportException;

	/**
	 * Set cache or not for original document archive.
	 * 
	 * @param cache
	 */
	void setCacheOriginalDocument(boolean cacheOriginalDocument);

	/**
	 * Returns true if report was processed and false otherwise.
	 * 
	 * @return
	 */
	public boolean isPreprocessed();

	/**
	 * Returns the last modified time when the report is loaded.
	 * 
	 * @return
	 */
	public long getLastModified();
}
