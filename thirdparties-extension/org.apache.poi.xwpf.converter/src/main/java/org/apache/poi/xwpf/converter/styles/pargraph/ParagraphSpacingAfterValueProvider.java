package org.apache.poi.xwpf.converter.styles.pargraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.internal.DxaUtil;
import org.apache.poi.xwpf.converter.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;

public class ParagraphSpacingAfterValueProvider
    extends AbstractSpacingParagraphValueProvider<Integer>
{

    public static ParagraphSpacingAfterValueProvider INSTANCE = new ParagraphSpacingAfterValueProvider();

    @Override
    protected Integer getDefaultValue( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        if ( paragraph.getPartType() == BodyType.TABLECELL )
        {
            return null;
        }
        return super.getDefaultValue( paragraph, stylesDocument );
    }

    @Override
    protected Integer getValue( CTSpacing spacing )
    {
        BigInteger after = spacing.getAfter();
        if ( after != null )
        {
            return DxaUtil.dxa2points( after );
        }
        return null;
    }

    @Override
    protected Integer getStaticValue( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        // if ( pargraph.getPartType() != BodyType.TABLECELL )
        // {
        return 10;
        // }
        // return null;
    }

}
