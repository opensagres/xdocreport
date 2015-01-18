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

import static fr.opensagres.xdocreport.document.tools.internal.MainHelper.getValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.document.tools.internal.BadArgException;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadataXMLSerializer;

public class Main
{

    public static void main( String[] args )
        throws Exception
    {

        String in = null;
        String out = null;
        String err = null;
        String templateEngineKind = null;
        String metadataFile = null;
        String dataDir = null;

        List<IDataProvider> dataProviders = new ArrayList<IDataProvider>();
        String arg = null;
        try
        {
            for ( int i = 0; i < args.length; i++ )
            {
                arg = args[i];
                if ( "-in".equals( arg ) )
                {
                    in = getValue( args, i );
                    if ( in != null )
                        i++;
                }
                else if ( "-out".equals( arg ) )
                {
                    out = getValue( args, i );
                    if ( out != null )
                        i++;
                }
                else if ( "-err".equals( arg ) )
                {
                    err = getValue( args, i );
                    if ( err != null )
                        i++;
                }
                else if ( "-engine".equals( arg ) )
                {
                    templateEngineKind = getValue( args, i );
                    if ( templateEngineKind != null )
                        i++;
                }
                else if ( "-metadataFile".equals( arg ) )
                {
                    metadataFile = getValue( args, i );
                }
                else if ( "-dataDir".equals( arg ) )
                {
                    dataDir = getValue( args, i );
                }
            }
        }
        catch ( BadArgException e )
        {
            printUsage();
            return;
        }

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
            process( in, out, templateEngineKind, metadataFile, dataDir, dataProviders );
        }
        else
        {
            try
            {
                process( in, out, templateEngineKind, metadataFile, dataDir, dataProviders );
            }
            catch ( Throwable e )
            {
                e.printStackTrace( new PrintStream( fileErr ) );
            }
        }

    }

    private static void process( String in, String out, String templateEngineKind, String metadataFile, String dataDir,
                                 List<IDataProvider> dataProviders )
        throws SAXException, IOException, FileNotFoundException, Exception
    {
        FieldsMetadata fieldsMetadata = null;
        if ( metadataFile != null )
        {
            fieldsMetadata = FieldsMetadataXMLSerializer.getInstance().load( new FileInputStream( metadataFile ) );
            templateEngineKind = fieldsMetadata.getTemplateEngineKind();
        }

        if ( dataDir != null )
        {
            File dir = new File( dataDir );
            if ( !dir.exists() )
            {
                dir.mkdirs();
            }
            if ( !dir.isDirectory() )
            {
                throw new Exception( "-dataDir=" + dataDir + " is not a directory" );
            }
            File file = null;
            String fileName = null;
            String extension = null;
            int index = -1;
            File[] files = dir.listFiles();
            if ( files.length > 0 )
            {
                for ( int i = 0; i < files.length; i++ )
                {
                    file = files[i];
                    extension = null;
                    if ( file.isFile() )
                    {
                        fileName = file.getName();
                        index = fileName.indexOf( '.' );
                        if ( index != -1 )
                        {
                            extension = fileName.substring( index + 1, fileName.length() );
                        }
                        IDataProvider provider =
                            DataProviderFactoryRegistry.getRegistry().create( extension, new FileInputStream( file ),
                                                                              null );
                        if ( provider != null )
                        {
                            dataProviders.add( provider );
                        }
                    }
                }
            }

            if ( dataProviders.size() < 1 && fieldsMetadata != null )
            {
                // Generate default JSON
                extension = "json";
                file = new File( dir, "default.json" );

                DataProviderFactoryRegistry.getRegistry().generateDefaultData( extension, fieldsMetadata,
                                                                               new FileOutputStream( file ) );

                IDataProvider provider =
                    DataProviderFactoryRegistry.getRegistry().create( extension, new FileInputStream( file ), null );
                dataProviders.add( provider );
            }
        }

        // Out
        File fileOut = new File( out );
        fileOut.getParentFile().mkdirs();

        Tools tools = Tools.getInstance();
        tools.process( new File( in ), fileOut, templateEngineKind, fieldsMetadata, dataProviders );
    }

    private static void printUsage()
    {

        System.out.print( "Usage: " );
        System.out.print( "java " + Main.class.getName() );
        System.out.print( " -in <a file in>" );
        System.out.print( " -out <a file out>" );
        System.exit( -1 );
    }
}
