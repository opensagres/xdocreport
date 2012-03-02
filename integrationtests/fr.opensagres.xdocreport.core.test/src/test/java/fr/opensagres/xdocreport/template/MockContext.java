package fr.opensagres.xdocreport.template;

import java.util.HashMap;

public class MockContext
    extends HashMap<String, Object>
    implements IContext
{

    public Object get( String key )
    {
        return super.get( key );
    }

}
