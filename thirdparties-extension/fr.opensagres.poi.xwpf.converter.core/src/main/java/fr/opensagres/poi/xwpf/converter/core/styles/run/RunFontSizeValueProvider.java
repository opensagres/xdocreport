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
package fr.opensagres.poi.xwpf.converter.core.styles.run;

import java.math.BigInteger;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.utils.XWPFUtils;

public class RunFontSizeValueProvider
    extends AbstractRunValueProvider<Float>
{

    public static RunFontSizeValueProvider INSTANCE = new RunFontSizeValueProvider();

    @Override
    public Float getValue( CTRPr pr, XWPFStylesDocument stylesDocument )
    {
        return ( pr != null && pr.sizeOfSzArray() > 0 ) ? XWPFUtils.bigIntegerValue(pr.getSzArray(0).xgetVal()).divide( new BigInteger( "2" ) ).floatValue() : null;
    }
    
    @Override
    protected Float getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument stylesDocument )
    {
        Float result = getValue( getRPr( docDefaults ), stylesDocument );
        if (result == null && docDefaults != null)
        {
            if (docDefaults.isSetRPrDefault())
            {
                CTRPr rpr = docDefaults.getRPrDefault().getRPr();
                if (rpr == null || rpr.sizeOfSzArray() == 0)
                {
                    result = 10f;
                }
            }
            else
            {
                result = 11f;
            }
        }
        return result;
    }
}
