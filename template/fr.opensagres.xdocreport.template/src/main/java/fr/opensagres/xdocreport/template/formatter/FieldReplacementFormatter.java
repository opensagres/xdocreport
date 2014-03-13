package fr.opensagres.xdocreport.template.formatter;

import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.utils.StringUtils;

/**
 * Custom replacement of merge/input field which is done while preprocessing step.
 */
public class FieldReplacementFormatter
    implements ICustomFormatter
{

    private final List<String> searchList;

    private final List<String> replacementList;

    public FieldReplacementFormatter()
    {
        this.searchList = new ArrayList<String>();
        this.replacementList = new ArrayList<String>();
    }

    public String format( String content, IDocumentFormatter formatter )
    {
        return StringUtils.replaceEach( content, searchList.toArray( StringUtils.EMPTY_STRING_ARRAY ),
                                        replacementList.toArray( StringUtils.EMPTY_STRING_ARRAY ) );
    }

    public void addMapping( String search, String replacement )
    {
        searchList.add( search );
        replacementList.add( replacement );
    }

}
