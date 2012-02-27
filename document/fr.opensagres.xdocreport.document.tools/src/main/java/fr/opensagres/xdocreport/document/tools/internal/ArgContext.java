package fr.opensagres.xdocreport.document.tools.internal;

import java.util.HashMap;

public class ArgContext
    extends HashMap<String, String>
{

    public ArgContext( String args[] )
    {
        String key = null;
        String value = null;
        for ( int i = 0; i < args.length; i++ )
        {
            if ( i % 2 == 0 )
            {
                key = args[i];
            }
            else
            {
                value = args[i];
                super.put( key, value );
                key = null;
            }
        }
    }
}
