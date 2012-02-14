package fr.opensagres.xdocreport.document.textstyling;

import java.io.IOException;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;

public class Style
{
    private static String[] searchList = { "\r", "\t", "\n" };

    private static String[] replacementList = { "", " ", "" };

    private final String id;

    private final String content;

    public Style( String id, String content )
    {
        this.id = id;
        this.content = content;
    }

    public String getId()
    {
        return id;
    }

    public String getContent()
    {
        return content;
    }

    public static Style load( String id, Class clazz )
    {
        String content = "";
        try
        {
            content = IOUtils.toString( clazz.getResourceAsStream( id + ".xml" ) );
            content = StringUtils.replaceEach( content, searchList, replacementList );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        Style style = new Style( id, content );
        return style;
    }
}
