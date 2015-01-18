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
package fr.opensagres.xdocreport.template.velocity.internal;

import java.util.Stack;

import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.ASTprocess;
import org.apache.velocity.runtime.visitor.BaseVisitor;

import fr.opensagres.xdocreport.template.FieldsExtractor;

public class ExtractVariablesVelocityVisitor
    extends BaseVisitor
{

    private final FieldsExtractor extractor;

    public ExtractVariablesVelocityVisitor( FieldsExtractor extractor )
    {
        this.extractor = extractor;
    }

    private Stack<Foreach> foreachStack = new Stack<Foreach>();

    @Override
    public Object visit( ASTprocess node, Object data )
    {
        // TODO Auto-generated method stub
        return super.visit( node, data );
    }

    @Override
    public Object visit( ASTDirective node, Object data )
    {
        boolean isForeach = "foreach".equals( node.getDirectiveName() );
        if ( isForeach )
        {
            foreachStack.push( new Foreach() );
        }
        Object o = super.visit( node, data );
        if ( isForeach )
        {
            foreachStack.pop();
        }
        return o;
    }

    @Override
    public Object visit( ASTReference node, Object data )
    {
        String variableName = node.literal();
        if ( variableName.startsWith( "$" ) )
        {
            variableName = variableName.substring( 1, variableName.length() );
        }
        if ( !foreachStack.isEmpty() )
        {
            Foreach currentForeach = foreachStack.peek();
            if ( currentForeach.getItem() == null )
            {
                currentForeach.setItem( variableName );
            }
            else if ( currentForeach.getSequence() == null )
            {
                currentForeach.setSequence( variableName );
            }
            else
            {
                String firstToken = variableName;
                int index = firstToken.indexOf( '.' );
                if ( index != -1 )
                {
                    firstToken = firstToken.substring( 0, index );
                }
                if ( firstToken.equals( currentForeach.getItem() ) )
                {
                    String field = "";
                    if ( index != -1 )
                    {
                        field = variableName.substring( index, variableName.length() );
                    }
                    extractor.addFieldName( currentForeach.getSequence() + field, true );
                }
                else {
                    extractor.addFieldName( variableName, false );
                }
            }
        }
        else
        {
            extractor.addFieldName( variableName, false );
        }
        return super.visit( node, data );
    }
}
