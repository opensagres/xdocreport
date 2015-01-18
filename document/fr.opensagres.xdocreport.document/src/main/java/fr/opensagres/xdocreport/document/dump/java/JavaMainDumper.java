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
package fr.opensagres.xdocreport.document.dump.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.dump.AbstractDumper;
import fr.opensagres.xdocreport.document.dump.DumpHelper;
import fr.opensagres.xdocreport.document.dump.DumperKind;
import fr.opensagres.xdocreport.document.dump.DumperOptions;
import fr.opensagres.xdocreport.document.dump.IDumper;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * {@link IDumper} implementation which dump a report and context to generate a single Java Main class which generates a
 * report :
 * <ul>
 * <li>the template document is encoded as binary 64 String.</li>
 * <li>the data context is encoded as binary 64 as JSON.</li>
 * <li>the fields metadata is encoded as binary 64 as XML.</li>
 * </ul>
 */
public class JavaMainDumper
    extends AbstractDumper
{
    private static final IDumper INSTANCE = new JavaMainDumper();

    public static IDumper getInstance()
    {
        return INSTANCE;
    }

    public static class JavaMainDumperOptions
        extends DumperOptions
    {

        public JavaMainDumperOptions()
        {
            super( DumperKind.JavaMain );
        }

    }

    @Override
    protected void doDump( IXDocReport report, InputStream documentIn, IContext context, DumperOptions option,
                           ITemplateEngine templateEngine, OutputStream out )
        throws IOException, XDocReportException
    {

        IContext dumpContext = DumpHelper.createDumpContext( report, templateEngine, option );

        // document (docx, odt).
        String documentAsBinaryB4 = DumpHelper.toDocumentAsBinary64( report );
        dumpContext.put( "document", documentAsBinaryB4 );

        // XML fields
        String xmlFields = null;
        FieldsMetadata metadata = report.getFieldsMetadata();
        if ( metadata != null )
        {
            StringWriter xmlFieldsWriter = new StringWriter();
            metadata.saveXML( xmlFieldsWriter, false, true );
            xmlFields = xmlFieldsWriter.toString();
        }
        dumpContext.put( "xmlFields", xmlFields );

        // JSON as data
        boolean upperCaseFirstChar = templateEngine.isFieldNameStartsWithUpperCase();
        String json = DumpHelper.toJSON( context, upperCaseFirstChar, true );
        dumpContext.put( "json", json );

        // Java Main
        DumpHelper.generate( templateEngine, DumpHelper.JAVA_MAIN_DUMP_TEMPLATE, dumpContext, out );
    }

    //@Override
    public MimeMapping getMimeMapping()
    {
        return null;
    }
}
