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

import fr.opensagres.xdocreport.core.document.DocumentKind;

public class Options
{

    private String from;

    private String to;

    private String via;

    private Map<String, Object> properties = null;

    private Options()
    {
    }

    public static Options getFrom( DocumentKind from )
    {
        return getFrom( from.name() );
    }

    public static Options getFrom( String from )
    {
        Options options = new Options();
        options.from = from;
        return options;
    }

    public static Options getTo( ConverterTypeTo to )
    {
        return getTo( to.name() );
    }

    public static Options getTo( String to )
    {
        Options options = new Options();
        options.to = to;
        return options;
    }

    public String getFrom()
    {
        return from;
    }

    public Options from( String from )
    {
        this.from = from;
        return this;
    }

    public Options from( DocumentKind from )
    {
        return from( from.name() );
    }

    public String getTo()
    {
        return to;
    }

    public Options to( String to )
    {
        this.to = to;
        return this;
    }

    public Options to( ConverterTypeTo to )
    {
        return to( to.name() );
    }

    public String getVia()
    {
        return via;
    }

    public Options via( String via )
    {
        this.via = via;
        return this;
    }

    public Options via( ConverterTypeVia via )
    {
        return via( via.name() );
    }

    public Object getSubOptions( Class<?> subOptionsClass )
    {
        return subOptionsClass != null ? getProperty( subOptionsClass.getName() ) : null;
    }

    /**
     * Set sub-options specific to some stage of processing. In example to pass options to pdf converter use
     * construction like
     * <code>Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.ITEXT).subOptions(PDFViaITextOptions.create().fontEncoding("windows-1250"))</code>
     * 
     * @param subOptionsValue sub-options to set
     * @return this instance
     */
    public Options subOptions( Object subOptionsValue )
    {
        if ( subOptionsValue != null )
        {
            Class<?> clazz = subOptionsValue.getClass();
            String clazzName;
            if (clazz.isAnonymousClass()) {
                // not sure if getInterfaces can return null
                if (clazz.getInterfaces() != null && clazz.getInterfaces().length > 0) {
                    // anonymous classes can implement only one interface
                    clazzName = clazz.getInterfaces()[0].getName();
                }
                else {
                    clazzName = clazz.getSuperclass().getName();
                }
            }
            else {
                clazzName = clazz.getName();
            }
            setProperty( clazzName, subOptionsValue );
        }
        return this;
    }

    public void setProperty( String name, Object value )
    {
        if ( properties == null )
        {
            properties = new HashMap<String, Object>();
        }
        properties.put( name, value );
    }

    public Object getProperty( String name )
    {
        if ( properties == null )
        {
            return null;
        }
        return properties.get( name );
    }

    public boolean hasProperty( String name )
    {
        if ( properties == null )
        {
            return false;
        }
        return properties.containsKey( name );
    }
}
