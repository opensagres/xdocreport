package org.apache.poi.xwpf.converter.core;

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
