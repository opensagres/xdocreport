package fr.opensagres.xdocreport.remoting.resources.services;

import java.io.File;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.file.FileRepositoryService;

public class MockResourcesService
    extends FileRepositoryService
{

    public static final ResourcesService INSTANCE = new MockResourcesService();

    public MockResourcesService()
    {
        //super(new File("../../"));
        super( new File( "src/test/resources/fr/opensagres/xdocreport/remoting/resources" ) );
    }

    public String getName()
    {
        return "Test-RepositoryService";
    }

}
