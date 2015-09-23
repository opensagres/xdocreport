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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Converter to.
 */
public class ConverterTo
{

    private final String to;

    private final Map<String, ConverterVia> converters;

    public ConverterTo( String to )
    {
        this.to = to;
        this.converters = new HashMap<String, ConverterVia>();
    }

    public String getTo()
    {
        return to;
    }

    public Collection<ConverterVia> getConvertersVia()
    {
        return converters.values();
    }

    public ConverterVia getConverter( String via )
    {
        return converters.get( via );
    }

    /**
     * Returns the default converter or the first converter if none default converter.
     * 
     * @param with
     * @return
     */
    public IConverter getDefaultConverter( String with )
    {
        if ( with != null )
        {
            for ( ConverterVia converterVia : getConvertersVia() )
            {
                IConverter converter = converterVia.getConverter( with );
                if ( converter != null )
                {
                    return converter;
                }
            }
        }
        IConverter defaultConverter = null;
        for ( ConverterVia converterVia : getConvertersVia() )
        {
            IConverter converter = converterVia.getDefaultConverter();
            if ( converter.isDefault() )
            {
                return converter;
            }
            if ( defaultConverter == null )
            {
                defaultConverter = converter;
            }
        }
        return defaultConverter;
    }
    
    public ConverterVia getConverterVia( String via )
    {
        return converters.get( via );
    }
    
    public void addConverter( String via, String with, IConverter converter )
    {
        ConverterVia converterVia = getConverterVia( via );
        if ( converterVia == null )
        {
            converterVia = new ConverterVia( via );
            converters.put( via, converterVia );
        }
        converterVia.addConverter( with, converter );
     //   converters.put( via, converter );
    }

    public Collection<String> getVias()
    {
        return converters.keySet();
    }

}
