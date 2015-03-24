package fr.opensagres.xdocreport.document.images;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class ImageInfoTestCase {

    @Test
    public void testToRecognizedAJpegExifImageWithImageInfo() throws Exception {
        InputStream in = ImageInfoTestCase.class.getResourceAsStream("jpeg-exif.jpg");
        IImageInfo info = loadImageInfo(in);
        assertThat(info.getMimeType().getType(), is("jpeg"));
        assertThat(info.getHeight(), is(1536));
        assertThat(info.getWidth(), is(2048));
    }

    @Test
    public void testToRecognizedAJpegJFIFImageWithImageInfo() throws Exception {
        InputStream in = ImageInfoTestCase.class.getResourceAsStream("jpeg-jfif.jpg");
        IImageInfo info = loadImageInfo(in);
        assertThat(info.getMimeType().getType(), is("jpeg"));
        assertThat(info.getHeight(), is(1536));
        assertThat(info.getWidth(), is(2048));
    }

    private IImageInfo loadImageInfo(InputStream is) throws IOException {
        SimpleImageInfo imageInfo = new SimpleImageInfo();
        imageInfo.setInput(is);
        imageInfo.check();
        return imageInfo;
    }
}
