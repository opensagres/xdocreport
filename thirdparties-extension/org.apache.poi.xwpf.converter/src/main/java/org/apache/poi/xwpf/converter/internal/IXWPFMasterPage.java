package org.apache.poi.xwpf.converter.internal;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

public interface IXWPFMasterPage<T>
{

    public T getHeader();

    public void setHeader( T header );

    public T getFooter();

    public void setFooter( T footer );

    public CTSectPr getSectPr();
}
