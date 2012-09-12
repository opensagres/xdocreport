package org.apache.poi.xwpf.converter.internal.itext.stylable;

import org.apache.poi.xwpf.converter.internal.IXWPFMasterPage;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import fr.opensagres.xdocreport.itext.extension.IMasterPage;
import fr.opensagres.xdocreport.itext.extension.IMasterPageHeaderFooter;
import fr.opensagres.xdocreport.itext.extension.MasterPage;

public class StylableMasterPage
    extends MasterPage
    implements IMasterPage, IXWPFMasterPage<IMasterPageHeaderFooter>
{

    private final CTSectPr sectPr;

    public StylableMasterPage( CTSectPr sectPr )
    {
        super( null );
        this.sectPr = sectPr;
    }

    public CTSectPr getSectPr()
    {
        return sectPr;
    }
}
