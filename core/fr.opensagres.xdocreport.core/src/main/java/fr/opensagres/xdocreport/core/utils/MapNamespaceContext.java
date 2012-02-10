package fr.opensagres.xdocreport.core.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

public class MapNamespaceContext
    implements NamespaceContext
{
    private final Map<String, String> namespaces;

    public MapNamespaceContext()
    {
        this.namespaces = new HashMap<String, String>();
    }

    public void addNamespace( String prefix, String namespaceURI )
    {
        namespaces.put( prefix, namespaceURI );
    }

    public String getNamespaceURI( String prefix )
    {
        return namespaces.get( prefix );
    }

    public String getPrefix( String namespaceURI )
    {
        for ( Map.Entry<String, String> e : namespaces.entrySet() )
        {
            if ( e.getValue().equals( namespaceURI ) )
            {
                return e.getKey();
            }
        }
        return null;
    }

    public Iterator getPrefixes( String namespaceURI )
    {
        return null;
    }
}
