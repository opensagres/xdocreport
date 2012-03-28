package fr.opensagres.xdocreport.document.images;

import java.io.IOException;

import fr.opensagres.xdocreport.template.formatter.FieldMetadata;

public interface IImageHandler
{

    IImageProvider getImageProvider( Object imageProvider, String fieldName, FieldMetadata metadata )
        throws IOException;

}
