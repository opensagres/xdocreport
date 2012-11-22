package org.apache.poi.xwpf.converter.core.styles.paragraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;

public class ParagraphIndentationRightValueProvider
    extends AbstractIndentationParagraphValueProvider<Float>
{

    public static final ParagraphIndentationRightValueProvider INSTANCE = new ParagraphIndentationRightValueProvider();

    @Override
    public Float getValue( CTInd ind )
    {
        BigInteger right = ind.getRight();
        if ( right != null )
        {
            return DxaUtil.dxa2points( right );
        }
        return null;
    }

}
