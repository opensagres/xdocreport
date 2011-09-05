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
package fr.opensagres.xdocreport.webapp.datamodel;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.webapp.utils.HTMLUtils;
import fr.opensagres.xdocreport.webapp.utils.StringEscapeUtils;

public abstract class MetaDataModelField extends FieldExtractor {

	private String defaultValue;
	private String label;

	public MetaDataModelField(String name) {
		super(name);
	}

	public String getLabel() {
		return label;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = StringEscapeUtils.escapeHtml(defaultValue);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	protected void writeBaseURL(Writer writer, HttpServletRequest request,
			String reportId, String converter) throws IOException {
		writer.write(HTMLUtils.generateProcessReportURL(request, reportId,
				null, null, "view"));
		writer.write("&converter=");
		writer.write(converter);
	}

	protected String getConverter(HttpServletRequest request) {
		return request.getParameter("converter");
	}

	protected String getReportId(HttpServletRequest request) {
		return request.getParameter("reportId");
	}

	public abstract void populateContext(IContext context,
			HttpServletRequest request);

	public abstract void toHTML(Writer writer, HttpServletRequest request,
			boolean useDefaultValue, boolean showLabel, boolean preview)
			throws IOException;

}
