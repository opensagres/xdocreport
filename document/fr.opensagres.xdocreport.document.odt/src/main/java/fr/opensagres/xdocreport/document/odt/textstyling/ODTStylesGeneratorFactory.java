package fr.opensagres.xdocreport.document.odt.textstyling;

public class ODTStylesGeneratorFactory
{
    protected static ODTStylesGenerator generator;
    
    public static ODTStylesGenerator getStyleGenerator() {
        if (generator==null) {
            generator = new ODTDefaultStylesGenerator();
        }
        return generator;
    }
}
