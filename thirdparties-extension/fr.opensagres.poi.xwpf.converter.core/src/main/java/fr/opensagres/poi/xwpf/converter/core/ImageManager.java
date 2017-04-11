package fr.opensagres.poi.xwpf.converter.core;

import java.io.File;
import java.io.IOException;

/**
 * Created by zzt on 17/4/11.
 */
public class ImageManager extends FileImageExtractor implements IURIResolver {

    private final String imageSubDir;

    public ImageManager(File baseDir, String imageSubDir) {
        super(baseDir);
        this.imageSubDir = imageSubDir;
    }

    @Override
    public void extract(String imagePath, byte[] imageData) throws IOException {
        super.extract(getImageRelativePath(imagePath), imageData);
    }

    /**
     * using customized image directory to replace fixed {@link XWPFDocumentVisitor#WORD_MEDIA}
     *
     * @param imagePath image name with path
     * @return the path relative the original {@link #baseDir}
     */
    private String getImageRelativePath(String imagePath) {
        return new File(imageSubDir, getFileName(imagePath)).toString();
    }

    private String getFileName(String imagePath) {
        return new File(imagePath).getName();
    }

    @Override
    public String resolve(String uri) {
        return getImageRelativePath(uri);
    }

}
