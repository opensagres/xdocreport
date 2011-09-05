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
<%@page import="java.util.Set"%>
<%@page import="fr.opensagres.xdocreport.core.io.XDocArchive"%>
<%@page
	import="fr.opensagres.xdocreport.document.registry.XDocReportRegistry"%>
<%@page import="fr.opensagres.xdocreport.document.IXDocReport"%>
<%@page import="java.util.Collection"%>
<%@page
	import="fr.opensagres.xdocreport.webapp.defaultreport.DefaultReportRegistry"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="fr.opensagres.xdocreport.webapp.utils.HTMLUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>XDocReport - Document archive</title>
<link href="styles/xdocreport.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="scripts/xdocreport.js"></script>
</head>
<body>
<center>
<%
	Boolean preprocessed = (Boolean) request
			.getAttribute("Preprocessed");
	XDocArchive archive = (XDocArchive) request
			.getAttribute("XDocArchive");
%>
<table width="700px" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><a href="<%=HTMLUtils.generateIndexJSPURL(request)%>">&lt;&lt;
		Home</a>

		<fieldset><legend>Document archive</legend>
		<form name="sampleXDocReportForm"
			action="<%=request.getContextPath()%>/processReport" method="post"
			target="_blank"><input type="hidden" name="dispatch"
			id="dispatch" />
		<table width="100%">
			<tr>
				<td>
				<table>
					<tr>
						<td colspan="2">
						<p>odt, docx...file are a zip composed with several entries.
						This page give you the capability to show entries of the document
						archive (odt, docx...) and download it.
						<p>Choose an existing report to see the entries of the
						document archive. You can too <strong><a
							href="<%=HTMLUtils.generateLoadReportJSPURL(request)%>">Load
						report</a></strong> your own odt, docx... document.</p>
						</td>
					</tr>
					<tr>
						<td valign="top">
						<table>
							<tr>
								<td valign="top">
								<fieldset><legend>Choose a report</legend><select
									name="reportId"
									onchange="javascript:submitForm('entries', '_self');" size="16">
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
								<td valign="top" width="100%">
								<fieldset><legend>Entries</legend>
								<table>
									<thead>
										<tr>
											<th>Entry name</th>
											<th>Original</th>
											<th>Preprocessed</th>
										</tr>
									</thead>
									<tbody>
										<%
											if (archive == null) {
										%>
										<tr>
											<td colspan="2">Please select a document.</td>
										</tr>
										<%
											} else {

												Set<String> entryNames = archive.getEntryNames();
												for (String entryName : entryNames) {
										%>
										<tr>
											<td><%=entryName%></td>
											<td><a
												href="<%=HTMLUtils
							.generateProcessReportURLViewEntryDispatch(request,
									request.getParameter("reportId"),
									entryName, "original")%>"
												target="_blank">View</a> / <a
												href="<%=HTMLUtils
							.generateProcessReportURLDownloadEntryDispatch(
									request, request.getParameter("reportId"),
									entryName, "original")%>"
												target="_blank">Download</a></td>


											<td>
											<%
												if (preprocessed) {
											%> <a
												href="<%=HTMLUtils
								.generateProcessReportURLViewEntryDispatch(
										request,
										request.getParameter("reportId"),
										entryName, "preprocessed")%>"
												target="_blank">View</a> / <a
												href="<%=HTMLUtils
								.generateProcessReportURLDownloadEntryDispatch(
										request,
										request.getParameter("reportId"),
										entryName, "preprocessed")%>"
												target="_blank">Download</a> <%
 	}
 %>
											</td>
										</tr>
										<%
											}
											}
										%>
									</tbody>
								</table>
								</fieldset>
								</td>
							</tr>

							</tbody>
						</table>

						</td>
					</tr>
					<tr>
				</table>
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