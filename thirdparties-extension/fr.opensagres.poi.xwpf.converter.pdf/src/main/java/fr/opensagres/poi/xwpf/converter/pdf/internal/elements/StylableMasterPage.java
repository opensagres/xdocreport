/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.poi.xwpf.converter.pdf.internal.elements;

import java.util.HashMap;
import java.util.Map;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;

import fr.opensagres.poi.xwpf.converter.core.IXWPFMasterPage;
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
