package org.apache.poi.xwpf.converter.core.styles.pargraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;

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
        // if ( pargraph.getPartType() != BodyType.TABLECELL )
        // {
        return 10f;
        // }
        // return null;
    }

}
