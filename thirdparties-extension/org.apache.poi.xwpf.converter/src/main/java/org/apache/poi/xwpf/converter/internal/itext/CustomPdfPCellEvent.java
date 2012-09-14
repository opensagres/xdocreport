package org.apache.poi.xwpf.converter.internal.itext;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;

public class CustomPdfPCellEvent
    implements PdfPCellEvent
{

    @Override
    public void cellLayout( PdfPCell cell, Rectangle position, PdfContentByte[] canvases )
    {
        PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
        cb.setColorStroke( new GrayColor( 0.8f ) );
        cb.roundRectangle( position.getLeft() + 4, position.getBottom(), position.getWidth() - 8,
                           position.getHeight() - 4, 4 );
        cb.stroke();
    }

}
