package fr.opensagres.xdocreport.itext.extension;

import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfDocument;

public class ExtendedPdfAWriter extends PdfAWriter {

    protected ExtendedPdfAWriter(PdfDocument document, OutputStream os, PdfAConformanceLevel conformanceLevel) {
        super(document, os, conformanceLevel);
        pdf = document;
        directContent = new ExtendedPdfContentByte(this);
        directContentUnder = new ExtendedPdfContentByte(this);
    }

    public static ExtendedPdfAWriter getInstance(Document document, OutputStream os, PdfAConformanceLevel conformanceLevel) throws DocumentException {
        PdfDocument pdf = new PdfDocument();
        document.addDocListener(pdf);
        ExtendedPdfAWriter writer = new ExtendedPdfAWriter(pdf, os, conformanceLevel);
        pdf.addWriter(writer);
        return writer;
    }

}
