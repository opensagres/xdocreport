package fr.opensagres.xdocreport.remoting.resources.services;

import java.io.File;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.file.FileResourcesService;

public class MockResourcesService
    extends FileResourcesService
{

    public static final ResourcesService INSTANCE = new MockResourcesService();

    public MockResourcesService()
    {
        //super(new File("../../"));
        super( new File( "target/resources" ) );
    }

    public String getName()
    {
        return "Test-RepositoryService";
    }

}
