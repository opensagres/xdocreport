package fr.opensagres.xdocreport.document.tools.repository;

import java.io.File;

import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;
import fr.opensagres.xdocreport.remoting.repository.services.file.FileRepositoryService;

public class MockRepositoryService
    extends FileRepositoryService
{

    public static final IRepositoryService INSTANCE = new MockRepositoryService();

    public MockRepositoryService()
    {
        super(new File("src/test/resources/fr/opensagres/xdocreport/document/tools/repository"));
    }

    public String getName()
    {
        return "Test-Tools-RepositoryService";
    }

}
