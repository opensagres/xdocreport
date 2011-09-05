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
package fr.opensagres.xdocreport.webapp.defaultreport;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import javax.servlet.ServletRequest;

import fr.opensagres.xdocreport.document.dispatcher.BasicXDocReportDispatcher;
import fr.opensagres.xdocreport.webapp.datamodel.MetaDataModel;
import fr.opensagres.xdocreport.webapp.utils.HTMLUtils;

public class DefaultReportRegistry extends
		BasicXDocReportDispatcher<DefaultReportController> {

	public static DefaultReportRegistry INSTANCE = new DefaultReportRegistry();

	public DefaultReportRegistry() {
		register(new DocXHelloWorldWithVelocity());
		register(new DocXHelloWorldWithFreemarker());
		register(new DocxLettreRelance());
		register(new DocxCV());
		register(new DocxStructures());
		register(new DocxBig());
		register(new ODTHelloWorldWithVelocity());
		register(new ODTHelloWorldWithFreemarker());
		register(new ODTProjectWithVelocity());
		register(new ODTProjectWithFreemarker());
		register(new ODTStructures());
		register(new ODTLettreRelance());
		register(new ODTCV());
		register(new ODTBig());
	}

	private void register(DefaultReportController controler) {
		super.register(controler.getReportId(), controler);
	}

	public MetaDataModel getMetaDataModel(String reportId) {
		DefaultReportController defaultReport = getReportController(reportId);
		if (defaultReport != null) {
			return defaultReport.getMetaDataModel();
		}
		return null;
	}

	public void toHTMLOptions(ServletRequest request, Writer writer)
			throws IOException {
		Collection<DefaultReportController> reports = super.getControllers();
		for (DefaultReportController defaultReport : reports) {
			HTMLUtils.generateHTMLOption(defaultReport.getReportId(), request,
					writer, "reportId");
		}
	}

	public Collection<DefaultReportController> getDefaultReports() {
		return super.getControllers();
	}

	public String getConverterTypeFrom(String reportId) {
		DefaultReportController controler = getReportController(reportId);
		if (controler != null) {
			return controler.getConverterTypeFrom();
		}
		return null;
	}

}
