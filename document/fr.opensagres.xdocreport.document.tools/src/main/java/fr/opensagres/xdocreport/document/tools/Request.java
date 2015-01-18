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

import java.io.InputStream;
import java.util.HashMap;

import fr.opensagres.xdocreport.document.Generator;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class Request
    extends HashMap<String, String>
{

    private static final long serialVersionUID = 4987378991548527030L;

    private final InputStream in;

    private final FieldsMetadata fieldsMetadata;

    private final Iterable<IDataProvider> dataProviders;

    public Request( InputStream in, String templateEngineKind, FieldsMetadata fieldsMetadata,
                    Iterable<IDataProvider> dataProviders )
    {
        this.in = in;
        super.put( Generator.TEMPLATE_ENGINE_KIND_HTTP_PARAM, templateEngineKind );
        this.put( Generator.REPORT_ID_HTTP_PARAM, "generated" );
        this.fieldsMetadata = fieldsMetadata;
        this.dataProviders = dataProviders;
    }

    public InputStream getIn()
    {
        return in;
    }

    public Iterable<IDataProvider> getDataProviders()
    {
        return dataProviders;
    }

    public FieldsMetadata getFieldsMetadata()
    {
        return fieldsMetadata;
    }
}
