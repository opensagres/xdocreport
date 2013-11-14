package org.apache.poi.xwpf.converter.xhtml;

import java.io.OutputStream;
import java.io.Writer;

import org.xml.sax.ContentHandler;

/**
 * Default {@link IContentHandlerFactory} which create instance SAX {@link SimpleContentHandler}.
 */
public class DefaultContentHandlerFactory
    implements IContentHandlerFactory
{

    public static final IContentHandlerFactory INSTANCE = new DefaultContentHandlerFactory();

    public ContentHandler create( OutputStream out, Writer writer, XHTMLOptions options )
    {

        return out != null ? new SimpleContentHandler( out, options.getIndent() )
                        : new SimpleContentHandler( writer, options.getIndent() );
    }
}
