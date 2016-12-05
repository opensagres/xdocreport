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

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr;

import fr.opensagres.poi.xwpf.converter.core.styles.AbstractValueProvider;
import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.utils.StringUtils;
import fr.opensagres.poi.xwpf.converter.core.utils.StylesHelper;

public abstract class AbstractParagraphValueProvider<Value>
    extends AbstractValueProvider<Value, XWPFParagraph>
{

    public CTPPr getCTPPr( XWPFParagraph paragraph )
    {
        return paragraph.getCTP().getPPr();
    }

    public CTPPr getCTPPr( CTStyle style )
    {
        return style.getPPr();
    }

    public CTPPr getCTPPr( CTTblStylePr tblStylePr )
    {
        return tblStylePr.getPPr();
    }

    public CTPPr getCTPPr( CTDocDefaults docDefaults )
    {
        CTPPrDefault prDefault = docDefaults.getPPrDefault();
        if ( prDefault == null )
        {
            return null;
        }
        return prDefault.getPPr();
    }

    @Override
    public Value getValueFromElement( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        return getValue( getCTPPr( paragraph ) );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style, XWPFStylesDocument stylesDocument )
    {
        return getValue( getCTPPr( style ) );
    }

    @Override
    protected Value getValueFromTableStyle( CTTblStylePr tblStylePr, XWPFStylesDocument stylesDocument )
    {
        return getValue( getCTPPr( tblStylePr ) );
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument stylesDocument )
    {
        return getValue( getCTPPr( docDefaults ) );
    }

    public abstract Value getValue( CTPPr ppr );

    @Override
    protected String[] getStyleID( XWPFParagraph paragraph )
    {
        List<String> styleIDs = StylesHelper.getStyleIDs( paragraph );
        if ( styleIDs != null )
        {
            return styleIDs.toArray( StringUtils.EMPTY_STRING_ARRAY );
        }
        return null;
    }

    @Override
    protected CTStyle getDefaultStyle( XWPFParagraph element, XWPFStylesDocument styleManager )
    {
        return styleManager.getDefaultParagraphStyle();
    }

    @Override
    protected XWPFTableCell getParentTableCell( XWPFParagraph paragraph )
    {
        return StylesHelper.getEmbeddedTableCell( paragraph );
    }
}
