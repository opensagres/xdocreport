package fr.opensagres.xdocreport.document.docx.textstyling;

public class DocxStylesGeneratorProvider
{
    protected static IDocxStylesGenerator generator;
    
    public static IDocxStylesGenerator getStyleGenerator() {
        if (generator==null) {
            generator = new DocxDefaultStylesGenerator();
        }
        return generator;
    }
}
