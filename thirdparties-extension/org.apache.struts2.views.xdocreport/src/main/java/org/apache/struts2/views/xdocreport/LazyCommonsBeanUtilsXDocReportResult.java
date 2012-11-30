/**
 * Copyright (C) 2011-2012 The XDocReport Team <xdocreport@googlegroups.com>
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
package org.apache.struts2.views.xdocreport;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public class LazyCommonsBeanUtilsXDocReportResult
    extends AbstractXDocReportResult
{

    private static final long serialVersionUID = -5990250569335023528L;

    /**
     * @param report
     * @param finalLocation
     * @param invocation
     * @return
     * @throws XDocReportException
     */
    @Override
    protected void populateContext( IXDocReport report, IContext context, String finalLocation,
                                    ActionInvocation invocation )
        throws Exception
    {
        String[] expressions = getExpressions();
        for ( int i = 0; i < expressions.length; i++ )
        {
            populateContext( report, context, expressions[i], finalLocation, invocation );
        }
    }

    protected void populateContext( IXDocReport report, IContext context, String expression, String finalLocation,
                                    ActionInvocation invocation )
        throws Exception
    {

        ValueStack stack = invocation.getStack();
        Object action = stack.findValue( expression );
        LazyCommonsBeanUtilsPopulateContext.getInstance().populate( context, action );
    }

}
