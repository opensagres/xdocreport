package org.apache.poi.xwpf.converter.pdf.internal.elements;

import com.lowagie.text.Anchor;
import com.lowagie.text.Element;

import fr.opensagres.xdocreport.itext.extension.IITextContainer;

public class StylableAnchor
    extends Anchor
    implements IITextContainer
{

    private IITextContainer parent;

    public IITextContainer getITextContainer()
    {
        return parent;
    }

    public void setITextContainer( IITextContainer parent )
    {
        this.parent = parent;
    }

    public void addElement( Element element )
    {
        super.add( element );
    }

}
