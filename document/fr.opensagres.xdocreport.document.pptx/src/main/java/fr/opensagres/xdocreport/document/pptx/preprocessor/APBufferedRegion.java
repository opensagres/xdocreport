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
package fr.opensagres.xdocreport.document.pptx.preprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.ISavable;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class APBufferedRegion
    extends BufferedElement
{

    private final PPTXSlideDocument document;

    private final List<ARBufferedRegion> arBufferedRegions;

    private Integer level;

    private String itemNameList;

    private List<String> ignoreStartLoopDirective;

    public APBufferedRegion( PPTXSlideDocument document, BufferedElement parent, String uri, String localName,
                             String name, Attributes attributes )
    {
        super( parent, uri, localName, name, attributes );
        this.document = document;
        this.arBufferedRegions = new ArrayList<ARBufferedRegion>();
        this.ignoreStartLoopDirective = null;
    }

    @Override
    public void addRegion( ISavable region )
    {
        if ( region instanceof ARBufferedRegion )
        {
            arBufferedRegions.add( (ARBufferedRegion) region );
        }
        else
        {
            super.addRegion( region );
        }
    }

    public void process()
    {
        Collection<BufferedElement> toRemove = new ArrayList<BufferedElement>();
        int size = arBufferedRegions.size();
        String s = null;
        StringBuilder fullContent = new StringBuilder();
        ARBufferedRegion currentAR = null;
        ARBufferedRegion lastAR = null;
        boolean hasField = false;
        boolean fieldParsing = false;
        for ( int i = 0; i < size; i++ )
        {
            currentAR = arBufferedRegions.get( i );
            s = currentAR.getTContent();
            hasField = s != null && s.indexOf( "$" ) != -1;
            if ( fieldParsing )
            {
                // Field is parsing
                fieldParsing = ( s == null || s.length() == 0 || Character.isWhitespace( s.charAt( 0 ) ) );
                if ( !fieldParsing )
                {
                    if ( hasField )
                    {
                        update( toRemove, fullContent, lastAR );
                        fieldParsing = true;
                        fullContent.append( s );
                        toRemove.add( currentAR );

                    }
                    else
                    {
                        fullContent.append( s );
                        update( toRemove, fullContent, currentAR );
                    }
                }
                else
                {
                    fullContent.append( s );
                    toRemove.add( currentAR );
                }

            }
            else
            {
                if ( hasField )
                {
                    fieldParsing = true;
                    fullContent.append( s );
                    toRemove.add( currentAR );
                }
                else
                {
                    // Do nothing
                }
            }
            lastAR = currentAR;
        }
        update( toRemove, fullContent, lastAR );
        super.removeAll( toRemove );

    }

    private void update( Collection<BufferedElement> toRemove, StringBuilder fullContent, ARBufferedRegion lastAR )
    {
        if ( fullContent.length() > 0 )
        {
            String content = fullContent.toString();
            String itemNameList = getItemNameList( content );
            lastAR.setTContent( itemNameList != null ? itemNameList : content );
            fullContent.setLength( 0 );
            toRemove.remove( lastAR );
        }

    }

    private String getItemNameList( String content )
    {
        IDocumentFormatter formatter = document.getFormatter();
        FieldsMetadata fieldsMetadata = document.getFieldsMetadata();
        if ( formatter != null && fieldsMetadata != null )
        {

            Collection<String> fieldsAsList = fieldsMetadata.getFieldsAsList();
            for ( final String fieldName : fieldsAsList )
            {
                if ( content.contains( fieldName ) )
                {
                    this.itemNameList = formatter.extractItemNameList( content, fieldName, true );
                    if ( StringUtils.isNotEmpty( itemNameList ) )
                    {
                        if ( !isIgnoreStartLoopDirective( itemNameList ) )
                        {
                            setStartLoopDirective( formatter.getStartLoopDirective( itemNameList ) );
                            addIgnoreStartLoopDirective( itemNameList );
                        }
                        return formatter.formatAsFieldItemList( content, fieldName, true );
                    }
                }
            }
        }
        return null;
    }

    public void setEndLoopDirective( String itemNameList )
    {
        IDocumentFormatter formatter = document.getFormatter();
        this.endTagElement.setAfter( formatter.getEndLoopDirective( itemNameList ) );
    }

    private void setStartLoopDirective( String startLoopDirective )
    {
        this.startTagElement.setBefore( startLoopDirective );
    }

    public String getItemNameList()
    {
        return itemNameList;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel( Integer level )
    {
        this.level = level;
    }

    public void addIgnoreStartLoopDirective( String itemNameList )
    {
        if ( ignoreStartLoopDirective == null )
        {
            ignoreStartLoopDirective = new ArrayList<String>();

        }
        ignoreStartLoopDirective.add( itemNameList );
    }

    public boolean isIgnoreStartLoopDirective( String itemNameList )
    {
        if ( ignoreStartLoopDirective == null )
        {
            return false;
        }
        return ignoreStartLoopDirective.contains( itemNameList );
    }

}
