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
package fr.opensagres.xdocreport.document.docx.textstyling;

import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.textstyling.IStylesGenerator;

public interface IDocxStylesGenerator
    extends IStylesGenerator<DefaultStyle>
{

    public static final String generateAbstractNumBullet = "generateAbstractNumBullet";
    
    public static final String generateAbstractNumDecimal = "generateAbstractNumDecimal";
    
    String generateAllStyles( DefaultStyle defaultStyle );

    String getHyperLinkStyleId( DefaultStyle defaultStyle );

    String getHeaderStyleId( int level, DefaultStyle defaultStyle );

    Integer getAbstractNumIdForList( boolean ordered, DefaultStyle defaultStyle );

    String generateAbstractNumBullet( DefaultStyle defaultStyle );

    String generateAbstractNumDecimal( DefaultStyle defaultStyle, int abstractNumId );

}
