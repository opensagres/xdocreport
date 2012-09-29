package org.apache.poi.xwpf.converter.core.styles.run;

import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class RunUnderlineValueProvider
    extends AbstractRunValueProvider<UnderlinePatterns>
{

    public static final RunUnderlineValueProvider INSTANCE = new RunUnderlineValueProvider();

    @Override
    public UnderlinePatterns getValue( CTRPr rPr )
    {
        return ( rPr != null && rPr.isSetU() ) ? UnderlinePatterns.valueOf( rPr.getU().getVal().intValue() )
                        : UnderlinePatterns.NONE;
    }
}
