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
<%@page import="fr.opensagres.xdocreport.document.IXDocReport"%>
<%@page import="java.util.Collection"%>
<%@page
	import="fr.opensagres.xdocreport.document.registry.XDocReportRegistry"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="fr.opensagres.xdocreport.webapp.utils.HTMLUtils"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>XDocReport - HelloWorld</title>
<link href="styles/xdocreport.css" rel="stylesheet" type="text/css" />
</head>
<body>
<a href="<%=HTMLUtils.generateIndexJSPURL(request)%>">&lt;&lt; Home</a>

<fieldset><legend>Hello world</legend>
<form name="helloWorldForm"
	action="<%=request.getContextPath()%>/helloworld?reportId=ODTHelloWorldWithVelocity"
	method="post">
<table>
	<tr>
		<td>Name :</td>
		<td><input type="text" name="name" value="world" /></td>
	</tr>
	<tr>
		<td colspan="2"><input type="submit" value="Generate ODT Report"></td>
	</tr>
</table>
</form>
</fieldset>

<fieldset><legend>Explanation</legend>

<ol>
	<li>Servlet : <pre>/*******************************************************************************
package fr.opensagres.xdocreport.webapp;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.XDocReportNotFoundException;
import fr.opensagres.xdocreport.document.web.AbstractProcessXDocReportServlet;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * Hello world sample wich works ODTHelloWorldWithVelocity.odt ODT document
 * which contains content with velocity variable name :
 * 
 * &lt;pre&gt;
 * Hello $name!
 * &lt;/pre&gt;
 * 
 */
public class HelloWorldReportServlet extends AbstractProcessXDocReportServlet {

	private static final long serialVersionUID = 3993221341284875152L;
	private static final String ODT_HELLO_WORD_WITH_VELOCITY = "ODTHelloWorldWithVelocity";

	@Override
	protected InputStream getSourceStream(String reportId,
			HttpServletRequest request) throws IOException, XDocReportException {
		if (ODT_HELLO_WORD_WITH_VELOCITY.equals(reportId)) {
			// reportId = ODTHelloWorldWithVelocity, returns stream of the ODT
			return HelloWorldReportServlet.class
					.getResourceAsStream("ODTHelloWorldWithVelocity.odt");
		}
		throw new XDocReportNotFoundException(reportId);
	}

	@Override
	protected void populateContext(IContext context, String reportId,
			HttpServletRequest req) throws XDocReportException {
		if (reportId.equals(ODT_HELLO_WORD_WITH_VELOCITY)) {
			// reportId = ODTHelloWorldWithVelocity, prepare context with name
			String name = req.getParameter("name");
			context.put("name", name);
		}
		else {
			throw new XDocReportNotFoundException(reportId);
		}
	}

	@Override
	protected String getTemplateEngineKind(String reportId,
			HttpServletRequest request) {
		if (ODT_HELLO_WORD_WITH_VELOCITY.equals(reportId)) {
			return TemplateEngineKind.Velocity.name();
		}
		return super.getTemplateEngineKind(reportId, request);
	}

}
</pre></li>
	<li>web.xml <pre>
	&lt;!-- Servlet used to generate report with ODTHelloWorldWithVelocity.odt --&gt;
	&lt;servlet&gt;
		&lt;servlet-name&gt;HelloWorldReportServlet&lt;/servlet-name&gt;
		&lt;servlet-class&gt;
			fr.opensagres.xdocreport.webapp.HelloWorldReportServlet&lt;/servlet-class&gt;
	&lt;/servlet&gt;
	
	&lt;servlet-mapping&gt;
		&lt;servlet-name&gt;HelloWorldReportServlet&lt;/servlet-name&gt;
		&lt;url-pattern&gt;/helloworld/*&lt;/url-pattern&gt;
	&lt;/servlet-mapping&gt;
</pre></li>

	<li>Form HTML : <pre>&lt;form action="<%=request.getContextPath()%>/helloworld?reportId=ODTHelloWorldWithVelocity"
	method="post"&gt;
	&lt;input type="submit" value="Launch Report generation"&gt;
	&lt;input type="text" name="name" value="world" /&gt;
&lt;/form&gt;</pre></li>
</ol>
</fieldset>
</body>
</html>