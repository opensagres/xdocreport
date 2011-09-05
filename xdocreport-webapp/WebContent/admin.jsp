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
<%@page import="fr.opensagres.xdocreport.template.ITemplateEngine"%>
<%@page
	import="fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry"%>
<%@page import="java.util.Date"%>
<%@page
	import="fr.opensagres.xdocreport.webapp.WebAppXDocReportConstants"%>
<%@page import="fr.opensagres.xdocreport.converter.ConverterTo"%>
<%@page import="fr.opensagres.xdocreport.converter.ConverterFrom"%>
<%@page import="fr.opensagres.xdocreport.converter.IConverter"%>
<%@page import="fr.opensagres.xdocreport.converter.ConverterRegistry"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="fr.opensagres.xdocreport.webapp.utils.HTMLUtils"%>
<%@page
	import="fr.opensagres.xdocreport.document.registry.XDocReportRegistry"%>
<%@page import="fr.opensagres.xdocreport.document.IXDocReport"%>
<%@page import="java.util.Collection"%>
<%@page
	import="fr.opensagres.xdocreport.document.discovery.IXDocReportFactoryDiscovery"%>
<%@page
	import="fr.opensagres.xdocreport.template.discovery.ITemplateEngineDiscovery"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>XDocReport - Administration</title>
<link href="styles/xdocreport.css" rel="stylesheet" type="text/css" />
</head>

<body>
<center>
<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><a href="<%=HTMLUtils.generateIndexJSPURL(request)%>">&lt;&lt;
		Home</a>
		<h1>XDoc Report Factory</h1>
		<table class="border">
			<thead>
				<tr>
					<th>ID</th>
					<th>Description</th>
					<th>File extension</th>
					<th>Mime type</th>
					<th>Class</th>
				</tr>
			</thead>
			<%
				Collection<IXDocReportFactoryDiscovery> discoveries = XDocReportRegistry
						.getRegistry().getReportFactoryDiscoveries();
				for (IXDocReportFactoryDiscovery discovery : discoveries) {
			%>
			<tr>
				<td><%=discovery.getId()%></td>
				<td><%=discovery.getDescription()%></td>
				<td><%=discovery.getMimeMapping().getExtension()%></td>
				<td><%=discovery.getMimeMapping().getMimeType()%></td>
				<td><%=discovery.getReportClass().getName()%></td>
			</tr>
			<%
				}
			%>
		</table>
		<h1>Template engine</h1>
		<table class="border">
			<thead>
				<tr>
					<th>Kind</th>
					<th>Default?</th>
					<th>Class</th>
				</tr>
			</thead>
			<%
				Collection<ITemplateEngine> templateEngines = TemplateEngineRegistry
						.getRegistry().getTemplateEngines();
				for (ITemplateEngine templateEngine : templateEngines) {
			%>
			<tr>
				<td><%=templateEngine.getKind()%></td>
				<td><%=TemplateEngineRegistry.getRegistry().isDefault(
						templateEngine)%></td>
				<td><%=templateEngine.getClass().getName()%></td>
			</tr>
			<%
				}
			%>
		</table>
		<h1>Cached XDoc Report from the Registry</h1>
		<table class="border">
			<thead>
				<tr>
					<th>Report ID</th>
					<th>Template engine</th>
					<th>File extension</th>
					<th>Loaded date</th>
					<th>Remove</th>
					<th>Download Original</th>
					<th>Download Preprocessed</th>
				</tr>
			</thead>
			<%
				Collection<IXDocReport> reports = XDocReportRegistry.getRegistry()
						.getCachedReports();
				for (IXDocReport report : reports) {
			%>
			<tr>
				<td><%=report.getId()%></td>
				<td><%=report.getTemplateEngine().getId()%></td>
				<td><%=report.getMimeMapping().getExtension()%></td>
				<td><%=HTMLUtils.formatDate((Date) report
						.getData(WebAppXDocReportConstants.LOADED_REPORT_DATE_KEY))%></td>
				<td><a
					href="<%=HTMLUtils.generateProcessReportURLRemoveDispatch(
						request, report.getId())%>">Remove</a></td>
				<td><a
					href="<%=HTMLUtils.generateProcessReportURLDownloadDispatch(
						request, report.getId(), "original")%>">Download</a></td>
				<td>
				<%
					if (report.isPreprocessed()) {
				%><a
					href="<%=HTMLUtils
							.generateProcessReportURLDownloadDispatch(request,
									report.getId(), "preprocessed")%>">Download</a>
				<%
					}
				%>
				</td>
			</tr>
			<%
				}
			%>
		</table>

		<h1>Available Converters</h1>
		<table class="border">
			<thead>
				<tr>
					<th>From</th>
					<th>To</th>
					<th>Via</th>
					<th>File extension</th>
					<th>Mime type</th>
					<th>Class converter</th>
				</tr>
			</thead>
			<%
				Collection<String> froms = ConverterRegistry.getRegistry()
						.getFroms();
				for (String from : froms) {

					ConverterFrom fromConverter = ConverterRegistry.getRegistry()
							.getConverterFrom(from);
					Collection<ConverterTo> converterTos = fromConverter
							.getConvertersTo();
					for (ConverterTo converterTo : converterTos) {
						Collection<String> vias = converterTo.getVias();
						for (String via : vias) {
							IConverter converter = converterTo.getConverter(via);
			%>
			<tr>
				<td><%=from%></td>
				<td><%=converterTo.getTo()%></td>
				<td><%=via%></td>
				<td><%=converter.getMimeMapping().getExtension()%></td>
				<td><%=converter.getMimeMapping().getMimeType()%></td>
				<td><%=converter.getClass().getName()%></td>
			</tr>
			<%
				}
					}
				}
			%>
		</table>

		</td>
	</tr>
</table>
</center>
</body>
</html>