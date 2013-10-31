package org.apache.poi.xwpf.converter.core.openxmlformats;

import java.util.List;

import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FontsDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument;

public interface IOpenXMLFormatsPartProvider
{

    CTDocument1 getDocument()
        throws Exception;

    CTStyles getStyle()
        throws Exception;

    List<FontsDocument> getFontsDocument()
        throws Exception;

    List<ThemeDocument> getThemeDocuments()
        throws Exception;

    CTSettings getSettings()
        throws Exception;

    HdrDocument getHdrDocumentByPartId( String relId )
        throws Exception;

    FtrDocument getFtrDocumentByPartId( String relId )
        throws Exception;

}
