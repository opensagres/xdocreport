package fr.opensagres.poi.xwpf.converter.xhtml;

import fr.opensagres.poi.xwpf.converter.core.ImageManager;
import fr.opensagres.xdocreport.core.utils.Base64Utility;

import java.io.File;
import java.io.IOException;

/**
 * Created by zzt on 17/4/11.
 */
public class Base64EmbedImgManager extends ImageManager {
    private static final String EMBED_IMG_SRC_PREFIX = XHTMLConstants.DATA_ATTR + ";base64,";

    private byte[] picture;

    public Base64EmbedImgManager() {
        super(new File(""), "");
    }

    @Override
    public void extract(String imagePath, byte[] imageData) throws IOException {
        this.picture = imageData;
    }

    @Override
    public String resolve(String uri) {
        StringBuilder sb = new StringBuilder(picture.length + EMBED_IMG_SRC_PREFIX.length())
                .append(EMBED_IMG_SRC_PREFIX)
                .append(Base64Utility.encode(picture));
        return sb.toString();
    }
}
