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
package fr.opensagres.xdocreport.document.tools.json;

import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.document.tools.AbstractDataProviderFactory;
import fr.opensagres.xdocreport.document.tools.IDataProvider;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class JSONDataProviderFactory
    extends AbstractDataProviderFactory
{

    private static final String ID = "json";

    private static final String DESCRIPTION = "JSON Data Provider";

    public JSONDataProviderFactory()
    {
        super( ID, DESCRIPTION );
    }

    public IDataProvider create( InputStream data, InputStream properties )
        throws Exception
    {
        return new JSONDataProvider( data, properties );
    }

    public void generateDefaultData( FieldsMetadata fieldsMetadata, OutputStream out )
        throws Exception
    {
        // Generate JSON
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, out, true );
    }
}
