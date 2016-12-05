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

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr;

import fr.opensagres.poi.xwpf.converter.core.styles.AbstractValueProvider;
import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.utils.StringUtils;
import fr.opensagres.poi.xwpf.converter.core.utils.StylesHelper;

public abstract class AbstractRunValueProvider<Value>
    extends AbstractValueProvider<Value, XWPFRun>
{

    public CTRPr getRPr( XWPFRun run )
    {
        return run.getCTR().getRPr();
    }

    public CTRPr getRPr( CTStyle style )
    {
        return style.getRPr();
    }

    public CTRPr getRPr( CTDocDefaults docDefaults )
    {
        CTRPrDefault prDefault = docDefaults.getRPrDefault();
        if ( prDefault == null )
        {
            return null;
        }
        return prDefault.getRPr();
    }

    public CTRPr getRPr( CTTblStylePr tblStylePr )
    {
        return tblStylePr.getRPr();
    }

    @Override
    public Value getValueFromElement( XWPFRun run, XWPFStylesDocument stylesDocument )
    {
        return getValue( getRPr( run ), stylesDocument );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style, XWPFStylesDocument stylesDocument )
    {
        return getValue( getRPr( style ), stylesDocument );
    }

    @Override
    protected Value getValueFromTableStyle( CTTblStylePr tblStylePr, XWPFStylesDocument stylesDocument )
    {
        return getValue( getRPr( tblStylePr ), stylesDocument );
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument stylesDocument )
    {
        return getValue( getRPr( docDefaults ), stylesDocument );
    }

    public abstract Value getValue( CTRPr ppr, XWPFStylesDocument stylesDocument );

    @Override
    protected String[] getStyleID( XWPFRun run )
    {
        XWPFParagraph paragraph = run.getParagraph();
        List<String> styleIDs = StylesHelper.getStyleIDs( paragraph );
        CTRPr rPr = getRPr( run );
        if ( rPr != null )
        {
            CTString style = rPr.getRStyle();
            if ( style != null )
            {
                if ( styleIDs == null )
                {
                    styleIDs = new ArrayList<String>();
                }
                styleIDs.add( 0, style.getVal() );
            }
        }
        if ( styleIDs != null )
        {
            return styleIDs.toArray( StringUtils.EMPTY_STRING_ARRAY );
        }
        return null;
    }

    @Override
    protected CTStyle getDefaultStyle( XWPFRun element, XWPFStylesDocument styleManager )
    {
        return styleManager.getDefaultParagraphStyle();
    }

    @Override
    protected XWPFTableCell getParentTableCell( XWPFRun run )
    {
        return StylesHelper.getEmbeddedTableCell( run.getParagraph() );
    }
}
