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

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;

public class RBufferedRegion
    extends MergefieldBufferedRegion
{

    private String fldCharType;

    private String originalInstrText;

    private FieldMetadata fieldAsTextStyling;

    private boolean containsNote;

    public RBufferedRegion( TransformedBufferedDocumentContentHandler handler, BufferedElement parent, String uri,
                            String localName, String name, Attributes attributes )
    {
        super( handler, parent, uri, localName, name, attributes );
        this.originalInstrText = null;
        this.containsNote = false;
    }

    public void setFldCharType( String fldCharType )
    {
        this.fldCharType = fldCharType;
    }

    public String getFldCharType()
    {
        return fldCharType;
    }

    public void setTContent(int index, String tContent )
    {
        // w:t could not exists in some case, NPE test must be done. 
        // See https://github.com/opensagres/xdocreport/issues/36
        BufferedElement wt = getTRegion(index);
        if (wt != null) {
            wt.setTextContent( tContent );
        }
    }

    @Override
    public String setInstrText( String instrText, FieldMetadata fieldAsTextStyling )
    {
        boolean instrTextChanged = instrText != null && !instrText.equals( originalInstrText );
        this.originalInstrText = instrText;
        this.fieldAsTextStyling = fieldAsTextStyling;
        if ( instrTextChanged )
        {
            // compute new instr text only if it has changed.
            instrText = super.setInstrText( instrText, fieldAsTextStyling );
            super.append( instrText );
        }
        return instrText;
    }

    public String getOriginalInstrText()
    {
        return originalInstrText;
    }

    public boolean hasInstrText()
    {
        return originalInstrText != null;
    }

    public String getTContent()
    {
        return getTRegion(0).getTextContent();
    }

    public FieldMetadata getFieldAsTextStyling()
    {
        return fieldAsTextStyling;
    }

    public void setContainsNote( boolean containsNote )
    {
        this.containsNote = containsNote;
    }

    public boolean isContainsNote()
    {
        return containsNote;
    }
}
