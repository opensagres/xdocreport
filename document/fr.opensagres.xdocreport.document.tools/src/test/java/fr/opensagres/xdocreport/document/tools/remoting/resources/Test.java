package fr.opensagres.xdocreport.document.tools.remoting.resources;

import java.io.File;

import org.junit.Assert;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServiceName;

public class Test
{

    private static final String url1 = "http://xdocreport.opensagres.cloudbees.net/jaxrs";
    
    private static final String url2 = "http://localhost:9999/jaxrs";
    
    private static final String url3 = "http://localhost:8080/xdocreport-webapp/cxf/resources";

    public static void main1( String[] s ) throws Exception
    {

        String fileToDownload = "Opensagres____ODTCV.odt";
        File downlodedFile = new File( "DownlodedSimple.docx" );
        String[] args =
            { "-baseAddress", url1, "-serviceName", ResourcesServiceName.download.name(), "-resources",
                fileToDownload, "-out", downlodedFile.getPath() };
        Main.main( args );
        Assert.assertTrue( downlodedFile.exists() );
    }
    
    public static void main( String[] s ) throws Exception
    {

        File downlodedFile = new File( "name.txt" );
        String[] args =
            { "-baseAddress", url3, "-serviceName", ResourcesServiceName.name.name(),
                 "-out", downlodedFile.getPath(), "-serviceType", "SOAP" };
        Main.main( args );
        Assert.assertTrue( downlodedFile.exists() );
    }
}
