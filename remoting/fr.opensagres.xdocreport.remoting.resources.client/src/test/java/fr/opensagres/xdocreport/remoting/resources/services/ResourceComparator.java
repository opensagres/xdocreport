package fr.opensagres.xdocreport.remoting.resources.services;

import java.util.Comparator;

import fr.opensagres.xdocreport.remoting.resources.domain.Resource;

public class ResourceComparator
    implements Comparator<Resource>
{

    public static Comparator<Resource> INSTANCE = new ResourceComparator();

    public int compare( Resource o1, Resource o2 )
    {
        return (o1.getName().compareTo( o2.getName() ));
    }

}
