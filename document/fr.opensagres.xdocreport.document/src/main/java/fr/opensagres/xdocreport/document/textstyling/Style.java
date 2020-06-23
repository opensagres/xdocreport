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
package fr.opensagres.xdocreport.document.textstyling;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;

public class Style
{
    private static String[] searchList = { "\r", "\t", "\n" };

    private static String[] replacementList = { "", " ", "" };

    private final String id;

    private final String content;

    public Style( String id, String content )
    {
        this.id = id;
        this.content = content;
    }

    public String getId()
    {
        return id;
    }

    public String getContent()
    {
        return content;
    }

    public static Style load( String id, Class clazz )
    {
        String content = "";
        try
        {
            content = IOUtils.toString( clazz.getResourceAsStream( id + ".xml" ), StandardCharsets.UTF_8.name() );
            content = StringUtils.replaceEach( content, searchList, replacementList );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        Style style = new Style( id, content );
        return style;
    }
}
