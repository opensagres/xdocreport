package fr.opensagres.xdocreport.remoting.resources.domain;

/**
 * Factory to create Resource.
 */
public class ResourceFactory
{

    private static final String META_INF = "META-INF";

    private static final String FIELDS_XML = ".fields.xml";

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
        return createResource( name, ResourceType.FOLDER, parent );
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
        return createResource( name, ResourceType.FILE, parent );
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
