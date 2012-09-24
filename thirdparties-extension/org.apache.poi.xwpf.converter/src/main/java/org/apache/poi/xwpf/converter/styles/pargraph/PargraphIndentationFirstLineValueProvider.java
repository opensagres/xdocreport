package org.apache.poi.xwpf.converter.styles.pargraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.internal.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;

public class PargraphIndentationFirstLineValueProvider
    extends AbstractIndentationParagraphValueProvider<Float>
{

    public static final PargraphIndentationFirstLineValueProvider INSTANCE = new PargraphIndentationFirstLineValueProvider();

    @Override
    public Float getValue( CTInd ind )
    {
        BigInteger firstLine = ind.getFirstLine();
        if ( firstLine != null )
        {
            return DxaUtil.dxa2pointsF( firstLine );
        }
        return null;
    }

}
