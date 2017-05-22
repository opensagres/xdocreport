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

import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline;

public class RunUnderlineValueProvider
    extends AbstractRunValueProvider<UnderlinePatterns>
{

    public static final RunUnderlineValueProvider INSTANCE = new RunUnderlineValueProvider();

    @Override
    public UnderlinePatterns getValue( CTRPr rPr, XWPFStylesDocument stylesDocument)
    {
        if (rPr != null && rPr.isSetU()) {
            CTUnderline u = rPr.getU();
            return u.isSetVal() ? UnderlinePatterns.valueOf(u.getVal().intValue()) : null;
        } else {
            return null;
        }
    }
}
