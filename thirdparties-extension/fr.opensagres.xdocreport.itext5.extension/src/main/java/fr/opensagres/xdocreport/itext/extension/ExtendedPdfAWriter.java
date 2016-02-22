package fr.opensagres.xdocreport.itext.extension;

import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfDocument;

public class ExtendedPdfAWriter
    extends PdfAWriter {

    private final IPdfAWriterConfiguration configuration;

    protected ExtendedPdfAWriter( PdfDocument document, OutputStream os, PdfAConformanceLevel conformanceLevel, IPdfAWriterConfiguration configuration )
    {
        super( document, os, conformanceLevel );
        pdf = document;
        this.configuration = configuration;
        directContent = new ExtendedPdfContentByte( this );
        directContentUnder = new ExtendedPdfContentByte( this );
    }

    public static ExtendedPdfAWriter getInstance( Document document, OutputStream os, IPdfAWriterConfiguration configuration )
            throws DocumentException
    {
        PdfDocument pdf = new PdfDocument();
        document.addDocListener( pdf );

        PdfAConformanceLevel conformanceLevel = null;
        if ( configuration != null ) {
            conformanceLevel = configuration.getConformanceLevel();
        }

        ExtendedPdfAWriter writer = new ExtendedPdfAWriter( pdf, os, conformanceLevel, configuration );
        pdf.addWriter( writer );
        return writer;
    }

    @Override
    public void open() {
        super.open();

        if ( configuration != null ) {
            configuration.configureOutputIntents(this);
        }
    }

}
