package org.apache.poi.xwpf.converters.core.styles.run;

import java.math.BigInteger;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class RunFontSizeValueProvider
    extends AbstractRunValueProvider<Integer>
{

    public static RunFontSizeValueProvider INSTANCE = new RunFontSizeValueProvider();

    @Override
    public Integer getValue( CTRPr pr )
    {
        return ( pr != null && pr.isSetSz() ) ? pr.getSz().getVal().divide( new BigInteger( "2" ) ).intValue() : null;
    }
}
