package org.apache.poi.xwpf.converter.core.styles.paragraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;

public class ParagraphIndentationFirstLineValueProvider
    extends AbstractIndentationParagraphValueProvider<Float>
{

    public static final ParagraphIndentationFirstLineValueProvider INSTANCE = new ParagraphIndentationFirstLineValueProvider();

    @Override
    public Float getValue( CTInd ind )
    {
        BigInteger firstLine = ind.getFirstLine();
        if ( firstLine != null )
        {
            return DxaUtil.dxa2points( firstLine );
        }
        return null;
    }

}
