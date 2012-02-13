package fr.opensagres.xdocreport.document.docx.textstyling;

import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;

public interface IDocxStylesGenerator
{

    public static final String generateAllStyles = "generateAllStyles";

    String generateAllStyles( DefaultStyle defaultStyle );

    String getHyperLinkStyleId( DefaultStyle defaultStyle );
    
    String getHeaderStyleId( int level, DefaultStyle defaultStyle );

}
