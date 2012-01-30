package fr.opensagres.xdocreport.document.tools;

import java.io.OutputStream;

public class Response
{

    private final OutputStream out;

    public Response( OutputStream out )
    {
        this.out = out;
    }

    public OutputStream getOut()
    {
        return out;
    }

}
