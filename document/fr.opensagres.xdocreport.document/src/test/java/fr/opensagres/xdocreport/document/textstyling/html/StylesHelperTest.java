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
package fr.opensagres.xdocreport.document.textstyling.html;

import fr.opensagres.xdocreport.document.textstyling.properties.Color;
import fr.opensagres.xdocreport.document.textstyling.properties.ContainerProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ContainerType;
import junit.framework.Assert;
import org.junit.Test;

public class StylesHelperTest
{

    @Test
    public void createSpanPropertiesColor()
    {
        ContainerProperties expectedSpanProperties = new ContainerProperties( ContainerType.SPAN );
        expectedSpanProperties.setColor( new Color( 230, 0, 0 ) );

        Assert.assertEquals( expectedSpanProperties,
                StylesHelper.createProperties( "color: rgb(230, 0, 0);", ContainerType.SPAN ) );
        Assert.assertEquals( expectedSpanProperties,
                StylesHelper.createProperties( "color: #E60000;", ContainerType.SPAN ) );
    }

    @Test
    public void createSpanPropertiesColorInvalid()
    {
        ContainerProperties expectedSpanProperties = new ContainerProperties( ContainerType.SPAN );

        Assert.assertEquals( expectedSpanProperties,
                StylesHelper.createProperties( "color: rgb(2303, 0, 0);", ContainerType.SPAN ) );
        Assert.assertEquals( expectedSpanProperties,
                StylesHelper.createProperties( "color: #1234567;", ContainerType.SPAN ) );
        Assert.assertEquals( expectedSpanProperties,
                StylesHelper.createProperties( "color: invalid;", ContainerType.SPAN ) );
    }
}
