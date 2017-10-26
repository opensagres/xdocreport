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

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule.Enum;

import fr.opensagres.poi.xwpf.converter.core.ParagraphLineSpacing;
import fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil;

/**
 * <p>
 * 17.3.1.33 spacing (Spacing Between Lines and Above/Below Paragraph) This element specifies the inter-line and
 * inter-paragraph spacing which shall be applied to the contents of this paragraph when it is displayed by a consumer.
 * If this element is omitted on a given paragraph, each of its values is determined by the setting previously set at
 * any level of the style hierarchy (i.e. that previous setting remains unchanged). If this setting is never specified
 * in the style hierarchy, then the paragraph shall have no spacing applied to its lines, or above and below its
 * contents. [Example: Consider the following WordprocessingML paragraph: <w:pPr> <w:spacing w:after="200" w:line="276"
 * w:lineRule="auto" /> </w:pPr> This paragraph specifies that it must have at least 200 twentieths of a point after the
 * last line in each paragraph, and that the spacing in each line should be automatically calculated based on a 1.15
 * times (276 divided by 240) the normal single spacing calculation. end example]
 * </p>
 */
public class ParagraphLineSpacingValueProvider
    extends AbstractSpacingParagraphValueProvider<ParagraphLineSpacing>
{

    protected static final float LINE_SPACING_FACTOR = 240f;
                    
    public static final ParagraphLineSpacingValueProvider INSTANCE = new ParagraphLineSpacingValueProvider();

    @Override
    protected ParagraphLineSpacing getValue( CTSpacing spacing )
    {
        // // spacing before
        // BigInteger before = spacing.getBefore();
        // Float spaceBefore = null;
        // if ( before != null )
        // {
        // spaceBefore = DxaUtil.dxa2points( before );
        // }
        //
        // // spacing after
        // BigInteger after = spacing.getAfter();
        // Float spaceAfter = null;
        // if ( after != null )
        // {
        // spaceAfter = DxaUtil.dxa2points( after );
        // }

        // line
        Float lineHeight = null;
        Float lineMultiple = null;

        BigInteger line = spacing.getLine();
        if ( line == null )
        {
            return null;
        }

        // Specifies how the spacing between lines is calculated as stored in the line attribute.
        Enum lineRule = spacing.getLineRule();
        if ( lineRule == null )
        {
            // If this attribute is omitted, then it shall be assumed to be of a value auto if a line attribute
            // value is present.
            lineRule = STLineSpacingRule.AUTO;
        }
        if ( lineRule == STLineSpacingRule.AUTO )
        {
            // The lineRule attribute value of auto specifies that the value of the line attribute is to be
            // interpreted in 240ths of a single line height
            lineMultiple = line.floatValue() / LINE_SPACING_FACTOR;
        }
        else
        {
            lineHeight = DxaUtil.dxa2points( line );
        }

        return new ParagraphLineSpacing( null, null, lineHeight, lineMultiple );
    }

}
