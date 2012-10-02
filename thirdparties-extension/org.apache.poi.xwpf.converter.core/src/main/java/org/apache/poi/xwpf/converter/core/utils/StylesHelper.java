package org.apache.poi.xwpf.converter.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.IBody;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;

public class StylesHelper
{

    public static List<String> getStyleIDs( XWPFParagraph paragraph )
    {
        List<String> styleIDs = null;
        String paragraphStyleId = paragraph.getStyleID();
        boolean hasParagraphStyleId = StringUtils.isNotEmpty( paragraphStyleId );
        if ( hasParagraphStyleId )
        {
            if ( styleIDs == null )
            {
                styleIDs = new ArrayList<String>();
            }
            styleIDs.add( paragraphStyleId );
        }
        // if paragraph is include in a table which defines a style, use this tabe style
        IBody body = paragraph.getBody();
        if ( body != null && body.getPartType() == BodyType.TABLECELL )
        {
            XWPFTableCell cell = (XWPFTableCell) body;
            XWPFTable table = cell.getTableRow().getTable();
            String tableStyleID = table.getStyleID();
            if ( StringUtils.isNotEmpty( tableStyleID ) )
            {
                if ( styleIDs == null )
                {
                    styleIDs = new ArrayList<String>();
                }
                styleIDs.add( tableStyleID );
            }
        }
        return styleIDs;
    }

    public static XWPFTableCell getEmbeddedTableCell( XWPFParagraph paragraph )
    {
        IBody body = paragraph.getBody();
        if ( body != null && body.getPartType() == BodyType.TABLECELL )
        {
            return (XWPFTableCell) body;
        }
        return null;
    }
}
