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
package fr.opensagres.xdocreport.document.tools.remoting.resources;

import java.io.File;

import org.junit.Assert;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServiceName;

public class Test
{

    private static final String url1 = "http://xdocreport.opensagres.cloudbees.net/jaxrs";
    
    private static final String url2 = "http://localhost:9999/jaxrs";
    
    private static final String url3 = "http://localhost:8080/xdocreport-webapp/cxf/resources";
    
    private static final String url4 = "http://xdocreport.opensagres.cloudbees.net/cxf";

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
    
    public static void main2( String[] s ) throws Exception
    {

        File downlodedFile = new File( "name.txt" );
        String[] args =
            { "-baseAddress", url3, "-serviceName", ResourcesServiceName.name.name(),
                 "-out", downlodedFile.getPath(), "-serviceType", "SOAP" };
        Main.main( args );
        Assert.assertTrue( downlodedFile.exists() );
    }
    
    public static void main( String[] s ) throws Exception
    {

        String fileToDownload = "Opensagres____ODTCV.odt";
        File downlodedFile = new File( "ODTCV.odt" );
        String[] args =
            { "-baseAddress", url4, "-serviceName", ResourcesServiceName.upload.name(), "-resources",
                fileToDownload, "-out", downlodedFile.getPath() };
        Main.main( args );
    }
}
