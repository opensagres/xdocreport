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
package fr.opensagres.xdocreport.template.internal;

import java.util.HashMap;

/**
 * DynamicBean is used to emulate Java Bean.
 */
public class DynamicBean
    extends HashMap<String, Object>
{

    private static final long serialVersionUID = 5652931397585026247L;

    public void setValue( String[] keys, Object value, int startIndex )
    {
        DynamicBean bean = this;
        String key = null;
        for ( int i = startIndex; i < keys.length; i++ )
        {
            key = keys[i];
            if ( i == keys.length - 1 )
            {
                if ( bean != null )
                {
                    bean.setValue( key, value );
                }
            }
            else
            {
                bean = bean.getDynamicBean( key );
            }
        }
    }

    private void setValue( String key, Object value )
    {
        super.put( key, value );
    }

    private DynamicBean getDynamicBean( String key )
    {
        Object result = super.get( key );
        if ( result == null )
        {
            DynamicBean bean = new DynamicBean();
            super.put( key, bean );
            return bean;
        }
        else if ( result instanceof DynamicBean )
        {
            return (DynamicBean) result;
        }
        return null;
    }

}
