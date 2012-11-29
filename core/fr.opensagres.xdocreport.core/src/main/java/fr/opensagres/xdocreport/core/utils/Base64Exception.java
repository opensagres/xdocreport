package fr.opensagres.xdocreport.core.utils;

public class Base64Exception
    extends Exception
{

    private static final long serialVersionUID = -4692237798562339250L;

    public Base64Exception( String msg )
    {
        super( msg );
    }

    public Base64Exception( String msg, Throwable t )
    {
        super( msg, t );
    }

    public Base64Exception( Throwable cause )
    {
        super( cause );
    }
}