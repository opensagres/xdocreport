package fr.opensagres.xdocreport.document.textstyling;


public interface IStylesGenerator<DefaultStyle>
{

    public static final String generateAllStyles = "generateAllStyles";
    
    String generateAllStyles( DefaultStyle defaultStyle );

}
