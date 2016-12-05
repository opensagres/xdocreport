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
package fr.opensagres.poi.xwpf.converter.core;

import java.util.HashMap;
import java.util.Map;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;

public class ListContext
    extends ListItemContext
{

    private final Map<Integer, ListItemContext> listItemByLevel;

    private int levelMax;
    
    public ListContext()
    {
        super( null, 0, null );
        this.listItemByLevel = new HashMap<Integer, ListItemContext>();
    }

    public ListItemContext addItem( CTLvl lvl )
    {
        int level = lvl.getIlvl().intValue();
        ListItemContext parent = getParent( level - 1 );
        ListItemContext newItem = parent.createAndAddItem( lvl );
        for ( int i = level; i <= levelMax; i++ )
        {
            listItemByLevel.remove( i );
        }
        listItemByLevel.put( level, newItem );
        if (level>levelMax) {
            levelMax = level;
        }
        return newItem;
    }

    private ListItemContext getParent( int level )
    {
        if ( level < 0 )
        {
            return this;
        }
        ListItemContext parent = null;
        for ( int i = level; i >= 0; i-- )
        {
            parent = listItemByLevel.get( level );
            if ( parent != null )
            {
                return parent;
            }
        }
        return this;
    }
    
    @Override
    public boolean isRoot() {
        return true;
    }
}
