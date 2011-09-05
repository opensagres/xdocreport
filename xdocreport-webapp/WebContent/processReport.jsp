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
<%@page import="fr.opensagres.xdocreport.core.utils.StringUtils"%>
<%@page import="fr.opensagres.xdocreport.webapp.datamodel.MetaDataModel"%>
<%@page
	import="fr.opensagres.xdocreport.webapp.defaultreport.DefaultReportRegistry"%>
<%@page import="fr.opensagres.xdocreport.converter.ConverterTo"%>
<%@page
	import="fr.opensagres.xdocreport.webapp.WebAppXDocReportConstants"%>
<%@page import="fr.opensagres.xdocreport.converter.ConverterFrom"%>
<%@page import="fr.opensagres.xdocreport.template.ITemplateEngine"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>XDocReport - Process/Convert report</title>
<link href="styles/xdocreport.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="scripts/xdocreport.js"></script>
</head>
<body>
<center>
<%
	ConverterFrom converterFrom = (ConverterFrom) request
			.getAttribute(WebAppXDocReportConstants.FROM_CONVERTER_ATTR_REQUEST);
	String previewReport = request.getParameter("previewReport");
%>
<table width="90%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><a href="<%=HTMLUtils.generateIndexJSPURL(request)%>">&lt;&lt;
		Home</a>

		<fieldset><legend>Reports Process/Conversion</legend>
		<form name="sampleXDocReportForm"
			action="<%=request.getContextPath()%>/processReport" method="post"
			target="_blank"><input type="hidden" name="dispatch"
			id="dispatch" />
		<table width="100%">
			<tr>
				<td>
				<table>
					<tr>
						<td colspan="2">Choose an existing report and generate report
						or go at <strong><a
							href="<%=HTMLUtils.generateLoadReportJSPURL(request)%>">Load
						report</a></strong> to upload your own odt, docx... document.</td>
					</tr>
					<tr>
						<td colspan="2"><strong><span style="color: red;">Note
						for converter</span></strong> : please note that converter are not finished. It
						exists some layout problems. We have no intention to support Draw
						shapes for the moment.
					</tr>
					<tr>
						<td colspan="2"><strong><span style="color: red;">Note
						for data model</span></strong> : change value of data model and generate report.
						If converter is selected, data model change will refresh the
						converted (2 XHTML, PDF) generated report in the "Report preview"
						iframe.
					</tr>
					<tr>
						<td valign="top">
						<table>
							<tr>
								<td><input type="submit" value="Report" /> <%
 	if (converterFrom != null) {
 %> <select name="converter"
									onchange="javascript:submitForm('load', '_self');">
									<%
										HTMLUtils.generateHTMLOption("", "-- No conversion --",
													request, out, "converter");

											Collection<ConverterTo> converterTos = converterFrom
													.getConvertersTo();
											for (ConverterTo converterTo : converterTos) { 
												Collection<String> vias = converterTo.getVias();
												for (String via : vias) {

													HTMLUtils.generateHTMLOption(converterTo.getTo() + "_"
															+ via, "To " + converterTo.getTo() + " via "
															+ via, request, out, "converter");
												}
											}
									%>
								</select> <%
 	}
 %>
								</td>
							</tr>
							<tr>
								<td>
								<fieldset><legend>Choose a report</legend><select
									name="reportId"
									onchange="javascript:submitForm('load', '_self');"
									size="16">
									<%
										// Generation des option HTML des document par defaut
										DefaultReportRegistry.INSTANCE.toHTMLOptions(request, out);

										Collection<IXDocReport> reports = XDocReportRegistry.getRegistry()
												.getCachedReports();
										for (IXDocReport report : reports) {
											if (report.getData("default") != null
													&& (Boolean) report.getData("default") == false) {
												HTMLUtils.generateHTMLOption(report.getId(), request, out,
														"reportId");
											}
										}
									%>
								</select></fieldset>
								</td>
							</tr>
							<tr>
								<td>
								<fieldset><legend>Report detail</legend> <%
 	ITemplateEngine templateEngine = (ITemplateEngine) request
 			.getAttribute("TemplateEngine");
 	if (templateEngine != null) {
 %>Template engine:<%=templateEngine.getId()%> <%
 	}
 %>
								</fieldset>
								</td>
							</tr>
							<tr>
								<td><input type="submit" value="Report" /></td>
							</tr>
						</table>
						</td>
						<td valign="top" width="100%">
						<fieldset><legend>Data model</legend>
						<table width="100%" id="dataModel">
							<thead>
								<tr>
									<th width="10%">Field name</th>
									<th>Value</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<%
										MetaDataModel dataModel = (MetaDataModel) request
												.getAttribute("HTMLDataModelForm");
										if (dataModel != null) {
											dataModel.toHTML(out, request, 60, true, true);
										} else {
									%><td>Not available</td>
									<%
										}
									%>
								</tr>
							</tbody>
						</table>
						</fieldset>
						</td>
					</tr>
					<tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<fieldset><legend>Report preview</legend> <%
 	String reportId = request.getParameter("reportId");
 	String converter = request.getParameter("converter");
 	if (StringUtils.isNotEmpty(reportId)
 			&& StringUtils.isNotEmpty(converter)) {
 %> <iframe id="previewFrame" width="100%" height="500px">
				<p>Your browser does not support iframes.</p>
				</iframe> <script type="text/javascript">processReport('<%=request.getContextPath()%>/processReport?dispatch=view&reportId=<%=reportId%>&converter=<%=converter%>', null);</script>
				<%
					}
				%>
				</fieldset> 
				</td>
			</tr>
		</table>
		</form>
		</fieldset>

		</td>
	</tr>
</table>
</center>
</body>
</html>