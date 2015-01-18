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
package fr.opensagres.xdocreport.document.pptx.preprocessor.txbody;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.pptx.preprocessor.APBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.ISavable;

/**
 * <p:txBody>
 */
public class TxBodyBufferedRegion
    extends BufferedElement
{

    private List<APBufferedRegion> apBufferedRegions = new ArrayList<APBufferedRegion>();

    private List<String> ignoreEndLoopDirective;

    public TxBodyBufferedRegion( BufferedElement parent, String uri, String localName, String name,
                                  Attributes attributes )
    {
        super( parent, uri, localName, name, attributes );
    }

    @Override
    public void addRegion( ISavable region )
    {
        if ( region instanceof APBufferedRegion )
        {
            apBufferedRegions.add( (APBufferedRegion) region );
        }
        else
        {
            super.addRegion( region );
        }
    }

    public void process()
    {
        APBufferedRegion p = null;
        String itemNameList = null;
        int size = apBufferedRegions.size();
        Integer level = null;
        for ( int i = 0; i < size; i++ )
        {
            p = apBufferedRegions.get( i );
            p.process();

            itemNameList = p.getItemNameList();
            if ( StringUtils.isNotEmpty( itemNameList ) )
            {
                level = p.getLevel();
                APBufferedRegion nextP = null;
                if ( i < size )
                {
                    // Get next p
                    nextP = getNextPInferiorToLevel( i + 1, level );
                }
                if ( nextP != null )
                {
                    nextP.addIgnoreStartLoopDirective( itemNameList );
                    if ( !isIgnoreEndLoopDirective( itemNameList ) )
                    {
                        nextP.setEndLoopDirective( itemNameList );
                        addIgnoreEndLoopDirective( itemNameList );
                    }
                }
                else
                {
                    if ( !isIgnoreEndLoopDirective( itemNameList ) )
                    {
                        p.setEndLoopDirective( itemNameList );
                        addIgnoreEndLoopDirective( itemNameList );
                    }
                }
            }

        }
    }

    private APBufferedRegion getNextPInferiorToLevel( int start, Integer level )
    {
        APBufferedRegion p = null;
        APBufferedRegion lastP = null;
        for ( int i = start; i < apBufferedRegions.size(); i++ )
        {
            p = apBufferedRegions.get( i );
            if ( p.getLevel() == null )
            {
                return lastP;
            }
            if ( level != null )
            {
                if ( p.getLevel() >= level )
                {
                    return lastP;
                }
            }
            lastP = p;
        }
        return lastP;
    }

    public void addIgnoreEndLoopDirective( String itemNameList )
    {
        if ( ignoreEndLoopDirective == null )
        {
            ignoreEndLoopDirective = new ArrayList<String>();

        }
        ignoreEndLoopDirective.add( itemNameList );
    }

    public boolean isIgnoreEndLoopDirective( String itemNameList )
    {
        if ( ignoreEndLoopDirective == null )
        {
            return false;
        }
        return ignoreEndLoopDirective.contains( itemNameList );
    }
}
