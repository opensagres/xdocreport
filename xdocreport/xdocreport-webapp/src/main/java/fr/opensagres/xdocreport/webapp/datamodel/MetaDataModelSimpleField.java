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

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.webapp.utils.StringEscapeUtils;

public class MetaDataModelSimpleField extends MetaDataModelField {

	private String defaultValue;
	private String label;

	public MetaDataModelSimpleField(String name, String defaultValue,
			String label) {
		super(name);
		setDefaultValue(defaultValue);
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void populateContext(IContext context, HttpServletRequest request) {
		String value = request.getParameter(super.getName());
		if (value != null) {
			context.put(super.getName(), value);
		}
	}

	public void toHTML(Writer writer, HttpServletRequest request,
			boolean useDefaultValue, boolean showLabel, boolean preview)
			throws IOException {
		if (showLabel) {
			writer.write("<td>");
			writer.write(label);
			writer.write("</td>");
		}
		writer.write("<td>");
		writer.write("<textarea  name=\"");
		writer.write(super.getName());
		writer.write("\"");
		String value = useDefaultValue ? defaultValue : request
				.getParameter(super.getName());
		if (StringUtils.isEmpty(value)) {
			value = defaultValue;
		}

		String converter = getConverter(request);
		if (preview && StringUtils.isNotEmpty(converter)) {
			String reportId = getReportId(request);
			writer.write(" onkeyup=\"javascript:processReport('");
			writeBaseURL(writer, request, reportId, converter);
			writer.write("', this);\" ");
		}
		writer.write(" oldValue=\"");
		writer.write(value);
		writer.write("\" ");

		writer.write(">");
		writer.write(value);
		writer.write("</textarea></td>");
		
		
		
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = StringEscapeUtils.escapeHtml(defaultValue);
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
