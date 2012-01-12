package fr.opensagres.xdocreport.document.tools;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.tools.json.JSONPoupluateContextAware;

public class Main {

	public static void main(String[] args) throws Exception {

		String fileIn = null;
		String fileOut = null;
		String templateEngineKind = null;
		String jsonData = null;
		String jsonFile = null;

		IPopulateContextAware contextAware = null;
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
				contextAware = new JSONPoupluateContextAware(jsonData);
			} else if ("-jsonFile".equals(arg)) {
				jsonFile = getValue(args, i);
				StringWriter jsonDataWriter = new StringWriter();
				IOUtils.copy(new FileReader(new File(jsonFile)), jsonDataWriter);
				contextAware = new JSONPoupluateContextAware(
						jsonDataWriter.toString());
			}
		}

		Tools tools = new Tools();
		tools.process(new File(fileIn), new File(fileOut), templateEngineKind,
				contextAware);

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
