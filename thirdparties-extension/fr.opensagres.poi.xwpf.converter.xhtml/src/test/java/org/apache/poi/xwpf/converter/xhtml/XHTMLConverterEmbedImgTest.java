package org.apache.poi.xwpf.converter.xhtml;

import fr.opensagres.poi.xwpf.converter.xhtml.Base64EmbedImgManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.converter.core.AbstractXWPFPOIConverterTest;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by zzt on 4/16/17.
 * <p>
 * <h3></h3>
 */
public class XHTMLConverterEmbedImgTest extends AbstractXWPFPOIConverterTest {

    protected void doGenerate(String fileInName)
            throws IOException {
        doGenerateSysOut(fileInName);
        doGenerateHTMLFile(fileInName);
    }

    private void doGenerateSysOut(String fileInName)
            throws IOException
    {

        long startTime = System.currentTimeMillis();

        XWPFDocument document = new XWPFDocument( AbstractXWPFPOIConverterTest.class.getResourceAsStream( fileInName ) );

        XHTMLOptions options = XHTMLOptions.create().indent( 4 ).setImageManager(new Base64EmbedImgManager());
        OutputStream out = System.out;
        XHTMLConverter.getInstance().convert( document, out, options );

        System.err.println( "Elapsed time=" + ( System.currentTimeMillis() - startTime ) + "(ms)" );
    }

    private void doGenerateHTMLFile(String fileInName) throws IOException {
        String root = "target";
        String fileOutName = root + "/" + fileInName + ".html";

        long startTime = System.currentTimeMillis();

        XWPFDocument document = new XWPFDocument(AbstractXWPFPOIConverterTest.class.getResourceAsStream(fileInName));

        XHTMLOptions options = XHTMLOptions.create();
        // Extract image
        options.setImageManager(new Base64EmbedImgManager());

        OutputStream out = new FileOutputStream(new File(fileOutName));
        XHTMLConverter.getInstance().convert(document, out, options);

        System.out.println("Generate " + fileOutName + " with " + (System.currentTimeMillis() - startTime) + " ms.");
    }
}
