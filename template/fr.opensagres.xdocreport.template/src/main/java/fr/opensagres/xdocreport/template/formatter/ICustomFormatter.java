package fr.opensagres.xdocreport.template.formatter;

/**
 * Custom formatter field which is used to format input/mergefield while preprocessing step.
 * 
 * @author azerr
 */
public interface ICustomFormatter
{

    /**
     * returns the result of the format of teh given content coming from input/merge field.
     * 
     * @param content
     * @param formatter
     * @return
     */
    String format( String content, IDocumentFormatter formatter );

}
