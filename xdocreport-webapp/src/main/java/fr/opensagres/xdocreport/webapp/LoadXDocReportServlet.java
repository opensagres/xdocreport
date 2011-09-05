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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.document.web.UploadXDocReportServlet;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.webapp.datamodel.MetaDataModel;
import fr.opensagres.xdocreport.webapp.datamodel.MetaDataModelField;
import fr.opensagres.xdocreport.webapp.defaultreport.DefaultReportController;
import fr.opensagres.xdocreport.webapp.defaultreport.DefaultReportRegistry;

public class LoadXDocReportServlet extends UploadXDocReportServlet implements
		WebAppXDocReportConstants {

	private static final long serialVersionUID = 9102651291455406387L;
	private static final String LOAD_DISPATCH = "load";
	private static final String SAVE_DISPATCH = "save";
	private static final String DOWNLOAD_DISPATCH = "download";

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String dispatch = getDispatchParameter(request);
		if (LOAD_DISPATCH.equals(dispatch)) {
			doLoad(request, response);
		} else if (SAVE_DISPATCH.equals(dispatch)) {
			doSave(request, response);
		} else if (DOWNLOAD_DISPATCH.equals(dispatch)) {
			doDownload(request, response);
		} else {
			// Clear registry from HTTP session
			super.clearRegistryFromHTTPSession(request);
			// Upload
			super.processRequest(request, response);
		}
	}

	private void doLoad(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ITemplateEngine templateEngine = null;
		String reportId = getReportId(request);
		IXDocReport report = getRegistryFromHTTPSession(request).getReport(
				reportId);
		if (report != null) {
			templateEngine = getTemplateEngine(report, request);
			if (templateEngine != null) {
				MetaDataModel extractor = new MetaDataModel();
				try {
					report.extractFields(extractor, templateEngine);
					report.setData(DATA_MODEL_REPORT_KEY, extractor);
				} catch (XDocReportException e) {
					throw new ServletException(e);
				}
			}
		}
		super.doForward(report, request, response);
	}

	private void doSave(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ITemplateEngine templateEngine = null;
		String reportId = getReportId(request);
		IXDocReport report = getRegistryFromHTTPSession(request).getReport(
				reportId);
		boolean reportTmp = report != null;
		if (report == null) {
			report = getRegistry(request).getReport(reportId);
		}
		if (report != null) {
			templateEngine = getTemplateEngine(report, request);
			if (templateEngine != null) {
				report.setTemplateEngine(templateEngine);
				updateMetaDataModel(report, request);
				if (reportTmp) {
					// Clear registry from HTTP session
					super.clearRegistryFromHTTPSession(request);
					// Mark as saved report
					report.setData(SAVED_REPORT_KEY, true);
					report.setData(LOADED_REPORT_DATE_KEY, Calendar
							.getInstance().getTime());
					try {
						// Register the report in global registry
						getRegistry(request).registerReport(report);
					} catch (XDocReportException e) {
						throw new ServletException(e);
					}
				}
			}
		}
		super.doForward(report, request, response);
	}

	private void doDownload(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String reportId = getReportId(request);
		if (StringUtils.isNotEmpty(reportId)) {
			DefaultReportController defaultReport = DefaultReportRegistry.INSTANCE
					.getReportController(reportId);
			if (defaultReport != null) {
				// Get mime mapping
				MimeMapping mimeMapping = null;
				try {
					mimeMapping = XDocReportRegistry.getRegistry()
							.getMimeMapping(defaultReport.getFileExtension());
				} catch (XDocReportException e) {
				}
				prepareHTTPResponse(reportId, mimeMapping, request, response);
				IOUtils.copy(defaultReport.getSourceStream(),
						response.getOutputStream());
			}
		}
	}

	private void updateMetaDataModel(IXDocReport report,
			HttpServletRequest request) {
		MetaDataModel extractor = report.getData(DATA_MODEL_REPORT_KEY);
		if (extractor != null) {
			List<MetaDataModelField> fields = extractor.getFields();
			for (MetaDataModelField field : fields) {
				String name = field.getName();
				String defaultValue = request.getParameter(name);
				field.setDefaultValue(defaultValue);
			}
		}
	}

	@Override
	protected XDocReportRegistry getRegistryForUpload(HttpServletRequest request) {
		return super.getRegistryFromHTTPSession(request);
	}

	@Override
	protected void reportLoaded(IXDocReport report, HttpServletRequest request) {
		super.reportLoaded(report, request);
		report.setData(DEFAULT_REPORT_KEY, false);
		// Clone and store original XDocArchive because when template engine
		// change, preprocessing must be redone.
		// report.setData(LOADED_REPORT_DATE_KEY,
		// report.getDocumentArchive().createCopy());
	}
}
