package org.apache.poi.xwpf.converter.styles.pargraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.internal.DxaUtil;
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
            return DxaUtil.dxa2pointsF( right );
        }
        return null;
    }

}
