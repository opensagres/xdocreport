package fr.opensagres.xdocreport.document.tools.remoting.resources;

import java.io.File;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.file.FileRepositoryService;

public class MockRepositoryService
    extends FileRepositoryService
{

    public static final ResourcesService INSTANCE = new MockRepositoryService();

    public MockRepositoryService()
    {
        super(new File("src/test/resources/fr/opensagres/xdocreport/document/tools/remoting/resources"));
    }

    public String getName()
    {
        return "Test-Tools-RepositoryService";
    }

}
