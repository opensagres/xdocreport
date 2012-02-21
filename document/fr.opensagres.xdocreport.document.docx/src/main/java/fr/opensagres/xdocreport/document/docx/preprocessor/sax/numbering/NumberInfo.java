package fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering;

public class NumberInfo
{

    private final int numId;

    private final int abstractNumId;

    public NumberInfo( int numId, int abstractNumId )
    {
        this.numId = numId;
        this.abstractNumId = abstractNumId;
    }

    public int getNumId()
    {
        return numId;
    }

    public int getAbstractNumId()
    {
        return abstractNumId;
    }
}
