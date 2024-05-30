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
package fr.opensagres.xdocreport.remoting.resources.services.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.LargeBinaryData;

/**
 * {@link MessageBodyReader} used by JAXRS to read the {@link BinaryData} from an Http request
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 */
@Provider
public class LargeBinaryDataMessageBodyReader
    implements MessageBodyReader<LargeBinaryData>
{

    public boolean isReadable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        return LargeBinaryData.class.isAssignableFrom( type );
    }

    public LargeBinaryData readFrom( Class<LargeBinaryData> type, Type genericType, Annotation[] annotations,
                                MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                                InputStream entityStream )
        throws IOException, WebApplicationException
    {

        String filename = "";
        String cd = httpHeaders.getFirst( "Content-Disposition" );
        if ( cd != null )
        {
            filename = cd.replace( "attachement;filename=", "" );
        }

        String mimetype = httpHeaders.getFirst( "Content-Type" );

        String resourceId = httpHeaders.getFirst( "X-resourceId" );


        LargeBinaryData data = new LargeBinaryData( );
        data.setContent(entityStream);
        data.setFileName(filename);
        data.setMimeType(mimetype);
        data.setResourceId( resourceId );

        return data;
    }
}
