package fr.opensagres.poi.xwpf.converter.core.styles.run;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun;

public class RunVerticalAlignValueProvider extends AbstractRunValueProvider<VerticalAlign> {

    public static RunVerticalAlignValueProvider INSTANCE = new RunVerticalAlignValueProvider();

    @Override
    public VerticalAlign getValue(CTRPr rpr, XWPFStylesDocument stylesDocument) {
        if (rpr == null) {
            return VerticalAlign.BASELINE;
        }

        CTVerticalAlignRun vertAlign = rpr.getVertAlign();
        if (vertAlign == null) {
            return VerticalAlign.BASELINE;
        }

        return VerticalAlign.valueOf(vertAlign.getVal().intValue());
    }
}
