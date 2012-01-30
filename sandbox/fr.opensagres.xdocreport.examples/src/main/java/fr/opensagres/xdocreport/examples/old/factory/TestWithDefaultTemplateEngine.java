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
package fr.opensagres.xdocreport.examples.old.factory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.debug.SysOutDebugger;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.examples.old.model.Command;
import fr.opensagres.xdocreport.examples.old.model.Project;
import fr.opensagres.xdocreport.template.IContext;

public class TestWithDefaultTemplateEngine {

	public static void main(String[] args) {

		// Load ODT

		try {
			IXDocReport odtReport = XDocReportRegistry.getRegistry()
					.loadReport(
							TestWithDefaultTemplateEngine.class
									.getResourceAsStream("TestWithDefaultTemplateEngine.odt"));
			System.out.println(odtReport);
			System.out.println(odtReport.getTemplateEngine());

			IContext context = odtReport.createContext();
			Project project = new Project("XDocReport");
			context.put("project", project);
			context.put("adresse_magasin", "yessssssssssss");

			List<Command> commands = new ArrayList<Command>();
			commands.add(new Command("ref1"));
			commands.add(new Command("ref2"));
			context.put("lines", commands);

			File file = new File("out/TestWithDefaultTemplateEngine_Out.docx");
			odtReport.process(context, new FileOutputStream(file));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XDocReportException e) {
			e.printStackTrace();
		}
		try {
			// Load Docx
			IXDocReport docxReport =  XDocReportRegistry.getRegistry()
					.loadReport(
							TestWithDefaultTemplateEngine.class
									.getResourceAsStream("TestWithDefaultTemplateEngine.docx"));
			System.out.println(docxReport);
			System.out.println(docxReport.getTemplateEngine());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XDocReportException e) {
			e.printStackTrace();
		}
	}
}
