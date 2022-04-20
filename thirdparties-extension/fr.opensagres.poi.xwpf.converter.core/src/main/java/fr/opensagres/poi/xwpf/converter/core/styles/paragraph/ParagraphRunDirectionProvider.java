package fr.opensagres.poi.xwpf.converter.core.styles.paragraph;


import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
/*enabling bidirectional support -> paragraph direction*/
public class ParagraphRunDirectionProvider extends AbstractParagraphValueProvider<CTOnOff>{

    public static final ParagraphRunDirectionProvider INSTANCE = new ParagraphRunDirectionProvider();

    @Override
    public CTOnOff getValue(CTPPr ppr) {
        return ppr == null || !ppr.isSetBidi() ? null : ppr.getBidi();
    }
}
