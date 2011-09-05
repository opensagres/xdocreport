/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.template.velocity;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import fr.opensagres.xdocreport.core.EncodingConstants;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.AbstractTemplateEngine;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.config.ITemplateEngineConfiguration;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.cache.XDocReportEntryResourceLoader;
import fr.opensagres.xdocreport.template.velocity.internal.ExtractVariablesVelocityVisitor;
import fr.opensagres.xdocreport.template.velocity.internal.XDocReportEscapeReference;
import fr.opensagres.xdocreport.template.velocity.internal.XDocVelocityContext;

/**
 * Velocity template engine implementation.
 * 
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine implements
		VelocityConstants {

	private static String ID = "Velocity";

	private VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
	private VelocityEngine velocityEngine;

	public String getKind() {
		return TemplateEngineKind.Velocity.name();
	}

	public String getId() {
		return ID;
	}

	public IContext createContext() {
		return new XDocVelocityContext();
	}

	@Override
	protected void processWithCache(String templateName, IContext context,
			Writer writer)
			throws XDocReportException, IOException {
		VelocityEngine velocityEngine = getVelocityEngine();
		Template template = velocityEngine.getTemplate(templateName,
				EncodingConstants.UTF_8.name());
		if (template != null) {
			template.merge((VelocityContext) context, writer);
		}
	}

	@Override
	protected void processNoCache(String entryName, IContext context,
			Reader reader, Writer writer)
			throws XDocReportException, IOException {
		VelocityEngine velocityEngine = getVelocityEngine();
		velocityEngine.evaluate((VelocityContext) context, writer, "", reader);
	}

	protected synchronized VelocityEngine getVelocityEngine()
			throws XDocReportException {
		if (velocityEngine == null) {
			velocityEngine = new VelocityEngine();
			Properties p = new Properties();
			// Initialize properties to use XDocReportEntryResourceLoader to
			// load template from entry name of XDocArchive.
			p.setProperty("resource.loader", "file, class, jar ,report");
			p.setProperty("report.resource.loader.class",
					XDocReportEntryResourceLoader.class.getName());
			p.setProperty("report.resource.loader.cache", "true");
			p.setProperty("report.resource.loader.modificationCheckInterval",
					"1");

			ITemplateEngineConfiguration configuration = super
					.getConfiguration();
			if (configuration != null && configuration.escapeXML()) {
				p.setProperty("eventhandler.referenceinsertion.class",
						XDocReportEscapeReference.class.getName());
			}
			try {
				velocityEngine.setProperty(VELOCITY_TEMPLATE_ENGINE_KEY, this);
				velocityEngine.init(p);
			} catch (Exception e) {
				throw new XDocReportException(e);
			}
		}
		return velocityEngine;
	}

	public IDocumentFormatter getDocumentFormatter() {
		return formatter;
	}

	public void extractFields(Reader reader, String entryName,
			FieldsExtractor extractor) throws XDocReportException {
		try {
			SimpleNode document = RuntimeSingleton.parse(reader, entryName);
			ExtractVariablesVelocityVisitor visitor = new ExtractVariablesVelocityVisitor(
					extractor);
			visitor.setContext(null);
			// visitor.setWriter(new PrintWriter(System.out));
			document.jjtAccept(visitor, null);

		} catch (ParseException e) {
			throw new XDocReportException(e);
		}
	}
}
