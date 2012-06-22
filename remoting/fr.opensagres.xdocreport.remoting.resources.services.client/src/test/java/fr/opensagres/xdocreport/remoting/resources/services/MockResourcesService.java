package fr.opensagres.xdocreport.remoting.resources.services;

import java.io.File;

import fr.opensagres.xdocreport.remoting.resources.services.server.file.FileResourcesService;

public class MockResourcesService
    extends FileResourcesService
{

    //public static final ResourcesService INSTANCE = new MockResourcesService();

    public MockResourcesService(String resourcesDir)
    {
        //super(new File("../../"));
        super( new File( "target/" + resourcesDir ) );
    }

    public String getName()
    {
        return "Test-RepositoryService";
    }

}
