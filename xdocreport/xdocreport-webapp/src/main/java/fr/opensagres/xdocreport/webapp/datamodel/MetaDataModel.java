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

import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;

public class MetaDataModel extends FieldsExtractor<MetaDataModelField> {

	public void toHTML(Writer writer, HttpServletRequest request, int size,
			boolean useDefaultValue, boolean preview) throws IOException {
		for (MetaDataModelField field : super.getFields()) {
			writer.write("<tr>");
			field.toHTML(writer, request, useDefaultValue, true, preview);
			writer.write("</tr>");
		}
	}

	public MetaDataModelSimpleField addSimpleField(String fieldName,
			String defaultValue) {
		return addSimpleField(fieldName, defaultValue, fieldName);
	}

	public MetaDataModelSimpleField addSimpleField(String fieldName,
			String defaultValue, String fieldLabel) {
		MetaDataModelField field = super.addFieldName(fieldName);
		if (field != null) {
			field.setLabel(fieldLabel);
			field.setDefaultValue(defaultValue);
		}
		return (MetaDataModelSimpleField)field;
	}

	public void populateContext(IContext context, HttpServletRequest request) {
		for (MetaDataModelField field : super.getFields()) {
			field.populateContext(context, request);
		}
	}

	@Override
	protected MetaDataModelField createField(String fieldName) {
		return new MetaDataModelSimpleField(fieldName, fieldName + "_Value",
				fieldName);
	}

	public MetaDataModelListField addListField(String fieldName) {
		MetaDataModelListField list = new MetaDataModelListField(fieldName);
		list.setLabel(fieldName);
		super.getFields().add(list);
		return list;
	}
}
