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

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class ExtendedPdfContentByte
    extends PdfContentByte
{
    public ExtendedPdfContentByte( PdfWriter wr )
    {
        super( wr );
    }

    @Override
    public PdfContentByte getDuplicate()
    {
        return new ExtendedPdfContentByte( writer );
    }

    @Override
    public void addImage( Image image, double a, double b, double c, double d, double e, double f, boolean inlineImage, boolean isMCBlockOpened )
        throws DocumentException
    {
        if ( image instanceof ExtendedImage )
        {
            ExtendedImage extImg = (ExtendedImage) image;
            super.addImage( extImg.getImage(), a, b, c, d, e, f + extImg.getOffsetY(), inlineImage, isMCBlockOpened );
        }
        else
        {
            super.addImage( image, a, b, c, d, e, f, inlineImage, isMCBlockOpened );
        }
    }
}
