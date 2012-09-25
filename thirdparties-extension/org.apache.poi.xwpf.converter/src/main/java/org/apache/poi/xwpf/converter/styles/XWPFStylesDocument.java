package org.apache.poi.xwpf.converter.styles;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.converter.styles.pargraph.ParagraphAlignmentValueProvider;
import org.apache.poi.xwpf.converter.styles.pargraph.ParagraphIndentationFirstLineValueProvider;
import org.apache.poi.xwpf.converter.styles.pargraph.ParagraphIndentationLeftValueProvider;
import org.apache.poi.xwpf.converter.styles.pargraph.ParagraphIndentationRightValueProvider;
import org.apache.poi.xwpf.converter.styles.pargraph.ParagraphSpacingAfterValueProvider;
import org.apache.poi.xwpf.converter.styles.pargraph.ParagraphSpacingBeforeValueProvider;
import org.apache.poi.xwpf.converter.styles.run.RunFontColorValueProvider;
import org.apache.poi.xwpf.converter.styles.run.RunFontFamilyValueProvider;
import org.apache.poi.xwpf.converter.styles.run.RunFontSizeValueProvider;
import org.apache.poi.xwpf.converter.styles.run.RunFontStyleBoldValueProvider;
import org.apache.poi.xwpf.converter.styles.run.RunFontStyleItalicValueProvider;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public class XWPFStylesDocument
{

    public static final Object EMPTY_VALUE = new Object();

    private final XWPFDocument document;

    private final Map<String, CTStyle> stylesByStyleId;

    private CTStyle defaultParagraphStyle;

    private final Map<String, Object> values;

    public XWPFStylesDocument( XWPFDocument document )
    {
        this.document = document;
        this.stylesByStyleId = new HashMap<String, CTStyle>();
        this.values = new HashMap<String, Object>();
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
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.Enum type = style.getType();

            if ( isDefault != null
                && isDefault.intValue() == org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff.INT_X_1 )
            {
                // default
                if ( type != null )
                {
                    switch ( type.intValue() )
                    {
                        case org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.INT_PARAGRAPH:
                            defaultParagraphStyle = style;
                    }
                }

            }
            stylesByStyleId.put( style.getStyleId(), style );
        }

    }

    public CTStyle getDefaultParagraphStyle()
    {
        return defaultParagraphStyle;
    }

    public CTStyle getStyle( String styleId )
    {
        return stylesByStyleId.get( styleId );
    }

    public CTDocDefaults getDocDefaults()
    {
        try
        {
            return document.getStyle().getDocDefaults();
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
        return null;
    }

    public Integer getSpacingBefore( XWPFParagraph docxParagraph )
    {
        return ParagraphSpacingBeforeValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public Integer getSpacingAfter( XWPFParagraph docxParagraph )
    {
        return ParagraphSpacingAfterValueProvider.INSTANCE.getValue( docxParagraph, this );
    }

    public Float getIndentationLeft( XWPFParagraph paragraph )
    {
        return ParagraphIndentationLeftValueProvider.INSTANCE.getValue( paragraph, this );
    }

    public Float getIndentationRight( XWPFParagraph paragraph )
    {
        return ParagraphIndentationRightValueProvider.INSTANCE.getValue( paragraph, this );
    }

    public Float getIndentationFirstLine( XWPFParagraph paragraph )
    {
        return ParagraphIndentationFirstLineValueProvider.INSTANCE.getValue( paragraph, this );
    }

    public <T> T getValue( String key )
    {
        return (T) values.get( key );
    }

    public <T> void setValue( String key, T value )
    {
        values.put( key, value );
    }

    public String getFontFamily( XWPFRun run )
    {
        return RunFontFamilyValueProvider.INSTANCE.getValue( run, this );
    }

    public Integer getFontSize( XWPFRun run )
    {
        return RunFontSizeValueProvider.INSTANCE.getValue( run, this );
    }

    public Boolean getFontStyleBold( XWPFRun run )
    {
        return RunFontStyleBoldValueProvider.INSTANCE.getValue( run, this );
    }

    public Boolean getFontStyleItalic( XWPFRun run )
    {
        return RunFontStyleItalicValueProvider.INSTANCE.getValue( run, this );
    }

    public Color getFontColor( XWPFRun run )
    {
        return RunFontColorValueProvider.INSTANCE.getValue( run, this );
    }

    public ParagraphAlignment getParagraphAlignment( XWPFParagraph paragraph )
    {
        return ParagraphAlignmentValueProvider.INSTANCE.getValue( paragraph, this );
    }
}
