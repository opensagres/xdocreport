package fr.opensagres.xdocreport.document.tools;

import org.junit.Test;

public class MainVithFreemarkerTestCase
{

    public static final String BASE_DIR = "src/test/resources/fr/opensagres/xdocreport/document/tools";

    // @Test
    public void testGenerateODTWithJSONFile()
        throws Exception
    {
        String[] args =
            { "-in", BASE_DIR + "/ODTHelloWordWithFreemarker.odt", "-out", "target/ODTHelloWordWithFreemarker_Out.odt",
                "-engine", "Freemarker", "-jsonFile", BASE_DIR + "/JSONDataFreemarker.json" };
        Main.main( args );
    }
}
