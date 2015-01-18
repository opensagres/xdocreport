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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * API of dumper. A dumper is enable to dump the whole context when report is generated :
 * <ul>
 * <li>the document template of the report.</li>
 * <li>the data context which can be exported as JSON.</li>
 * <li>the {@link FieldsMetadata} which can be exported as XML.</li>
 * </ul>
 * 
 * @see https://code.google.com/p/xdocreport/wiki/XDocReportDumper
 */
public interface IDumper
{

    /**
     * Dump the given report and context by using the dumper configured with the given options and generates the result
     * of the dump in the given out. This method must be called only if
     * {@link IXDocReport#setCacheOriginalDocument(true)} is called when report is loaded.
     * 
     * @param report the {@link IXDocReport} to dump.
     * @param context the {@link IContext} to dump.
     * @param options the dumper options.
     * @param out the output stream where dump must be written.
     * @throws IOException
     * @throws XDocReportException
     */
    void dump( IXDocReport report, IContext context, DumperOptions options, OutputStream out )
        throws IOException, XDocReportException;

    /**
     * Dump the given report and context by using the dumper configured with the given options and generates the result
     * of the dump in the given out.
     * 
     * @param report the {@link IXDocReport} to dump.
     * @param documentIn the {@link InputStream} of the report.
     * @param context the {@link IContext} to dump.
     * @param options the dumper options.
     * @param out the output stream where dump must be written.
     * @throws IOException
     * @throws XDocReportException
     */
    void dump( IXDocReport report, InputStream documentIn, IContext context, DumperOptions option, OutputStream out )
        throws IOException, XDocReportException;

    /**
     * Returns mime mapping switch the kind of the dumper.
     * 
     * @return
     */
    MimeMapping getMimeMapping();
}
