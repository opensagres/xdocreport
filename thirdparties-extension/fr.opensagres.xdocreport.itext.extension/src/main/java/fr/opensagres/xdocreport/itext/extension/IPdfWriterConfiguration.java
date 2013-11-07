package fr.opensagres.xdocreport.itext.extension;

import com.lowagie.text.pdf.PdfWriter;

/**
 * API for the configuration to use to configure iText {@link PdfWriter} and null otherwise
 */
public interface IPdfWriterConfiguration
{

    /**
     * Configure the given {@link PdfWriter}
     * 
     * @param writer iText {@link PdfWriter} to configure.
     */
    void configure( PdfWriter writer );
}
