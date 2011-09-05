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

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.webapp.datamodel.MetaDataModel;

public class ODTHelloWorldWithFreemarker extends DefaultReportController {

	public ODTHelloWorldWithFreemarker() {
		super("ODTHelloWorldWithFreemarker.odt", TemplateEngineKind.Freemarker,
				DocumentKind.ODT);
	}

	@Override
	protected MetaDataModel createMetaDataModel() {
		MetaDataModel model = new MetaDataModel();
		model.addSimpleField("name", "world");
		return model;
	}

	@Override
	protected FieldsMetadata createFieldsMetadata() {
		return FieldsMetadata.EMPTY;
	}
}
