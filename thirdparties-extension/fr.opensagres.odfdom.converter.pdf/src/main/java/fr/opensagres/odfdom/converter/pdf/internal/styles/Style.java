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
package fr.opensagres.odfdom.converter.pdf.internal.styles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.itext.extension.font.IFontProvider;

public class Style
{
    private final String styleName;

    private final String familyName;

    private String masterPageName;

    private StylePageLayoutProperties pageLayoutProperties;

    private StyleHeaderFooterProperties headerProperties;

    private StyleHeaderFooterProperties footerProperties;

    private StyleParagraphProperties paragraphProperties;

    private List<StyleTabStopProperties> tabStopPropertiesList;

    private StyleTextProperties textProperties;

    private StyleTableProperties tableProperties;

    private StyleTableRowProperties tableRowProperties;

    private StyleTableCellProperties tableCellProperties;

    private StyleGraphicProperties graphicProperties;

    private Map<Integer, StyleListProperties> listPropertiesMap;

    private Map<Integer, StyleListProperties> outlinePropertiesMap;

    private StyleSectionProperties sectionProperties;

    private List<StyleColumnProperties> columnPropertiesList;

    private final IFontProvider fontProvider;

    public Style( IFontProvider fontProvider, String styleName, String familyName, String masterPageName )
    {
        this.styleName = styleName;
        this.familyName = familyName;
        this.masterPageName = masterPageName;
        this.fontProvider = fontProvider;
    }

    public void merge( Style style, boolean fullPropagation )
    {
        if ( fullPropagation )
        {
            // propagate properties which are not fully merged
            if ( style.getPageLayoutProperties() != null )
            {
                pageLayoutProperties = style.getPageLayoutProperties();
            }
            if ( style.getHeaderProperties() != null )
            {
                headerProperties = style.getHeaderProperties();
            }
            if ( style.getFooterProperties() != null )
            {
                footerProperties = style.getFooterProperties();
            }
            if ( style.getTabStopPropertiesList() != null )
            {
                tabStopPropertiesList = new ArrayList<StyleTabStopProperties>( style.getTabStopPropertiesList() );
                tabStopPropertiesList = Collections.unmodifiableList( tabStopPropertiesList );
            }
            if ( style.getListPropertiesMap() != null )
            {
                listPropertiesMap = new HashMap<Integer, StyleListProperties>( style.getListPropertiesMap() );
                listPropertiesMap = Collections.unmodifiableMap( listPropertiesMap );
            }
            if ( style.getOutlinePropertiesMap() != null )
            {
                outlinePropertiesMap = new HashMap<Integer, StyleListProperties>( style.getOutlinePropertiesMap() );
                outlinePropertiesMap = Collections.unmodifiableMap( outlinePropertiesMap );
            }
            if ( style.getSectionProperties() != null )
            {
                sectionProperties = style.getSectionProperties();
            }
            if ( style.getColumnPropertiesList() != null )
            {
                columnPropertiesList = new ArrayList<StyleColumnProperties>( style.getColumnPropertiesList() );
                columnPropertiesList = Collections.unmodifiableList( columnPropertiesList );
            }
        }

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
                textProperties = new StyleTextProperties( fontProvider, style.getTextProperties() );
            }
        }
        else
        {
            if ( style.getTextProperties() != null )
            {
                textProperties.merge( style.getTextProperties() );
            }
        }

        // Merge table properties
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

        // Merge table-row properties
        if ( tableRowProperties == null )
        {
            if ( style.getTableRowProperties() != null )
            {
                tableRowProperties = new StyleTableRowProperties( style.getTableRowProperties() );
            }
        }
        else
        {
            if ( style.getTableRowProperties() != null )
            {
                tableRowProperties.merge( style.getTableRowProperties() );
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

        // Merge graphic properties
        if ( graphicProperties == null )
        {
            if ( style.getGraphicProperties() != null )
            {
                graphicProperties = new StyleGraphicProperties( style.getGraphicProperties() );
            }
        }
        else
        {
            if ( style.getGraphicProperties() != null )
            {
                graphicProperties.merge( style.getGraphicProperties() );
            }
        }
    }

    public String getStyleName()
    {
        return styleName;
    }

    public String getFamilyName()
    {
        return familyName;
    }

    public String getMasterPageName()
    {
        return masterPageName;
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

    public StyleParagraphProperties getParagraphProperties()
    {
        return paragraphProperties;
    }

    public void setParagraphProperties( StyleParagraphProperties paragraphProperties )
    {
        this.paragraphProperties = paragraphProperties;
    }

    public List<StyleTabStopProperties> getTabStopPropertiesList()
    {
        return tabStopPropertiesList;
    }

    public void setTabStopPropertiesList( List<StyleTabStopProperties> tabStopPropertiesList )
    {
        this.tabStopPropertiesList = tabStopPropertiesList;
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

    public StyleGraphicProperties getGraphicProperties()
    {
        return graphicProperties;
    }

    public void setGraphicProperties( StyleGraphicProperties graphicProperties )
    {
        this.graphicProperties = graphicProperties;
    }

    public Map<Integer, StyleListProperties> getListPropertiesMap()
    {
        return listPropertiesMap;
    }

    public void setListPropertiesMap( Map<Integer, StyleListProperties> listPropertiesMap )
    {
        this.listPropertiesMap = listPropertiesMap;
    }

    public Map<Integer, StyleListProperties> getOutlinePropertiesMap()
    {
        return outlinePropertiesMap;
    }

    public void setOutlinePropertiesMap( Map<Integer, StyleListProperties> outlinePropertiesMap )
    {
        this.outlinePropertiesMap = outlinePropertiesMap;
    }

    public StyleSectionProperties getSectionProperties()
    {
        return sectionProperties;
    }

    public void setSectionProperties( StyleSectionProperties sectionProperties )
    {
        this.sectionProperties = sectionProperties;
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
