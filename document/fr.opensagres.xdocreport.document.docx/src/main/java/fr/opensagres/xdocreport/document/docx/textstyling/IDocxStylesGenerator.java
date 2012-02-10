package fr.opensagres.xdocreport.document.docx.textstyling;

import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;

public interface IDocxStylesGenerator
{

    public static final String generateHyperlinkStyle = "generateHyperlinkStyle";

    String generateHyperlinkStyle( DefaultStyle defaultStyle );

    String getHyperLinkStyleId( DefaultStyle defaultStyle );

}
