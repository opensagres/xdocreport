package fr.opensagres.xdocreport.document.docx.textstyling;

import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.textstyling.IStylesGenerator;

public interface IDocxStylesGenerator
    extends IStylesGenerator<DefaultStyle>
{

    String generateAllStyles( DefaultStyle defaultStyle );

    String getHyperLinkStyleId( DefaultStyle defaultStyle );

    String getHeaderStyleId( int level, DefaultStyle defaultStyle );

}
