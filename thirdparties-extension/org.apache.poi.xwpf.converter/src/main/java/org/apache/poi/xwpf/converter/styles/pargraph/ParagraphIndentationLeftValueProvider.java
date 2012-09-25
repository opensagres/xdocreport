package org.apache.poi.xwpf.converter.styles.pargraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.internal.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;

public class ParagraphIndentationLeftValueProvider
    extends AbstractIndentationParagraphValueProvider<Float>
{

    public static final ParagraphIndentationLeftValueProvider INSTANCE = new ParagraphIndentationLeftValueProvider();

    @Override
    public Float getValue( CTInd ind )
    {
        BigInteger left = ind.getLeft();
        if ( left != null )
        {
            return DxaUtil.dxa2pointsF( left );
        }
        return null;
    }

}
