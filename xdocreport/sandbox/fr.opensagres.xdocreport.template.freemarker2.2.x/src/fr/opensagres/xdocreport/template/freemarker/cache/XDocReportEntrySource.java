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

import fr.opensagres.xdocreport.core.document.IXDocReport;
import fr.opensagres.xdocreport.core.document.XDocArchive;

/**
 * Store the entry name of the {@link XDocArchive} which must be managed with
 * Freemarker template loader.
 * 
 */
public class XDocReportEntrySource {

	private final IXDocReport report;
	private final String entryName;

	public XDocReportEntrySource(final IXDocReport report,
			final String entryName) {
		this.report = report;
		this.entryName = entryName;
	}

	public IXDocReport getReport() {
		return report;
	}

	public String getEntryName() {
		return entryName;
	}
}
