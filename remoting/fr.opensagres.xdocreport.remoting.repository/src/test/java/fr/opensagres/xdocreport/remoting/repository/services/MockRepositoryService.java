package fr.opensagres.xdocreport.remoting.repository.services;

import java.io.File;

import fr.opensagres.xdocreport.remoting.repository.services.file.FileRepositoryService;

public class MockRepositoryService
    extends FileRepositoryService
{

    public static final IRepositoryService INSTANCE = new MockRepositoryService();

    public MockRepositoryService()
    {
        //super(new File("../../"));
        super( new File( "src/test/resources/fr/opensagres/xdocreport/remoting/repository" ) );
    }

    public String getName()
    {
        return "Test-RepositoryService";
    }

}
