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
<title>XDocReport - HelloWorld - Dispatcher</title>
<link href="styles/xdocreport.css" rel="stylesheet" type="text/css" />
</head>
<body>
<a href="<%=HTMLUtils.generateIndexJSPURL(request)%>">&lt;&lt; Home</a>

<fieldset><legend>Hello world - Dispatcher</legend>
<form name="helloWorldForm"
	action="<%=request.getContextPath()%>/helloworldDispatcher?reportId=ODTHelloWorldWithVelocity"
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
	<li>HelloWorldControler : <pre>
package fr.opensagres.xdocreport.webapp;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.web.dispatcher.AbstractXDocReportWEBControler;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * Hello world sample controler which work with ODTHelloWorldWithVelocity.odt ODT document
 * which contains content with velocity variable name :
 * 
 * &lt;pre&gt;
 * Hello $name!
 * &lt;/pre&gt;
 * 
 */
public class HelloWorldControler extends AbstractXDocReportWEBControler {

	public static final String REPORT_ID = "ODTHelloWorldWithVelocity";

	public HelloWorldControler() {
		super(TemplateEngineKind.Velocity, DocumentKind.ODT);
	}

	public InputStream getSourceStream() {
		return HelloWorldControler.class
		.getResourceAsStream("ODTHelloWorldWithVelocity.odt");
	}
	
	@Override
	protected FieldsMetadata createFieldsMetadata() {		
		return null;
	}
	
	public void populateContext(IContext context, IXDocReport report,
			HttpServletRequest request) {
		String name = request.getParameter("name");
		context.put("name", name);		
	}
}
</pre></li>
	<li>HelloWorldDispatcher : <pre>
package fr.opensagres.xdocreport.webapp;

import fr.opensagres.xdocreport.document.dispatcher.BasicXDocReportDispatcher;
import fr.opensagres.xdocreport.document.web.dispatcher.IXDocReportWEBControler;

public class HelloWorldDispatcher extends
		BasicXDocReportDispatcher&lt;IXDocReportWEBControler&gt; {

	public HelloWorldDispatcher() {
		super.register(HelloWorldControler.REPORT_ID, new HelloWorldControler());
	}
}	
	</pre></li>
	<li>web.xml declare servlet and set the dispatcher HelloWorldDispatcher to use : <pre>
	&lt;!-- Servlet used to generate report with ODTHelloWorldWithVelocity.odt --&gt;
	&lt;servlet&gt;
		&lt;servlet-name&gt;HelloWorldDispatcherReportServlet&lt;/servlet-name&gt;
		&lt;servlet-class&gt;
			fr.opensagres.xdocreport.document.web.ProcessDispatcherXDocReportServlet&lt;/servlet-class&gt;
		&lt;init-param&gt;
			&lt;param-name&lt;dispatchers&lt;/param-name&gt;
			&lt;param-value&gt;fr.opensagres.xdocreport.webapp.HelloWorldDispatcher&lt;/param-value&gt;
		&lt;/init-param&gt;			
	&lt;/servlet&gt;
	
	&lt;servlet-mapping&gt;
		&lt;servlet-name&gt;HelloWorldDispatcherReportServlet&lt;/servlet-name&gt;
		&lt;url-pattern&gt;/helloworldDispatcher/*&lt;/url-pattern&gt;
	&lt;/servlet-mapping&gt;
</pre></li>

	<li>Form HTML : <pre>&lt;form action="<%=request.getContextPath()%>/helloworldDispatcher?reportId=ODTHelloWorldWithVelocity"
	method="post"&gt;
	&lt;input type="submit" value="Generate Report"&gt;
	&lt;input type="text" name="name" value="world" /&gt;
&lt;/form&gt;</pre></li>
</ol>
</fieldset>
</body>
</html>