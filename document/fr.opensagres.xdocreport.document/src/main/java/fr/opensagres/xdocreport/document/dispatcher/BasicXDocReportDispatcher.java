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
package fr.opensagres.xdocreport.document.dispatcher;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class BasicXDocReportDispatcher<T extends IXDocReportController>
    extends AbstractXDocReportDispatcher<T>
{

    private final Map<String, T> controllersMap;

    public BasicXDocReportDispatcher()
    {
        this.controllersMap = new LinkedHashMap<String, T>();
    }

    public T getReportController( String reportId )
    {
        return controllersMap.get( reportId );
    }

    public void register( String reportId, T controller )
    {

        controllersMap.put( reportId, controller );
    }

    public void unregister( final T controller )
    {
        controllersMap.entrySet().removeIf(new Predicate<Map.Entry<String, T>>() {
            public boolean test(Map.Entry<String, T> t) {
                return t.getValue() == controller;
            }
        });
    }

    public void unregister( String reportId )
    {
        controllersMap.remove( reportId );
    }

    public Collection<T> getControllers()
    {
        return controllersMap.values();
    }

}
