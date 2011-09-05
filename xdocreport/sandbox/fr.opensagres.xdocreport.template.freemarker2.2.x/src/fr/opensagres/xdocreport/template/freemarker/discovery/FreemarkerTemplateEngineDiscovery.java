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
package fr.opensagres.xdocreport.template.freemarker.discovery;

import fr.opensagres.xdocreport.core.document.loader.discovery.ITemplateEngineDiscovery;
import fr.opensagres.xdocreport.core.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerConstants;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;

/**
 * Freemarker template engine discovery used to returns
 * {@link FreemarkerTemplateEngine}.
 * 
 */
public class FreemarkerTemplateEngineDiscovery implements
		ITemplateEngineDiscovery, FreemarkerConstants {

	public ITemplateEngine getTemplateEngine() {
		return FreemarkerTemplateEngine.getDefault();
	}

	public String getDescription() {
		return DESCRIPTION_DISCOVERY;
	}

	public String getId() {
		return ID_DISCOVERY;
	}
}
