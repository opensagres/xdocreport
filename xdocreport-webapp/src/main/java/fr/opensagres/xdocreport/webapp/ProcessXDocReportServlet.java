/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com>, Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.opensagres.xdocreport.webapp;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.opensagres.xdocreport.converter.ConverterFrom;
import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.dispatcher.IXDocReportDispatcher;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.document.web.dispatcher.ProcessDispatcherXDocReportServlet;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.webapp.datamodel.MetaDataModel;
import fr.opensagres.xdocreport.webapp.defaultreport.DefaultReportRegistry;

public class ProcessXDocReportServlet extends
		ProcessDispatcherXDocReportServlet implements WebAppXDocReportConstants {

	private static final String XDOC_ARCHIVE = "XDocArchive";
	private static final String PREPROCESSED = "Preprocessed";
	private static final long serialVersionUID = 3993221341284875152L;
	private static final String PROCESSREPORT_JSP = "processReport.jsp";
	private static final String DOCUMENT_ARCHIVE_JSP = "documentArchive.jsp";

	// Dispatch values
	protected static final String LOAD_DISPATCH = "load";
	private static final String ENTRIES_DISPATCH = "entries";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.registerDispatcher(DefaultReportRegistry.INSTANCE);
		super.init(config);
	}

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String dispatch = super.getDispatchParameter(request);
		if (LOAD_DISPATCH.equals(dispatch)) {
			doLoad(request, response);
		} else if (ENTRIES_DISPATCH.equals(dispatch)) {
			doEntries(request, response);
		} else {
			super.processRequest(request, response);
		}
	}

	private void doLoad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			registerReportInfos(getReportId(request), request);
		} catch (XDocReportException e) {
			throw new ServletException(e);
		}
		request.getRequestDispatcher(PROCESSREPORT_JSP).forward(request,
				response);
	}

	private void registerReportInfos(String reportId, HttpServletRequest request)
			throws IOException, XDocReportException {
		if (StringUtils.isEmpty(reportId)) {
			return;
		}
		MetaDataModel dataModelForm = null;
		ITemplateEngine templateEngine = null;
		IXDocReport report = super.getRegistry(request).getReport(reportId);
		if (report != null) {
			dataModelForm = report.getData(DATA_MODEL_REPORT_KEY);
			templateEngine = report.getTemplateEngine();
		} else {
			dataModelForm = getMetaDataModel(reportId);
			templateEngine = getTemplateEngine(reportId, request);
		}

		if (dataModelForm != null) {
			request.setAttribute("HTMLDataModelForm", dataModelForm);
		}
		if (templateEngine != null) {
			request.setAttribute("TemplateEngine", templateEngine);
		}

		// Register converter type from in request attributes
		String from = getConverterTypeFrom(report, reportId, request);
		if (StringUtils.isNotEmpty(from)) {
			ConverterFrom converterFrom = ConverterRegistry.getRegistry()
					.getConverterFrom(from);
			if (converterFrom != null) {
				request.setAttribute(FROM_CONVERTER_ATTR_REQUEST, converterFrom);
			}
		}
	}

	private MetaDataModel getMetaDataModel(String reportId) {
		for (IXDocReportDispatcher<?> dispatcher : dispatchers) {
			MetaDataModel metaDataModel = ((DefaultReportRegistry) dispatcher)
					.getMetaDataModel(reportId);
			if (metaDataModel != null) {
				return metaDataModel;
			}
		}
		return null;
	}

	private void doEntries(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			IXDocReport report = super.getReport(request);
			if (report != null) {
				XDocArchive archive = report.getPreprocessedDocumentArchive();
				request.setAttribute(XDOC_ARCHIVE, archive);
				request.setAttribute(PREPROCESSED, report.isPreprocessed());
			}
		} catch (XDocReportException e) {
			throw new ServletException(e);
		}
		request.getRequestDispatcher(DOCUMENT_ARCHIVE_JSP).forward(request,
				response);
	}

	@Override
	protected IXDocReport getReport(HttpServletRequest request)
			throws IOException, XDocReportException {
		IXDocReport report = super.getReport(request);
		if (report != null) {
			registerReportInfos(report.getId(), request);
		}
		return report;
	}

	@Override
	protected IXDocReport loadReport(String reportId,
			XDocReportRegistry registry, HttpServletRequest request)
			throws IOException, XDocReportException {
		IXDocReport report = super.loadReport(reportId, registry, request);
		report.setData(LOADED_REPORT_DATE_KEY, Calendar.getInstance().getTime());
		return report;
	}

	// @Override
	// protected void populateContext(IContext context, String reportId,
	// HttpServletRequest request) throws IOException, XDocReportException {
	// IXDocReport report = super.getRegistry(request).getReport(reportId);
	// if (report != null) {
	// MetaDataModel dataModelForm = report.getData(DATA_MODEL_REPORT_KEY);
	// if (dataModelForm == null) {
	// // If report was not already loaded (default report) , create
	// // default data model
	// dataModelForm = getMetaDataModel(reportId);
	// if (dataModelForm != null) {
	// // It's a default report, store the data model in the report
	// report.setData(DATA_MODEL_REPORT_KEY, dataModelForm);
	// }
	// }
	// if (dataModelForm != null) {
	// dataModelForm.populateContext(context, request);
	// }
	// }
	// }

	private String getConverterTypeFrom(IXDocReport report, String reportId,
			HttpServletRequest request) throws IOException, XDocReportException {
		if (report != null) {
			return report.getKind();
		}
		return getConverterTypeFrom(reportId, request);
	}

	protected String getConverterTypeFrom(String reportId,
			HttpServletRequest request) throws IOException, XDocReportException {
		String converterTypeFrom = DefaultReportRegistry.INSTANCE
				.getConverterTypeFrom(reportId);
		if (StringUtils.isNotEmpty(converterTypeFrom)) {
			return converterTypeFrom;
		}
		return null;
	}
}
