package fr.opensagres.xdocreport.document.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.tools.json.FieldsMetadataJSONSerializer;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadataXMLSerializer;

public class Main {

	public static void main(String[] args) throws Exception {

		String fileIn = null;
		String fileOut = null;
		String templateEngineKind = null;
		String jsonData = null;
		String jsonFile = null;
		String metadataFile = null;
		boolean autoGenData = false;

		IDataProvider dataProvider = null;
		String arg = null;
		for (int i = 0; i < args.length; i++) {
			arg = args[i];
			if ("-in".equals(arg)) {
				fileIn = getValue(args, i);
			} else if ("-out".equals(arg)) {
				fileOut = getValue(args, i);
			} else if ("-engine".equals(arg)) {
				templateEngineKind = getValue(args, i);
			} else if ("-jsonData".equals(arg)) {
				jsonData = getValue(args, i);
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("jsonData", jsonData);
				dataProvider = DataProviderFactoryRegistry.getRegistry()
						.create("json", parameters);
			} else if ("-jsonFile".equals(arg)) {
				jsonFile = getValue(args, i);
			} else if ("-autoGenData".equals(arg)) {
				autoGenData = StringUtils.asBoolean(getValue(args, i), false);
			} else if ("-metadataFile".equals(arg)) {
				metadataFile = getValue(args, i);
			}
		}

		FieldsMetadata fieldsMetadata = null;
		if (metadataFile != null) {
			fieldsMetadata = FieldsMetadataXMLSerializer.getInstance().load(
					new FileInputStream(metadataFile));
		}

		if (!StringUtils.isEmpty(jsonFile)) {
			StringWriter jsonDataWriter = new StringWriter();

			File f = new File(jsonFile);
			if (!f.exists() && autoGenData && fieldsMetadata != null) {
				// Generate JSON
				FieldsMetadataJSONSerializer.getInstance().save(fieldsMetadata,
						new FileOutputStream(jsonFile), true);
			}

			IOUtils.copy(new FileReader(f), jsonDataWriter);
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("jsonData", jsonDataWriter.toString());
			dataProvider = DataProviderFactoryRegistry.getRegistry().create(
					"json", parameters);

		}

		Tools tools = new Tools();
		tools.process(new File(fileIn), new File(fileOut), templateEngineKind,
				fieldsMetadata, dataProvider);

	}

	private static String getValue(String[] args, int i) {
		if (i == (args.length - 1)) {
			printUsage();
			return null;
		} else {
			return args[i + 1];
		}
	}

	private static void printUsage() {

		System.out.print("Usage: ");
		System.out.print("java " + Main.class.getName());
		System.out.print(" -in <a file in>");
		System.out.print(" -out <a file out>");
		System.exit(-1);
	}
}
