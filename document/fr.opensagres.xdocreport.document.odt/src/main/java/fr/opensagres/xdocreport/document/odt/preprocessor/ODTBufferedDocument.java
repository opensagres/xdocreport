package fr.opensagres.xdocreport.document.odt.preprocessor;

import fr.opensagres.xdocreport.document.odt.ODTUtils;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocument;

public class ODTBufferedDocument
    extends TransformedBufferedDocument
{

    @Override
    protected boolean isTable( String uri, String localName, String name )
    {
        return ODTUtils.isTable( uri, localName, name );
    }

    @Override
    protected boolean isTableRow( String uri, String localName, String name )
    {
        return ODTUtils.isTableRow( uri, localName, name );
    }
}
