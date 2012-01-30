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
package fr.opensagres.xdocreport.template.formatter;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.discovery.IBaseDiscovery;

/**
 * Fields metadata serializer used to load {@link FieldsMetadata} from Java {@link Class} model.
 */
public interface IFieldsMetadataClassSerializer
    extends IBaseDiscovery
{

    /**
     * Load simple fields metadata in the given fieldsMetadata by using the given key and Java Class.
     * 
     * @param fieldsMetadata the fieldsMetadata where fields metadata must be added.
     * @param key the key (first token) to use to generate field name.
     * @param clazz the Java class model to use to load fields metadata.
     */
    void load( FieldsMetadata fieldsMetadata, String key, Class<?> clazz )
        throws XDocReportException;

    /**
     * Load simple/list fields metadata in the given fieldsMetadata by using the given key and Java Class.
     * 
     * @param fieldsMetadata the fieldsMetadata where fields metadata must be added.
     * @param key the key (first token) to use to generate field name.
     * @param clazz the Java class model to use to load fields metadata.
     * @param listType true if it's a list and false otherwise.
     */
    void load( FieldsMetadata fieldsMetadata, String key, Class<?> clazz, boolean listType )
        throws XDocReportException;
}
