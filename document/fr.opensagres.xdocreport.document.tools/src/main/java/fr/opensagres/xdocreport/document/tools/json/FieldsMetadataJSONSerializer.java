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
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class FieldsMetadataJSONSerializer
{

    private static final String LF = System.getProperty( "line.separator" );

    private static final String TAB = "\t";

    private static final FieldsMetadataJSONSerializer INSTANCE = new FieldsMetadataJSONSerializer();

    public static FieldsMetadataJSONSerializer getInstance()
    {
        return INSTANCE;
    }

    public void save( FieldsMetadata fieldsMetadata, Writer writer, boolean indent )
        throws IOException, JSONException
    {
        save( fieldsMetadata, writer, null, indent );
    }

    public void save( FieldsMetadata fieldsMetadata, OutputStream out, boolean indent )
        throws IOException, JSONException
    {
        save( fieldsMetadata, null, out, indent );
    }

    private void save( FieldsMetadata fieldsMetadata, Writer writer, OutputStream out, boolean indent )
        throws IOException, JSONException
    {
        Map<String, Object> bean = toMap( fieldsMetadata );
        JSONObject json = new JSONObject( bean );
        if ( indent )
        {
            write( json.toString( 1 ), writer, out );
        }
        else
        {
            write( json.toString(), writer, out );
        }
    }

    private Map<String, Object> toMap( FieldsMetadata fieldsMetadata )
    {
        Map<String, Object> bean = new LinkedHashMap<String, Object>();
        Collection<FieldMetadata> fields = fieldsMetadata.getFields();
        String fieldName = null;
        String[] names = null;
        for ( FieldMetadata field : fields )
        {
            toMap( bean, field.getFieldName(), field.isListType(), -1 );
        }
        return bean;
    }

    private void toMap( Map root, String fieldName, boolean list, int itemIndex )
    {
        int index = fieldName.indexOf( '.' );
        if ( index != -1 )
        {
            String[] names = null;
            names = fieldName.split( "[.]" );
            Map bean = null;
            List<Map> beanList = null;
            Object o = null;
            for ( int i = 0; i < names.length; i++ )
            {
                fieldName = names[i];
                if ( i == 0 )
                {
                    o = root.get( fieldName );
                    if ( o == null )
                    {
                        if ( list )
                        {
                            beanList = new ArrayList<Map>();
                            root.put( fieldName, beanList );
                        }
                        else
                        {
                            bean = new LinkedHashMap();
                            root.put( fieldName, bean );
                        }
                    }
                    else
                    {
                        if ( list )
                        {
                            beanList = (List) o;
                        }
                        else
                        {
                            bean = (Map) o;
                        }
                    }

                }
                else
                {
                    if ( beanList != null )
                    {
                        if ( beanList.size() < 1 )
                        {
                            for ( int j = 0; j < 10; j++ )
                            {
                                beanList.add( new LinkedHashMap() );
                            }
                        }
                        for ( int j = 0; j < 10; j++ )
                        {
                            toMap( beanList.get( j ), fieldName, false, j );
                        }

                    }
                    else
                    {
                        toMap( bean, fieldName, false, -1 );
                    }

                }
            }
        }
        else
        {
            if ( list )
            {
                Collection beanList = new ArrayList();
                for ( int i = 0; i < 10; i++ )
                {

                }
            }
            else
            {
                root.put( fieldName, getValue( fieldName, itemIndex ) );
            }
        }
    }

    protected String getValue( String fieldName, int index )
    {
        StringBuilder value = new StringBuilder( fieldName );
        value.append( "_Value" );
        if ( index != -1 )
        {
            value.append( index );
        }
        return value.toString();
    }

    private String getFirstToken( String fieldName )
    {
        int index = fieldName.indexOf( '.' );
        if ( index != -1 )
        {
            return fieldName.substring( 0, index );
        }
        return fieldName;

    }

    private void toMap( Map<String, Object> bean, FieldMetadata field )
    {
        String fieldName;
        String[] names;
        fieldName = field.getFieldName();
        if ( fieldName.indexOf( '.' ) != -1 )
        {
            names = fieldName.split( "[.]" );
            for ( int i = 0; i < names.length; i++ )
            {
                fieldName = names[i];
                if ( i == 0 )
                {

                }
            }

        }
        else
        {
            bean.put( fieldName, fieldName + "_Value" );
        }
    }

    private void write( String s, Writer writer, OutputStream out )
        throws IOException
    {
        if ( writer == null )
        {
            out.write( s.getBytes() );
        }
        else
        {
            writer.write( s );
        }
    }

}
