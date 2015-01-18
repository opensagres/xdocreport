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
package fr.opensagres.xdocreport.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.converter.discovery.IConverterDiscovery;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.registry.AbstractRegistry;

/**
 * {@link IConverter} registry.
 */
public class ConverterRegistry
    extends AbstractRegistry<IConverterDiscovery>
{

    private static final ConverterRegistry INSTANCE = new ConverterRegistry();

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( ConverterRegistry.class.getName() );

    private Map<String /* from */, ConverterFrom> converters = new HashMap<String, ConverterFrom>();

    public ConverterRegistry()
    {

        super( IConverterDiscovery.class );

    }

    public static ConverterRegistry getRegistry()
    {
        return INSTANCE;
    }

    public IConverter findConverter( Options options )
        throws XDocConverterException
    {
        return findConverter( options.getFrom(), options.getTo(), options.getVia() );
    }

    public IConverter findConverter( String from, String to, String via )
        throws XDocConverterException
    {
        return internalFindConverter( from, to, via, true );
    }

    public IConverter getConverter( Options options )
    {
        return getConverter( options.getFrom(), options.getTo(), options.getVia() );
    }

    public IConverter getConverter( String from, String to, String via )
    {
        try
        {
            return internalFindConverter( from, to, via, false );
        }
        catch ( XDocConverterException e )
        {
            if ( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( e.getMessage() );
            }
            return null;
        }
    }

    public ConverterFrom getConverterFrom( String from )
    {
        initializeIfNeeded();
        return converters.get( from );
    }

    public Set<String> getFroms()
    {
        initializeIfNeeded();
        return converters.keySet();
    }

    private IConverter internalFindConverter( String from, String to, String via, boolean throwError )
        throws XDocConverterException
    {
        initializeIfNeeded();
        ConverterFrom fromConverters = getConverterFrom( from );
        if ( fromConverters == null )
        {
            if ( throwError )
            {
                String msg = String.format( "Cannot find converters from=%s", from );
                LOGGER.severe( msg );
                throw new XDocConverterException( msg );
            }
            return null;
        }
        ConverterTo toConverters = fromConverters.getConverterTo( to );
        if ( toConverters == null )
        {
            if ( throwError )
            {
                String msg = String.format( "Cannot find converters for to=%s for from=%s", to, from );
                LOGGER.severe( msg );
                throw new XDocConverterException( msg );
            }
            return null;
        }
        if ( via == null )
        {
            IConverter converter = toConverters.getDefaultConverter();
            if ( throwError && converter == null )
            {
                if ( throwError )
                {
                    String msg = String.format( "Cannot find converters for to=%s for from=%s", to, from );
                    LOGGER.severe( msg );
                    throw new XDocConverterException( msg );
                }
            }
            return converter;
        }

        IConverter converter = toConverters.getConverter( via );
        if ( converter == null )
        {
            if ( throwError )
            {
                String msg = String.format( "Cannot find converters via %s for to=%s for from=%s", via, to, from );
                LOGGER.severe( msg );
                throw new XDocConverterException( msg );
            }
        }
        return converter;
    }

    @Override
    protected void doDispose()
    {
        this.converters.clear();
    }

    @Override
    protected boolean registerInstance( IConverterDiscovery discovery )
    {
        String from = discovery.getFrom();
        String to = discovery.getTo();
        String via = discovery.getVia();
        IConverter converter = discovery.getConverter();
        ConverterFrom converterFrom = converters.get( from );
        if ( converterFrom == null )
        {
            converterFrom = new ConverterFrom( from );
            converters.put( converterFrom.getFrom(), converterFrom );
        }
        converterFrom.addConverter( to, via, converter );
        return true;
    }
}
