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

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.web.dispatcher.AbstractXDocReportWEBController;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.webapp.ProcessXDocReportServlet;
import fr.opensagres.xdocreport.webapp.WebAppXDocReportConstants;
import fr.opensagres.xdocreport.webapp.datamodel.MetaDataModel;

public abstract class DefaultReportController extends
		AbstractXDocReportWEBController implements WebAppXDocReportConstants {

	private MetaDataModel dataModel;
	private final String reportId;

	public DefaultReportController(String reportId,
			TemplateEngineKind templateEngineKind,
			DocumentKind converterTypeFrom) {
		super(templateEngineKind, converterTypeFrom);
		this.reportId = reportId;
	}

	public String getReportId() {
		return reportId;
	}

	public MetaDataModel getMetaDataModel() {
		if (dataModel == null) {
			dataModel = createMetaDataModel();
		}
		return dataModel;
	}

	public InputStream getSourceStream() {
		return ProcessXDocReportServlet.class
				.getResourceAsStream(getReportId());
	}

	public void populateContext(IContext context, IXDocReport report,
			HttpServletRequest request) {
		MetaDataModel dataModelForm = report.getData(DATA_MODEL_REPORT_KEY);
		if (dataModelForm == null) {
			// If report was not already loaded (default report) , create
			// default data model
			dataModelForm = getMetaDataModel();
			if (dataModelForm != null) {
				// It's a default report, store the data model in the report
				report.setData(DATA_MODEL_REPORT_KEY, dataModelForm);
			}
		}
		if (dataModelForm != null) {
			dataModelForm.populateContext(context, request);
		}

	}

	protected abstract MetaDataModel createMetaDataModel();
}
