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
package fr.opensagres.xdocreport.itext.extension;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfWriter;

public class ExtendedDocument
    extends Document
    implements IITextContainer
{
    private Map<String, IMasterPage> masterPagesCache = new HashMap<String, IMasterPage>();

    private IMasterPage defaultMasterPage;

    private final ExtendedHeaderFooter headerFooter;

    protected final PdfWriter writer;

    protected float originMarginTop;

    protected float originMarginBottom;

    protected float originMarginRight;

    protected float originMarginLeft;

    private PageOrientation orientation = PageOrientation.Portrait;

    public ExtendedDocument( OutputStream out )
        throws DocumentException
    {
        this( out, ( IPdfWriterConfiguration ) null );
    }

    public ExtendedDocument( OutputStream out, IPdfWriterConfiguration configuration )
        throws DocumentException
    {
        this.writer = ExtendedPdfWriter.getInstance( this, out );
        if (configuration != null) {
            configuration.configure( writer );
        }
        headerFooter = createExtendedHeaderFooter();
        initAttributes();
    }

    public ExtendedDocument(OutputStream out, IPdfAWriterConfiguration configuration)
        throws DocumentException
    {
        if ( configuration != null ) {
            this.writer = ExtendedPdfAWriter.getInstance( this, out, configuration );
            configuration.configure( (PdfAWriter) writer );
        } else {
            this.writer = ExtendedPdfAWriter.getInstance( this, out );
        }
        headerFooter = createExtendedHeaderFooter();
        initAttributes();
    }

    private void initAttributes()
    {
        writer.setPageEvent( headerFooter );
        this.originMarginTop = marginTop;
        this.originMarginBottom = marginBottom;
        this.originMarginRight = marginRight;
        this.originMarginLeft = marginLeft;
    }

    protected ExtendedHeaderFooter createExtendedHeaderFooter()
    {
        return new ExtendedHeaderFooter( this );
    }

    @Override
    public int getPageNumber()
    {
        return writer.getCurrentPageNumber();
    }

    public boolean setOriginalMargins( float marginLeft, float marginRight, float marginTop, float marginBottom )
    {
        this.originMarginTop = marginTop;
        this.originMarginBottom = marginBottom;
        this.originMarginRight = marginRight;
        this.originMarginLeft = marginLeft;
        return super.setMargins( marginLeft, marginRight, marginTop, marginBottom );
    }

    public void addElement( Element element )
    {
        try
        {
            add( element );
        }
        catch ( DocumentException e )
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add( Element element )
        throws DocumentException
    {
        if ( !isOpen() )
        {
            open();
        }
        return super.add( element );
    }

    public IMasterPage getDefaultMasterPage()
    {
        return defaultMasterPage;
    }

    public float getOriginMarginBottom()
    {
        return originMarginBottom;
    }

    public float getOriginMarginLeft()
    {
        return originMarginLeft;
    }

    public float getOriginMarginRight()
    {
        return originMarginRight;
    }

    public float getOriginMarginTop()
    {
        return originMarginTop;
    }

    public void setActiveMasterPage( IMasterPage masterPage )
    {
        headerFooter.setMasterPage( masterPage );
    }

    public IMasterPage getActiveMasterPage() {
        return headerFooter.getMasterPage();
    }

    public void addMasterPage( IMasterPage currentMasterPage )
    {
        if ( defaultMasterPage == null )
        {
            defaultMasterPage = currentMasterPage;
        }
        masterPagesCache.put( currentMasterPage.getName(), currentMasterPage );
    }

    public void setActiveMasterPage( String masterPageName )
    {
        IMasterPage masterPage = getMasterPage( masterPageName );
        if ( masterPage != null )
        {
            setActiveMasterPage( masterPage );
        }
    }

    public IMasterPage getMasterPage( String masterPageName )
    {
        if ( masterPageName == null )
        {
            return null;
        }
        return masterPagesCache.get( masterPageName );
    }

    public IITextContainer getITextContainer()
    {
        return null;
    }

    public void setITextContainer( IITextContainer container )
    {

    }

    public PageOrientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation( PageOrientation orientation )
    {
        if ( !this.orientation.equals( orientation ) )
        {
            super.getPageSize().rotate();
        }
        this.orientation = orientation;
    }
}
