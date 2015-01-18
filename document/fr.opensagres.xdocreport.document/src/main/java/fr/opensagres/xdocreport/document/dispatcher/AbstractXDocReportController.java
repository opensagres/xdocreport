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
package fr.opensagres.xdocreport.document.dispatcher;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public abstract class AbstractXDocReportController
    implements IXDocReportController
{

    private final String templateEngineKind;

    private final String converterTypeFrom;

    private final String fileExtension;

    private FieldsMetadata fieldsMetadata;

    public AbstractXDocReportController( TemplateEngineKind templateEngineKind, DocumentKind converterTypeFrom )
    {
        this( templateEngineKind.name(), converterTypeFrom.name() );
    }

    public AbstractXDocReportController( String templateEngineKind, String converterTypeFrom )
    {
        this.templateEngineKind = templateEngineKind;
        this.converterTypeFrom = converterTypeFrom;
        this.fileExtension = converterTypeFrom.toLowerCase();
    }

    public String getTemplateEngineKind()
    {
        return templateEngineKind;
    }

    public String getConverterTypeFrom()
    {
        return converterTypeFrom;
    }

    public String getFileExtension()
    {
        return fileExtension;
    }

    public FieldsMetadata getFieldsMetadata()
    {
        if ( fieldsMetadata == null )
        {
            fieldsMetadata = createFieldsMetadata();
        }
        return fieldsMetadata;
    }

    protected abstract FieldsMetadata createFieldsMetadata();

    public boolean isCacheReport()
    {
        return true;
    }
}
