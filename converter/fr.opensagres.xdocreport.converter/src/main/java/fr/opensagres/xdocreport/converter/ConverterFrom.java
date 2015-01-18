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
 * Converter from.
 */
public class ConverterFrom
{

    private final String from;

    private final Map<String, ConverterTo> convertersTo;

    public ConverterFrom( String from )
    {
        this.from = from;
        this.convertersTo = new HashMap<String, ConverterTo>();
    }

    public String getFrom()
    {
        return from;
    }

    public Collection<ConverterTo> getConvertersTo()
    {
        return convertersTo.values();
    }

    public ConverterTo getConverterTo( String to )
    {
        return convertersTo.get( to );
    }

    public void addConverter( String to, String via, IConverter converter )
    {
        ConverterTo converterTo = getConverterTo( to );
        if ( converterTo == null )
        {
            converterTo = new ConverterTo( to );
            convertersTo.put( to, converterTo );
        }
        converterTo.addConverter( via, converter );
    }
}
