package fr.opensagres.xdocreport.document.tools;

import org.junit.Test;

public class MainVithVelocityTestCase
{

    public static final String BASE_DIR = "src/test/resources/fr/opensagres/xdocreport/document/tools";

    // @Test
    public void testGenerateODTWithJSONFile()
        throws Exception
    {
        String[] args =
            { "-in", BASE_DIR + "/ODTHelloWordWithVelocity.odt", "-out", "target/ODTHelloWordWithVelocity_Out.odt",
                "-engine", "Velocity", "-jsonFile", BASE_DIR + "/JSONDataVelocity.json" };
        Main.main( args );
    }
}
