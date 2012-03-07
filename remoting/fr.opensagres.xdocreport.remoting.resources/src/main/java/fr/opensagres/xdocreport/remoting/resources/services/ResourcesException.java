package fr.opensagres.xdocreport.remoting.resources.services;

public class ResourcesException
    extends RuntimeException
{

    public ResourcesException( String message )
    {
        super( message );
    }

    public ResourcesException( Throwable e )
    {
        super( e );
    }

}
