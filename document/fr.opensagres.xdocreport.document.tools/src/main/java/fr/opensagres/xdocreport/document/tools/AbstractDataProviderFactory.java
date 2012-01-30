package fr.opensagres.xdocreport.document.tools;

public abstract class AbstractDataProviderFactory
    implements IDataProviderFactory
{

    private final String id;

    private final String description;

    public AbstractDataProviderFactory( String id, String description )
    {
        this.id = id;
        this.description = description;
    }

    public String getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }
}
