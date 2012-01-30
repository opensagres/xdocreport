package fr.opensagres.xdocreport.document.tools;

import java.io.File;

import org.junit.Test;

public class MainVithFreemarkerAutoGenTestCase
{

    public static final String BASE_DIR = "src/test/resources/fr/opensagres/xdocreport/document/tools/freemarker";

    // @Test
    // public void testMain() {
    // try {
    // String jsonData = "{\"name\":\"XXXX\"}";
    // String[] args = { "-in", "ODTHelloWordWithFreemarker.odt", "-out",
    // "test.odt", "-engine", "Freemarker", "-jsonData", jsonData };
    // Main.main(args);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    @Test
    public void testGenerateODTWithJSONFile()
        throws Exception
    {
        File jsonFile = new File( "target/default.json" );
        if ( jsonFile.exists() )
        {
            jsonFile.delete();
        }

        String[] args =
            { "-in", BASE_DIR + "/ODTHelloWordWithFreemarker.odt", "-out", "target/ODTHelloWordWithFreemarker_Out.odt",
                "-engine", "Freemarker", "-dataDir", "target", "-autoGenData", "true", "-metadataFile",
                BASE_DIR + "/freemarker.fields.xml" };
        // Main.main(args);
    }
}
