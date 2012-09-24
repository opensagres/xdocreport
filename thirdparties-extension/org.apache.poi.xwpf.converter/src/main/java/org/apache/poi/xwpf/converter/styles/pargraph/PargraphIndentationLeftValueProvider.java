package org.apache.poi.xwpf.converter.styles.pargraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.internal.DxaUtil;
import org.apache.poi.xwpf.converter.styles.XWPFStylesDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

public class PargraphIndentationLeftValueProvider
    extends AbstractIndentationParagraphValueProvider<Float>
{

    public static final PargraphIndentationLeftValueProvider INSTANCE = new PargraphIndentationLeftValueProvider();

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
