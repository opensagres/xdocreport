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
package fr.opensagres.xdocreport.samples.docxandvelocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.samples.docxandvelocity.model.Developer;
import fr.opensagres.xdocreport.samples.docxandvelocity.model.Project;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class DocxProjectWithVelocityList2PDF {

	public static void main(String[] args) {
		try {
			// 1) Load Docx file by filling Velocity template engine and cache
			// it to the registry
			InputStream in = DocxProjectWithVelocityList2PDF.class
					.getResourceAsStream("DocxProjectWithVelocityList.docx");
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					in, TemplateEngineKind.Velocity);

			// 2) Create fields metadata to manage lazy loop (#forech velocity) for table row. 
			FieldsMetadata metadata = new FieldsMetadata();
			metadata.addFieldAsList("developers.Name");
			metadata.addFieldAsList("developers.LastName");
			metadata.addFieldAsList("developers.Mail");
			report.setFieldsMetadata(metadata);

			// 3) Create context Java model
			IContext context = report.createContext();
			// Register project
			Project project = new Project("XDocReport");
			context.put("project", project);
			// Register developers list
			List<Developer> developers = new ArrayList<Developer>();
			developers.add(new Developer("ZERR", "Angelo", "angelo.zerr@gmail.com"));
			developers.add(new Developer("Leclercq", "Pascal", "pascal.leclercq@gmail.com"));
			context.put("developers", developers);
			
			// 4) Generate report by merging Java model with the Docx
			OutputStream out = new FileOutputStream(new File(
					"DocxProjectWithVelocityList_Out.pdf"));
			// report.process(context, out);
			Options options = Options.getTo(ConverterTypeTo.PDF).via(
					ConverterTypeVia.ITEXT);
			report.convert(context, options, out);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XDocReportException e) {
			e.printStackTrace();
		}
	}
}

