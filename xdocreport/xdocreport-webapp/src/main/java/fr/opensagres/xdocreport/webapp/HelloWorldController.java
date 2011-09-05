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

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.web.dispatcher.AbstractXDocReportWEBController;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class HelloWorldController extends AbstractXDocReportWEBController {

	public static final String REPORT_ID = "ODTHelloWorldWithVelocity";

	public HelloWorldController() {
		super(TemplateEngineKind.Velocity, DocumentKind.ODT);
	}

	public InputStream getSourceStream() {
		return HelloWorldController.class
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
