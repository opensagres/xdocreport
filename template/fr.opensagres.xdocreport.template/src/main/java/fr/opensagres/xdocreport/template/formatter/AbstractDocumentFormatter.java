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
package fr.opensagres.xdocreport.template.formatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDocumentFormatter
    implements IDocumentFormatter
{

    public static final String NO_ESCAPE = "___NoEscape";

    private String startDocumentDirective;

    private String endDocumentDirective;

    protected enum DirectiveToParse
    {
        START_LOOP, END_LOOP, START_IF, END_IF
    }

    public String extractItemNameList( String content, String fieldName, boolean forceAsField )
    {
        if ( !forceAsField && !isModelField( content, fieldName ) )
        {
            return null;
        }
        int dotIndex = fieldName.indexOf( '.' );
        if ( dotIndex != -1 )
        {
            return getItemToken() + fieldName.substring( 0, dotIndex );
        }
        return getItemToken() + fieldName;
    }

    public String getStartDocumentDirective()
    {
        return startDocumentDirective;
    }

    public void setStartDocumentDirective( String startDocumentDirective )
    {
        this.startDocumentDirective = startDocumentDirective;
    }

    public String getEndDocumentDirective()
    {
        return endDocumentDirective;
    }

    public void setEndDocumentDirective( String endDocumentDirective )
    {
        this.endDocumentDirective = endDocumentDirective;
    }

    public String getStartLoopDirective( String itemNameList )
    {
        return getStartLoopDirective( itemNameList, itemNameList.substring( getItemToken().length() ) );
    }

    public int extractListDirectiveInfo( String content, DirectivesStack directives )
    {
        return extractListDirectiveInfo( content, directives, false );
    }

    protected abstract String getItemToken();

    protected abstract boolean isModelField( String content, String fieldName );

    protected DirectiveToParse getDirectiveToParse( int startLoopIndex, int endLoopIndex, int startIfIndex,
                                                    int endIfIndex )
    {
        int minIndex = getMinIndex( startLoopIndex, endLoopIndex, startIfIndex, endIfIndex );
        if ( minIndex == -1 )
        {
            return null;
        }
        if ( minIndex == startLoopIndex )
        {
            return DirectiveToParse.START_LOOP;
        }
        if ( minIndex == endLoopIndex )
        {
            return DirectiveToParse.END_LOOP;
        }
        if ( minIndex == startIfIndex )
        {
            return DirectiveToParse.START_IF;
        }
        if ( minIndex == endIfIndex )
        {
            return DirectiveToParse.END_IF;
        }
        return null;
    }

    public int getMinIndex( int startLoopIndex, int endLoopIndex, int startIfIndex, int endIfIndex )
    {
        List<Integer> coll = new ArrayList<Integer>();
        if ( startLoopIndex != -1 )
        {
            coll.add( startLoopIndex );
        }
        if ( endLoopIndex != -1 )
        {
            coll.add( endLoopIndex );
        }
        if ( startIfIndex != -1 )
        {
            coll.add( startIfIndex );
        }
        if ( endIfIndex != -1 )
        {
            coll.add( endIfIndex );
        }
        if ( coll.size() == 0 )
        {
            return -1;
        }
        if ( coll.size() == 1 )
        {
            return coll.get( 0 );
        }
        return Collections.min( coll );
    }

    protected String getVariableName( long variableIndex )
    {
        StringBuilder key = new StringBuilder( NO_ESCAPE );
        key.append( variableIndex );
        return key.toString();
    }

    public String formatAsSimpleField( boolean encloseInDirective, String... fields )
    {
        return formatAsSimpleField( false, encloseInDirective, fields );
    }

    public String getFunctionDirective( String key, String methodName, String... parameters )
    {
        return getFunctionDirective( true, key, methodName, parameters );
    }
    
    public String getSetDirective( String name, String value )
    {
        return getSetDirective( name, value, true );
    }
    
    public String getStartIfDirective( String fieldName )
    {
        return getStartIfDirective( fieldName, true );
    }
}
