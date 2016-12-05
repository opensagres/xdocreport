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
package fr.opensagres.poi.xwpf.converter.core.styles.paragraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil;

public class ParagraphSpacingAfterValueProvider
    extends AbstractSpacingParagraphValueProvider<Float>
{

    public static ParagraphSpacingAfterValueProvider INSTANCE = new ParagraphSpacingAfterValueProvider();

    @Override
    protected Float getDefaultValue( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        if ( paragraph.getPartType() == BodyType.TABLECELL )
        {
            return null;
        }
        return super.getDefaultValue( paragraph, stylesDocument );
    }

    @Override
    protected Float getValue( CTSpacing spacing )
    {
        BigInteger after = spacing.getAfter();
        if ( after != null )
        {

            return DxaUtil.dxa2points( after );
        }
        return null;
    }

    @Override
    protected Float getStaticValue( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        // Not sure with this rule. But it seems that when styles.xml doesn't declare "w:rPrDefault"
        // default value is 10pt.
        if ( stylesDocument.getDocDefaults() == null )
        {
            // see Issue52 JUnit.
            return 10f;
        }
        return null;
    }

}
