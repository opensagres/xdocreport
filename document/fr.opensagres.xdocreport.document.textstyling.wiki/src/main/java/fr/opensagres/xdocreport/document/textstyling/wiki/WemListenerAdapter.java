/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package fr.opensagres.xdocreport.document.textstyling.wiki;

import java.io.IOException;
import java.util.List;

import org.wikimodel.wem.EmptyWemListener;
import org.wikimodel.wem.IWemConstants;
import org.wikimodel.wem.WikiFormat;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiStyle;

import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;

/**
 * Wiki Event Model Adaptor to call methods of {@link IDocumentHandler}.
 */
public class WemListenerAdapter
    extends EmptyWemListener
{

    protected final IDocumentHandler documentHandler;

    public WemListenerAdapter( IDocumentHandler visitor )
    {
        this.documentHandler = visitor;
    }

    @Override
    public void beginDocument()
    {
        try
        {
            documentHandler.startDocument();
        }
        catch ( IOException e )
        {
            // Do nothing
        }
    }

    @Override
    public void endDocument()
    {
        try
        {
            documentHandler.endDocument();
        }
        catch ( IOException e )
        {
            // Do nothing
        }
    }

    @Override
    public void beginFormat( WikiFormat format )
    {
        List<WikiStyle> styles = format.getStyles();
        for ( WikiStyle style : styles )
        {
            try
            {
                if ( IWemConstants.STRONG.equals( style ) )
                {
                    documentHandler.startBold();
                }
                else if ( IWemConstants.EM.equals( style ) )
                {
                    documentHandler.startItalics();
                }
            }
            catch ( IOException e )
            {
                // Do nothing
            }
        }
    }

    @Override
    public void endFormat( WikiFormat format )
    {
        List<WikiStyle> styles = format.getStyles();
        for ( WikiStyle style : styles )
        {
            try
            {
                if ( IWemConstants.STRONG.equals( style ) )
                {
                    documentHandler.endBold();
                }
                else if ( IWemConstants.EM.equals( style ) )
                {
                    documentHandler.endItalics();
                }
            }
            catch ( IOException e )
            {
                // Do nothing
            }
        }
    }

    @Override
    public void beginList( WikiParameters params, boolean ordered )
    {
        try
        {
            if ( ordered )
            {
                documentHandler.startOrderedList();
            }
            else
            {
                documentHandler.startUnorderedList();
            }
        }
        catch ( IOException e )
        {
            // Do nothing
        }
    }

    @Override
    public void beginListItem()
    {
        try
        {
            documentHandler.startListItem();
        }
        catch ( IOException e )
        {
            // Do nothing
        }
    }

    @Override
    public void endListItem()
    {
        try
        {
            documentHandler.endListItem();
        }
        catch ( IOException e )
        {
            // Do nothing
        }
    }

    @Override
    public void endList( WikiParameters params, boolean ordered )
    {
        try
        {
            if ( ordered )
            {
                documentHandler.endOrderedList();
            }
            else
            {
                documentHandler.endUnorderedList();
            }
        }
        catch ( IOException e )
        {
            // Do nothing
        }
    }

    @Override
    public void onSpace( String str )
    {
        try
        {
            documentHandler.handleString( str );
        }
        catch ( IOException e )
        {
            // Do nothing
        }
    }

    @Override
    public void onWord( String str )
    {
        try
        {
            documentHandler.handleString( str );
        }
        catch ( IOException e )
        {
            // Do nothing
        }
    }

    @Override
    public void beginParagraph( WikiParameters params )
    {
        try
        {
            documentHandler.startParagraph();
        }
        catch ( IOException e )
        {
            // Do nothing
        }
    }

    @Override
    public void endParagraph( WikiParameters params )
    {
        try
        {
            documentHandler.endParagraph();
        }
        catch ( IOException e )
        {
            // Do nothing
        }
    }

}
