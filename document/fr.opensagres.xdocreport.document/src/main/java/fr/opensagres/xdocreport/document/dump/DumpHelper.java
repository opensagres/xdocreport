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
package fr.opensagres.xdocreport.document.dump;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.utils.Base64Utility;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.json.JSONObject;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * Dump helper.
 */
public class DumpHelper
{

    private static final String ESCAPED_DOUBLE_QUOTE = "\\\"";

    private static final String DOUBLE_QUOTE = "\"";

    public static final String JAVA_MAIN_DUMP_TEMPLATE = "JavaMainDump";

    // ---------------- document (odt, docx, etc)

    /**
     * @param report
     * @return
     * @throws IOException
     */
    public static String toDocumentAsBinary64( IXDocReport report )
        throws IOException
    {
        InputStream in = getDocument( report );
        return Base64Utility.encode( in );
    }

    public static InputStream getDocument( IXDocReport report )
        throws IOException
    {
        InputStream in = XDocArchive.getInputStream( report.getOriginalDocumentArchive() );
        return in;
    }

    private static String getDocumentFileName( IXDocReport report, IContext dumpContext )
    {
        String documentFileName = report.getId() + "." + report.getKind().toLowerCase();
        dumpContext.put( "documentFile", documentFileName );
        return documentFileName;
    }

    public static void generateDocumentEntry( IXDocReport report, InputStream documentIn, IContext dumpContext,
                                              String src, File baseDir, ZipOutputStream out )
        throws IOException
    {
        if ( out != null )
        {
            generateDocumentZipEntry( report, documentIn, dumpContext, out, src );
        }
        else
        {
            generateDocumentFile( report, documentIn, dumpContext, new File( baseDir, src ) );
        }
    }

    public static void generateDocumentFile( IXDocReport report, InputStream documentIn, IContext dumpContext,
                                             File srcDir )
        throws FileNotFoundException, IOException
    {
        String documentFileName = getDocumentFileName( report, dumpContext );

        File documentFile = new File( srcDir, documentFileName );
        documentFile.getParentFile().mkdirs();
        OutputStream documentOut = null;

        try
        {
            documentOut = new FileOutputStream( documentFile );
            IOUtils.copy( documentIn, documentOut );
        }
        finally
        {
            if ( documentOut != null )
            {
                IOUtils.closeQuietly( documentOut );
            }
        }
    }

    public static void generateDocumentZipEntry( IXDocReport report, InputStream documentIn, IContext dumpContext,
                                                 ZipOutputStream out, String src )
        throws IOException
    {
        String documentFileName = getDocumentFileName( report, dumpContext );

        ZipEntry zipEntry = new ZipEntry( src + "/" + documentFileName );
        out.putNextEntry( zipEntry );

        try
        {
            IOUtils.copy( documentIn, out );
        }
        finally
        {
            out.closeEntry();
        }
    }

    // ---------------- Java Main

    public static String getClassName( IXDocReport report )
    {
        String className = report.getId();
        className = StringUtils.replaceEach( className, new String[] { "@", "." }, new String[] { "_", "_" } );
        return className.substring( 0, 1 ).toUpperCase() + className.substring( 1, className.length() );
    }

    public static String getClassNameFile( IContext dumpContext )
    {
        return (String) dumpContext.get( "className" ) + ".java";
    }

    // ---------------- JSON as data

    public static String toJSON( IContext context, boolean upperCaseFirstChar )
    {
        return toJSON( context, upperCaseFirstChar, false );
    }

    public static String toJSON( IContext context, boolean upperCaseFirstChar, boolean formatAsJavaString )
    {
        JSONObject json = new JSONObject( context.getContextMap(), upperCaseFirstChar );
        String jsonString = json.toString();
        if ( !formatAsJavaString )
        {
            return jsonString;
        }
        return jsonString.replace( DOUBLE_QUOTE, ESCAPED_DOUBLE_QUOTE );
    }

    public static void generateJSONEntry( IXDocReport report, IContext context, boolean upperCaseFirstChar,
                                          IContext dumpContext, String resourcesSrcPath, File baseDir,
                                          ZipOutputStream out )
        throws IOException
    {
        if ( out != null )
        {
            generateJSONZipEntry( report, context, upperCaseFirstChar, dumpContext, out, resourcesSrcPath );
        }
        else
        {
            generateJSONFile( report, context, upperCaseFirstChar, dumpContext, new File( baseDir, resourcesSrcPath ) );
        }

    }

    public static void generateJSONFile( IXDocReport report, IContext context, boolean upperCaseFirstChar,
                                         IContext dumpContext, File srcDir )
        throws IOException
    {
        String jsonFileName = getJSONFileName( report, dumpContext );

        JSONObject jsonObject = new JSONObject( context.getContextMap(), upperCaseFirstChar );

        File jsonFile = new File( srcDir, jsonFileName );
        jsonFile.getParentFile().mkdirs();
        Writer jsonWriter = null;
        try
        {
            jsonWriter = new FileWriter( jsonFile );
            String json = jsonObject.toString( 1 );
            IOUtils.write( json, jsonWriter );
        }
        finally
        {
            if ( jsonWriter != null )
            {
                IOUtils.closeQuietly( jsonWriter );
            }
        }
    }

    public static void generateJSONZipEntry( IXDocReport report, IContext context, boolean upperCaseFirstChar,
                                             IContext dumpContext, ZipOutputStream out, String src )
        throws IOException
    {
        String jsonFileName = getJSONFileName( report, dumpContext );

        JSONObject jsonObject = new JSONObject( context.getContextMap(), upperCaseFirstChar );

        ZipEntry zipEntry = new ZipEntry( src + "/" + jsonFileName );
        out.putNextEntry( zipEntry );

        Writer jsonWriter = null;
        try
        {
            jsonWriter = new OutputStreamWriter( out );
            String json = jsonObject.toString( 1 );
            IOUtils.write( json, jsonWriter );
        }
        finally
        {
            if ( jsonWriter != null )
            {
                jsonWriter.flush();
            }
            out.closeEntry();
        }
    }

