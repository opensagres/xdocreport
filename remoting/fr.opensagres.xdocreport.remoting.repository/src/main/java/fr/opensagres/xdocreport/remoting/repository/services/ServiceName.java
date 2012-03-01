package fr.opensagres.xdocreport.remoting.repository.services;

public enum ServiceName
{

    name, root;

    public static ServiceName getServiceName( String value )
    {
        ServiceName[] names = ServiceName.values();
        ServiceName currentName = null;
        for ( int i = 0; i < names.length; i++ )
        {
            currentName = names[i];
            if ( names[i].name().equals( value ) )
            {
                return currentName;
            }
        }
        return null;
    }
}
