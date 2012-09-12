package org.apache.poi.xwpf.converter.internal.xhtml;

import org.apache.poi.xwpf.converter.internal.IXWPFMasterPage;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

public class XHTMLMasterPage
    implements IXWPFMasterPage<String>
{

    private final CTSectPr sectPr;

    private String header;

    private String footer;

    public XHTMLMasterPage( CTSectPr sectPr )
    {
        this.sectPr = sectPr;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader( String header )
    {
        this.header = header;
    }

    public String getFooter()
    {
        return footer;
    }

    public void setFooter( String footer )
    {
        this.footer = footer;
    }

    public CTSectPr getSectPr()
    {
        return sectPr;
    }

}
