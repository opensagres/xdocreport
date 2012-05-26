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

public interface IDocumentFormatter
{

    String getStartDocumentDirective();

    String getEndDocumentDirective();

    String formatAsFieldItemList( String content, String fieldName, boolean forceAsField );

    String extractItemNameList( String content, String fieldName, boolean forceAsField );

    String getStartLoopDirective( String itemNameList );

    String getStartLoopDirective( String itemNameList, String listName );

    String getEndLoopDirective( String itemNameList );

    String getLoopCountDirective( String fieldName );

    String getStartIfDirective( String fieldName );

    String getStartIfDirective( String fieldName, boolean exists );
    
    String getEndIfDirective( String fieldName );

    String formatAsSimpleField( boolean noescape, boolean encloseInDirective, String... fields );
    
    String formatAsSimpleField( boolean encloseInDirective, String... fields );

    boolean containsInterpolation( String content );

    int extractListDirectiveInfo( String content, DirectivesStack directives );

    int extractListDirectiveInfo( String content, DirectivesStack directives, boolean dontRemoveListDirectiveInfo );

    String extractModelTokenPrefix( String newContent );

    int getIndexOfScript( String fieldName );

    String getFunctionDirective( boolean encloseInDirective, String key, String methodName, String... parameters );

    String getFunctionDirective( String key, String methodName, String... parameters );

    boolean hasDirective( String characters );

    String formatAsCallTextStyling( long variableIndex, String fieldName, String documentKind, String syntaxKind,
                                    boolean syntaxWithDirective, String elementId, String entryName );

    String formatAsTextStylingField( long variableIndex, String textBeforeProperty );

    String getElseDirective();

    String getSetDirective( String name, String value, boolean valueIsField );
    
    String getSetDirective( String name, String value );

    String getStartNoParse();

    String getEndNoParse();

    String getDefineDirective( String name, String value );
}
