/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.odfdom.converter.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.DefaultElementVisitor;
import org.odftoolkit.odfdom.dom.element.draw.DrawImageElement;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import fr.opensagres.odfdom.converter.core.utils.StringUtils;

public abstract class ElementVisitorConverter
    extends DefaultElementVisitor
{

    private static final Logger LOGGER = Logger.getLogger( ElementVisitorConverter.class.getName() );

    protected final OdfDocument odfDocument;

    protected final OutputStream out;

    protected final Writer writer;

    private final IImageExtractor extractor;

    public ElementVisitorConverter( final OdfDocument odfDocument, final OutputStream out, Writer writer )
    {
        this( odfDocument, null, out, writer );
    }

    public ElementVisitorConverter( final OdfDocument odfDocument, final IImageExtractor extractor,
                                    final OutputStream out, Writer writer )
    {
        this.odfDocument = odfDocument;
        this.out = out;
        this.writer = writer;
        this.extractor = extractor;
    }

    @Override
    public void visit( OdfElement ele )
    {
        Node node = ele.getFirstChild();

        int nodeType = -1;
        while ( node != null )
        {
            nodeType = node.getNodeType();
            switch ( nodeType )
            {
                case Node.ELEMENT_NODE:
                    OdfElement element = (OdfElement) node;
                    element.accept( this );
                    break;
                case Node.TEXT_NODE:
                    processTextNode( (Text) node );
            }
            node = node.getNextSibling();
        }
    }

    protected abstract void processTextNode( Text node );

    @Override
    public final void visit( DrawImageElement ele )
    {
        String href = ele.getXlinkHrefAttribute();
        if ( StringUtils.isNotEmpty( href ) )
        {
            IImageExtractor extractor = getExtractor();
            boolean needImageStream = isNeedImageStream() || extractor != null;
            byte[] imageStream = needImageStream ? odfDocument.getPackage().getBytes( href ) : null;
            if ( extractor != null && imageStream != null )
            {
                try
                {
                    extractor.extract( href, imageStream );
                }
                catch ( Throwable e )
                {
                    LOGGER.log( Level.SEVERE, "Error while extracting the image " + href, e );
                }

            }
            visitImage( ele, href, imageStream );
        }
    }

    protected void visitImage( DrawImageElement ele, String href, byte[] imageStream )
    {
        // do nothing
    }

    protected boolean isNeedImageStream()
    {
        return false;
    }

    public IImageExtractor getExtractor()
    {
        return extractor;
    }

    public void save()
        throws IOException
    {
        if ( out != null )
        {
            out.close();
        }
        if ( writer != null )
        {
            writer.close();
        }
    }
}
