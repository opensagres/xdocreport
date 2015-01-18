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
package fr.opensagres.xdocreport.template.freemarker.cache;

import java.io.IOException;
import java.io.Reader;

import fr.opensagres.xdocreport.core.io.IEntryInfo;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.cache.ITemplateCacheInfoProvider;
import fr.opensagres.xdocreport.template.utils.TemplateUtils;
import freemarker.cache.TemplateLoader;

/**
 * Freemarker template loader {@link TemplateLoader} implementation used to cache entry name of {@link XDocArchive}
 * which must be merged with Java model with freemarker template engine.
 */
public class XDocReportEntryTemplateLoader
    implements TemplateLoader
{

    private ITemplateEngine templateEngine;

    public XDocReportEntryTemplateLoader( ITemplateEngine freemarkerTemplateEngine )
    {
        this.templateEngine = freemarkerTemplateEngine;
    }

    public Object findTemplateSource( final String name )
        throws IOException
    {
        ITemplateCacheInfoProvider templateCacheInfoProvider = templateEngine.getTemplateCacheInfoProvider();
        return TemplateUtils.getTemplateCacheInfo( templateCacheInfoProvider, name );
        //
        // // Name received (see getCachedTemplateName) is like
        // // this $reportId '!' $entryName
        // int index = getIndexReportEntryName(name);
        // if (index == -1) {
        // return null;
        // }
        // String reportId = getReportId(name, index);
        // String entryName = getEntryName(name, index);
        //
        // // Retrieve the report with the registry and create
        // // XDocReportEntrySource
        // // to set which entry name must be used as template.
        // return new XDocReportEntrySource(XDocReportRegistry.getRegistry()
        // .getReport(reportId), entryName);
    }

    public long getLastModified( final Object templateSource )
    {
        // Get XDocReportEntrySource created with findTemplateSource
        IEntryInfo cacheInfo = (IEntryInfo) templateSource;
        return cacheInfo.getLastModified();
        // XDocReportEntrySource entrySource = (XDocReportEntrySource)
        // templateSource;
        // IXDocReport report = entrySource.getReport();
        // String entryName = entrySource.getEntryName();
        // return report.getDocumentArchive().getLastModifiedEntry(entryName);
    }

    public Reader getReader( final Object templateSource, final String encoding )
        throws IOException
    {
        IEntryInfo cacheInfo = (IEntryInfo) templateSource;
        return cacheInfo.getReader();
        // Get XDocReportEntrySource created with findTemplateSource
        // XDocReportEntrySource entrySource = (XDocReportEntrySource)
        // templateSource;
        // IXDocReport report = entrySource.getReport();
        // String entryName = entrySource.getEntryName();
        // // Returns the reader of the entry document archive of the report.
        // return report.getDocumentArchive().getEntryReader(entryName);
    }

    public void closeTemplateSource( Object templateSource )
    {
        // Do nothing.
    }

}
