package org.apache.poi.xwpf.converter.core;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

public interface IMasterPageHandler<E extends IXWPFMasterPage>
{

    XWPFStylesDocument getStylesDocument();

    E createMasterPage( CTSectPr sectPr );

    void setActiveMasterPage( E currentMasterPage );

    void visitHeaderRef( CTHdrFtrRef headerRef, CTSectPr sectPr, E masterPage )
        throws Exception;

    void visitFooterRef( CTHdrFtrRef footerRef, CTSectPr sectPr, E masterPage )
        throws Exception;

}
