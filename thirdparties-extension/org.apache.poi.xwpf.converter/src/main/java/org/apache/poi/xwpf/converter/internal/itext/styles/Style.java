/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
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
package org.apache.poi.xwpf.converter.internal.itext.styles;

import java.util.List;

import org.apache.poi.xwpf.converter.internal.itext.stylable.StyleColumnProperties;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StyleColumnsProperties;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StyleSectionProperties;

public class Style
{

    private final String styleName;

    private StylePageLayoutProperties pageLayoutProperties;

    private StyleHeaderFooterProperties headerProperties;

    private StyleHeaderFooterProperties footerProperties;

    private StyleParagraphProperties paragraphProperties;

    private StyleTextProperties textProperties;

    private StyleTableProperties tableProperties;

    private StyleTableRowProperties tableRowProperties;

    private StyleTableCellProperties tableCellProperties;

    private StyleSectionProperties sectionProperties;

    private StyleColumnProperties columnProperties;

    private StyleColumnsProperties columnsProperties;

    private List<StyleColumnProperties> columnPropertiesList;

    public Style( String styleName )
    {
        this.styleName = styleName;
    }

    public void merge( Style style )
    {
        // Merge paragraph properties
        if ( paragraphProperties == null )
        {
            if ( style.getParagraphProperties() != null )
            {
                paragraphProperties = new StyleParagraphProperties( style.getParagraphProperties() );
            }
        }
        else
        {
            if ( style.getParagraphProperties() != null )
            {
                paragraphProperties.merge( style.getParagraphProperties() );
            }
        }

        // Merge text properties
        if ( textProperties == null )
        {
            if ( style.getTextProperties() != null )
            {
                textProperties = new StyleTextProperties( style.getTextProperties() );
            }
        }
        else
        {
            if ( style.getTextProperties() != null )
            {
                textProperties.merge( style.getTextProperties() );
            }
        }

        // Merge table-cell properties
        if ( tableProperties == null )
        {
            if ( style.getTableProperties() != null )
            {
                tableProperties = new StyleTableProperties( style.getTableProperties() );
            }
        }
        else
        {
            if ( style.getTableProperties() != null )
            {
                tableProperties.merge( style.getTableProperties() );
            }
        }

        // Merge table-cell properties
        if ( tableCellProperties == null )
        {
            if ( style.getTableCellProperties() != null )
            {
                tableCellProperties = new StyleTableCellProperties( style.getTableCellProperties() );
            }
        }
        else
        {
            if ( style.getTableCellProperties() != null )
            {
                tableCellProperties.merge( style.getTableCellProperties() );
            }
        }
    }

    public StyleParagraphProperties getParagraphProperties()
    {
        return paragraphProperties;
    }

    public void setParagraphProperties( StyleParagraphProperties paragraphProperties )
    {
        this.paragraphProperties = paragraphProperties;
    }

    public StyleTextProperties getTextProperties()
    {
        return textProperties;
    }

    public void setTextProperties( StyleTextProperties textProperties )
    {
        this.textProperties = textProperties;
    }

    public StyleTableProperties getTableProperties()
    {
        return tableProperties;
    }

    public void setTableProperties( StyleTableProperties tableProperties )
    {
        this.tableProperties = tableProperties;
    }

    public StyleTableRowProperties getTableRowProperties()
    {
        return tableRowProperties;
    }

    public void setTableRowProperties( StyleTableRowProperties tableRowProperties )
    {
        this.tableRowProperties = tableRowProperties;
    }

    public StyleTableCellProperties getTableCellProperties()
    {
        return tableCellProperties;
    }

    public void setTableCellProperties( StyleTableCellProperties tableCellProperties )
    {
        this.tableCellProperties = tableCellProperties;
    }

    public StylePageLayoutProperties getPageLayoutProperties()
    {
        return pageLayoutProperties;
    }

    public void setPageLayoutProperties( StylePageLayoutProperties pageLayoutProperties )
    {
        this.pageLayoutProperties = pageLayoutProperties;
    }

    public StyleHeaderFooterProperties getHeaderProperties()
    {
        return headerProperties;
    }

    public void setHeaderProperties( StyleHeaderFooterProperties headerProperties )
    {
        this.headerProperties = headerProperties;
    }

    public StyleHeaderFooterProperties getFooterProperties()
    {
        return footerProperties;
    }

    public void setFooterProperties( StyleHeaderFooterProperties footerProperties )
    {
        this.footerProperties = footerProperties;
    }

    public String getStyleName()
    {
        return styleName;
    }

    public String getMasterPageName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public StyleSectionProperties getSectionProperties()
    {
        return sectionProperties;
    }

    public void setSectionProperties( StyleSectionProperties styleSectionProperties )
    {
        this.sectionProperties = styleSectionProperties;
    }

    public StyleColumnProperties getColumnProperties()
    {
        return columnProperties;
    }

    public void setColumnProperties( StyleColumnProperties columnProperties )
    {
        this.columnProperties = columnProperties;
    }

    public StyleColumnsProperties getColumnsProperties()
    {
        return columnsProperties;
    }

    public void setColumnsProperties( StyleColumnsProperties columnsProperties )
    {
        this.columnsProperties = columnsProperties;
    }

    public List<StyleColumnProperties> getColumnPropertiesList()
    {
        return columnPropertiesList;
    }

    public void setColumnPropertiesList( List<StyleColumnProperties> columnPropertiesList )
    {
        this.columnPropertiesList = columnPropertiesList;
    }
}
