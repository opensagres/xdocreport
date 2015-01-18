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

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

public abstract class AbstractDumper
    implements IDumper
{

    public void dump( IXDocReport report, IContext context, DumperOptions option, OutputStream out )
        throws IOException, XDocReportException
    {
        dump( report, null, context, option, out );

    }

    public final void dump( IXDocReport report, InputStream documentIn, IContext context, DumperOptions option,
                            OutputStream out )
        throws IOException, XDocReportException
    {
        try
        {
            ITemplateEngine templateEngine =
                TemplateEngineRegistry.getRegistry().getTemplateEngine( report.getTemplateEngine().getKind() );
            if ( documentIn == null )
            {
                documentIn = DumpHelper.getDocument( report );
            }
            doDump( report, documentIn, context, option, templateEngine, out );
        }
        finally
        {
            if ( documentIn != null )
            {
                IOUtils.closeQuietly( documentIn );
            }
            if ( out != null )
            {
                IOUtils.closeQuietly( out );
            }
        }
    }

    protected abstract void doDump( IXDocReport report, InputStream documentIn, IContext context, DumperOptions option,
                                    ITemplateEngine templateEngine, OutputStream out )
        throws IOException, XDocReportException;
}
