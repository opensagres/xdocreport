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
package fr.opensagres.xdocreport.remoting.resources.domain;

/**
 * Factory to create Resource.
 */
public class ResourceFactory
{

    public static final String META_INF = "META-INF";

    public static final String FIELDS_XML = ".fields.xml";

    /**
     * Create generic resource.
     * 
     * @param name of the resource to create.
     * @param type of the resource to create.
     * @param parent resource of the resource to create.
     * @return
     */
    public static Resource createResource( String name, ResourceType type, Resource parent )
    {
        Resource resource = new Resource();
        resource.setName( name );
        resource.setType( type );
        if ( parent != null )
        {
            parent.getChildren().add( resource );
        }
        return resource;
    }

    /**
     * Create category resource.
     * 
     * @param name of the resource to create.
     * @param parent resource of the resource to create.
     * @return
     */
    public static Resource createCategory( String name, Resource parent )
    {
        return createResource( name, ResourceType.CATEGORY, parent );
    }

    /**
     * Create document resource.
     * 
     * @param name of the resource to create.
     * @param parent resource of the resource to create.
     * @return
     */
    public static Resource createDocument( String name, Resource parent )
    {
        return createResource( name, ResourceType.DOCUMENT, parent );
    }

    /**
     * Create template resource.
     * 
     * @param name of the resource to create.
     * @param parent resource of the resource to create.
     * @return
     */
    public static Resource createTemplate( String name, Resource parent )
    {
        String templateName = name;
        int index = name.lastIndexOf( '.' );
        if ( index != -1 )
        {
            templateName = templateName.substring( 0, index );
        }
        Resource template = createResource( templateName, ResourceType.TEMPLATE, parent );
        // Add document (docx, odt...)
        createDocument( name, template );
        // Add META-INF folder
        Resource metaInf = createCategory( META_INF, template );
        // Add *fields.xml
        StringBuilder fieldsMetadata = new StringBuilder( templateName );
        fieldsMetadata.append( FIELDS_XML );
        createDocument( fieldsMetadata.toString(), metaInf );

        return template;
    }
}
