package fr.opensagres.xdocreport.document.docx.preprocessor;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.document.docx.textstyling.IDocxStylesGenerator;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * SAX Content Handler which parses word/styles.xml to add XDocReport styles (hyperlink, numbered list, etc...)
 */
public class DocxStylesDocumentContentHandler
    extends BufferedDocumentContentHandler
{

    // Elements
    private static final String STYLE_ELT = "style";

    private static final String STYLES_ELT = "styles";

    private static final String NAME_ELT = "name";

    // Attributes
    private static final String W_STYLE_ID_ATTR = "w:styleId";

    private static final String W_VAL_ATTR = "w:val";

    // Styles names
    private static final String HYPERLINK_STYLE_NAME = "Hyperlink";

    private final IDocumentFormatter formatter;

    private final Map<String, Object> sharedContext;

    // Current w:styleId attribute.
    private String currentStyleId = null;

    public DocxStylesDocumentContentHandler( IDocumentFormatter formatter, Map<String, Object> sharedContext )
    {
        this.formatter = formatter;
        this.sharedContext = sharedContext;
    }

    @Override
    public boolean doStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        if ( STYLE_ELT.equals( localName ) )
        {
            this.currentStyleId = attributes.getValue( W_STYLE_ID_ATTR );
        }
        else if ( NAME_ELT.equals( localName ) )
        {
            /**
             * w:style w:type="character" w:styleId="Lienhypertexte"> <w:name w:val="Hyperlink" />
             */
            String val = attributes.getValue( W_VAL_ATTR );
            if ( HYPERLINK_STYLE_NAME.equals( val ) )
            {
                DefaultStyle defaultStyle = getDefaultStyle();
                defaultStyle.setHyperLinkStyleId( currentStyleId );
            }
        }
        return super.doStartElement( uri, localName, name, attributes );
    }

    private DefaultStyle getDefaultStyle()
    {
        DefaultStyle defaultStyle = (DefaultStyle) sharedContext.get( DocxContextHelper.DEFAULT_STYLE_KEY );
        if ( defaultStyle == null )
        {
            defaultStyle = new DefaultStyle();
            sharedContext.put( DocxContextHelper.DEFAULT_STYLE_KEY, defaultStyle );
        }
        return defaultStyle;
    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {

        if ( STYLE_ELT.equals( localName ) )
        {
            this.currentStyleId = null;
        }
        else if ( STYLES_ELT.equals( localName ) )
        {
            // Generate Hyperlink styles
            IBufferedRegion region = getCurrentElement();
            region.append( formatter.getFunctionDirective( DocxContextHelper.STYLES_GENERATOR_KEY,
                                                           IDocxStylesGenerator.generateHyperlinkStyle,
                                                           DocxContextHelper.DEFAULT_STYLE_KEY ) );
        }
        super.doEndElement( uri, localName, name );
    }

    public IDocumentFormatter getFormatter()
    {
        return formatter;
    }
}
