package old;

import java.util.List;

import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.AbstractResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

public class XDocReportRepositoryService
    extends AbstractResourcesService
{

    private final String METADATA_KEY = XDocReportRepositoryService.class.getName();

    private static final ResourcesService INSTANCE = new XDocReportRepositoryService();

    private final XDocReportRegistry registry;

    public static ResourcesService getDefault()
    {
        return INSTANCE;
    }

    public XDocReportRepositoryService( XDocReportRegistry registry )
    {
        this.registry = registry;
    }

    public XDocReportRepositoryService()
    {
        this( XDocReportRegistry.getRegistry() );
    }

    public String getName()
    {
        return "XDocReport";
    }
    
//    private ResourceMetadata getMetadata( IXDocReport report )
//    {
//        if ( report == null )
//        {
//            // TODO throw exception
//            return null;
//        }
//        ResourceMetadata metadata = report.getData( METADATA_KEY );
//        if ( metadata == null )
//        {
//            metadata = new ResourceMetadata();
//            metadata.setId( report.getId() );
//            report.setData( METADATA_KEY, metadata );
//        }
//        return metadata;
//    }
//
//    private IXDocReport getReport( String resourceId )
//    {
//        return registry.getReport( resourceId );
//    }
//
//    public ResourceContent download( String resourceId, Filter filter )
//    {
//        // TODO manage filter
//        ResourceContent content = null;
//        IXDocReport report = getReport( resourceId );
//        if ( report != null )
//        {
//            content = new ResourceContent();
//            content.setId( report.getId() );
//
//            XDocArchive archive = null;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//            archive = report.getOriginalDocumentArchive();
//
//            try
//            {
//                XDocArchive.writeZip( archive, out );
//            }
//            catch ( IOException e )
//            {
//                e.printStackTrace();
//            }
//
//            content.setContent( out.toByteArray() );
//        }
//        return content;
//    }
//
//    public List<ResourceMetadata> getMetadatas( Filter filter )
//    {
//        ResourceMetadata metadata = null;
//        List<ResourceMetadata> metadatas = new ArrayList<ResourceMetadata>();
//        Collection<IXDocReport> reports = registry.getCachedReports();
//        for ( IXDocReport report : reports )
//        {
//            metadata = getMetadata( report.getId(), filter );
//            if ( metadata != null )
//            {
//                metadatas.add( metadata );
//            }
//        }
//        return metadatas;
//    }
//
//    public ResourceMetadata getMetadata( String resourceId, Filter filter )
//    {
//        // TODO: manage filter
//        IXDocReport report = getReport( resourceId );
//        return getMetadata( report );
//    }
//
//    public void upload( ResourceContent content )
//    {
//        String templateEngineKind = "Velocity";
//        String reportId = content.getId();
//        InputStream sourceStream;
//        try
//        {
//            sourceStream = getInputStream( content.getContent() );
//
//            IXDocReport report = registry.loadReport( sourceStream, reportId, templateEngineKind );
//            report.setCacheOriginalDocument( true );
//        }
//        catch ( Exception e )
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }

    public Resource getRoot( Filter filter )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public List<byte[]> download( List<String> resourcePaths )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public byte[] download( String resourcePath )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void upload( String resourcePath, byte[] content )
    {
        // TODO Auto-generated method stub
        
    }

}
