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

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.TemplateContextHelper;
import fr.opensagres.xdocreport.template.formatter.AbstractDocumentFormatter;
import fr.opensagres.xdocreport.template.formatter.DirectivesStack;
import fr.opensagres.xdocreport.template.formatter.IfDirective;
import fr.opensagres.xdocreport.template.formatter.LoopDirective;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Velocity document formatter used to format fields list with Velocity syntax.
 */
public class VelocityDocumentFormatter
    extends AbstractDocumentFormatter
{

    private static final String EQUALS = "=";

    private static final String END_SET_DIRECTIVE = ")";

    private static final String START_SET_DIRECTIVE = "#set(";

    private static final String DOLLAR_START_BRACKET = "${";

    protected static final String ITEM_TOKEN = "item_";

    private static final String START_FOREACH_DIRECTIVE = "#foreach(";

    private static final String IN_DIRECTIVE = " in ";

    private static final String END_FOREACH_DIRECTIVE = "#{end}";

    private static final char DOLLAR_TOKEN = '$';

    private static final String START_IF_DIRECTIVE = "#if(";

    private static final String ELSE_DIRECTIVE = "#{else}";

    private static final String END_IF_DIRECTIVE = "#{end}";

    private static final String VELOCITY_COUNT = "$velocityCount";

    private static final String START_NOPARSE = "#[[";

    private static final String END_NOPARSE = "]]#";

    private static final String START_DEFINE_DIRECTIVE = "#define(";

    private static final Object CLOSE_DEFINE_DIRECTIVE = ")";

    private static final Object END_DEFINE_DIRECTIVE = "#{end}";

    public String formatAsFieldItemList( String content, String fieldName, boolean forceAsField )
    {

        Set<ModelFieldType> types = getModelFieldTypes(content, fieldName);
        if(types.isEmpty())
        {
            if(forceAsField)
            {
                return DOLLAR_TOKEN + getItemToken() + content;
            }
            return content;
        }

        int typesCount = types.size();
        String[] searchList = new String[typesCount];
        String[] replacementList = new String[typesCount];

        int i = 0;
        for(ModelFieldType type : types)
        {
            searchList[i] = type.getPrefix() + fieldName;
            replacementList[i] = type.getPrefix() + ITEM_TOKEN + fieldName;
            i++;
        }

        return StringUtils.replaceEach(content, searchList, replacementList);
    }

    public String formatAsFieldItemList( String content, String fieldName)
    {
        return formatAsFieldItemList(content, fieldName, false);
    }

    public String getStartLoopDirective( String itemNameList, String listName )
    {
        StringBuilder result = new StringBuilder( START_FOREACH_DIRECTIVE );
        if ( itemNameList.charAt(0) != DOLLAR_TOKEN)
        {
            result.append(DOLLAR_TOKEN);
        }
        result.append( itemNameList );
        result.append( IN_DIRECTIVE );
        if ( listName.charAt(0) != DOLLAR_TOKEN)
        {
            result.append(DOLLAR_TOKEN);
        }
        result.append( listName );
        result.append( ')' );
        return result.toString();
    }

    public String getEndLoopDirective( String itemNameList )
    {
        return END_FOREACH_DIRECTIVE;
    }

    public String getElseDirective()
    {
        return ELSE_DIRECTIVE;
    }

    @Override
    protected boolean isModelField( String content, String fieldName )
    {
        return !getModelFieldTypes( content, fieldName ).isEmpty();
    }

    private Set<ModelFieldType> getModelFieldTypes(String content, String fieldName )
    {
        if ( StringUtils.isEmpty( content ) )
        {
            return Collections.emptySet();
        }

        int startIndex = 0;
        EnumSet<ModelFieldType> result = EnumSet.noneOf(ModelFieldType.class);
        int index = -1;
        while((index = content.indexOf(fieldName, startIndex)) != -1)
        {
            final ModelFieldType fieldType = obtainModelFieldType(content, index);
            if(fieldType != null)
            {
                result.add(fieldType);
            }
            startIndex = index + fieldName.length();
        }
        return result;
    }

    private ModelFieldType obtainModelFieldType(String content, int index) {
        for(ModelFieldType type : ModelFieldType.values())
        {
            String prefix = type.getPrefix();
            int prefixLength = prefix.length();
            if(index >= prefixLength)
            {
                String actualPrefix = content.substring(index-prefixLength, index);
                if(actualPrefix.equals(prefix))
                {
                    return type;
                }
            }
        }
        return null;
    }

    @Override
    protected String getItemToken()
    {
        return ITEM_TOKEN;
    }

    public String getFunctionDirective( boolean noescape, boolean encloseInDirective, String key, String methodName,
                                        String... parameters )
    {
        StringBuilder directive = new StringBuilder();
        if ( encloseInDirective )
        {
            directive.append( DOLLAR_START_BRACKET );
        }
        directive.append( key );
        directive.append( '.' );
        directive.append( methodName );
        directive.append( '(' );
        String p = null;
        if ( parameters != null )
        {
            for ( int i = 0; i < parameters.length; i++ )
            {
                p = parameters[i];
                if ( i > 0 )
                {
                    directive.append( ',' );
                }
                p = addDollarIfNeed( p );
                directive.append( p );
            }
        }
        directive.append( ')' );
        if ( encloseInDirective )
        {
            directive.append( '}' );
        }
        return directive.toString();
    }

    private String addDollarIfNeed( String p )
    {
        if ( !p.startsWith( "$" ) && ( p.startsWith( "___" ) || ( !p.startsWith( "'" ) && !p.startsWith( "\"" ) ) ) )
        {
            return "$" + p;
        }
        return p;
    }

    public String formatAsSimpleField( boolean noescape, boolean encloseInDirective, String... fields )
    {
        StringBuilder field = new StringBuilder();
        if ( encloseInDirective )
        {
            field.append(DOLLAR_TOKEN);
        }
        for ( int i = 0; i < fields.length; i++ )
        {
            if ( i == 0 )
            {
                field.append( fields[i] );
            }
            else
            {
                field.append( '.' );
                String f = fields[i];
                field.append( f.substring( 0, 1 ).toUpperCase() );
                field.append( f.substring( 1, f.length() ) );
            }
        }
        return field.toString();
    }

    public String getStartIfDirective( String fieldName, boolean exists )
    {
        StringBuilder directive = new StringBuilder( START_IF_DIRECTIVE );
        if ( fieldName.charAt(0) != DOLLAR_TOKEN)
        {
            directive.append(DOLLAR_TOKEN);
        }
        directive.append( fieldName );
        directive.append( ')' );
        return directive.toString();
    }

    public String getEndIfDirective( String fieldName )
    {
        return END_IF_DIRECTIVE;
    }

    public String getLoopCountDirective( String fieldName )
    {
        return VELOCITY_COUNT;
    }

    public boolean containsInterpolation( String content )
    {
        if ( StringUtils.isEmpty( content ) )
        {
            return false;
        }
        int dollarIndex = content.indexOf(DOLLAR_TOKEN);
        if ( dollarIndex == -1 )
        {
            // Not included to FM directive
            return false;
        }
        return true;
    }

    public int extractListDirectiveInfo( String content, DirectivesStack directives, boolean dontRemoveListDirectiveInfo )
    {
        // content='xxxx#foreach($d in $developers)yyy'
        int startOfEndListDirectiveIndex = content.indexOf( END_FOREACH_DIRECTIVE );
        int startOfStartListDirectiveIndex = content.indexOf( START_FOREACH_DIRECTIVE );
        int startOfStartIfDirectiveIndex = content.indexOf( START_IF_DIRECTIVE );
        DirectiveToParse directiveToParse =
            getDirectiveToParse( startOfStartListDirectiveIndex, startOfEndListDirectiveIndex,
                                 startOfStartIfDirectiveIndex, startOfEndListDirectiveIndex );
        if ( directiveToParse == null )
        {
            return 0;
        }

        switch ( directiveToParse )
        {
            case START_LOOP:
                return parseStartLoop( content, directives, startOfStartListDirectiveIndex );
            case START_IF:
                return parseStartIf( content, directives, startOfStartIfDirectiveIndex );
            case END_IF:
            case END_LOOP:
                return parseEndDirective( content, directives, dontRemoveListDirectiveInfo,
                                          startOfEndListDirectiveIndex );
        }
        return 0;
    }

    public int parseEndDirective( String content, DirectivesStack directives, boolean dontRemoveListDirectiveInfo,
                                  int startOfEndListDirectiveIndex )
    {
        // content contains (at first #end)
        if ( !dontRemoveListDirectiveInfo && !directives.isEmpty() )
        {
            // remove the LoopDirective from the stack
            directives.pop();
        }
        // get content after the #end
        String afterEndList =
            content.substring( END_FOREACH_DIRECTIVE.length() + startOfEndListDirectiveIndex, content.length() );
        int nbLoop = -1;
        // parse the content after the #end
        nbLoop += extractListDirectiveInfo( afterEndList, directives );
        return nbLoop;
    }

    public int parseStartIf( String content, DirectivesStack directives, int startOfStartIfDirectiveIndex )
    {
        String contentWhichStartsWithIf = content.substring( startOfStartIfDirectiveIndex, content.length() );
        int endOfStartIfDirectiveIndex = contentWhichStartsWithIf.indexOf( ')' );
        if ( endOfStartIfDirectiveIndex == -1 )
        {
            // #if( not closed with ')'
            return 0;
        }
        // startIfDirective='#if($d)'
        String startIfDirective = contentWhichStartsWithIf.substring( 0, endOfStartIfDirectiveIndex + 1 );
        // // contentWichStartsWithList='xxx#if($d)yyy'
        int nbIf = 1;
        directives.push( new IfDirective( directives.peekOrNull(), startIfDirective, getEndIfDirective( null ) ) );
        // afterIf = 'yyy'
        String afterIf = content.substring( startOfStartIfDirectiveIndex + startIfDirective.length(), content.length() );
        nbIf += extractListDirectiveInfo( afterIf, directives );
        return nbIf;
    }

    public int parseStartLoop( String content, DirectivesStack directives, int startOfStartListDirectiveIndex )
    {
        // contentWichStartsWithList='#foreach($d in $developers)yyy'
        String contentWhichStartsWithList = content.substring( startOfStartListDirectiveIndex, content.length() );
        int endOfStartListDirectiveIndex = contentWhichStartsWithList.indexOf( ')' );
        if ( endOfStartListDirectiveIndex == -1 )
        {
            // [#list not closed with ')'
            return 0;
        }
        // startLoopDirective='#foreach($d in $developers)'
        String startLoopDirective = contentWhichStartsWithList.substring( 0, endOfStartListDirectiveIndex + 1 );
        // insideLoop='developers as d]'
        String insideLoop =
            startLoopDirective.substring( START_FOREACH_DIRECTIVE.length(), startLoopDirective.length() );
        int indexBeforeIn = insideLoop.indexOf( " " );
        if ( indexBeforeIn == -1 )
        {
            return 0;
        }

        // afterItem=' in $developers]'
        String afterItem = insideLoop.substring( indexBeforeIn, insideLoop.length() );
        int indexAfterIn = afterItem.indexOf( IN_DIRECTIVE );
        if ( indexAfterIn == -1 )
        {
            return 0;
        }

        // item='$d'
        String item = insideLoop.substring( 0, indexBeforeIn ).trim();
        // remove $
        // item='d'
        if ( item.charAt(0) == DOLLAR_TOKEN)
        {
            item = item.substring( 1, item.length() );
        }
        if ( StringUtils.isEmpty( item ) )
        {
            return 0;
        }
        // afterIn='$developers)'
        String afterIn = afterItem.substring( IN_DIRECTIVE.length(), afterItem.length() );
        int endListIndex = afterIn.indexOf( ')' );
        if ( endListIndex == -1 )
        {
            return 0;
        }
        // sequence='$developers'
        String sequence = afterIn.substring( 0, endListIndex ).trim();
        // remove $
        // item='d'
        if ( sequence.charAt(0) == DOLLAR_TOKEN)
        {
            sequence = sequence.substring( 1, sequence.length() );
        }
        if ( StringUtils.isEmpty( sequence ) )
        {
            return 0;
        }

        int nbLoop = 1;
        directives.push( new LoopDirective( directives.peekOrNull(), startLoopDirective, getEndLoopDirective( null ),
                                            sequence, item ) );

        // afterList = 'yyy'
        String afterList =
            content.substring( startOfStartListDirectiveIndex + startLoopDirective.length(), content.length() );
        nbLoop += extractListDirectiveInfo( afterList, directives );
        return nbLoop;
    }

    public String extractModelTokenPrefix( String fieldName )
    {
        // fieldName = '$developers.Name'
        if ( fieldName == null )
        {
            return null;
        }
        int dollarIndex = fieldName.indexOf(DOLLAR_TOKEN);
        if ( dollarIndex == -1 )
        {
            return null;
        }
        int endIndex = fieldName.indexOf( ' ' );
        if ( endIndex != -1 )
        {
            fieldName = fieldName.substring( 0, fieldName.length() );
        }
        // fieldNameWithoutDollar='developers.Name'
        String fieldNameWithoutDollar = fieldName.substring( dollarIndex + 1, fieldName.length() );
        int lastDotIndex = fieldNameWithoutDollar.lastIndexOf( '.' );
        if ( lastDotIndex == -1 )
        {
            return fieldNameWithoutDollar;
        }
        // fieldNameWithoutDollar='developers'
        return fieldNameWithoutDollar.substring( 0, lastDotIndex );
    }

    public int getIndexOfScript( String fieldName )
    {
        if ( fieldName == null )
        {
            return -1;
        }
        return fieldName.indexOf( "#" );
    }

    public String formatAsCallTextStyling( long variableIndex, String fieldName, String documentKind,
                                           String syntaxKind, boolean syntaxWithDirective, String elementId,
                                           String entryName )
    {
        StringBuilder newContent = new StringBuilder( START_SET_DIRECTIVE );
        newContent.append( formatAsSimpleField( true, getVariableName( variableIndex ) ) );
        newContent.append( EQUALS );
        newContent.append( getFunctionDirective( TemplateContextHelper.TEXT_STYLING_REGISTRY_KEY,
                                                 TemplateContextHelper.TRANSFORM_METHOD, fieldName, "\"" + syntaxKind
                                                     + "\"",
                                                 syntaxWithDirective ? StringUtils.TRUE : StringUtils.FALSE, "\""
                                                     + documentKind + "\"", "\"" + elementId + "\"", "$"
                                                     + TemplateContextHelper.CONTEXT_KEY, "\"" + entryName + "\"" ) );
        newContent.append( END_SET_DIRECTIVE );
        return newContent.toString();
    }

    public String formatAsTextStylingField( long variableIndex, String property )
    {
        return formatAsSimpleField( true, getVariableName( variableIndex ), property );
    }

    public boolean hasDirective( String characters )
    {
        return characters.indexOf( "#" ) != -1;
    }

    public String getSetDirective( String name, String value, boolean valueIsField )
    {
        StringBuilder newContent = new StringBuilder( START_SET_DIRECTIVE );
        newContent.append( formatAsSimpleField( true, name ) );
        newContent.append( EQUALS );
        if ( valueIsField )
        {
            newContent.append( formatAsSimpleField( true, value ) );
        }
        else
        {
            value = addDollarIfNeed( value );
            newContent.append( value );
        }
        newContent.append( END_SET_DIRECTIVE );
        return newContent.toString();
    }

    public String getStartNoParse()
    {
        return START_NOPARSE;
    }

    public String getEndNoParse()
    {
        return END_NOPARSE;
    }

    public String getDefineDirective( String name, String value )
    {
        StringBuilder newContent = new StringBuilder( START_DEFINE_DIRECTIVE );
        newContent.append( formatAsSimpleField( true, name ) );
        newContent.append( CLOSE_DEFINE_DIRECTIVE );
        newContent.append( value );
        newContent.append( END_DEFINE_DIRECTIVE );
        return newContent.toString();
    }
}
