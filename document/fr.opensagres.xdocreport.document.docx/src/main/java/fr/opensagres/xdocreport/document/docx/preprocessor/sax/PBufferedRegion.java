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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.ISavable;

/**
 * <pre>
 * <w:p w:rsidR="007B3A53" w:rsidRDefault="00F95F02">
 *  <w:pPr>
 *  <w:rPr>
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   </w:pPr>
 *  <w:r w:rsidRPr="00CE3A74">
 *  <w:rPr>
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   <w:fldChar w:fldCharType="begin" /> 
 *   </w:r>
 *  <w:r w:rsidR="000050F2" w:rsidRPr="00CE3A74">
 *  <w:rPr>
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   <w:instrText xml:space="preserve">MERGEFIELD Titre</w:instrText> 
 *   </w:r>
 *  <w:r w:rsidRPr="00CE3A74">
 *  <w:rPr>
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   <w:fldChar w:fldCharType="separate" /> 
 *   </w:r>
 *  <w:r w:rsidR="006716CB">
 *  <w:rPr>
 *   <w:noProof /> 
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   <w:t>�Titre�</w:t> 
 *   </w:r>
 *  <w:r w:rsidRPr="00CE3A74">
 *  <w:rPr>
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   <w:fldChar w:fldCharType="end" /> 
 *   </w:r>
 *   </w:p>
 * 
 * </pre>
 */
public class PBufferedRegion
    extends BufferedElement
{

    private static final String BEGIN = "begin";

    private static final String SEPARATE = "separate";

    private static final String END = "end";

    private List<RBufferedRegion> rBufferedRegions = new ArrayList<RBufferedRegion>();

    private final DocxBufferedDocument document;

    private boolean containsField;

    public PBufferedRegion( DocxBufferedDocument document, BufferedElement parent, String uri, String localName,
                            String name, Attributes attributes )
    {
        super( parent, uri, localName, name, attributes );
        this.document = document;
        this.containsField = false;
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
        Collection<BufferedElement> toRemove = new ArrayList<BufferedElement>();
        boolean remove = false;
        boolean fieldNameSetted = false;
        String fieldName = null;
        boolean rReseted = false;
        for ( int i = 0; i < rBufferedRegions.size(); i++ )
        {
            RBufferedRegion rBufferedRegion = rBufferedRegions.get( i );
            if ( BEGIN.equals( rBufferedRegion.getFldCharType() ) )
            {
                int nextIndexAfterBegin = i + 1;
                RBufferedRegion nextR = rBufferedRegions.get( nextIndexAfterBegin );
                // Remove the begin w:r Next w:r
                // 1) has fieldName : <w:r w:rsidR="000050F2"
                // w:rsidRPr="00CE3A74"><w:rPr><w:color w:val="FF0000"
                // /></w:rPr><w:instrText xml:space="preserve">MERGEFIELD
                // Titre</w:instrText>
                // 2) was reseted (if @before-row, @after-row ... if the
                // MERGEFIELD name starts with thoses tokens.
                rReseted = nextR.isReseted();
                if ( rReseted )
                {
                    toRemove.add( rBufferedRegion );
                    toRemove.add( nextR );
                    remove = true;
                }
                else
                {
                    // Test if there are several <w:instrText which splits the
                    // content
                    // ex: <w:r>
                    // <w:instrText xml:space="preserve"> MERGEFIELD
                    // ${ctx.serviceclientdeparture} \* M</w:instrText>
                    // </w:r>
                    // <w:r>
                    // <w:instrText xml:space="preserve">ERGEFORMAT
                    // </w:instrText>
                    // </w:r>
                    List<RBufferedRegion> rMerged = new ArrayList<RBufferedRegion>();
                    StringBuilder mergedInstrText = new StringBuilder();
                    for ( int j = nextIndexAfterBegin; j < rBufferedRegions.size(); j++ )
                    {
                        RBufferedRegion r = rBufferedRegions.get( j );
                        if ( r.hasInstrText() )
                        {
                            rMerged.add( r );
                            mergedInstrText.append( r.getOriginalInstrText() );
                        }
                        else
                        {
                            break;
                        }
                    }
                    if ( rMerged.size() > 0 )
                    {
                        RBufferedRegion firstR = rMerged.get( 0 );
                        firstR.setInstrText( mergedInstrText.toString(), firstR.getFieldAsTextStyling() );
                        fieldName = firstR.getFieldName();
                        if ( fieldName != null )
                        {

                            if ( document.processScriptBeforeAfter( firstR ) )
                            {
                                rReseted = true;
                            }

                            for ( RBufferedRegion r : rMerged )
                            {
                                toRemove.add( r );
                                i++;
                            }
                            toRemove.add( rBufferedRegion );
                            remove = true;
                        }
                    }
                }
            }
            else if ( SEPARATE.equals( rBufferedRegion.getFldCharType() ) && remove )
            {
                // begin w:r was removed, remove the separate w:r.
                toRemove.add( rBufferedRegion );
            }
            else if ( END.equals( rBufferedRegion.getFldCharType() ) && remove )
            {
                // begin w:r was removed, remove the end w:r.
                toRemove.add( rBufferedRegion );
                remove = false;
                fieldName = null;
                fieldNameSetted = false;
                rReseted = false;
            }
            else if ( fieldName != null || rReseted )
            {
                // other w:r
                if ( !fieldNameSetted && !rReseted )
                {
                    // fieldName was not setted and w:r was not rested, modify
                    // the t content with fieldName
                    rBufferedRegion.setTContent(0, fieldName );
                    fieldNameSetted = true;
                    containsField = true;
                }
                else
                {
                    // remove
                    if (!rBufferedRegion.isContainsNote()) {
                        toRemove.add( rBufferedRegion );
                    }                    
                }
            }
        }
        rBufferedRegions.removeAll( toRemove );
        super.removeAll( toRemove );
    }

    public boolean isContainsField()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
