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
package fr.opensagres.xdocreport.document.pptx.preprocessor;

import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isAP;
import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isAPPr;
import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isAR;
import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isATxBody;
import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isPTxBody;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.pptx.PPTXConstants;
import fr.opensagres.xdocreport.document.pptx.preprocessor.txbody.ATxBodyBufferedRegion;
import fr.opensagres.xdocreport.document.pptx.preprocessor.txbody.PTxBodyBufferedRegion;
import fr.opensagres.xdocreport.document.pptx.preprocessor.txbody.TxBodyBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocument;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class PPTXSlideDocument
    extends TransformedBufferedDocument
{

    private APBufferedRegion currentAPRegion;

    private ARBufferedRegion currentARRegion;

    private TxBodyBufferedRegion currentTtxBodyRegion;

    private final PPTXSlideContentHandler handler;

    public PPTXSlideDocument( PPTXSlideContentHandler handler )
    {
        this.handler = handler;
    }

    @Override
    protected boolean isTable( String arg0, String arg1, String arg2 )
    {
        return false;
    }

    @Override
    protected boolean isTableRow( String arg0, String arg1, String arg2 )
    {
        return false;
    }

    @Override
    protected BufferedElement createElement( BufferedElement parent, String uri, String localName, String name,
                                             Attributes attributes )
        throws SAXException
    {
        if ( isPTxBody( uri, localName, name ) )
        {
            currentTtxBodyRegion = new PTxBodyBufferedRegion( parent, uri, localName, name, attributes );
            return currentTtxBodyRegion;
        }
        if ( isATxBody( uri, localName, name ) )
        {
            currentTtxBodyRegion = new ATxBodyBufferedRegion( parent, uri, localName, name, attributes );
            return currentTtxBodyRegion;
        }
        if ( isAP( uri, localName, name ) )
        {
            currentAPRegion = new APBufferedRegion( this, parent, uri, localName, name, attributes );
            if ( currentTtxBodyRegion != null )
            {
                currentTtxBodyRegion.addRegion( currentAPRegion );
            }
            return currentAPRegion;
        }
        if ( isAPPr( uri, localName, name ) )
        {
            if ( currentAPRegion != null )
            {
                Integer level = getLevel( attributes );
                currentAPRegion.setLevel( level );
            }
        }
        if ( isAR( uri, localName, name ) )
        {
            currentARRegion = new ARBufferedRegion( parent, uri, localName, name, attributes );
            if ( currentAPRegion != null )
            {
                currentAPRegion.addRegion( currentARRegion );
            }
            return currentARRegion;
        }
        return super.createElement( parent, uri, localName, name, attributes );
    }

    private Integer getLevel( Attributes attributes )
    {
        if ( attributes == null )
        {
            return null;
        }
        return StringUtils.asInteger( attributes.getValue( PPTXConstants.LVL_ATTR ) );
    }

    @Override
    public void onEndEndElement( String uri, String localName, String name )
    {
        if ( ( isPTxBody( uri, localName, name ) || isATxBody( uri, localName, name ) ) && currentTtxBodyRegion != null )
        {
            super.onEndEndElement( uri, localName, name );
            currentTtxBodyRegion.process();
            currentTtxBodyRegion = null;
            return;
        }
        if ( isAP( uri, localName, name ) && currentAPRegion != null )
        {
            super.onEndEndElement( uri, localName, name );
            currentAPRegion = null;
            return;
        }
        if ( isAR( uri, localName, name ) && currentAPRegion != null )
        {
            super.onEndEndElement( uri, localName, name );
            currentARRegion = null;
            return;
        }
        super.onEndEndElement( uri, localName, name );
    }

    public APBufferedRegion getCurrentAPRegion()
    {
        return currentAPRegion;
    }

    public ARBufferedRegion getCurrentARRegion()
    {
        return currentARRegion;
    }

    public FieldsMetadata getFieldsMetadata()
    {
        return handler.getFieldsMetadata();
    }

    public IDocumentFormatter getFormatter()
    {
        return handler.getFormatter();
    }
}
