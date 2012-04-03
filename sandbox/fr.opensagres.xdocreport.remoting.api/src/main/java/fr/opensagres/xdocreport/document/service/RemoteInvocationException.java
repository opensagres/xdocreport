package fr.opensagres.xdocreport.document.service;

public class RemoteInvocationException
    extends RuntimeException
{

    /**
     *
     */
    private static final long serialVersionUID = 7138194539569870719L;

    public RemoteInvocationException(String message)
    {
        super(message);
    }
    public RemoteInvocationException(String message, StackTraceElement[] rootCause)
    {
        super(message);
        setStackTrace( rootCause );
    }

}
