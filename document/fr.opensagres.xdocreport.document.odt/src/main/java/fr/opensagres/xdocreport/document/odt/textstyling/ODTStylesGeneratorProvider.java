package fr.opensagres.xdocreport.document.odt.textstyling;

public class ODTStylesGeneratorProvider
{
    protected static IODTStylesGenerator generator;
    
    public static IODTStylesGenerator getStyleGenerator() {
        if (generator==null) {
            generator = new ODTDefaultStylesGenerator();
        }
        return generator;
    }
}
