package fr.opensagres.xdocreport.remoting.resources.services.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;

/**
 * 
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 * 
 */
public class BinaryDataMessageBodyWriter implements
        MessageBodyWriter<BinaryData> {

    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return BinaryData.class.isAssignableFrom(type);
    }

    public long getSize(BinaryData t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    public void writeTo(BinaryData t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException,
            WebApplicationException {

        // here we should access a Stream from the BinaryData
        // otherwise we still load everything in JVM Memory
        ByteArrayInputStream in = new ByteArrayInputStream(t.getContent());

        // XXX add http headers in the response
        // filename
        // mimetype

        // manual copy
        byte[] buffer = new byte[65536];
        int read;
        while ((read = in.read(buffer)) != -1) {
            entityStream.write(buffer, 0, read);
        }
        in.close();
    }

}
