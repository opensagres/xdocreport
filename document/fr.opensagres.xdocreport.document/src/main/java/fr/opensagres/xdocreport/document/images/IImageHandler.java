package fr.opensagres.xdocreport.document.images;

import java.io.IOException;

public interface IImageHandler
{

    IImageProvider getImageProvider( Object imageProvider, String fieldName ) throws IOException;

}
