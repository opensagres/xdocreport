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

import org.apache.commons.io.IOUtils;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.XDocArchive;
import fr.opensagres.xdocreport.core.template.IContext;
import fr.opensagres.xdocreport.core.template.ITemplateEngine;

public class XDocArchiveWithFreemarkerTest extends TestCase {

	public void testWrite() throws IOException, XDocReportException {

		XDocArchive archive = new XDocArchive();
		Writer contentWriter = archive.getEntryWriter("content.xml");
		contentWriter.write("bla bla bla ${var}");
		contentWriter.close();

		ITemplateEngine templateEngine = FreemarkerTemplateEngine.getDefault();
		IContext context = templateEngine.createContext();
		context.put("var", "AAA");

		contentWriter = archive.getEntryWriter("content.xml");
		Reader contentReader = archive.getEntryReader("content.xml");

		templateEngine.process("", context, contentReader, contentWriter, null);
		
		contentReader = archive.getEntryReader("content.xml");
		assertEquals("bla bla bla AAA", IOUtils.toString(contentReader));
	}
}
