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
import fr.opensagres.xdocreport.webapp.utils.DateUtils;

public class ODTLettreRelance extends DefaultReportController {

	public ODTLettreRelance() {
		super("ODTLettreRelance.odt", TemplateEngineKind.Velocity,
				DocumentKind.ODT);
	}

	@Override
	protected MetaDataModel createMetaDataModel() {
		MetaDataModel model = new MetaDataModel();

		// User
		model.addSimpleField("user.name", "ZERR");
		model.addSimpleField("user.lastName", "Angelo");

		// User Adress
		model.addSimpleField("user.adress.street", "6 rue de l'espace");
		model.addSimpleField("user.adress.codePostal", "59000");
		model.addSimpleField("user.adress.city", "Villeneuve d'Ascq");

		// Company
		model.addSimpleField("company.name", "OpenSagres");

		// Company Adress
		model.addSimpleField("company.adress.street", "4 rue de la liberté");
		model.addSimpleField("company.adress.codePostal", "59000");
		model.addSimpleField("company.adress.city", "Lille");

		// Date
		model.addSimpleField("date", DateUtils.formatDate(DateUtils.getToday()));

		// Commands list
		MetaDataModelListField listField = model.addListField("cds");
		listField.addSimpleField("reference", "95856512568");
		listField.addSimpleField("description", "Tournevis");
		listField.addSimpleField("prixUnit", "20.15 €");
		listField.addSimpleField("quantite", "2");
		listField.addSimpleField("prixTotal", "40.30 €");
		
		return model;
	}
	
	@Override
	protected FieldsMetadata createFieldsMetadata() {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		fieldsMetadata.addFieldAsList("cds.reference");
		fieldsMetadata.addFieldAsList("cds.description");
		fieldsMetadata.addFieldAsList("cds.prixUnit");
		fieldsMetadata.addFieldAsList("cds.quantite");
		fieldsMetadata.addFieldAsList("cds.prixTotal");
		return fieldsMetadata;
	}

}
