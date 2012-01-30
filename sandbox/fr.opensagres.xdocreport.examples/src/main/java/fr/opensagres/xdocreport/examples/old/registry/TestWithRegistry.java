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
package fr.opensagres.xdocreport.examples.old.registry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.debug.SysOutDebugger;
import fr.opensagres.xdocreport.document.odt.ODTReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.examples.old.model.Command;
import fr.opensagres.xdocreport.examples.old.model.Project;
import fr.opensagres.xdocreport.template.IContext;

public class TestWithRegistry {

	public static void main(String[] args) {
		try {
			// Set debugger
			XDocReportRegistry.getRegistry().setDebugger(
					SysOutDebugger.INSTANCE);

			// Load ODT file and cache it to the registry
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					TestWithRegistry.class
							.getResourceAsStream("TestWithRegistry.odt"));
			// Here report id was updated. You can set the id with
			// XDocReportRegistry.getRegistry().loadReport(stream, id)

			if (report instanceof ODTReport) {
				System.out.println("ODT report");
			} else {
				System.out.println("Argh! C'est pas un ODT");
			}
			// Generate report
			generate(report, 1);

			// Retrieve report from the registry
			String id = report.getId();
			IXDocReport report2 = XDocReportRegistry.getRegistry()
					.getReport(id);
			if (report != null && report.equals(report2)) {
				System.out.println("Report retrouv� dans le registry ");
			} else {
				System.out.println("Argh! Le registry ne marche pas");
			}

			// Generate a second report generation. See log to check that
			// performance report generation are improved.
			generate(report, 2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XDocReportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void generate(IXDocReport report2, int i)
			throws XDocReportException, IOException, FileNotFoundException {
		IContext context = report2.createContext();
		Project project = new Project("XDocReport_" + i);
		context.put("project", project);
		context.put("adresse_magasin", "yessssssssssss");

		List<Command> commands = new ArrayList<Command>();
		commands.add(new Command("ref1_" + i));
		commands.add(new Command("ref2_" + i));
		context.put("lines", commands);

		File outFolder = new File("out");
		if (!outFolder.exists())
			outFolder.mkdir();

		File file = new File(outFolder, "TestWithRegistry" + i + "_Out.odt");
		report2.process(context, new FileOutputStream(file));
	}
}
