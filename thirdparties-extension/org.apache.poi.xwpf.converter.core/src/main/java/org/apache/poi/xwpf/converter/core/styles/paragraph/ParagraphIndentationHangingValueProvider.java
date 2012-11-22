package org.apache.poi.xwpf.converter.core.styles.paragraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;

public class ParagraphIndentationHangingValueProvider
    extends AbstractIndentationParagraphValueProvider<Float>
{

    public static final ParagraphIndentationHangingValueProvider INSTANCE = new ParagraphIndentationHangingValueProvider();

    @Override
    public Float getValue( CTInd ind )
    {
        BigInteger hanging = ind.getHanging();
        if ( hanging != null )
        {
            return DxaUtil.dxa2points( hanging );
        }
        return null;
    }

}
