/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
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
package org.apache.poi.xwpf.converter.internal.itext;

import java.awt.Color;

import org.apache.poi.xwpf.converter.internal.XWPFUtils;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public class XWPFParagraphUtils
{

    public static CTPPr getPPr( XWPFStyle style )
    {
        if ( style == null )
        {
            return null;
        }

        CTStyle ctStyle = style.getCTStyle();
        if ( ctStyle == null )
        {
            return null;
        }
        return ctStyle.getPPr();
    }

    public static CTPPr getPPr( XWPFParagraph paragraph )
    {
        return paragraph.getCTP().getPPr();
    }

    public static Color getBackgroundColor( XWPFParagraph paragraph )
    {
        CTPPr pPr = getPPr( paragraph );
        if ( pPr == null )
        {
            return null;
        }
        return XWPFUtils.getFillColor( pPr.getShd() );
    }

    public static Color getBackgroundColor( XWPFRun run )
    {
        CTR ctr = run.getCTR();
        if ( ctr == null )
        {
            return null;
        }
        CTRPr ctrPr = ctr.getRPr();
        if ( ctrPr == null )
        {
            return null;
        }
        return XWPFUtils.getFillColor( ctrPr.getShd() );
    }

    public static Color getForegroundColor( XWPFParagraph paragraph )
    {
        CTPPr pPr = getPPr( paragraph );
        if ( pPr == null )
        {
            return null;
        }
        return XWPFUtils.getColor( pPr.getShd() );
    }

}
