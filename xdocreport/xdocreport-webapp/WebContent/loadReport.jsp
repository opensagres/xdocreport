<%--

    Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com>, Pascal Leclercq <pascal.leclercq@gmail.com>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@page import="fr.opensagres.xdocreport.webapp.defaultreport.DefaultReportController"%>
<%@page
	import="fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry"%>
<%@page
	import="fr.opensagres.xdocreport.webapp.defaultreport.DefaultReportRegistry"%>
<%@page
	import="fr.opensagres.xdocreport.document.web.XDocBaseServletConstants"%>
<%@page
	import="fr.opensagres.xdocreport.webapp.WebAppXDocReportConstants"%>
<%@page import="fr.opensagres.xdocreport.template.FieldsExtractor"%>
<%@page import="fr.opensagres.xdocreport.core.utils.StringUtils"%>
<%@page
	import="fr.opensagres.xdocreport.document.discovery.IXDocReportFactoryDiscovery"%>
<%@page import="fr.opensagres.xdocreport.webapp.utils.HTMLUtils"%>
<%@page import="fr.opensagres.xdocreport.webapp.datamodel.MetaDataModel"%>
<%@page import="fr.opensagres.xdocreport.document.IXDocReport"%>
<%@page import="java.util.Collection"%>
<%@page
	import="fr.opensagres.xdocreport.document.registry.XDocReportRegistry"%>
<%@page
	import="fr.opensagres.xdocreport.template.discovery.ITemplateEngineDiscovery"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>XDocReport - Load report</title>
<link href="styles/xdocreport.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="scripts/xdocreport.js"></script>
</head>
<body>
<%
	IXDocReport report = (IXDocReport) request
			.getAttribute(XDocBaseServletConstants.XDOCREPORT_ATTR_KEY);
%>
<center>
<table width="700px" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><a href="<%=HTMLUtils.generateIndexJSPURL(request)%>">&lt;&lt;
		Home</a> <%
 	if (report == null) {
 		// Step 1/2 : upload document
 %>
		<fieldset><legend> Step 1/2 : Upload your
		document. </legend>
		<form name="uploadXDocReportForm"
			action="<%=HTMLUtils.generateLoadReportURL(request)%>" method="post"
			enctype="multipart/form-data">Specify your document:<input
			type="file" name="uploadfile" /> <input type="submit" name="Submit"
			value="Submit your document">
		<p>Note : only <%
			boolean severalExtension = false;
				Collection<IXDocReportFactoryDiscovery> discoveries = XDocReportRegistry
						.getRegistry().getReportFactoryDiscoveries();
				for (IXDocReportFactoryDiscovery discovery : discoveries) {
					if (severalExtension) {
		%>,<%
			}
		%> <strong><%=discovery.getMimeMapping().getExtension()%></strong> <%
 	severalExtension = true;

 		}
 %>are supported. It depends on your JAR you have setted in your WEB
		application classpath. Go at <a
			href="<%=HTMLUtils.generateAdminJSPURL(request)%>">XDoc Report
		Administration.</a> to check installed XDocReport factory.</p>
		</form>
		</fieldset>

		<fieldset><legend>Download</legend> You can download
		default provided sample document, modify it and upload it to start to
		create your document or go at <strong><a
			href="<%=HTMLUtils.generateAdminJSPURL(request)%>">XDoc Report
		Administration</a></strong> to download cached XDoc reports.
		<table class="border">
			<thead>
				<tr>
					<th>ID</th>
					<th>Download</th>
				</tr>
			</thead>
			<tbody>
				<%
					Collection<DefaultReportController> defaultReports = DefaultReportRegistry.INSTANCE
								.getDefaultReports();
						for (DefaultReportController defaultReport : defaultReports) {
				%>
				<tr>
					<td><%=defaultReport.getReportId()%></td>
					<td><a
						href="<%=HTMLUtils.generateLoadReportURLDownloadDispatch(
							request, defaultReport.getReportId())%>">Download</a></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
		</fieldset>

		<%
			} else {
				// Step 2/2 : define template engine and data model
		%>
		<fieldset><legend> Step 2/2 : Define template
		engine and data model. </legend> <%
 	if (report.getData(WebAppXDocReportConstants.SAVED_REPORT_KEY) != null) {
 %><strong>XDoc Report is saved. Go at <a
			href="<%=HTMLUtils.generateProcessReportURLLoadDispatch(
							request, report.getId())%>">Process
		report</a> to generate your report.</strong> <%
 	}
 %>
		<form name="saveXDocReportForm"
			action="<%=request.getContextPath()%>/loadReport" method="post">
		<input type="hidden" name="dispatch" id="dispatch" />
		<table width="100%">
			<tr>
				<td>Report ID: <%=report.getId()%><input type="hidden"
					name="reportId" value="<%=report.getId()%>" /></td>
			</tr>
			<tr>
				<td>Template engine:<select name="templateEngineKind"
					onchange="javascript:submitForm('load');">
					<%
						HTMLUtils.generateHTMLOption("",
									"--- Choose a template engine ---", request, out,
									"templateEngineKind");
							Collection<String> kinds = TemplateEngineRegistry.getRegistry()
									.getTemplateEngineKinds();
							for (String templateEngineKind : kinds) {

								HTMLUtils.generateHTMLOption(templateEngineKind, request,
										out, "templateEngineKind");
							}
					%>
				</select></td>
			</tr>
			<tr>
				<td>
				<fieldset><legend>Data Model</legend>
				<table width="100%">
					<thead>
						<tr>
							<th>Field name</th>
							<th>Default value</th>
						</tr>
					</thead>
					<tbody>
						<%
							String templateEngineKind = request
										.getParameter("templateEngineKind");
								if (StringUtils.isNotEmpty(templateEngineKind)) {
						%>
						<tr>
							<td><input type="button" value="Save"
								onclick="javascript:submitForm('save');" /></td>
						</tr>
						<%
							}
								MetaDataModel extractor = (MetaDataModel) report
										.getData(WebAppXDocReportConstants.DATA_MODEL_REPORT_KEY);
								if (extractor != null) {
									extractor.toHTML(out, request, 70, false, false);
								}
						%>
						<%
							if (StringUtils.isNotEmpty(templateEngineKind)) {
						%>
						<tr>
							<td><input type="button" value="Save"
								onclick="javascript:submitForm('save');" /></td>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>
				</fieldset>
				</td>
			</tr>
		</table>
		</form>
		<%
			}
		%>
		</fieldset>

		</td>
	</tr>
</table>
</center>
</body>
</html>