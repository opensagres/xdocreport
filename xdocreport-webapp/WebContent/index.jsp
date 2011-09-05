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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="fr.opensagres.xdocreport.webapp.utils.HTMLUtils"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>XDocReport - Home</title>
</head>
<link href="styles/xdocreport.css" rel="stylesheet" type="text/css" />
<body>

<center>
<table width="700px" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<p>Welcome to <a href="http://code.google.com/p/xdocreport/">XDocReport</a>
		WEB Application. This application give you the capability to upload
		odt, docx... document which use <a
			href="http://freemarker.sourceforge.net/">Freemarker</a>, <a
			href="http://velocity.apache.org/">Velocity</a> syntax and merge it
		with Java objects to generate report. You can use too XDocReport to
		convert odt, docx... document to FO, PDF...</p>
		<p>
		<ul>
			<li><a
				href="<%=HTMLUtils.generateProcessReportJSPURL(request)%>">Process
			report</a> to generate report by using odt, docx document source and
			convert it to FO, PDF...</li>
			<li><a href="<%=HTMLUtils.generateLoadReportJSPURL(request)%>">Load
			report</a> to upload your odt, docx... document and configure (easily)
			data model waited by the report.</li>
			<li><a href="<%=HTMLUtils.generateAdminJSPURL(request)%>">XDoc
			Report Administration.</a> show you XDocReport configuration (available
			document report, template engine, converter and cached reports).</li>
			<li><a
				href="<%=HTMLUtils.generateDocumentArchiveJSPURL(request)%>">Document
			archive</a> unzip the document archive (odt, docx...) and show the
			hierarchy of the archive with links to download content of each
			entries.
			<li><a href="<%=HTMLUtils.generateHelloWorldJSPURL(request)%>">Hello
			world!</a> sample explains how generate odt report by using odt file
			source. This source document contains "Hello $name!" and use Velocity
			syntax to replace $name field and generate odt report.</li>
			<li><a href="<%=HTMLUtils.generateHelloWorldDispatcherJSPURL(request)%>">Hello
			world! (with Dispatcher/Controler)</a> sample explains how generate odt report by using odt file
			source. This source document contains "Hello $name!" and use Velocity
			syntax to replace $name field and generate odt report.</li>
		</ul>
		</p>
		<p>Please visit <a href="http://code.google.com/p/xdocreport/">http://code.google.com/p/xdocreport/</a>
		for more information.</p>
		</td>
	</tr>
</table>
</center>
</body>
</html>