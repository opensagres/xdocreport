package fr.opensagres.xdocreport.remoting.resources.services;

public class ResourcesException
    extends RuntimeException
{

    /**
	 *
	 */
	private static final long serialVersionUID = 540848168001129189L;

	public ResourcesException( String message )
    {
        super( message );
    }

    public ResourcesException( Throwable e )
    {
        super( e );
    }

}
