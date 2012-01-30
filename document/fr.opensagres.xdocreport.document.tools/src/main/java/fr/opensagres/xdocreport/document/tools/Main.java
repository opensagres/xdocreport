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
package fr.opensagres.xdocreport.document.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadataXMLSerializer;

public class Main
{

    public static void main( String[] args )
        throws Exception
    {

        String fileIn = null;
        String fileOut = null;
        String templateEngineKind = null;
        String jsonData = null;
        String jsonFile = null;
        String metadataFile = null;
        boolean autoGenData = false;
        String dataDir = null;

        List<IDataProvider> dataProviders = new ArrayList<IDataProvider>();
        String arg = null;
        for ( int i = 0; i < args.length; i++ )
        {
            arg = args[i];
            if ( "-in".equals( arg ) )
            {
                fileIn = getValue( args, i );
            }
            else if ( "-out".equals( arg ) )
            {
                fileOut = getValue( args, i );
            }
            else if ( "-engine".equals( arg ) )
            {
                templateEngineKind = getValue( args, i );
            }
            else if ( "-jsonFile".equals( arg ) )
            {
                jsonFile = getValue( args, i );
            }
            else if ( "-autoGenData".equals( arg ) )
            {
                autoGenData = StringUtils.asBoolean( getValue( args, i ), false );
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

        Tools tools = new Tools();
        File out = new File( fileOut );
        out.getParentFile().mkdirs();
        tools.process( new File( fileIn ), out, templateEngineKind, fieldsMetadata, dataProviders );

    }

    private static String getValue( String[] args, int i )
    {
        if ( i == ( args.length - 1 ) )
        {
            printUsage();
            return null;
        }
        else
        {
            return args[i + 1];
        }
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
