package fr.opensagres.xdocreport.template;

import java.util.HashMap;

import fr.opensagres.xdocreport.template.IContext;

public class MockContext
    extends HashMap<String, Object>
    implements IContext
{

    public Object get( String key )
    {
        return super.get( key );
    }

}
