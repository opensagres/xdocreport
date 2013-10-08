package fr.opensagres.xdocreport.document.dump;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.utils.Base64Utility;
import fr.opensagres.xdocreport.document.IXDocReport;

public class JavaDumper {

	private final StringBuffer buffer = new StringBuffer(2048);
	private static final String CR = System.getProperty("line.separator");
	private static final String TAB = "\t";
	private static final String OPENING_BRACKET = "{";
	private static final String CLOSING_BRACKET = "}";

	protected String toBase64(IXDocReport IXDocReport) throws IOException {
		InputStream in = XDocArchive.getInputStream(IXDocReport
				.getPreprocessedDocumentArchive());
		byte[] bytes = IOUtils.toByteArray(in);
		String encoded = Base64Utility.encode(bytes);
		return encoded;
	}

	protected void endClass() {
		buffer.append(CR).append(CLOSING_BRACKET);

	}

	protected void startClass() {
		buffer.append("public class Main").append(CR).append(OPENING_BRACKET);

	}

	protected void generateImport() {
		// import
		// fr.opensagres.xdocreport.samples.docxandvelocity.model.Project;
		buffer.append("import java.io.File;").append(CR);
		buffer.append("import java.io.FileOutputStream;").append(CR);
		buffer.append("import java.io.IOException;").append(CR);
		buffer.append("import java.io.InputStream;").append(CR);
		buffer.append("import java.io.OutputStream;").append(CR);
		buffer.append(
				"import fr.opensagres.xdocreport.core.XDocReportException;")
				.append(CR);
		buffer.append("import fr.opensagres.xdocreport.document.IXDocReport;")
				.append(CR);
		buffer.append(
				"import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;")
				.append(CR);
		buffer.append("import fr.opensagres.xdocreport.template.IContext;")
				.append(CR);
		buffer.append(
				"import fr.opensagres.xdocreport.template.TemplateEngineKind;")
				.append(CR);
	}

	public void balanceLaPuree(String fileName) throws IOException {
		File aFile = new File(fileName);
		FileWriter fw = new FileWriter(aFile);
		fw.write(buffer.toString());
		fw.flush();
		fw.close();
		
	}
}
