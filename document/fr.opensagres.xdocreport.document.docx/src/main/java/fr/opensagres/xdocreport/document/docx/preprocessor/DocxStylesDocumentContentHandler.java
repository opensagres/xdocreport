package fr.opensagres.xdocreport.document.docx.preprocessor;

import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.document.docx.textstyling.DocxStylesGeneratorProvider;
import fr.opensagres.xdocreport.document.docx.textstyling.IDocxStylesGenerator;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;

/**
 * SAX Content Handler which parses word/styles.xml to add XDocReport styles (hyperlink, numbered list, etc...)
 */
public class DocxStylesDocumentContentHandler
    extends BufferedDocumentContentHandler
{

    private static final String STYLES_ELT = "styles";

    protected final IDocxStylesGenerator stylesGen;

    public DocxStylesDocumentContentHandler()
    {
        stylesGen = DocxStylesGeneratorProvider.getStyleGenerator();
    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {
        if ( STYLES_ELT.equals( localName ) )
        {
            // Generate Hyperlink styles
            IBufferedRegion region = getCurrentElement();
            region.append( stylesGen.generateHyperlinkStyle() );
        }
        super.doEndElement( uri, localName, name );
    }
}
