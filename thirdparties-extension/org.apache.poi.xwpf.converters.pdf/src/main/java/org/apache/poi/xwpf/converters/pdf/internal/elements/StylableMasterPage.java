package org.apache.poi.xwpf.converters.pdf.internal.elements;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xwpf.converters.core.IXWPFMasterPage;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;

import fr.opensagres.xdocreport.itext.extension.IMasterPage;
import fr.opensagres.xdocreport.itext.extension.IMasterPageHeaderFooter;
import fr.opensagres.xdocreport.itext.extension.MasterPage;

public class StylableMasterPage
    extends MasterPage
    implements IMasterPage, IXWPFMasterPage<IMasterPageHeaderFooter>
{

    private final CTSectPr sectPr;

    private int type;

    private final Map<Integer, IMasterPageHeaderFooter> headers;

    private final Map<Integer, IMasterPageHeaderFooter> footers;

    private IMasterPageHeaderFooter currentHeader;

    private IMasterPageHeaderFooter currentFooter;

    public StylableMasterPage( CTSectPr sectPr )
    {
        super( null );
        this.sectPr = sectPr;
        this.headers = new HashMap<Integer, IMasterPageHeaderFooter>();
        this.footers = new HashMap<Integer, IMasterPageHeaderFooter>();
    }

    public CTSectPr getSectPr()
    {
        return sectPr;
    }

    public boolean setType( int type )
    {
        this.type = type;
        boolean h = updateHeader( type );
        boolean f = updateFooter( type );
        return h || f;

    }

    private boolean updateHeader( int type )
    {
        IMasterPageHeaderFooter oldHeader = currentHeader;
        currentHeader = headers.get( type );
        if ( currentHeader == null )
        {
            currentHeader = headers.get( STHdrFtr.INT_DEFAULT );
        }
        if ( currentHeader == null )
        {
            return oldHeader != null;
        }
        return !currentHeader.equals( oldHeader );
    }

    private boolean updateFooter( int type )
    {
        IMasterPageHeaderFooter oldFooter = currentFooter;
        currentFooter = footers.get( type );
        if ( currentFooter == null )
        {
            currentFooter = footers.get( STHdrFtr.INT_DEFAULT );
        }
        if ( currentFooter == null )
        {
            return oldFooter != null;
        }
        return !currentFooter.equals( oldFooter );
    }

    public int getType()
    {
        return type;
    }

    @Override
    public void setHeader( IMasterPageHeaderFooter header )
    {
        header.flush();
        if ( !header.isEmpty() )
        {
            headers.put( type, header );
        }
    }

    @Override
    public void setFooter( IMasterPageHeaderFooter footer )
    {
        footer.flush();
        if ( !footer.isEmpty() )
        {
            footers.put( type, footer );
        }
    }

    @Override
    public IMasterPageHeaderFooter getHeader()
    {
        return currentHeader;
    }

    @Override
    public IMasterPageHeaderFooter getFooter()
    {
        return currentFooter;
    }
}
