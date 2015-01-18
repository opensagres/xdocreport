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
package fr.opensagres.xdocreport.document.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * XDocReport support for SQL {@link ResultSet}. The implementation of this class
 * <ul>
 * <li>transform a {@link ResultSet} to {@link Map} which can be used instead of {@link IContext}.</li>
 * <li>update the {@link FieldsMetadata} with simple and list field from the {@link ResultSet}.</li>
 * </ul>
 */
public abstract class SQLDataProvider
    extends HashMap<String, Object>
{

    private static final long serialVersionUID = 6237301616073252200L;

    /**
     * SQL Data provider constructor.
     * 
     * @param rs the SQL {@link ResultSet}
     * @param metadata the {@link FieldsMetadata}.
     * @throws SQLException
     */
    public SQLDataProvider( ResultSet rs, FieldsMetadata metadata )
        throws SQLException
    {
        Map<String, Object> pojoItem = null;
        // Loop for each row of the ResultSet.
        while ( rs.next() )
        {
            pojoItem = null;
            int columnCount = getColumnCount( rs );
            String tableName = null;
            String columnLabel = null;
            // Loop for column of the current row
            for ( int columnIndex = 1; columnIndex < columnCount + 1; columnIndex++ )
            {
                // retrieve table name + column label.
                tableName = getTableName( rs, columnIndex );
                columnLabel = getColumnLabel( rs, columnIndex );

                String listName = getListName( tableName, columnLabel );
                if ( listName != null )
                {
                    // The field is a list, build a Collection
                    Collection<Map<String, Object>> list = (Collection<Map<String, Object>>) super.get( listName );
                    if ( list == null )
                    {
                        // initialize list if need.
                        list = new ArrayList<Map<String, Object>>();
                        super.put( listName, list );
                    }
                    if ( pojoItem == null )
                    {
                        pojoItem = new HashMap<String, Object>();
                        list.add( pojoItem );
                    }

                    String getterName = columnLabel;
                    boolean sameListName = ( listName.equals( tableName ) );
                    if ( !sameListName )
                    {
                        getterName = tableName + "_" + columnLabel;

                    }
                    // update the pojo item with current column value
                    pojoItem.put( getterName, rs.getObject( columnIndex ) );
                    String fieldName = listName + "." + getterName;
                    metadata.addFieldAsList( fieldName );
                    if ( !sameListName )
                    {
                        metadata.addFieldReplacement( tableName + "." + columnLabel, fieldName );
                    }

                }
                else
                {
                    // The field is a not list, build a Pojo with Map.
                    Map<String, Object> pojo = (Map<String, Object>) super.get( tableName );
                    if ( pojo == null )
                    {
                        pojo = new HashMap<String, Object>();
                        super.put( tableName, pojo );
                    }
                    // update the pojo item with current column value
                    metadata.addField( tableName + "." + columnLabel, null, null, null, null );
                    // update fields metadata with the field as simple field.
                    pojo.put( columnLabel, rs.getObject( columnIndex ) );
                }
            }
        }

    }

    /**
     * Returns the column count. Override this method if your JDBC driver cannot support that.
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    protected int getColumnCount( ResultSet rs )
        throws SQLException
    {
        return rs.getMetaData().getColumnCount();
    }

    /**
     * Returns the table name of the given column of the given ResultSet. Override this method if your JDBC driver
     * cannot support that.
     * 
     * @param rs
     * @param column
     * @return
     * @throws SQLException
     */
    protected String getTableName( ResultSet rs, int column )
        throws SQLException
    {
        return rs.getMetaData().getTableName( column );
    }

    /**
     * Returns the column label name of the given column of the given ResultSet. Override this method if your JDBC
     * driver cannot support that.
     * 
     * @param rs
     * @param column
     * @return
     * @throws SQLException
     */
    protected String getColumnLabel( ResultSet rs, int column )
        throws SQLException
    {
        return rs.getMetaData().getColumnLabel( column );
    }

    /**
     * Returns true if the given column of the given table is a field list and false otherwise.
     * 
     * @param tableName
     * @param columnName
     * @return
     */
    protected abstract String getListName( String tableName, String columnName );
}
