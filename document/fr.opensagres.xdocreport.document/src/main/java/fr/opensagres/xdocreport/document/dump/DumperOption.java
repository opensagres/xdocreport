package fr.opensagres.xdocreport.document.dump;

import java.util.HashMap;

public class DumperOption
    extends HashMap<String, Object>
{

    private static final long serialVersionUID = 1L;

    public static final String PACKAGE_NAME = "packageName";


    public String getPackageName()
    {
        return (String) super.get( PACKAGE_NAME );
    }

    public void setPackageName( String packageName )
    {
        super.put( PACKAGE_NAME, packageName );
    }

}
