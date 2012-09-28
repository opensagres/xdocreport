package org.apache.poi.xwpf.converter.core.styles.run;

import java.math.BigInteger;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class RunFontSizeValueProvider
    extends AbstractRunValueProvider<Float>
{

    public static RunFontSizeValueProvider INSTANCE = new RunFontSizeValueProvider();

    @Override
    public Float getValue( CTRPr pr )
    {
        return ( pr != null && pr.isSetSz() ) ? pr.getSz().getVal().divide( new BigInteger( "2" ) ).floatValue() : null;
    }
}
