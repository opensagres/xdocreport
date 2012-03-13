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

public abstract class Directive
{

    public enum DirectiveType
    {
        LOOP, IF
    }

    private final Directive parent;

    private final String startDirective;

    private final String endDirective;

    public Directive( Directive parent, String startDirective, String endDirective )
    {
        this.parent = parent;
        this.startDirective = startDirective;
        this.endDirective = endDirective;
    }

    public String getStartDirective()
    {
        return startDirective;
    }

    public String getEndDirective()
    {
        return endDirective;
    }

    public abstract DirectiveType getType();

    public Directive getParent()
    {
        return parent;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append( "[START_" );
        s.append( getType() );
        s.append( ": " );
        s.append( startDirective );
        s.append( ", END_" );
        s.append( getType() );
        s.append( ": " );
        s.append( endDirective );
        return s.toString();
    }

    public static String formatDirective( String directive, String startNoParse, String endNoParse )
    {
        if ( startNoParse != null )
        {
            return endNoParse + directive + startNoParse;
        }
        return directive;
    }

    public static String getStartNoParse( IDocumentFormatter formatter, FieldsMetadata fieldsMetadata )
    {
        if ( isEvaluateEngineOnlyForFields( formatter, fieldsMetadata ) )
        {
            return formatter.getStartNoParse();
        }
        return null;

    }

    public static String getEndNoParse( IDocumentFormatter formatter, FieldsMetadata fieldsMetadata )
    {
        if ( isEvaluateEngineOnlyForFields( formatter, fieldsMetadata ) )
        {
            return formatter.getEndNoParse();
        }
        return null;
    }

    public static boolean isEvaluateEngineOnlyForFields( IDocumentFormatter formatter, FieldsMetadata fieldsMetadata )
    {
        if ( formatter == null )
        {
            return false;
        }
        if ( fieldsMetadata == null || !fieldsMetadata.isEvaluateEngineOnlyForFields() )
        {
            return false;
        }
        return true;
    }

}
