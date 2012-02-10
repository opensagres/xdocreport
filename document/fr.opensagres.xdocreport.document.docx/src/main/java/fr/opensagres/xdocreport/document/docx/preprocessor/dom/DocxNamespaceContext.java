package fr.opensagres.xdocreport.document.docx.preprocessor.dom;

import javax.xml.namespace.NamespaceContext;

import fr.opensagres.xdocreport.core.utils.MapNamespaceContext;
import fr.opensagres.xdocreport.document.docx.DocxConstants;

public class DocxNamespaceContext
    extends MapNamespaceContext
{

    public static final NamespaceContext INSTANCE = new DocxNamespaceContext();

    public DocxNamespaceContext()
    {
        super.addNamespace( "w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main" );
    }
}
