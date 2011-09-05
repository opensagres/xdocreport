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
import fr.opensagres.xdocreport.webapp.datamodel.MetaDataModelListField;

public class ODTCV extends DefaultReportController {

	public ODTCV() {
		super("ODTCV.odt", TemplateEngineKind.Velocity, DocumentKind.ODT);
	}

	@Override
	protected MetaDataModel createMetaDataModel() {
		MetaDataModel model = new MetaDataModel();
		model.addSimpleField("user.name", "ZERR");
		model.addSimpleField("user.lastName", "Angelo");
		model.addSimpleField("user.function", "Ingénieur JEE – Eclipse RCP");
		model.addSimpleField("user.experienceYear", "10");

		MetaDataModelListField formations = model.addListField("formations");
		formations.addSimpleField("year", "2001");
		formations.addSimpleField("description", "Diplôme d’ingénieur en informatique à l’INSA de Lyon (69).");		
		
		MetaDataModelListField references = model.addListField("refs");
		references.addSimpleField("date", "2009");
		references
				.addSimpleField(
						"description",
						"Commiteur Eclipse depuis janvier 2009. Participe à la future version de l'IDE Eclipse E4 (IBM).  Créateur du moteur CSS de E4.");

		MetaDataModelListField languages = model.addListField("langs");
		languages.addSimpleField("name", "Anglais");
		languages.addSimpleField("level", "Niveau Technique");

		MetaDataModelListField experiences = model.addListField("experiences");
		experiences.addSimpleField("date", "Avril 2009 - Aujourd'hui");
		experiences.addSimpleField("client", "CAF");
		experiences.addSimpleField("project", "Projet SIDoc");
		experiences.addSimpleField("mission", "Conception / Développement");
		experiences
				.addSimpleField("envTech",
						"Java JVM Sun (JDK 6), JEE\nbase XML X-DB, XQuery, XSL.\nAjax (Dojo Toolkit)");
		experiences.addSimpleField("envFonc", "Gestion documentaire.");
		experiences
				.addSimpleField(
						"detail",
						"Mise en place de l'application WEB de diffusion (qui sera accéssible dans les accueils des CAF) qui permet de publier les documents XML produits par l'application WEB de production.");

		return model;
	}

	@Override
	protected FieldsMetadata createFieldsMetadata() {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addFieldAsList("refs.date");
		fieldsMetadata.addFieldAsList("refs.description");
		fieldsMetadata.addFieldAsList("langs.name");
		fieldsMetadata.addFieldAsList("langs.level");
		return fieldsMetadata;
	}

}
