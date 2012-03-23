package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

public class FootnoteInfo
{

    private final String id;

    private final String content;

    public FootnoteInfo( String id, String content )
    {
        this.id = id;
        this.content = content;
    }

    public String getId()
    {
        return id;
    }

    public String getContent()
    {
        return content;
    }

}
