package fr.opensagres.xdocreport.document.tools;

import org.junit.Test;

public class MainVithVelocityTestCase {

	public static final String BASE_DIR = "src/test/resources/fr/opensagres/xdocreport/document/tools";

	@Test
	public void testMain() {
		try {
			String jsonData = "{\"name\":\"XXXX\"}";
			String[] args = { "-in", BASE_DIR + "/ODTHelloWordWithVelocity.odt", "-out",
					"target/ODTHelloWordWithVelocity_Out.odt", "-engine", "Velocity", "-jsonData", jsonData };
			Main.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGenerateODTWithJSONFile() {
		try {
			String[] args = { "-in",
					BASE_DIR + "/ODTHelloWordWithVelocity.odt", "-out",
					"target/ODTHelloWordWithVelocity_Out.odt", "-engine",
					"Velocity", "-jsonFile",
					BASE_DIR + "/JSONDataVelocity.json" };
			Main.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
