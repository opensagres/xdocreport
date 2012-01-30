/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com>, Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.opensagres.xdocreport.converter.internal.xslt;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

/**
 * Abstract {@link URIResolver} which check if {@link InputStream} return by
 * {@link AbstractURIResolver#getInputStream(String, String)}is not null.
 */
public abstract class AbstractURIResolver
    implements URIResolver
{

    public Source resolve( String href, String base )
        throws TransformerException
    {
        InputStream in = getInputStream( href, base );
        if ( in == null )
        {
            throw new TransformerException( "Resource does not exist. \"" + href + "\" is not accessible." );
        }
        return new StreamSource( in );
    }

    /**
     * Returns the input stream.
     * 
     * @param href
     * @param base
     * @return
     */
    protected abstract InputStream getInputStream( String href, String base );

}
