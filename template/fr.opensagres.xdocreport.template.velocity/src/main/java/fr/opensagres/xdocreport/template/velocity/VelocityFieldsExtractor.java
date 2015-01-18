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
package fr.opensagres.xdocreport.template.velocity;

import java.io.Reader;

import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.velocity.internal.ExtractVariablesVelocityVisitor;

public class VelocityFieldsExtractor
{

    private static final VelocityFieldsExtractor INSTANCE = new VelocityFieldsExtractor();

    public static VelocityFieldsExtractor getInstance()
    {
        return INSTANCE;
    }

    public void extractFields( Reader reader, String entryName, FieldsExtractor extractor )
        throws XDocReportException
    {
        try
        {
            SimpleNode document = RuntimeSingleton.parse( reader, entryName );
            ExtractVariablesVelocityVisitor visitor = new ExtractVariablesVelocityVisitor( extractor );
            visitor.setContext( null );
            document.jjtAccept( visitor, null );

        }
        catch ( ParseException e )
        {
            throw new XDocReportException( e );
        }
    }
}
