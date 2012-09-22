package org.apache.poi.xwpf.converter.styles.pargraph;

import org.apache.poi.xwpf.converter.styles.AbstractValueProvider;
import org.apache.poi.xwpf.converter.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

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

    @Override
    protected String getStyleID( XWPFParagraph paragraph )
    {
        return paragraph.getStyleID();
    }

    @Override
    protected CTStyle getDefaultStype( XWPFParagraph element, XWPFStylesDocument styleManager )
    {
        return styleManager.getDefaultParagraphStyle();
    }
}
