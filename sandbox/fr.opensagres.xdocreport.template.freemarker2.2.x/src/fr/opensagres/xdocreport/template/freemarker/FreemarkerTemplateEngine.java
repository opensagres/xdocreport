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
package fr.opensagres.xdocreport.template.freemarker;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import fr.opensagres.xdocreport.core.EncodingConstants;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.debug.ITemplateEngineDebugger;
import fr.opensagres.xdocreport.core.fields.IFieldFormater;
import fr.opensagres.xdocreport.core.template.AbstractTemplateEngine;
import fr.opensagres.xdocreport.core.template.IContext;
import fr.opensagres.xdocreport.template.freemarker.cache.XDocReportEntryTemplateLoader;
import fr.opensagres.xdocreport.template.freemarker.internal.XDocFreemarkerContext;
import freemarker.template.Configuration;
import freemarker.template.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Freemarker template engine implementation.
 * 
 */
public class FreemarkerTemplateEngine extends AbstractTemplateEngine implements FreemarkerConstants {

	private static final FreemarkerTemplateEngine INSTANCE = new FreemarkerTemplateEngine();

	private static Configuration DEFAULT_FREEMARKER_CONFIGURATION = null;

	private Configuration freemarkerConfiguration = null;

	public static FreemarkerTemplateEngine getDefault() {
		return INSTANCE;
	}

	public String getId() {
		return ID_DISCOVERY;
	}

	public IContext createContext() {
		return new XDocFreemarkerContext();
	}

	@Override
	protected void processWithCache(String templateName, IContext context,
			Writer writer, ITemplateEngineDebugger debugger)
			throws XDocReportException, IOException {
		// Get template from cache.
		Template template = getFreemarkerConfiguration().getTemplate(
				templateName);
		// Merge template with Java model
		process(context, writer, template);
	}

	protected void processNoCache(String entryName, IContext context,
			Reader reader, Writer writer, ITemplateEngineDebugger debugger)
			throws XDocReportException, IOException {
		// Create a new template.
		Template template = new Template(entryName, reader,
				getFreemarkerConfiguration());
		// Merge template with Java model
		process(context, writer, template);
	}

	/**
	 * Merge template with Java model.
	 * 
	 * @param context
	 * @param writer
	 * @param template
	 * @throws IOException
	 * @throws XDocReportException
	 */
	private void process(IContext context, Writer writer, Template template)
			throws IOException, XDocReportException {
		try {
			Environment environment = template.createProcessingEnvironment(
					context, writer);
			environment.process();
		} catch (TemplateException e) {
			throw new XDocReportException(e);
		}
	}

	public Configuration getFreemarkerConfiguration() {
		if (freemarkerConfiguration == null) {
			return getDefaultConfiguration();
		}
		return freemarkerConfiguration;
	}

	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
		this.freemarkerConfiguration = freemarkerConfiguration;
		// Force square bracket syntaxt to write [#list instead of <#list.
		// Square bracket is used because <#list is not well XML.
		// this.freemarkerConfiguration
		// .setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		this.freemarkerConfiguration
		// Force template loader with XDocReportEntryLoader to use
				// XDocReportRegistry.
				.setTemplateLoader(XDocReportEntryTemplateLoader.INSTANCE);
		// as soon as report changes when source (odt, docx,...) change,
		// template entry must be refreshed.
		try {
			this.freemarkerConfiguration.setSetting(
					Configuration.TEMPLATE_UPDATE_DELAY_KEY, "0");
		} catch (TemplateException e) {
		}
		this.freemarkerConfiguration.setLocalizedLookup(false);
	}

	public IFieldFormater getFieldFormater() {
		return FreemarkerFieldFormater.INSTANCE;
	}

	/**
	 * Get the default Freemarker configuration
	 * 
	 * @return
	 */
	private Configuration getDefaultConfiguration() {
		if (DEFAULT_FREEMARKER_CONFIGURATION == null) {
			DEFAULT_FREEMARKER_CONFIGURATION = new Configuration();
			DEFAULT_FREEMARKER_CONFIGURATION
					.setDefaultEncoding(EncodingConstants.UTF_8.name());
			// DEFAULT_FREEMARKER_CONFIGURATION
			// .setOutputEncoding(EncodingConstants.UTF_8.name());
			// DEFAULT_FREEMARKER_CONFIGURATION.setObjectWrapper(new
			// DefaultObjectWrapper());
			setFreemarkerConfiguration(DEFAULT_FREEMARKER_CONFIGURATION);
		}
		return DEFAULT_FREEMARKER_CONFIGURATION;
	}
}
