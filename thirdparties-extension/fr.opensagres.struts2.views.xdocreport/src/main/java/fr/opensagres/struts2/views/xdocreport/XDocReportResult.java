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
package fr.opensagres.struts2.views.xdocreport;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public class XDocReportResult
    extends AbstractXDocReportResult
{

    private static final Map<Class, Boolean> isPopulateContextAwareClassCache = new HashMap<Class, Boolean>();

    @Override
    protected void populateContext( IXDocReport report, IContext context, String finalLocation,
                                    ActionInvocation invocation )
        throws Exception
    {
        ValueStack stack = invocation.getStack();
        Object action = stack.findValue( ACTION_KEY );
        if ( action == null )
        {
            throw new XDocReportException( "Cannot retrieve action instance in value stack with key=" + ACTION_KEY );
        }
        PopulateContextAware populateContextAware = getPopulateContextAware( action );
        if ( populateContextAware == null )
        {
            throw new XDocReportException( "Action " + action.getClass().getName() + " doesn't implement "
                + PopulateContextAware.class.getName() );
        }
        populateContextAware.populateContext( report, context );

    }

    protected PopulateContextAware getPopulateContextAware( Object action )
    {
        Boolean result = isPopulateContextAwareClassCache.get( action.getClass() );
        if ( result == null )
        {
            result = ( action instanceof PopulateContextAware );
            isPopulateContextAwareClassCache.put( action.getClass(), result );
        }
        if ( result )
        {
            return ( (PopulateContextAware) action );
        }
        return null;
    }
}
