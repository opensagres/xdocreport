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
package fr.opensagres.xdocreport.webapp.utils;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.web.XDocBaseServletConstants;
import fr.opensagres.xdocreport.document.web.XDocProcessServletConstants;

public class HTMLUtils implements XDocBaseServletConstants {

	private static final String PROCESS_REPORT_SERVLET_NAME = "processReport";
	private static final String LOAD_REPORT_SERVLET_NAME = "loadReport";
	private static final String LOADREPORT_SERVLET_NAME = "loadReport";
	private static final String ADMIN_JSP = "admin.jsp";
	private static final String HELLOWORLD_JSP = "helloworld.jsp";
	private static final String HELLOWORLD_DISPATCHER_JSP = "helloworldDispatcher.jsp";
	private static final String INDEX_JSP = "index.jsp";
	private static final String LOADREPORT_JSP = "loadReport.jsp";
	private static final String PROCESSREPORT_JSP = "processReport.jsp";
	private static final String DOCUMENT_ARCHIVE_JSP = "documentArchive.jsp";

	public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy HH:mm";

	public static void generateHTMLOption(String optionValue,
			String optionLabel, ServletRequest request, Writer writer,
			String parameterName) throws IOException {
		writer.write("<option value=\"");
		writer.write(optionValue);
		writer.write("\"");
		String parameterValue = request.getParameter(parameterName);
		if (StringUtils.isEmpty(parameterValue)) {
			parameterValue = (String) request.getAttribute(parameterName);
		}
		if (optionValue.equals(parameterValue)) {
			writer.write(" selected=\"true\" ");
		}
		writer.write(" >");
		writer.write(optionLabel);
		writer.write("</option>");
	}

	public static void generateHTMLOption(String optionValue,
			ServletRequest request, Writer writer, String parameterName)
			throws IOException {
		generateHTMLOption(optionValue, optionValue, request, writer,
				parameterName);
	}

