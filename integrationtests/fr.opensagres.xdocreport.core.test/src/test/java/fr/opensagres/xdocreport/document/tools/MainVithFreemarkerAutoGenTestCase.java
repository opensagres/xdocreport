/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
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
