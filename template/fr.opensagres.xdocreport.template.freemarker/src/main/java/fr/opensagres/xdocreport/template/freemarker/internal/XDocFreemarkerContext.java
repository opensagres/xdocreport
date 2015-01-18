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
package fr.opensagres.xdocreport.template.freemarker.internal;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.utils.TemplateUtils;

/**
 * Freemarker context.
 */
public class XDocFreemarkerContext
    implements IContext
{

    public static final long serialVersionUID = 1L;

    private final Map<String, Object> map;

    public XDocFreemarkerContext()
    {
        this( new HashMap<String, Object>() );
    }

    public XDocFreemarkerContext( Map<String, Object> contextMap )
    {
        map = contextMap;
    }

    public Object put( String key, Object value )
    {
        Object result = TemplateUtils.putContextForDottedKey( this, key, value );
        if ( result == null )
        {
            return map.put( key, value );
        }
        return result;
    }

    public Object get( String key )
    {
        return map.get( key );
    }

    //@Override
    public void putMap( Map<String, Object> contextMap )
    {
        map.putAll( contextMap );
    }

	//@Override
	public Map<String, Object> getContextMap() {
		return map;
	}

}
