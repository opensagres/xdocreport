package fr.opensagres.xdocreport.document.odt.textstyling;

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
