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
package fr.opensagres.xdocreport.xhtml.extension;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class CSSStyleSheet
    extends AbstractContentBuffer
    implements XHTMLConstants, CSSStylePropertyConstants
{

    private StringBuilder cssStyles = new StringBuilder();

    public CSSStyleSheet( int indent )
    {
        super( indent );
        // By default p element are no margin top/bottom
        startCSSStyleDeclaration( P_ELEMENT );
        setCSSProperty( MARGIN_TOP, "0" );
        setCSSProperty( MARGIN_BOTTOM, "0" );
        endCSSStyleDeclaration();

        // By default ol element are no margin top/bottom
        startCSSStyleDeclaration( OL_ELEMENT );
        setCSSProperty( MARGIN_TOP, "0" );
        setCSSProperty( MARGIN_BOTTOM, "0" );
        endCSSStyleDeclaration();

        // By default ul element are no margin top/bottom
        startCSSStyleDeclaration( UL_ELEMENT );
        setCSSProperty( MARGIN_TOP, "0" );
        setCSSProperty( MARGIN_BOTTOM, "0" );
        endCSSStyleDeclaration();

    }

    public void save( Writer writer )
        throws IOException
    {
        writer.write( cssStyles.toString() );
    }

    public void save( OutputStream out )
        throws IOException
    {
        out.write( cssStyles.toString().getBytes() );
    }

    public boolean isEmpty()
    {
        return cssStyles.length() == 0;
    }

    public void setComment( String comment )
    {
        doIndentIfNeeded( 1 );
        cssStyles.append( '/' );
        cssStyles.append( '*' );
        cssStyles.append( comment );
        cssStyles.append( '*' );
        cssStyles.append( '/' );

    }

    @Override
    protected StringBuilder getCurrentBuffer()
    {
        return cssStyles;
    }

    public void startCSSStyleDeclaration( String selector )
    {
        doIndentIfNeeded( 1 );
        cssStyles.append( selector );
        cssStyles.append( ' ' );
        cssStyles.append( '{' );
    }

    public void endCSSStyleDeclaration()
    {
        doIndentIfNeeded( 1 );
        cssStyles.append( '}' );
    }

    public void setCSSProperty( String name, String value )
    {
        doIndentIfNeeded( 2 );
        cssStyles.append( name );
        cssStyles.append( ':' );
        cssStyles.append( value );
        cssStyles.append( ';' );
    }

    protected void doIndentIfNeeded( int subIndent )
    {
        doIndentIfNeeded( getCurrentBuffer(), indent > 0 ? indent + subIndent : 0 );
    }

}
