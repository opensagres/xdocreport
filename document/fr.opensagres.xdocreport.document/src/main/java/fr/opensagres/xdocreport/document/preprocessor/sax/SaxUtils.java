package fr.opensagres.xdocreport.document.preprocessor.sax;

/**
 * <p>
 * User: Kevin Senechal <kevin@dooapp.com>
 * Date: 23/11/2015
 * Time: 17:28
 */
public class SaxUtils {

    public static final String FOREACH_INSTRUCTION = "#foreach(";
    public static final String IF_INSTRUCTION = "#if(";
    public static final String SET_INSTRUCTION = "#set(";

    public static boolean isInstruction(String tagContent){
        return tagContent.contains(FOREACH_INSTRUCTION) || tagContent.contains(IF_INSTRUCTION) || tagContent.contains(SET_INSTRUCTION);
    }
}
