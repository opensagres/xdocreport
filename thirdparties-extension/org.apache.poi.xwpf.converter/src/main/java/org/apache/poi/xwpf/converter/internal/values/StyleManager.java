package org.apache.poi.xwpf.converter.internal.values;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public class StyleManager
    implements IStyleManager
{

    private final XWPFDocument document;

    private final Map<String, CTStyle> stylesByStyleId;

    public StyleManager( XWPFDocument document )
    {
        this.document = document;
        this.stylesByStyleId = new HashMap<String, CTStyle>();
        try
        {
            initialize();
        }
        catch ( XmlException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initialize()
        throws XmlException, IOException
    {
        List<CTStyle> styles = document.getStyle().getStyleList();
        for ( CTStyle style : styles )
        {
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff.Enum isDefault = style.getDefault();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.Enum stype = style.getType();

            stylesByStyleId.put( style.getStyleId(), style );
        }

    }

    @Override
    public CTStyle getStyle( String styleId )
    {
        return stylesByStyleId.get( styleId );
    }

    @Override
    public Object getValue( IValueProvider provider, String styleId )
    {
        if ( styleId != null && styleId.length() > 0 )
        {
            return null;
        }
        return null;
    }

}
