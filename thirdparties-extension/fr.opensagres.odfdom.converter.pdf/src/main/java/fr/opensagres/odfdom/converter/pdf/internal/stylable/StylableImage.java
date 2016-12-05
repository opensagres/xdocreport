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
package fr.opensagres.odfdom.converter.pdf.internal.stylable;

import java.util.zip.Inflater;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Image;

import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleGraphicProperties;
import fr.opensagres.xdocreport.itext.extension.ExtendedImage;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StylableImage
    implements IStylableElement
{
    private IStylableContainer parent;

    private Image image;

    private Float x;

    private Float y;

    private boolean runThrough;

    private Chunk chunk;

    private Style lastStyleApplied = null;

    public StylableImage( StylableDocument ownerDocument, IStylableContainer parent, Image image, Float x, Float y,
                          Float width, Float height )
    {
        this.parent = parent;
        this.image = image;
        this.x = x;
        this.y = y;
        if ( width != null )
        {
            image.scaleAbsoluteWidth( width );
        }
        if ( height != null )
        {
            image.scaleAbsoluteHeight( height );
        }
    }

    public void applyStyles( Style style )
    {
        lastStyleApplied = style;

        StyleGraphicProperties graphicProperties = style.getGraphicProperties();
        if ( graphicProperties != null )
        {
            runThrough = Boolean.TRUE.equals( graphicProperties.getRunThrough() );
        }
    }

    public Style getLastStyleApplied()
    {
        return lastStyleApplied;
    }

    public IStylableContainer getParent()
    {
        return parent;
    }

    public Element getElement()
    {
        if ( chunk == null )
        {
            float offsetX = x != null ? x : 0.0f;
            // negate offsetY because open office and iText vertical coordinates
            // are interpreted differently
            // in open office negative offset means "move up"
            // but in iText it means "move down"
            float offsetY = y != null ? -y : 0.0f;
            // iText image workaround
            // iText cannot draw an image higher than current vertical position
            // we create special image with y coordinate offset
            // this offset will be used while drawing by ExtendedPdfContentByte
            ExtendedImage extImg = new ExtendedImage( image, offsetY );
            // if run-through set line height to zero
            // so subsequent text will run through the image, not below
            chunk = new Chunk( extImg, offsetX, runThrough ? -image.getScaledHeight() : 0.0f );
        }
        return chunk;
    }

    public static Image getImage( byte[] imgb )
    {
        try
        {
            if ( imgb.length >= 6 && imgb[0] == 'V' && imgb[1] == 'C' && imgb[2] == 'L' && imgb[3] == 'M'
                && imgb[4] == 'T' && imgb[5] == 'F' )
            {
                // the image is in StarViewMetafile format
                // this format is undocumented and there is no java library to parse it
                // this image probably contains a wrapped bitmap
                // we don't try to interpret this format, instead we do a hack, we search for bitmap magic number
                int bmpStartOffset = getBmpStartOffset( imgb );
                if ( bmpStartOffset >= 0 )
                {
                    // we found the bitmap, which consists of
                    // <bitmap header - 14 bytes>
                    // <dib header - variable size>
                    // <image data - variable size>
                    int bmpFileSize = getInt( imgb, bmpStartOffset + 2 ); // whole bitmap size including headers
                    byte[] bmpb = new byte[bmpFileSize];
                    int bmpHeaderSize = 14;
                    int compressionMethod = getInt( imgb, bmpStartOffset + bmpHeaderSize + 16 );
                    int ZCOMPRESS = 'S' | 'D' << 8 | 0x01000000;
                    if ( compressionMethod == ZCOMPRESS )
                    {
                        // this is a new "invention", a bitmap with nonstandard compression
                        // of course it is undocumented too
                        // the idea how to process it was taken from
                        // 'vcl/source/gdi/bitmap2.cxx' - open office source file
                        //
                        // the original <image data> is replaced by
                        // <compressed data size - 4 bytes>
                        // <uncompressed data size - 4 bytes>
                        // <original compression method - 4 bytes>
                        // <zlib compressed image data - variable size>
                        int dibHeaderSize = getInt( imgb, bmpStartOffset + bmpHeaderSize );
                        int allHeadersSize = bmpHeaderSize + dibHeaderSize;
                        int compressedSize = getInt( imgb, bmpStartOffset + allHeadersSize );
                        int uncompressedSize = getInt( imgb, bmpStartOffset + allHeadersSize + 4 );
                        // uncompress data
                        Inflater inflater = new Inflater();
                        inflater.setInput( imgb, bmpStartOffset + allHeadersSize + 12, compressedSize );
                        byte[] uncompressedData = new byte[uncompressedSize];
                        inflater.inflate( uncompressedData );
                        // gather all parts together
                        System.arraycopy( imgb, bmpStartOffset, bmpb, 0, allHeadersSize );
                        System.arraycopy( imgb, bmpStartOffset + allHeadersSize + 8, bmpb, bmpHeaderSize + 16, 4 );
                        System.arraycopy( uncompressedData, 0, bmpb, allHeadersSize, uncompressedSize );
                    }
                    else
                    {
                        // standard bitmap
                        System.arraycopy( imgb, bmpStartOffset, bmpb, 0, bmpFileSize );
                    }
                    imgb = bmpb;
                }
            }
            return Image.getInstance( imgb );
        }
        catch ( Exception e )
        {
            return null;
        }
    }

    private static int getBmpStartOffset( byte[] imgb )
    {
        for ( int i = 0; i < imgb.length - 1; i++ )
        {
            if ( imgb[i] == 'B' && imgb[i + 1] == 'M' )
            {
                return i;
            }
        }
        return -1;
    }

    private static int getInt( byte[] blob, int pos )
    {
        return ( blob[pos + 0] & 0xff ) + ( ( blob[pos + 1] & 0xff ) << 8 ) + ( ( blob[pos + 2] & 0xff ) << 16 )
            + ( ( blob[pos + 3] & 0xff ) << 24 );
    }
}
