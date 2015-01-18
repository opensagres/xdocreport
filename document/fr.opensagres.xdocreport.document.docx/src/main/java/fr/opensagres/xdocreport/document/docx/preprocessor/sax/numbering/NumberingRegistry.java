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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering;

import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class NumberingRegistry
{

    public static final String numbersMethod = "numbers";

    private List<NumberInfo> numbers;

    public NumberingRegistry()
    {
        this.numbers = new ArrayList<NumberInfo>();
    }

    public static boolean hasDynamicNumbering( FieldsMetadata fieldsMetadata )
    {
        if ( fieldsMetadata == null )
        {
            return false;
        }
        if ( fieldsMetadata.getFieldsAsTextStyling().size() < 1 )
        {
            return false;
        }
        return true;
    }

    public NumberInfo addNum( int abstractNumId, Integer maxNumId, boolean ordered )
    {
        int numId = maxNumId != null ? maxNumId.intValue() + 1 + numbers.size() : numbers.size()+1;
        NumberInfo info = new NumberInfo( numId, abstractNumId, ordered );
        numbers.add( info );
        return info;
    }

    public List<NumberInfo> getNumbers()
    {
        return numbers;
    }
}
