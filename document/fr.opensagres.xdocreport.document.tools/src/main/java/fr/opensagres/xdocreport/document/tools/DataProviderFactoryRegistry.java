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
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class DataProviderFactoryRegistry
    extends AbstractRegistry<IDataProviderFactory>
{

    private static final DataProviderFactoryRegistry INSTANCE = new DataProviderFactoryRegistry();

    private Map<String, IDataProviderFactory> factories = new HashMap<String, IDataProviderFactory>();

    public static DataProviderFactoryRegistry getRegistry()
    {
        return INSTANCE;
    }

    public DataProviderFactoryRegistry()
    {
        super( IDataProviderFactory.class );
    }

    @Override
    protected boolean registerInstance( IDataProviderFactory factory )
    {
        factories.put( factory.getId(), factory );
        return true;
    }

    @Override
    protected void doDispose()
    {
        factories.clear();
    }

    public IDataProvider create( String id, InputStream data, InputStream properties )
        throws Exception
    {
        if ( id == null )
        {
            return null;
        }
        IDataProviderFactory factory = getFactory( id );
        if ( factory == null )
        {
            return null;
        }
        return factory.create( data, properties );
    }

    public void generateDefaultData( String id, FieldsMetadata fieldsMetadata, OutputStream out )
        throws Exception
    {
        if ( id == null )
        {
            return;
        }
        IDataProviderFactory factory = getFactory( id );
        if ( factory == null )
        {
            return;
        }
        factory.generateDefaultData( fieldsMetadata, out );
    }

    public IDataProviderFactory getFactory( String id )
    {
        initializeIfNeeded();
        return factories.get( id );
    }

}
