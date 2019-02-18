/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
