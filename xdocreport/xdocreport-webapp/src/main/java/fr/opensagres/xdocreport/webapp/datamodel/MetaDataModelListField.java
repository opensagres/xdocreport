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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.internal.DynamicBean;
import fr.opensagres.xdocreport.webapp.utils.StringEscapeUtils;

public class MetaDataModelListField extends MetaDataModelField {

	private List<MetaDataModelSimpleField> fields = new ArrayList<MetaDataModelSimpleField>();
	private String defaultValue;
	private String label;

	public MetaDataModelListField(String name) {
		super(name);
	}

	public String getLabel() {
		return label;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void populateContext(IContext context, HttpServletRequest request) {

		List<DynamicBean> dataModelList = new ArrayList<DynamicBean>();
		for (MetaDataModelSimpleField field : fields) {
			String name = field.getName();
			String[] values = request.getParameterValues(name);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					DynamicBean bean = null;
					if (dataModelList.size() >= i + 1) {
						bean = dataModelList.get(i);
					} else {
						bean = new DynamicBean();
						dataModelList.add(bean);
					}
					String value = values[i];
					String key = name.substring(super.getName().length() + 1,
							name.length());
					bean.setValue(key.split("[.]"), value, 0);
				}
			}
		}
		context.put(super.getName(), dataModelList);
	}

	@Override
	public void toHTML(Writer writer, HttpServletRequest request,
			boolean useDefaultValue, boolean showLabel, boolean preview)
			throws IOException {
		String reportId = getReportId(request);
		String converter = getConverter(request);		
		String tableId = super.getName() + "Table";
		
		writer.write("<td valign=\"top\" >");
		writer.write(label);
		writer.write("</td>");
		writer.write("<td>");
		writer.write("<fieldset><legend>");
		writer.write(label);
		writer.write(" list</legend>");
		writer.write("<table width=\"100%\" ");
		writer.write("id=\"");
		writer.write(tableId);
		writer.write("\" >");

		writer.write("<tr>");
		writer.write("<td colspan=\"");
		writer.write(String.valueOf(fields.size()));
		writer.write("\" >");
		writer.write("<a href=\"javascript:add('");		
		writeBaseURL(writer, request, reportId, converter);
		writer.write("','");
		writer.write(tableId);
		writer.write("');\">Add</a>");
		writer.write(" <a href=\"javascript:remove('");
		writeBaseURL(writer, request, reportId, converter);
		writer.write("','");
		writer.write(tableId);
		writer.write("');\">Remove</a>");
		writer.write("</tr>");

		writer.write("<tr>");
		for (MetaDataModelSimpleField field : fields) {
			String name = field.getName();
			writer.write("<th>");
			writer.write(name);
			writer.write("</th>");
		}
		writer.write("</tr>");

		writer.write("<tr>");
		for (MetaDataModelSimpleField field : fields) {
			field.toHTML(writer, request, useDefaultValue, false, preview);
		}
		writer.write("</tr>");

		writer.write("</table>");
		writer.write("</fieldset>");
		writer.write("</td>");
	}

	public void toURLParameter(Writer writer, ServletRequest request,
			boolean useDefaultValue) throws IOException {
		writer.write(super.getName());
		writer.write("=");
		String value = useDefaultValue ? defaultValue : request
				.getParameter(super.getName());
		if (StringUtils.isEmpty(value)) {
			value = defaultValue;
		}
		writer.write(value);
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = StringEscapeUtils.escapeHtml(defaultValue);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public MetaDataModelSimpleField addSimpleField(String fieldName,
			String fieldValue) {
		fieldName = super.getName() + "." + fieldName;
		MetaDataModelSimpleField field = addFieldName(fieldName);
		if (field != null) {
			field.setLabel(fieldName);
			field.setDefaultValue(fieldValue);
		}
		return field;

	}

	private MetaDataModelSimpleField addFieldName(String fieldName) {
		MetaDataModelSimpleField field = new MetaDataModelSimpleField(
				fieldName, "", fieldName);
		fields.add(field);
		return field;
	}
}
