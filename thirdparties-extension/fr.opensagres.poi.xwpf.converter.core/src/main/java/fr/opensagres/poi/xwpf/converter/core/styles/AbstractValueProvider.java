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
package fr.opensagres.poi.xwpf.converter.core.styles;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr;

import fr.opensagres.poi.xwpf.converter.core.utils.StringUtils;

public abstract class AbstractValueProvider<Value, XWPFElement>
    implements IValueProvider<Value, XWPFElement>
{

    public Value getValue( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        // long start = System.currentTimeMillis();
        Value value = internalGetValue( element, stylesDocument );
        // System.err.println( "value=" + value + " with " + ( System.currentTimeMillis() - start ) + "ms" );
        return value;

    };

    public Value internalGetValue( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        // 1) Inline style : search value retrieved from the XWPF element (XWPFParagraph, XWPFTable etc)
        Value value = getValueFromElement( element, stylesDocument );
        if ( value != null )
        {
            // Value declared in the inline style, return it.
            return value;
        }
        if ( stylesDocument == null )
        {
            return null;
        }
        // 2) External styles: search value declared in a style.
        return getValueFromStyles( element, stylesDocument );
    }

    public Value getValueFromStyles( XWPFElement element, XWPFStylesDocument stylesDocument )
    {

        // 1) At first get from cache or compute the default value
        String key = getKey( element, stylesDocument, null, null );
        // search from the cache
        Object defaultValue = stylesDocument.getValue( key );
        if ( defaultValue == null )
        {
            // compute the default value and cache it
            defaultValue = getDefaultValue( element, stylesDocument );
            if ( defaultValue == null )
            {
                defaultValue = XWPFStylesDocument.EMPTY_VALUE;
            }
            updateValueCache( stylesDocument, key, defaultValue );
        }

        // 2) Search value from the linked style
        Object result = getValueFromStyleIds( element, stylesDocument, defaultValue );
        if ( result != null )
        {
            return getValueOrNull( result );
        }
        // 3) Search if the XWPF element (paragraph or run) belongs to table which is styled.
        XWPFTableCell cell = getParentTableCell( element );
        if ( cell != null )
        {
            XWPFTable table = cell.getTableRow().getTable();
            String tableStyleID = table.getStyleID();
            if ( StringUtils.isNotEmpty( tableStyleID ) )
            {
                // the current XWPFElement paragraph, run, etc belongs to a cell of a table which is styled.

                // 1) search styles from <w:style w:type="table" w:styleId="XXX"><w:tblStylePr
                // w:type="firstRow">, <w:tblStylePr w:type="lastRow">, etc
                TableCellInfo cellInfo = stylesDocument.getTableCellInfo( cell );
                result = getValueFromTableStyleId( element, stylesDocument, tableStyleID, cellInfo );
                if ( result != null )
                {
                    return getValueOrNull( result );
                }
                // no styles founded, search from the <w:style w:type="table" w:styleId="XXXX">
                result = getValueFromStyleId( element, stylesDocument, tableStyleID, defaultValue );
                if ( result != null )
                {
                    return getValueOrNull( result );
                }
            }
        }
        updateValueCache( stylesDocument, key, defaultValue );
        return getValueOrNull( defaultValue );
    }

    private Object getValueFromTableStyleId( XWPFElement element, XWPFStylesDocument stylesDocument,
                                             String tableStyleID, TableCellInfo cellInfo )
    {
        if ( StringUtils.isEmpty( tableStyleID ) )
        {
            return null;
        }

        Object value = getValueFromTableStyleIdRow( element, stylesDocument, tableStyleID, cellInfo );
        if ( value != null )
        {
            return value;
        }
        if ( cellInfo.canApplyFirstCol() )
        {
            Object result = getValueFromTableStyleIdFirstCol( element, stylesDocument, tableStyleID );
            if ( result != null )
            {
                return result;
            }
        }
        else if ( cellInfo.canApplyLastCol() )
        {
            Object result = getValueFromTableStyleIdLastCol( element, stylesDocument, tableStyleID );
            if ( result != null )
            {
                return result;
            }
        }
        return null;
    }

    private Object getValueFromTableStyleIdRow( XWPFElement element, XWPFStylesDocument stylesDocument,
                                                String tableStyleID, TableCellInfo cellInfo )
    {
        if ( cellInfo.canApplyFirstRow() )
        {
            Object value = getValueFromTableStyleIdFirstRow( element, stylesDocument, tableStyleID );
            if ( value != null )
            {
                return value;
            }

        }
        else if ( cellInfo.canApplyLastRow() )
        {
            Object result = getValueFromTableStyleIdLastRow( element, stylesDocument, tableStyleID );
            if ( result != null )
            {
                return result;
            }
        }
        return null;
    }

    private Object getValueFromTableStyleIdFirstRow( XWPFElement element, XWPFStylesDocument stylesDocument,
                                                     String tableStyleID )
    {
        return getValueFromTableStyleId( element,
                                         stylesDocument,
                                         tableStyleID,
                                         org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.FIRST_ROW );
    }

    private Object getValueFromTableStyleIdLastRow( XWPFElement element, XWPFStylesDocument stylesDocument,
                                                    String tableStyleID )
    {
        return getValueFromTableStyleId( element,
                                         stylesDocument,
                                         tableStyleID,
                                         org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.LAST_ROW );
    }

    private Object getValueFromTableStyleIdFirstCol( XWPFElement element, XWPFStylesDocument stylesDocument,
                                                     String tableStyleID )
    {
        return getValueFromTableStyleId( element,
                                         stylesDocument,
                                         tableStyleID,
                                         org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.FIRST_COL );
    }

    private Object getValueFromTableStyleIdLastCol( XWPFElement element, XWPFStylesDocument stylesDocument,
                                                    String tableStyleID )
    {
        return getValueFromTableStyleId( element,
                                         stylesDocument,
                                         tableStyleID,
                                         org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.LAST_COL );
    }

    private Object getValueFromTableStyleId( XWPFElement element,
                                             XWPFStylesDocument stylesDocument,
                                             String tableStyleID,
                                             org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.Enum type )
    {
        Object defaultValue = XWPFStylesDocument.EMPTY_VALUE;
        if ( StringUtils.isEmpty( tableStyleID ) )
        {
            return null;
        }

        // Search from cache
        String key = getKey( element, stylesDocument, tableStyleID, type );
        Object result = stylesDocument.getValue( key );
        if ( result != null )
        {
            return getValueOrNull( result );
        }
        // Value is not computed, compute it

        // Get the table style
        CTStyle style = stylesDocument.getStyle( tableStyleID );
        if ( style == null )
        {
            // should never come
            stylesDocument.setValue( key, defaultValue );
            return null;
        }

        // try to compute it
        Object value = null;
        CTTblStylePr tblStylePr = stylesDocument.getTableStyle( tableStyleID, type );
        if ( tblStylePr != null )
        {
            value = getValueFromTableStyle( tblStylePr, stylesDocument );
            if ( value != null )
            {
                // Value is computed, cache it and return it.
                stylesDocument.setValue( key, value );
                return value;
            }

            // Check if style has ancestor with basedOn
            value = getValueFromTableStyleId( element, stylesDocument, getBasisStyleID( style ), type );
        }
        value = value != null ? value : defaultValue;
        updateValueCache( stylesDocument, key, value );
        return getValueOrNull( value );
    }

    private Value getValueOrNull( Object result )
    {
        return result.equals( XWPFStylesDocument.EMPTY_VALUE ) ? null : (Value) result;
    }

    public Object getValueFromStyleIds( XWPFElement element, XWPFStylesDocument stylesDocument, Object defaultValue )
    {
        String[] styleIds = getStyleID( element );
        if ( styleIds != null )
        {
            String styleId = null;
            for ( int i = 0; i < styleIds.length; i++ )
            {
                styleId = styleIds[i];
                Object value = getValueFromStyleId( element, stylesDocument, styleId, defaultValue );
                if ( value != null )
                {
                    return getValueOrNull( value );
                }
            }
        }
        return null;
    }

    public abstract Value getValueFromElement( XWPFElement element, XWPFStylesDocument stylesDocument );

    protected Value getDefaultValue( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        Value value = getValueFromDefaultStyle( element, stylesDocument );
        if ( value != null )
        {
            return value;
        }
        value = getValueFromDocDefaultsStyle( element, stylesDocument );
        if ( value != null )
        {
            return value;
        }
        return getStaticValue( element, stylesDocument );
    }

    protected Value getStaticValue( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        return null;
    }

    private Object getValueFromStyleId( XWPFElement element, XWPFStylesDocument stylesDocument, String styleId,
                                        Object defaultValue )
    {

        if ( StringUtils.isEmpty( styleId ) )
        {
            return null;
        }

        // Search from cache
        String key = getKey( element, stylesDocument, styleId, null );
        Object result = stylesDocument.getValue( key );
        if ( result != null )
        {
            return result;
        }

        // Value is not computed, compute it

        // Get the style
        CTStyle style = stylesDocument.getStyle( styleId );
        if ( style == null )
        {
            // should never come
            stylesDocument.setValue( key, defaultValue );
            return null;
        }

        // try to compute value
        Object value = getValueFromStyle( style, stylesDocument );
        if ( value != null )
        {
            // Value is computed, cache it and return it.
            stylesDocument.setValue( key, value );
            return value;
        }

        // Check if style has ancestor with basedOn
        value = getValueFromStyleId( element, stylesDocument, getBasisStyleID( style ), defaultValue );
        value = value != null ? value : defaultValue;
        updateValueCache( stylesDocument, key, value );
        return value;
    }

    private void updateValueCache( XWPFStylesDocument stylesDocument, String key, Object value )
    {
        if ( value != null )
        {
            // Value is computed, cache it and return it.
            stylesDocument.setValue( key, value );
        }
        else
        {
            stylesDocument.setValue( key, XWPFStylesDocument.EMPTY_VALUE );
        }
    }

    protected String getKey( XWPFElement element, XWPFStylesDocument stylesDocument, String styleId,
                             org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.Enum type )
    {
        return getKeyBuffer( element, stylesDocument, styleId, type ).toString();
    }

    protected StringBuilder getKeyBuffer( XWPFElement element,
                                          XWPFStylesDocument stylesDocument,
                                          String styleId,
                                          org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.Enum type )
    {
        StringBuilder key = new StringBuilder( this.getClass().getName() );
        if ( StringUtils.isNotEmpty( styleId ) )
        {
            key.append( "_" ).append( styleId ).toString();
        }
        if ( type != null )
        {
            key.append( "_table" );
            key.append( type.intValue() );
        }
        return key;
    }

    private String getBasisStyleID( CTStyle style )
    {
        if ( style.getBasedOn() != null )
            return style.getBasedOn().getVal();
        else
            return null;
    }

    protected abstract String[] getStyleID( XWPFElement element );

    protected abstract Value getValueFromStyle( CTStyle style, XWPFStylesDocument stylesDocument );

    protected abstract Value getValueFromTableStyle( CTTblStylePr tblStylePr, XWPFStylesDocument stylesDocument );

    protected Value getValueFromDefaultStyle( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        Value value = null;
        CTStyle style = getDefaultStyle( element, stylesDocument );
        if ( style != null )
        {
            value = getValueFromStyle( style, stylesDocument );
        }
        return value;
    }

    protected Value getValueFromDocDefaultsStyle( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        CTDocDefaults docDefaults = stylesDocument.getDocDefaults();
        if ( docDefaults == null )
        {
            return null;
        }
        Value value = getValueFromDocDefaultsStyle( docDefaults, stylesDocument );
        if ( value != null )
        {
            return value;
        }
        return null;
    }

    protected abstract Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument stylesDocument );

    protected abstract CTStyle getDefaultStyle( XWPFElement element, XWPFStylesDocument stylesDocument );

    /**
     * Returns the table cell which is the parent of the XWPF element and null otherwise
     * 
     * @param element
     * @return
     */
    protected abstract XWPFTableCell getParentTableCell( XWPFElement element );

}
