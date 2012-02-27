/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package fr.opensagres.xdocreport.document.tools.repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.tools.internal.ArgContext;
import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;
import fr.opensagres.xdocreport.remoting.repository.services.RepositoryServiceClientFactory;
import fr.opensagres.xdocreport.remoting.repository.services.ServiceName;
import fr.opensagres.xdocreport.remoting.repository.services.ServiceType;

public class Main
{

    public static final String BASE_ADDRESS_ARG = "-baseAddress";

    // -baseAddress %BASE_ADDRESS% -user %USER% -password %PASSWORD% -serviceType %SERVICE_TYPE% -serviceName
    // %SERVICE_NAME% -out %OUT% -err %ERR%
    public static void main( String[] args )
        throws Exception
    {

        ArgContext context = new ArgContext( args );
        String baseAddress = context.get( BASE_ADDRESS_ARG );
        String user = context.get( "-password" );
        String password = context.get( "-password" );

        ServiceType serviceType = ServiceType.REST;
        ServiceName serviceName = ServiceName.name;

        String out = context.get( "-out" );
        String err = context.get( "-err" );

        // Err
        File fileErr = null;
        if ( err != null )
        {
            fileErr = new File( err );
            if ( fileErr.exists() )
            {
                fileErr.delete();
            }
            else
            {
                fileErr.getParentFile().mkdirs();
            }
        }

        if ( fileErr == null )
        {
            process( baseAddress, user, password, serviceType, serviceName, out );
        }
        else
        {
            try
            {
                process( baseAddress, user, password, serviceType, serviceName, out );
            }
            catch ( Throwable e )
            {
                e.printStackTrace( new PrintStream( fileErr ) );
            }
        }
    }

    private static void process( String baseAddress, String user, String password, ServiceType serviceType,
                                 ServiceName serviceName, String out )
        throws IOException
    {
        IRepositoryService client = RepositoryServiceClientFactory.create( baseAddress, serviceType, user, password );
        switch ( serviceName )
        {
            case name:
                generateRepositoryNameFile( client.getName(), new File( out ) );
                break;
        }
    }

    private static void generateRepositoryNameFile( String repositoryName, File file )
        throws IOException
    {
        Writer writer = new FileWriter( file );
        try
        {
            writer.write( repositoryName );
        }
        finally
        {

            IOUtils.closeQuietly( writer );
        }
    }

    private static void printUsage()
    {
        // TODO Auto-generated method stub

    }
}
