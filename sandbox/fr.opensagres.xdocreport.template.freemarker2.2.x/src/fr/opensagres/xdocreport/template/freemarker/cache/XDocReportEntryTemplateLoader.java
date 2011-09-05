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
package fr.opensagres.xdocreport.template.freemarker.cache;

import static fr.opensagres.xdocreport.core.utils.TemplateCacheUtils.getEntryName;
import static fr.opensagres.xdocreport.core.utils.TemplateCacheUtils.getIndexReportEntryName;
import static fr.opensagres.xdocreport.core.utils.TemplateCacheUtils.getReportId;

import java.io.IOException;
import java.io.Reader;

import fr.opensagres.xdocreport.core.document.IXDocReport;
import fr.opensagres.xdocreport.core.document.XDocArchive;
import fr.opensagres.xdocreport.core.document.registry.XDocReportRegistry;
import freemarker.cache.TemplateLoader;

/**
 * Freemarker template loader {@link TemplateLoader} implementation used to
 * cache entry name of {@link XDocArchive} which must be merged with Java model
 * with freemarker template engine.
 * 
 */
public class XDocReportEntryTemplateLoader implements TemplateLoader {

	public static final TemplateLoader INSTANCE = new XDocReportEntryTemplateLoader();

	public Object findTemplateSource(final String name) throws IOException {
		// Name received (see getCachedTemplateName) is like
		// this $reportId '!' $entryName
		int index = getIndexReportEntryName(name);
		if (index == -1) {
			return null;
		}
		String reportId = getReportId(name, index);
		String entryName = getEntryName(name, index);

		// Retrieve the report with the registry and create
		// XDocReportEntrySource
		// to set which entry name must be used as template.
		return new XDocReportEntrySource(XDocReportRegistry.getRegistry()
				.getReport(reportId), entryName);
	}

	public long getLastModified(final Object templateSource) {
		// Get XDocReportEntrySource created with findTemplateSource
		XDocReportEntrySource entrySource = (XDocReportEntrySource) templateSource;
		IXDocReport report = entrySource.getReport();
		String entryName = entrySource.getEntryName();
		return report.getDocumentArchive().getLastModifiedEntry(entryName);
	}

	public Reader getReader(final Object templateSource, final String encoding)
			throws IOException {
		// Get XDocReportEntrySource created with findTemplateSource
		XDocReportEntrySource entrySource = (XDocReportEntrySource) templateSource;
		IXDocReport report = entrySource.getReport();
		String entryName = entrySource.getEntryName();
		// Returns the reader of the entry document archive of the report.
		return report.getDocumentArchive().getEntryReader(entryName);
	}

	public void closeTemplateSource(Object templateSource) {
		// Do nothing.
	}

}
