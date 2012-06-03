/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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

package fr.opensagres.xdocreport.remoting.resources.services.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;

/**
 * {@link MessageBodyWriter} that streams an {@link BinaryData} object in an Http response.
 * <p>
 * To allow streaming the binday data is directly sent inside the Http body and the other attributes are passed as http
 * header (it avoids to use MultiPart encoding)
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 */
@Provider
public class BinaryDataMessageBodyWriter
    implements MessageBodyWriter<BinaryData>
{

    public boolean isWriteable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        return BinaryData.class.isAssignableFrom( type );
    }

    public long getSize( BinaryData t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        return t.getLength();
    }

    public void writeTo( BinaryData t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                         MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream )
        throws IOException, WebApplicationException
    {

        InputStream content = t.getContent();

        httpHeaders.add( "Content-Disposition", "attachement;filename=" + t.getFileName() );
        httpHeaders.add( "Content-Type", t.getMimeType() );
        httpHeaders.add( "X-resourceId", t.getResourceId() );
        //IOUtils.write(content, entityStream);
        IOUtils.copyLarge(content, entityStream);

    }

}
