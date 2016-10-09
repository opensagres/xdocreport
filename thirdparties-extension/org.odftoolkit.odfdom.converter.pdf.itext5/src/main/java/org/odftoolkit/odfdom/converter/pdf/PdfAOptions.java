package org.odftoolkit.odfdom.converter.pdf;

import com.itextpdf.text.pdf.PdfAWriter;

import fr.opensagres.xdocreport.itext.extension.IPdfAWriterConfiguration;

public class PdfAOptions
    extends PdfOptions
{

    private IPdfAWriterConfiguration pdfAconfiguration;

    protected PdfAOptions()
    {
        super();
    }

    /**
     * Create an instance of Pdf options.
     *
     * @return
     */
    public static PdfAOptions create()
    {
        return new PdfAOptions();
    }

    /**
     * Returns the configuration to use to configure iText {@link PdfAWriter} and null otherwise.
     *
     * @return
     */
    public IPdfAWriterConfiguration getPdfAConfiguration()
    {
        return pdfAconfiguration;
    }

    /**
     * Set the configuration to use to configure iText {@link PdfAWriter} and null otherwise.
     *
     * @param configuration
     *            the configuration to use to configure iText {@link PdfAWriter} and null otherwise
     */
    public void setPdfAConfiguration( IPdfAWriterConfiguration configuration )
    {
        this.pdfAconfiguration = configuration;
    }

}
