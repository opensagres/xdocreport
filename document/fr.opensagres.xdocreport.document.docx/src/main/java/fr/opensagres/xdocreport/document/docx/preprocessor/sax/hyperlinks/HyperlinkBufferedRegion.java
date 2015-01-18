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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.RBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedAttribute;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.ISavable;
import fr.opensagres.xdocreport.document.preprocessor.sax.ProcessRowResult;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * <pre>
 *  <w:hyperlink r:id="rId5" w:history="1">
 * 		
 * 		<w:r w:rsidRPr="000F2653">
 * 		 	<w:rPr>
 *   			<w:rStyle w:val="Lienhypertexte" /> 
 *   		</w:rPr>
 *   		<w:t>$</w:t> 
 *  	</w:r>
 *  	<w:proofErr w:type="spellStart" /> 
 *  	<w:r w:rsidRPr="000F2653">
 *  		<w:rPr>
 *   			<w:rStyle w:val="Lienhypertexte" /> 
 *   		</w:rPr>
 *   		<w:t>developers.Mail</w:t> 
 *  	</w:r>
 *  	<w:proofErr w:type="spellEnd" /> 
 *  </w:hyperlink>
 * </pre>
 */
public class HyperlinkBufferedRegion
    extends BufferedElement
{

    private final DocXBufferedDocumentContentHandler handler;

    private List<RBufferedRegion> rBufferedRegions = new ArrayList<RBufferedRegion>();

    private BufferedAttribute idAttribute;

    public HyperlinkBufferedRegion( DocXBufferedDocumentContentHandler handler, BufferedElement parent, String uri,
                                    String localName, String name, Attributes attributes )
    {
        super( parent, uri, localName, name, attributes );
        this.handler = handler;
    }

    @Override
    public void addRegion( ISavable region )
    {
        if ( region instanceof RBufferedRegion )
        {
            rBufferedRegions.add( (RBufferedRegion) region );
        }
        else
        {
            super.addRegion( region );
        }
    }

    public void process()
    {
        IDocumentFormatter formatter = handler.getFormatter();
        if ( formatter == null )
        {
            return;
        }
        // Concat all w:t text node
        String content = getAllTContent();
        ProcessRowResult result = handler.getProcessRowResult( content, false );
        String newContent = result.getContent();
        if ( newContent != null )
        {
            if ( result.getFieldName() != null )
            {
                // Hyperlink is in a Table and hyperlink is a list (see
                // FieldsMetadata), transform it.

                // 1) Modify w:t
                modifyTContents( newContent );
            }
            else
            {
                if ( formatter.containsInterpolation( newContent ) )
                {
                    // the new content contains fields which are interpolation
                    // (ex:${developer.name}

                    // 1) Modify w:t
                    modifyTContents( newContent );
                }
            }

            if ( handler.hasSharedContext() )
            {
                // There is shared context, search if it exists Map with initial
                // Hyperlink.
                InitialHyperlinkMap hyperlinksMap =
                    (InitialHyperlinkMap) HyperlinkUtils.getInitialHyperlinkMap( handler.getEntryName(),
                                                                                 handler.getSharedContext() );
                if ( hyperlinksMap != null )
                {
                    // Map with Initial Hyperlink exists.
                    String relationId = idAttribute.getValue();
                    // Search if the current hyperlink exists
                    HyperlinkInfo hyperlink = hyperlinksMap.get( relationId );
                    if ( hyperlink != null )
                    {
                        // Current hyperlink exsists
                        // Try to modify it to generate
                        // <w:hyperlink w:history="1"
                        // r:id="${___HyperlinkRegistry.registerHyperlink("rId5","mailto:${d.mail}","External")}">
                        String target = StringUtils.decode( hyperlink.getTarget() );
                        String targetMode = hyperlink.getTargetMode();
                        ProcessRowResult hyperlinkResult = handler.getProcessRowResult( target, false );
                        boolean dynamicHyperlink = false;
                        if ( hyperlinkResult != null && hyperlinkResult.getFieldName() != null )
                        {
                            target = hyperlinkResult.getContent();
                            dynamicHyperlink = true;
                        }
                        else if ( formatter.containsInterpolation( target ) )
                        {
                            dynamicHyperlink = true;
                        }

                        if ( dynamicHyperlink )
                        {
                            // Hyperlink is dynamic, modify the id attribute to
                            // generate
                            // <w:hyperlink w:history="1"
                            // r:id="${___HyperlinkRegistry.registerHyperlink("rId5","mailto:${d.mail}","External")}">
                            String id =
                                formatter.getFunctionDirective( HyperlinkUtils.getHyperlinkRegistryKey( handler.getEntryName() ),
                                                                "registerHyperlink", "\""
                                                                    + target + "\"", "\"" + targetMode + "\"" );
                            idAttribute.setValue( id );
                            hyperlinksMap.remove( hyperlink.getId() );
                            
                            if ( StringUtils.isNotEmpty( handler.getStartNoParse() ) )
                            {
                                this.setContentBeforeStartTagElement( handler.getEndNoParse() );
                                this.setContentAfterEndTagElement( handler.getStartNoParse() );
                            }
                        }
                    }
                }
            }
        }

    }

    private String getAllTContent()
    {
        StringBuilder t = new StringBuilder();
        for ( RBufferedRegion r : rBufferedRegions )
        {
            String c = r.getTContent();
            if ( c != null )
            {
                t.append( c );
            }
        }
        return t.toString();
    }

    private void modifyTContents( String newContent )
    {
        for ( int i = 0; i < rBufferedRegions.size(); i++ )
        {
            if ( i == rBufferedRegions.size() - 1 )
            {
                rBufferedRegions.get( i ).setTContent(0, newContent );
            }
            else
            {
                rBufferedRegions.get( i ).setTContent(0, "" );
            }
        }
    }

    public void setId( String name, String value )
    {
        if ( idAttribute == null )
        {
            idAttribute = super.setAttribute( name, value );
        }
        idAttribute.setValue( value );
    }

}
