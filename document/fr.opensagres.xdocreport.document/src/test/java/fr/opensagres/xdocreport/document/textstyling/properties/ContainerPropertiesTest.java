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
package fr.opensagres.xdocreport.document.textstyling.properties;

import org.junit.Assert;
import org.junit.Test;

public class ContainerPropertiesTest
{
    @Test
    public void combineKeepTypeIfSame()
    {
        ContainerProperties a = new ContainerProperties( ContainerType.SPAN );
        ContainerProperties b = new ContainerProperties( ContainerType.SPAN );

        ContainerProperties result = ContainerProperties.combine( a, b );
        Assert.assertEquals( result.getType(), ContainerType.SPAN );
    }

    @Test
    public void combineCombinedTypeIfDifferent()
    {
        ContainerProperties a = new ContainerProperties( ContainerType.SPAN );
        ContainerProperties b = new ContainerProperties( ContainerType.HEADER );

        ContainerProperties result = ContainerProperties.combine( a, b );
        Assert.assertEquals( result.getType(), ContainerType.COMBINED );
    }

    @Test
    public void combineCombined()
    {
        ContainerProperties a = new ContainerProperties( ContainerType.SPAN );
        a.setPageBreakBefore( true );
        a.setPageBreakAfter( true );
        a.setBold( true );
        a.setItalic( false );
        a.setUnderline( false );
        a.setStrike( true );
        a.setSubscript( true );
        a.setSuperscript( true );
        a.setTextAlignment( TextAlignment.Center );
        final Color color = new Color( 120, 130, 140 );
        a.setColor( color );
        a.setStyleName( "a" );

        ContainerProperties b = new ContainerProperties( ContainerType.HEADER );
        b.setPageBreakBefore( false );
        b.setPageBreakAfter( true );
        b.setBold( false );
        b.setItalic( false );
        b.setUnderline( true );
        b.setStrike( false );
        b.setSubscript( false );
        b.setSuperscript( false );
        b.setTextAlignment( TextAlignment.Inherit );
        b.setColor( null );
        b.setStyleName( "b" );

        ContainerProperties result = ContainerProperties.combine( a, b );
        Assert.assertEquals( result.getType(), ContainerType.COMBINED );
        Assert.assertTrue( result.isPageBreakBefore() );
        Assert.assertTrue( result.isPageBreakAfter() );
        Assert.assertTrue( result.isBold() );
        Assert.assertFalse( result.isItalic() );
        Assert.assertTrue( result.isUnderline() );
        Assert.assertTrue( result.isStrike() );
        Assert.assertTrue( result.isSubscript() );
        Assert.assertTrue( result.isSuperscript() );
        Assert.assertEquals( TextAlignment.Inherit, result.getTextAlignment() );
        Assert.assertEquals( color, result.getColor() );
        Assert.assertEquals( "b", result.getStyleName() );
    }
}