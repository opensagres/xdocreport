package org.apache.poi.xwpf.converter.xhtml;

import java.io.OutputStream;
import java.io.Writer;

import org.xml.sax.ContentHandler;

/**
 * SAX {@link ContentHandler} factory used by the XHTML converter.
 * 
 */
public interface IContentHandlerFactory
{

    ContentHandler create( OutputStream out, Writer writer, XHTMLOptions options );

}
