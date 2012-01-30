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
package fr.opensagres.xdocreport.examples.msword.docx.converter.fo;

import java.io.IOException;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.debug.SysOutDebugger;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.examples.msword.docx.DocXHelloWordWithVelocity;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * Example with MS Word DOCX which contains an cell with the content Hello
 * !$name. Merge with Velocity template engine will replace this cell with Hello
 * world!
 * 
 */
public class DocXHelloWordWithVelocity2FO {

	public static void main(String[] args) {
		try {
			// 1) Set debugger to track XDocReport process
			XDocReportRegistry.getRegistry().setDebugger(
					SysOutDebugger.INSTANCE);

			// 2) Load ODS file by filling Velocity template engine and cache it
			// to the registry
			IXDocReport report = XDocReportRegistry
					.getRegistry()
					.loadReport(
							DocXHelloWordWithVelocity.class
									.getResourceAsStream("DocXHelloWordWithVelocity.docx"),
							TemplateEngineKind.Velocity);

			// 3) Create context Java model
			IContext context = report.createContext();
			context.put("name", "world");

			Options options = Options.getTo(ConverterTypeTo.FO);
			report.convert(context, options, System.err);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XDocConverterException e) {
			e.printStackTrace();
		} catch (XDocReportException e) {
			e.printStackTrace();
		}
	}
}
