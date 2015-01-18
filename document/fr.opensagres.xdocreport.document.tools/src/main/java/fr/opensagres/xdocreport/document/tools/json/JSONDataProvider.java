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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.tools.AbstractDataProvider;
import fr.opensagres.xdocreport.template.IContext;

public class JSONDataProvider
    extends AbstractDataProvider
{

    private JSONObject jsonObject;

    public JSONDataProvider( InputStream data, InputStream properties )
        throws Exception
    {
        super( data, properties );
        jsonObject = new JSONObject( getDataAsString() );
    }

    public void populateContext( IXDocReport report, IContext context )
        throws IOException, XDocReportException
    {
        Iterator i = jsonObject.keys();
        while ( i.hasNext() )
        {
            String key = (String) i.next();
            try
            {
                Object value = jsonObject.get( key );
                if ( value instanceof JSONObject )
                {
                    Map subBean = toMap( (JSONObject) value );
                    context.put( key, subBean );
                }
                else if ( value instanceof JSONArray )
                {
                    JSONArray array = (JSONArray) value;
                    int length = array.length();
                    List list = new ArrayList( length );
                    for ( int j = 0; j < length; j++ )
                    {
                        Object itemValue = array.get( j );
                        if ( itemValue instanceof JSONObject )
                        {
                            list.add( toMap( (JSONObject) itemValue ) );
                        }
                        else
                        {
                            list.add( itemValue );
                        }
                    }
                    context.put( key, list );
                }
                else
                {
                    context.put( key, value );
                }
            }
            catch ( JSONException e )
            {
                throw new XDocReportException( e );
            }
        }
    }

    private Map toMap( JSONObject jsonObject )
        throws JSONException
    {
        Map parentBean = new HashMap();
        Iterator i = jsonObject.keys();
        while ( i.hasNext() )
        {
            String key = (String) i.next();
            Object value = jsonObject.get( key );
            if ( value instanceof JSONObject )
            {
                Map subBean = toMap( (JSONObject) value );
                parentBean.put( key, subBean );
            }
            else if ( value instanceof JSONArray )
            {
                JSONArray array = (JSONArray) value;
                int length = array.length();
                List list = new ArrayList( length );
                for ( int j = 0; j < length; j++ )
                {
                    Object itemValue = array.get( j );
                    if ( itemValue instanceof JSONObject )
                    {
                        list.add( toMap( (JSONObject) itemValue ) );
                    }
                    else
                    {
                        list.add( itemValue );
                    }
                }
                parentBean.put( key, list );
            }
            else
            {
                parentBean.put( key, value );
            }
        }
        return parentBean;
    }
}
