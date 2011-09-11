package fr.opensagres.xdocreport.samples.odtandfreemarker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.samples.odtandfreemarker.model.Developer;
import fr.opensagres.xdocreport.samples.odtandfreemarker.model.Project;
import fr.opensagres.xdocreport.samples.odtandfreemarker.model.Role;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class ODTTableWithoutFieldsMetadataWithFreemarker {

	public static void main(String[] args) {
		try {
			// 1) Load Docx file by filling Freemarker template engine and cache
			// it to the registry
			InputStream in = ODTTableWithoutFieldsMetadataWithFreemarker.class
					.getResourceAsStream("ODTTableWithoutFieldsMetadataWithFreemarker.odt");
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					in, TemplateEngineKind.Freemarker);

			// 2) Create fields metadata to manage lazy loop (#forech velocity)
			// for table row.
			// FieldsMetadata metadata = new FieldsMetadata();
			// metadata.addFieldAsList("developers.name");
			// metadata.addFieldAsList("developers.lastName");
			// metadata.addFieldAsList("developers.mail");
			// report.setFieldsMetadata(metadata);

			// 3) Create context Java model
			IContext context = report.createContext();
			Project project = new Project("XDocReport");
			context.put("project", project);
			// Register developers list
			List<Developer> developers = new ArrayList<Developer>();

			developers.add(new Developer("ZERR", "Angelo",
					"angelo.zerr@gmail.com").addRole(new Role("Architecte"))
					.addRole(new Role("Developer")));

			developers.add(new Developer("Leclercq", "Pascal",
					"pascal.leclercq@gmail.com")
					.addRole(new Role("Architecte")).addRole(
							new Role("Developer")));

			developers.add(new Developer("Bousta", "Amine", "")
					.addRole(new Role("Developer")));

			context.put("developers", developers);

			// 4) Generate report by merging Java model with the Docx
			OutputStream out = new FileOutputStream(new File(
					"ODTTableWithoutFieldsMetadataWithFreemarker_Out.odt"));
			report.process(context, out);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XDocReportException e) {
			e.printStackTrace();
		}
	}
}
