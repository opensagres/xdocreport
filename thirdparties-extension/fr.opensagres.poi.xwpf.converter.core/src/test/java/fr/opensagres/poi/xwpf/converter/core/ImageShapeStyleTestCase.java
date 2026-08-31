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
package fr.opensagres.poi.xwpf.converter.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ImageShapeStyleTestCase
{
    private static final float DELTA = 0.001f;

    @Test
    public void inlineShape()
    {
        ImageShapeStyle style = ImageShapeStyle.parse( "width:130.5pt;height:36pt" );
        assertEquals( 130.5f, style.getWidth(), DELTA );
        assertEquals( 36f, style.getHeight(), DELTA );
        assertTrue( style.isCanUse() );
        assertFalse( style.isAbsolutePosition() );
        assertEquals( 0f, style.getMarginLeft(), DELTA );
        assertEquals( 0f, style.getMarginTop(), DELTA );
    }

    @Test
    public void floatingShape()
    {
        ImageShapeStyle style =
            ImageShapeStyle.parse( "position:absolute;margin-left:-71.65pt;margin-top:-69.65pt;width:595.5pt;height:403.5pt;z-index:-1;visibility:visible" );
        assertEquals( 595.5f, style.getWidth(), DELTA );
        assertEquals( 403.5f, style.getHeight(), DELTA );
        assertTrue( style.isCanUse() );
        assertTrue( style.isAbsolutePosition() );
        assertEquals( -71.65f, style.getMarginLeft(), DELTA );
        assertEquals( -69.65f, style.getMarginTop(), DELTA );
    }

    @Test
    public void positionRelative()
    {
        ImageShapeStyle style =
            ImageShapeStyle.parse( "position:absolute;margin-left:0;margin-top:0;width:100pt;height:50pt;"
                + "mso-position-horizontal:center;mso-position-horizontal-relative:page;"
                + "mso-position-vertical:top;mso-position-vertical-relative:margin" );
        assertEquals( "center", style.getPositionHorizontal() );
        assertEquals( "page", style.getPositionHorizontalRelative() );
        assertEquals( "top", style.getPositionVertical() );
        assertEquals( "margin", style.getPositionVerticalRelative() );
    }

    @Test
    public void missingPositionRelativeIsNull()
    {
        ImageShapeStyle style = ImageShapeStyle.parse( "position:absolute;width:100pt;height:50pt" );
        assertNull( style.getPositionHorizontal() );
        assertNull( style.getPositionHorizontalRelative() );
        assertNull( style.getPositionVertical() );
        assertNull( style.getPositionVerticalRelative() );
    }

    @Test
    public void unknownSizeFallsBackToDefault()
    {
        ImageShapeStyle style = ImageShapeStyle.parse( "position:absolute" );
        assertEquals( 10f, style.getWidth(), DELTA );
        assertEquals( 10f, style.getHeight(), DELTA );
        assertFalse( style.isCanUse() );
    }

    @Test
    public void lengthUnits()
    {
        assertEquals( 12f, ImageShapeStyle.parseLength( "12pt" ).floatValue(), DELTA );
        assertEquals( 72f, ImageShapeStyle.parseLength( "1in" ).floatValue(), DELTA );
        assertEquals( 28.3464f, ImageShapeStyle.parseLength( "1cm" ).floatValue(), DELTA );
        assertEquals( 2.83464f, ImageShapeStyle.parseLength( "1mm" ).floatValue(), DELTA );
        assertEquals( 12f, ImageShapeStyle.parseLength( "1pc" ).floatValue(), DELTA );
        assertEquals( 0.75f, ImageShapeStyle.parseLength( "1px" ).floatValue(), DELTA );
        // no unit means pixel
        assertEquals( 0.75f, ImageShapeStyle.parseLength( "1" ).floatValue(), DELTA );
        assertEquals( -0.75f, ImageShapeStyle.parseLength( "-1" ).floatValue(), DELTA );
        assertNull( ImageShapeStyle.parseLength( "1em" ) );
        assertNull( ImageShapeStyle.parseLength( "auto" ) );
        assertNull( ImageShapeStyle.parseLength( "" ) );
        assertNull( ImageShapeStyle.parseLength( null ) );
    }

    @Test
    public void malformedStyleIsIgnored()
    {
        ImageShapeStyle style = ImageShapeStyle.parse( ";;position:absolute;;flip;width:10pt;height:20pt;" );
        assertTrue( style.isAbsolutePosition() );
        assertEquals( 10f, style.getWidth(), DELTA );
        assertEquals( 20f, style.getHeight(), DELTA );
    }
}
