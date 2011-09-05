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
 * <pre>
 * Hello $name!
 * </pre>
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
		} else {
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
