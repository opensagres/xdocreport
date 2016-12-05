/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.poi.xwpf.converter.core.utils;

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
//        IBody body = paragraph.getBody();
//        if ( body != null && body.getPartType() == BodyType.TABLECELL )
//        {
//            XWPFTableCell cell = (XWPFTableCell) body;
//            XWPFTable table = cell.getTableRow().getTable();
//            String tableStyleID = table.getStyleID();
//            if ( StringUtils.isNotEmpty( tableStyleID ) )
//            {
//                if ( styleIDs == null )
//                {
//                    styleIDs = new ArrayList<String>();
//                }
//                styleIDs.add( tableStyleID );
//            }
//        }
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
