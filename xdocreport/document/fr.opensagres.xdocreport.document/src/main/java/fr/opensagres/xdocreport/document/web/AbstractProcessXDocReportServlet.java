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
package fr.opensagres.xdocreport.document.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.IURIResolver;
import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.ProcessState;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * Abstract XDoc Report servlet used to process report (generate report and
 * remove report from cache).
 * 
 */
public abstract class AbstractProcessXDocReportServlet extends
		BaseXDocReportServlet implements XDocProcessServletConstants {

	private static final String TEXT_HTML_CONTENT_TYPE = "text/html";
	private static final String WEB_URI_RESOLVER_DATA_KEY = WEBURIResolver.class
			.getName();

	private static final long serialVersionUID = -4228326301636062279L;

	// JSP files
	private static final String ADMIN_JSP = "admin.jsp";
	private boolean cacheOriginalDocument = false;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.cacheOriginalDocument = StringUtils.asBoolean(
				super.getInitParameter("cacheOriginalDocument"), false);
	}

	/**
	 * Handles all requests (by default).
	 * 
	 * @param request
	 *            HttpServletRequest object containing client request
	 * @param response
	 *            HttpServletResponse object for the response
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String dispatch = getDispatchParameter(request);
		if (REMOVE_DISPATCH.equals(dispatch)) {
			doRemoveReport(request, response);
		} else {
			String entryName = getEntryName(request);
			ProcessState processState = super.getProcessState(request);
			if (processState == null) {
				processState = (StringUtils.isNotEmpty(entryName)) ? ProcessState.PREPROCESSED
						: ProcessState.GENERATED;
			}
			switch (processState) {
			case ORIGINAL:
			case PREPROCESSED:
				doDocumentArchive(processState, entryName, request, response);
				break;
			case GENERATED:
				doGenerateReport(entryName, request, response);
				break;
			}
		}
	}

	/**
	 * Remove report from the registry.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void doRemoveReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String reportId = getReportId(request);
		if (StringUtils.isNotEmpty(reportId)) {
			getRegistry(request).unregisterReport(reportId);
		}
		doRedirectAfterRemoveReport(request, response);
	}

	protected void doRedirectAfterRemoveReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendRedirect(ADMIN_JSP);
	}

	protected void doDocumentArchive(ProcessState state, String entryName,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (StringUtils.isEmpty(entryName)) {
			doSaveReport(state, request, response);
		} else {
			doSaveEntry(state, entryName, request, response);
		}
	}

	/**
	 * Save document archive of the report.
	 * 
	 * @param processState
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doSaveReport(ProcessState processState,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		try {
			IXDocReport report = getReport(request);
			if (report != null) {
				// 2) Prepare HTTP response content type
				prepareHTTPResponse(report.getId(), report.getMimeMapping(),
						request, response);
				try {
					report.save(processState, response.getOutputStream());
				} catch (XDocReportException e) {
					throw new ServletException(e);
				}
			}
		} catch (XDocReportException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * Extract entry from a report.
	 * 
	 * @param processState
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doSaveEntry(ProcessState processState, String entryName,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			IXDocReport report = getReport(request);
			if (report != null) {
				try {
					// 2) Prepare HTTP response content type
					prepareHTTPResponse(report.getId(), entryName, request,
							response);
					report.saveEntry(entryName, processState,
							response.getOutputStream());
				} catch (XDocReportException e) {
					throw new ServletException(e);
				}
			}
		} catch (XDocReportException e) {
			throw new ServletException(e);
		}
	}

	protected boolean doGenerateReport(String entryName,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// 1) Get XDoc report
			IXDocReport report = getReport(request);
			if (report == null) {
				throw new XDocReportException(
						"Cannot get XDoc Report for the HTTP request");
			}
			Options options = getOptionsConverter(report, request);
			if (options == null) {
				doProcessReport(report, entryName, request, response);
			} else {
				doProcessReportWithConverter(report, options, request, response);
			}
			return true;
		} catch (Exception e) {
			/*
			 * call the error handler to let the derived class do something
			 * useful with this failure.
			 */
			error(request, response, e);
			return false;
		}
	}

	/**
	 * Generate report with process.
	 * 
	 * @param report
	 * @param entryName
	 * @param request
	 * @param response
	 * @throws XDocReportException
	 * @throws IOException
	 */
	private void doProcessReport(IXDocReport report, String entryName,
			HttpServletRequest request, HttpServletResponse response)
			throws XDocReportException, IOException {
		// 1) Prepare Java model context
		IContext context = report.createContext();
		populateContext(context, report.getId(), request);

		if (StringUtils.isEmpty(entryName)) {
			// 2) Prepare HTTP response content type
			prepareHTTPResponse(report.getId(), report.getMimeMapping(),
					request, response);
			// 3) Generate report
			report.process(context, response.getOutputStream());
		} else {
			// 2) Prepare HTTP response content type
			prepareHTTPResponse(report.getId(), entryName, request, response);
			// 3) Generate report
			report.process(context, entryName, response.getOutputStream());
		}
	}

	/**
	 * Generate report with conversion.
	 * 
	 * @param report
	 * @param options
	 * @param request
	 * @param response
	 * @throws XDocReportException
	 * @throws IOException
	 * @throws XDocConverterException
	 */
	private void doProcessReportWithConverter(IXDocReport report,
			Options options, HttpServletRequest request,
			HttpServletResponse response) throws XDocReportException,
			IOException, XDocConverterException {
		IContext context = null;
		ITemplateEngine templateEngine = report.getTemplateEngine();
		if (templateEngine != null) {
			// 1) Prepare Java model context
			context = report.createContext();
			populateContext(context, report.getId(), request);
		}

		// 2) Get converter
		IConverter converter = report.getConverter(options);
		// 3) Prepare HTTP response content type
		prepareHTTPResponse(report.getId(), converter.getMimeMapping(),
				request, response);
		// 4) Generate report with conversion
		report.convert(context, options, response.getOutputStream());
	}

	// ----------------- Get Report

	/**
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws XDocReportException
	 */
	protected IXDocReport getReport(HttpServletRequest request)
			throws IOException, XDocReportException {
		XDocReportRegistry registry = getRegistry(request);
		// 1) Get report id
		String reportId = getReportId(request);
		if (StringUtils.isNotEmpty(reportId)) {
			// Search if report is cached in the registry
			IXDocReport report = registry.getReport(reportId);
			if (report != null) {
				return report;
			}
		}
		return loadReport(reportId, registry, request);
	}

	/**
	 * Load report.
	 * 
	 * @param reportId
	 * @param registry
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws XDocReportException
	 */
	protected IXDocReport loadReport(String reportId,
			XDocReportRegistry registry, HttpServletRequest request)
			throws IOException, XDocReportException {
		// 2) Get sourceStream
		InputStream sourceStream = getSourceStream(reportId, request);
		if (sourceStream == null) {
			throw new XDocReportException("Input stream is null with reportId="
					+ reportId);
		}
		IXDocReport report = null;
		// 3) Get template engine to use for the report
		ITemplateEngine templateEngine = null;

		String templateEngineKind = getTemplateEngineKind(reportId, request);
		if (StringUtils.isNotEmpty(templateEngineKind)) {
			// 3.1) Load report with template engine kind
			report = registry.loadReport(sourceStream, reportId,
					templateEngineKind);
		} else {
			// 3.1) Load report with template engine
			templateEngine = getTemplateEngine(reportId, request);
			report = registry
					.loadReport(sourceStream, reportId, templateEngine);
		}

		// 6) Set FieldsMetaData
		FieldsMetadata fieldsMetadata = getFieldsMetadata(reportId, request);
		report.setFieldsMetadata(fieldsMetadata);

		// 7) Set cache
		report.setCacheOriginalDocument(isCacheOriginalDocument(reportId,
				request));

		
		return report;
	}

	protected boolean isCacheOriginalDocument(String reportId,
			HttpServletRequest request) {
		return cacheOriginalDocument;
	}

	protected FieldsMetadata getFieldsMetadata(String reportId,
			HttpServletRequest request) {
		return null;
	}

	/**
	 * Invoked when there is an error thrown in any part of doRequest()
	 * processing. <br>
	 * <br>
	 * Default will send a simple HTML response indicating there was a problem.
	 * 
	 * @param request
	 *            original HttpServletRequest from servlet container.
	 * @param response
	 *            HttpServletResponse object from servlet container.
	 * @param cause
	 *            Exception that was thrown by some other part of process.
	 */
	protected void error(HttpServletRequest request,
			HttpServletResponse response, Exception cause)
			throws ServletException, IOException {
		if (response.isCommitted()) {
			throw new ServletException(cause);
		}
		response.setContentType(TEXT_HTML_CONTENT_TYPE);
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<title>Error</title>");
		html.append("<body bgcolor=\"#ffffff\">");
		html.append("<h2>XDocReport Servlet: Error report generation</h2>");
		html.append("<pre>");
		String why = cause.getMessage();
		if (why != null && why.trim().length() > 0) {
			html.append(why);
			html.append("<br>");
		}

		StringWriter sw = new StringWriter();
		cause.printStackTrace(new PrintWriter(sw));

		html.append(sw.toString());
		html.append("</pre>");
		html.append("</body>");
		html.append("</html>");
		response.getOutputStream().print(html.toString());

	}

	/**
	 * Returns the converter id.
	 * 
	 * @param request
	 * @return
	 */
	protected String getConverterId(IXDocReport report,
			HttpServletRequest request) {
		return (String) request.getParameter(CONVERTER_ID_HTTP_PARAM);
	}

	protected Options getOptionsConverter(IXDocReport report,
			HttpServletRequest request) {
		final String converterId = getConverterId(report, request);
		if (StringUtils.isEmpty(converterId)) {
			return null;
		}
		Options options = null;
		int index = converterId.lastIndexOf('_');
		if (index != -1) {
			String to = converterId.substring(0, index);
			String via = converterId.substring(index + 1, converterId.length());
			options = Options.getTo(to).via(via);
		} else {
			options = Options.getTo(converterId);
		}
		prepareOptions(options, report, converterId, request);
		return options;
	}

	protected void prepareOptions(Options options, IXDocReport report,
			String converterId, HttpServletRequest request) {
		if (ConverterTypeTo.FO.name().equals(options.getTo())
				|| ConverterTypeTo.XHTML.name().equals(options.getTo())) {
			options.setProperty(IURIResolver.class.getName(),
					createWEBURIResolver(report, converterId, request));
		}
	}

	public IURIResolver createWEBURIResolver(IXDocReport report,
			String converterId, HttpServletRequest request) {
		WEBURIResolver resolver = report.getData(WEB_URI_RESOLVER_DATA_KEY);
		if (resolver == null) {
			resolver = new WEBURIResolver(report.getId(), request);
			report.setData(WEB_URI_RESOLVER_DATA_KEY, resolver);
		}
		return resolver;
	}

	@Override
	protected boolean isGenerateContentDisposition(String reportId,
			MimeMapping mimeMapping, HttpServletRequest request) {
		return !VIEW_DISPATCH.equals(getDispatchParameter(request));
	}

	/**
	 * Returns input stream of the report to load identified with
	 * <code>reportId</code>.
	 * 
	 * @param reportId
	 *            report id.
	 * @param request
	 *            Http servlet request context.
	 * @return
	 * @throws IOException
	 * @throws XDocReportException
	 */
	protected abstract InputStream getSourceStream(String reportId,
			HttpServletRequest request) throws IOException, XDocReportException;

	/**
	 * Put the Java model in the context for the report <code>reportId</code>.
	 * 
	 * @param context
	 *            XDocReport context to register Java data model.
	 * @param reportId
	 *            report id.
	 * @param request
	 *            Http servlet request context.
	 * @throws IOException
	 * @throws XDocReportException
	 */
	protected abstract void populateContext(IContext context, String reportId,
			HttpServletRequest request) throws IOException, XDocReportException;
}
