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
package fr.opensagres.poi.xwpf.converter.core.openxmlformats.styles.run;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.utils.XWPFUtils;

public class RunFontStyleBoldValueProvider
    extends AbstractRunValueProvider<Boolean>
{

    public static RunFontStyleBoldValueProvider INSTANCE = new RunFontStyleBoldValueProvider();

    @Override
    public Boolean getValue( CTRPr rpr, XWPFStylesDocument stylesDocument )
    {
        return isBold( rpr );
    }

    @Override
    public Boolean getValue( CTParaRPr rpr, XWPFStylesDocument stylesDocument )
    {
        return isBold( rpr );
    }

    private static Boolean isBold( CTRPr pr )
    {
        if ( pr == null || pr.sizeOfBArray() == 0 )
        {
            return null;
        }
        return XWPFUtils.isCTOnOff( pr.getBArray(0) );
    }

    private static Boolean isBold( CTParaRPr pr )
    {
        if ( pr == null || pr.sizeOfBArray() == 0 )
        {
            return null;
        }
        return XWPFUtils.isCTOnOff( pr.getBArray(0) );
    }
}
