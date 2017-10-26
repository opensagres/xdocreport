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

import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.Enum;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;

public abstract class AbstractIndentationParagraphValueProvider<Value>
    extends AbstractParagraphValueProvider<Value>
{
    @Override
    public Value getValue( CTPPr ppr )
    {
        CTInd ind = getInd( ppr );
        if ( ind == null )
        {
            return null;
        }
        return getValue( ind );
    }

    public CTInd getInd( CTPPr pr )
    {
        if ( pr == null )
        {
            return null;
        }
        return pr.getInd() == null ? null : pr.getInd();
    }

    @Override
    protected StringBuilder getKeyBuffer( XWPFParagraph element, XWPFStylesDocument stylesDocument, String styleId,
                                          Enum type )
    {
        if ( element.getPartType() == BodyType.TABLECELL )
        {
            return super.getKeyBuffer( element, stylesDocument, styleId, type ).append( "_cell" );
        }
        return super.getKeyBuffer( element, stylesDocument, styleId, type );
    }

    public abstract Value getValue( CTInd ind );

}
