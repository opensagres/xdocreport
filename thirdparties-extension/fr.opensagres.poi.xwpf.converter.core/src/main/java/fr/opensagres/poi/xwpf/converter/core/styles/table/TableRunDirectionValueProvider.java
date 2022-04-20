package fr.opensagres.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;

/*enabling bidirectional support -> table run direction*/
public class TableRunDirectionValueProvider extends AbstractTableValueProvider <CTOnOff>{

    public static final TableRunDirectionValueProvider INSTANCE = new TableRunDirectionValueProvider();

    @Override
    public CTOnOff getValue(CTTblPr tblPr) {
        return getTableRunDirection(tblPr);
    }

    @Override
    public CTOnOff getValue(CTTblPrBase tblPr) {
        return getTableRunDirection(tblPr);
    }

    private CTOnOff getTableRunDirection(CTTblPrBase tblPr){
        return tblPr == null || !tblPr.isSetBidiVisual() ? null : tblPr.getBidiVisual();
    }
}
