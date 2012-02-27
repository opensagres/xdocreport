package fr.opensagres.xdocreport.document.tools.internal;

public class MainHelper
{

    public static String getValue( String[] args, int i ) throws BadArgException
    {
        if ( i == ( args.length - 1 ) )
        {
            throw new BadArgException();
        }
        else
        {
            return args[i + 1];
        }
    }
}
