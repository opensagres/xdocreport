package org.apache.poi.xwpf.converter.styles.pargraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.internal.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;

public class PargraphSpacingAfterValueProvider
    extends AbstractSpacingParagraphValueProvider<Integer>
{

    public static PargraphSpacingAfterValueProvider INSTANCE = new PargraphSpacingAfterValueProvider();

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

}
