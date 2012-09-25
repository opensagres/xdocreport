package org.apache.poi.xwpf.converters.core.styles.pargraph;

import java.math.BigInteger;

import org.apache.poi.xwpf.converters.core.utils.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;

public class ParagraphSpacingBeforeValueProvider
    extends AbstractSpacingParagraphValueProvider<Float>
{

    public static ParagraphSpacingBeforeValueProvider INSTANCE = new ParagraphSpacingBeforeValueProvider();

    @Override
    protected Float getValue( CTSpacing spacing )
    {
        BigInteger before = spacing.getBefore();
        if ( before != null )
        {
            return DxaUtil.dxa2points( before );
        }
        return null;
    }
}
