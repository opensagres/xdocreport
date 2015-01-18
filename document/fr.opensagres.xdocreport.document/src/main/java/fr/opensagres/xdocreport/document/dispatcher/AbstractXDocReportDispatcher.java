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

import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public abstract class AbstractXDocReportDispatcher<T extends IXDocReportController>
    implements IXDocReportDispatcher<T>
{

    public InputStream getSourceStream( String reportId )
        throws IOException
    {
        T controller = getReportController( reportId );
        if ( controller != null )
        {
            return controller.getSourceStream();
        }
        return null;
    }

    public String getTemplateEngineKind( String reportId )
    {
        T controller = getReportController( reportId );
        if ( controller != null )
        {
            return controller.getTemplateEngineKind();
        }
        return null;
    }

    public FieldsMetadata getFieldsMetadata( String reportId )
    {
        T controller = getReportController( reportId );
        if ( controller != null )
        {
            return controller.getFieldsMetadata();
        }
        return null;
    }

    public Boolean isCacheReport( String reportId )
    {
        T controller = getReportController( reportId );
        if ( controller != null )
        {
            return controller.isCacheReport();
        }
        return null;
    }

}
