/* 
 * TemplatesServiceImpl.java Copyright (C) 2012
 * 
 * This file is part of xdocreport project
 * 
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 * 
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package org.springframework.xdocreport.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.xdocreport.core.TemplatesService;
import org.springframework.xdocreport.core.utils.TemplatesUtils;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * Templates generator based on a directory
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 * @see <a href="http://code.google.com/p/xdocreport/">xdocreport</a>
 */
public class TemplatesServiceImpl implements TemplatesService {

	private static final Log LOG = LogFactory
			.getLog(TemplatesServiceImpl.class);

	/**
	 * Directory when are the templates
	 */
	private String templatesDirectory;

	/**
	 * Default template kind to use (velocity)
	 */
	private TemplateEngineKind defaultTemplateKind = TemplateEngineKind.Velocity;

	/**
	 * Generate a template with values
	 * 
	 * @param values
	 * 
	 * @param template
	 *            to generate
	 * 
	 * @return InputStream with the report generated
	 */
	public InputStream generateTemplate(Map<String, String> values,
			String template) {
		File file = null;
		OutputStream out = null;
		try {
			// 1) Load ODT file and set Velocity template engine and cache it to
			// the registry
			InputStream in = new FileInputStream(new File(templatesDirectory
					+ TemplatesUtils.getDocumentName(template))
					+ TemplatesUtils.PUNTO
					+ TemplatesUtils.getDocumentExtension(template));
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					in, defaultTemplateKind);

			// 2) Create Java model context
			IContext context = report.createContext();
			if (values != null && !values.isEmpty()) {
				for (String key : values.keySet()) {
					context.put(key, values.get(key));
				}
			}

			// 3) Generate report by merging Java model with the template
			file = TemplatesUtils.createFileTemp(template);
			out = new FileOutputStream(file);
			report.process(context, out);
		} catch (Exception e) {
			LOG.error(e);
		}

		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {

			return null;
		}
	}

	public String getTemplatesDirectory() {
		return templatesDirectory;
	}

	public void setTemplatesDirectory(String templatesDirectory) {
		this.templatesDirectory = templatesDirectory;
	}

	public TemplateEngineKind getDefaultTemplateKind() {
		return defaultTemplateKind;
	}

	public void setDefaultTemplateKind(TemplateEngineKind defaultTemplateKind) {
		this.defaultTemplateKind = defaultTemplateKind;
	}

}
