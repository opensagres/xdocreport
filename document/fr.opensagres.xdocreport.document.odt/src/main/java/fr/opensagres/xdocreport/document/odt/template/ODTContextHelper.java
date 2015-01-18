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
package fr.opensagres.xdocreport.document.odt.template;

import java.util.Map;

import fr.opensagres.xdocreport.document.odt.textstyling.IODTStylesGenerator;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTDefaultStyle;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTDefaultStylesGenerator;
import fr.opensagres.xdocreport.template.IContext;

public class ODTContextHelper
{

    public static final String DEFAULT_STYLE_KEY = "___DefaultStyle";

    public static final String STYLES_GENERATOR_KEY = "___NoEscapeStylesGenerator";

    public static void putStylesGenerator( IContext context, IODTStylesGenerator stylesGenerator )
    {
        context.put( STYLES_GENERATOR_KEY, stylesGenerator );
    }

    public static IODTStylesGenerator getStylesGenerator( IContext context )
    {
        IODTStylesGenerator stylesGenerator = (IODTStylesGenerator) context.get( STYLES_GENERATOR_KEY );
        if ( stylesGenerator == null )
        {
            stylesGenerator = new ODTDefaultStylesGenerator();
            putStylesGenerator( context, stylesGenerator );
        }
        return stylesGenerator;
    }

    public static void putDefaultStyle( IContext context, ODTDefaultStyle defaultStyle )
    {
        context.put( DEFAULT_STYLE_KEY, defaultStyle );
    }

    public static ODTDefaultStyle getDefaultStyle( IContext context )
    {
        return (ODTDefaultStyle) context.get( DEFAULT_STYLE_KEY );
    }

    public static ODTDefaultStyle getDefaultStyle( Map<String, Object> sharedContext )
    {
        ODTDefaultStyle defaultStyle = (ODTDefaultStyle) sharedContext.get( ODTContextHelper.DEFAULT_STYLE_KEY );
        if ( defaultStyle == null )
        {
            defaultStyle = new ODTDefaultStyle();
            sharedContext.put( ODTContextHelper.DEFAULT_STYLE_KEY, defaultStyle );
        }
        return defaultStyle;
    }

}