	/**
	 * Generate URL to call admin.jsp.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/admin.jsp
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateAdminJSPURL(HttpServletRequest request) {
		StringBuilder href = new StringBuilder(request.getContextPath());
		href.append('/');
		href.append(ADMIN_JSP);
		return href.toString();
	}

	/**
	 * Generate URL to call helloworld.jsp.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/helloworld.jsp
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateHelloWorldJSPURL(HttpServletRequest request) {
		StringBuilder href = new StringBuilder(request.getContextPath());
		href.append('/');
		href.append(HELLOWORLD_JSP);
		return href.toString();
	}

	/**
	 * Generate URL to call helloworldDispatcher.jsp.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/helloworldDispatcher.jsp
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateHelloWorldDispatcherJSPURL(
			HttpServletRequest request) {
		StringBuilder href = new StringBuilder(request.getContextPath());
		href.append('/');
		href.append(HELLOWORLD_DISPATCHER_JSP);
		return href.toString();
	}

	/**
	 * Generate URL to call index.jsp.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/index.jsp
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateIndexJSPURL(HttpServletRequest request) {
		StringBuilder href = new StringBuilder(request.getContextPath());
		href.append('/');
		href.append(INDEX_JSP);
		return href.toString();
	}

	/**
	 * Generate URL to call loadReport servlet name.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/loadReport
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateLoadReportURL(HttpServletRequest request) {
		StringBuilder href = new StringBuilder(request.getContextPath());
		href.append('/');
		href.append(LOADREPORT_SERVLET_NAME);
		return href.toString();
	}

	/**
	 * Generate URL to call loadReport.jsp.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/loadReport.jsp
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateLoadReportJSPURL(HttpServletRequest request) {
		StringBuilder href = new StringBuilder(request.getContextPath());
		href.append('/');
		href.append(LOADREPORT_JSP);
		return href.toString();
	}

	/**
	 * Generate URL to call loadReport servlet to download original document
	 * archive from the report.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/loadReport?reportId=$reportId&dispatch=download
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateLoadReportURLDownloadDispatch(
			HttpServletRequest request, String reportId) {
		return generateLoadReportURL(request, reportId,
				XDocProcessServletConstants.DOWNLOAD_DISPATCH);
	}

	/**
	 * Generate URL to call loadReport servlet.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/loadReport?reportId=$reportId&dispatch=$dispatch
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateLoadReportURL(HttpServletRequest request,
			String reportId, String dispatch) {
		StringBuilder href = new StringBuilder(request.getContextPath());
		href.append('/');
		href.append(LOAD_REPORT_SERVLET_NAME);
		boolean hasParam = false;
		if (StringUtils.isNotEmpty(reportId)) {
			hasParam = true;
			href.append('?');
			href.append(REPORT_ID_HTTP_PARAM);
			href.append('=');
			href.append(reportId);
		}
		if (StringUtils.isNotEmpty(dispatch)) {
			if (hasParam) {
				href.append('&');
			} else {
				href.append('?');
			}
			href.append(DISPATCH_HTTP_PARAM);
			href.append('=');
			href.append(dispatch);
		}
		return href.toString();
	}

	/**
	 * Generate URL to call processReport.jsp.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/processReport.jsp
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateProcessReportJSPURL(HttpServletRequest request) {
		StringBuilder href = new StringBuilder(request.getContextPath());
		href.append('/');
		href.append(PROCESSREPORT_JSP);
		return href.toString();
	}

	/**
	 * Generate URL to call processReport servlet to remove from the report from
	 * the registry.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/processReport?reportId=$reportId&dispatch=remove
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateProcessReportURLRemoveDispatch(
			HttpServletRequest request, String reportId) {
		return generateProcessReportURL(request, reportId, null, null,
				XDocProcessServletConstants.REMOVE_DISPATCH);
	}

	/**
	 * Generate URL to call processReport servlet to download original document
	 * archive from the report.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/processReport?reportId=$reportId&dispatch=download
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateProcessReportURLDownloadDispatch(
			HttpServletRequest request, String reportId, String processState) {
		return generateProcessReportURL(request, reportId, null, processState,
				XDocProcessServletConstants.DOWNLOAD_DISPATCH);
	}

	/**
	 * Generate URL to call processReport servlet to download entry name from
	 * the document archive archive from the report.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/processReport?reportId=$reportId&dispatch=download
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateProcessReportURLDownloadEntryDispatch(
			HttpServletRequest request, String reportId, String entryName,
			String processState) {
		return generateProcessReportURL(request, reportId, entryName,
				processState, XDocProcessServletConstants.DOWNLOAD_DISPATCH);
	}

	/**
	 * Generate URL to call processReport servlet to view entry name from the
	 * document archive archive from the report.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/processReport?reportId=$reportId&dispatch=view
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateProcessReportURLViewEntryDispatch(
			HttpServletRequest request, String reportId, String entryName,
			String processState) {
		return generateProcessReportURL(request, reportId, entryName,
				processState, XDocProcessServletConstants.VIEW_DISPATCH);
	}

	/**
	 * Generate URL to call processReport servlet.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/processReport?reportId=$reportId&dispatch=load
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateProcessReportURLLoadDispatch(
			HttpServletRequest request, String reportId) {
		return generateProcessReportURL(request, reportId, null, null, "load");
	}

	/**
	 * Generate URL to call processReport servlet.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/processReport?reportId=$reportId&dispatch=$dispatch
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateProcessReportURL(HttpServletRequest request,
			String reportId, String entryName, String processState,
			String dispatch) {
		StringBuilder href = new StringBuilder(request.getContextPath());
		href.append('/');
		href.append(PROCESS_REPORT_SERVLET_NAME);
		boolean hasParam = false;
		if (StringUtils.isNotEmpty(reportId)) {
			hasParam = true;
			href.append('?');
			href.append(REPORT_ID_HTTP_PARAM);
			href.append('=');
			href.append(reportId);
		}
		if (StringUtils.isNotEmpty(entryName)) {
			if (hasParam) {
				href.append('&');
			} else {
				href.append('?');
			}
			href.append(ENTRY_NAME_HTTP_PARAM);
			href.append('=');
			href.append(entryName);
		}
		if (StringUtils.isNotEmpty(processState)) {
			if (hasParam) {
				href.append('&');
			} else {
				href.append('?');
			}
			href.append(PROCESS_STATE_HTTP_PARAM);
			href.append('=');
			href.append(processState);
		}
		if (StringUtils.isNotEmpty(dispatch)) {
			if (hasParam) {
				href.append('&');
			} else {
				href.append('?');
			}
			href.append(DISPATCH_HTTP_PARAM);
			href.append('=');
			href.append(dispatch);
		}
		return href.toString();
	}

	/**
	 * Generate URL to call documentArchive.jsp.
	 * 
	 * <pre>
	 *  ex : http://localhost:8080/xdocreport-webapp/documentArchive.jsp
	 * </pre>
	 * 
	 * @param request
	 * @param reportId
	 * @param dispatch
	 * @return
	 */
	public static String generateDocumentArchiveJSPURL(
			HttpServletRequest request) {
		StringBuilder href = new StringBuilder(request.getContextPath());
		href.append('/');
		href.append(DOCUMENT_ARCHIVE_JSP);
		return href.toString();
	}

	public static String formatDate(Date date) {
		return formatDate(date, DEFAULT_DATE_PATTERN);
	}

	public static String formatDate(Date date, String outputPattern) {
		if (date == null) {
			return "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(outputPattern);
		return sdf.format(date);
	}
}
