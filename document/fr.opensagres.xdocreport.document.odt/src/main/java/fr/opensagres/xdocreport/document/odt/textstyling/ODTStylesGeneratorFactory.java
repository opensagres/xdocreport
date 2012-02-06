package fr.opensagres.xdocreport.document.odt.textstyling;

public class ODTStylesGeneratorFactory
{
    protected static IODTStylesGenerator generator;
    
    public static IODTStylesGenerator getStyleGenerator() {
        if (generator==null) {
            generator = new ODTDefaultStylesGenerator();
        }
        return generator;
    }
}
