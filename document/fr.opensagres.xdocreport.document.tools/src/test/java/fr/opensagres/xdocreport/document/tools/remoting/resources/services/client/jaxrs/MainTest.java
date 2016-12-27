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
package fr.opensagres.xdocreport.document.tools.remoting.resources.services.client.jaxrs;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.tools.remoting.resources.FileUtils;
import fr.opensagres.xdocreport.document.tools.remoting.resources.Main;
import fr.opensagres.xdocreport.document.tools.remoting.resources.services.server.jaxrs.MockJAXRSResourcesApplication;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServiceName;

public class MainTest
{

    private static final int PORT = 9991;

    private static Server server;

    private static final String BASE_ADDRESS = "http://localhost:" + PORT;

    public static File tempFolder = new File( "target" );

    public static File resourcesFolder = new File( tempFolder, "resources" );

    public static File srcFolder =
        new File( "src/test/resources/fr/opensagres/xdocreport/document/tools/remoting/resources" );

    @BeforeClass
    public static void startServer()
        throws Exception
    {

        // 1) Copy resources in the target folder.
        initResources();
        CXFNonSpringJaxrsServlet s = new CXFNonSpringJaxrsServlet(new MockJAXRSResourcesApplication());
        
        ServletHolder holder = new ServletHolder( s );

        holder.setInitParameter( "timeout", "60000" );
        server = new Server( PORT );

        ServletContextHandler context = new ServletContextHandler( server, "/", ServletContextHandler.SESSIONS );

        context.addServlet( holder, "/*" );
        server.start();

    }

    @Test
    public void testBadBaseAddress()
        throws Exception
    {
        File nameFile = new File( tempFolder, "name.txt" );
        File errFile = new File( tempFolder, "name.err.txt" );
        String[] args =
            { "-baseAddress", "http://localhost:99/remaaaaa=aaaa", "-out", nameFile.getPath(), "-err",
                errFile.getPath() };
        Main.main( args );

        Assert.assertTrue( errFile.exists() );
        String s = IOUtils.toString( new FileReader( errFile ) );
        Assert.assertNotNull( s );
        // System.err.println( s );
        // Assert.assertEquals( "XDocReport", s );
    }

    @Test
    public void name()
        throws Exception
    {
        File nameFile = new File( tempFolder, "name.txt" );
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ResourcesServiceName.name.name(), "-out",
                nameFile.getPath() };
        Main.main( args );

        Assert.assertTrue( nameFile.exists() );
        String s = IOUtils.toString( new FileReader( nameFile ) );
        Assert.assertEquals( "Test-Tools-RepositoryService", s );
    }

    @Test
    public void root()
        throws Exception
    {
        File rootFile = new File( tempFolder, "root.xml" );
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ResourcesServiceName.root.name(), "-out",
                rootFile.getPath() };
        Main.main( args );

        Assert.assertTrue( rootFile.exists() );
        String s = IOUtils.toString( new FileReader( rootFile ) );

        // According OS, File#listFiles() are not the same order.
        // Test cannot be done -(
        // Assert.assertEquals(
        // "<resource name=\"resources\" type=\"0\"><resource name=\"Custom\" type=\"0\"><resource name=\"CustomSimple.docx\" type=\"1\"/><resource name=\"CustomSimple.odt\" type=\"1\"/></resource><resource name=\"Opensagres\" type=\"0\"><resource name=\"OpensagresSimple.docx\" type=\"1\"/><resource name=\"OpensagresSimple.odt\" type=\"1\"/></resource><resource name=\"Simple.docx\" type=\"1\"/><resource name=\"Simple.odt\" type=\"1\"/></resource>",
        // s );
    }

    @Test
    public void downloadARootFile()
        throws Exception
    {
        String fileToDownload = "Simple.docx";
        File downlodedFile = new File( tempFolder, "DownlodedSimple.docx" );
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ResourcesServiceName.download.name(), "-resources",
                fileToDownload, "-out", downlodedFile.getPath() };
        Main.main( args );
        Assert.assertTrue( downlodedFile.exists() );
    }

    @Test
    public void downloadAFileInFolder()
        throws Exception
    {
        String fileToDownload = "Custom____CustomSimple.docx";
        File downlodedFile = new File( tempFolder, "DownlodedCustomSimple.docx" );
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ResourcesServiceName.download.name(), "-resources",
                fileToDownload, "-out", downlodedFile.getPath() };
        Main.main( args );
        Assert.assertTrue( downlodedFile.exists() );
    }

    @Test
    public void downloadALargeFileInFolder()
        throws Exception
    {
        String fileToDownload = "Custom____CustomSimple.docx";
        File downlodedFile = new File( tempFolder, "DownlodedCustomSimple.docx" );
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ResourcesServiceName.downloadLarge.name(), "-resources",
                fileToDownload, "-out", downlodedFile.getPath() };
        Main.main( args );
        Assert.assertTrue( downlodedFile.exists() );
    }

    @Test
    public void uploadARootFile()
        throws Exception
    {
        File fileToUploadFile = new File( srcFolder, "Simple.docx" );
        String resourceId = "NewSimple.docx";
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ResourcesServiceName.upload.name(), "-resources",
                resourceId, "-out", fileToUploadFile.getPath() };
        Main.main( args );
        File uploadedFile = new File( resourcesFolder, "NewSimple.docx" );
        Assert.assertTrue( uploadedFile.exists() );
    }

    @Test
    public void uploadLargeARootFile()
        throws Exception
    {
        File fileToUploadFile = new File( srcFolder, "Simple.docx" );
        String resourceId = "NewSimple.docx";
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ResourcesServiceName.uploadLarge.name(), "-resources",
                resourceId, "-out", fileToUploadFile.getPath() };
        Main.main( args );
        File uploadedFile = new File( resourcesFolder, "NewSimple.docx" );
        Assert.assertTrue( uploadedFile.exists() );
    }

    @Test
    public void uploadAFileInFolder()
        throws Exception
    {
        File fileToUploadFile = new File( srcFolder, "Simple.docx" );
        String resourceId = "XXX____NewSimple.docx";
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ResourcesServiceName.upload.name(), "-resources",
                resourceId, "-out", fileToUploadFile.getPath() };
        Main.main( args );
        File uploadedFile = new File( resourcesFolder, "XXX/NewSimple.docx" );
        Assert.assertTrue( uploadedFile.exists() );
    }

    @AfterClass
    public static void stopServer()
        throws Exception
    {
        server.stop();
    }

    private static void initResources()
        throws IOException
    {
        if ( resourcesFolder.exists() )
        {
            resourcesFolder.delete();
        }
        FileUtils.copyDirectory( srcFolder, resourcesFolder );
    }

}