    public static String getJSONFileName( IXDocReport report, IContext dumpContext )
    {
        String jsonFileName = report.getId() + ".json";
        dumpContext.put( "jsonFile", jsonFileName );
        return jsonFileName;
    }

    // XML fields available.

    public static void generateFieldsMetadataEntry( IXDocReport report, IContext dumpContext, String resourcesSrcPath,
                                                    File baseDir, ZipOutputStream out )
        throws IOException
    {
        if ( out != null )
        {
            generateFieldsMetadataZipEntry( report, dumpContext, out, resourcesSrcPath );
        }
        else
        {
            generateFieldsMetadataFile( report, dumpContext, new File( baseDir, resourcesSrcPath ) );
        }
    }

    public static void generateFieldsMetadataFile( IXDocReport report, IContext dumpContext, File srcDir )
        throws IOException
    {
        FieldsMetadata metadata = report.getFieldsMetadata();
        if ( metadata != null )
        {

            String xmlFieldsFileName = getXMLFieldsFileName( report, dumpContext );

            File xmlFieldsFile = new File( srcDir, xmlFieldsFileName );
            xmlFieldsFile.getParentFile().mkdirs();
            Writer xmlFieldsWriter = null;
            try
            {
                xmlFieldsWriter = new FileWriter( xmlFieldsFile );
                metadata.saveXML( xmlFieldsWriter, true, false );
            }
            finally
            {
                if ( xmlFieldsWriter != null )
                {
                    IOUtils.closeQuietly( xmlFieldsWriter );
                }
            }
        }
    }

    public static void generateFieldsMetadataZipEntry( IXDocReport report, IContext dumpContext, ZipOutputStream out,
                                                       String src )
        throws IOException
    {
        FieldsMetadata metadata = report.getFieldsMetadata();
        if ( metadata != null )
        {

            String xmlFieldsFileName = getXMLFieldsFileName( report, dumpContext );

            ZipEntry zipEntry = new ZipEntry( src + "/" + xmlFieldsFileName );
            out.putNextEntry( zipEntry );

            Writer xmlFieldsWriter = null;
            try
            {
                xmlFieldsWriter = new OutputStreamWriter( out );
                metadata.saveXML( xmlFieldsWriter, true, false );
            }
            finally
            {
                if ( xmlFieldsWriter != null )
                {
                    xmlFieldsWriter.flush();
                }
                out.closeEntry();
            }
        }
    }

    private static String getXMLFieldsFileName( IXDocReport report, IContext dumpContext )
    {
        String xmlFieldsFileName = report.getId() + ".fields.xml";
        dumpContext.put( "xmlFieldsFile", xmlFieldsFileName );
        return xmlFieldsFileName;
    }

    public static IContext createDumpContext( IXDocReport report, ITemplateEngine templateEngine, DumperOptions option )
        throws IOException
    {
        IContext dumpContext = templateEngine.createContext();

        if ( option != null )
        {
            String packageName = option.getPackageName();
            dumpContext.put( "packageName", packageName );
        }
        String className = DumpHelper.getClassName( report );
        dumpContext.put( "className", className );

        dumpContext.put( "templateEngineKind", templateEngine.getKind() );

        String outFileName = report.getId() + "_Out" + "." + report.getKind().toLowerCase();
        dumpContext.put( "outFileName", outFileName );

        return dumpContext;
    }

    public static void generate( ITemplateEngine templateEngine, String templateName, IContext dumpContext,
                                 OutputStream out )
        throws IOException, XDocReportException
    {
        Writer javaWriter = null;
        try
        {
            javaWriter = new OutputStreamWriter( out );
            templateEngine.process( templateName, dumpContext, javaWriter );
        }
        finally
        {
            if ( javaWriter != null )
            {
                IOUtils.closeQuietly( javaWriter );
            }
        }
    }

    public static void generateFile( ITemplateEngine templateEngine, String templateName, IContext dumpContext,
                                     File javaFile )
        throws IOException, XDocReportException
    {
        Writer javaWriter = null;
        try
        {
            javaFile.getParentFile().mkdirs();
            javaWriter = new FileWriter( javaFile );
            templateEngine.process( templateName, dumpContext, javaWriter );
        }
        finally
        {
            if ( javaWriter != null )
            {
                IOUtils.closeQuietly( javaWriter );
            }
        }
    }

    public static void generateZipEntry( ITemplateEngine templateEngine, String templateName, IContext dumpContext,
                                         String entryName, ZipOutputStream out )
        throws IOException, XDocReportException
    {
        ZipEntry zipEntry = new ZipEntry( entryName );
        out.putNextEntry( zipEntry );

        Writer javaWriter = null;
        try
        {
            javaWriter = new OutputStreamWriter( out );
            templateEngine.process( templateName, dumpContext, javaWriter );
        }
        finally
        {
            if ( javaWriter != null )
            {
                javaWriter.flush();
            }
            out.closeEntry();
        }
    }

    public static void generateEntry( ITemplateEngine templateEngine, String templateName, IContext dumpContext,
                                      String entryName, File baseDir, ZipOutputStream out )
        throws IOException, XDocReportException
    {
        if ( out != null )
        {
            // Zip
            generateZipEntry( templateEngine, templateName, dumpContext, entryName, out );
        }
        else
        {
            // File
            File file = new File( baseDir, entryName );
            generateFile( templateEngine, templateName, dumpContext, file );
        }
    }

}
