package fr.opensagres.xdocreport.document.tools.remoting.resources;

import java.io.File;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.file.FileResourcesService;

public class MockResourcesService
    extends FileResourcesService
{

    public MockResourcesService()
    {
        super( new File( "target/resources" ) );
    }

    public String getName()
    {
        return "Test-Tools-RepositoryService";
    }

}
