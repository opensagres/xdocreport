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
package fr.opensagres.xdocreport.examples.old;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.debug.SysOutDebugger;
import fr.opensagres.xdocreport.document.docx.DocXReport;
import fr.opensagres.xdocreport.examples.old.model.Command;
import fr.opensagres.xdocreport.examples.old.model.Project;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;

public class TestDocxWithFreemarker {

	public static void main(String[] args) {
		ITemplateEngine templateEngine = new FreemarkerTemplateEngine();
		IXDocReport report = new DocXReport();
		report.setDebugger(SysOutDebugger.INSTANCE);
		report.setTemplateEngine(templateEngine);

		FieldsMetadata metadata = new FieldsMetadata();
		metadata.addFieldAsList("lines.reference");
		report.setFieldsMetadata(metadata);

		try {
			report.load(TestDocxWithFreemarker.class
					.getResourceAsStream("TestDocxWithFreemarker.docx"));

			IContext context = report.createContext();
			Project project = new Project("XDocReport");
			context.put("project", project);

			context.put("adresse_magasin", "yessssssssssss");

			List<Command> commands = new ArrayList<Command>();
			commands.add(new Command("ref1"));
			commands.add(new Command("ref2"));
			context.put("lines", commands);

			File file = new File("out/TestDocxWithFreemarker_Out.docx");
			report.process(context, new FileOutputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XDocReportException e) {
			e.printStackTrace();
		}

	}
}
