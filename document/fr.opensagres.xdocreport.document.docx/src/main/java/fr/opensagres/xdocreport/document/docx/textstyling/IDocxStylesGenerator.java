package fr.opensagres.xdocreport.document.docx.textstyling;

import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.textstyling.IStylesGenerator;

public interface IDocxStylesGenerator
    extends IStylesGenerator<DefaultStyle>
{

    public static final String generateAbstractNumBullet = "generateAbstractNumBullet";
    
    public static final String generateAbstractNumDecimal = "generateAbstractNumDecimal";
    
    String generateAllStyles( DefaultStyle defaultStyle );

    String getHyperLinkStyleId( DefaultStyle defaultStyle );

    String getHeaderStyleId( int level, DefaultStyle defaultStyle );

    Integer getAbstractNumIdForList( boolean ordered, DefaultStyle defaultStyle );

    String generateAbstractNumBullet( DefaultStyle defaultStyle );

    String generateAbstractNumDecimal( DefaultStyle defaultStyle );

}
