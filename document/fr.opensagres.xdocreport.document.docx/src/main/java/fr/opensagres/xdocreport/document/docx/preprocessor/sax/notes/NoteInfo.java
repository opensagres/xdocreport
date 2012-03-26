package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

public class NoteInfo
{
    
    public static final String CONTEXT_KEY = "___NoEscapeNoteInfo";
    
    public static final String ID_PROPERTY = "id";
    
    public static final String CONTENT_PROPERTY = "content";

    private final String id;

    private final String content;

    public NoteInfo( String id, String content )
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
