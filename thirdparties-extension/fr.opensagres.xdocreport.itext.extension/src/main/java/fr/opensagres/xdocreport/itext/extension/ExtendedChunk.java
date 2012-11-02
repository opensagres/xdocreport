package fr.opensagres.xdocreport.itext.extension;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;

public class ExtendedChunk
    extends Chunk
{

    private final ExtendedDocument ownerDocument;

    private boolean pageNumberChunk;

    public ExtendedChunk( ExtendedDocument ownerDocument, String textContent )
    {
        this( ownerDocument, textContent, new Font() );
    }

    public ExtendedChunk( ExtendedDocument ownerDocument, String textContent, Font font )
    {
        super( textContent, font );
        this.ownerDocument = ownerDocument;
    }

    public ExtendedChunk( ExtendedDocument ownerDocument, boolean pageNumberChunk, Font font )
    {
        super( "", font );
        this.pageNumberChunk = pageNumberChunk;
        this.ownerDocument = ownerDocument;
    }

    public boolean isPageNumberChunk()
    {
        return pageNumberChunk;
    }

    public void setPageNumberChunk( boolean pageNumberChunk )
    {
        this.pageNumberChunk = pageNumberChunk;
    }

    @Override
    public String getContent()
    {
        return pageNumberChunk ? String.valueOf( ownerDocument.getPageNumber() ) : super.getContent();
    }
}
