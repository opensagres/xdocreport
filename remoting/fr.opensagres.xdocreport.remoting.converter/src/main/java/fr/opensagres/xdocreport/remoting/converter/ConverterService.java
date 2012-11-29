package fr.opensagres.xdocreport.remoting.converter;

import javax.activation.DataSource;
import javax.ws.rs.core.Response;

/**
 * Document converter REST Web Service API.
 */
public interface ConverterService
{

    /**
     * Convert the given document (docx, odt) to the given format (PDF, XHTML).
     * 
     * @param document the data source stream of the document to convert.
     * @param outputFormat the output format.
     * @param via used to select the well converter (docx4j, XDOcReport etc)
     * @param download true if converted document must be download and false otherwise.
     * @return
     */
    Response convert( final DataSource document, String outputFormat, final String via, boolean download );

}