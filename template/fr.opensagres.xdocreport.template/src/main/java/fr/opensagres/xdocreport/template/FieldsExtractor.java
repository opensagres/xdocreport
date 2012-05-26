/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package fr.opensagres.xdocreport.template;

import java.util.ArrayList;
import java.util.List;

public class FieldsExtractor<T extends FieldExtractor>
{

    private List<T> fields = new ArrayList<T>();

    public T addFieldName( String fieldName, boolean list )
    {
        for ( T field : fields )
        {
            if ( fieldName.equals( field.getName() ) )
            {
                return null;
            }
        }
        T field = createField( fieldName, list );
        if ( field != null )
        {
            fields.add( field );
        }
        return field;
    }

    public List<T> getFields()
    {
        return fields;
    }

    protected T createField( String fieldName, boolean list )
    {
        return (T) new FieldExtractor( fieldName, list );
    }

    public static FieldsExtractor<FieldExtractor> create()
    {
        return new FieldsExtractor<FieldExtractor>();
    }
}
